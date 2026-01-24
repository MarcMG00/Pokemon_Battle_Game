package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class FlameBodyAbility implements AbilityEffect {
	private static final double BURNED_CHANCE = 0.30;

	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, Weather weather, boolean isWeatherSuppressed) {

		if (attacker.getStatusCondition().getStatusCondition() == StatusConditions.BURNED) {
			return;
		}

		// Attack must make contact
		if (!attack.getMakesContact())
			return;

		// Probability
		if (Math.random() >= BURNED_CHANCE) {
			return;
		}

		// Try to apply burned
		attacker.trySetStatus(new State(StatusConditions.BURNED), null, false, attack);
		System.out.println(attacker.getName() + " fue quemado por la habilidad Cuerpo llama del Pokémon rival");
	}
}
