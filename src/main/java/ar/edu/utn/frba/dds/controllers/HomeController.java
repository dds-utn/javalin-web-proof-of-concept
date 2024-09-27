package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Captura;
import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HomeController implements WithSimplePersistenceUnit, TransactionalOps {
    public Map<String, Object> index(@NotNull Context ctx) {
        String filtroParametro = ctx.queryParam("likeNombre");
        Usuario usuario = UsuarioRepositorio.INSTANCE.findAny();
        List<Captura> capturas = Optional.ofNullable(filtroParametro)
                .map(filtro -> usuario.filtrarPorNombre(filtro))
                .orElseGet(() -> usuario.getCapturas());

        Map model = new HashMap<>();
        model.put("likeNombre", filtroParametro);
        model.put("usuario", usuario);
        model.put("capturas", capturas);

        return model;
    }

    public Map<String, Object> show(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Usuario usuario = UsuarioRepositorio.INSTANCE.findAny();

        Map model = showPokemon(usuario, id);
        return model;
    }

    public Map<String,? extends Object> save(Context ctx) {
        String apodo = ctx.formParam("apodo");
        Long id = Long.parseLong(ctx.pathParam("id"));
        Usuario usuario = UsuarioRepositorio.INSTANCE.findAny();

        withTransaction(() -> {
            usuario.getPokemonCapturado(id).setApodo(apodo);
        });

        return this.show(ctx);
    }

    @NotNull
    private static Map showPokemon(Usuario usuario, Long id) {
        Map model = new HashMap<>();
        model.put("usuario", usuario);
        model.put("captura", usuario.getPokemonCapturado(id));
        return model;
    }
}
