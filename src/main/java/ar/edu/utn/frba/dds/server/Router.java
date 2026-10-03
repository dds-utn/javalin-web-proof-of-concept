package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controller.NoticiaController;
import ar.edu.utn.frba.dds.controller.WebhookController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

public class Router {
  public void configure(Javalin app) {
    NoticiaController controller = new NoticiaController();
    WebhookController webhookController = new WebhookController();

    app.before("/api/noticias*", ctx -> {
      if (ctx.method().name().equals("GET")) {
        return;
      }
      verifyAuthentication(ctx);
    });

    app.before("/api/noticias/webhooks", ctx -> {
      var token = verifyAuthentication(ctx);
      String usuario = JWT.decode(token).getClaim("usuario").asString();
      if (!"admin".equals(usuario)) {
        throw new ForbiddenResponse();
      }
    });

    app.get("/api/noticias", controller::listar);
    app.get("/api/noticias/{id}", controller::obtener);
    app.post("/api/noticias", controller::publicar);
    app.put("/api/noticias/{id}", controller::actualizar);
    app.delete("/api/noticias/{id}", controller::retractar);
    app.post("/api/noticias/{id}/comentarios", controller::comentar);
    app.sse("/api/noticias/{id}/comentarios/eventos", controller::seguirComentarios);
    app.post("/api/noticias/webhooks", webhookController::registrar);
  }

  private String verifyAuthentication(Context ctx) {
    String auth = ctx.header("Authorization");
    if (auth == null || !auth.startsWith("Bearer ")) throw new UnauthorizedResponse();
    try {
      var build = JWT.require(Algorithm.HMAC256(Bootstrap.JWT_SECRET)).build();
      String token = auth.substring(7);
      build.verify(token);
      return token;
    } catch (JWTVerificationException e) {
      throw new UnauthorizedResponse();
    }
  }
}
