package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.model.Ability;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class TraceAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(Game game, Pokemon owner, Pokemon defender) {

		if (defender.getAbilitySelected().getId() == 1) {
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

		System.out.println(owner.getName() + " copió la habilidad " + defender.getAbilitySelected().getName() + " de "
				+ defender.getName() + " dada su habilidad Calco");

		// Applies immediately abilities that are onSwitchIn or startBattle
		if ((owner.hasActiveEphemeralStatus(StatusConditions.ASLEEP))) {
			// 72_Vital_Spirit forbid to get asleep, so wake up instantly
			if (defender.getAbilitySelected().getId() == 72) {
				owner.removeEphemeralStatus(StatusConditions.ASLEEP);

				System.out.println(owner.getName() + " se despertó gracias a la habilidad copiada "
						+ defender.getAbilitySelected().getName());
			}
		}
		owner.getAbilitySelected().getEffect().onBattleStart(game, owner);
		owner.getAbilitySelected().getEffect().onSwitchIn(game, owner, defender);

	}

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {
		System.out.println(owner.getName() + " dejó de copiar la habilidad del rival");
		owner.setAbilitySelected(new Ability(owner.getBaseAbility()));
	}
}
