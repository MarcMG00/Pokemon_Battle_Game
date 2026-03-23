package pokemon.abilityInterface;

import pokemon.model.Game;
import pokemon.model.Pokemon;

public class AirLockAbility implements AbilityEffect {
	@Override
	public void onBattleStart(Game game, Pokemon owner) {
		// Follows first Pokemon generation rules (only during Pokemon on battle)
		game.setIsWeatherSuppressed(true);
		System.out.println(owner.getName() + " anuló los efectos del clima con Esclusa de aire");
	}

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {
		game.setIsWeatherSuppressed(false);
		System.out.println("Los efectos del clima vuelven a la normalidad");
	}
}
