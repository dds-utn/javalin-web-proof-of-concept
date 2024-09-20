package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.javalin.http.Context;

public class UsuarioController {
    public Usuario randomUser() {
        return UsuarioRepositorio.INSTANCE.findAny();
    }
}
