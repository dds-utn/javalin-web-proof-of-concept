package ar.edu.utn.frba.dds.grpc;

import ar.edu.utn.frba.dds.grpc.proto.*;
import ar.edu.utn.frba.dds.model.Comentario;
import ar.edu.utn.frba.dds.model.Noticia;
import ar.edu.utn.frba.dds.protobuf.ComentarioMapper;
import ar.edu.utn.frba.dds.protobuf.NoticiaMapper;
import ar.edu.utn.frba.dds.repositories.NoticiaRepositorio;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class NoticiasServiceImpl extends NoticiasServiceGrpc.NoticiasServiceImplBase {
  private final NoticiaRepositorio repositorio = NoticiaRepositorio.INSTANCE;

  @NotNull
  private static Consumer<Comentario> notificarComentario(long noticiaId,
                                                          ServerCallStreamObserver<ar.edu.utn.frba.dds.grpc.proto.Comentario> serverOut) {
    return (comentario) -> {
      if (!serverOut.isCancelled()) {
        serverOut.onNext(ComentarioMapper.toProto(comentario));
      }
    };
  }

  @Override
  public void consultarNoticia(ConsultarNoticiaRequest req,
                               StreamObserver<ar.edu.utn.frba.dds.grpc.proto.Noticia> out) {
    out.onNext(NoticiaMapper.toProto(findOrThrow(req.getId())));
    out.onCompleted();
  }

  @Override
  public void publicarNoticia(PublicarNoticiaRequest req,
                              StreamObserver<ar.edu.utn.frba.dds.grpc.proto.Noticia> out) {
    Noticia n = new Noticia(req.getTitulo(), req.getCuerpo(), req.getAutor());
    repositorio.registrar(n);
    out.onNext(NoticiaMapper.toProto(n));
    out.onCompleted();
  }

  @Override
  public void actualizarNoticia(ActualizarNoticiaRequest req,
                                StreamObserver<ar.edu.utn.frba.dds.grpc.proto.Noticia> out) {
    Noticia n = findOrThrow(req.getId());
    n.actualizar(req.getTitulo(), req.getCuerpo());
    out.onNext(NoticiaMapper.toProto(n));
    out.onCompleted();
  }

  @Override
  public void retractarNoticia(RetractarNoticiaRequest req,
                               StreamObserver<ar.edu.utn.frba.dds.grpc.proto.Noticia> out) {
    Noticia n = findOrThrow(req.getId());
    n.retractar();
    out.onNext(NoticiaMapper.toProto(n));
    out.onCompleted();
  }

  @Override
  public void comentarNoticia(ComentarNoticiaRequest req,
                              StreamObserver<ar.edu.utn.frba.dds.grpc.proto.Comentario> out) {
    long noticiaId = req.getNoticiaId();
    Noticia noticia = findOrThrow(noticiaId);
    Comentario c = new Comentario(req.getAutor(), req.getTexto());
    noticia.agregarComentario(c);
    noticia.notificarComentario(c);
    out.onNext(ComentarioMapper.toProto(c));
    out.onCompleted();
  }

  @Override
  public void seguirComentarios(SeguirComentariosRequest req,
                                StreamObserver<ar.edu.utn.frba.dds.grpc.proto.Comentario> out) {
    long noticiaId = req.getNoticiaId();
    var noticia = findOrThrow(noticiaId);

    ServerCallStreamObserver<ar.edu.utn.frba.dds.grpc.proto.Comentario> serverOut =
        (ServerCallStreamObserver<ar.edu.utn.frba.dds.grpc.proto.Comentario>) out;

    Consumer<Comentario> anteComentario = notificarComentario(noticiaId, serverOut);
    noticia.anteComentario(anteComentario);

    serverOut.setOnCancelHandler(() -> {
      noticia.quitarAnteComentario(anteComentario);
    });
  }

  private Noticia findOrThrow(long id) {
    return repositorio.findById(id)
        .orElseThrow(() -> new StatusRuntimeException(Status.NOT_FOUND));
  }
}
