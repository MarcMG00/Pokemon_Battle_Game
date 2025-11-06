package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Player {
	private ArrayList<Pokemon> pokemon;
	private Pokemon pkCombatting;
	private Pokemon pkFacing;
	private Attack nextAttack;

	public Player() {
		super();
		this.pokemon = new ArrayList<>();
		this.pkCombatting = new Pokemon();
		this.nextAttack = new Attack();
		this.pkFacing = new Pokemon();
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

	public Attack getNextAttack() {
		return nextAttack;
	}

	public void setNextAttack(Attack nextAttack) {
		this.nextAttack = nextAttack;
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

	// Adds 4 attacks to each Pokemon from player (1 other, 2 physicals, 1 special)
	public void addAttacksForEachPokemon() {
		
		for (Pokemon pk : this.pokemon) {
			
			System.out.println(pk.getName());
			
			Random rand = new Random();
			pk.addAttacks(pk.getOtherAttacks().get(rand.nextInt(pk.getOtherAttacks().size())));

			for (int times = 0; times < 2; times++) {
				
				rand = new Random();
				pk.addAttacks(pk.getPhysicalAttacks().get(rand.nextInt(pk.getPhysicalAttacks().size())));
				
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
	
//	public void addSpecificAttacksForTests() {
//		for (Pokemon pk : this.getPokemon()) {
//			pk.addAtaques(pk.getAtaFisicos().stream().filter(af -> af.getIdAta() == 7).findFirst().get());
//			pk.addAtaques(pk.getAtaFisicos().stream().filter(af -> af.getIdAta() == 9).findFirst().get());
//			pk.addAtaques(pk.getAtaFisicos().stream().filter(af -> af.getIdAta() == 19).findFirst().get());
//			
//
//			// Adds the Ids of attacks chosed in a list
//			for (Ataque ataChosed : pk.getCuatroAtaques()) {
//				pk.addAtaquesIds(ataChosed.getIdAta());
//			}
//		}
//	}

	// Puts in a Map the number of times that appears the elements in the list
	@SuppressWarnings("unused")
	private Map<String, Long> countDuplicates(List<String> list) {
		Map<String, Long> couterMap;
		return couterMap = list.stream().collect(Collectors.groupingBy(e -> e.toString(), Collectors.counting()));
	}

	// Order all the attacks by damage level against the Pokemon facing
	public void orderAttacksFromDammageLevelPokemon(HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {
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
		ArrayList<PokemonType> noRepeatedAttackTypes = new ArrayList<>();

		// Gets information about the type of Pokemon (different damages...)
		Map<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypesFiltered;
		
		// Types that doesn't hurt the Pokemon facing
		ArrayList<PokemonType> hasNoEffect = new ArrayList<>();

		// Vars to get the different information for each type of the Pokemon facing
		List<String> lotDamageRepeatedTypes = new ArrayList<>();
		List<String> normalDamageRepeatedTypes = new ArrayList<>();
		List<String> lowDamageRepeatedTypes = new ArrayList<>();

		// Puts the types of IA Pokemon attacking in the list (without duplicates)
		for (Attack atck : this.getPkCombatting().getFourPrincipalAttacks()) {
			
			if (!noRepeatedAttackTypes.contains(atck.getStrTypeToPkType())) {
				
				noRepeatedAttackTypes.add(atck.getStrTypeToPkType());
				
			}
		}

		for (int iType = 0; iType < pkFacing.size(); iType++) {
			
			int iFinalType = iType;
			
			// Filters in a new Map all the damages of the Pokemon facing from the picked type
			effectPerTypesFiltered = effectPerTypes.entrySet().stream()
					.filter(ef -> ef.getKey().toUpperCase().equals(this.getPkFacing().getTypes().get(iFinalType).getName()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			// Put the results in a new Map
			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> eftf : effectPerTypesFiltered.entrySet()) {
				
				effectPerTypesCopy.put(eftf.getKey(), eftf.getValue());
				
			}
		}

		// If Pokemon facing has 2 types
		if (effectPerTypesCopy.size() == 2) {
			
			// Each value in "effectPerTypesCopy" corresponds a type of Pokemon facing
			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> eftc : effectPerTypesCopy.entrySet()) {
				
				// Put only types that doesn't hurt
				if (eftc.getValue().containsKey("No tiene efecto") && eftc.getValue().get("No tiene efecto").size() != 0) {
					
					for (PokemonType noEffect : eftc.getValue().get("No tiene efecto")) {
						
						if (!hasNoEffect.contains(noEffect)) {
							
							hasNoEffect.add(noEffect);
							
						}
					}
				}

				// Put only types that hurts a lot
				if (eftc.getValue().containsKey("Le rebientan") && eftc.getValue().get("Le rebientan").size() != 0) {
					
					for (PokemonType lotDamage : eftc.getValue().get("Le rebientan")) {
						
						lotDamageRepeatedTypes.add(lotDamage.getName().toUpperCase());
						
					}
				}

				// Put only types that hurt a little
				if (eftc.getValue().containsKey("Le Rebientan poco") && eftc.getValue().get("Le Rebientan poco").size() != 0) {
					
					for (PokemonType lowDamage : eftc.getValue().get("Le Rebientan poco")) {
						
						lowDamageRepeatedTypes.add(lowDamage.getName().toUpperCase());
						
					}
				}

				// Put normal attacks (doesn't hurt a lot, but are not the weakest)
				for (PokemonType pAttck : noRepeatedAttackTypes) {
					
					if (((eftc.getValue().containsKey("Le rebientan") && !eftc.getValue().get("Le rebientan").contains(pAttck))
						  || (eftc.getValue().containsKey("Le Rebientan poco") && !eftc.getValue().get("Le Rebientan poco").contains(pAttck)))
						  && !hasNoEffect.contains(pAttck)) {
						
						normalDamageRepeatedTypes.add(pAttck.getName().toUpperCase());
						
					}
				}
			}

			// Counts how many times appears one type in a list
//			System.out.println();
			Map<String, Long> finalLotDammageRepeatedTypes = countDuplicates(lotDamageRepeatedTypes);
//			System.out.println("tipos rebientan si o si : " + tiposRepetidosRebientanFinal);

			Map<String, Long> finalLittleDammageRepeatedTypes = countDuplicates(lowDamageRepeatedTypes);
//			System.out.println("tipos debiles : " + tiposRepetidosDebilesFinal);

//			Map<String, Long> tiposRepetidosNormalesFinal = countDuplicates(tiposRepetidosNormales);
//			System.out.println("tipos normales a los dos : " + tiposRepetidosNormalesFinal);

			for (Attack finalAttack : this.getPkCombatting().getFourPrincipalAttacks()) {
				
				// Don't put the attack twice at time in a different list
				boolean isPicked = false;
				
				// Attack doesn't hurt if it is in the list "no afecta"
				if (hasNoEffect.contains(finalAttack.getStrTypeToPkType()) && !iaHasNoEffectAttacks.contains(finalAttack)) {
					
					iaHasNoEffectAttacks.add(finalAttack);
					
				} else {
					
					// If type attack appears twice in the list "Rebientan", it affects both types from Pokemon facing
					for (Map.Entry<String, Long> key : finalLotDammageRepeatedTypes.entrySet()) {
						
						if (key.getKey().equals(finalAttack.getStrTypeToPkType().getName().toUpperCase()) && key.getValue() == 2 && !iaLotDamageAttacks.contains(finalAttack)) {
							
							iaLotDamageAttacks.add(finalAttack);
							isPicked = true;
							
						}
					}
					
					// If type attack appears twice in list "Debiles", it affects both types from Pokemon facing
					for (Map.Entry<String, Long> key : finalLittleDammageRepeatedTypes.entrySet()) {
						
						if (key.getKey().equals(finalAttack.getStrTypeToPkType().getName().toUpperCase()) && key.getValue() == 2 && !iaLowAttacks.contains(finalAttack)) {
							
							iaLowAttacks.add(finalAttack);
							isPicked = true;
							
						}
					}
					
					// If type is in list "Debiles", it doesn't matter if it hurts a lot to one type from Pokemon facing
					// still doesn't hurts the other type, so it is a weak attack
					if (!isPicked) {
						
						if (finalLittleDammageRepeatedTypes.containsKey(finalAttack.getStrTypeToPkType().getName().toUpperCase()) && !iaLowAttacks.contains(finalAttack)) {
							
							iaLowAttacks.add(finalAttack);
							
						}
						
						// If type appears in "Rebienta" and in normal, it ascends to "hurts a lot"
						else if (finalLotDammageRepeatedTypes.containsKey(finalAttack.getStrTypeToPkType().getName().toUpperCase())
								&& normalDamageRepeatedTypes.contains(finalAttack.getStrTypeToPkType().getName().toUpperCase())
								&& !iaLotDamageAttacks.contains(finalAttack)) {
							
							iaLotDamageAttacks.add(finalAttack);
							
						}
						
						// Else it is normal
						else {
							if (!iaNormalDamageAttacks.contains(finalAttack)) {
								
								iaNormalDamageAttacks.add(finalAttack);
								
							}
						}
					}
				}
			}
		}
		
		// If Pokemon facing has 1 type
		else {
			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> ef : effectPerTypesCopy.entrySet()) {
				
				for (Attack finalAttack : this.getPkCombatting().getFourPrincipalAttacks()) {
					
					if (ef.getValue().get("Le rebientan").contains(finalAttack.getStrTypeToPkType()) && !iaLotDamageAttacks.contains(finalAttack)
							&& !hasNoEffect.contains(finalAttack.getStrTypeToPkType())) {
						
						iaLotDamageAttacks.add(finalAttack);
						
					} else if (ef.getValue().get("Le Rebientan poco").contains(finalAttack.getStrTypeToPkType()) && !iaLowAttacks.contains(finalAttack)
							&& !hasNoEffect.contains(finalAttack.getStrTypeToPkType())) {
						
						iaLowAttacks.add(finalAttack);
						
					} else if (hasNoEffect.contains(finalAttack.getStrTypeToPkType())) {
						
						iaHasNoEffectAttacks.add(finalAttack);
						
					} else {
						
						if (!iaNormalDamageAttacks.contains(finalAttack) && !hasNoEffect.contains(finalAttack.getStrTypeToPkType())) {
							
							iaNormalDamageAttacks.add(finalAttack);
							
						}
					}
				}
			}
		}

		// Sets all the attacks from the Pokemon by their level of damage
		this.getPkCombatting().setLotDamageAttacks(iaLotDamageAttacks);
		this.getPkCombatting().setNormalAttacks(iaNormalDamageAttacks);
		this.getPkCombatting().setLowAttacks(iaLowAttacks);
		this.getPkCombatting().setNotEffectAttacks(iaHasNoEffectAttacks);

//		System.out.println("Ataques no afectan");
//		for (Ataque a : this.getPkCombatting().getAtaquesNoAfectan()) {
//			System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
//		}
//		System.out.println("Ataques rebientan");
//		for (Ataque a : this.getPkCombatting().getAtaquesRebientan()) {
//			System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
//		}
//		System.out.println("Ataques normales");
//		for (Ataque a : this.getPkCombatting().getAtaquesNormales()) {
//			System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
//		}
//		System.out.println("Ataques debiles");
//		for (Ataque a : this.getPkCombatting().getAtaquesDebiles()) {
//			System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
//		}
//		System.out.println("next method");
//		System.out.println();
	}

	// Choice the attack : detects if it's machine or the player
	public void prepareBestAttack(boolean isNormalPlayer, Object idAttackPkPlayer) {
		// Gets a random number to chose the base of the attack (>10 : "otros", and if it's the machine choice)
		int randomNumber = 0;
		
		if(!isNormalPlayer) {
			
			randomNumber = (int) (Math.random() * (100) + 1);
//			System.out.println(randomNumber);
			
		}

		// Allows to chose an attack if doesn't meet the conditions before
		boolean isAttackChosen = false;

		// Physical or special attack
		if (randomNumber > 10 || isNormalPlayer) {
			
			// Attacks that hurt a lot (same type as the Pk IA)
			if (!this.getPkCombatting().getFourPrincipalAttacks().isEmpty()) {
				
				for (PokemonType pkT : this.getPkCombatting().getTypes()) {
					
					Optional<Attack> nextAttack = Optional.of(new Attack());
					
					if (isNormalPlayer) {
						
						// Takes only the best attack that is matching the same type as the Pokemon
						nextAttack = this.getPkCombatting().getLotDamageAttacks().stream().filter(a -> a.getId() == (int) idAttackPkPlayer && a.getStrTypeToPkType() == pkT).findFirst();
						
					} else {
						
						nextAttack = this.getPkCombatting().getLotDamageAttacks().stream().filter(a -> a.getStrTypeToPkType() == pkT && !a.getBases().contains("otros")).findFirst();
						
					}
					
					if (nextAttack.isPresent()) {
						
						// Effectiveness and bonus
						if(isNormalPlayer) {
							
							if(!nextAttack.get().getBases().contains("otros") && nextAttack.get().getStrTypeToPkType() == pkT) {
								
								nextAttack.get().setEffectivenessAgainstPkFacing(2);
								nextAttack.get().setBonus(randomNumber);
								
							}
							else if(!nextAttack.get().getBases().contains("otros")) {
								
								nextAttack.get().setEffectivenessAgainstPkFacing(1.5f);
								nextAttack.get().setBonus(1f);
								
							}
						}
						else {
							
							nextAttack.get().setEffectivenessAgainstPkFacing(2);
							nextAttack.get().setBonus(1.5f);
							
						}
						
						this.getPkCombatting().setNextMouvement(nextAttack.get());
						
						System.out.println(nextAttack.get().getName() + " - rebienta y mismo tipo " + nextAttack.get().getBases());
						isAttackChosen = true;
						break;
					}
				}
			}
			
			if (!isAttackChosen) {
				
				// Attacks that hurt a lot (different type as the Pk IA)
				if (!this.getPkCombatting().getLotDamageAttacks().isEmpty()) {
					
					Optional<Attack> nextAttack = Optional.of(new Attack());
					
					if (isNormalPlayer) {
						
						// Takes the best attack in the list
						nextAttack = this.getPkCombatting().getLotDamageAttacks().stream().filter(a -> a.getId() == (int) idAttackPkPlayer).findFirst();
						
					}
					
					else {
						
						nextAttack = this.getPkCombatting().getLotDamageAttacks().stream().filter(a -> !a.getBases().contains("otros")).findFirst();
						
					}
					if (nextAttack.isPresent()) {
						
						// Effectiveness and bonus
						if(isNormalPlayer) {
							
							if(!nextAttack.get().getBases().contains("otros") && this.getPkCombatting().getTypes().contains(nextAttack.get().getStrTypeToPkType())) {
								
								nextAttack.get().setEffectivenessAgainstPkFacing(2);
								nextAttack.get().setBonus(1.5f);
								
							}
							
							else if(!nextAttack.get().getBases().contains("otros")) {
								
								nextAttack.get().setEffectivenessAgainstPkFacing(1.5f);
								nextAttack.get().setBonus(1);
								
							}
							
						}
						
						else {
							
							nextAttack.get().setEffectivenessAgainstPkFacing(1.5f);
							nextAttack.get().setBonus(1);
							
						}
						
						this.getPkCombatting().setNextMouvement(nextAttack.get());
						
						System.out.println(nextAttack.get().getName() + " - rebienta y tipo diferente "+ nextAttack.get().getBases());
						isAttackChosen = true;
						
					}
				}
			}
			
			// Else choose first attack that doesn't match with "otros" and is not the weakest attack
			if (!isAttackChosen) {
				
				if (!this.getPkCombatting().getNormalAttacks().isEmpty()) {
					
					Optional<Attack> nextAttack = Optional.of(new Attack());
					
					if (isNormalPlayer) {
						
						nextAttack = this.getPkCombatting().getNormalAttacks().stream().filter(a -> a.getId() == (int) idAttackPkPlayer).findFirst();
						
					}
					
					else {
						
						nextAttack = this.getPkCombatting().getNormalAttacks().stream().filter(a -> !a.getBases().contains("otros")
										&& !this.getPkCombatting().getLowAttacks().contains(a)).findFirst();
						
					}
					
					if (nextAttack.isPresent()) {
						
						// Effectiveness and bonus
						if(isNormalPlayer) {
							
							if(!nextAttack.get().getBases().contains("otros")) {
								
								nextAttack.get().setEffectivenessAgainstPkFacing(1);
								nextAttack.get().setBonus(1);
								
							}
							
						}
						
						else {
							
							nextAttack.get().setEffectivenessAgainstPkFacing(1);
							nextAttack.get().setBonus(1);
							
						}
						
						this.getPkCombatting().setNextMouvement(nextAttack.get());
						
						System.out.println(nextAttack.get().getName() + " - normal " + nextAttack.get().getBases());
						
						isAttackChosen = true;
						
					}
				}
			}
			
			// Else choose a random attack that doesn't match with "otros"
			if (!isAttackChosen) {
				
				Optional<Attack> nextAttack = Optional.of(new Attack());
				
				if (isNormalPlayer) {
					
					nextAttack = this.getPkCombatting().getFourPrincipalAttacks().stream().filter(a -> a.getId() == (int) idAttackPkPlayer).findFirst();
					
				}
				
				else {
					
					nextAttack = this.getPkCombatting().getFourPrincipalAttacks().stream().filter(a -> !a.getBases().contains("otros")).findFirst();
					
				}
				
				if (nextAttack.isPresent()) {
					
					// Effectiveness and bonus
					if(isNormalPlayer) {
						
						if(!nextAttack.get().getBases().contains("otros") && this.getPkCombatting().getLowAttacks().contains(nextAttack.get())) {
							
							nextAttack.get().setEffectivenessAgainstPkFacing(0.5f);
							nextAttack.get().setBonus(1);
							
						}
						
					}
					
					else {
						
						if (this.getPkCombatting().getLotDamageAttacks().contains(nextAttack.get())) {
							
							nextAttack.get().setEffectivenessAgainstPkFacing(randomNumber);
							nextAttack.get().setBonus(1);
							
						} else if (this.getPkCombatting().getNormalAttacks().contains(nextAttack.get())) {
							
							nextAttack.get().setEffectivenessAgainstPkFacing(1);
							nextAttack.get().setBonus(1);
							
						} else {
							
							nextAttack.get().setEffectivenessAgainstPkFacing(0.5f);
							nextAttack.get().setBonus(1);
							
						}
					}
					
					this.getPkCombatting().setNextMouvement(nextAttack.get());
					
					System.out.println(nextAttack.get().getName() + " - random sin otros " + nextAttack.get().getBases());
					
					isAttackChosen = true;
					
				}
			}
			
		} else {
			
			// Choose the only attack from "otros"
			// Doesn't has effectiveness
			this.getPkCombatting().setNextMouvement(this.getPkCombatting().getFourPrincipalAttacks().stream().filter(a -> a.getBases().contains("otros")).findFirst().get());
			
			System.out.println(this.getPkCombatting().getNextMouvement().getName());
			
		}
	}

	// Prints the attacks of current Pokemon
	public void printAttacksFromPokemonCombating() {
		for (Attack currentAttack : this.getPkCombatting().getFourPrincipalAttacks()) {
			
			System.out.println(currentAttack.getId() + " - " + currentAttack.getName() + " - " + currentAttack.getType());
			
		}
	}

	// Prints all the info from all the Pokemon player
	public void printPokemonInfo() {
		for (Pokemon pk : this.getPokemon()) {
			
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
	
	// Applie dammage
	public void applyDamage(boolean useAttackChargedNow) {
		PkVPk battleVS = new PkVPk(this.getPkCombatting(), this.getPkFacing());
		
		battleVS.getProbabilityOfAttackingAndAttack(useAttackChargedNow);
	}
}
