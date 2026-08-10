package pokemon.model;

import pokemon.enums.StatType;
import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;

public class StatService {
	// -----------------------------
	// Get attack stage for normal attack
	// -----------------------------
	public float getEffectiveAttack(Pokemon pk, boolean ignoreStage, Weather weather) {
		int stage = pk.getAttackStage();
		float multiplier;
		float attack = pk.getAttack();

		if (pk.hasActiveStatusCondition(StatusConditions.BURNED))
			// Reduces current damage by 50%
			attack /= 2f;

		// 55_Hustle ability rises attack by 50%
		if (pk.hasHustleAbility() && pk.getNextMovement().getBases().contains("fisico")) {
			attack *= 1.5f;
			System.out.println(
					pk.getName() + " aumentó su ataque gracias a su habilidad " + pk.getAbilitySelected().getName());
		}

		// 62_Guts ability rises attack by 50%
		if (pk.hasGutsAbility() && (pk.hasStatusCondition() || pk.hasEphemeralStatus())) {
			attack *= 1.5f;
			System.out.println(pk.getName() + " aumentó su ataque gracias a su habilidad Agallas");
		}

		// 122_Flower_Gift ability increases attack by 50%
		if (weather == Weather.SUN && pk.hasFlowerGiftAbility()) {
			attack *= 1.5f;
			System.out.println(pk.getName() + " aumentó su ataque gracias a su habilidad Don Floral");
		}

		// 129_Deafeatist ability reduces attack by 50% if PS under 50% of initial PS
		if (pk.isDefeatistActive())
			attack /= 2f;

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
	public float getEffectiveSpecialAttack(Pokemon pk, boolean ignoreStage, Weather weather) {
		int stage = pk.getSpecialAttackStage();
		float multiplier;
		float specialAttack = pk.getSpecialAttack();

		// 57_Plus ability
		if (pk.hasPlusAbility() && pk.getOwner().getPokemon().stream().anyMatch(pok -> pok.hasMinusAbility()))
			specialAttack *= 1.5f;

		// 58_Minus ability
		if (pk.hasMinusAbility() && pk.getOwner().getPokemon().stream().anyMatch(pok -> pok.hasPlusAbility()))
			specialAttack *= 1.5f;

		// 94_Solar_Power increases special attack by 50%
		if (weather == Weather.SUN && pk.hasSolarPowerAbility()) {
			specialAttack *= 1.5f;
			System.out.println(pk.getName() + " aumentó su ataque especial gracias a su habilidad Poder solar");
		}

		// 129_Deafeatist ability reduces special attack by 50% if PS under 50% of
		// initial PS
		if (pk.isDefeatistActive())
			specialAttack /= 2f;

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
