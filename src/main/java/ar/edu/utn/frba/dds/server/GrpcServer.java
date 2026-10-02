package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.grpc.AuthInterceptor;
import ar.edu.utn.frba.dds.grpc.NoticiasServiceImpl;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.ServerMethodDefinition;
import io.grpc.ServerServiceDefinition;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class GrpcServer {
    public void start() throws IOException {
        AuthInterceptor auth = new AuthInterceptor();

        ServerServiceDefinition base = new NoticiasServiceImpl().bindService();
        ServerServiceDefinition def = ServerServiceDefinition
            .builder(base.getServiceDescriptor())
            .addMethod(method(base, "ConsultarNoticia"))
            .addMethod(method(base, "SeguirComentarios"))
            .addMethod(protect(method(base, "PublicarNoticia"),    auth))
            .addMethod(protect(method(base, "ActualizarNoticia"),  auth))
            .addMethod(protect(method(base, "RetractarNoticia"),   auth))
            .addMethod(protect(method(base, "ComentarNoticia"),    auth))
            .build();

        Server server = ServerBuilder.forPort(9002)
            .addService(def)
            .addService(ProtoReflectionService.newInstance())
            .build()
            .start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        System.out.println("gRPC server started on port 9002");
    }

    @SuppressWarnings("unchecked")
    private <Req, Resp> ServerMethodDefinition<Req, Resp> method(ServerServiceDefinition def, String name) {
        String fullName = def.getServiceDescriptor().getName() + "/" + name;
        return (ServerMethodDefinition<Req, Resp>) def.getMethod(fullName);
    }

    private <Req, Resp> ServerMethodDefinition<Req, Resp> protect(
            ServerMethodDefinition<Req, Resp> method, ServerInterceptor interceptor) {
        ServerCallHandler<Req, Resp> handler = method.getServerCallHandler();
        return ServerMethodDefinition.create(
            method.getMethodDescriptor(),
            (call, headers) -> interceptor.interceptCall(call, headers, handler));
    }
}
