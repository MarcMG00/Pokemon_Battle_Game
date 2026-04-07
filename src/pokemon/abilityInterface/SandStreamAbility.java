package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class SandStreamAbility implements AbilityEffect {
	@Override
	public void onBattleStart(BattleContext battleCtx, Pokemon owner) {
		if (battleCtx.getWeather() == Weather.SANDSTORM) {
			System.out.println(owner.getName() + " invocó una tormenta de arena - pero ya hay una");
			return;
		}

		battleCtx.setWeather(Weather.SANDSTORM);

		System.out.println(owner.getName() + " invocó una tormenta de arena con Chorro arena!");
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
		if (battleCtx.getWeather() == Weather.SANDSTORM) {
			System.out.println(owner.getName() + " invocó una tormenta de arena - pero ya hay una");
			return;
		}

		battleCtx.setWeather(Weather.SANDSTORM);

		System.out.println(owner.getName() + " invocó una tormenta de arena con Chorro arena!");
	}
}
