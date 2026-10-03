package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Webhook;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebhookRepositorio {
    public static final WebhookRepositorio INSTANCE = new WebhookRepositorio();

    private final List<Webhook> webhooks = new CopyOnWriteArrayList<>();

    public void registrar(Webhook webhook) { webhooks.add(webhook); }

    public List<Webhook> findAll() { return webhooks; }
}
