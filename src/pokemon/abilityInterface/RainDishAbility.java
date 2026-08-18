package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class RainDishAbility extends AbilityEffect {
	public RainDishAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void endOfTurn(BattleContext battleCtx) {
		if (battleCtx.getWeather() != Weather.RAIN)
			return;

		if (owner.hasMaxPS())
			return;
		else {
			// Rises current PS by 6.25%
			float incrementPs = owner.getInitialPs() * 0.0625f;

			owner.setPs(Math.min(owner.getPs() + incrementPs, owner.getInitialPs()));
			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")"
					+ " recuperó algo de PS gracias a su habilidad Cura lluvia");
		}
	}
}
