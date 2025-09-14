cmake_minimum_required(VERSION 3.15)
project(worker CXX)

set(CMAKE_CXX_STANDARD 17)

# Protobuf y gRPC
find_package(Protobuf REQUIRED)
find_package(gRPC REQUIRED)

# AWS SDK
find_package(AWSSDK REQUIRED COMPONENTS s3)

# Archivos generados de Protobuf y gRPC
set(PROTO_SRC
  ${CMAKE_CURRENT_SOURCE_DIR}/gridmr.pb.cc
  ${CMAKE_CURRENT_SOURCE_DIR}/gridmr.grpc.pb.cc
)

# Ejecutable
add_executable(worker
  worker.cpp
  ${PROTO_SRC}
)

# Incluir directorios
target_include_directories(worker PRIVATE
  ${Protobuf_INCLUDE_DIRS}
  ${gRPC_INCLUDE_DIRS}
  ${AWSSDK_INCLUDE_DIRS}
)

# Enlazar librerías necesarias
target_link_libraries(worker
  gRPC::grpc++
  protobuf::libprotobuf
  aws-cpp-sdk-core
  aws-cpp-sdk-s3
  pthread
  utf8_range         
  utf8_validity
)
