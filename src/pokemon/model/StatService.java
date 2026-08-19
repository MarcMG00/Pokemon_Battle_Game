package pokemon.model;

import pokemon.enums.StatType;
import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;

public class StatService {
	// -----------------------------
	// Get attack stage for normal attack
	// -----------------------------
	public float getEffectiveAttack(Pokemon attacker, boolean ignoreStage, Weather weather) {
		int stage = attacker.getAttackStage();
		float multiplier;
		float attack = attacker.getAttack();

		// Reduces current damage by 50% (only if doesn't activate abilities with status
		// conditions rules)
		if (attacker.hasActiveStatusCondition(StatusConditions.BURNED) && !attacker.hasGutsAbility())
			attack /= 2f;

		// 55_Hustle ability rises attack by 50%
		if (attacker.hasHustleAbility() && attacker.getNextMovement().getBases().contains("fisico")) {
			attack *= 1.5f;
			System.out.println(attacker.getName() + " aumentó su ataque gracias a su habilidad "
					+ attacker.getAbilitySelected().getName());
		}

		// 62_Guts ability rises attack by 50%
		if (attacker.hasGutsAbility() && (attacker.hasStatusCondition() || attacker.hasEphemeralStatus())) {
			attack *= 1.5f;
			System.out.println(attacker.getName() + " aumentó su ataque gracias a su habilidad Agallas");
		}

		// 122_Flower_Gift ability increases attack by 50%
		if (weather == Weather.SUN && attacker.hasFlowerGiftAbility()) {
			attack *= 1.5f;
			System.out.println(attacker.getName() + " aumentó su ataque gracias a su habilidad Don Floral");
		}

		// 129_Deafeatist ability reduces attack by 50% if PS under 50% of initial PS
		if (attacker.isDefeatistActive())
			attack /= 2f;

		// 137_Toxic_boost rises attack by 50% if attacker is poisoned
		if (attacker.hasToxicBoostAbility() && (attacker.hasActiveStatusCondition(StatusConditions.POISONED)
				|| attacker.hasActiveStatusCondition(StatusConditions.BADLY_POISONED))) {
			attack *= 1.5f;
			System.out.println(attacker.getName() + " aumentó su ataque gracias a su habilidad Impetu Tóxico");
		}

		if (!ignoreStage) {
			if (stage >= 0)
				multiplier = (2f + stage) / 2f;
			else
				multiplier = 2f / (2f - stage);
		} else
			multiplier = 1f;

		return attack * multiplier;
	}

	// -----------------------------
	// Get effective special attack
	// -----------------------------
	public float getEffectiveSpecialAttack(Pokemon attacker, boolean ignoreStage, Weather weather) {
		int stage = attacker.getSpecialAttackStage();
		float multiplier;
		float specialAttack = attacker.getSpecialAttack();

		// 57_Plus ability
		if (attacker.hasPlusAbility()
				&& attacker.getOwner().getPokemon().stream().anyMatch(pok -> pok.hasMinusAbility()))
			specialAttack *= 1.5f;

		// 58_Minus ability
		if (attacker.hasMinusAbility()
				&& attacker.getOwner().getPokemon().stream().anyMatch(pok -> pok.hasPlusAbility()))
			specialAttack *= 1.5f;

		// 94_Solar_Power increases special attack by 50%
		if (weather == Weather.SUN && attacker.hasSolarPowerAbility()) {
			specialAttack *= 1.5f;
			System.out.println(attacker.getName() + " aumentó su ataque especial gracias a su habilidad Poder solar");
		}

		// 129_Deafeatist ability reduces special attack by 50% if PS under 50% of
		// initial PS
		if (attacker.isDefeatistActive())
			specialAttack /= 2f;

		// 138_Flare_boost rises special attack by 50% if attacker is burned
		if (attacker.hasFlareBoostAbility() && attacker.hasActiveStatusCondition(StatusConditions.BURNED)) {
			specialAttack *= 1.5f;
			System.out.println(attacker.getName() + " aumentó su ataque gracias a su habilidad Impetu Ardiente");
		}

		if (!ignoreStage) {
			if (stage >= 0)
				multiplier = (2f + stage) / 2f;
			else
				multiplier = 2f / (2f - stage);
		} else
			multiplier = 1f;

		return specialAttack * multiplier;
	}

	// -----------------------------
	// Get effective defense
	// -----------------------------
	public float getEffectiveDefense(Pokemon pk, boolean ignoreStage) {
		int stage = pk.getDefenseStage();
		float multiplier;
		float defense = pk.getDef();

		// 63_Marvel_Scale => if has any status condition or ephemeral status, rises
		// defense
		if (pk.hasMarvelScaleAbility() && (!pk.hasStatusCondition() || !pk.hasEphemeralStatus())) {
			defense *= 1.5f;
			System.out.println(pk.getName() + " aumentó su defensa gracias a su habilidad Escama especial");
		}

		if (!ignoreStage) {
			if (stage >= 0)
				multiplier = (2f + stage) / 2f;
			else
				multiplier = 2f / (2f - stage);
		} else
			multiplier = 1f;

		return defense * multiplier;
	}

	// -----------------------------
	// Get effective special defense
	// -----------------------------
	public float getEffectiveSpecialDefense(Pokemon pk, boolean ignoreStage, Weather weather) {
		int stage = pk.getSpecialDefenseStage();
		float multiplier;
		float specialDefense = pk.getSpecialDefense();

		// 122_Flower_Gift ability increases special defense by 50%
		if (weather == Weather.SUN && pk.hasFlowerGiftAbility()) {
			specialDefense *= 1.5f;
			System.out.println(pk.getName() + " aumentó su defensa especial gracias a su habilidad Don Floral");
		}

		if (!ignoreStage) {
			if (stage >= 0)
				multiplier = (2f + stage) / 2f;
			else
				multiplier = 2f / (2f - stage);
		} else
			multiplier = 1f;

		return specialDefense * multiplier;
	}

	// -----------------------------
	// Get effective precision
	// -----------------------------
	public int getEffectivePrecision(Pokemon pk, boolean ignoreStage) {
		if (ignoreStage)
			return 0;

		int stage = pk.getPrecisionStage();
		int precisionPoints = stage > 0 ? stage : 0;

		return precisionPoints;
	}

	// -----------------------------
	// Get effective evasion
	// -----------------------------
	public int getEffectiveEvasion(Pokemon pk, boolean ignoreStage) {
		if (ignoreStage)
			return 0;

		int stage = pk.getEvasionStage();
		int evasionPoints = stage > 0 ? stage : 0;

		return evasionPoints;
	}

	// -----------------------------
	// Get effective speed (only start of the turn)
	// -----------------------------
	public float getEffectiveSpeed(Pokemon pk) {
		int stage = pk.getSpeedStage();
		float multiplier;
		float speed = pk.getSpeed();

		if (pk.hasActiveStatusCondition(StatusConditions.PARALYZED)) {
			// 95_Quick_Feet increase doesn't apply reduction of speed (continues with the
			// 50% increased)
			if (!pk.hasQuickFeetAbility())
				// Modifies speed of Pokemon (reduces by 50%)
				speed *= 0.5f;
		}

		// 95_Quick_Feet increase speed by 50%
		if (pk.hasQuickFeetAbility() && (pk.hasStatusCondition() || pk.hasEphemeralStatus()))
			speed *= 1.5f;

		if (stage >= 0)
			multiplier = (2f + stage) / 2.0f;
		else
			multiplier = 2.0f / (2f - stage);

		return speed * multiplier;
	}

	// -----------------------------
	// Modify stat stage from rival attacks
	// -----------------------------
	public void reduceStatStage(Pokemon defender, StatType stat, int stages, boolean isMistEffectActivated) {
		boolean isReduceStatStage = true;

		// 126_Contrary ability reverse the increase or reduce stat stage
		if (defender.hasContraryAbility())
			isReduceStatStage = false;

		if (isStatDropImmune(defender, stat))
			return;

		if (isMistEffectActivated) {
			System.out.println(defender.getName() + " (Id:" + defender.getId() + ")"
					+ " no pudo bajar las estadísticas gracias a Neblina");
			return;
		}

		if (defender.getStage(stat) <= -6) {
			System.out.println(
					stat + " de " + defender.getName() + " (Id:" + defender.getId() + ")" + " no puede bajar más!");
			return;
		}

		stages *= applyModifiersNbStage(defender, isReduceStatStage);
		defender.setStageValueStats(stat, stages, isReduceStatStage);

		System.out
				.println(isReduceStatStage ? defender.getName() + " (Id:" + defender.getId() + ")" + " bajó su " + stat
						: defender.getName() + " (Id:" + defender.getId() + ")" + " aumentó su " + stat);

		// 128_Defiant ability increases by 2 the attack for each stat reduced
		if (isReduceStatStage && defender.hasDefiantAbility()) {
			if (defender.getStage(StatType.ATTACK) < 6) {
				defender.setStageValueStats(StatType.ATTACK, 2, false);
				System.out.println(defender.getName() + " aumentó mucho su ataque gracias a su habilidad Competitivo");
			}
		}
	}

	// -----------------------------
	// Return the number of stages to modify the stat
	// -----------------------------
	public int applyModifiersNbStage(Pokemon pk, boolean isStatDrop) {
		// 86_Simple ability duplicates by 2 the stage (whether it's negative or
		// positive)
		if (pk.hasSimpleAbility()) {
			System.out.println(pk.getName() + " (Id:" + pk.getId() + ") " + (isStatDrop ? "bajó" : "subió")
					+ " el doble, dada su habilidad " + pk.getAbilitySelected().getName());
			return 2;
		}
		return 1;
	}

	// -----------------------------
	// Check if can drop stats
	// -----------------------------
	public boolean isStatDropImmune(Pokemon pk, StatType stat) {
		// 29_Clear_Body / 73_White_Smoke abilities cannot be reduced stats
		if (pk.hasClearBodyAbility() || pk.hasWhiteSmokeAbility())
			return true;

		switch (stat) {
		case ATTACK:
			return pk.hasHyperCutterAbility();

		case PRECISION:
			return pk.hasKeenEyeAbility() || pk.hasIlluminateAbility();

		default:
			return false;
		}
	}

	// -----------------------------
	// Check if can be intimidated
	// -----------------------------
	public boolean isIntimidateImmune(Pokemon pk) {
		return pk.hasObliviousAbility() || pk.hasOwnTempoAbility() || pk.hasClearBodyAbility()
				|| pk.hasInnerFocusAbility();
	}
}
