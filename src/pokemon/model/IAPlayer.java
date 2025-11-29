package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class IAPlayer extends Player {

	// ==================================== CONSTRUCTORS
	// ====================================

	public IAPlayer() {
		super();
	}

	// ==================================== METHODS
	// ====================================

	// -----------------------------
	// Chooses Pokemon by comparing the player's Pokemon list
	// -----------------------------
	public void IAPokemonChoice(ArrayList<Pokemon> playerPokemon, HashMap<String, ArrayList<Pokemon>> pokemonPerType,
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {

		for (Pokemon pkPlayer : playerPokemon) {

			// Gets a random type index depending on how many types the Pokemon has
			int typeIndex = (pkPlayer.getTypes().size() == 2) ? ThreadLocalRandom.current().nextInt(0, 2) : 0;

			String chosenTypeName = pkPlayer.getTypes().get(typeIndex).getName().toUpperCase();

			// Filters in a new Map all the damages for the chosen type
			Map<String, HashMap<String, ArrayList<PokemonType>>> filteredEffects = effectPerTypes.entrySet().stream()
					.filter(e -> e.getKey().equalsIgnoreCase(chosenTypeName))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> entry : filteredEffects.entrySet()) {

				// Picks a random "Le rebientan" type (the type that hits hard)
				ArrayList<PokemonType> rebientaList = entry.getValue().get("Le rebientan");
				if (rebientaList == null || rebientaList.isEmpty())
					continue;

				PokemonType pkTRandom = rebientaList.get(ThreadLocalRandom.current().nextInt(rebientaList.size()));

				String randomTypeName = pkTRandom.getName().toUpperCase();

				// Gets all the Pokemon of the random type chosen
				ArrayList<Pokemon> pokemonsOfType = pokemonPerType.get(randomTypeName);
				if (pokemonsOfType == null || pokemonsOfType.isEmpty())
					continue;

				// Chooses a random Pokemon of that type
				Pokemon pkRandom = pokemonsOfType.get(ThreadLocalRandom.current().nextInt(pokemonsOfType.size()));

				// Ensures the IA does not take the same Pokemon twice
				while (this.getPokemon().contains(pkRandom)) {
					pkRandom = pokemonsOfType.get(ThreadLocalRandom.current().nextInt(pokemonsOfType.size()));
				}

				// Adds the selected Pokemon to the IA team
				this.addPokemon(pkRandom);
			}
		}
	}
}
