package pokemon.model;

import java.util.ArrayList;
import java.util.List;

import pokemon.enums.StatusConditions;

public class Player {

	// ==================================== FIELDS
	// ====================================

	private ArrayList<Pokemon> pokemon;
	private Pokemon pkCombatting;
	private Pokemon pkFacing;
	private boolean forceSwitchPokemon;

	// ==================================== CONSTRUCTORS
	// ====================================

	public Player() {
		super();
		this.pokemon = new ArrayList<>();
		this.pkCombatting = new Pokemon();
		this.pkFacing = new Pokemon();
		this.forceSwitchPokemon = false;
	}

	// ==================================== GETTERS/SETTERS
	// ====================================

	public ArrayList<Pokemon> getPokemon() {
		return pokemon;
	}

	public void setPokemon(ArrayList<Pokemon> pokemon) {
		this.pokemon = pokemon;
	}

	public Pokemon getPkCombatting() {
		return pkCombatting;
	}

	public void setPkCombatting(Pokemon pkCombatting) {
		this.pkCombatting = pkCombatting;
	}

	// Adds Pokemon to Pokemon player
	public void addPokemon(Pokemon pk) {
		this.pokemon.add(pk);
	}

	public Pokemon getPkFacing() {
		return pkFacing;
	}

	public void setPkFacing(Pokemon pkFacing) {
		this.pkFacing = pkFacing;
	}

	public boolean isForcedSwitchPokemon() {
		return forceSwitchPokemon;
	}

	public void setForcedSwitchPokemon(boolean forceSwitchPokemon) {
		this.forceSwitchPokemon = forceSwitchPokemon;
	}

	// ==================================== METHODS
	// ====================================

	// -----------------------------
	// Prints the attacks of current Pokemon
	// -----------------------------
	public void printAttacksFromPokemonCombating() {
		List<Attack> attacksAvailable = this.getPkCombatting().getFourPrincipalAttacks().stream()
				.filter(a -> a.getPp() > 0).toList();

		for (Attack currentAttack : attacksAvailable) {
			System.out.println(currentAttack.getId() + " - " + currentAttack.getName() + " - " + currentAttack.getType()
					+ " -  PP : " + currentAttack.getPp());
		}
	}

	// -----------------------------
	// Prints all the info from all the Pokemon player
	// -----------------------------
	public void printPokemonInfo() {
		// Get only Pokemon not debilitated
		List<Pokemon> pokemonAvailable = this.getPokemon().stream()
				.filter(pk -> pk.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED).toList();

		for (Pokemon pk : pokemonAvailable) {
			System.out.println(pk.getId() + " - " + pk.getName() + " - tipo(s) : ");

			for (PokemonType pkT : pk.getTypes())
				System.out.println(pkT.getName());

			for (Attack currentAttacks : pk.getFourPrincipalAttacks())
				System.out.println(currentAttacks.getName() + " - " + currentAttacks.getType());

			System.out.println("---------------");
		}
	}

	// -----------------------------
	// Check if player has available Pokemon to change
	// -----------------------------
	public boolean hasAvailableSwitch() {
		// Get available Pokemon from current player
		List<Pokemon> options = this.getPokemon().stream().filter(pk -> pk != this.getPkCombatting())
				.filter(pk -> pk.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED).toList();

		if (options.isEmpty())
			return false; // If no more Pokemon remaining, attack fails

		return true;
	}
}
