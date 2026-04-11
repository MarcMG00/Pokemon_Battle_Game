package pokemon.abilityInterface;

import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class CloudNineAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
		battleCtx.setWeatherSuppressed(true);
		System.out.println(owner.getName() + " anuló los efectos del clima con Aclimatación");
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx, Pokemon owner) {
		battleCtx.setWeatherSuppressed(false);
		System.out.println("Los efectos del clima vuelven a la normalidad");
	}
}
