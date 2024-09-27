package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.LinkedList;
import java.util.List;

public class UsuarioRepositorio implements WithSimplePersistenceUnit  {

	public static UsuarioRepositorio INSTANCE = new UsuarioRepositorio();
	
	public Usuario findAny() {
		return (Usuario) this.entityManager().createQuery("from Usuario").getResultList().get(0);
	}
	
	public Usuario findByUsername(int username) {
		throw new RuntimeException("findByUsername aun no esta implementado");
	}

	public void registrar(Usuario usuario) {
		entityManager().persist(usuario);
	}
	
	
}
