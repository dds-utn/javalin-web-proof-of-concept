package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.repositories.RepositorioUsuaries;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class HomeController implements WithSimplePersistenceUnit, TransactionalOps {
  public void index(Context ctx) {
    Map model = new HashMap<>();
    model.put("personas", RepositorioUsuaries.INSTANCE.contar());
    ctx.render("home.hbs", model);
  }
}
