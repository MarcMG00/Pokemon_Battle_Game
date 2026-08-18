package pokemon.abilityInterface;

import pokemon.model.BattleContext;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class NaturalCureAbility extends AbilityEffect {
	public NaturalCureAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx) {
		// Remove status condition and ephemeral status from Pokemon leaving
		if (owner.hasStatusCondition() || owner.hasEphemeralStatus()) {
			owner.setStatusCondition(new State());
			owner.getEphemeralStatuses().clear();

			System.out.println(
					"Todos los estados de " + owner.getName() + " se fueron gracias a su habilidad Cura natural");
		}
	}
}
