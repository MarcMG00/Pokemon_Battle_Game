package pokemon.model;

import java.util.ArrayList;
import java.util.Random;

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
	
	// Adds Pokemon to Pokemon player
	public void addPokemon(Pokemon pk) {
		this.pokemons.add(pk);
	}
	
	// Adds 4 attacs to a Pokemon (1 other, 2 fisics, 1 special)
	public void addAtacsForEackPokemon() {
		for(Pokemon pk : this.pokemons) {
			Random rand = new Random();
			pk.addAtaques(pk.getAtaEstado().get(rand.nextInt(pk.getAtaEstado().size())));
			
			for(int times = 0; times < 2; times++) {
				rand = new Random();
				pk.addAtaques(pk.getAtaFisicos().get(rand.nextInt(pk.getAtaFisicos().size())));
			}
			
			rand = new Random();
			pk.addAtaques(pk.getAtaEspeciales().get(rand.nextInt(pk.getAtaEspeciales().size())));
		}
	}
	
}
