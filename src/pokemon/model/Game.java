package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import pokemon.importData.ReaderData;
import pokemon.importData.ScrappingWeb;
import pokemon.importData.WritterData;

public class Game {

	// ==================================== FIELDS
	// ====================================

	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_RESET = "\u001B[0m";

	private ArrayList<Pokemon> pokemon;
	private HashMap<String, ArrayList<PokemonType>> pokemonTypePerPokemon;
	private ArrayList<Ability> abilities;
	private HashMap<String, HashMap<String, ArrayList<Ability>>> abilitiesPerPokemon;
	private ArrayList<PokemonType> types;
	private HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes;
	private ArrayList<Attack> attacks;
	private HashMap<Integer, HashMap<String, ArrayList<Integer>>> attacksPerPokemon;
	private HashMap<String, ArrayList<Pokemon>> pokemonPerType;
	private Map<Integer, PokemonType> typeById = new HashMap<>();
	private Map<Integer, Pokemon> pokemonById = new HashMap<>();
	private Map<Integer, Attack> attackById = new HashMap<>();
	private ScrappingWeb scrappingWeb;
	private WritterData writterData;
	private ReaderData readerData;

	private Player player;
	private IAPlayer IA;

	private final AbilityService abilityService;

	// ==================================== CONSTRUCTORS
	// ====================================

	public Game() {
		this.pokemon = new ArrayList<>();
		this.pokemonTypePerPokemon = new HashMap<>();
		this.abilities = new ArrayList<>();
		this.abilitiesPerPokemon = new HashMap<>();
		this.types = new ArrayList<>();
		this.effectPerTypes = new HashMap<>();
		this.attacks = new ArrayList<>();
		this.attacksPerPokemon = new HashMap<>();
		this.player = new Player();
		this.IA = new IAPlayer();
		this.pokemonPerType = new HashMap<>();
		this.typeById = new HashMap<>();
		this.pokemonById = new HashMap<>();
		this.attackById = new HashMap<>();
		this.scrappingWeb = new ScrappingWeb();
		this.writterData = new WritterData();
		this.readerData = new ReaderData();
		this.abilityService = new AbilityService();
	}

	// ==================================== GETTERS/SETTERS
	// ====================================

	public ArrayList<Pokemon> getPokemon() {
		return pokemon;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public HashMap<String, HashMap<String, ArrayList<Ability>>> getAbilitiesPerPokemon() {
		return abilitiesPerPokemon;
	}

	public HashMap<String, ArrayList<PokemonType>> getPokemonTypePerPokemon() {
		return pokemonTypePerPokemon;
	}

	public ArrayList<PokemonType> getTypes() {
		return types;
	}

	public HashMap<String, HashMap<String, ArrayList<PokemonType>>> getEffectPerTypes() {
		return effectPerTypes;
	}

	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

	public HashMap<Integer, HashMap<String, ArrayList<Integer>>> getAttacksPerPokemon() {
		return attacksPerPokemon;
	}

	public Player getPlayer() {
		return player;
	}

	public IAPlayer getIA() {
		return IA;
	}

	public HashMap<String, ArrayList<Pokemon>> getPokemonPerType() {
		return pokemonPerType;
	}

	public Map<Integer, PokemonType> getTypeById() {
		return typeById;
	}

	public Map<Integer, Pokemon> getPokemonById() {
		return pokemonById;
	}

	public Map<Integer, Attack> getAttackById() {
		return attackById;
	}

	public ScrappingWeb getScrappingWeb() {
		return scrappingWeb;
	}

	public WritterData getWritterData() {
		return writterData;
	}

	public ReaderData getReaderData() {
		return readerData;
	}

	// ==================================== METHODS
	// ====================================

	// -----------------------------
	// Prints all the Pokemon
	// -----------------------------
	public void printPokemon() {
		for (Pokemon pk : this.getPokemon()) {
			System.out.println(pk.getId() + " - " + pk.getName() + " - " + pk.getTypes().size() + " :");

			pk.getTypes().forEach(tp -> {
				System.out.println(tp.getName());
			});
		}
	}

	// -----------------------------
	// Order Pokemon by type
	// -----------------------------
	public void classPkPerType() {
		ArrayList<Pokemon> steelType = new ArrayList<>();
		ArrayList<Pokemon> waterType = new ArrayList<>();
		ArrayList<Pokemon> fireType = new ArrayList<>();
		ArrayList<Pokemon> bugType = new ArrayList<>();
		ArrayList<Pokemon> dragonType = new ArrayList<>();
		ArrayList<Pokemon> electricType = new ArrayList<>();
		ArrayList<Pokemon> ghostType = new ArrayList<>();
		ArrayList<Pokemon> fairyType = new ArrayList<>();
		ArrayList<Pokemon> iceType = new ArrayList<>();
		ArrayList<Pokemon> fightingType = new ArrayList<>();
		ArrayList<Pokemon> normalType = new ArrayList<>();
		ArrayList<Pokemon> grassType = new ArrayList<>();
		ArrayList<Pokemon> psychicType = new ArrayList<>();
		ArrayList<Pokemon> rockType = new ArrayList<>();
		ArrayList<Pokemon> darkType = new ArrayList<>();
		ArrayList<Pokemon> groundType = new ArrayList<>();
		ArrayList<Pokemon> poisonType = new ArrayList<>();
		ArrayList<Pokemon> flyingType = new ArrayList<>();

		Optional<PokemonType> pkTOpt;

		for (Pokemon pk : this.getPokemon()) {
			// If 2 types, puts the Pokemon in the two different types
			for (PokemonType pkty : pk.getTypes()) {
				pkTOpt = this.getTypes().stream().filter(t -> t.getId() == pkty.getId()).findFirst();

				if (pkTOpt.isPresent()) {
					switch (pkTOpt.get().getId()) {
					case 1:
						steelType.add(pk);
						break;
					case 2:
						waterType.add(pk);
						break;
					case 3:
						bugType.add(pk);
						break;
					case 4:
						dragonType.add(pk);
						break;
					case 5:
						electricType.add(pk);
						break;
					case 6:
						ghostType.add(pk);
						break;
					case 7:
						fireType.add(pk);
						break;
					case 8:
						fairyType.add(pk);
						break;
					case 9:
						iceType.add(pk);
						break;
					case 10:
						fightingType.add(pk);
						break;
					case 11:
						normalType.add(pk);
						break;
					case 12:
						grassType.add(pk);
						break;
					case 13:
						psychicType.add(pk);
						break;
					case 14:
						rockType.add(pk);
						break;
					case 15:
						darkType.add(pk);
						break;
					case 16:
						groundType.add(pk);
						break;
					case 17:
						poisonType.add(pk);
						break;
					case 18:
						flyingType.add(pk);
						break;
					}
				}
			}
		}

		this.getPokemonPerType().put("ACERO", steelType);
		this.getPokemonPerType().put("AGUA", waterType);
		this.getPokemonPerType().put("BICHO", bugType);
		this.getPokemonPerType().put("DRAGON", dragonType);
		this.getPokemonPerType().put("ELECTRICO", electricType);
		this.getPokemonPerType().put("FANTASMA", ghostType);
		this.getPokemonPerType().put("FUEGO", fireType);
		this.getPokemonPerType().put("HADA", fairyType);
		this.getPokemonPerType().put("HIELO", iceType);
		this.getPokemonPerType().put("LUCHA", fightingType);
		this.getPokemonPerType().put("NORMAL", normalType);
		this.getPokemonPerType().put("PLANTA", grassType);
		this.getPokemonPerType().put("PSIQUICO", psychicType);
		this.getPokemonPerType().put("ROCA", rockType);
		this.getPokemonPerType().put("SINIESTRO", darkType);
		this.getPokemonPerType().put("TIERRA", groundType);
		this.getPokemonPerType().put("VENENO", poisonType);
		this.getPokemonPerType().put("VOLADOR", flyingType);

		System.out.println("Finished reading classPkPerType");
	}

	// -----------------------------
	// Regex to match Pokemon player choices
	// -----------------------------
	public String checkRegexToChoosePokemon() {
		// Player can choose with format : d,d,d,d,d,d, and numbers are between 1 and
		// 807
		String strRegex = "\\b([1-9]";

		// 1 to 99
		for (int i = 1; i <= 9; i++)
			// The "|" represents OR
			strRegex += "|" + i + "[0-9]";

		// 100 to 799
		for (int i = 10; i <= 79; i++)
			strRegex += "|" + i + "[0-9]";

		// Complete the rest : 800 to 807
		strRegex += "|800|801|802|803|804|805|806|807)\\b,";

		// Repeat regex 6 times cause 6 Pokemon
		strRegex = repeat(6, strRegex);

		// Remove last ","
		strRegex = StringUtils.chop(strRegex);

		return strRegex;
	}

	// -----------------------------
	// Repeats sequence of the string
	// -----------------------------
	public static String repeat(int count, String with) {
		return new String(new char[count]).replace("\0", with);
	}

	// -----------------------------
	// Repeats sequence of the string
	// -----------------------------
	public static String repeat(int count) {
		return repeat(count, " ");
	}

	// ==================================== GAME
	// ====================================

	// -----------------------------
	// Intialize all vars from files
	// -----------------------------
	public void InitiateVars() {
		// Instantiate all Pokemon (if CSV not already created)
//		this.pokemon = this.getScrappingWeb().scrappingWebPokemon();

		// Write all Pokemon to a CSV file
//		this.getWritterData().setPokemon(this.pokemon);
//	    this.getWritterData().writePokemonCSV(this.pokemon);

		// Instantiate all the abilities (if CSV not already created)
//		this.abilities = this.getScrappingWeb().scrappingWebAllAbs() ;

		// Write all abilities to a CSV file
//		this.getWritterData().setAbilities(this.abilities);
//		this.getWritterData().writeAbilitiesCSV(this.abilities);

		// Initialize the different lists
		this.getReaderData().readPkTypes(this.types, this.typeById);
//		this.getReaderData().readPokemon(SAMPLE_CSV_ALL_POKEMON, this.types, this.pokemon, this.pokemonById);
		this.getReaderData().readPokemon(this.types, this.pokemon, this.pokemonById);
		this.getReaderData().readAbilities(this.abilities);
		this.getReaderData().readAttacks(this.types);

		// Adds the abilities for different Pokemon (on the general list)
//		this.abilitiesPerPokemon =  this.getScrappingWeb().scrappingWebReadAbsFromPokemonAllTables();

		// Append abilities to pokemonList.csv (pokemonList2.csv)
//		this.getWritterData().setAbilitiesPerPokemon(this.abilitiesPerPokemon);
//		this.getWritterData().AppendAbilities();

		// Adds abilities to Pokemon (on the Pokemon)
		this.getReaderData().readAddAbsForEachPokemon(this.pokemon);

		// Puts the lists of different damages for each type
		this.getReaderData().readPkTypesEffectsToOtherTypes(this.typeById, this.effectPerTypes);

		// Adds the type for each Pokemon
//		this.pokemonTypePerPokemon = this.getScrappingWeb().scrappingWebReadTypeForEachPokemon();

		// Append Pokemon type to pokemonList.csv (pokemonList3.csv)
//		this.getWritterData().setPokemonTypePerPokemon(this.pokemonTypePerPokemon);
//		this.getWritterData().AppendPokemonTypes();

		// Adds types to Pokemon (on the Pokemon)
//		this.getReaderData().readAddTypesForEachPokemon(SAMPLE_CSV_ALL_POKEMON_TYPES, this.pokemon);

		// All the attacks
//		this.attacks = this.getScrappingWeb().scrappingWebAttacks();

		// Writes the attacks
//		this.getWritterData().setAttacks(this.attacks);
//		this.getWritterData().writeAttacksCSV();

		// Takes accents
//		this.getWritterData().writeAttacksCSV2();

		// All attacks for each Pokemon
//		this.attacksPerPokemon = this.getScrappingWeb().scrappingWebAttacksForEachPokemon();

		// Writes in a new CSV all the attacks for each Pokemon
//		this.getWritterData().setAttacksPerPokemon(this.attacksPerPokemon);
//		this.getWritterData().writeAttacksForEachPokemon();

		// Adds the attacks for each Pokemon
		this.getReaderData().readAttacksForEachPokemon(this.pokemonById);

		// Order Pokemon by type
		classPkPerType();
	}

	// -----------------------------
	// Start choice of Pokemon
	// -----------------------------
	public void PokemonChoice() {
		printPokemon();
		printGameDescription();

		Scanner sc = new Scanner(System.in);

		String allPkPlayer = askPlayerPokemonChoice(sc);

		addPokemonToPlayer(allPkPlayer);

		// Pokemon machine choices
		this.getIA().IAPokemonChoice(this.getPlayer().getPokemon(), this.getPokemonPerType(), this.getEffectPerTypes());

		initializePokemonAttacksAndAbilities();

		initializeFirstPokemonForBattle();

		printTeams();

		orderAttacksByDamage();

		// IA Prepares best attack against Pokemon player
		AttackAnalyzer.prepareBestAttackIA(this.getIA(), this.getPlayer().getPkCombatting());

		System.out.println("Next attack from machine :");
		System.out.println(this.getIA().getPkCombatting().getNextMovement().getName() + " - "
				+ this.getIA().getPkCombatting().getNextMovement().getPkType().getName());
	}

	// -----------------------------
	// Print Pokemon game description
	// -----------------------------
	private void printGameDescription() {
		System.out.println();
		System.out.println(
				"Escoge 6 Pokémon para el combate (todos están al nivel 100, con sus estadísticas a nivel máximo, como si fueran Pokémon favorecidos, es decir, su mejor versión),\n"
						+ "Puedes escoger el mismo Pokémon 6 veces seguidas. La máquina no podrá.\n"
						+ "Los ataques de cada Pokémon serán iniciados aleatoriamente una vez escojas los Pokémon. Lo que quiere decir que cada Pokémon idéntico, puede tener ataques diferentes. "
						+ "Cada Pokémon tendrá 1 ataque especial, 2 ataques normales y 1 ataque de tipo otro (protección, ataques de estado, etc.).\n"
						+ "Cada Pokémon tendrá ataques que puede tener en los juegos. Es decir, que no habrá un Charizard con surf."
						+ "Solo puedes escoger Pokémon entre el 1 y el 807. Algunos números no están disponibles, así que asegúrate de mirar bien la lista presentada arriba.\n"
						+ "No importa el orden en que los escojas, solo determina el primer Pokémon que va a salir (el primero en escoger)\n"
						+ "Ten en cuenta que por cada Pokémon que escojas, la máquina va a buscar el tipo que más le afecte a tu Pokémon\n"
						+ "Ten en cuenta, que quizás a la máquina no le salgan ataques muy favorecidos, ni a ti tampoco jeje\n"
						+ "Para escoger los Pokémon, utiliza el formato : número,número,número,número,número,número => ej : 31,45,3,69,500,666");

		System.out.println("Escoge tus 6 Pokémon :");
	}

	// -----------------------------
	// Player Pokemon selection
	// -----------------------------
	private String askPlayerPokemonChoice(Scanner sc) {
		String matchFormatChoice = checkRegexToChoosePokemon();

		String allPkPlayer = sc.next();
		sc.useDelimiter(";|\r?\n|\r");

		while (!allPkPlayer.matches(matchFormatChoice)) {
			System.out.println(
					"Para escoger los Pokémon, utiliza el formato :número,número,número,número,número,número y los números deben estar entre el 1 y el 807");

			allPkPlayer = sc.next();
			sc.useDelimiter(";|\r?\n|\r");
		}

		return allPkPlayer;
	}

	// -----------------------------
	// Add Pokmeon selected to player's list
	// -----------------------------
	private void addPokemonToPlayer(String allPkPlayer) {
		String[] pkByPkPlayer = allPkPlayer.split(",");

		for (String pkID : pkByPkPlayer) {
			Optional<Pokemon> pkOpt = this.getPokemon().stream().filter(pk -> pk.getId() == Integer.parseInt(pkID))
					.findFirst();

			if (pkOpt.isPresent())
				this.getPlayer().addPokemon(pkOpt.get());
			else {
				System.out.println("El número marcado no está en la lista : " + pkID);
				System.out.println("Tendrás que volver a escoger tus Pokémon (reinicia el juego)");
			}
		}
	}

	// -----------------------------
	// Put attacks and abilities to all Pokemon on game (player and IA)
	// -----------------------------
	private void initializePokemonAttacksAndAbilities() {
		AttackAnalyzer.addAttacksForEachPokemon(this.getPlayer());
		AttackAnalyzer.addAttacksForEachPokemon(this.getIA());

		abilityService.selectAbilityForEachPokemon(this.getPlayer(), this.getAbilities());
		abilityService.selectAbilityForEachPokemon(this.getIA(), this.getAbilities());
	}

	// -----------------------------
	// Set attackers and defenders to each player (for first turn)
	// -----------------------------
	private void initializeFirstPokemonForBattle() {
		this.getPlayer().setPkCombatting(this.getPlayer().getPokemon().get(0));
		this.getIA().setPkCombatting(this.getIA().getPokemon().get(0));

		this.getPlayer().setPkFacing(this.getIA().getPkCombatting());
		this.getIA().setPkFacing(this.getPlayer().getPkCombatting());
	}

	// -----------------------------
	// Print final teams
	// -----------------------------
	private void printTeams() {
		System.out.println("Player");
		printPokemonTeam(this.getPlayer().getPokemon());

		System.out.println();
		System.out.println("IA");
		printPokemonTeam(this.getIA().getPokemon());
	}

	private void printPokemonTeam(List<Pokemon> team) {
		for (Pokemon p : team) {
			System.out.println(p.getName() + ":");
			System.out.println();

			for (PokemonType pt : p.getTypes())
				System.out.println(pt.getName());

			for (Attack a : p.getFourPrincipalAttacks())
				System.out.println(a.getName() + " - " + a.getPkType().getName());

			System.out.println();
		}
	}

	// -----------------------------
	// Order attacks for each level of damage (for first turn)
	// -----------------------------
	private void orderAttacksByDamage() {
		AttackAnalyzer.orderAttacksByDamage(this.getIA().getPkCombatting(), this.getIA().getPkFacing(),
				this.getEffectPerTypes());
		AttackAnalyzer.orderAttacksByDamage(this.getPlayer().getPkCombatting(), this.getPlayer().getPkFacing(),
				this.getEffectPerTypes());
	}

	// -----------------------------
	// Main battle (start battle)
	// -----------------------------
	public void launchBattleService() {
		BattleContext battleCtx = new BattleContext(this.getPlayer(), this.getIA(), this.getEffectPerTypes(),
				this.getTypes());

		BattleService battleService = new BattleService(battleCtx);
		battleService.startBattle();
	}

	// -----------------------------
	// Tests for attacks (466 Electivire, 398 Staraptor, 6 Charizard, 127 Pinsir,
	// 123 Scyther, 16 Pidgey, 95 Onix, 523 Zebstrika, 106 Hitmonlee, 248 Tyranitar,
	// 382 Kyogre)
	// -----------------------------
	public void doTest() {
		// Sets the same Pk
		String allPkPlayer = "008,008,008";
		String allPkIA = "467,467,467";

		String[] pkByPkPlayer = allPkPlayer.split(",");
		Map<Integer, Integer> pkCount = new HashMap<>();

		// Add Pokemon to player
		for (String PkID : pkByPkPlayer) {
			int baseId = Integer.parseInt(PkID);

			Optional<Pokemon> pkOpt = this.getPokemon().stream().filter(pk -> pk.getId() == baseId).findFirst();

			if (pkOpt.isPresent()) {
				// Creates a new instance of Pokemon in memory (otherwise there are problems of
				// duplications)
				Pokemon newPk = new Pokemon(pkOpt.get());

				// Increase count for this base ID
				int count = pkCount.getOrDefault(baseId, 0);

				// If it's not the first one, modify ID
				if (count > 0) {
					int newId = baseId * 1000 + count;
					newPk.setId(newId);
				}

				// Update repetitions counter
				pkCount.put(baseId, count + 1);

				newPk.setOwner(this.getPlayer());

				// Add to player team
				this.getPlayer().addPokemon(newPk);
			}
		}

		// Sets first Pokemon to combat for player
		this.getPlayer().setPkCombatting(this.getPlayer().getPokemon().get(0));
		// this.getPlayer().getPkCombatting().setStatusCondition(new
		// State(StatusConditions.FROZEN));

		// Sets the attacks to pokemon's player to test
		for (Pokemon pk : this.getPlayer().getPokemon()) {
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 1).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 5).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 7).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 9).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 19).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 15).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 14).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 43).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 39).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 46).findFirst().get());
			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 54).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 27).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 22).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 29).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 33).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 5).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 8).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 10).findFirst().get());
//			pk.addAttacks(pk.getSpecialAttacks().stream().filter(af -> af.getId() == 84).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 38).findFirst().get());
		}

		pkCount = new HashMap<Integer, Integer>();
		String[] pkByPkIA = allPkIA.split(",");

		// Add Pokemon to IA
		for (String PkID : pkByPkIA) {
			int baseId = Integer.parseInt(PkID);

			Optional<Pokemon> pkOpt = this.getPokemon().stream().filter(pk -> pk.getId() == baseId).findFirst();

			if (pkOpt.isPresent()) {
				// Creates a new instance of Pokemon in memory (otherwise there are problems of
				// duplications)
				Pokemon newPk = new Pokemon(pkOpt.get());

				// Increase count for this base ID
				int count = pkCount.getOrDefault(baseId, 0);

				// If it's not the first one, modify ID
				if (count > 0) {
					int newId = baseId * 1000 + count;
					newPk.setId(newId);
				}

				// Update repetitions counter
				pkCount.put(baseId, count + 1);

				newPk.setOwner(this.getIA());

				// Add to player team
				this.getIA().addPokemon(newPk);
			}
		}

		// Sets first Pokemon to combat for IA
		this.getIA().setPkCombatting(this.getIA().getPokemon().get(0));

		for (Pokemon pk : this.getIA().getPokemon()) {
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 7).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 5).findFirst().get());
			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 9).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 19).findFirst().get());
//			pk.addAttacks(pk.getSpecialAttacks().stream().filter(af -> af.getId() == 57).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 34).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 47).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 45).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 28).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 15).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 5).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 18).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 17).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 15).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 29).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 47).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 33).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 40).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 73).findFirst().get());
//			AttackAnalyzer.addAttacksForEachPokemon(this.getIA());

		}

		// Select an ability for each Pokemon list
		abilityService.selectAbilityForEachPokemon(this.getPlayer(), this.getAbilities());
		abilityService.selectAbilityForEachPokemon(this.getIA(), this.getAbilities());

		// Sets Pokemon facing to each other
		this.getPlayer().setPkFacing(this.getIA().getPokemon().get(0));
		this.getIA().setPkFacing(this.getPlayer().getPokemon().get(0));

		AttackAnalyzer.orderAttacksByDamage(this.getIA().getPkCombatting(), this.getIA().getPkFacing(),
				this.getEffectPerTypes());
		AttackAnalyzer.orderAttacksByDamage(this.getPlayer().getPkCombatting(), this.getPlayer().getPkFacing(),
				this.getEffectPerTypes());
	}
}