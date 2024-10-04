package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.repositories.RepositorioUsuaries;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class SessionController {
   public void show(Context context) {
    Map<String, Object> modelo = new HashMap<>();
    context.render("login.hbs", modelo);
  }

  public void create(Context context) {
    try {
      var usuarie = RepositorioUsuaries.INSTANCE.buscar(
          context.formParam("nombre"),
          context.formParam("password")
      );

      context.sessionAttribute("user_id", usuarie.getId());
      context.redirect("/"); // TODO aca va a convenir leer el origen

    } catch (Exception e) {
      Map<String, Object> modelo = new HashMap<>();
      e.printStackTrace();
      modelo.put("error", "usuario o contraseña invalidas");
      context.render("login.hbs", modelo);


    }
  }

}
