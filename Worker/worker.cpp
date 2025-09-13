// worker/src/main.cpp
#include <iostream>
#include <string>
#include <fstream>
#include <grpcpp/grpcpp.h>
#include "gridmr.grpc.pb.h"

using namespace gridmr;
using grpc::Channel;
using grpc::ClientContext;
using grpc::Status;
using grpc::Server;
using grpc::ServerBuilder;
using grpc::ServerContext;
using grpc::ServerReader;
using grpc::ServerWriter;

class WorkerServiceImpl final : public DataTransferService::Service {
public:
    Status SendFile(ServerContext* context, ServerReader<FileChunk>* reader, SendFileResponse* response) override {
        std::string filePath = "/worker_data/output.txt"; //Path where the file will be saved
        std::ofstream file(filePath, std::ios::binary);
        if (!file.is_open()) {
            return Status(grpc::StatusCode::INTERNAL, "Could not open file for writing.");
        }
        FileChunk chunk;
        while (reader->Read(&chunk)) {
            file.write(chunk.data().c_str(), chunk.data().length());
        }
        file.close();
        response->set_success(true);
        return Status::OK;
    }
};

class WorkerClient {
public:
    WorkerClient(std::shared_ptr<Channel> channel) : master_stub_(MapReduceService::NewStub(channel)) {}

    void registerWorker(const std::string& workerId, const std::string& address) {
        WorkerRegistrationRequest request;
        request.set_workerid(workerId);
        request.set_address(address);
        WorkerRegistrationResponse response;
        ClientContext context;
        Status status = master_stub_->RegisterWorker(&context, request, &response);
        if (status.ok()) {
            std::cout << "Worker registered successfully." << std::endl;
        } else {
            std::cerr << "Registration failed: " << status.error_message() << std::endl;
        }
    }

    void run() {
        while (true) {
            TaskRequest request;
            request.set_workerid("worker-1"); 
            TaskResponse response;
            ClientContext context;
            Status status = master_stub_->GetTask(&context, request, &response);

            if (status.ok() && response.task_type() != TaskResponse::NO_TASK) {
                std::cout << "Received task: " << response.task_id() << std::endl;
                handleTask(response);
            } else {
                std::cout << "No task available. Sleeping..." << std::endl;
            }
            std::this_thread::sleep_for(std::chrono::seconds(5));
        }
    }

private:
    void handleTask(const TaskResponse& task) {
        if (task.task_type() == TaskResponse::MAP_TASK) {
            std::cout << "Handling Map task. Retrieving file: " << task.data_split_path() << std::endl;
            receiveFileFromMaster(task.data_split_path());
            // Logic for executing the Map function (simulated)
            submitTaskResult(task.task_id(), "worker-1", true, "/worker_data/output_map_" + task.task_id() + ".txt");
        } else if (task.task_type() == TaskResponse::REDUCE_TASK) {
            std::cout << "Handling Reduce task." << std::endl;
            // Logic for middle files form other workers (Simulated)
            // Logic for executing the Reduce function
            std::string finalResultPath = "final_result_" + task.task_id() + ".txt";
            sendFileToMaster(finalResultPath);
            submitTaskResult(task.task_id(), "worker-1", true, finalResultPath);
        }
    }

    void receiveFileFromMaster(const std::string& filePath) {
        std::shared_ptr<Channel> channel = grpc::CreateChannel("master:50051", grpc::InsecureChannelCredentials());
        std::unique_ptr<DataTransferService::Stub> stub = DataTransferService::NewStub(channel);
        ClientContext context;
        ReceiveFileRequest request;
        request.set_filepath(filePath);
        std::unique_ptr<ClientReader<FileChunk>> reader(stub->ReceiveFile(&context, request));

        std::ofstream outputFile("/worker_data/" + filePath, std::ios::binary);
        FileChunk chunk;
        while (reader->Read(&chunk)) {
            outputFile.write(chunk.data().c_str(), chunk.data().length());
        }
        outputFile.close();
        Status status = reader->Finish();
        if (status.ok()) {
            std::cout << "File received successfully." << std::endl;
        } else {
            std::cerr << "File receive failed: " << status.error_message() << std::endl;
        }
    }

    void sendFileToMaster(const std::string& filePath) {
        std::shared_ptr<Channel> channel = grpc::CreateChannel("master:50051", grpc::InsecureChannelCredentials());
        std::unique_ptr<DataTransferService::Stub> stub = DataTransferService::NewStub(channel);
        ClientContext context;
        SendFileResponse response;
        std::unique_ptr<ClientWriter<FileChunk>> writer(stub->SendFile(&context, &response));

        std::ifstream file(filePath, std::ios::binary);
        if (!file.is_open()) {
            std::cerr << "Failed to open file for sending." << std::endl;
            return;
        }
        char buffer[1024];
        while (!file.eof()) {
            file.read(buffer, sizeof(buffer));
            FileChunk chunk;
            chunk.set_data(buffer, file.gcount());
            writer->Write(chunk);
        }
        file.close();
        writer->WritesDone();
        Status status = writer->Finish();
        if (status.ok()) {
            std::cout << "File sent successfully." << std::endl;
        } else {
            std::cerr << "File send failed: " << status.error_message() << std::endl;
        }
    }

    void submitTaskResult(const std::string& taskId, const std::string& workerId, bool success, const std::string& resultPath) {
        TaskResult result;
        result.set_taskid(taskId);
        result.set_workerid(workerId);
        result.set_success(success);
        result.set_resultpath(resultPath);
        TaskResultResponse response;
        ClientContext context;
        master_stub_->SubmitTaskResult(&context, result, &response);
    }
    std::unique_ptr<MapReduceService::Stub> master_stub_;
};

void runWorkerServer(const std::string& workerAddress) {
    WorkerServiceImpl service;
    ServerBuilder builder;
    builder.AddListeningPort(workerAddress, grpc::InsecureServerCredentials());
    builder.RegisterService(&service);
    std::unique_ptr<Server> server(builder.BuildAndStart());
    std::cout << "Worker server listening on " << workerAddress << std::endl;
    server->Wait();
}

int main(int argc, char** argv) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " <master_address> <worker_address>" << std::endl;
        return 1;
    }
    std::string master_address = argv[1];
    std::string worker_address = argv[2];

    WorkerClient worker(grpc::CreateChannel(master_address, grpc::InsecureChannelCredentials()));
    worker.registerWorker("worker-1", worker_address); 
    
    std::thread serverThread(runWorkerServer, worker_address);
    worker.run();
    serverThread.join();
    return 0;
}