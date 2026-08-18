package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class SandStreamAbility extends AbilityEffect {
	public SandStreamAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		if (battleCtx.getWeather() == Weather.SANDSTORM) {
			System.out.println(owner.getName() + " invocó una tormenta de arena - pero ya hay una");
			return;
		}

		battleCtx.setWeather(Weather.SANDSTORM);

		System.out.println(owner.getName() + " invocó una tormenta de arena con Chorro arena!");
	}
}
