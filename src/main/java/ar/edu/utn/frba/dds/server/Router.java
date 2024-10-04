package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.SessionController;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import io.javalin.Javalin;

public class Router implements SimplePersistenceTest {
  public void configure(Javalin app) {
    var home = new HomeController();
     var session = new SessionController();

    app.before(ctx -> {
      entityManager().clear();
    });
    app.get("/", home::index);
    app.get("/login", session::show);
    app.post("/login", session::create);

  }
}
