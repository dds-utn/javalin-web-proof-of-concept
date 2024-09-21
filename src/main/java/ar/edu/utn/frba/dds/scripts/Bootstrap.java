package ar.edu.utn.frba.dds.scripts;

import ar.edu.utn.frba.dds.model.Pokemon;
import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bootstrap {
	public static void init() {
		List<Usuario> usuarios = usuarios();
		usuarios.forEach((usuario) -> UsuarioRepositorio.INSTANCE.registrar(usuario));
		
		List<Pokemon> pokemons = pokemons();
		
		Usuario gaston = usuarios.get(0);
		pokemons.stream().forEach((pokemon) -> gaston.capturar(pokemon));
		
	}

	private static List<Pokemon> pokemons() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ClassLoader classLoader = Bootstrap.class.getClassLoader();
		List<Pokemon> pokemons = new ArrayList<>();
		try (Reader reader = new InputStreamReader(classLoader.getResourceAsStream("pokedex.json"))) {
			PokedexList pokedex = gson.fromJson(reader, PokedexList.class);

			pokemons = pokedex.getResults().stream().map(dto -> new Pokemon(
					dto.getNumber(),
					StringUtils.capitalize(dto.getName()),
					null,
					null
			)).toList();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pokemons;
	}

	private static List<Usuario> usuarios() {
		return Arrays.asList(new Usuario("gaston", "gaston"),
		new Usuario("lucas", "lucas"));
	}
}
