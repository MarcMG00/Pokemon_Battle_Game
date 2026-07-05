package pokemon.model;

import java.util.Optional;

public class MoveSelector {

	private MoveSelector() {
	}

	// -----------------------------
	// Chooses the best attack for the IA Pokemon
	// -----------------------------
	public static Attack chooseBestAttack(Pokemon attacker, Pokemon defender) {
		// If no PPs remaining in any attack => use Struggle
		boolean hasPP = attacker.getFourPrincipalAttacks().stream().anyMatch(a -> a.getPp() > 0);

		if (!hasPP)
			return getStruggleAttack(attacker);

		Attack bestEffectiveAttack = null;
		float bestScore = -1f;

		Attack bestNormalAttack = null;
		Attack bestOtherAttack = null;

		// Check all possible attacks
		for (Attack atk : attacker.getFourPrincipalAttacks()) {

			if (atk.getPp() <= 0)
				continue;

			float effectiveness = AttackAnalyzer.getEffectiveness(atk.getStrTypeToPkType(), defender);

			float stab = attacker.getTypes().contains(atk.getStrTypeToPkType()) ? 1.5f : 1f;

			float power = atk.getPower() > 0 ? atk.getPower() : 1f;

			float score = effectiveness * stab * power;

			// Save best super effective attack
			if (effectiveness > 1f && score > bestScore) {
				bestScore = score;
				bestEffectiveAttack = atk;
			}

			// Save a normal damaging attack
			if (effectiveness > 0f && !atk.getBases().contains("otros")) {

				if (bestNormalAttack == null) {
					bestNormalAttack = atk;
				}
			}

			// Save a status / other attack
			if (atk.getBases().contains("otros")) {

				if (bestOtherAttack == null) {
					bestOtherAttack = atk;
				}
			}
		}

		// Final decision
		if (bestEffectiveAttack != null)
			return bestEffectiveAttack;

		if (bestNormalAttack != null)
			return bestNormalAttack;

		if (bestOtherAttack != null)
			return bestOtherAttack;

		// Last case : any attack with PP
		return attacker.getFourPrincipalAttacks().stream().filter(a -> a.getPp() > 0).findFirst().orElse(null);
	}

	// -----------------------------
	// Returns Struggle (ID 165)
	// -----------------------------
	private static Attack getStruggleAttack(Pokemon pokemon) {
		return pokemon.getPhysicalAttacks().stream().filter(a -> a.getId() == 165).findFirst()
				.orElseThrow(() -> new IllegalStateException("Struggle attack (ID 165) not found."));
	}

	// -----------------------------
	// Prepares best attack for player
	// -----------------------------
	public void prepareBestAttackPlayer(Player owner, int attackId, Pokemon pokemonRival) {
		Optional<Attack> nextAttack = owner.getPkCombatting().getFourPrincipalAttacks().stream()
				.filter(a -> a.getId() == attackId).findFirst();

		if (nextAttack.isEmpty()) {
			return;
		}

		Attack atk = nextAttack.get();

		prepareAttack(atk, owner.getPkCombatting(), pokemonRival);

		owner.getPkCombatting().setNextMovement(atk);
	}

	// -----------------------------
	// Prepare best attack
	// -----------------------------
	private void prepareAttack(Attack atk, Pokemon attacker, Pokemon defender) {
		PokemonType attackType = atk.getStrTypeToPkType();

		// 1 - Real effectiveness
		float effectiveness = getEffectiveness(attackType, defender);
		atk.setEffectivenessAgainstPkFacing(effectiveness);

		// 2 - Stab
		float bonus = attacker.getTypes().contains(attackType) ? 1.5f : 1f;
		atk.setBonus(bonus);
	}

	// -----------------------------
	// Get effectiveness against Pokemon facing
	// -----------------------------
	public float getEffectiveness(PokemonType attackType, Pokemon defender) {
		float effectiveness = 1f;

		for (PokemonType defenderType : defender.getTypes()) {

			int defTypeId = defenderType.getId();

			if (attackType.getNoEffect().contains(defTypeId)) {
				return 0f; // immunity
			}

			if (attackType.getPktDoLotDamage().contains(defTypeId)) {
				effectiveness *= 2f;
			} else if (attackType.getPktDoLowDamage().contains(defTypeId)) {
				effectiveness *= 0.5f;
			}
		}

		return effectiveness;
	}
}
