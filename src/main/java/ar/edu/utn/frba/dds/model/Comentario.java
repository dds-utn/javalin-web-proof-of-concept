package ar.edu.utn.frba.dds.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Comentario {
    private static long nextId = 1;

    private long id;
    private String autor;
    private String contenido;

    @JsonCreator
    public Comentario(@JsonProperty("autor") String autor,
                      @JsonProperty("contenido") String contenido) {
        this.id = nextId++;
        this.autor = autor;
        this.contenido = contenido;
    }

    public long getId() { return id; }
    public String getAutor() { return autor; }
    public String getContenido() { return contenido; }
}
