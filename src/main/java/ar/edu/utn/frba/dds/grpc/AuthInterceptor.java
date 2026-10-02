package ar.edu.utn.frba.dds.grpc;

import ar.edu.utn.frba.dds.server.Bootstrap;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

public class AuthInterceptor implements ServerInterceptor {
    private static final Metadata.Key<String> AUTH_KEY =
        Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <Req, Resp> ServerCall.Listener<Req> interceptCall(
            ServerCall<Req, Resp> call, Metadata headers, ServerCallHandler<Req, Resp> next) {
        String auth = headers.get(AUTH_KEY);
        if (auth == null || !auth.startsWith("Bearer ")) {
            call.close(Status.UNAUTHENTICATED.withDescription("Token ausente"), new Metadata());
            return new ServerCall.Listener<>() {};
        }
        try {
            JWT.require(Algorithm.HMAC256(Bootstrap.JWT_SECRET)).build().verify(auth.substring(7));
        } catch (JWTVerificationException e) {
            call.close(Status.UNAUTHENTICATED.withDescription("Token inválido"), new Metadata());
            return new ServerCall.Listener<>() {};
        }
        return next.startCall(call, headers);
    }
}
