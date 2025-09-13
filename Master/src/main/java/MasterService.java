import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.ByteString;
import gridmr.DataTransferServiceGrpc;
import gridmr.FileChunk;
import gridmr.JobRequest;
import gridmr.JobResponse;
import gridmr.MapReduceServiceGrpc;
import gridmr.ReceiveFileRequest;
import gridmr.TaskRequest;
import gridmr.TaskResponse;
import gridmr.TaskResult;
import gridmr.TaskResultResponse;
import gridmr.WorkerRegistrationRequest;
import gridmr.WorkerRegistrationResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MasterService {

    private final int port;
    private Server server;
    private final ConcurrentHashMap<String, String> registeredWorkers = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<TaskResponse> mapTasks = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<TaskResponse> reduceTasks = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, TaskResponse> runningTasks = new ConcurrentHashMap<>();

    public MasterService(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new MasterServiceImpl())
                .addService(new DataTransferServiceImpl())
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

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }


    private class MasterServiceImpl extends MapReduceServiceGrpc.MapReduceServiceImplBase {
        @Override
        public void submitJob(JobRequest request, StreamObserver<JobResponse> responseObserver) {
            System.out.println("Received new job: " + request.getJobId());
            String jobToken = UUID.randomUUID().toString();
            for (int i = 0; i < 5; i++) {
                String taskId = UUID.randomUUID().toString();
                String inputFilePath = request.getInputDataPath() + "/block_" + i + ".txt";
                TaskResponse mapTask = TaskResponse.newBuilder()
                        .setTaskId(taskId)
                        .setTaskType(TaskResponse.TaskType.MAP_TASK)
                        .setDataSplitPath(inputFilePath)
                        .setJobId(request.getJobId())
                        .build();
                mapTasks.add(mapTask);
            }
            JobResponse response = JobResponse.newBuilder().setSuccess(true).setMessage("Job submitted").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void registerWorker(WorkerRegistrationRequest request, StreamObserver<WorkerRegistrationResponse> responseObserver) {
            registeredWorkers.put(request.getWorkerId(), request.getAddress());
            System.out.println("Worker registered: " + request.getWorkerId() + " at " + request.getAddress());
            WorkerRegistrationResponse response = WorkerRegistrationResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

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

        @Override
        public void submitTaskResult(TaskResult request, StreamObserver<TaskResultResponse> responseObserver) {
            System.out.println("Task completed: " + request.getTaskId());
            runningTasks.remove(request.getTaskId());
            TaskResultResponse response = TaskResultResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    private static class DataTransferServiceImpl extends MapReduceServiceGrpc.MapReduceServiceImplBase {
        public void receiveFile(ReceiveFileRequest request, StreamObserver<FileChunk> responseObserver) {
            System.out.println("Master receiving request for file: " + request.getFilePath());
            File file = new File(request.getFilePath());
            if (!file.exists()) {
                responseObserver.onError(io.grpc.Status.NOT_FOUND.withDescription("File not found").asRuntimeException());
                return;
            }
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    responseObserver.onNext(FileChunk.newBuilder().setData(ByteString.copyFrom(buffer, 0, bytesRead)).build());
                }
                responseObserver.onCompleted();
            } catch (IOException e) {
                responseObserver.onError(io.grpc.Status.INTERNAL.withDescription("File read error").asRuntimeException());
            }
        }
    }
}