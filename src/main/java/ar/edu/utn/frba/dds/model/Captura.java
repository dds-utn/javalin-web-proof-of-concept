package ar.edu.utn.frba.dds.model;

import com.github.jknack.handlebars.internal.lang3.StringUtils;

import java.time.LocalDateTime;

public class Captura {

	private String apodo;
	private Pokemon pokemon;
	private int nivel;
	private LocalDateTime fechaCaptura;

	public Captura(String apodo, Pokemon pokemon, int nivel) {
		super();
		this.apodo = apodo;
		this.pokemon = pokemon;
		this.nivel = nivel;
		this.fechaCaptura = LocalDateTime.now();
	}

	public String getApodo() {
		return apodo;
	}

	public Pokemon getPokemon() {
		return pokemon;
	}

	public int getNivel() {
		return nivel;
	}

	public LocalDateTime getFechaCaptura() {
		return fechaCaptura;
	}

	@Override
	public String toString() {
		return "PokemonCapturado [nombre=" + apodo + ", pokemon=" + pokemon + ", nivel=" + nivel + ", fechaCaptura="
				+ fechaCaptura + "]";
	}

	public boolean sePareceNombreA(String nombre) {
		return StringUtils.startsWithIgnoreCase(this.apodo, nombre);
	}

}
