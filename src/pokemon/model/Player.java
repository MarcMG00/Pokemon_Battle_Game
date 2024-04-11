package pokemon.model;

import java.util.ArrayList;

public class Player {
	private ArrayList<Pokemon> pokemons;

	public Player() {
		super();
		this.pokemons = new ArrayList<>();
	}

	public ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setPokemons(ArrayList<Pokemon> pokemons) {
		this.pokemons = pokemons;
	}
	
	public void addPokemon(Pokemon pk) {
		this.pokemons.add(pk);
	}
	
}
