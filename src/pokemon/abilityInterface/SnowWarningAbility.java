package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class SnowWarningAbility extends AbilityEffect {
	public SnowWarningAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		if (battleCtx.getWeather() == Weather.HAIL) {
			System.out.println(owner.getName() + " invocó un granizo con Nevada! - pero ya está granizando");
			return;
		}

		battleCtx.setWeather(Weather.HAIL);

		System.out.println(owner.getName() + " invocó un granizo con Nevada!");
	}
}
