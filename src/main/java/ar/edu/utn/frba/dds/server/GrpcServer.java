package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.grpc.NoticiasServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class GrpcServer {
    public void start() throws IOException {
        Server server = ServerBuilder.forPort(9002)
            .addService(new NoticiasServiceImpl())
            .addService(ProtoReflectionService.newInstance())
            .build()
            .start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        System.out.println("gRPC server started on port 9002");
    }
}
