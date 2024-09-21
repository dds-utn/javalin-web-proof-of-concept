package ar.edu.utn.frba.dds.server;

import io.javalin.Javalin;

public class Router {
    public void configure(Javalin app) {
        HomeController controller = new HomeController();
        app.get("/home", ctx -> ctx.render("home.hbs", controller.index(ctx)));
    }
}
