package pokemon.abilityInterface;

import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class RegeneratorAbility extends AbilityEffect {
	public RegeneratorAbility(Pokemon owner) {
		super(owner);
	}

	public void onSwitchOutCondition(BattleContext battleCtx) {
		if (owner.hasMaxPS())
			return;

		float regeneratePS = owner.getInitialPs() / 3f;

		owner.setPs(Math.min(owner.getPs() + regeneratePS, owner.getInitialPs()));

		System.out.println(owner.getName() + " recuperó PS gracias a la habilidad Regeneración");
	}
}
