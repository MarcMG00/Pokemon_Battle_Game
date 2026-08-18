package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class DroughtAbility extends AbilityEffect {
	public DroughtAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		if (battleCtx.getWeather() == Weather.SUN) {
			System.out.println(owner.getName() + " invocó Día soleado con Sequía! - pero ya hay sol");
			return;
		}

		battleCtx.setWeather(Weather.SUN);

		System.out.println(owner.getName() + " invocó Día soleado con Sequía!");
	}
}
