package pokemon.abilityInterface;

import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class AirLockAbility extends AbilityEffect {
	public AirLockAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		// Follows first Pokemon generation rules (only during Pokemon on battle)
		battleCtx.setWeatherSuppressed(true);
		System.out.println(owner.getName() + " anuló los efectos del clima con Esclusa de aire");
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx) {
		battleCtx.setWeatherSuppressed(false);
		System.out.println("Los efectos del clima vuelven a la normalidad");
	}
}
