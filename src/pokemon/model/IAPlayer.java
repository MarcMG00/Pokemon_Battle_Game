package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class IAPlayer extends Player {

	public IAPlayer() {
		super();
	}

	// Chose Pokemon comparing Pokemon list from the player
	public void IAChoicePokemon(ArrayList<Pokemon> playerPokemon, HashMap<String, ArrayList<Pokemon>> pokemonPerType,
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {

		for (Pokemon pkPlayer : playerPokemon) {

			int randomNumberToGetPKTypefor2Types;

			// Gets a random number from the types list size of the current Pokemon
			if (pkPlayer.getTypes().size() == 2) {
				
				randomNumberToGetPKTypefor2Types = (int) ((Math.random() * (pkPlayer.getTypes().size())));
				
			} else {
				
				// If there is only one type, takes first element
				randomNumberToGetPKTypefor2Types = 0;
				
			}

			// Filters in a new Map all the damages of the Pokemon from the picked type
			Map<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypesFiltered = effectPerTypes.entrySet()
					.stream()
					.filter(ef -> ef.getKey().toUpperCase().equals(pkPlayer.getTypes().get(randomNumberToGetPKTypefor2Types).getName().toUpperCase()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> ef : effectPerTypesFiltered.entrySet()) {

				// Pick a random type from the types of "Le rebientan"
				Random rand = new Random();
				
				PokemonType pkTRandom = ef.getValue().get("Le rebientan").get(rand.nextInt(ef.getValue().get("Le rebientan").size()));
				
				// Get all the Pokemon concerned by the random type picked
				Map<String, ArrayList<Pokemon>> pokemonPerTypeFiltered = pokemonPerType.entrySet()
						.stream()
						.filter(ppt -> ppt.getKey().equals(pkTRandom.getName().toUpperCase()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

				// Gets a random Pokemon from the list of pokemonPerTypeFiltered
				Pokemon pkRandom = pokemonPerTypeFiltered.get(pkTRandom.getName().toUpperCase())
														 .get(rand.nextInt(pokemonPerTypeFiltered.get(pkTRandom.getName().toUpperCase()).size()));

				// Adds the Pokemon to the list of IA, but cannot have the same Pokemon twice
				if (this.getPokemon().contains(pkRandom)) {
					
					while (this.getPokemon().contains(pkRandom)) {
						
						pkRandom = pokemonPerTypeFiltered.get(pkTRandom.getName().toUpperCase())
														 .get(rand.nextInt(pokemonPerTypeFiltered.get(pkTRandom.getName().toUpperCase()).size()));
						
					}
				} else {
					
					this.addPokemon(pkRandom);
					
				}
			}
		}
	}
}
