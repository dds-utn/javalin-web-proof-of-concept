package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.HomeController;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import io.javalin.Javalin;

public class Router implements SimplePersistenceTest {
    public void configure(Javalin app) {
        HomeController controller = new HomeController();

        app.before(ctx -> {
           entityManager().clear();
        });
        app.get("/", context -> context.redirect("/capturas"));
        app.get("/capturas", ctx -> ctx.render("home.hbs", controller.index(ctx)));
        app.get("/capturas/{id}", ctx -> ctx.render("pokemon.hbs", controller.show(ctx)));
        app.put("/capturas/{id}", ctx -> ctx.render("alert.hbs", controller.save(ctx)));
    }
}
