package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.Pokemon;
import ar.edu.utn.frba.dds.model.TipoPokemon;
import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bootstrap {
	public static void init() {
		List<Usuario> usuarios = usuarios();
		usuarios.forEach((usuario) -> UsuarioRepositorio.INSTANCE.registrar(usuario));
		
		List<Pokemon> pokemons = pokemons();
		
		Usuario gaston = usuarios.get(0);
		pokemons.stream().limit(4).forEach((pokemon) -> gaston.capturar(pokemon));
		
	}

	private static List<Pokemon> pokemons() {
		Pokemon golduck = new Pokemon(TipoPokemon.AGUA, "Golduck", null)
				.agregarFoto("https://i.pinimg.com/564x/5e/eb/80/5eeb80e6a927f0114c1e42868951d18b.jpg");
		Pokemon charizard = new Pokemon(TipoPokemon.FUEGO, "Charizard", null)
				.agregarFoto("https://i.pinimg.com/564x/c2/af/bf/c2afbf85f891b61ac54812e89cedcfd2.jpg");
		Pokemon charmeleon = new Pokemon(TipoPokemon.FUEGO, "Charmeleon", charizard)
				.agregarFoto("https://i.pinimg.com/564x/e8/be/e6/e8bee6e124e0848cd3f99b232011b699.jpg");
		
		List<Pokemon> pokemons = Arrays.asList(
				new Pokemon(TipoPokemon.FUEGO, "Charmander", charmeleon)
					.agregarFoto("https://i.pinimg.com/564x/63/63/04/6363049dfea665132eeb278dbda59d68.jpg"),
				new Pokemon(TipoPokemon.AGUA, "Psyduck", golduck)
					.agregarFoto("https://cdn.dribbble.com/users/7824408/screenshots/15980091/media/245e0c6058782aaf4311964ebd30b28b.jpg"),
				new Pokemon(TipoPokemon.NORMAL, "Snorlax", null)
					.agregarFoto("https://i.pinimg.com/564x/26/65/96/266596b8e9d33571651107d08d7a136b.jpg"),
				new Pokemon(TipoPokemon.HADA, "Dedenne", null)
					.agregarFoto("https://d.furaffinity.net/art/huiro/1421855255/1421855255.huiro_%E7%94%B5%E5%A5%B6%E5%A5%B6.png"),
				new Pokemon(TipoPokemon.PSIQUICO, "Mew", null));
		
		pokemons.stream().collect(Collectors.toList()).addAll(Arrays.asList(golduck, charizard, charmeleon));
		
		return pokemons;
	}

	private static List<Usuario> usuarios() {
		return Arrays.asList(new Usuario("gaston", "gaston"),
		new Usuario("lucas", "lucas"));
	}
}
