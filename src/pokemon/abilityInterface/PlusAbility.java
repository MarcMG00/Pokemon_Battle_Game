package pokemon.abilityInterface;

import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class PlusAbility extends AbilityEffect {
	public PlusAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		// Check if player has another Pokemon with "Minus" ability => informative
		// message
		if (owner.getOwner().getPokemon().stream().anyMatch(pk -> pk.hasMinusAbility()))
			System.out.println("El ataque especial de " + owner.getName() + " aumentó gracias a "
					+ owner.getAbilitySelected().getName());

	}

	@Override
	public void onSwitchOut(BattleContext battleCtx) {
		// Reset special attack => informative message
		if (owner.getOwner().getPokemon().stream().anyMatch(pk -> pk.hasMinusAbility()))
			System.out.println("El ataque especial de " + owner.getName() + " volvió a la normalidad");
	}
}
