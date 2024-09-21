package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Captura;
import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HomeController {
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
}
