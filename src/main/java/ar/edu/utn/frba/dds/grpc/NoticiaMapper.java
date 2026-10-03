package ar.edu.utn.frba.dds.grpc;

import ar.edu.utn.frba.dds.grpc.proto.ListarNoticiasResponse;
import ar.edu.utn.frba.dds.model.Comentario;
import ar.edu.utn.frba.dds.model.Noticia;

import java.util.List;

public class NoticiaMapper {
    public static ar.edu.utn.frba.dds.grpc.proto.Noticia toProto(Noticia n) {
        return ar.edu.utn.frba.dds.grpc.proto.Noticia.newBuilder()
            .setId(n.getId())
            .setTitulo(n.getTitulo())
            .setCuerpo(n.getContenido())
            .setAutor(n.getAutor())
            .setRetractada(n.isRetractada())
            .build();
    }

    public static ar.edu.utn.frba.dds.grpc.proto.Comentario toProto(long noticiaId, Comentario comentario) {
        return ar.edu.utn.frba.dds.grpc.proto.Comentario.newBuilder()
            .setId(comentario.getId())
            // TODO remover de acá
            .setNoticiaId(noticiaId)
            .setAutor(comentario.getAutor())
            .setTexto(comentario.getContenido())
            .build();
    }

    public static ListarNoticiasResponse toProtoList(List<Noticia> noticias) {
        ListarNoticiasResponse.Builder builder = ListarNoticiasResponse.newBuilder();
        noticias.forEach(n -> builder.addNoticias(toProto(n)));
        return builder.build();
    }
}
