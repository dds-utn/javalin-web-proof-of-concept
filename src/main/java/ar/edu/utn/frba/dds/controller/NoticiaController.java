package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.grpc.ComentarioNotificador;
import ar.edu.utn.frba.dds.grpc.NoticiaMapper;
import ar.edu.utn.frba.dds.model.Comentario;
import ar.edu.utn.frba.dds.model.Noticia;
import ar.edu.utn.frba.dds.repositories.NoticiaRepositorio;
import com.google.protobuf.MessageLite;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;

import java.util.List;

public class NoticiaController {
    private static final String PROTOBUF = "application/x-protobuf";

    private final NoticiaRepositorio repositorio = NoticiaRepositorio.INSTANCE;

    public void listar(Context ctx) {
        List<Noticia> noticias = repositorio.findAll();
        responder(ctx, noticias, NoticiaMapper.toProtoList(noticias));
    }

    public void obtener(Context ctx) {
        long id = Long.parseLong(ctx.pathParam("id"));
        Noticia noticia = repositorio.findById(id).orElseThrow(NotFoundResponse::new);
        responder(ctx, noticia, NoticiaMapper.toProto(noticia));
    }

    public void publicar(Context ctx) {
        Noticia noticia = ctx.bodyAsClass(Noticia.class);
        repositorio.registrar(noticia);
        ctx.status(HttpStatus.CREATED);
        responder(ctx, noticia, NoticiaMapper.toProto(noticia));
    }

    public void actualizar(Context ctx) {
        long id = Long.parseLong(ctx.pathParam("id"));
        Noticia noticia = repositorio.findById(id).orElseThrow(NotFoundResponse::new);
        Noticia cambios = ctx.bodyAsClass(Noticia.class);
        noticia.actualizar(cambios.getTitulo(), cambios.getContenido());
        responder(ctx, noticia, NoticiaMapper.toProto(noticia));
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
        ComentarioNotificador.INSTANCE.notificar(id, comentario);
        ctx.status(HttpStatus.CREATED);
        responder(ctx, comentario, NoticiaMapper.toProto(id, comentario));
    }

    private void responder(Context ctx, Object domainObj, MessageLite proto) {
        if (PROTOBUF.equals(ctx.header("Accept"))) {
            ctx.result(proto.toByteArray()).contentType(PROTOBUF);
        } else {
            ctx.json(domainObj);
        }
    }
}
