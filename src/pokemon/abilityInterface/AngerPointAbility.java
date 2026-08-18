package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class AngerPointAbility extends AbilityEffect {
	public AngerPointAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {

		// 1️ - The attack has to retreat defender
		if (!isACriticAttack || defender.getAttackStage() >= 6)
			return;

		// Puts max stage
		defender.setAttackStage(6);
		System.out.println(defender.getName() + " aumentó su ataque al máximo gracias a su habilidad Irascible");
	}
}
