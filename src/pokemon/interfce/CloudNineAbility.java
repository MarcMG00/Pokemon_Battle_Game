package pokemon.interfce;

import pokemon.model.Game;
import pokemon.model.Pokemon;

public class CloudNineAbility implements AbilityEffect {
	@Override
	public void onBattleStart(Game game, Pokemon owner) {
		game.setIsWeatherSuppressed(true);
		System.out.println(owner.getName() + " anuló los efectos del clima con Aclimatación");
	}

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {
		game.setIsWeatherSuppressed(false);
		System.out.println("Los efectos del clima vuelven a la normalidad");
	}
}
