package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class FlashFireAbility implements AbilityEffect {
	@Override
	public boolean beforeDamage(Game game, Pokemon attacker, Pokemon defender, Attack attack) {

		// Only fire movements
		if (attack.getStrTypeToPkType().getId() != 7) {
			return true;
		}

		// If Pokemon frozen => don't activate the ability
		if (defender.getStatusCondition().getStatusCondition() == StatusConditions.FROZEN) {
			return true;
		}

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + attack.getName());

		// If already has the boost => only immunity (no cumulative)
		if (defender.getIsFireBoostActive()) {
			System.out.println(defender.getName() + " absorbió el ataque de fuego!");
			return false; // cannot be attacked
		}

		// First fire attack => activate the ability
		defender.setIsFireBoostActive(true);
		System.out.println(defender.getName() + " activó Absorbe Fuego!");

		return false; // cannot be attacked
	}

	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, Weather weather, boolean isWeatherSuppressed) {
		// If Pokemon frozen => don't activate the ability
		if (defender.getStatusCondition().getStatusCondition() == StatusConditions.FROZEN) {
			defender.setStatusCondition(new State(StatusConditions.NO_STATUS));
			System.out.println(defender.getName() + " se descongeló!");
		}
	}

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {
		// Reinitialize the activation of ability
		owner.setIsFireBoostActive(false);
	}
}
