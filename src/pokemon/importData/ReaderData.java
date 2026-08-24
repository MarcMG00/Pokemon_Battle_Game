package pokemon.importData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pokemon.enums.AttackCategory;
import pokemon.enums.SecondaryEffectType;
import pokemon.enums.StatType;
import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Ability;
import pokemon.model.Attack;
import pokemon.model.Pokemon;
import pokemon.model.PokemonType;
import pokemon.model.SecondaryEffect;

public class ReaderData {

	// ==================================== FIELDS
	// ====================================

	private HashMap<String, ArrayList<PokemonType>> pokemonTypePerPokemon = new HashMap<>();
	private ArrayList<Ability> abilities = new ArrayList<>();
	private HashMap<String, HashMap<String, ArrayList<Ability>>> abilitiesPerPokemon = new HashMap<>();
	private HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes = new HashMap<>();
	private ArrayList<Attack> attacks = new ArrayList<>();
	private HashMap<Integer, HashMap<String, ArrayList<Integer>>> attacksPerPokemon = new HashMap<>();
	private Map<Integer, Attack> attackById = new HashMap<>();

	private static final String SAMPLE_CSV_ALL_POKEMON = "./data/pokemonList.csv";
	private static final String SAMPLE_CSV_ALL_POKEMON_ABS = "./data/pokemonList2.csv";
	private static final String SAMPLE_CSV_ALL_ABS = "./data/absList.csv";
	private static final String SAMPLE_CSV_ALL_TYPES = "./data/typesList.txt";
	private static final String SAMPLE_CSV_ALL_POKEMON_TYPES = "./data/pokemonList3.csv";
	private static final String SAMPLE_CSV_ALL_ATTACKS = "./data/attacksList.txt";
	private static final String SAMPLE_CSV_ALL_ATTACKS_FOREACH_POKEMON = "./data/attacksForEachPokemon.txt";

	// ==================================== CONSTRUCTORS
	// ====================================

	public ReaderData() {

	}

	// ==================================== GETTERS/SETTERS
	// ====================================

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}

	public HashMap<String, HashMap<String, ArrayList<Ability>>> getAbilitiesPerPokemon() {
		return abilitiesPerPokemon;
	}

	public void setAbilitiesPerPokemon(HashMap<String, HashMap<String, ArrayList<Ability>>> abilitiesPerPokemon) {
		this.abilitiesPerPokemon = abilitiesPerPokemon;
	}

	public HashMap<String, ArrayList<PokemonType>> getPokemonTypePerPokemon() {
		return pokemonTypePerPokemon;
	}

	public void setPokemonTypePerPokemon(HashMap<String, ArrayList<PokemonType>> pokemonTypePerPokemon) {
		this.pokemonTypePerPokemon = pokemonTypePerPokemon;
	}

	public HashMap<String, HashMap<String, ArrayList<PokemonType>>> getEffectPerTypes() {
		return effectPerTypes;
	}

	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

	public void setAttacks(ArrayList<Attack> attacks) {
		this.attacks = attacks;
	}

	public HashMap<Integer, HashMap<String, ArrayList<Integer>>> getAttacksPerPokemon() {
		return attacksPerPokemon;
	}

	public void setAttacksPerPokemon(HashMap<Integer, HashMap<String, ArrayList<Integer>>> attacksPerPokemon) {
		this.attacksPerPokemon = attacksPerPokemon;
	}

	public Map<Integer, Attack> getAttackById() {
		return attackById;
	}

	public void setAttackById(Map<Integer, Attack> attackById) {
		this.attackById = attackById;
	}

	// ==================================== METHODS
	// ====================================

	// -----------------------------
	// Reads pokemon.csv file and adds to Pokemon list
	// -----------------------------
	public void readPokemon(ArrayList<PokemonType> types, ArrayList<Pokemon> pokemon,
			Map<Integer, Pokemon> pokemonById) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(SAMPLE_CSV_ALL_POKEMON_TYPES);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] pks = line.split(",");

				if (pokemon.size() == 809)
					break;
				else {
					Pokemon pokemonToAdd = new Pokemon(Integer.parseInt(pks[0]), pks[1], Integer.parseInt(pks[2]),
							Integer.parseInt(pks[3]), Integer.parseInt(pks[4]), Integer.parseInt(pks[5]),
							Integer.parseInt(pks[6]), Integer.parseInt(pks[7]));

					// Gets first ability (all Pokemon have at least one ability)
					if (!pks[12].isEmpty()) {
						for (PokemonType pkty : types) {
							if (pkty.getId() == Integer.parseInt(pks[12])) {
								pokemonToAdd.addType(pkty);
								pokemonToAdd.addInitialType(pkty);
							}
						}
					}

					// Gets the other type (if a Pokemon has 2 types)
					if (pks.length == 14) {
						// It detects a " " " at the end of the second type, so we remove it : we have
						// 17" instead of 17
//							pks[13] = pks[13].substring(0, pks[13].length() - 1);

						// Gets second type
						if (!pks[13].isEmpty()) {
							for (PokemonType pkty : types) {
								if (pkty.getId() == Integer.parseInt(pks[13])) {
									pokemonToAdd.addType(pkty);
									pokemonToAdd.addInitialType(pkty);
								}
							}
						}
					}

					pokemon.add(pokemonToAdd);
					pokemonById.put(pokemonToAdd.getId(), pokemonToAdd);

				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file  : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();

				if (bufferedReader != null)
					bufferedReader.close();
				System.out.println("Finished reading readPokemon");
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	// -----------------------------
	// Reads habsList.csv file and adds to abilities list
	// -----------------------------
	public void readAbilities(ArrayList<Ability> abilities) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(SAMPLE_CSV_ALL_ABS);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] ablty = line.split(",");
				if (this.getAbilities().size() == 309)
					break;
				else {
					Ability abilityToAdd = new Ability(Integer.parseInt(ablty[0]), ablty[1].toUpperCase(), ablty[2]);
					setAbilityIsWeatherType(abilityToAdd);

					this.getAbilities().add(abilityToAdd);
					abilities.add(abilityToAdd);
				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();

				if (bufferedReader != null)
					bufferedReader.close();

				System.out.println("Finished reading readAbilities");
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	// -----------------------------
	// Reads pokemon.csv file (for abilities) and adds to Pokemon
	// -----------------------------
	public void readAddAbsForEachPokemon(ArrayList<Pokemon> pokemon) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(SAMPLE_CSV_ALL_POKEMON_ABS);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				Optional<Pokemon> pkOpt;
				String[] abltysPk = line.split(",", -1);

				// Gets the current Pokemon of the line from the Pokemon list
				pkOpt = pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(abltysPk[0])).findFirst();

				if (pkOpt.isPresent()) {
					// Gets first ability (all Pokemon have at least one ability)
					if (!abltysPk[8].isEmpty()) {
						for (Ability ablty : this.getAbilities()) {
							if (ablty.getId() == Integer.parseInt(abltysPk[8]))
								pkOpt.get().addNormalAbility(ablty);
						}
					}

					// Gets other abilities (if a Pokemon has more)
					if (abltysPk.length > 9) {
						// Gets second ability
						if (!abltysPk[9].isEmpty()) {
							for (Ability ablty : this.getAbilities()) {
								if (ablty.getId() == Integer.parseInt(abltysPk[9]))
									pkOpt.get().addNormalAbility(ablty);
							}
						}

						// Gets hidden ability
						if (!abltysPk[10].isEmpty()) {
							for (Ability ablty : this.getAbilities()) {
								if (ablty.getId() == Integer.parseInt(abltysPk[10]))
									pkOpt.get().addHiddenAbility(ablty);
							}
						}

						// Gets second hidden ability : only one Pokemon at the moment
						if (abltysPk.length == 12) {
							if (!abltysPk[11].isEmpty()) {
								for (Ability ablty : this.getAbilities()) {
									if (ablty.getId() == Integer.parseInt(abltysPk[11]))
										pkOpt.get().addHiddenAbility(ablty);
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file  : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();

				if (bufferedReader != null)
					bufferedReader.close();

				System.out.println("Finished reading addAbsForEachPokemon");
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	// -----------------------------
	// Reads pokemon.csv file (for types) and adds to Pokemon
	// -----------------------------
	public void readAddTypesForEachPokemon(String fileName, ArrayList<PokemonType> types, ArrayList<Pokemon> pokemon) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				Optional<Pokemon> pkOpt;
				String[] typesRead = line.split(",");

				// It detects a " at the beginning of the Pokemon Id, so we remove it => ex : we
				// have "001 instead of 001
				typesRead[0] = typesRead[0].substring(1);

				// Gets the current Pokemon of the line from the Pokemon list
				pkOpt = pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(typesRead[0])).findFirst();

				if (pkOpt.isPresent()) {
					// Gets first ability (all Pokemon have at least one ability)
					if (!typesRead[12].isEmpty()) {
						for (PokemonType pkty : types) {
							if (pkty.getId() == Integer.parseInt(typesRead[12]))
								pkOpt.get().addType(pkty);
						}
					}

					// Gets the other type (if a Pokemon has 2 types)
					if (typesRead.length == 14) {
						// It detects a " at the end of the second type, so we remove it => ex : we have
						// 17" instead of 17
						typesRead[13] = typesRead[13].substring(0, typesRead[13].length() - 1);

						// Gets second type
						if (!typesRead[13].isEmpty()) {
							for (PokemonType pkty : types) {
								if (pkty.getId() == Integer.parseInt(typesRead[13]))
									pkOpt.get().addType(pkty);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file  : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();

				if (bufferedReader != null)
					bufferedReader.close();

				System.out.println("Finished reading readAddTypesForEachPokemon");
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	// -----------------------------
	// Reads typesList.csv file and adds to types list
	// -----------------------------
	public void readPkTypes(ArrayList<PokemonType> types, Map<Integer, PokemonType> typeById) {

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(SAMPLE_CSV_ALL_TYPES);
			bufferedReader = new BufferedReader(fileReader);

			// Skip header
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] pkTypes = line.split(",");

				if (types.size() == 18)
					break;

				PokemonType pkType = new PokemonType(Integer.parseInt(pkTypes[0]), pkTypes[1].toUpperCase());

				// -----------------------------
				// Offensive
				// -----------------------------
				pkType.setPktDoLotDamage(parseIntList(pkTypes[2])); // Arrebienta
				pkType.setPktDoLowDamage(parseIntList(pkTypes[4])); // NoArrebientaMucho
				pkType.setNoEffect(parseIntList(pkTypes[6])); // NoLeHaceNingunDano

				// -----------------------------
				// Defensive
				// -----------------------------
				pkType.setPktRecieveLotDamage(parseIntList(pkTypes[3])); // LeArrebientan
				pkType.setPktReceiveLowDamage(parseIntList(pkTypes[5])); // LeArrebietanPoco

				types.add(pkType);
				typeById.put(pkType.getId(), pkType);
			}

		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();

				if (bufferedReader != null)
					bufferedReader.close();

				System.out.println("Finished reading readPkTypes");
			} catch (IOException e) {
				System.out.println("Exception closing the file : " + e.getMessage());
			}
		}
	}

	// -----------------------------
	// Reads typesList.csv file and adds the effects against other types
	// -----------------------------
	public void readPkTypesEffectsToOtherTypes(Map<Integer, PokemonType> typeById,
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(SAMPLE_CSV_ALL_TYPES))) {

			bufferedReader.readLine(); // skip header
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] type = line.split(",");

				String typeName = type[1];
				HashMap<String, ArrayList<PokemonType>> types = new HashMap<>();

				// Defensive parse helper
				ArrayList<PokemonType> effects;
				PokemonType pType;

				// Helper para parsear una lista de IDs separadas por ";"
				String[][] keysAndColumns = { { "Rebienta", type[2] }, { "Le rebientan", type[3] },
						{ "Rebienta poco", type[4] }, { "Le Rebientan poco", type[5] },
						{ "No tiene efecto", type[6] } };

				for (String[] pair : keysAndColumns) {
					String key = pair[0];
					String raw = pair[1];

					if (raw.equals("0"))
						continue;

					String[] ids = raw.split(";");

					effects = new ArrayList<>();

					for (String idStr : ids) {
						int id = Integer.parseInt(idStr);
						pType = typeById.get(id);

						if (pType != null)
							effects.add(pType);
					}

					if (!effects.isEmpty())
						types.put(key, effects);
				}

				// Save into main dictionary
				effectPerTypes.put(typeName, types);
			}

			System.out.println("Finished reading readPokeTypeEffectsToOtherTypes");

		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		}
	}

	// -----------------------------
	// Reads typesList.csv file and adds the effects against other types
	// -----------------------------
//		public void readPkTypesEffectsToOtherTypes() {
//			FileReader fileReader = null;
//			BufferedReader bufferedReader = null;
	//
//			try {
//				fileReader = new FileReader(SAMPLE_CSV_ALL_TYPES);
//				bufferedReader = new BufferedReader(fileReader);
	//
//				// Skips first line
//				bufferedReader.readLine();
//				String line;
	//
//				while ((line = bufferedReader.readLine()) != null) {
	//
//					HashMap<String, ArrayList<PokemonType>> types = new HashMap<>();
//					ArrayList<PokemonType> typesEffect = new ArrayList<>();
	//
//					String[] type = line.split(",");
	//
//					// Each string[] can have several values: we split them by ";"
//					String[] pkTypeEffectBigDmg = type[2].split(";");
//					String[] pkTypeEffectVulnerable = type[3].split(";");
//					String[] pkTypeEffectLittleDmg = type[4].split(";");
//					String[] pkTypeEffectLittleVulnerable = type[5].split(";");
//					String[] pkTypeNoEffect = type[6].split(";");
	//
//					Optional<PokemonType> pTypeOpt;
//					PokemonType pkTy;
	//
//					// Do a lot of damage
//					if (pkTypeEffectBigDmg.length >= 1 && Integer.parseInt(pkTypeEffectBigDmg[0]) != 0) {
	//
//						for (String pkT : pkTypeEffectBigDmg) {
	//
//							pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
	//
//							if (pTypeOpt.isPresent()) {
	//
//								pkTy = pTypeOpt.get();
	//
//								typesEffect.add(pkTy);
	//
//							}
//						}
	//
//						types.put("Rebienta", typesEffect);
//						typesEffect = new ArrayList<>();
//					}
	//
//					// Hurts from other types
//					if (pkTypeEffectVulnerable.length >= 1 && Integer.parseInt(pkTypeEffectVulnerable[0]) != 0) {
	//
//						for (String pkT : pkTypeEffectVulnerable) {
	//
//							pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
	//
//							if (pTypeOpt.isPresent()) {
	//
//								pkTy = pTypeOpt.get();
	//
//								typesEffect.add(pkTy);
	//
//							}
//						}
	//
//						types.put("Le rebientan", typesEffect);
//						typesEffect = new ArrayList<>();
//					}
	//
//					// Do little damage
//					if (pkTypeEffectLittleDmg.length >= 1 && Integer.parseInt(pkTypeEffectLittleDmg[0]) != 0) {
	//
//						for (String pkT : pkTypeEffectLittleDmg) {
	//
//							pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
	//
//							if (pTypeOpt.isPresent()) {
	//
//								pkTy = pTypeOpt.get();
	//
//								typesEffect.add(pkTy);
	//
//							}
//						}
	//
//						types.put("Rebienta poco", typesEffect);
//						typesEffect = new ArrayList<>();
//					}
	//
//					// It hurts less than other types
//					if (pkTypeEffectLittleVulnerable.length >= 1
//							&& Integer.parseInt(pkTypeEffectLittleVulnerable[0]) != 0) {
	//
//						for (String pkT : pkTypeEffectLittleVulnerable) {
	//
//							pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
	//
//							if (pTypeOpt.isPresent()) {
	//
//								pkTy = pTypeOpt.get();
	//
//								typesEffect.add(pkTy);
	//
//							}
//						}
	//
//						types.put("Le Rebientan poco", typesEffect);
//						typesEffect = new ArrayList<>();
//					}
	//
//					// Doesn't has effect to him
//					if (pkTypeNoEffect.length >= 1 && Integer.parseInt(pkTypeNoEffect[0]) != 0) {
	//
//						for (String pkT : pkTypeNoEffect) {
	//
//							pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
	//
//							if (pTypeOpt.isPresent()) {
	//
//								pkTy = pTypeOpt.get();
	//
//								typesEffect.add(pkTy);
	//
//							}
//						}
	//
//						types.put("No tiene efecto", typesEffect);
//					}
	//
//					// We put the dico in our principal var
//					if (!this.effectPerTypes.containsKey(type[1])) {
	//
//						this.effectPerTypes.put(type[1], types);
	//
//					}
	//
//				}
//			} catch (IOException e) {
//				System.out.println("Exception reading the file : " + e.getMessage());
//			} finally {
//				try {
//					if (fileReader != null) {
//						fileReader.close();
//					}
//					if (bufferedReader != null) {
//						bufferedReader.close();
//					}
//					System.out.println("Finished reading readPokeTypeEffectsToOtherTypes");
//				} catch (IOException e) {
//					System.out.println("Exception closing the file : " + e.getMessage());
//				}
//			}
//		}

	// -----------------------------
	// Reads attacksList.csv file and adds to attacks list
	// -----------------------------
	public void readAttacks(ArrayList<PokemonType> types) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(SAMPLE_CSV_ALL_ATTACKS);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] attacks = line.split(",");

				Attack attack = new Attack(Integer.parseInt(attacks[0]), attacks[1], attacks[2].toUpperCase(),
						Integer.parseInt(attacks[3]), Integer.parseInt(attacks[4]), Integer.parseInt(attacks[5]),
						attacks[6]);

				// Some attacks can have 2 bases (so we split with ";")
				String[] bs = attacks[7].split(";");

				if (bs.length > 1)
					for (String s : bs)
						attack.addBase(s);
				else
					attack.addBase(bs[0]);

				// Set the type of the attack to his Pokemon type instead of a string
				attack.transformStrTypeToPokemonType(types);

				// Add the attacks that can hit while Pokemon facing is invulnerable
				setCanHitInvulnerableAttacks(attack);
				// Set the category type of the attack
				setCategoryAttackType(attack);
				// Set the attack is One-Hit KO
				setAttackIsOneHit(attack);
				// Set the attack if makes contact
				setAttackMakesContact(attack);
				// Set the attack if has secondary effects
				setAttackHasSecondaryEffects(attack);
				// Set the attack if always hits
				setAttackAlwaysHits(attack);
				// Set the attack can hurt its self if fails
				setCanRecieveDamageFailAttacks(attack);
				// Set attack always hits under a specific weather
				setAttackAlwaysHeatswithWeather(attack);
				// Set if attack forces to change Pokemon
				setAttackForceChange(attack);
				// Set if attack is punch type
				setAttackIsPunch(attack);
				// Set the attack is applied to attacker its self
				setAttackIsAppliedOnItsSelf(attack);
				// Set if attack is state type (don't do damage and have support/secondary
				// effects, etc. against PK facing)
				setIsStateAttackAgainstPkFacing(attack);

				// Adds the attack to the general var
				this.getAttacks().add(attack);
				this.getAttackById().put(attack.getId(), attack);
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();

				if (bufferedReader != null)
					bufferedReader.close();

				System.out.println("Finished reading readAttacks");
			} catch (IOException e) {
				System.out.println("Exception closing the file : " + e.getMessage());
			}
		}
	}

	// -----------------------------
	// Reads attacks for each Pokemon
	// -----------------------------
	public void readAttacksForEachPokemon(Map<Integer, Pokemon> pokemonById) {
		try (BufferedReader bufferedReader = new BufferedReader(
				new FileReader(SAMPLE_CSV_ALL_ATTACKS_FOREACH_POKEMON))) {

			bufferedReader.readLine(); // skip header
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] cols = line.split(",");

				int pokemonId = Integer.parseInt(cols[0]);
				Pokemon pk = pokemonById.get(pokemonId);

				if (pk == null)
					continue;

				// Physical
				if (!cols[1].equals("0")) {
					for (String idStr : cols[1].split(";")) {
						Attack a = this.getAttackById().get(Integer.parseInt(idStr));
						if (a != null) {
							Attack attackDeepCopy = new Attack(a);
							pk.addPhysicalAttack(attackDeepCopy);
						}
					}
				}
				// Put "Struggle" to all Pokemon (used when has no remaining PP on the principal
				// attacks)
				pk.addPhysicalAttack(this.getAttackById().get(165));

				// Special
				if (!cols[2].equals("0")) {
					for (String idStr : cols[2].split(";")) {
						Attack a = this.getAttackById().get(Integer.parseInt(idStr));
						if (a != null) {
							Attack attackDeepCopy = new Attack(a);
							pk.addSpecialAttack(attackDeepCopy);
						}
					}
				}

				// Other
				if (!cols[3].equals("0")) {
					for (String idStr : cols[3].split(";")) {
						Attack a = this.getAttackById().get(Integer.parseInt(idStr));
						if (a != null) {
							Attack attackDeepCopy = new Attack(a);
							pk.addOtherAttack(attackDeepCopy);
						}
					}
				}
			}

			System.out.println("Finished reading readAttacksForEachPokemon");

		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		}
	}

	// -----------------------------
	// Reads attacksForeachPokemon.txt file and adds to each Pokemon
	// -----------------------------
//		public void readAttacksForEachPokemon() {
//			FileReader fileReader = null;
//			BufferedReader bufferedReader = null;
	//
//			try {
//				fileReader = new FileReader(SAMPLE_CSV_ALL_ATTACKS_FOREACH_POKEMON);
//				bufferedReader = new BufferedReader(fileReader);
	//
//				// Skips first line
//				bufferedReader.readLine();
//				String line;
	//
//				while ((line = bufferedReader.readLine()) != null) {
	//
//					Optional<Pokemon> pkOpt;
//					Optional<Attack> pAtaOpt;
	//
//					String[] pkAttacks = line.split(",");
	//
//					// Each string[] can have several values: we split them by ";"
//					String[] pkPhAttacks = pkAttacks[1].split(";");
//					String[] pkSpAttacks = pkAttacks[2].split(";");
//					String[] pkOtAttacks = pkAttacks[3].split(";");
	//
//					// Gets the current Pokemon of the line from the Pokemon list
//					pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(pkAttacks[0])).findFirst();
	//
//					if (pkOpt.isPresent()) {
	//
//						// Physical attacks
//						// Some Pokemon can have 0 attacks in a type of attack
//						if (pkPhAttacks.length >= 1 && Integer.parseInt(pkPhAttacks[0]) != 0) {
	//
//							for (String phAtt : pkPhAttacks) {
	//
//								pAtaOpt = this.attacks.stream().filter(a -> a.getId() == Integer.parseInt(phAtt))
//										.findFirst();
	//
//								if (pAtaOpt.isPresent()) {
	//
//									pkOpt.get().addPhysicalAttack(pAtaOpt.get());
	//
//								}
//							}
//						}
	//
//						// Special attacks
//						if (pkSpAttacks.length >= 1 && Integer.parseInt(pkSpAttacks[0]) != 0) {
	//
//							for (String spAtt : pkSpAttacks) {
	//
//								pAtaOpt = this.attacks.stream().filter(a -> a.getId() == Integer.parseInt(spAtt))
//										.findFirst();
	//
//								if (pAtaOpt.isPresent()) {
	//
//									pkOpt.get().addSpecialAttack(pAtaOpt.get());
	//
//								}
//							}
//						}
	//
//						// Other attacks
//						if (pkOtAttacks.length >= 1 && Integer.parseInt(pkOtAttacks[0]) != 0) {
	//
//							for (String otAtt : pkOtAttacks) {
	//
//								pAtaOpt = this.attacks.stream().filter(a -> a.getId() == Integer.parseInt(otAtt))
//										.findFirst();
	//
//								if (pAtaOpt.isPresent()) {
	//
//									pkOpt.get().addOtherAttack(pAtaOpt.get());
	//
//								}
//							}
//						}
//					}
//				}
//			} catch (IOException e) {
//				System.out.println("Exception reading the file  : " + e.getMessage());
//			} finally {
//				try {
//					if (fileReader != null) {
//						fileReader.close();
//					}
//					if (bufferedReader != null) {
//						bufferedReader.close();
//					}
//					System.out.println("Finished reading readAttacksForEachPokemon");
//				} catch (IOException e) {
//					System.out.println("Exception closing the file  : " + e.getMessage());
//				}
//			}
//		}

	private static void setAbilityIsWeatherType(Ability ability) {
		switch (ability.getId()) {
		// Llovizna/Drizzle
		case 2:
			ability.setIsWeatherType(true);
			break;
		// Chorro arena/Sand stream
		case 45:
			ability.setIsWeatherType(true);
			break;
		// Sequía/Drought
		case 70:
			ability.setIsWeatherType(true);
			break;
		default:
			ability.setIsWeatherType(false);
			break;
		}
	}

	// -----------------------------
	// Add the attacks that can hit while Pokemon facing is invulnerable
	// -----------------------------
	private static void setCanHitInvulnerableAttacks(Attack attack) {
		List<Integer> canHitWhileInvulnerable = new ArrayList<>();

		switch (attack.getId()) {
		case 16:
			canHitWhileInvulnerable.add(19);
			canHitWhileInvulnerable.add(340);
			canHitWhileInvulnerable.add(507);
		case 57:
			canHitWhileInvulnerable.add(291);
		case 89:
			canHitWhileInvulnerable.add(91);
		case 87:
			canHitWhileInvulnerable.add(19);
		case 239:
			canHitWhileInvulnerable.add(19);
		case 327:
			canHitWhileInvulnerable.add(19);
		case 479:
			canHitWhileInvulnerable.add(19);
		case 542:
			canHitWhileInvulnerable.add(19);
			break;
		}

		// Some charged attacks can be hit by all the movements (13_Razor_Wind /
		// 76_Solar_Beam)
		canHitWhileInvulnerable.add(13);
		canHitWhileInvulnerable.add(76);

		attack.setCanHitWhileInvulnerable(canHitWhileInvulnerable);
	}

	// -----------------------------
	// Set the attack if can hurt Pokemon owner if it fails
	// -----------------------------
	private static void setCanRecieveDamageFailAttacks(Attack attack) {
		switch (attack.getId()) {
		case 26:
			attack.setCanRecieveDamage(true);
			break;
		}
	}

	// -----------------------------
	// Set the category type of the attack
	// -----------------------------
	private static void setCategoryAttackType(Attack attack) {
		switch (attack.getId()) {
		case 19:
		case 76:
			attack.setCategory(AttackCategory.CHARGED);
			break;
		default:
			attack.setCategory(AttackCategory.NORMAL);
		}
	}

	// -----------------------------
	// Set if the attack is one hit KO
	// -----------------------------
	private static void setAttackIsOneHit(Attack attack) {
		switch (attack.getId()) {
		case 12:
		case 32:
			attack.setIsOneHitKO(true);
			break;
		default:
			attack.setIsOneHitKO(false);
		}
	}

	// -----------------------------
	// Set if the attack makes contact (physical attack)
	// -----------------------------
	private static void setAttackMakesContact(Attack attack) {
		if (attack.getBases() != null && attack.getBases().contains("fisico"))
			attack.setMakesContact(true);
	}

	// -----------------------------
	// Set if the attack makes contact (physical attack)
	// -----------------------------
	private static void setAttackIsAppliedOnItsSelf(Attack attack) {
		switch (attack.getId()) {
		case 14:
		case 74:
		case 96:
		case 97:
			attack.setAppliedToAttacker(true);
			break;
		default:
			attack.setAppliedToAttacker(false);
		}
	}

	// -----------------------------
	// Set if the attack is state type and has secondary effects agianst Pokémon
	// facing, or support on attacker etc. => used for abilities like
	// 147_Wonder_Skin
	// -----------------------------
	private static void setIsStateAttackAgainstPkFacing(Attack attack) {
		switch (attack.getId()) {
		case 18:
		case 28:
		case 39:
		case 43:
		case 45:
		case 46:
		case 47:
		case 48:
		case 50:
		case 54:
		case 73:
		case 77:
		case 78:
		case 79:
		case 81:
		case 86:
		case 92:
		case 95:
		case 103:
		case 108:
		case 109:
		case 114:
		case 134:
		case 137:
		case 139:
		case 142:
		case 147:
		case 148:
		case 169:
		case 171:
		case 174: // if used by a ghost type
		case 178:
		case 180:
		case 184:
		case 186:
		case 191:
		case 193: // if used by a ghost type
		case 194:
		case 195:
		case 201:
		case 204:
		case 207:
		case 212:
		case 213:
		case 227:
		case 230:
		case 240:
		case 241:
		case 258:
		case 259:
		case 260:
		case 261:
		case 262:
		case 266:
		case 269:
		case 270:
		case 271:
		case 273:
		case 277:
		case 281:
		case 285:
		case 286:
		case 288:
		case 297:
		case 298:
		case 300:
		case 313:
		case 316: // if used by a ghost type
		case 319:
		case 320:
		case 321:
		case 335:
		case 346:
		case 356:
		case 357: // if used by a dark type
		case 373:
		case 375:
		case 377:
		case 380:
		case 384:
		case 385:
		case 388:
		case 390:
		case 391:
		case 415:
		case 432:
		case 433:
		case 445:
		case 446:
		case 464:
		case 470:
		case 471:
		case 472:
		case 477:
		case 478:
		case 487:
		case 493:
		case 494:
		case 495:
		case 511:
		case 516:
		case 564:
		case 567:
		case 568:
		case 569:
		case 571:
		case 575:
		case 576:
		case 579: // not sure (support type ?)
		case 580: // not sure (support type ?)
		case 581: // not sure (support type ?)
		case 582:
		case 587:
		case 589:
		case 590:
		case 596:
		case 598:
		case 599:
		case 600:
		case 604:
		case 608:
		case 661:
		case 668:
		case 672:
		case 678:
		case 683:
		case 685: // not sure
		case 689: // not sure
		case 715:
		case 747:
		case 748:
		case 749:
		case 750:
		case 752:
		case 756:
		case 777: // not sure
		case 792:
		case 810:
		case 852:
		case 858:
		case 881:
		case 883:
		case 909:
			attack.setIsStateAttackAgainstPkFacing(true);
			break;
		default:
			attack.setIsStateAttackAgainstPkFacing(false);
		}
	}

	// -----------------------------
	// Set if the attack has secondary effects
	// -----------------------------
	private static void setAttackHasSecondaryEffects(Attack attack) {
		SecondaryEffect secondaryEffect = new SecondaryEffect();

		switch (attack.getId()) {
		case 7:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.BURNED, 0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 8:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.FROZEN, 0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 9:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.PARALYZED,
					0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 23:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.FLINCH, 0.30);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 27:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.FLINCH, 0.30);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 29:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.FLINCH, 0.30);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 34:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.PARALYZED,
					0.30);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 40:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.POISONED,
					0.30);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 41:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.POISONED,
					0.20);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 44:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.FLINCH, 0.30);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 51:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STAT_DROP, StatType.DEFENSE, 1, 0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 52:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.BURNED, 0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 53:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.BURNED, 0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 58:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.FROZEN, 0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 59:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.FROZEN, 0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 60:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.EPHEMERAL_STATUS, StatusConditions.CONFUSED,
					0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 61:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STAT_DROP, StatType.SPEED, 1, 0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 62:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STAT_DROP, StatType.ATTACK, 1, 0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 84:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.PARALYZED,
					0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 85:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.PARALYZED,
					0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		case 87:
			secondaryEffect = new SecondaryEffect(SecondaryEffectType.STATUS_CONDITION, StatusConditions.PARALYZED,
					0.10);
			attack.addSecondaryEffect(secondaryEffect);
			break;
		default:
			attack.setSecondaryEffectsNull();
			break;
		}
	}

	// -----------------------------
	// Set if attack always hits
	// -----------------------------
	private static void setAttackAlwaysHits(Attack attack) {
		switch (attack.getId()) {
		case 14:
		case 18:
		case 46:
		case 74:
		case 54:
		case 165: // "Struggle" attack has 100% of precision (used when no more PPs remaining on
					// other attacks, etc.)
			attack.setAlwaysHits(true);
			break;
		default:
			attack.setAlwaysHits(false);
		}
	}

	// -----------------------------
	// Set if the attack always hits with a specific weather
	// -----------------------------
	private static void setAttackAlwaysHeatswithWeather(Attack attack) {
		switch (attack.getId()) {
		case 87:
			attack.setGuaranteedWeather(Weather.RAIN);
			break;
		default:
			attack.setGuaranteedWeather(Weather.NONE);
		}
	}

	// -----------------------------
	// Set if the attack forces the Pokemon rival to change
	// -----------------------------
	private static void setAttackForceChange(Attack attack) {
		switch (attack.getId()) {
		case 18:
		case 46:
			attack.setForceChange(true);
			break;
		default:
			attack.setForceChange(false);
		}
	}

	// -----------------------------
	// Set if the attack is a punch move (used for 89_Iron_fist ability)
	// -----------------------------
	private static void setAttackIsPunch(Attack attack) {
		switch (attack.getId()) {
		case 838:
		case 818:
		case 742:
		case 327:
		case 817:
		case 359:
		case 665:
		case 5:
		case 264:
		case 418:
		case 4:
		case 223:
		case 409:
		case 7:
		case 889:
		case 8:
		case 612:
		case 857:
		case 146:
		case 309:
		case 325:
		case 9:
		case 721:
		case 183:
			attack.setPunchMove(true);
			break;
		default:
			attack.setPunchMove(false);
		}
	}

	// -----------------------------
	// Parse string to int
	// -----------------------------
	private ArrayList<Integer> parseIntList(String value) {
		ArrayList<Integer> list = new ArrayList<>();

		if (value == null || value.equals("0") || value.isEmpty())
			return list;

		String[] values = value.split(";");
		for (String v : values)
			list.add(Integer.parseInt(v.trim()));

		return list;
	}
}
