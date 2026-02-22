package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class HydratationAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(Game game, Pokemon owner, Pokemon defender) {
		if (game.getCurrentWeather() != Weather.RAIN)
			return;

		// Remove status condition and ephemeral status from Pokemon leaving
		if (owner.getStatusCondition().getStatusCondition() != StatusConditions.NO_STATUS
				|| !owner.getEphemeralStates().isEmpty()) {
			owner.setStatusCondition(new State(StatusConditions.NO_STATUS));
			owner.getEphemeralStates().clear();

			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")"
					+ " se curó de todos sus problemas de estado gracias a su habilidad Cura lluvia");
		}
	}

	@Override
	public void endOfTurn(Game game, Pokemon owner) {
		if (game.getCurrentWeather() != Weather.RAIN)
			return;

		// Remove status condition and ephemeral status from Pokemon leaving
		if (owner.getStatusCondition().getStatusCondition() != StatusConditions.NO_STATUS
				|| !owner.getEphemeralStates().isEmpty()) {
			owner.setStatusCondition(new State(StatusConditions.NO_STATUS));
			owner.getEphemeralStates().clear();

			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")"
					+ " se curó de todos sus problemas de estado gracias a su habilidad Cura lluvia");
		}
	}
}
