package ar.edu.utn.frba.dds.grpc;

import ar.edu.utn.frba.dds.grpc.proto.ActualizarNoticiaRequest;
import ar.edu.utn.frba.dds.grpc.proto.ComentarNoticiaRequest;
import ar.edu.utn.frba.dds.grpc.proto.ConsultarNoticiaRequest;
import ar.edu.utn.frba.dds.grpc.proto.NoticiasServiceGrpc;
import ar.edu.utn.frba.dds.grpc.proto.PublicarNoticiaRequest;
import ar.edu.utn.frba.dds.grpc.proto.RetractarNoticiaRequest;
import ar.edu.utn.frba.dds.grpc.proto.SeguirComentariosRequest;
import ar.edu.utn.frba.dds.model.Comentario;
import ar.edu.utn.frba.dds.model.Noticia;
import ar.edu.utn.frba.dds.repositories.NoticiaRepositorio;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

public class NoticiasServiceImpl extends NoticiasServiceGrpc.NoticiasServiceImplBase {
    private final NoticiaRepositorio repositorio = NoticiaRepositorio.INSTANCE;

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
        Noticia n = findOrThrow(noticiaId);
        Comentario c = new Comentario(req.getAutor(), req.getTexto());
        n.agregarComentario(c);
        ComentarioNotificador.INSTANCE.notificar(noticiaId, c);
        out.onNext(NoticiaMapper.toProto(noticiaId, c));
        out.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void seguirComentarios(SeguirComentariosRequest req,
                                  StreamObserver<ar.edu.utn.frba.dds.grpc.proto.Comentario> out) {
        long noticiaId = req.getNoticiaId();
        findOrThrow(noticiaId);

        ServerCallStreamObserver<ar.edu.utn.frba.dds.grpc.proto.Comentario> serverOut =
            (ServerCallStreamObserver<ar.edu.utn.frba.dds.grpc.proto.Comentario>) out;

        Runnable desregistrar = ComentarioNotificador.INSTANCE.registrar(noticiaId, c -> {
            if (!serverOut.isCancelled()) {
                serverOut.onNext(NoticiaMapper.toProto(noticiaId, c));
            }
        });

        serverOut.setOnCancelHandler(desregistrar::run);
    }

    private Noticia findOrThrow(long id) {
        return repositorio.findById(id)
            .orElseThrow(() -> new StatusRuntimeException(Status.NOT_FOUND));
    }
}
