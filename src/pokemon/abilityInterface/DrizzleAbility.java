package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class DrizzleAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
		if (battleCtx.getWeather() == Weather.RAIN) {
			System.out.println(owner.getName() + " invocó la lluvia con Llovizna! - pero ya está lloviendo");
			return;
		}

		battleCtx.setWeather(Weather.RAIN);

		System.out.println(owner.getName() + " invocó la lluvia con Llovizna!");
	}
}
