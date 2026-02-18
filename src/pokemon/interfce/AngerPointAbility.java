package pokemon.interfce;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class AngerPointAbility implements AbilityEffect {
	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {

		// How to check if got a critic attack ?
		// 1️ - The attack has to retreat defender
		if (!isACriticAttack || defender.getAttackStage() >= 6)
			return;

		// Puts max stage
		defender.setAttackStage(6);
		System.out.println(defender.getName() + " aumentó su ataque al máximo gracias a su habilidad Irascible");
	}
}
