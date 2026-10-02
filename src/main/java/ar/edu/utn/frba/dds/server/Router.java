package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controller.NoticiaController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;

public class Router {
    public void configure(Javalin app) {
        NoticiaController controller = new NoticiaController();

        app.before("/api/noticias*", ctx -> {
            if (ctx.method().name().equals("GET")) return;
            String auth = ctx.header("Authorization");
            if (auth == null || !auth.startsWith("Bearer ")) throw new UnauthorizedResponse();
            try {
                JWT.require(Algorithm.HMAC256(Bootstrap.JWT_SECRET)).build().verify(auth.substring(7));
            } catch (JWTVerificationException e) {
                throw new UnauthorizedResponse();
            }
        });

        app.get("/api/noticias", controller::listar);
        app.get("/api/noticias/{id}", controller::obtener);
        app.post("/api/noticias", controller::publicar);
        app.put("/api/noticias/{id}", controller::actualizar);
        app.delete("/api/noticias/{id}", controller::retractar);
        app.post("/api/noticias/{id}/comentarios", controller::comentar);
    }
}
