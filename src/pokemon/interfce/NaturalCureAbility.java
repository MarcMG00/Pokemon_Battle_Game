package pokemon.interfce;

import java.util.ArrayList;

import pokemon.enums.StatusConditions;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class NaturalCureAbility implements AbilityEffect {

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {
		// Remove status condition and ephemeral status from Pokemon leaving
		owner.setStatusCondition(new State(StatusConditions.NO_STATUS));
		owner.setEphemeralStates(new ArrayList<>());

		System.out
				.println("Todos los estados de " + owner.getName() + " se fueron gracias a su habilidad Cura natural");
	}
}
