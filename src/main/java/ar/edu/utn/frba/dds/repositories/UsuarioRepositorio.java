package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Usuario;

import java.util.LinkedList;
import java.util.List;

public class UsuarioRepositorio {

	private List<Usuario> usuarios = new LinkedList<>();
	public static UsuarioRepositorio INSTANCE = new UsuarioRepositorio();
	
	public Usuario findAny() {
		return usuarios.stream().findAny().orElse(null);
	}
	
	public Usuario findByUsername(int username) {
		throw new RuntimeException("findByUsername aun no esta implementado");
	}

	public void registrar(Usuario usuario) {
		usuarios.add(usuario);
	}
	
	
}
