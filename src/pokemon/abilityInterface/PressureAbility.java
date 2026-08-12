package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class PressureAbility extends AbilityEffect {
	public PressureAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
		// Some conditions don't allow to do remove an extra PP (if charging an attack,
		// if trapped by own attack, etc.)
		if ((attacker.getNextMovement().isFly() && attacker.getIsChargingAttackForNextRound())
				|| (attacker.hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK))
				|| (attacker.getNextMovement().isSolarBeam() && attacker.getIsChargingAttackForNextRound()))
			return;

		// Reduces by one more the PPs of the attacker
		attacker.getNextMovement().setPp(attacker.getNextMovement().getPp() - 1);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		System.out.println(owner.getName() + " ejerce presión sobre " + defender.getName());
	}
}
