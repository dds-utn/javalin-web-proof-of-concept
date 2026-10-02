package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.Comentario;
import ar.edu.utn.frba.dds.model.Noticia;
import ar.edu.utn.frba.dds.repositories.NoticiaRepositorio;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;

public class NoticiaController {
    private final NoticiaRepositorio repositorio = NoticiaRepositorio.INSTANCE;

    public void listar(Context ctx) {
        ctx.json(repositorio.findAll());
    }

    public void obtener(Context ctx) {
        long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(repositorio.findById(id).orElseThrow(NotFoundResponse::new));
    }

    public void publicar(Context ctx) {
        Noticia noticia = ctx.bodyAsClass(Noticia.class);
        repositorio.registrar(noticia);
        ctx.status(HttpStatus.CREATED).json(noticia);
    }

    public void actualizar(Context ctx) {
        long id = Long.parseLong(ctx.pathParam("id"));
        Noticia noticia = repositorio.findById(id).orElseThrow(NotFoundResponse::new);
        noticia.actualizar(ctx.bodyAsClass(Noticia.class));
        ctx.json(noticia);
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
        ctx.status(HttpStatus.CREATED).json(comentario);
    }
}
