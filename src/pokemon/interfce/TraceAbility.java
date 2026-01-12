package pokemon.interfce;

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

		if (owner.getBaseAbility().getId() != 36)
			return;

		Ability targetAbility = defender.getAbilitySelected();
		Ability AbilityDeepCopy = new Ability(targetAbility);

		owner.setAbilitySelected(AbilityDeepCopy);

		System.out
				.println(owner.getName() + " copió la habilidad de " + defender.getName() + " dada su habilidad Calco");

		// Applies immediately abilities that are onSwitchIn or startBattle
		owner.getAbilitySelected().getEffect().onBattleStart(game, owner);
		owner.getAbilitySelected().getEffect().onSwitchIn(game, owner, defender);

	}

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {
		System.out.println(owner.getName() + "dejó de copiar la habilidad del rival");
		owner.setAbilitySelected(new Ability(owner.getBaseAbility()));
	}
}
