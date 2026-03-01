package pokemon.interfce;

import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class NaturalCureAbility implements AbilityEffect {

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {
		// Remove status condition and ephemeral status from Pokemon leaving
		if (owner.hasStatusCondition() || owner.hasEphemeralStatus()) {
			owner.setStatusCondition(new State());
			owner.getEphemeralStatuses().clear();

			System.out.println(
					"Todos los estados de " + owner.getName() + " se fueron gracias a su habilidad Cura natural");
		}
	}
}
