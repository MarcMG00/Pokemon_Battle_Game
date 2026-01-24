package pokemon.interfce;

import pokemon.enums.Weather;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class RainDishAbility implements AbilityEffect {
	@Override
	public void endOfTurn(Game game, Pokemon owner) {

		if (game.getCurrentWeather() != Weather.RAIN)
			return;

		if (owner.getPs() >= owner.getInitialPs()) {
			return;
		} else {
			// Reduces current PS by 6.25%
			float incrementPs = owner.getInitialPs() * 0.0625f;

			owner.setPs(owner.getPs() + incrementPs);
			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")" + " recuperó algo de PS gracias a su habilidad Cura lluvia");
		}
	}
}
