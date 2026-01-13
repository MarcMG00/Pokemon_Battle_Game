package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class EffectSporeAbility implements AbilityEffect {
	private static final double STATUS_PROBABILITY = 0.10d;

	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch) {

		// Attack must make contact
		if (!attack.getMakesContact())
			return;

		// Attacker must hace ability 142_Overcoat
		if (attacker.getAbilitySelected().getId() == 142) {
			System.out.println(
					attacker.getName() + " no puede sufrir efectos de Efecto espora (dada su habilidad Funda)");
			return;
		}

		// Asleep status
		if (Math.random() <= STATUS_PROBABILITY) {
			// Check if the attacker doesn't have the status Asleep (is a status that
			// can be accumulated with other ephemeral status)
			if (!(attacker.getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.ASLEEP))) {
				System.out.println(attacker.getName() + " fue dormido por la habilidad Efecto espora");

				int nbTurnsHoldingStatus;

				nbTurnsHoldingStatus = 1 + (int) (Math.random() * (7 - 1 + 1));

				System.out.println(
						attacker.getName() + " cayó en un sueño profundo por " + nbTurnsHoldingStatus + " turnos");

				State asleep = new State(StatusConditions.ASLEEP, nbTurnsHoldingStatus + 1);

				attacker.addEphemeralState(asleep);
			}
			return;
		}

		// Poisoned status
		if (Math.random() <= STATUS_PROBABILITY) {
			// Already has a status
			if (attacker.getStatusCondition().getStatusCondition() != StatusConditions.NO_STATUS)
				return;

			System.out.println(attacker.getName() + " fue envenenado por la habilidad Efecto espora");
			attacker.setStatusCondition(new State(StatusConditions.POISONED));
			return;
		}

		// Paralyzed status
		if (Math.random() <= STATUS_PROBABILITY) {
			// Already has a status
			if (attacker.getStatusCondition().getStatusCondition() != StatusConditions.NO_STATUS)
				return;

			System.out.println(attacker.getName() + " fue paralizado por la habilidad Efecto espora");
			attacker.setStatusCondition(new State(StatusConditions.PARALYZED));
			return;
		}
	}
}
