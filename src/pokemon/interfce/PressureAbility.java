package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class PressureAbility implements AbilityEffect {
	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, Weather weather, boolean isWeatherSuppressed) {

		// Some conditions don't allow to do remove an extra PP (if charging an attack,
		// if trapped by own attack, etc.)
		if ((attacker.getNextMovement().getId() == 19 && attacker.getIsChargingAttackForNextRound())
				|| (attacker.getEphemeralStates().stream()
						.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPEDBYOWNATTACK))
				|| (attacker.getNextMovement().getId() == 76 && attacker.getIsChargingAttackForNextRound()))
			return;

		// Reduces by one more the PPs of the attacker
		attacker.getNextMovement().setPp(attacker.getNextMovement().getPp() - 1);
	}

	@Override
	public void onSwitchIn(Game game, Pokemon owner, Pokemon defender) {

		System.out.println(owner.getName() + " ejerce presión sobre " + defender.getName());
	}
}
