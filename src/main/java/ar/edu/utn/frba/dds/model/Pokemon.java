package ar.edu.utn.frba.dds.model;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {

	private TipoPokemon tipo;
	private String nombre;
	private Pokemon evolucion;
	private List<String> fotos;
	private int pokedexNumber;

	public Pokemon(int pokedexNumber, String nombre, TipoPokemon tipo, Pokemon evolucion) {
		super();
		this.tipo = tipo;
		this.nombre = nombre;
		this.evolucion = evolucion;
		this.pokedexNumber = pokedexNumber;
		this.fotos = new ArrayList<>();
	}

	public TipoPokemon getTipo() {
		return tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public Pokemon getEvolucion() {
		return evolucion;
	}

	public String principalImage() {
		return String.format("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/%d.svg", this.pokedexNumber);
	}

	@Override
	public String toString() {
		return "Pokemon [tipo=" + tipo + ", nombre=" + nombre + ", fotos=" + fotos + "]";
	}

}
