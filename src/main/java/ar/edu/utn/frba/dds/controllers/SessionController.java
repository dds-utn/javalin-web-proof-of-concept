package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.repositories.RepositorioUsuaries;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class SessionController {
  public void show(Context context) {
    if (context.sessionAttribute("user_id") != null) {
      context.redirect("/");
    }

    Map<String, Object> modelo = new HashMap<>();
    if ("true".equals(context.queryParam("error"))) {
      modelo.put("error", "usuario o contraseña invalidas");
    }
    context.render("login.hbs", modelo);
  }

  public void create(Context context) {
    try {
      var usuarie = RepositorioUsuaries.INSTANCE.buscar(
          context.formParam("nombre"),
          context.formParam("password")
      );

      context.sessionAttribute("user_id", usuarie.getId());
      context.redirect("/");

    } catch (Exception e) {
      Map<String, Object> modelo = new HashMap<>();
      e.printStackTrace();

      context.redirect("/login?error=true");
      // y hay otra opción mas....
    }
  }

}

// JSESSIONID=node01rra7at67x33zkdgq3rqze4ck0.node0
// JSESSIONID=node01rra7at67x33zkdgq3rqze4ck0.node0
// JSESSIONID=node01xqpogtx1ksio68qwagj8tpx0.node0
// JSESSIONID=node01xqpogtx1ksio68qwagj8tpx0.node0
// JSESSIONID=node01xqpogtx1ksio68qwagj8tpx0.node0

// client => COOKIE (puede mandar una cookie)
//
// <=  server (mantener la cookie como está)
// <=  server SET-COOKIE (cambia la cookie por otra)


// pero qué es la cookie?
// - podría ser la propia información de sesión (ruby)
// - podría ser un identificar de esa información de sesión (java)

// mecansimos para gestión de sesión
// - cookies
// - tokens al portador (bearer) (como una cabecera, tipicamente Authorization, como un query
// param, o como quieras)
//      - JWT => tipo de dato




