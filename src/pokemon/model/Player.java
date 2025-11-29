package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import pokemon.enums.StatusConditions;

public class Player {
	private ArrayList<Pokemon> pokemon;
	private Pokemon pkCombatting;
	private Pokemon pkFacing;
	private boolean forceSwitchPokemon;

	public Player() {
		super();
		this.pokemon = new ArrayList<>();
		this.pkCombatting = new Pokemon();
		this.pkFacing = new Pokemon();
		this.forceSwitchPokemon = false;
	}

	public ArrayList<Pokemon> getPokemon() {
		return pokemon;
	}

	public void setPokemon(ArrayList<Pokemon> pokemon) {
		this.pokemon = pokemon;
	}

	public Pokemon getPkCombatting() {
		return pkCombatting;
	}

	public void setPkCombatting(Pokemon pkCombatting) {
		this.pkCombatting = pkCombatting;
	}

	// Adds Pokemon to Pokemon player
	public void addPokemon(Pokemon pk) {
		this.pokemon.add(pk);
	}

	public Pokemon getPkFacing() {
		return pkFacing;
	}

	public void setPkFacing(Pokemon pkFacing) {
		this.pkFacing = pkFacing;
	}

	public boolean getIsForceSwitchPokemon() {
		return forceSwitchPokemon;
	}

	public void setForceSwitchPokemon(boolean forceSwitchPokemon) {
		this.forceSwitchPokemon = forceSwitchPokemon;
	}

	// Adds 4 attacks to each Pokemon from player (1 other, 2 physicals, 1 special)
	public void addAttacksForEachPokemon() {

		for (Pokemon pk : this.pokemon) {

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

			// Adds the Ids of attacks chosen in a list
			for (Attack attackChosen : pk.getFourPrincipalAttacks()) {

				pk.addIdAttack(attackChosen.getId());

			}

			System.out.println("fin PK");
		}
	}

	// Order all the attacks by damage level against the Pokemon facing
	public void orderAttacksFromDammageLevelPokemon(
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {
		// Gets the Pokemon types that player is currently facing
		ArrayList<PokemonType> pkFacing = this.pkFacing.getTypes();

		// Copy all the effects for each type of the current Pokemon player
		HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypesCopy = new HashMap<>();

		// Vars that put the attacks by their level of damage
		ArrayList<Attack> iaLotDamageAttacks = new ArrayList<>();
		ArrayList<Attack> iaNormalDamageAttacks = new ArrayList<>();
		ArrayList<Attack> iaLowAttacks = new ArrayList<>();
		ArrayList<Attack> iaHasNoEffectAttacks = new ArrayList<>();

		// Puts the different types of the attacks in a list without duplicates
		ArrayList<PokemonType> noRepeatedAttackTypes = getUniqueAttackTypes();

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

			for (Attack finalAttack : this.getPkCombatting().getFourPrincipalAttacks()) {

				boolean isPicked = false;

				if (hasNoEffect.contains(finalAttack.getStrTypeToPkType())
						&& !iaHasNoEffectAttacks.contains(finalAttack)) {

					iaHasNoEffectAttacks.add(finalAttack);

				} else {

					// Affects both types strongly
					isPicked = addIfDoubleType(finalAttack, finalLotDammageRepeatedTypes, iaLotDamageAttacks);
					isPicked |= addIfDoubleType(finalAttack, finalLittleDammageRepeatedTypes, iaLowAttacks);

					// If not picked, apply single-type logic
					if (!isPicked) {

						if (finalLittleDammageRepeatedTypes
								.containsKey(finalAttack.getStrTypeToPkType().getName().toUpperCase())
								&& !iaLowAttacks.contains(finalAttack)) {

							iaLowAttacks.add(finalAttack);

						} else if (finalLotDammageRepeatedTypes
								.containsKey(finalAttack.getStrTypeToPkType().getName().toUpperCase())
								&& normalDamageRepeatedTypes
										.contains(finalAttack.getStrTypeToPkType().getName().toUpperCase())
								&& !iaLotDamageAttacks.contains(finalAttack)) {

							iaLotDamageAttacks.add(finalAttack);

						} else if (!iaNormalDamageAttacks.contains(finalAttack)) {

							iaNormalDamageAttacks.add(finalAttack);

						}
					}
				}
			}
		}

		// If Pokemon facing has 1 type
		else {
			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> ef : effectPerTypesCopy.entrySet()) {

				for (Attack finalAttack : this.getPkCombatting().getFourPrincipalAttacks()) {

					if (ef.getValue().get("Le rebientan").contains(finalAttack.getStrTypeToPkType())
							&& !iaLotDamageAttacks.contains(finalAttack)
							&& !hasNoEffect.contains(finalAttack.getStrTypeToPkType())) {

						iaLotDamageAttacks.add(finalAttack);

					} else if (ef.getValue().get("Le Rebientan poco").contains(finalAttack.getStrTypeToPkType())
							&& !iaLowAttacks.contains(finalAttack)
							&& !hasNoEffect.contains(finalAttack.getStrTypeToPkType())) {

						iaLowAttacks.add(finalAttack);

					} else if (hasNoEffect.contains(finalAttack.getStrTypeToPkType())) {

						iaHasNoEffectAttacks.add(finalAttack);

					} else if (!iaNormalDamageAttacks.contains(finalAttack)
							&& !hasNoEffect.contains(finalAttack.getStrTypeToPkType())) {

						iaNormalDamageAttacks.add(finalAttack);
					}
				}
			}
		}

		// Sets all the attacks from the Pokemon by their level of damage
		this.getPkCombatting().setLotDamageAttacks(iaLotDamageAttacks);
		this.getPkCombatting().setNormalAttacks(iaNormalDamageAttacks);
		this.getPkCombatting().setLowAttacks(iaLowAttacks);
		this.getPkCombatting().setNotEffectAttacks(iaHasNoEffectAttacks);

		// System.out.println("Ataques no afectan");
		// for (Ataque a : this.getPkCombatting().getAtaquesNoAfectan()) {
		// System.out.println(a.getNombreAta() + " - " +
		// a.getStrTipoToPkType().getNombreTipo());
		// }
		// System.out.println("Ataques rebientan");
		// for (Ataque a : this.getPkCombatting().getAtaquesRebientan()) {
		// System.out.println(a.getNombreAta() + " - " +
		// a.getStrTipoToPkType().getNombreTipo());
		// }
		// System.out.println("Ataques normales");
		// for (Ataque a : this.getPkCombatting().getAtaquesNormales()) {
		// System.out.println(a.getNombreAta() + " - " +
		// a.getStrTipoToPkType().getNombreTipo());
		// }
		// System.out.println("Ataques debiles");
		// for (Ataque a : this.getPkCombatting().getAtaquesDebiles()) {
		// System.out.println(a.getNombreAta() + " - " +
		// a.getStrTipoToPkType().getNombreTipo());
		// }
		// System.out.println("next method");
		// System.out.println();
	}

	/* -------------------------- Helper Methods -------------------------- */

	/**
	 * Returns a list of unique PokemonType from the attacks of the current Pokemon
	 */
	private ArrayList<PokemonType> getUniqueAttackTypes() {
		ArrayList<PokemonType> uniquePkType = new ArrayList<>();

		for (Attack atck : this.getPkCombatting().getFourPrincipalAttacks()) {

			if (!uniquePkType.contains(atck.getStrTypeToPkType())) {

				uniquePkType.add(atck.getStrTypeToPkType());

			}
		}

		return uniquePkType;
	}

	/** Add attacks that hit both types strongly or weakly */
	private boolean addIfDoubleType(Attack attack, Map<String, Long> repeatedTypeMap, ArrayList<Attack> targetList) {
		for (Map.Entry<String, Long> key : repeatedTypeMap.entrySet()) {

			if (key.getKey().equals(attack.getStrTypeToPkType().getName().toUpperCase()) && key.getValue() == 2
					&& !targetList.contains(attack)) {

				targetList.add(attack);

				return true;

			}
		}

		return false;
	}

	/**
	 * Fill lists of strong, weak, normal and no effect type names based on the
	 * facing Pokemon
	 */
	private void fillDamageTypeLists(HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypesCopy,
			ArrayList<PokemonType> noRepeatedAttackTypes, ArrayList<PokemonType> hasNoEffect,
			List<String> lotDamageRepeatedTypes, List<String> normalDamageRepeatedTypes,
			List<String> lowDamageRepeatedTypes) {

		for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> eftc : effectPerTypesCopy.entrySet()) {

			// Put only types that doesn't hurt
			if (eftc.getValue().containsKey("No tiene efecto")) {

				for (PokemonType noEffect : eftc.getValue().get("No tiene efecto")) {

					if (!hasNoEffect.contains(noEffect)) {

						hasNoEffect.add(noEffect);

					}
				}
			}

			// Put only types that hurts a lot
			if (eftc.getValue().containsKey("Le rebientan")) {

				for (PokemonType lotDamage : eftc.getValue().get("Le rebientan")) {

					lotDamageRepeatedTypes.add(lotDamage.getName().toUpperCase());

				}
			}

			// Put only types that hurt a little
			if (eftc.getValue().containsKey("Le Rebientan poco")) {

				for (PokemonType lowDamage : eftc.getValue().get("Le Rebientan poco")) {

					lowDamageRepeatedTypes.add(lowDamage.getName().toUpperCase());

				}
			}

			// Put normal attacks
			for (PokemonType pAttck : noRepeatedAttackTypes) {

				boolean notStrong = eftc.getValue().containsKey("Le rebientan")
						&& !eftc.getValue().get("Le rebientan").contains(pAttck);

				boolean notWeak = eftc.getValue().containsKey("Le Rebientan poco")
						&& !eftc.getValue().get("Le Rebientan poco").contains(pAttck);

				if ((notStrong || notWeak) && !hasNoEffect.contains(pAttck)) {

					normalDamageRepeatedTypes.add(pAttck.getName().toUpperCase());

				}
			}
		}
	}

	// Puts in a Map the number of times that appears the elements in the list
	private Map<String, Long> countDuplicates(List<String> list) {
		return list.stream().collect(Collectors.groupingBy(e -> e.toString(), Collectors.counting()));
	}

	// Chooses the attack from machine
	public void prepareBestAttackIA(HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {

		// If Pokemon combating doesn't have PP remaining on his attacks => change
		// Pokemon
		if (!hasAnyPPLeft(this.getPkCombatting())) {
			System.out.println("La IA no tiene PP en ningún movimiento. Forzando cambio de Pokémon...");
			Pokemon newPk = decideBestChangePokemon(this.getPkCombatting(), effectPerTypes);

			if (newPk != null) {
				this.setPkCombatting(newPk);
				System.out.println("IA eligió a " + newPk.getName() + " (Id:" + newPk.getId() + ")");
			} else {
				System.out.println("No hay Pokémon útiles para cambiar. La IA debe usar Struggle!");
				selectStruggle();
			}
			return;
		}

		// Gets a random number to choose the attack base (>10 : "others", and if it's
		// the machine's choice)
		int randomNumber = (int) (Math.random() * 100) + 1;

		boolean isAttackChosen = false;

		// Physical or special attack
		if (randomNumber > 10) {

			// ===============================
			// 1️ High damage attack - same type as Pokemon
			// ===============================
			if (!this.getPkCombatting().getFourPrincipalAttacks().isEmpty()) {

				for (PokemonType pkType : this.getPkCombatting().getTypes()) {

					Optional<Attack> nextAttack = Optional.empty();

					// Machine: chooses best attack matching type and not "otros"
					nextAttack = this.getPkCombatting().getLotDamageAttacks().stream().filter(
							a -> a.getStrTypeToPkType() == pkType && !a.getBases().contains("otros") && a.getPp() > 0)
							.findFirst();

					if (nextAttack.isPresent()) {

						Attack atk = nextAttack.get();

						// Set effectiveness & bonus
						atk.setEffectivenessAgainstPkFacing(2);
						atk.setBonus(1.5f);

						this.getPkCombatting().setNextMovement(atk);
						System.out.println(atk.getName() + " - high damage and same type " + atk.getBases());
						isAttackChosen = true;
						break;
					}
				}
			}

			// ===============================
			// 2️ High damage attack - different type
			// ===============================
			if (!isAttackChosen && !this.getPkCombatting().getLotDamageAttacks().isEmpty()) {

				Optional<Attack> nextAttack = this.getPkCombatting().getLotDamageAttacks().stream()
						.filter(a -> !a.getBases().contains("otros") && a.getPp() > 0).findFirst();

				if (nextAttack.isPresent()) {

					Attack atk = nextAttack.get();

					atk.setEffectivenessAgainstPkFacing(1.5f);
					atk.setBonus(1);

					this.getPkCombatting().setNextMovement(atk);
					System.out.println(atk.getName() + " - high damage and different type " + atk.getBases());
					isAttackChosen = true;
				}
			}

			// ===============================
			// 3️ Normal attack
			// ===============================
			if (!isAttackChosen && !this.getPkCombatting().getNormalAttacks().isEmpty()) {

				Optional<Attack> nextAttack = this.getPkCombatting().getNormalAttacks().stream()
						.filter(a -> !a.getBases().contains("otros")
								&& !this.getPkCombatting().getLowAttacks().contains(a) && a.getPp() > 0)
						.findFirst();

				if (nextAttack.isPresent()) {

					Attack atk = nextAttack.get();

					if (!atk.getBases().contains("otros")) {

						atk.setEffectivenessAgainstPkFacing(1);
						atk.setBonus(1);

					}

					this.getPkCombatting().setNextMovement(atk);
					System.out.println(atk.getName() + " - normal " + atk.getBases());
					isAttackChosen = true;

				}
			}

			// ===============================
			// 4️ Random attack (no "otros")
			// ===============================
			if (!isAttackChosen) {

				Optional<Attack> nextAttack = this.getPkCombatting().getFourPrincipalAttacks().stream()
						.filter(a -> !a.getBases().contains("otros") && a.getPp() > 0).findFirst();

				if (nextAttack.isPresent()) {

					Attack atk = nextAttack.get();

					if (this.getPkCombatting().getLotDamageAttacks().contains(atk)) {

						atk.setEffectivenessAgainstPkFacing(1.5f);
						atk.setBonus(1);

					} else if (this.getPkCombatting().getNormalAttacks().contains(atk)) {

						atk.setEffectivenessAgainstPkFacing(1);
						atk.setBonus(1);

					} else {

						atk.setEffectivenessAgainstPkFacing(0.5f);
						atk.setBonus(1);

					}

					this.getPkCombatting().setNextMovement(atk);
					System.out.println(atk.getName() + " - random without 'otros' " + atk.getBases());
					isAttackChosen = true;
				}

				// ===============================
				// 4️ Random attack ("otros")
				// ===============================
				else {
					nextAttack = this.getPkCombatting().getFourPrincipalAttacks().stream().filter(a -> a.getPp() > 0)
							.findFirst();

					if (nextAttack.isPresent()) {

						Attack atk = nextAttack.get();

						this.getPkCombatting().setNextMovement(atk);
						System.out.println(atk.getName() + " - random 'otros' " + atk.getBases());
						isAttackChosen = true;
					}
				}
			}

		} else {
			// ===============================
			// 5️ Attack from "otros" OR first attack founded
			// ===============================
			if (this.getPkCombatting().getFourPrincipalAttacks().stream()
					.anyMatch(a -> a.getBases().contains("otros") && a.getPp() > 0)) {

				this.getPkCombatting().setNextMovement(this.getPkCombatting().getFourPrincipalAttacks().stream()
						.filter(a -> a.getBases().contains("otros") && a.getPp() > 0).findFirst().get());
			} else {
				this.getPkCombatting().setNextMovement(this.getPkCombatting().getFourPrincipalAttacks().stream()
						.filter(a -> a.getPp() > 0).findFirst().get());
			}

			System.out.println(this.getPkCombatting().getNextMovement().getName());
		}
	}

	// Prints the attacks of current Pokemon
	public void printAttacksFromPokemonCombating() {
		List<Attack> attacksAvailable = this.getPkCombatting().getFourPrincipalAttacks().stream()
				.filter(a -> a.getPp() > 0).toList();

		for (Attack currentAttack : attacksAvailable) {

			System.out
					.println(currentAttack.getId() + " - " + currentAttack.getName() + " - " + currentAttack.getType());
		}
	}

	// Prints all the info from all the Pokemon player
	public void printPokemonInfo() {

		// Get only Pokemon not debilitated
		List<Pokemon> pokemonAvailable = this.getPokemon().stream()
				.filter(pk -> pk.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED).toList();

		for (Pokemon pk : pokemonAvailable) {

			System.out.println(pk.getId() + " - " + pk.getName() + " - tipo(s) : ");

			for (PokemonType pkT : pk.getTypes()) {

				System.out.println(pkT.getName());
			}

			for (Attack currentAttacks : pk.getFourPrincipalAttacks()) {

				System.out.println(currentAttacks.getName() + " - " + currentAttacks.getType());
			}

			System.out.println("---------------");
		}
	}

	// Prepares best attack for player
	public void prepareBestAttackPlayer(int attackId) {
		Optional<Attack> nextAttack = this.getPkCombatting().getFourPrincipalAttacks().stream()
				.filter(a -> a.getId() == attackId).findFirst();
		boolean isAttackChoosen = false;

		if (nextAttack.isPresent()) {

			Attack atk = nextAttack.get();
			Optional<PokemonType> typeOpt = this.getPkCombatting().getTypes().stream()
					.filter(ty -> ty == atk.getStrTypeToPkType()).findFirst();

			// ===============================
			// 1️ High damage attack - same type as Pokemon
			// ===============================
			if (typeOpt.isPresent()) {

				if (!atk.getBases().contains("otros")) {

					atk.setEffectivenessAgainstPkFacing(2);
					atk.setBonus(1f);

				} else if (!atk.getBases().contains("otros")) {

					atk.setEffectivenessAgainstPkFacing(1.5f);
					atk.setBonus(1f);

				}
				isAttackChoosen = true;
			}
			// ===============================
			// 2️ High damage attack - different type
			// ===============================
			if (!isAttackChoosen && !this.getPkCombatting().getLotDamageAttacks().isEmpty()) {

				if (!atk.getBases().contains("otros")
						&& this.getPkCombatting().getTypes().contains(atk.getStrTypeToPkType())) {

					atk.setEffectivenessAgainstPkFacing(2);
					atk.setBonus(1.5f);

				} else if (!atk.getBases().contains("otros")) {

					atk.setEffectivenessAgainstPkFacing(1.5f);
					atk.setBonus(1);

				}
				isAttackChoosen = true;
			}
			// ===============================
			// 3️ Normal attack
			// ===============================
			if (!isAttackChoosen && !this.getPkCombatting().getNormalAttacks().isEmpty()) {

				if (!atk.getBases().contains("otros")) {

					atk.setEffectivenessAgainstPkFacing(1);
					atk.setBonus(1);

				}
				isAttackChoosen = true;
			}
			// ===============================
			// 4️ Random attack (no "otros")
			// ===============================
			if (!isAttackChoosen) {

				if (!atk.getBases().contains("otros") && this.getPkCombatting().getLowAttacks().contains(atk)) {

					atk.setEffectivenessAgainstPkFacing(0.5f);
					atk.setBonus(1);

				}
				isAttackChoosen = true;
			}
			// ===============================
			// 5️ Attack from "otros"
			// ===============================
			// Else is an attack from "otros", so nothing to do

			this.getPkCombatting().setNextMovement(atk);
		}
	}

	// Decides best Pokemon change against Pokemon facing
	public Pokemon decideBestChangePokemon(Pokemon pkPlayerFacing,
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {

		// 1️ - Random check (15% of probability to change)
		int random = (int) (Math.random() * 100) + 1;
		if (random > 15) {
			return null; // don't change
		}

		Pokemon currentPkCombatingBeforeChange = this.getPkCombatting();

		// 2️ - Analyze actual attacks from Pokemon combating
		this.orderAttacksFromDammageLevelPokemon(effectPerTypes);
		int currentBestDamage = getDamageScore(currentPkCombatingBeforeChange);

		// 3️ - Search other Pokemon form the team (not including Pokemon combating)
		Pokemon bestCandidate = null;
		int bestScore = currentBestDamage;

		// Get only Pokemon not debilitated
		List<Pokemon> pokemonAvailable = this.getPokemon().stream()
				.filter(pk -> pk.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED).toList();

		for (Pokemon candidate : pokemonAvailable) {

			if (candidate == currentPkCombatingBeforeChange)
				continue;

			// Ordenate attacks from the candidate Pokemon vs Pokemon facing
			this.setPkCombatting(candidate);
			this.setPkFacing(pkPlayerFacing);
			this.orderAttacksFromDammageLevelPokemon(effectPerTypes);

			int score = getDamageScore(candidate);

			// Rule 1: Pokemon with STAB super effective
			if (hasSTABSuperEffective(candidate)) {
				return candidate;
			}

			// Rule 2: Pokemon with a very high damage but no STAB
			if (score > bestScore) {
				bestScore = score;
				bestCandidate = candidate;
			}
		}

		// Restore real battler before returning!
		this.setPkCombatting(currentPkCombatingBeforeChange);

		// 4️ - Apply rule 2 if founded something better than actual Pokemon
		if (bestCandidate != null && bestScore > currentBestDamage) {
			return bestCandidate;
		}

		// 5️ - If nothing founded, don't change
		return null;
	}

	// Get damage score from Pokemon candidate
	private int getDamageScore(Pokemon pk) {
		if (!pk.getLotDamageAttacks().isEmpty())
			return 3; // very high damage
		if (!pk.getNormalAttacks().isEmpty())
			return 2; // normal damage
		if (!pk.getLowAttacks().isEmpty())
			return 1; // low damage

		return 0; // no effect
	}

	// Check if an attack has STAB super effective
	private boolean hasSTABSuperEffective(Pokemon pk) {

		for (Attack atk : pk.getLotDamageAttacks()) {

			// "same type" (STAB)
			if (pk.getTypes().contains(atk.getStrTypeToPkType())) {
				return true;
			}
		}

		return false;
	}

	// Check if any attack from Pokemon has PP remaining
	public boolean hasAnyPPLeft(Pokemon pk) {
		return pk.getFourPrincipalAttacks().stream().anyMatch(a -> a.getPp() > 0);
	}

	// If no remaining Pokemon with PP on attacks, set a new attack "Struggle" (used
	// by all Pokemon)
	public void selectStruggle() {
		this.getPkCombatting().setNextMovement(
				this.getPkCombatting().getPhysicalAttacks().stream().filter(a -> a.getId() == 165).findFirst().get());
	}

	public boolean hasAvailableSwitch() {
		// Get available Pokemon from defender
		List<Pokemon> options = this.getPokemon().stream().filter(pk -> pk != this.getPkCombatting())
				.filter(pk -> pk.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED).toList();

		if (options.isEmpty()) {
			return false; // If no more Pokemon remaining, attack fails
		}
		return true;
	}
}
