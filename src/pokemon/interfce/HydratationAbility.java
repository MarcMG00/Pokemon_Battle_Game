package pokemon.interfce;

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
		if (owner.hasStatusCondition() || owner.hasEphemeralStatus()) {
			owner.setStatusCondition(new State());
			owner.getEphemeralStatuses().clear();

			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")"
					+ " se curó de todos sus problemas de estado gracias a su habilidad Cura lluvia");
		}
	}

	@Override
	public void endOfTurn(Game game, Pokemon owner) {
		if (game.getCurrentWeather() != Weather.RAIN)
			return;

		// Remove status condition and ephemeral status from Pokemon leaving
		if (owner.hasStatusCondition() || owner.hasEphemeralStatus()) {
			owner.setStatusCondition(new State());
			owner.getEphemeralStatuses().clear();

			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")"
					+ " se curó de todos sus problemas de estado gracias a su habilidad Cura lluvia");
		}
	}
}
