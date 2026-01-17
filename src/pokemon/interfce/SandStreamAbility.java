package pokemon.interfce;

import pokemon.enums.Weather;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class SandStreamAbility implements AbilityEffect {
	@Override
	public void onBattleStart(Game game, Pokemon owner) {

		if (game.getCurrentWeather() == Weather.SANDSTORM) {
			System.out.println(owner.getName() + " invocó una tormenta de arena - pero ya hay una");
			return;
		}

		game.setCurrentWeather(Weather.SANDSTORM);

		System.out.println(owner.getName() + " invocó una tormenta de arena con Chorro arena!");
	}
	
	@Override
	public void onSwitchIn(Game game, Pokemon owner, Pokemon defender) {

		if (game.getCurrentWeather() == Weather.SANDSTORM) {
			System.out.println(owner.getName() + " invocó una tormenta de arena - pero ya hay una");
			return;
		}

		game.setCurrentWeather(Weather.SANDSTORM);

		System.out.println(owner.getName() + " invocó una tormenta de arena con Chorro arena!");
	}
}
