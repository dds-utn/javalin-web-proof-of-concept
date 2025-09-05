package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.javalin.http.Context;

import java.util.Optional;

public class UsuarioController {
    public Usuario randomUser() {
        return UsuarioRepositorio.INSTANCE.findAny();
    }
}
