package pokemon.interfce;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class RoughSkinAbility implements AbilityEffect {
	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, Weather weather, boolean isWeatherSuppressed) {

		// Attack must make contact
		if (!attack.getMakesContact())
			return;

		// Return damage to attacker
		float attackerInitialPs = attacker.getInitialPs();
		// Removes 6,25% of initial PS
		float damage = attackerInitialPs * (1f - 0.625f);
		attacker.setPs(attacker.getPs() - damage);

		System.out.println(attacker.getName() + " fue dañado por la habilidad Piel tosca del Pokémon rival");
	}
}
