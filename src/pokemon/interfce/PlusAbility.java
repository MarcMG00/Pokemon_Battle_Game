package pokemon.interfce;

import pokemon.model.Game;
import pokemon.model.Pokemon;

public class PlusAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(Game game, Pokemon owner, Pokemon defender) {

		// Get player from Pokemon concerned (adds method on Pokemon ? => setPlayer...)
		// Check if player has another Pokemon with "Minus" ability
		// If true => rise special attack by 50%
		if (owner.getOwner().getPokemon().stream().anyMatch(pk -> pk.getAbilitySelected().getId() == 58)) {
			owner.setSpecialAttack(owner.getSpecialAttack() * 1.5f);
			System.out.println("El ataque especial de " + owner.getName() + " aumentó gracias a "
					+ owner.getAbilitySelected().getName());
		}

	}

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {

		// Reset special attack
		if (owner.getOwner().getPokemon().stream().anyMatch(pk -> pk.getAbilitySelected().getId() == 58)) {
			owner.setSpecialAttack(owner.getInitialSpecialAttack());
			System.out.println("El ataque especial de " + owner.getName() + " volvió a la normalidad");
		}
	}
}
