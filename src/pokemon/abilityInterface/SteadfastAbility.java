package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class SteadfastAbility implements AbilityEffect {
	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
		// 1️ - The attack has to retreat defender
		if (!defender.getHasRetreated())
			return;

		if (defender.getSpeedStage() >= 6) {
			System.out.println(defender.getName() + " no puede aumentar más su velocidad");
			return;
		}

		defender.setSpeedStage(Math.min(defender.getSpeedStage() + 1, 6));
		System.out.println(defender.getName() + " aumentó su velocidad gracias a su habilidad Impasible");
	}
}
