package ar.edu.utn.frba.dds.grpc;

import ar.edu.utn.frba.dds.model.Comentario;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ComentarioNotificador {
    public static final ComentarioNotificador INSTANCE = new ComentarioNotificador();

    private final Map<Long, List<Consumer<Comentario>>> listeners = new ConcurrentHashMap<>();

    public Runnable registrar(long noticiaId, Consumer<Comentario> listener) {
        listeners.computeIfAbsent(noticiaId, k -> new CopyOnWriteArrayList<>()).add(listener);
        return () -> {
            List<Consumer<Comentario>> list = listeners.get(noticiaId);
            if (list != null) list.remove(listener);
        };
    }

    public void notificar(long noticiaId, Comentario comentario) {
        List<Consumer<Comentario>> list = listeners.get(noticiaId);
        if (list != null) list.forEach(l -> l.accept(comentario));
    }
}
