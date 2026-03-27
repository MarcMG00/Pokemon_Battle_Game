package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class HydratationAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
		if (battleCtx.getWeather() != Weather.RAIN)
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
	public void endOfTurn(BattleContext battleCtx, Pokemon owner) {
		if (battleCtx.getWeather() != Weather.RAIN)
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
