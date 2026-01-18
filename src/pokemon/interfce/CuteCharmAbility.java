package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class CuteCharmAbility implements AbilityEffect {
	private static final double INFATUEATED_CHANCE = 0.30;

	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, Weather weather, boolean isWeatherSuppressed) {

		if (defender.getEphemeralStates().stream()
				.anyMatch(e -> e.getStatusCondition() == StatusConditions.INFATUATED)) {
			return;
		}

		// Attack must make contact
		if (!attack.getMakesContact())
			return;

		// Probability
		if (Math.random() >= INFATUEATED_CHANCE) {
			return;
		}

		// Try to apply infatuated
		if (!attacker.trySetEphemeralStatus(StatusConditions.INFATUATED, attack))
			return;

		int nbTurnsHoldingStatus = 1 + (int) (Math.random() * (7 - 1 + 1));

		State infatuated = new State(StatusConditions.INFATUATED, nbTurnsHoldingStatus + 1);

		defender.addEphemeralState(infatuated);

		System.out.println(attacker.getName() + " cayó enamorado por la habilidad Gran encanto del Pokémon rival");
	}
}
