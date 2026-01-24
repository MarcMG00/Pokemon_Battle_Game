package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class PoisonPointAbility implements AbilityEffect {
	private static final double POISONED_CHANCE = 0.30;

	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, Weather weather, boolean isWeatherSuppressed) {

		if (attacker.getStatusCondition().getStatusCondition() == StatusConditions.POISONED) {
			return;
		}

		// Attack must make contact
		if (!attack.getMakesContact())
			return;

		// Probability
		if (Math.random() >= POISONED_CHANCE) {
			return;
		}

		// Try to apply paralysis
		attacker.trySetStatus(new State(StatusConditions.POISONED), null, false, attack);
		System.out.println(attacker.getName() + " fue envenenado por la habilidad punto tóxico del Pokémon rival");
	}
}
