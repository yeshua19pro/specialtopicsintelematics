import grpc
import gridmr_pb2
import gridmr_pb2_grpc

def run():
    # Dirección del Master (IP:puerto)
    channel = grpc.insecure_channel("localhost:50051")
    stub = gridmr_pb2_grpc.MapReduceServiceStub(channel)

    # Leer archivo de entrada como bytes
    with open("input.txt", "rb") as f:
        file_data = f.read()

    # Definir parámetros del Job con inputData
    job_request = gridmr_pb2.JobRequest(
        jobId="job-123",
        mapFunction="wordcount_map",
        reduceFunction="wordcount_reduce",
        inputData=file_data,
        numReducers=2
    )

    # Enviar el Job al Master
    response = stub.SubmitJob(job_request)
    if response.success:
        print("✅ Job submitted successfully:", response.message)
    else:
        print("❌ Job submission failed:", response.message)

    # Simulación de pedir tarea al Master
    task_request = gridmr_pb2.TaskRequest(workerId="client-test")
    task_response = stub.GetTask(task_request)

    print("📌 Task response from master:", task_response)


if __name__ == "__main__":
    run()
