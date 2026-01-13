package pokemon.interfce;

import pokemon.enums.Weather;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class DrizzleAbility implements AbilityEffect {
	@Override
	public void onBattleStart(Game game, Pokemon owner) {

		if (game.getCurrentWeather() == Weather.RAIN) {
			System.out.println(owner.getName() + " invocó la lluvia con Llovizna! - pero ya está lloviendo");
			return;
		}

		game.setCurrentWeather(Weather.RAIN);

		System.out.println(owner.getName() + " invocó la lluvia con Llovizna!");
	}
}
