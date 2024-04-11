package pokemon.game;

import pokemon.model.Game;

public class AppPokemon {

	public static void main(String[] args) {
		
		Game pokemonGame = new Game();
		pokemonGame.InitiateVars();
		pokemonGame.PokemonChoice();

	}
}
