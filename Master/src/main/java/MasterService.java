
import gridmr.JobRequest;
import gridmr.JobResponse;
import gridmr.MapReduceServiceGrpc;
import gridmr.TaskRequest;
import gridmr.TaskResponse;
import gridmr.TaskResult;
import gridmr.TaskResultResponse;
import gridmr.WorkerRegistrationRequest;
import gridmr.WorkerRegistrationResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.regions.Region;
import java.io.InputStream;

/**
 * This class is responsible for coordinating distributed MapReduce jobs. It
 * manages worker registration, job submission, task scheduling, and result
 * collection.
 *
 * * @author Yashua and Jose
 */
public class MasterService {

    private final int port;
    private Server server;
    private final ConcurrentHashMap<String, String> registeredWorkers = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<TaskResponse> mapTasks = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<TaskResponse> reduceTasks = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, TaskResponse> runningTasks = new ConcurrentHashMap<>();
    private final S3Client s3;
    private final String s3BucketName = "your-unique-gridmr-bucket-name";

    /**
     * Constructs a new MasterService.
     *
     * @param port The port on which the gRPC server will listen.
     */
    public MasterService(int port) {
        this.port = port;
        // Initializes the S3 client for the desired region.
        // Authentication is handled via EC2 instance credentials or AWS profile.
        this.s3 = S3Client.builder().region(Region.US_EAST_1).build();
    }

    /**
     * Starts the gRPC server, binding it to the specified port. Also sets up a
     * shutdown hook for graceful termination.
     *
     * @throws IOException If the server fails to bind to the port.
     */
    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new MasterServiceImpl())
                .build()
                .start();
        System.out.println("Master started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server gracefully.");
            if (server != null) {
                server.shutdown();
            }
        }));
    }

    /**
     * Waits for the gRPC server to end. Keeps the main thread alive.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Inner class that handles all gRPC calls from clients and workers.
     */
    private class MasterServiceImpl extends MapReduceServiceGrpc.MapReduceServiceImplBase {

        /**
         * Divides the input data into blocks
         *
         * @param request The JobRequest containing job details.
         * @param responseObserver A stream observer to send back the
         * JobResponse.
         */
        @Override
        public void submitJob(JobRequest request, StreamObserver<JobResponse> responseObserver) {
            System.out.println("Received new job: " + request.getJobId());
            String jobToken = UUID.randomUUID().toString();
            String inputS3KeyPrefix = "jobs/" + jobToken + "/input/";

            // Reemplaza el código de simulación por el nuevo método para subir archivos en bloques
            try {
                int numberOfBlocks = uploadFileInBlocksToS3(request.getInputDataPath(), inputS3KeyPrefix);

                for (int i = 0; i < numberOfBlocks; i++) {
                    String inputS3Key = inputS3KeyPrefix + "block_" + i + ".bin";
                    String taskId = UUID.randomUUID().toString();
                    TaskResponse mapTask = TaskResponse.newBuilder()
                            .setTaskId(taskId)
                            .setTaskType(TaskResponse.TaskType.MAP_TASK)
                            .setDataSplitPath(inputS3Key)
                            .setJobId(request.getJobId())
                            .build();
                    mapTasks.add(mapTask);
                }

                JobResponse response = JobResponse.newBuilder().setSuccess(true).setMessage("Job submitted").build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

            } catch (IOException e) {
                System.err.println("Failed to read local file: " + e.getMessage());
                responseObserver.onError(io.grpc.Status.INTERNAL.withDescription("Failed to read local file").asRuntimeException());
            } catch (Exception e) {
                System.err.println("Failed to upload data to S3: " + e.getMessage());
                responseObserver.onError(io.grpc.Status.INTERNAL.withDescription("Failed to upload data to S3").asRuntimeException());
            }
        }

        /**
         * This registers a new worker with the master. The worker's ID and
         * network address are stored for task assignment.
         *
         * @param request The WorkerRegistrationRequest containing worker
         * details.
         * @param responseObserver A stream observer to send back the
         * registration response.
         */
        @Override
        public void registerWorker(WorkerRegistrationRequest request, StreamObserver<WorkerRegistrationResponse> responseObserver) {
            registeredWorkers.put(request.getWorkerId(), request.getAddress());
            System.out.println("Worker registered: " + request.getWorkerId() + " at " + request.getAddress());
            WorkerRegistrationResponse response = WorkerRegistrationResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        /**
         * Assigns a new task (either Map or Reduce) to a worker. The master
         * checks its queues for available tasks and assigns them based on a
         * first-come, first-served basis.
         *
         * @param request The TaskRequest from the worker.
         * @param responseObserver A stream observer to send back the assigned
         * task.
         */
        @Override
        public void getTask(TaskRequest request, StreamObserver<TaskResponse> responseObserver) {
            TaskResponse task = mapTasks.poll();
            if (task != null) {
                runningTasks.put(task.getTaskId(), task);
                responseObserver.onNext(task);
            } else {
                task = reduceTasks.poll();
                if (task != null) {
                    runningTasks.put(task.getTaskId(), task);
                    responseObserver.onNext(task);
                } else {
                    responseObserver.onNext(TaskResponse.newBuilder().setTaskType(TaskResponse.TaskType.NO_TASK).build());
                }
            }
            responseObserver.onCompleted();
        }

        /**
         * Receives the result of a completed task from a worker.
         *
         * @param request The task's outcome.
         * @param responseObserver An Observer to confirm task completion.
         */
        @Override
        public void submitTaskResult(TaskResult request, StreamObserver<TaskResultResponse> responseObserver) {
            System.out.println("Task completed: " + request.getTaskId());
            runningTasks.remove(request.getTaskId());
            // Logic to manage intermediate and final results in S3
            TaskResultResponse response = TaskResultResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    /**
     * Reads a local file in fixed-size blocks and uploads each block to an S3
     * bucket.
     *
     * @param localFilePath The local path of the file to be processed.
     * @param inputS3KeyPrefix The S3 key prefix for the uploaded blocks.
     * @return The number of blocks uploaded to S3.
     * @throws IOException If the file cannot be read.
     */
    private int uploadFileInBlocksToS3(String localFilePath, String inputS3KeyPrefix) throws IOException {
        File file = new File(localFilePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + localFilePath);
        }

        long partSize = 64 * 1024 * 1024; // 64 MB
        int partNumber = 0;

        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) partSize];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String s3Key = inputS3KeyPrefix + "block_" + partNumber + ".bin";

                byte[] finalBuffer = new byte[bytesRead];
                System.arraycopy(buffer, 0, finalBuffer, 0, bytesRead);

                s3.putObject(PutObjectRequest.builder().bucket(s3BucketName).key(s3Key).build(), RequestBody.fromBytes(finalBuffer));

                System.out.println("Uploaded part " + partNumber + " with " + bytesRead + " bytes to " + s3Key);
                partNumber++;
            }
        }

        return partNumber;
    }
}
