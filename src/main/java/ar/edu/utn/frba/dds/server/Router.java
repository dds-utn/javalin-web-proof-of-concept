package ar.edu.utn.frba.dds.server;

import io.javalin.Javalin;

public class Router {
    public void configure(Javalin app) {
        UsuarioController controller = new UsuarioController();
        app.get("/users/random", ctx -> ctx.status(200));
    }
}
