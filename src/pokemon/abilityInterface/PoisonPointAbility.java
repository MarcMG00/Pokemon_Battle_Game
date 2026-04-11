package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class PoisonPointAbility implements AbilityEffect {
	private static final double POISONED_CHANCE = 0.30;

	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
		if (attacker.hasActiveStatusCondition(StatusConditions.POISONED))
			return;

		// Attack must make contact
		if (!attack.getMakesContact())
			return;

		// Probability
		if (Math.random() >= POISONED_CHANCE)
			return;

		// Try to apply poison
		battleCtx.getStatusService().trySetStatus(attacker, new State(StatusConditions.POISONED), null, false, attack);
		System.out.println(attacker.getName() + " fue envenenado por la habilidad punto tóxico del Pokémon rival");
	}
}
