package gridmr;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * NEW SERVICE FOR DATA TRANSFER
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: gridmr.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DataTransferServiceGrpc {

  private DataTransferServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "gridmr.DataTransferService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<gridmr.ReceiveFileRequest,
      gridmr.FileChunk> getReceiveFileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReceiveFile",
      requestType = gridmr.ReceiveFileRequest.class,
      responseType = gridmr.FileChunk.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<gridmr.ReceiveFileRequest,
      gridmr.FileChunk> getReceiveFileMethod() {
    io.grpc.MethodDescriptor<gridmr.ReceiveFileRequest, gridmr.FileChunk> getReceiveFileMethod;
    if ((getReceiveFileMethod = DataTransferServiceGrpc.getReceiveFileMethod) == null) {
      synchronized (DataTransferServiceGrpc.class) {
        if ((getReceiveFileMethod = DataTransferServiceGrpc.getReceiveFileMethod) == null) {
          DataTransferServiceGrpc.getReceiveFileMethod = getReceiveFileMethod =
              io.grpc.MethodDescriptor.<gridmr.ReceiveFileRequest, gridmr.FileChunk>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReceiveFile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.ReceiveFileRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.FileChunk.getDefaultInstance()))
              .setSchemaDescriptor(new DataTransferServiceMethodDescriptorSupplier("ReceiveFile"))
              .build();
        }
      }
    }
    return getReceiveFileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gridmr.FileChunk,
      gridmr.SendFileResponse> getSendFileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendFile",
      requestType = gridmr.FileChunk.class,
      responseType = gridmr.SendFileResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<gridmr.FileChunk,
      gridmr.SendFileResponse> getSendFileMethod() {
    io.grpc.MethodDescriptor<gridmr.FileChunk, gridmr.SendFileResponse> getSendFileMethod;
    if ((getSendFileMethod = DataTransferServiceGrpc.getSendFileMethod) == null) {
      synchronized (DataTransferServiceGrpc.class) {
        if ((getSendFileMethod = DataTransferServiceGrpc.getSendFileMethod) == null) {
          DataTransferServiceGrpc.getSendFileMethod = getSendFileMethod =
              io.grpc.MethodDescriptor.<gridmr.FileChunk, gridmr.SendFileResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendFile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.FileChunk.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gridmr.SendFileResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataTransferServiceMethodDescriptorSupplier("SendFile"))
              .build();
        }
      }
    }
    return getSendFileMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DataTransferServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataTransferServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataTransferServiceStub>() {
        @java.lang.Override
        public DataTransferServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataTransferServiceStub(channel, callOptions);
        }
      };
    return DataTransferServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DataTransferServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataTransferServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataTransferServiceBlockingStub>() {
        @java.lang.Override
        public DataTransferServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataTransferServiceBlockingStub(channel, callOptions);
        }
      };
    return DataTransferServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DataTransferServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataTransferServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataTransferServiceFutureStub>() {
        @java.lang.Override
        public DataTransferServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataTransferServiceFutureStub(channel, callOptions);
        }
      };
    return DataTransferServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * NEW SERVICE FOR DATA TRANSFER
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void receiveFile(gridmr.ReceiveFileRequest request,
        io.grpc.stub.StreamObserver<gridmr.FileChunk> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReceiveFileMethod(), responseObserver);
    }

    /**
     */
    default io.grpc.stub.StreamObserver<gridmr.FileChunk> sendFile(
        io.grpc.stub.StreamObserver<gridmr.SendFileResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getSendFileMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DataTransferService.
   * <pre>
   * NEW SERVICE FOR DATA TRANSFER
   * </pre>
   */
  public static abstract class DataTransferServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DataTransferServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DataTransferService.
   * <pre>
   * NEW SERVICE FOR DATA TRANSFER
   * </pre>
   */
  public static final class DataTransferServiceStub
      extends io.grpc.stub.AbstractAsyncStub<DataTransferServiceStub> {
    private DataTransferServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataTransferServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataTransferServiceStub(channel, callOptions);
    }

    /**
     */
    public void receiveFile(gridmr.ReceiveFileRequest request,
        io.grpc.stub.StreamObserver<gridmr.FileChunk> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getReceiveFileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<gridmr.FileChunk> sendFile(
        io.grpc.stub.StreamObserver<gridmr.SendFileResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getSendFileMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DataTransferService.
   * <pre>
   * NEW SERVICE FOR DATA TRANSFER
   * </pre>
   */
  public static final class DataTransferServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DataTransferServiceBlockingStub> {
    private DataTransferServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataTransferServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataTransferServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<gridmr.FileChunk> receiveFile(
        gridmr.ReceiveFileRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getReceiveFileMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DataTransferService.
   * <pre>
   * NEW SERVICE FOR DATA TRANSFER
   * </pre>
   */
  public static final class DataTransferServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<DataTransferServiceFutureStub> {
    private DataTransferServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataTransferServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataTransferServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_RECEIVE_FILE = 0;
  private static final int METHODID_SEND_FILE = 1;

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
        case METHODID_RECEIVE_FILE:
          serviceImpl.receiveFile((gridmr.ReceiveFileRequest) request,
              (io.grpc.stub.StreamObserver<gridmr.FileChunk>) responseObserver);
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
        case METHODID_SEND_FILE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sendFile(
              (io.grpc.stub.StreamObserver<gridmr.SendFileResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getReceiveFileMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              gridmr.ReceiveFileRequest,
              gridmr.FileChunk>(
                service, METHODID_RECEIVE_FILE)))
        .addMethod(
          getSendFileMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              gridmr.FileChunk,
              gridmr.SendFileResponse>(
                service, METHODID_SEND_FILE)))
        .build();
  }

  private static abstract class DataTransferServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DataTransferServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return gridmr.Gridmr.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DataTransferService");
    }
  }

  private static final class DataTransferServiceFileDescriptorSupplier
      extends DataTransferServiceBaseDescriptorSupplier {
    DataTransferServiceFileDescriptorSupplier() {}
  }

  private static final class DataTransferServiceMethodDescriptorSupplier
      extends DataTransferServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DataTransferServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DataTransferServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DataTransferServiceFileDescriptorSupplier())
              .addMethod(getReceiveFileMethod())
              .addMethod(getSendFileMethod())
              .build();
        }
      }
    }
    return result;
  }
}
