package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.HomeController;
import io.javalin.Javalin;

public class Router {
    public void configure(Javalin app) {
        HomeController controller = new HomeController();
        app.get("/", context -> context.redirect("/capturas"));
        app.get("/capturas", ctx -> ctx.render("home.hbs", controller.index(ctx)));
        app.get("/capturas/{apodo}", ctx -> ctx.render("pokemon.hbs", controller.show(ctx)));
    }
}
