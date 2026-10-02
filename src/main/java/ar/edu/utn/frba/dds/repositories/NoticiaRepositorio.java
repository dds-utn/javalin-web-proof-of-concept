package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Noticia;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class NoticiaRepositorio {
    private List<Noticia> noticias = new LinkedList<>();
    public static final NoticiaRepositorio INSTANCE = new NoticiaRepositorio();

    public List<Noticia> findAll() {
        return noticias;
    }

    public Optional<Noticia> findById(long id) {
        return noticias.stream().filter(n -> n.getId() == id).findFirst();
    }

    public void registrar(Noticia noticia) {
        noticias.add(noticia);
    }

    public void eliminar(long id) {
        noticias.removeIf(n -> n.getId() == id);
    }
}
