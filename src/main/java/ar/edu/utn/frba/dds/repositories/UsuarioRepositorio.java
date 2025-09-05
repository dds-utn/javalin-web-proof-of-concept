package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Usuario;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositorio {

	private List<Usuario> usuarios = new LinkedList<>();
	public static UsuarioRepositorio INSTANCE = new UsuarioRepositorio();
	
	public Usuario findAny() {
		return usuarios.stream().findAny().orElse(null);
	}
	public void registrar(Usuario usuario) {
		usuarios.add(usuario);
	}
	
	
}
