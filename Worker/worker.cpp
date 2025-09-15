#include <iostream>
#include <fstream>
#include <sstream>
#include <map>
#include <cctype>
#include <thread>
#include <chrono>
#include <grpcpp/grpcpp.h>
#include "gridmr.grpc.pb.h"

using namespace gridmr;
using grpc::Channel;
using grpc::ClientContext;
using grpc::Status;

class WorkerClient {
public:
    WorkerClient(std::shared_ptr<Channel> channel)
        : master_stub_(MapReduceService::NewStub(channel)) {}

    void registerWorker(const std::string &workerId, const std::string &address) {
        WorkerRegistrationRequest request;
        request.set_workerid(workerId);
        request.set_address(address);
        WorkerRegistrationResponse response;
        ClientContext context;
        Status status = master_stub_->RegisterWorker(&context, request, &response);
        if (status.ok()) std::cout << "Worker registered successfully.\n";
        else std::cerr << "Registration failed: " << status.error_message() << "\n";
    }

    void run() {
        while (true) {
            TaskRequest request;
            request.set_workerid("worker-1");
            TaskResponse response;
            ClientContext context;
            Status status = master_stub_->GetTask(&context, request, &response);

            if (status.ok() && response.tasktype() != TaskResponse::NO_TASK) {
                std::cout << "Received task: " << response.taskid() << "\n";
                handleTask(response);
            } else {
                std::cout << "No task available. Sleeping...\n";
            }
            std::this_thread::sleep_for(std::chrono::seconds(5));
        }
    }

private:
    std::unique_ptr<MapReduceService::Stub> master_stub_;

    void handleTask(const TaskResponse &task) {
        if (task.tasktype() == TaskResponse::MAP_TASK) {
            std::string inputPath = task.datasplitpath(); // local path
            std::string outputPath = "worker_data/output_" + task.taskid() + ".out";

            std::ifstream inputFile(inputPath);
            std::ofstream outputFile(outputPath);
            std::string line;

            while (std::getline(inputFile, line)) {
                std::stringstream ss(line);
                std::string word;
                while (ss >> word) {
                    std::string cleanedWord;
                    for (char c : word) if (std::isalpha(c)) cleanedWord += std::tolower(c);
                    if (!cleanedWord.empty()) outputFile << cleanedWord << "\t1\n";
                }
            }

            inputFile.close();
            outputFile.close();
            submitTaskResult(task.taskid(), "worker-1", true, outputPath);

        } else if (task.tasktype() == TaskResponse::REDUCE_TASK) {
            std::map<std::string, int> wordCounts;
            for (const auto &filePath : task.intermediatefiles()) {
                std::ifstream in(filePath);
                std::string line;
                while (std::getline(in, line)) {
                    std::stringstream ss(line);
                    std::string word; int count;
                    ss >> word >> count;
                    wordCounts[word] += count;
                }
            }
            std::string finalPath = "worker_data/final_result_" + task.taskid() + ".out";
            std::ofstream out(finalPath);
            for (auto &p : wordCounts) out << p.first << "\t" << p.second << "\n";
            submitTaskResult(task.taskid(), "worker-1", true, finalPath);
        }
    }

    void submitTaskResult(const std::string &taskId, const std::string &workerId, bool success, const std::string &resultPath) {
        TaskResult result;
        result.set_taskid(taskId);
        result.set_workerid(workerId);
        result.set_success(success);
        result.set_resultpath(resultPath);
        TaskResultResponse response;
        ClientContext context;
        master_stub_->SubmitTaskResult(&context, result, &response);
    }
};

int main(int argc, char **argv) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " <master_address> <worker_address>\n";
        return 1;
    }

    std::string master_address = argv[1];
    std::string worker_address = argv[2];

    WorkerClient worker(grpc::CreateChannel(master_address, grpc::InsecureChannelCredentials()));
    worker.registerWorker("worker-1", worker_address);
    worker.run();

    return 0;
}
