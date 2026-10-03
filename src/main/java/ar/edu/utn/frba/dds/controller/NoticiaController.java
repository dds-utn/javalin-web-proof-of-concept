package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.protobuf.ComentarioMapper;
import ar.edu.utn.frba.dds.protobuf.NoticiaMapper;
import ar.edu.utn.frba.dds.model.Comentario;
import ar.edu.utn.frba.dds.model.Noticia;
import ar.edu.utn.frba.dds.repositories.NoticiaRepositorio;
import ar.edu.utn.frba.dds.repositories.WebhookRepositorio;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.MessageLite;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.sse.SseClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class NoticiaController {
    private static final String PROTOBUF = "application/x-protobuf";
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private final NoticiaRepositorio repositorio = NoticiaRepositorio.INSTANCE;

    public void listar(Context ctx) {
        List<Noticia> noticias = repositorio.findAll();
        responder(ctx, noticias, NoticiaMapper::toProtoList);
    }

    public void obtener(Context ctx) {
        long id = Long.parseLong(ctx.pathParam("id"));
        Noticia noticia = repositorio.findById(id).orElseThrow(NotFoundResponse::new);
        responder(ctx, noticia, NoticiaMapper::toProto);
    }

    public void publicar(Context ctx) {
        Noticia noticia = ctx.bodyAsClass(Noticia.class);
        repositorio.registrar(noticia);
        dispararWebhooks(noticia);
        ctx.status(HttpStatus.CREATED);
        responder(ctx, noticia, NoticiaMapper::toProto);
    }

    private void dispararWebhooks(Noticia noticia) {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        WebhookRepositorio.INSTANCE.findAll().forEach(webhook -> {
            try {
                HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(webhook.getUrl()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(noticia)))
                    .timeout(Duration.ofSeconds(5))
                    .build();
                httpClient.sendAsync(req, HttpResponse.BodyHandlers.discarding());
            } catch (Exception ignored) {}
        });
    }

    public void actualizar(Context ctx) {
        long id = Long.parseLong(ctx.pathParam("id"));
        Noticia noticia = repositorio.findById(id).orElseThrow(NotFoundResponse::new);
        Noticia cambios = ctx.bodyAsClass(Noticia.class);
        noticia.actualizar(cambios.getTitulo(), cambios.getContenido());
        responder(ctx, noticia, NoticiaMapper::toProto);
    }

    public void retractar(Context ctx) {
        long id = Long.parseLong(ctx.pathParam("id"));
        repositorio.findById(id).orElseThrow(NotFoundResponse::new);
        repositorio.eliminar(id);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    public void comentar(Context ctx) {
        long id = Long.parseLong(ctx.pathParam("id"));
        Noticia noticia = repositorio.findById(id).orElseThrow(NotFoundResponse::new);
        Comentario comentario = ctx.bodyAsClass(Comentario.class);
        noticia.agregarComentario(comentario);
        noticia.notificarComentario(comentario);
        ctx.status(HttpStatus.CREATED);
        responder(ctx, comentario, c -> ComentarioMapper.toProto(c));
    }

    public void seguirComentarios(SseClient client) {
        long id = Long.parseLong(client.ctx().pathParam("id"));
        var noticia = repositorio.findById(id);
        if (noticia.isEmpty()) {
            client.sendEvent("error", "{\"mensaje\":\"Noticia no encontrada\"}");
            client.close();
            return;
        }

        // mensaje inicial (opcional)
        client.sendComment("hello");

        // mensaje de keep alive para que proxies http no
        // aborten la conexión (opcional)
        var keepAlive = Executors.newSingleThreadScheduledExecutor();
        keepAlive.scheduleAtFixedRate(() -> {
            client.sendComment("keep-alive");
        }, 30, 30, TimeUnit.SECONDS);

        var anteComentario = notificarComentario(client);
        client.onClose(() -> {
            noticia.get().quitarAnteComentario(anteComentario);
            keepAlive.shutdown();
        });

        noticia.get().anteComentario(anteComentario);
        client.keepAlive();
    }

    private static Consumer<Comentario> notificarComentario(SseClient client) {
        ObjectMapper mapper = new ObjectMapper();
        return comentario -> {
            if (!client.terminated()) {
                try {
                    client.sendEvent("comentario", mapper.writeValueAsString(comentario));
                } catch (Exception ignored) {
                    // noop
                }
            }
        };
    }

    // TODO este código es genérico y podría reutilizarse fácilmente en otros controladores
    private <T> void responder(Context ctx, T respuesta, Function<T, MessageLite> toProto) {
        if (PROTOBUF.equals(ctx.header("Accept"))) {
            ctx.result(toProto.apply(respuesta).toByteArray()).contentType(PROTOBUF);
        } else {
            // Asumimos application/json
            // Aunque bien podríamos chequearlo
            ctx.json(respuesta);
        }
    }
}
