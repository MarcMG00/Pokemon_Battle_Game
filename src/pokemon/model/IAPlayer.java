package pokemon.model;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class IAPlayer extends Player {

	public IAPlayer() {
		super();
	}

	public void IAChoicePokemon(ArrayList<Pokemon> playerPokemons, HashMap<String, ArrayList<Pokemon>> pokemonsPorTipo,
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> efectoPorTipos) {

		for (Pokemon pkPlayer : playerPokemons) {

			int randomNumberToGetPKTypefor2Types;

			// Gets a random number from the types list size of the current Pokemon
			if (pkPlayer.getTypes().size() == 2) {
				randomNumberToGetPKTypefor2Types = (int) ((Math.random() * (pkPlayer.getTypes().size())));
			} else {
				randomNumberToGetPKTypefor2Types = 0;
			}

			// Filters in a new Map all the damages of the pokemon from the picked type
			Map<String, HashMap<String, ArrayList<PokemonType>>> efectoPorTiposFiltered = efectoPorTipos.entrySet()
					.stream()
					.filter(ef -> ef.getKey().toUpperCase()
							.equals(pkPlayer.getTypes().get(randomNumberToGetPKTypefor2Types).getNombreTipo()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> ef : efectoPorTiposFiltered.entrySet()) {

				System.out.println(ef.getKey() + " le rebientan :");
				// Gets the Pokemon types that "Le rebientan"
				for (PokemonType entryV : ef.getValue().get("Le rebientan")) {
					System.out.println(entryV.getIdPkTipo() + " - " + entryV.getNombreTipo());
				}
				System.out.println();

				// Pick a random type from the types of "Le rebientan"
				Random rand = new Random();
				PokemonType pkTRandom = ef.getValue().get("Le rebientan")
						.get(rand.nextInt(ef.getValue().get("Le rebientan").size()));
				// Get all the Pokemons concerned by the random type picked
				Map<String, ArrayList<Pokemon>> pokemonsPorTipoFiltered = pokemonsPorTipo.entrySet().stream()
						.filter(ppt -> ppt.getKey().equals(pkTRandom.getNombreTipo().toUpperCase()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

				// Gets a random Pokemon from the list of pokemonsPorTipoFiltered
				Pokemon pkRandom = pokemonsPorTipoFiltered.get(pkTRandom.getNombreTipo().toUpperCase())
						.get(rand.nextInt(pokemonsPorTipoFiltered.get(pkTRandom.getNombreTipo().toUpperCase()).size()));
				// Adds the pokemon to th elist of IA
				this.addPokemon(pkRandom);

			}
		}

		// Prints the pokemons from the IA
		for (Pokemon p : this.getPokemons()) {
			System.out.println(p.getNombrePokemon() + " with types:");
			for (PokemonType pt : p.getTypes()) {
				System.out.println(pt.getIdPkTipo() + " - " + pt.getNombreTipo());
			}
		}
	}
	
}
