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
import pokemon.interfce.StenchAbility;
import pokemon.model.Ability;
import pokemon.model.Attack;
import pokemon.model.Pokemon;
import pokemon.model.PokemonType;

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

	public void setEffectPerTypes(HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {
		this.effectPerTypes = effectPerTypes;
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

				if (pokemon.size() == 809) {

					break;

				} else {

					Pokemon pokemonToAdd = new Pokemon(Integer.parseInt(pks[0]), pks[1], Integer.parseInt(pks[2]),
							Integer.parseInt(pks[3]), Integer.parseInt(pks[4]), Integer.parseInt(pks[5]),
							Integer.parseInt(pks[6]), Integer.parseInt(pks[7]));

					// Gets first ability (all Pokemon have at least one ability)
					if (!pks[12].isEmpty()) {

						for (PokemonType pkty : types) {

							if (pkty.getId() == Integer.parseInt(pks[12])) {

								pokemonToAdd.addType(pkty);

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
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
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

				if (this.getAbilities().size() == 309) {

					break;

				} else {

					Ability abilityToAdd = new Ability(Integer.parseInt(ablty[0]), ablty[1].toUpperCase(), ablty[2]);
					setAbilityEffect(abilityToAdd);
					
					this.getAbilities().add(abilityToAdd);
					abilities.add(abilityToAdd);
				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
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
				String[] abltysPk = line.split(",");

				// Gets the current Pokemon of the line from the Pokemon list
				pkOpt = pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(abltysPk[0])).findFirst();

				if (pkOpt.isPresent()) {

					// Gets first ability (all Pokemon have at least one ability)
					if (!abltysPk[8].isEmpty()) {

						for (Ability ablty : this.getAbilities()) {

							if (ablty.getId() == Integer.parseInt(abltysPk[8])) {

								pkOpt.get().addNormalAbility(ablty);

							}
						}
					}

					// Gets other abilities (if a Pokemon has more)
					if (abltysPk.length > 9) {

						// Gets second ability
						if (!abltysPk[9].isEmpty()) {

							for (Ability ablty : this.getAbilities()) {

								if (ablty.getId() == Integer.parseInt(abltysPk[9])) {

									pkOpt.get().addNormalAbility(ablty);

								}
							}
						}

						// Gets hidden ability
						if (!abltysPk[10].isEmpty()) {

							for (Ability ablty : this.getAbilities()) {

								if (ablty.getId() == Integer.parseInt(abltysPk[10])) {

									pkOpt.get().addHiddenAbility(ablty);

								}
							}
						}

						// Gets second hidden ability : only one Pokemon at the moment
						if (abltysPk.length == 12) {

							if (!abltysPk[11].isEmpty()) {

								for (Ability ablty : this.getAbilities()) {

									if (ablty.getId() == Integer.parseInt(abltysPk[11])) {

										pkOpt.get().addHiddenAbility(ablty);

									}
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
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
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

							if (pkty.getId() == Integer.parseInt(typesRead[12])) {

								pkOpt.get().addType(pkty);

							}
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

								if (pkty.getId() == Integer.parseInt(typesRead[13])) {

									pkOpt.get().addType(pkty);

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
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
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

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {

				String[] pkTypes = line.split(",");

				if (types.size() == 18) {

					break;

				} else {

					PokemonType pkType = new PokemonType(Integer.parseInt(pkTypes[0]), pkTypes[1].toUpperCase());
					types.add(pkType);
					typeById.put(pkType.getId(), pkType);

				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file  : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				System.out.println("Finished reading readPkTypes");
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	// -----------------------------
	// Reads typesList.csv file and adds the effects against other types
	// -----------------------------
	public void readPkTypesEffectsToOtherTypes(Map<Integer, PokemonType> typeById) {
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
				this.getEffectPerTypes().put(typeName, types);
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

				if (bs.length > 1) {

					for (String s : bs) {

						attack.addBase(s);

					}

				} else {

					attack.addBase(bs[0]);

				}

				// Set the type of the attack to his Pokemon type instead of a string
				attack.transformStrTypeToPokemonType(types);

				// Add the attacks that can hit while Pokemon facing is invulnerable
				putCanHitInvulnerableAttacks(attack);

				// Set the category type of the attack
				setCategoryAttackType(attack);

				// Adds the attack to the general var
				this.getAttacks().add(attack);
				this.getAttackById().put(attack.getId(), attack);
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
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
						if (a != null)
							pk.addPhysicalAttack(a);
					}
				}
				// Put "Struggle" to all Pokemon (used when has no remaining PP on the principal
				// attacks)
				pk.addPhysicalAttack(this.getAttackById().get(165));

				// Special
				if (!cols[2].equals("0")) {
					for (String idStr : cols[2].split(";")) {
						Attack a = this.getAttackById().get(Integer.parseInt(idStr));
						if (a != null)
							pk.addSpecialAttack(a);
					}
				}

				// Other
				if (!cols[3].equals("0")) {
					for (String idStr : cols[3].split(";")) {
						Attack a = this.getAttackById().get(Integer.parseInt(idStr));
						if (a != null)
							pk.addOtherAttack(a);
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

	// -----------------------------
	// Add the attacks that can hit while Pokemon facing is invulnerable
	// -----------------------------
	public static void putCanHitInvulnerableAttacks(Attack attack) {
		List<Integer> canHitWhileInvulnerable = new ArrayList<>();

		switch (attack.getId()) {
		case 16:
		case 87:
		case 239:
		case 327:
		case 479:
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
	// Set attack that can hurt Pokemon owner if fails
	// -----------------------------
	public static void putCanRecieveDamageFailAttacks(Attack attack) {
		switch (attack.getId()) {
		case 26:
			attack.setCanRecieveDamage(true);
			break;
		}
	}

	// -----------------------------
	// Set attack that can flinch/retreat Pokemon
	// -----------------------------
	public void putCanFlinchAttacks(Attack attack) {
		switch (attack.getId()) {
		case 23:
		case 27:
		case 29:
		case 44:
			attack.setCanBeFlinched(true);
			break;
		default:
			attack.setCanBeFlinched(false);
		}
	}

	// -----------------------------
	// Set the category type of the attack
	// -----------------------------
	public void setCategoryAttackType(Attack attack) {
		switch (attack.getId()) {
		case 19:
			attack.setCategory(AttackCategory.CHARGED);
			break;
		default:
			attack.setCategory(AttackCategory.NORMAL);
		}
	}

	// -----------------------------
	// Set the category type of the attack
	// -----------------------------
	public void setAbilityEffect(Ability ability) {
		switch (ability.getId()) {
		case 1:
			ability.setEffect(new StenchAbility());
			break;
		default:
			ability.setEffect(new StenchAbility());
		}
	}
}
