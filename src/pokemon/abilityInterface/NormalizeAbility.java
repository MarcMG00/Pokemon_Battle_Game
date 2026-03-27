package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class NormalizeAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
		// Pass each attack to NORMAL type
		for (Attack attack : owner.getFourPrincipalAttacks()) {
			attack.setType("NORMAL");
			// Set the type of the attack to his Pokemon type instead of a string
			attack.transformStrTypeToPokemonType(battleCtx.getTypes());
		}

		System.out
				.println(owner.getName() + " transformó todos sus ataques a tipo NORMAL dada su habilidad Normalidad");
	}
}
