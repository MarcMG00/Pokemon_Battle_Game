package pokemon.abilityInterface;

import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class PlusAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
		// Check if player has another Pokemon with "Minus" ability
		if (owner.getOwner().getPokemon().stream().anyMatch(pk -> pk.hasMinusAbility()))
			System.out.println("El ataque especial de " + owner.getName() + " aumentó gracias a "
					+ owner.getAbilitySelected().getName());

	}

	@Override
	public void onSwitchOut(BattleContext battleCtx, Pokemon owner) {
		// Reset special attack
		if (owner.getOwner().getPokemon().stream().anyMatch(pk -> pk.hasMinusAbility()))
			System.out.println("El ataque especial de " + owner.getName() + " volvió a la normalidad");
	}
}
