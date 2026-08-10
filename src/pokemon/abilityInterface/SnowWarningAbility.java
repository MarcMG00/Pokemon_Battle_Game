package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class SnowWarningAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
		if (battleCtx.getWeather() == Weather.HAIL) {
			System.out.println(owner.getName() + " invocó un granizo con Nevada! - pero ya está granizando");
			return;
		}

		battleCtx.setWeather(Weather.HAIL);

		System.out.println(owner.getName() + " invocó un granizo con Nevada!");
	}
}
