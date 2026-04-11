package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class CuteCharmAbility implements AbilityEffect {
	private static final double INFATUEATED_CHANCE = 0.30;

	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {

		if (defender.getAbilitySelected().getId() != 56)
			return;

		if (defender.getSex() == attacker.getSex())
			return;

		if (attacker.hasActiveEphemeralStatus(StatusConditions.INFATUATED))
			return;

		// Attack must make contact
		if (!attack.getMakesContact())
			return;

		// Probability
		if (Math.random() >= INFATUEATED_CHANCE)
			return;

		int nbTurnsHoldingStatus = 1 + (int) (Math.random() * (7 - 1 + 1));
		State infatuated = new State(StatusConditions.INFATUATED, nbTurnsHoldingStatus + 1);
		
		// Try to apply infatuated
		battleCtx.getStatusService().trySetEphemeralStatus(infatuated, attacker, StatusConditions.INFATUATED, attack);

		System.out.println(attacker.getName() + " cayó enamorado por la habilidad Gran encanto del Pokémon rival");
	}
}
