package ar.edu.utn.frba.dds.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Noticia {
    private static long nextId = 1;

    private long id;
    private String titulo;
    private String contenido;
    private String autor;
    private LocalDateTime fechaPublicacion;
    private boolean retractada = false;
    private List<Comentario> comentarios = new LinkedList<>();

    @JsonCreator
    public Noticia(@JsonProperty("titulo") String titulo,
                   @JsonProperty("contenido") String contenido,
                   @JsonProperty("autor") String autor) {
        this.id = nextId++;
        this.titulo = titulo;
        this.contenido = contenido;
        this.autor = autor;
        this.fechaPublicacion = LocalDateTime.now();
    }

    public long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
    public String getAutor() { return autor; }
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public boolean isRetractada() { return retractada; }
    public List<Comentario> getComentarios() { return comentarios; }

    public void actualizar(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public void retractar() { this.retractada = true; }

    public void agregarComentario(Comentario comentario) {
        this.comentarios.add(comentario);
    }
}
