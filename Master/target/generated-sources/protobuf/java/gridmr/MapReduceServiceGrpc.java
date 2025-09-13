package gridmr;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: gridmr.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MapReduceServiceGrpc {

  private MapReduceServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "gridmr.MapReduceService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<gridmr.JobRequest,
      gridmr.JobResponse> getSubmitJobMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubmitJob",
      requestType = gridmr.JobRequest.class,
      responseType = gridmr.JobResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gridmr.JobRequest,
      gridmr.JobResponse> getSubmitJobMethod() {
    io.grpc.MethodDescriptor<gridmr.JobRequest, gridmr.JobResponse> getSubmitJobMethod;
    if ((getSubmitJobMethod = MapReduceServiceGrpc.getSubmitJobMethod) == null) {
      synchronized (MapReduceServiceGrpc.class) {
        if ((getSubmitJobMethod = MapReduceServiceGrpc.getSubmitJobMethod) == null) {
          MapReduceServiceGrpc.getSubmitJobMethod = getSubmitJobMethod =
              io.grpc.MethodDescriptor.<gridmr.JobRequest, gridmr.JobResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SubmitJob"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.JobRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.JobResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MapReduceServiceMethodDescriptorSupplier("SubmitJob"))
              .build();
        }
      }
    }
    return getSubmitJobMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gridmr.WorkerRegistrationRequest,
      gridmr.WorkerRegistrationResponse> getRegisterWorkerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterWorker",
      requestType = gridmr.WorkerRegistrationRequest.class,
      responseType = gridmr.WorkerRegistrationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gridmr.WorkerRegistrationRequest,
      gridmr.WorkerRegistrationResponse> getRegisterWorkerMethod() {
    io.grpc.MethodDescriptor<gridmr.WorkerRegistrationRequest, gridmr.WorkerRegistrationResponse> getRegisterWorkerMethod;
    if ((getRegisterWorkerMethod = MapReduceServiceGrpc.getRegisterWorkerMethod) == null) {
      synchronized (MapReduceServiceGrpc.class) {
        if ((getRegisterWorkerMethod = MapReduceServiceGrpc.getRegisterWorkerMethod) == null) {
          MapReduceServiceGrpc.getRegisterWorkerMethod = getRegisterWorkerMethod =
              io.grpc.MethodDescriptor.<gridmr.WorkerRegistrationRequest, gridmr.WorkerRegistrationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterWorker"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.WorkerRegistrationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.WorkerRegistrationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MapReduceServiceMethodDescriptorSupplier("RegisterWorker"))
              .build();
        }
      }
    }
    return getRegisterWorkerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gridmr.TaskRequest,
      gridmr.TaskResponse> getGetTaskMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTask",
      requestType = gridmr.TaskRequest.class,
      responseType = gridmr.TaskResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gridmr.TaskRequest,
      gridmr.TaskResponse> getGetTaskMethod() {
    io.grpc.MethodDescriptor<gridmr.TaskRequest, gridmr.TaskResponse> getGetTaskMethod;
    if ((getGetTaskMethod = MapReduceServiceGrpc.getGetTaskMethod) == null) {
      synchronized (MapReduceServiceGrpc.class) {
        if ((getGetTaskMethod = MapReduceServiceGrpc.getGetTaskMethod) == null) {
          MapReduceServiceGrpc.getGetTaskMethod = getGetTaskMethod =
              io.grpc.MethodDescriptor.<gridmr.TaskRequest, gridmr.TaskResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTask"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.TaskRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.TaskResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MapReduceServiceMethodDescriptorSupplier("GetTask"))
              .build();
        }
      }
    }
    return getGetTaskMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gridmr.TaskResult,
      gridmr.TaskResultResponse> getSubmitTaskResultMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubmitTaskResult",
      requestType = gridmr.TaskResult.class,
      responseType = gridmr.TaskResultResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gridmr.TaskResult,
      gridmr.TaskResultResponse> getSubmitTaskResultMethod() {
    io.grpc.MethodDescriptor<gridmr.TaskResult, gridmr.TaskResultResponse> getSubmitTaskResultMethod;
    if ((getSubmitTaskResultMethod = MapReduceServiceGrpc.getSubmitTaskResultMethod) == null) {
      synchronized (MapReduceServiceGrpc.class) {
        if ((getSubmitTaskResultMethod = MapReduceServiceGrpc.getSubmitTaskResultMethod) == null) {
          MapReduceServiceGrpc.getSubmitTaskResultMethod = getSubmitTaskResultMethod =
              io.grpc.MethodDescriptor.<gridmr.TaskResult, gridmr.TaskResultResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SubmitTaskResult"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.TaskResult.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.TaskResultResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MapReduceServiceMethodDescriptorSupplier("SubmitTaskResult"))
              .build();
        }
      }
    }
    return getSubmitTaskResultMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MapReduceServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MapReduceServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MapReduceServiceStub>() {
        @java.lang.Override
        public MapReduceServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MapReduceServiceStub(channel, callOptions);
        }
      };
    return MapReduceServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MapReduceServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MapReduceServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MapReduceServiceBlockingStub>() {
        @java.lang.Override
        public MapReduceServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MapReduceServiceBlockingStub(channel, callOptions);
        }
      };
    return MapReduceServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MapReduceServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MapReduceServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MapReduceServiceFutureStub>() {
        @java.lang.Override
        public MapReduceServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MapReduceServiceFutureStub(channel, callOptions);
        }
      };
    return MapReduceServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void submitJob(gridmr.JobRequest request,
        io.grpc.stub.StreamObserver<gridmr.JobResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubmitJobMethod(), responseObserver);
    }

    /**
     */
    default void registerWorker(gridmr.WorkerRegistrationRequest request,
        io.grpc.stub.StreamObserver<gridmr.WorkerRegistrationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterWorkerMethod(), responseObserver);
    }

    /**
     */
    default void getTask(gridmr.TaskRequest request,
        io.grpc.stub.StreamObserver<gridmr.TaskResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTaskMethod(), responseObserver);
    }

    /**
     */
    default void submitTaskResult(gridmr.TaskResult request,
        io.grpc.stub.StreamObserver<gridmr.TaskResultResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubmitTaskResultMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MapReduceService.
   */
  public static abstract class MapReduceServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MapReduceServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MapReduceService.
   */
  public static final class MapReduceServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MapReduceServiceStub> {
    private MapReduceServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MapReduceServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MapReduceServiceStub(channel, callOptions);
    }

    /**
     */
    public void submitJob(gridmr.JobRequest request,
        io.grpc.stub.StreamObserver<gridmr.JobResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubmitJobMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerWorker(gridmr.WorkerRegistrationRequest request,
        io.grpc.stub.StreamObserver<gridmr.WorkerRegistrationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterWorkerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTask(gridmr.TaskRequest request,
        io.grpc.stub.StreamObserver<gridmr.TaskResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTaskMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void submitTaskResult(gridmr.TaskResult request,
        io.grpc.stub.StreamObserver<gridmr.TaskResultResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubmitTaskResultMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MapReduceService.
   */
  public static final class MapReduceServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MapReduceServiceBlockingStub> {
    private MapReduceServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MapReduceServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MapReduceServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public gridmr.JobResponse submitJob(gridmr.JobRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubmitJobMethod(), getCallOptions(), request);
    }

    /**
     */
    public gridmr.WorkerRegistrationResponse registerWorker(gridmr.WorkerRegistrationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterWorkerMethod(), getCallOptions(), request);
    }

    /**
     */
    public gridmr.TaskResponse getTask(gridmr.TaskRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTaskMethod(), getCallOptions(), request);
    }

    /**
     */
    public gridmr.TaskResultResponse submitTaskResult(gridmr.TaskResult request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubmitTaskResultMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MapReduceService.
   */
  public static final class MapReduceServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MapReduceServiceFutureStub> {
    private MapReduceServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MapReduceServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MapReduceServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gridmr.JobResponse> submitJob(
        gridmr.JobRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubmitJobMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gridmr.WorkerRegistrationResponse> registerWorker(
        gridmr.WorkerRegistrationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterWorkerMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gridmr.TaskResponse> getTask(
        gridmr.TaskRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTaskMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gridmr.TaskResultResponse> submitTaskResult(
        gridmr.TaskResult request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubmitTaskResultMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SUBMIT_JOB = 0;
  private static final int METHODID_REGISTER_WORKER = 1;
  private static final int METHODID_GET_TASK = 2;
  private static final int METHODID_SUBMIT_TASK_RESULT = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBMIT_JOB:
          serviceImpl.submitJob((gridmr.JobRequest) request,
              (io.grpc.stub.StreamObserver<gridmr.JobResponse>) responseObserver);
          break;
        case METHODID_REGISTER_WORKER:
          serviceImpl.registerWorker((gridmr.WorkerRegistrationRequest) request,
              (io.grpc.stub.StreamObserver<gridmr.WorkerRegistrationResponse>) responseObserver);
          break;
        case METHODID_GET_TASK:
          serviceImpl.getTask((gridmr.TaskRequest) request,
              (io.grpc.stub.StreamObserver<gridmr.TaskResponse>) responseObserver);
          break;
        case METHODID_SUBMIT_TASK_RESULT:
          serviceImpl.submitTaskResult((gridmr.TaskResult) request,
              (io.grpc.stub.StreamObserver<gridmr.TaskResultResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSubmitJobMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gridmr.JobRequest,
              gridmr.JobResponse>(
                service, METHODID_SUBMIT_JOB)))
        .addMethod(
          getRegisterWorkerMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gridmr.WorkerRegistrationRequest,
              gridmr.WorkerRegistrationResponse>(
                service, METHODID_REGISTER_WORKER)))
        .addMethod(
          getGetTaskMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gridmr.TaskRequest,
              gridmr.TaskResponse>(
                service, METHODID_GET_TASK)))
        .addMethod(
          getSubmitTaskResultMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gridmr.TaskResult,
              gridmr.TaskResultResponse>(
                service, METHODID_SUBMIT_TASK_RESULT)))
        .build();
  }

  private static abstract class MapReduceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MapReduceServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return gridmr.Gridmr.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MapReduceService");
    }
  }

  private static final class MapReduceServiceFileDescriptorSupplier
      extends MapReduceServiceBaseDescriptorSupplier {
    MapReduceServiceFileDescriptorSupplier() {}
  }

  private static final class MapReduceServiceMethodDescriptorSupplier
      extends MapReduceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MapReduceServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MapReduceServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MapReduceServiceFileDescriptorSupplier())
              .addMethod(getSubmitJobMethod())
              .addMethod(getRegisterWorkerMethod())
              .addMethod(getGetTaskMethod())
              .addMethod(getSubmitTaskResultMethod())
              .build();
        }
      }
    }
    return result;
  }
}
