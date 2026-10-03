package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.Webhook;
import ar.edu.utn.frba.dds.repositories.WebhookRepositorio;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class WebhookController {
    private final WebhookRepositorio repositorio = WebhookRepositorio.INSTANCE;

    public void registrar(Context ctx) {
        Webhook webhook = ctx.bodyAsClass(Webhook.class);
        repositorio.registrar(webhook);
        ctx.status(HttpStatus.CREATED).json(webhook);
    }
}
