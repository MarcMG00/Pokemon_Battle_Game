package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.Ability;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class TraceAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
		if (defender.hasStenchAbility()) {
			System.out.println(owner.getName() + " no puede copiar la habilidad de " + defender.getName() + " : "
					+ defender.getAbilitySelected().getName());
			return;
		}

		// Allows to switch again with new ability
		if (owner.getBaseAbility().getId() != 36)
			return;

		Ability targetAbility = defender.getAbilitySelected();
		Ability AbilityDeepCopy = new Ability(targetAbility);

		owner.setAbilitySelected(AbilityDeepCopy);

		System.out
				.println(owner.getName() + " copió la habilidad de " + defender.getName() + " dada su habilidad Calco");

		// Applies immediately abilities that are onSwitchIn or startBattle
		if ((owner.hasActiveEphemeralStatus(StatusConditions.ASLEEP))) {
			// 72_Vital_Spirit forbid to get asleep, so wake up instantly
			if (defender.hasVitalSpiritAbility()) {
				owner.removeEphemeralStatus(StatusConditions.ASLEEP);

				System.out.println(owner.getName() + " se despertó gracias a la habilidad copiada "
						+ defender.getAbilitySelected().getName());
			}
		}
		owner.getAbilitySelected().getEffect().onSwitchIn(battleCtx, owner, defender);
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx, Pokemon owner) {
		System.out.println(owner.getName() + " dejó de copiar la habilidad del rival");
		owner.setAbilitySelected(new Ability(owner.getBaseAbility()));
	}
}
