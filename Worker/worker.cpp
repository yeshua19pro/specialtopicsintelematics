#include <iostream>
#include <string>
#include <fstream>
#include <thread>
#include <chrono>
#include <sstream>
#include <map>
#include <cctype>

// gRPC includes
#include <grpcpp/grpcpp.h>
#include "gridmr.grpc.pb.h"

// AWS SDK includes for S3
#include <aws/core/Aws.h>
#include <aws/s3/S3Client.h>
#include <aws/s3/model/GetObjectRequest.h>
#include <aws/s3/model/PutObjectRequest.h>
#include <aws/core/utils/memory/stl/AWSStreamFwd.h>

using namespace gridmr;
using grpc::Channel;
using grpc::ClientContext;
using grpc::Server;
using grpc::ServerBuilder;
using grpc::ServerContext;
using grpc::ServerReader;
using grpc::ServerWriter;
using grpc::Status;

/**
 * @class WorkerClient
 * @brief Represents a worker node in the GridMR system.
 *
 * This class is responsible for communicating with the Master,
 */
class WorkerClient
{
public:
    /**
     * @brief Constructs a WorkerClient instance.
     * @param channel A shared pointer to the gRPC channel for communication with the master.
     * @param s3BucketName The name of the S3 bucket to use for data storage.
     */
    WorkerClient(std::shared_ptr<Channel> channel, const std::string &s3BucketName);

    /**
     * @brief Registers the worker with the master node.
     * @param workerId A unique ID for the worker.
     * @param address The network address (IP:port) of the worker.
     */
    void registerWorker(const std::string &workerId, const std::string &address);

    /**
     * @brief Starts the main loop of the worker.
     * The worker continuously polls the master for new tasks.
     */
    void run();

private:
    /**
     * @brief Handles and executes a task assigned by the master.
     * @param task The TaskResponse object containing the task details.
     */
    void handleTask(const TaskResponse &task);

    /**
     * @brief Downloads a file from the S3 bucket.
     * @param key The S3 object key (path).
     * @param destinationPath The local file path to save the downloaded file.
     */
    void downloadFileFromS3(const std::string &key, const std::string &destinationPath);

    /**
     * @brief Uploads a file from the local disk to the S3 bucket.
     * @param sourcePath The local file path to the file to be uploaded.
     * @param key The S3 object key (path) to save the file as.
     */
    void uploadFileToS3(const std::string &sourcePath, const std::string &key);

    /**
     * @brief Submits the result of a completed task to the master.
     * @param taskId The ID of the task that was completed.
     * @param workerId The ID of the worker.
     * @param success A boolean indicating if the task was successful.
     * @param resultPath The S3 key of the final output file.
     */
    void submitTaskResult(const std::string &taskId, const std::string &workerId, bool success, const std::string &resultPath);

    std::unique_ptr<MapReduceService::Stub> master_stub_;
    Aws::S3::S3Client s3_client_;
    std::string s3BucketName_;
};

/**
 * @brief Constructs a WorkerClient instance.
 * @param channel A shared pointer to the gRPC channel for communication with the master.
 * @param s3BucketName The name of the S3 bucket to use for data storage.
 */
WorkerClient::WorkerClient(std::shared_ptr<Channel> channel, const std::string &s3BucketName)
    : master_stub_(MapReduceService::NewStub(channel)), s3_client_(), s3BucketName_(s3BucketName)
{
}

/**
 * @brief Registers the worker with the master node.
 * @param workerId A unique ID for the worker.
 * @param address The network address (IP:port) of the worker.
 */
void WorkerClient::registerWorker(const std::string &workerId, const std::string &address)
{
    WorkerRegistrationRequest request;
    request.set_workerid(workerId);
    request.set_address(address);
    WorkerRegistrationResponse response;
    ClientContext context;
    Status status = master_stub_->RegisterWorker(&context, request, &response);
    if (status.ok())
    {
        std::cout << "Worker registered successfully." << std::endl;
    }
    else
    {
        std::cerr << "Registration failed: " << status.error_message() << std::endl;
    }
}

/**
 * @brief Starts the main loop of the worker.
 * The worker continuously polls the master for new tasks.
 */
void WorkerClient::run()
{
    while (true)
    {
        TaskRequest request;
        request.set_workerid("worker-1");
        TaskResponse response;
        ClientContext context;
        Status status = master_stub_->GetTask(&context, request, &response);

        if (status.ok() && response.task_type() != TaskResponse::NO_TASK)
        {
            std::cout << "Received task: " << response.task_id() << std::endl;
            handleTask(response);
        }
        else
        {
            std::cout << "No task available. Sleeping..." << std::endl;
        }
        std::this_thread::sleep_for(std::chrono::seconds(5));
    }
}

/**
 * @brief Handles and executes a task assigned by the master.
 *
 * @param task The TaskResponse object containing the task details.
 */
void WorkerClient::handleTask(const TaskResponse &task)
{
    if (task.task_type() == TaskResponse::MAP_TASK)
    {
        std::cout << "Handling Map task. Retrieving file from S3: " << task.data_split_path() << std::endl;
        std::string localPath = "/worker_data/input_" + task.task_id() + ".bin";
        downloadFileFromS3(task.data_split_path(), localPath);

        std::string outputKey = "jobs/" + task.job_id() + "/intermediate/" + task.task_id() + ".out";
        std::string outputPath = "/worker_data/output_" + task.task_id() + ".out";

        std::ifstream inputFile(localPath);
        std::ofstream outputFile(outputPath);
        std::string line;

        while (std::getline(inputFile, line))
        {
            std::stringstream ss(line);
            std::string word;
            while (ss >> word)
            {
                std::string cleanedWord;
                for (char c : word)
                {
                    if (std::isalpha(c))
                    {
                        cleanedWord += std::tolower(c);
                    }
                }
                if (!cleanedWord.empty())
                {
                    outputFile << cleanedWord << "\t1\n";
                }
            }
        }
        inputFile.close();
        outputFile.close();

        uploadFileToS3(outputPath, outputKey);
        submitTaskResult(task.task_id(), "worker-1", true, outputKey);
    }
    else if (task.task_type() == TaskResponse::REDUCE_TASK)
    {
        std::cout << "Handling Reduce task." << std::endl;

        std::map<std::string, int> wordCounts;

        for (const auto &intermediateFileKey : task.intermediate_files())
        {
            std::string localInterimPath = "/worker_data/interim_" + task.task_id() + ".bin";
            downloadFileFromS3(intermediateFileKey, localInterimPath);

            std::ifstream interimFile(localInterimPath);
            std::string line;
            while (std::getline(interimFile, line))
            {
                std::stringstream ss(line);
                std::string word;
                int count;
                ss >> word >> count;
                wordCounts[word] += count;
            }
            interimFile.close();
        }

        std::string finalPath = "/worker_data/final_result_" + task.task_id() + ".out";
        std::ofstream finalOutputFile(finalPath);
        for (const auto &pair : wordCounts)
        {
            finalOutputFile << pair.first << "\t" << pair.second << "\n";
        }
        finalOutputFile.close();

        std::string finalResultKey = "jobs/" + task.job_id() + "/final/result_" + task.task_id() + ".out";
        uploadFileToS3(finalPath, finalResultKey);

        submitTaskResult(task.task_id(), "worker-1", true, finalResultKey);
    }
}

/**
 * @brief Downloads a file from the S3 bucket.
 * @param key The S3 object key (path).
 * @param destinationPath The local file path to save the downloaded file.
 */
void WorkerClient::downloadFileFromS3(const std::string &key, const std::string &destinationPath)
{
    Aws::S3::Model::GetObjectRequest getObjectRequest;
    getObjectRequest.SetBucket(s3BucketName_.c_str());
    getObjectRequest.SetKey(key.c_str());

    auto getObjectOutcome = s3_client_.GetObject(getObjectRequest);

    if (getObjectOutcome.IsSuccess())
    {
        std::cout << "Successfully retrieved " << key << " from S3." << std::endl;
        std::ofstream localFile(destinationPath, std::ios::binary);
        localFile << getObjectOutcome.GetResult().GetBody().rdbuf();
        localFile.close();
    }
    else
    {
        std::cerr << "Error: GetObject failed with error: " << getObjectOutcome.GetError().GetMessage() << std::endl;
    }
}

/**
 * @brief Uploads a file from the local disk to the S3 bucket.
 * @param sourcePath The local file path to the file to be uploaded.
 * @param key The S3 object key (path) to save the file as.
 */
void WorkerClient::uploadFileToS3(const std::string &sourcePath, const std::string &key)
{
    Aws::S3::Model::PutObjectRequest putObjectRequest;
    putObjectRequest.SetBucket(s3BucketName_.c_str());
    putObjectRequest.SetKey(key.c_str());

    const std::shared_ptr<Aws::IOStream> inputData = Aws::MakeShared<Aws::FStream>("SampleAllocationTag", sourcePath.c_str(), std::ios_base::in | std::ios_base::binary);
    putObjectRequest.SetBody(inputData);

    auto putObjectOutcome = s3_client_.PutObject(putObjectRequest);

    if (putObjectOutcome.IsSuccess())
    {
        std::cout << "Successfully uploaded " << sourcePath << " to " << key << " in S3." << std::endl;
    }
    else
    {
        std::cerr << "Error: PutObject failed with error: " << putObjectOutcome.GetError().GetMessage() << std::endl;
    }
}

/**
 * @brief Submits the result of a completed task to the master.
 * @param taskId The ID of the task that was completed.
 * @param workerId The ID of the worker.
 * @param success A boolean indicating if the task was successful.
 * @param resultPath The S3 key of the final output file.
 */
void WorkerClient::submitTaskResult(const std::string &taskId, const std::string &workerId, bool success, const std::string &resultPath)
{
    TaskResult result;
    result.set_taskid(taskId);
    result.set_workerid(workerId);
    result.set_success(success);
    result.set_resultpath(resultPath);
    TaskResultResponse response;
    ClientContext context;
    master_stub_->SubmitTaskResult(&context, result, &response);
}

/**
 * @brief The main function of the worker program.
 * Initializes the AWS SDK and gRPC client, then starts the worker's main loop.
 * @param argc The number of command-line arguments.
 * @param argv An array of command-line arguments.
 * @return 0 on success, 1 on error.
 */
int main(int argc, char **argv)
{
    if (argc < 3)
    {
        std::cerr << "Usage: " << argv[0] << " <master_address> <worker_address>" << std::endl;
        return 1;
    }

    Aws::SDKOptions options;
    Aws::InitAPI(options);

    std::string master_address = argv[1];
    std::string worker_address = argv[2];
    std::string s3BucketName = "your-unique-gridmr-bucket-name";

    WorkerClient worker(grpc::CreateChannel(master_address, grpc::InsecureChannelCredentials()), s3BucketName);
    worker.registerWorker("worker-1", worker_address);

    worker.run();

    Aws::ShutdownAPI(options);

    return 0;
}