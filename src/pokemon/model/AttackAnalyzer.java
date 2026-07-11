package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import pokemon.enums.StatusConditions;

public final class AttackAnalyzer {
	private AttackAnalyzer() {
	}

	// -----------------------------
	// Adds 4 attacks to each Pokemon from player (1 other, 2 physicals, 1 special)
	// -----------------------------
	public static void addAttacksForEachPokemon(Player owner) {
		for (Pokemon pk : owner.getPokemon()) {
			System.out.println(pk.getName());

			// Struggle attack is only used as savior attack when no remaining PPs on other
			// attacks from Pokemon combating
			List<Attack> physicalAttacksWithoutStruggle = pk.getPhysicalAttacks().stream().filter(a -> a.getId() != 165)
					.toList();

			Random rand = new Random();
			pk.addAttacks(pk.getOtherAttacks().get(rand.nextInt(pk.getOtherAttacks().size())));

			for (int times = 0; times < 2; times++) {
				rand = new Random();
				pk.addAttacks(physicalAttacksWithoutStruggle.get(rand.nextInt(pk.getPhysicalAttacks().size())));
			}

			rand = new Random();
			pk.addAttacks(pk.getSpecialAttacks().get(rand.nextInt(pk.getSpecialAttacks().size())));

			System.out.println("fin PK");
		}
	}

	// -----------------------------
	// Order all the attacks by damage level against the Pokemon facing
	// -----------------------------
	public static void orderAttacksByDamage(Pokemon attacker, Pokemon defender,
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {
		// Gets the Pokemon types that player is currently facing
		ArrayList<PokemonType> pkFacing = defender.getTypes();

		// Copy all the effects for each type of the current Pokemon player
		HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypesCopy = new HashMap<>();

		// Vars that put the attacks by their level of damage
		ArrayList<Attack> iaLotDamageAttacks = new ArrayList<>();
		ArrayList<Attack> iaNormalDamageAttacks = new ArrayList<>();
		ArrayList<Attack> iaLowAttacks = new ArrayList<>();
		ArrayList<Attack> iaHasNoEffectAttacks = new ArrayList<>();

		// Puts the different types of the attacks in a list without duplicates
		ArrayList<PokemonType> noRepeatedAttackTypes = getUniqueAttackTypes(attacker);

		// Gets information about the type of Pokemon (different damages...)
		Map<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypesFiltered;

		// Types that doesn't hurt the Pokemon facing
		ArrayList<PokemonType> hasNoEffect = new ArrayList<>();

		// Vars to get the different information for each type of the Pokemon facing
		List<String> lotDamageRepeatedTypes = new ArrayList<>();
		List<String> normalDamageRepeatedTypes = new ArrayList<>();
		List<String> lowDamageRepeatedTypes = new ArrayList<>();

		// Filter all the damage information for each type of the Pokemon facing
		for (PokemonType facingType : pkFacing) {
			effectPerTypesFiltered = effectPerTypes.entrySet().stream()
					.filter(ef -> ef.getKey().equalsIgnoreCase(facingType.getName()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			effectPerTypesCopy.putAll(effectPerTypesFiltered);
		}

		// If Pokemon facing has 2 types
		if (effectPerTypesCopy.size() == 2) {
			fillDamageTypeLists(effectPerTypesCopy, noRepeatedAttackTypes, hasNoEffect, lotDamageRepeatedTypes,
					normalDamageRepeatedTypes, lowDamageRepeatedTypes);

			Map<String, Long> finalLotDammageRepeatedTypes = countDuplicates(lotDamageRepeatedTypes);
			Map<String, Long> finalLittleDammageRepeatedTypes = countDuplicates(lowDamageRepeatedTypes);

			for (Attack finalAttack : attacker.getFourPrincipalAttacks()) {
				boolean isPicked = false;

				if (hasNoEffect.contains(finalAttack.getStrTypeToPkType())
						&& !iaHasNoEffectAttacks.contains(finalAttack))
					iaHasNoEffectAttacks.add(finalAttack);
				else {
					// Affects both types strongly
					isPicked = addIfDoubleType(finalAttack, finalLotDammageRepeatedTypes, iaLotDamageAttacks);
					isPicked |= addIfDoubleType(finalAttack, finalLittleDammageRepeatedTypes, iaLowAttacks);

					// If not picked, apply single-type logic
					if (!isPicked) {
						if (finalLittleDammageRepeatedTypes
								.containsKey(finalAttack.getStrTypeToPkType().getName().toUpperCase())
								&& !iaLowAttacks.contains(finalAttack))
							iaLowAttacks.add(finalAttack);

						else if (finalLotDammageRepeatedTypes
								.containsKey(finalAttack.getStrTypeToPkType().getName().toUpperCase())
								&& normalDamageRepeatedTypes
										.contains(finalAttack.getStrTypeToPkType().getName().toUpperCase())
								&& !iaLotDamageAttacks.contains(finalAttack))
							iaLotDamageAttacks.add(finalAttack);

						else if (!iaNormalDamageAttacks.contains(finalAttack))
							iaNormalDamageAttacks.add(finalAttack);
					}
				}
			}
		}

		// If Pokemon facing has 1 type
		else {
			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> ef : effectPerTypesCopy.entrySet()) {
				ArrayList<PokemonType> rebientan = ef.getValue().get("Le rebientan");
				ArrayList<PokemonType> rebientanPoco = ef.getValue().get("Le Rebientan poco");

				for (Attack finalAttack : attacker.getFourPrincipalAttacks()) {
					PokemonType attackType = finalAttack.getStrTypeToPkType();

					if (rebientan != null && rebientan.contains(attackType) && !iaLotDamageAttacks.contains(finalAttack)
							&& !hasNoEffect.contains(attackType)) {

						iaLotDamageAttacks.add(finalAttack);

					} else if (rebientanPoco != null && rebientanPoco.contains(attackType)
							&& !iaLowAttacks.contains(finalAttack) && !hasNoEffect.contains(attackType)) {

						iaLowAttacks.add(finalAttack);

					} else if (hasNoEffect.contains(attackType)) {

						iaHasNoEffectAttacks.add(finalAttack);

					} else if (!iaNormalDamageAttacks.contains(finalAttack) && !hasNoEffect.contains(attackType)) {

						iaNormalDamageAttacks.add(finalAttack);
					}
				}
			}
		}

		// Sets all the attacks from the Pokemon by their level of damage
		attacker.setLotDamageAttacks(iaLotDamageAttacks);
		attacker.setNormalAttacks(iaNormalDamageAttacks);
		attacker.setLowAttacks(iaLowAttacks);
		attacker.setNoEffectAttacks(iaHasNoEffectAttacks);
	}

	// -----------------------------
	// Returns a list of unique PokemonType from the attacks of the current Pokemon
	// -----------------------------
	private static ArrayList<PokemonType> getUniqueAttackTypes(Pokemon attacker) {
		ArrayList<PokemonType> uniquePkType = new ArrayList<>();

		for (Attack atck : attacker.getFourPrincipalAttacks()) {
			if (!uniquePkType.contains(atck.getStrTypeToPkType()))
				uniquePkType.add(atck.getStrTypeToPkType());
		}

		return uniquePkType;
	}

	// -----------------------------
	// Add attacks that hit both types strongly or weakly
	// -----------------------------
	private static boolean addIfDoubleType(Attack attack, Map<String, Long> repeatedTypeMap,
			ArrayList<Attack> targetList) {
		for (Map.Entry<String, Long> key : repeatedTypeMap.entrySet()) {
			if (key.getKey().equals(attack.getStrTypeToPkType().getName().toUpperCase()) && key.getValue() == 2
					&& !targetList.contains(attack)) {
				targetList.add(attack);

				return true;
			}
		}

		return false;
	}

	// -----------------------------
	// Fill lists of strong, weak, normal and no effect type names based on the
	// facing Pokemon
	// -----------------------------
	private static void fillDamageTypeLists(HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypesCopy,
			ArrayList<PokemonType> noRepeatedAttackTypes, ArrayList<PokemonType> hasNoEffect,
			List<String> lotDamageRepeatedTypes, List<String> normalDamageRepeatedTypes,
			List<String> lowDamageRepeatedTypes) {

		for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> eftc : effectPerTypesCopy.entrySet()) {
			ArrayList<PokemonType> noEffectList = eftc.getValue().get("No tiene efecto");
			ArrayList<PokemonType> lotDamageList = eftc.getValue().get("Le rebientan");
			ArrayList<PokemonType> lowDamageList = eftc.getValue().get("Le Rebientan poco");

			// Put only types that doesn't hurt
			if (noEffectList != null) {
				for (PokemonType noEffect : noEffectList)
					if (!hasNoEffect.contains(noEffect))
						hasNoEffect.add(noEffect);
			}

			// Put only types that hurts a lot
			if (lotDamageList != null) {
				for (PokemonType lotDamage : lotDamageList)
					lotDamageRepeatedTypes.add(lotDamage.getName().toUpperCase());
			}

			// Put only types that hurt a little
			if (lowDamageList != null) {
				for (PokemonType lowDamage : lowDamageList)
					lowDamageRepeatedTypes.add(lowDamage.getName().toUpperCase());
			}

			// Put normal attacks
			for (PokemonType pAttck : noRepeatedAttackTypes) {
				boolean notStrong = lotDamageList == null || !lotDamageList.contains(pAttck);

				boolean notWeak = lowDamageList == null || !lowDamageList.contains(pAttck);

				if (notStrong && notWeak && !hasNoEffect.contains(pAttck))
					normalDamageRepeatedTypes.add(pAttck.getName().toUpperCase());
			}
		}
	}

	// -----------------------------
	// Puts in a Map the number of times that appears the elements in the list
	// -----------------------------
	private static Map<String, Long> countDuplicates(List<String> list) {
		return list.stream().collect(Collectors.groupingBy(e -> e.toString(), Collectors.counting()));
	}

	// -----------------------------
	// Returns true if attacker has at least one super effective move
	// -----------------------------
	public static boolean hasSuperEffectiveAttack(Pokemon attacker, Pokemon defender) {
		return attacker.getFourPrincipalAttacks().stream()
				.anyMatch(a -> getEffectiveness(a.getStrTypeToPkType(), defender) > 1f);
	}

	// -----------------------------
	// Returns true if attacker has an OHKO or self destruction move
	// -----------------------------
	public static boolean hasDangerousAttack(Pokemon pokemon) {
		return pokemon.getFourPrincipalAttacks().stream().anyMatch(a -> a.isOneHitKO() || a.isSelfDestruction());
	}

	// -----------------------------
	// Returns the best attack according to power, STAB and effectiveness
	// -----------------------------
	public static Attack getBestAttack(Pokemon attacker, Pokemon defender) {
		Attack bestAttack = null;
		float bestScore = -1f;

		for (Attack atk : attacker.getFourPrincipalAttacks()) {
			if (atk.getPp() <= 0)
				continue;

			float effectiveness = getEffectiveness(atk.getStrTypeToPkType(), defender);

			float stab = attacker.getTypes().contains(atk.getStrTypeToPkType()) ? 1.5f : 1f;

			float power = atk.getPower() > 0 ? atk.getPower() : 1f;

			float score = effectiveness * stab * power;

			if (score > bestScore) {
				bestScore = score;
				bestAttack = atk;
			}
		}

		return bestAttack;
	}

	// -----------------------------
	// Get effectiveness against defender
	// -----------------------------
	public static float getEffectiveness(PokemonType attackType, Pokemon defender) {
		float effectiveness = 1f;

		for (PokemonType defenderType : defender.getTypes()) {
			int defTypeId = defenderType.getId();

			if (attackType.getPktDoLotDamage().contains(defTypeId)) {
				effectiveness *= 2f;
			} else if (attackType.getPktDoLowDamage().contains(defTypeId)) {
				effectiveness *= 0.5f;
			}
		}

		return effectiveness;
	}

	// -----------------------------
	// Prepares best attack for player
	// -----------------------------
	public static void prepareBestAttackPlayer(Player owner, int attackId, Pokemon pokemonRival) {
		Pokemon defender = owner.getPkFacing();

		Optional<Attack> nextAttack = owner.getPkCombatting().getFourPrincipalAttacks().stream()
				.filter(a -> a.getId() == attackId).findFirst();

		if (nextAttack.isEmpty())
			return;

		Attack atk = nextAttack.get();
		PokemonType attackType = atk.getStrTypeToPkType();

		// 1 - Real effectiveness
		float effectiveness = getEffectiveness(attackType, pokemonRival);

		prepareAttack(atk, owner.getPkCombatting(), defender, effectiveness);

		owner.getPkCombatting().setNextMovement(atk);
	}

	// -----------------------------
	// Prepare best attack
	// -----------------------------
	private static void prepareAttack(Attack atk, Pokemon attacker, Pokemon defender, float effectiveness) {
		PokemonType attackType = atk.getStrTypeToPkType();

		// 110_Tinted_Lens ability (attacker) => low effectiveness is treated as neutral
		if (attacker.getAbilitySelected().getId() == 110 && effectiveness > 0f && effectiveness < 1f)
			effectiveness *= 2f;

		// 111_Filter ability (defender) => reduce super effective attack by 1/4
		if (defender.getAbilitySelected().getId() == 111 && effectiveness > 1f)
			effectiveness *= 0.75f;

		atk.setEffectivenessAgainstPkFacing(effectiveness);

		// 2 - Stab
		float bonus = attacker.getTypes().contains(attackType) ? 1.5f : 1f;
		atk.setBonus(bonus);
	}

	// -----------------------------
	// Chooses the attack from machine
	// -----------------------------
	public static void prepareBestAttackIA(Player owner, Pokemon opponent) {
		Pokemon attacker = owner.getPkCombatting();
		Pokemon defender = owner.getPkFacing();

		// If no PPs remaining in any attack => use 165_Struggle
		boolean hasPP = attacker.getFourPrincipalAttacks().stream().anyMatch(a -> a.getPp() > 0);

		if (!hasPP) {
			selectStruggle(owner);
			return;
		}

		Attack bestEffectiveAttack = null;
		float bestScore = -1f;

		Attack bestNormalAttack = null;
		float bestNormalScore = -1f;

		Attack bestOtrosAttack = null;

		// Check all possible attacks
		for (Attack atk : attacker.getFourPrincipalAttacks()) {
			if (atk.getPp() <= 0)
				continue;

			if (isAttackDisabled(attacker, atk))
				continue;

			PokemonType attackType = atk.getStrTypeToPkType();

			float effectiveness = getEffectiveness(attackType, opponent);
			float effectivenessForScore = effectiveness;

			// 110_Tinted_Lens ability (attacker) => low effectiveness is treated as neutral
			// for AI
			// scoring
			if (attacker.getAbilitySelected().getId() == 110 && effectiveness > 0f && effectiveness < 1f)
				effectivenessForScore *= 2f;

			// 111_Filter ability (defender) => reduce super effective attack by 1/4
			if (defender.getAbilitySelected().getId() == 111 && effectiveness > 1f)
				effectivenessForScore *= 0.75f;

			float stab = attacker.getTypes().contains(attackType) ? 1.5f : 1f;

			float power = atk.getPower() > 0 ? atk.getPower() : 1f;

			float score = effectivenessForScore * stab * power;

			// Save best attack score
			if (effectivenessForScore > 1f && score > bestScore) {
				bestScore = score;
				bestEffectiveAttack = atk;
			}

			// Save normal attack (just in case)
			if (effectivenessForScore == 1f && !atk.getBases().contains("otros") && score > bestNormalScore) {
				bestNormalScore = score;
				bestNormalAttack = atk;
			}

			// Save an attack from "others"
			if (atk.getBases().contains("otros")) {
				if (bestOtrosAttack == null) {
					bestOtrosAttack = atk;
				}
			}
		}

		// Final decision
		Attack chosenAttack;

		if (bestEffectiveAttack != null) {
			chosenAttack = bestEffectiveAttack;

		} else if (bestNormalAttack != null) {
			chosenAttack = bestNormalAttack;

		} else if (bestOtrosAttack != null) {
			chosenAttack = bestOtrosAttack;

		} else {
			// Last case : any attack with PP
			chosenAttack = attacker.getFourPrincipalAttacks().stream().filter(a -> a.getPp() > 0).findFirst().get();
		}

		// Get again effectiveness => for example for 110_Tinted_Lens ability doubles
		// low effectiveness damage => but this time from chosen attack
		float effectiveness = getEffectiveness(chosenAttack.getStrTypeToPkType(), opponent);
		// Apply effectiveness and real STAB
		prepareAttack(chosenAttack, attacker, opponent, effectiveness);
		attacker.setNextMovement(chosenAttack);

		System.out.println("IA eligió: " + chosenAttack.getName() + " | eff="
				+ chosenAttack.getEffectivenessAgainstPkFacing() + " | stab=" + chosenAttack.getBonus());
	}

	// -----------------------------
	// Returns true if the given attack is currently disabled for this Pokemon
	// -----------------------------
	private static boolean isAttackDisabled(Pokemon pk, Attack atk) {
		if (pk.hasActiveStatusCondition(StatusConditions.DISABLE)) {
			State disableStatus = pk.getStatusCondition();
			return disableStatus.getAttackDisabled().getId() == atk.getId();
		}
		return false;
	}

	// -----------------------------
	// If no remaining Pokemon with PP on attacks, set a new attack "Struggle" (used
	// by all Pokemon)
	// -----------------------------
	public static void selectStruggle(Player owner) {
		owner.getPkCombatting().setNextMovement(
				owner.getPkCombatting().getPhysicalAttacks().stream().filter(a -> a.getId() == 165).findFirst().get());
	}
}
