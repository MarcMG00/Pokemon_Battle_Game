package pokemon.interfce;

import pokemon.enums.Weather;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class DroughtAbility implements AbilityEffect {
	@Override
	public void onBattleStart(Game game, Pokemon owner) {

		if (game.getCurrentWeather() == Weather.SUN) {
			System.out.println(owner.getName() + " invocó Día soleado con Sequía! - pero ya hay sol");
			return;
		}

		game.setCurrentWeather(Weather.SUN);

		System.out.println(owner.getName() + " invocó Día soleado con Sequía!");
	}

	@Override
	public void onSwitchIn(Game game, Pokemon owner, Pokemon defender) {

		if (game.getCurrentWeather() == Weather.SUN) {
			System.out.println(owner.getName() + " invocó Día soleado con Sequía! - pero ya hay sol");
			return;
		}

		game.setCurrentWeather(Weather.SUN);

		System.out.println(owner.getName() + " invocó Día soleado con Sequía!");
	}
}
