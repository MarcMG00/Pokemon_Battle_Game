package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.State;

public class EffectSporeAbility implements AbilityEffect {
	private static final double STATUS_PROBABILITY = 0.10d;

	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		if (!attackCtx.getDefender().hasEffectSporeAbility())
			return true;

		if (attackCtx.getAttacker().hasStatusCondition())
			return true;

		// Attack must make contact
		if (!attackCtx.getAttack().makesContact() || attackResult.getDamage() <= 0f)
			return true;

		// Attacker must have ability 142_Overcoat
		if (attackCtx.getAttacker().hasOvercoatAbility()) {
			System.out.println(attackCtx.getAttacker().getName()
					+ " no puede sufrir efectos de Efecto espora (dada su habilidad Funda)");
			return true;
		}

		// ASLEEP status
		if (Math.random() <= STATUS_PROBABILITY) {
			// Check if the attacker doesn't have the status Asleep (is a status that
			// can be accumulated with other ephemeral status)
			System.out.println(attackCtx.getAttacker().getName() + " fue dormido por la habilidad Efecto espora");

			int nbTurnsHoldingStatus;

			nbTurnsHoldingStatus = 1 + (int) (Math.random() * (7 - 1 + 1));

			System.out.println(attackCtx.getAttacker().getName() + " cayó en un sueño profundo por "
					+ nbTurnsHoldingStatus + " turnos");

			State asleep = new State(StatusConditions.ASLEEP, nbTurnsHoldingStatus + 1);

			attackCtx.getAttacker().addEphemeralStatus(StatusConditions.ASLEEP, asleep);
			return true;
		}

		// Already has a status
		if (attackCtx.getAttacker().hasStatusCondition())
			return true;

		// POISONED status
		if (Math.random() <= STATUS_PROBABILITY) {
			System.out.println(attackCtx.getAttacker().getName() + " fue envenenado por la habilidad Efecto espora");
			attackCtx.getAttacker().setStatusCondition(new State(StatusConditions.POISONED));
			return true;
		}

		// Already has a status
		if (attackCtx.getAttacker().hasStatusCondition())
			return true;

		// PARALYZED status
		if (Math.random() <= STATUS_PROBABILITY) {
			System.out.println(attackCtx.getAttacker().getName() + " fue paralizado por la habilidad Efecto espora");
			attackCtx.getAttacker().setStatusCondition(new State(StatusConditions.PARALYZED));
			return true;
		}

		return true;
	}
}
