package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class CursedBodyAbility implements AbilityEffect {
	private static final double PROBABILITY = 0.30d;

	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {

		// Defender must have the current ability
		if (!defender.hasCursedBodyAbility())
			return;

		// Defender must have received damage
		if (dmg <= 0f)
			return;

		// Attacker must not have 165_Aroma_veil ability
		if (attacker.hasAromaVeilAbility())
			return;

		Attack lastAttack = attacker.getLastUsedAttack();
		// If rival hasn't used yet an attack => fails
		if (lastAttack == null || lastAttack.getId() == 0) {
			System.out.println(defender.getName() + " no pudo anular ningún ataque (habilidad Cuerpo maldito)");
			return;
		}

		// Only apply effect if don't have this one applied yet
		if (attacker.hasActiveEphemeralStatus(StatusConditions.DISABLE))
			return;

		if (Math.random() <= PROBABILITY) {
			State attackDisabled = new State(StatusConditions.DISABLE, 4 + 1); // 4 turns
			attackDisabled.setAttackDisabled(lastAttack);
			attacker.addEphemeralStatus(StatusConditions.DISABLE, attackDisabled);

			System.out.println(attacker.getName() + " no podrá usar " + lastAttack.getName()
					+ " por 4 turnos (habilidad Cuerpo maldito)");
		}
	}
}
