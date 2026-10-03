package ar.edu.utn.frba.dds.protobuf;

import ar.edu.utn.frba.dds.model.Comentario;

public class ComentarioMapper {
  public static ar.edu.utn.frba.dds.grpc.proto.Comentario toProto(Comentario comentario) {
    return ar.edu.utn.frba.dds.grpc.proto.Comentario.newBuilder()
        .setId(comentario.getId())
        .setAutor(comentario.getAutor())
        .setTexto(comentario.getContenido())
        .build();
  }
}
