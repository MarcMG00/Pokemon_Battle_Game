package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class IceBodyAbility implements AbilityEffect {
	@Override
	public void endOfTurn(BattleContext battleCtx, Pokemon owner) {
		if (battleCtx.getWeather() != Weather.HAIL)
			return;

		if (owner.getPs() >= owner.getInitialPs())
			return;
		else {
			// Rises current PS by 6.25%
			float incrementPs = owner.getInitialPs() * 0.0625f;

			owner.setPs(owner.getPs() + incrementPs);
			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")"
					+ " recuperó algo de PS gracias a su habilidad Gélido");
		}
	}
}
