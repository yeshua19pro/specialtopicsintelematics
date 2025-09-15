cmake_minimum_required(VERSION 3.15)
project(worker_local CXX)

set(CMAKE_CXX_STANDARD 17)

# Encontrar Protobuf y gRPC
find_package(Protobuf REQUIRED)
find_package(gRPC REQUIRED)

# Ruta al proto
set(PROTO_FILE ${CMAKE_CURRENT_SOURCE_DIR}/../Master/src/main/proto/gridmr.proto)
get_filename_component(PROTO_NAME ${PROTO_FILE} NAME_WE)

set(PROTO_SRC ${CMAKE_CURRENT_BINARY_DIR}/${PROTO_NAME}.pb.cc)
set(PROTO_HDR ${CMAKE_CURRENT_BINARY_DIR}/${PROTO_NAME}.pb.h)
set(GRPC_SRC ${CMAKE_CURRENT_BINARY_DIR}/${PROTO_NAME}.grpc.pb.cc)
set(GRPC_HDR ${CMAKE_CURRENT_BINARY_DIR}/${PROTO_NAME}.grpc.pb.h)

# Buscar el plugin de grpc
find_program(GRPC_CPP_PLUGIN_EXECUTABLE grpc_cpp_plugin)
if(NOT GRPC_CPP_PLUGIN_EXECUTABLE)
    message(FATAL_ERROR "No se encontró grpc_cpp_plugin. Instala grpc-tools.")
endif()

# Generar archivos .pb.cc y .grpc.pb.cc
add_custom_command(
    OUTPUT ${PROTO_SRC} ${PROTO_HDR} ${GRPC_SRC} ${GRPC_HDR}
    COMMAND protoc
        --cpp_out=${CMAKE_CURRENT_BINARY_DIR}
        --grpc_out=${CMAKE_CURRENT_BINARY_DIR}
        --plugin=protoc-gen-grpc=${GRPC_CPP_PLUGIN_EXECUTABLE}
        -I ${CMAKE_CURRENT_SOURCE_DIR}/../Master/src/main/proto
        ${PROTO_FILE}
    DEPENDS ${PROTO_FILE}
)

set(PROTO_GEN_SRC ${PROTO_SRC} ${GRPC_SRC})

# Ejecutable
add_executable(worker_local
    worker.cpp
    ${PROTO_GEN_SRC}
)

# Incluir directorios
target_include_directories(worker_local PRIVATE
    ${Protobuf_INCLUDE_DIRS}
    ${gRPC_INCLUDE_DIRS}
    ${CMAKE_CURRENT_BINARY_DIR}  # headers generados
)

# Enlazar librerías
target_link_libraries(worker_local PRIVATE
    gRPC::grpc++
    protobuf::libprotobuf
    utf8_range
    utf8_validity
    pthread
    dl
)
