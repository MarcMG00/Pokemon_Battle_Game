package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import pokemon.enums.StatusConditions;
import pokemon.importData.ReaderData;
import pokemon.importData.ScrappingWeb;
import pokemon.importData.WritterData;

public class Game {
	
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
	private PkVPk battleVS;
	private ScrappingWeb scrappingWeb;
	private WritterData writterData;
	private ReaderData readerData;

	private Player player;
	private IAPlayer IA;

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
		this.battleVS = new PkVPk(player, IA);
		this.scrappingWeb = new ScrappingWeb();
		this.writterData = new WritterData();
		this.readerData = new ReaderData();
	}

	public ArrayList<Pokemon> getPokemon() {
		return pokemon;
	}

	public void setPokemon(ArrayList<Pokemon> pokemon) {
		this.pokemon = pokemon;
	}

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

	public ArrayList<PokemonType> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PokemonType> types) {
		this.types = types;
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

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getIA() {
		return IA;
	}

	public void setIA(IAPlayer iA) {
		IA = iA;
	}

	public HashMap<String, ArrayList<Pokemon>> getPokemonPerType() {
		return pokemonPerType;
	}

	public void setPokemonPerType(HashMap<String, ArrayList<Pokemon>> pokemonPerType) {
		this.pokemonPerType = pokemonPerType;
	}

	public Map<Integer, PokemonType> getTypeById() {
		return typeById;
	}

	public void setTypeById(Map<Integer, PokemonType> typeById) {
		this.typeById = typeById;
	}

	public Map<Integer, Pokemon> getPokemonById() {
		return pokemonById;
	}

	public void setPokemonById(Map<Integer, Pokemon> pokemonById) {
		this.pokemonById = pokemonById;
	}

	public Map<Integer, Attack> getAttackById() {
		return attackById;
	}

	public void setAttackById(Map<Integer, Attack> attackById) {
		this.attackById = attackById;
	}

	public PkVPk getBattleVS() {
		return battleVS;
	}

	public void setBattleVS(PkVPk battleVS) {
		this.battleVS = battleVS;
	}

	public ScrappingWeb getScrappingWeb() {
		return scrappingWeb;
	}

	public void setScrappingWeb(ScrappingWeb scrappingWeb) {
		this.scrappingWeb = scrappingWeb;
	}

	public WritterData getWritterData() {
		return writterData;
	}

	public void setWritterData(WritterData writterData) {
		this.writterData = writterData;
	}

	public ReaderData getReaderData() {
		return readerData;
	}

	public void setReaderData(ReaderData readerData) {
		this.readerData = readerData;
	}

	// ==================================== GAME METHODS
	// ====================================

	// Prints all the Pokemon
	public void printPokemon() {
		for (Pokemon pk : this.pokemon) {

			System.out.println(pk.getId() + " - " + pk.getName() + " - " + pk.getTypes().size() + " :");

			pk.getTypes().forEach(tp -> {
				System.out.println(tp.getName());
			});
		}
	}

	// Order Pokemon by type
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

		for (Pokemon pk : this.pokemon) {

			// If 2 types, puts the Pokemon in the two different types
			for (PokemonType pkty : pk.getTypes()) {

				pkTOpt = this.types.stream().filter(t -> t.getId() == pkty.getId()).findFirst();

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

		this.pokemonPerType.put("ACERO", steelType);
		this.pokemonPerType.put("AGUA", waterType);
		this.pokemonPerType.put("BICHO", bugType);
		this.pokemonPerType.put("DRAGON", dragonType);
		this.pokemonPerType.put("ELECTRICO", electricType);
		this.pokemonPerType.put("FANTASMA", ghostType);
		this.pokemonPerType.put("FUEGO", fireType);
		this.pokemonPerType.put("HADA", fairyType);
		this.pokemonPerType.put("HIELO", iceType);
		this.pokemonPerType.put("LUCHA", fightingType);
		this.pokemonPerType.put("NORMAL", normalType);
		this.pokemonPerType.put("PLANTA", grassType);
		this.pokemonPerType.put("PSIQUICO", psychicType);
		this.pokemonPerType.put("ROCA", rockType);
		this.pokemonPerType.put("SINIESTRO", darkType);
		this.pokemonPerType.put("TIERRA", groundType);
		this.pokemonPerType.put("VENENO", poisonType);
		this.pokemonPerType.put("VOLADOR", flyingType);

		System.out.println("Finished reading classPkPerType");
	}

	// Regex to match Pokemon player choices
	public String checkRegexToChoosePokemon() {

		// Player can choose with format : d,d,d,d,d,d, and numbers are between 1 and
		// 807
		String strRegex = "\\b([1-9]";

		// 1 to 99
		for (int i = 1; i <= 9; i++) {

			// The "|" represents OR
			strRegex += "|" + i + "[0-9]";

		}

		// 100 to 799
		for (int i = 10; i <= 79; i++) {

			strRegex += "|" + i + "[0-9]";

		}

		// Complete the rest : 800 to 807
		strRegex += "|800|801|802|803|804|805|806|807)\\b,";

		// Repeat regex 6 times cause 6 Pokemon
		strRegex = repeat(6, strRegex);

		// Remove last ","
		strRegex = StringUtils.chop(strRegex);

		return strRegex;
	}

	// Repeats sequence of the string
	public static String repeat(int count, String with) {
		return new String(new char[count]).replace("\0", with);
	}

	public static String repeat(int count) {
		return repeat(count, " ");
	}

	// ==================================== GAME
	// ====================================

	// Intialize all vars from files
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
		this.getReaderData().readAbilities();
		this.getReaderData().readAttacks(this.types);

		// Adds the abilities for different Pokemon (on the general list)
//		this.abilitiesPerPokemon =  this.getScrappingWeb().scrappingWebReadAbsFromPokemonAllTables();

		// Append abilities to pokemonList.csv (pokemonList2.csv)
//		this.getWritterData().setAbilitiesPerPokemon(this.abilitiesPerPokemon);
//		this.getWritterData().AppendAbilities();

		// Adds abilities to Pokemon (on the Pokemon)
		this.getReaderData().readAddAbsForEachPokemon(this.pokemon);

		// Puts the lists of different damages for each type
		this.getReaderData().readPkTypesEffectsToOtherTypes(this.typeById);

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

	@SuppressWarnings("resource")
	public void PokemonChoice() {
		printPokemon();
		System.out.println();
		// Description of the game
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
						+ "Para escoger los Pokémon, utiliza el formato : número,número,número,número,número,número\n => ej : 31,45,3,69,500,666");

		System.out.println("Escoge tus 6 Pokémon :");

		// Player choice
		Scanner sc = new Scanner(System.in);
		String allPkPlayer = sc.next();
		sc.useDelimiter(";|\r?\n|\r");

		// Regex that match the Pokemon choices
		String matchFormatChoice = checkRegexToChoosePokemon();

		// While it doesn't match
		if (!allPkPlayer.matches(matchFormatChoice)) {

			while (!allPkPlayer.matches(matchFormatChoice)) {

				System.out.println(
						"Para escoger los Pokémon, utiliza el formato :número,número,número,número,número,número y los números deben estar entre el 1 y el 807");
				allPkPlayer = sc.next();
				sc.useDelimiter(";|\r?\n|\r");

				System.out.println(allPkPlayer.split(","));
			}
		}

		// Adds the Pokemon to player Pokemon list
		String[] pkByPkPlayer = allPkPlayer.split(",");

		for (String PkID : pkByPkPlayer) {

			Optional<Pokemon> pkOpt;
			pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(PkID)).findFirst();

			if (pkOpt.isPresent()) {

				player.addPokemon(pkOpt.get());

			} else {

				System.out.println("El número marcado no está en la lista : " + PkID);
				System.out.println("Tendrás que volver a escoger tus Pokémon (reinicia el juego)");
			}
		}

		// Pokemon machine choices
		IA.IAPokemonChoice(player.getPokemon(), this.pokemonPerType, this.effectPerTypes);

		// Add attacks to each Pokemon list
		player.addAttacksForEachPokemon();
		IA.addAttacksForEachPokemon();

		// Sets first Pokemon chosen for the combat
		player.setPkCombatting(player.getPokemon().get(0));
		IA.setPkCombatting(IA.getPokemon().get(0));

		player.setPkFacing(IA.getPkCombatting());
		IA.setPkFacing(player.getPkCombatting());

		System.out.println("Player");

		// Shows Pokemon from player
		for (Pokemon p : player.getPokemon()) {

			System.out.println(p.getName() + ":");
			System.out.println();

			// Shows types from Pokemon
			for (PokemonType pt : p.getTypes()) {

				System.out.println(pt.getName());
			}

			// Shows attacks from Pokemon
			for (Attack a : p.getFourPrincipalAttacks()) {

				System.out.println(a.getName() + " - " + a.getStrTypeToPkType().getName());
			}

			System.out.println();
		}

		System.out.println();
		System.out.println("IA");

		// Shows Pokemon from IA
		for (Pokemon p : IA.getPokemon()) {

			System.out.println(p.getName() + ":");
			System.out.println();

			// Shows types from Pokemon
			for (PokemonType pt : p.getTypes()) {

				System.out.println(pt.getName());
			}

			// Shows attacks from Pokemon
			for (Attack a : p.getFourPrincipalAttacks()) {

				System.out.println(a.getName() + " - " + a.getStrTypeToPkType().getName());
			}

			System.out.println();
		}

		System.out.println("Tipo ataques primer Pk IA");

		for (Attack a : IA.getPkCombatting().getFourPrincipalAttacks()) {

			System.out.println(a.getStrTypeToPkType().getName());
		}

		// Put attacks into different lists to determine damage from attacks
		IA.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);
		player.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);

		// IA Prepares best attack against Pokemon player
		IA.prepareBestAttackIA(effectPerTypes);

		System.out.println("Next attack from machine :");
		System.out.println(IA.getPkCombatting().getNextMovement().getName() + " - "
				+ IA.getPkCombatting().getNextMovement().getStrTypeToPkType().getName());
	}

	// Main battle (start battle)
	public void startBattle() {

		int nbRound = 1;
		Scanner sc = new Scanner(System.in);

		while (IA.getPokemon().size() >= 1 && player.getPokemon().size() >= 1) {

			System.out.println("----------------------------------");
			System.out.println("Let's start round nº : " + nbRound);
			System.out.println("----------------------------------");

			boolean isStartTurn = true;

			boolean playerIsCharging = player.getPkCombatting().getIsChargingAttackForNextRound();
			int attackChoice = playerIsCharging ? 1 : getPlayerChoice(sc);

			if (attackChoice == 1) {
				handleAttackTurn(sc, isStartTurn);
			} else {
				boolean cancelled = !handleChangeTurn(sc, isStartTurn);

				if (cancelled) {
					System.out.println("Cambio cancelado. Regresando al menú principal...");
					attackChoice = -1; // show again options : attack/change
					// Stay in the same round
					nbRound--;
				}
			}

			// Get next round
			nbRound++;
		}
	}

	// Handle attacks from the turn
	private void handleAttackTurn(Scanner sc, boolean isStartTurn) {

		int attackId = 0;

		if (!player.getPkCombatting().getIsChargingAttackForNextRound()) {
			Pokemon pk = player.getPkCombatting();

			// Checks that there is no more PPs on attacks from Pokemon combating
			if (!player.hasAnyPPLeft(pk)) {
				System.out.println(pk.getName() + " no tiene más PPs en ningún ataque.");
				System.out.println(pk.getName() + " usó Forcejeo!");
				attackId = 165; // ID of "Struggle" (default attack when no more attacks remaining)
			} else {
				attackId = getValidAttackId(sc, player);
			}
		}

		printPokemonStates();

		Pokemon pkPlayer = player.getPkCombatting();
		Pokemon pkIA = IA.getPkCombatting();

		checkCanAttackFromStatusCondition(pkPlayer);
		checkCanAttackFromStatusCondition(pkIA);

		// Apply status effects from beginning of the turn + prepare effectiveness and
		// bonus from attacks chosen
		applyEffectStatusCondition(pkPlayer);
		player.prepareBestAttackPlayer(attackId);

		// IA can decide to change Pokemon
		if (IA.getPkCombatting().getIsChargingAttackForNextRound() == false) {
			// If change realized => don't attack
			tryIAChange();
		}

		prepareIAIfPossible(isStartTurn);

		// Handle normal attack sequence
		handleNormalAttackSequence(sc);

		// Reset status conditions
		resetEffectStatusCondition(pkPlayer);
		resetEffectStatusCondition(IA.getPkCombatting());
	}

	// Handle attack from IA when player is changing the Pokemon
	private boolean handleChangeTurn(Scanner sc, boolean isStartTurn) {

		Pokemon pkIA = IA.getPkCombatting();

		boolean changed = changePokemon(sc);

		if (!changed)
			return false; // player cancelled the change (return to start options)

		checkCanAttackFromStatusCondition(pkIA);

		prepareIAIfPossible(isStartTurn);

		handleChangeSequence(sc); // only IA attacks

		// If defender has to change because of "Whirlwind"
		if (player.getIsForceSwitchPokemon()) {

			handleForcedSwitchWhirlwind(player);

			// Turn ends because of the change
			return true;
		}

		resetEffectStatusCondition(IA.getPkCombatting());

		return true;
	}

	// Prepare attack from IA if can attack (after checking status conditions from
	// the beginning of the turn)
	private void prepareIAIfPossible(boolean isStartTurn) {
		applyEffectStatusCondition(IA.getPkCombatting());
		IA.prepareBestAttackIA(effectPerTypes);
	}

	// Get the player choice (attack or change Pokemon)
	private int getPlayerChoice(Scanner sc) {
		System.out.println("Quieres atacar (1) o cambiar de Pokémon (2) :");
		int choice = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");
		return choice;
	}

	// Check validity of attack id from player Pokemon
	private int getValidAttackId(Scanner sc, Player player) {
		System.out.println("Escoge un ataque :");
		player.printAttacksFromPokemonCombating();

		int attackId = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");

		// While it's not a valid attack or doesn't have PP
		while (true) {

			// 1. Checks that the Pokemon has the attack chosen
			if (!player.getPkCombatting().getFourIdAttacks().contains(attackId)) {
				System.out.println("Escoge un ataque que tenga el Pokémon.");
			} else {

				// 2. Get the attack
				Attack atk = player.getPkCombatting().getNextMovementById(attackId);

				// 3. Verifies that the attack has PP
				if (atk.getPp() > 0) {
					return attackId; // valid
				} else {
					System.out.println("No tienes más PP para este ataque. Escoge otro.");
				}
			}

			// New reading
			attackId = sc.nextInt();
			sc.useDelimiter(";|\r?\n|\r");
		}
	}

	// Print Pokemon states (for debug)
	private void printPokemonStates() {
		System.out.println("Estado del Pokémon del jugador : "
				+ player.getPkCombatting().getStatusCondition().getStatusCondition());
		System.out.println(
				"Estado del Pokémon de la máquina : " + IA.getPkCombatting().getStatusCondition().getStatusCondition());
	}

	// Check if Pokemon combating can attack due to effect from status condition
	private void checkCanAttackFromStatusCondition(Pokemon attacker) {
		attacker.getStatusCondition().doEffectStatusCondition(attacker.getStatusCondition().getStatusCondition());
	}

	// Apply effect of status condition at the beginning of the turn for Pokemon
	// combating
	private void applyEffectStatusCondition(Pokemon attacker) {
		attacker.checkEffectsStatusCondition(true);
	}

	// Apply effect of status condition at the end of the turn for Pokemon
	// combating
	private void resetEffectStatusCondition(Pokemon attacker) {
		attacker.checkEffectsStatusCondition(false);
	}

	// Player attack first
	private boolean playerCanAttackFirst() {
		return player.getPkCombatting().getCanAttack()
				&& player.getPkCombatting().getSpeed() > IA.getPkCombatting().getSpeed();
	}

	// Handle normal attack sequence
	private void handleNormalAttackSequence(Scanner sc) {
		boolean playerFirst = playerCanAttackFirst();

		Player first = playerFirst ? player : IA;
		Player second = playerFirst ? IA : player;

		// 1. Get order of players
		boolean turnShouldEnd = attackAndCheckIfTurnEnds(first, second, sc);

		// 2. Second player attacks if turn can continue
		if (!turnShouldEnd) {
			attackAndCheckIfTurnEnds(second, first, sc);
		}

		// 3. Reset the flinch/retreat
		IA.getPkCombatting().setHasRetreated(false);
		player.getPkCombatting().setHasRetreated(false);
	}

	// Check if Pokemon can attack + do retaliation
	private boolean attackAndCheckIfTurnEnds(Player attacker, Player defender, Scanner sc) {

		Pokemon pk = attacker.getPkCombatting();

		// If retreated, cannot attack, but turn continues
		if (pk.getHasRetreated()) {
			System.out.println(pk.getName() + " retrocedió.");
			return false; // turn continues
		}

		// If Pokemon facing is debilitated, force change and ends turn
		if (pk.getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			checkForcedPokemonChange(sc);
			return true; // turn ends
		}

		// If attacker forces to change because of "Whirlwind"
		if (attacker.getIsForceSwitchPokemon()) {

			handleForcedSwitchWhirlwind(attacker);

			// Turn ends because of the change
			return true;
		}

		// Execute attack
		if (attacker == player)
			handlePlayerRetaliation();
		else
			handleIARetaliation();

		// If defender has to change because of "Whirlwind"
		if (defender.getIsForceSwitchPokemon()) {

			handleForcedSwitchWhirlwind(defender);

			// Turn ends because of the change
			return true;
		}

		// If defender got Pokemon debilitated during the attack, force change and ends
		// turn
		if (defender.getPkCombatting().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			checkForcedPokemonChange(sc);
			return true; // turn ends
		}

		return false; // turn continues
	}

	// Handle change sequence
	private void handleChangeSequence(Scanner sc) {
		handleIARetaliation();
		checkForcedPokemonChange(sc);
	}

	// Player handle attack
	private void handlePlayerRetaliation() {

		if (player.getPkCombatting().getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED) {

			PkVPk battleVS = new PkVPk(player, IA);
			this.setBattleVS(battleVS);

			if (player.getPkCombatting().getCanAttack()) {

				// Get probability of attacking (we already checked for status conditions. Now
				// we do it for evasion/accuracy)
				this.getBattleVS().getProbabilityOfAttacking();

				// Check again cause maybe there are attacks like "Whirlwind" meanwhile Pokemon
				// facing is invulnerable, etc.
				if (player.getPkCombatting().getCanAttack()) {

					System.out.println(ANSI_GREEN + "Pokemon player can attack" + ANSI_RESET);
					this.getBattleVS().doAttackEffect();
				}
			} else {
				System.out.println(ANSI_RED + "Pokemon player cannot attack" + ANSI_RESET);
			}

			player.getPkCombatting().restartParametersEffect();
		} else {
			System.out.println(ANSI_RED + "Pokemon player is debilitated" + ANSI_RESET);
		}
	}

	// IA handle attack
	private void handleIARetaliation() {
		if (IA.getPkCombatting().getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED) {

			PkVPk battleVS = new PkVPk(IA, player);
			this.setBattleVS(battleVS);

			if (IA.getPkCombatting().getCanAttack()) {

				// Get probability of attacking (we already checked for status conditions. Now
				// we do it for evasion/accuracy)
				this.getBattleVS().getProbabilityOfAttacking();

				// Check again cause maybe there are attacks like "Whirlwind" meanwhile Pokemon
				// facing is invulnerable, etc.
				if (IA.getPkCombatting().getCanAttack()) {

					System.out.println(ANSI_GREEN + "Pokemon IA can attack" + ANSI_RESET);
					this.getBattleVS().doAttackEffect();
				}

			} else {
				System.out.println(ANSI_RED + "Pokemon IA cannot attack" + ANSI_RESET);
			}

			IA.getPkCombatting().restartParametersEffect();
		} else {

			System.out.println(ANSI_RED + "Pokemon IA is debilitated" + ANSI_RESET);
		}
	}

	// Check if needed to chose a new Pokemon (ex : combating Pokemon dies from
	// burning in final turn while flying, etc.)
	private void checkForcedPokemonChange(Scanner sc) {

		// Player dies
		if (player.getPkCombatting().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			System.out.println(player.getPkCombatting().getName() + " fue derrotado.");
			System.out.println("¿Qué Pokémon deberías escoger?");

			boolean changed = false;
			while (!changed) {
				changed = changePokemon(sc); // chose a new Pokemon (mandatory)
			}
		}

		// IA dies
		if (IA.getPkCombatting().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			System.out.println(IA.getPkCombatting().getName() + " fue derrotado.");

			Pokemon newIA = IA.decideBestChangePokemon(player.getPkCombatting(), effectPerTypes);

			// If decideBestChangePokemon returns null => choose the first Pokemon available
			if (newIA == null) {
				newIA = IA.getPokemon().stream()
						.filter(pk -> pk.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED)
						.findFirst().get();
			}

			System.out.println("IA eligió a " + newIA.getName() + " (Id:" + newIA.getId() + ")");

			IA.setPkCombatting(newIA);
			IA.setPkFacing(player.getPkCombatting());

			player.setPkFacing(IA.getPkCombatting());
			refreshAttackOrders();
			IA.prepareBestAttackIA(effectPerTypes);
		}
	}

	// Change Pokemon
	private boolean changePokemon(Scanner sc) {

		while (true) {

			System.out.println("\n--- Cambio de Pokémon ---");
			player.printPokemonInfo();
			System.out.println("Escribe el ID del Pokémon a usar o '0' para cancelar : ");

			int id = sc.nextInt();
			sc.useDelimiter(";|\r?\n|\r");

			if (id == 0) {
				return false; // cancel change
			}

			// Not allowed to chose the Pokemon combating (and not debilitated)
			if (player.getPkCombatting().getId() == id && player.getPkCombatting().getStatusCondition()
					.getStatusCondition() != StatusConditions.DEBILITATED) {
				System.out.println("Ese Pokémon ya está combatiendo. Escoge otro.");
				continue;
			}

			Optional<Pokemon> opt = player.getPokemon().stream().filter(p -> p.getId() == id).findFirst();

			if (!opt.isEmpty()) {
				if (opt.get().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
					System.out.println(
							opt.get().getName() + " (Id:" + opt.get().getId() + ")" + " fue debilitado. Escoge otro.");
					continue;
				}
			}

			if (opt.isEmpty()) {
				System.out.println("No escogiste un Pokémon válido. Escoge un Pokémon de los que posees :");
				continue;
			}

			// Reinitialize some stats
			player.getPkCombatting().setAttackStage(0);
			player.getPkCombatting().setSpecialAttackStage(0);

			Pokemon selected = opt.get();

			System.out.println("Jugador eligió a " + selected.getName());

			// Update Pokemon combating
			player.setPkCombatting(selected);

			// Update facing Pokemon
			player.setPkFacing(IA.getPkCombatting());
			IA.setPkFacing(player.getPkCombatting());

			refreshAttackOrders();

			return true; // change successfully
		}
	}

	// Try IA to change Pokemon. Return true if IA changed Pokemon. If return false,
	// will attack normally
	private boolean tryIAChange() {
		// 15% of probability to change Pokemon
		int randomNumber = (int) (Math.random() * 100) + 1;
		if (randomNumber > 15) {
			System.out.println("IA no cambiará (probabilidad muy baja)");
			return false; // don't change
		}

		// Check from others Pokemon from the team to see a potential better option
		Pokemon changeTo = IA.decideBestChangePokemon(player.getPkCombatting(), this.effectPerTypes);

		if (changeTo == null) {
			System.out.println("IA no tiene un mejor Pokémon al que cambiar");
			return false; // doesn't exists a better option
		}

		// Reinitialize some stats
		IA.getPkCombatting().setAttackStage(0);
		IA.getPkCombatting().setSpecialAttackStage(0);

		// Do Pokemon change => update Pokemon comabting from IA, etc.
		System.out.println("IA cambió a " + changeTo.getName());
		IA.setPkCombatting(changeTo);

		// Update Pokemon facing for each player
		IA.setPkFacing(player.getPkCombatting());
		player.setPkFacing(IA.getPkCombatting());

		refreshAttackOrders();

		return true;
	}

	private void refreshAttackOrders() {
		IA.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);
		player.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);
	}

	// Handle if a player has to change to another random Pokemon because of
	// "Whirlwind"
	private void handleForcedSwitchWhirlwind(Player defender) {

		// Get available Pokemon
		List<Pokemon> alive = defender.getPokemon().stream()
				.filter(p -> p.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED
						&& p != defender.getPkCombatting())
				.toList();

		if (alive.isEmpty()) {
			defender.setForceSwitchPokemon(false);
			return; // Cannot change => does not anything
		}

		// Chose random Pokemon
		Pokemon newPk = alive.get((int) (Math.random() * alive.size()));

		System.out.println(defender.getPkCombatting().getName() + " fue expulsado por Remolino.");

		boolean isPlayer = defender == player;
		System.out.println((isPlayer ? "Jugador" : "IA") + " (jugador/IA) envía a " + newPk.getName() + " (Id:"
				+ newPk.getId() + ")");

		defender.setPkCombatting(newPk);

		// Update Pokemon facing etc.
		if (defender == player) {
			IA.setPkFacing(newPk);
			player.setPkFacing(IA.getPkCombatting());
		} else {
			player.setPkFacing(newPk);
			IA.setPkFacing(player.getPkCombatting());
		}

		defender.setForceSwitchPokemon(false);
	}

	// Tests for attacks (466 Electivire, 398 Staraptor, 6 Charizard, 127 Pinsir,
	// 123 Scyther, 16 Pidgey, 95 Onix, 523 Zebstrika)
	public void doTest() {
		// Sets the same Pk
		String allPkPlayer = "18,18,18";
		String allPkIA = "398,398,398";

		String[] pkByPkPlayer = allPkPlayer.split(",");
		Map<Integer, Integer> pkCount = new HashMap<>();

		// Add Pokemon to player
		for (String PkID : pkByPkPlayer) {

			int baseId = Integer.parseInt(PkID);

			Optional<Pokemon> pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == baseId).findFirst();

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

				// Add to player team
				player.addPokemon(newPk);
			}
		}

		// Sets first Pokemon to combat for player
		player.setPkCombatting(player.getPokemon().get(0));

		// Sets the attacks to pokemon's player to test
		for (Pokemon pk : player.getPokemon()) {

//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 7).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 9).findFirst().get());
			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 19).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 15).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 14).findFirst().get());
			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 18).findFirst().get());

			// Adds the Ids of attacks chosed in a list
//			for (Attack ataChosed : player.getPkCombatting().getFourPrincipalAttacks()) {
//
//				player.getPkCombatting().addIdAttack(ataChosed.getId());
//
//			}
			for (Attack ataChosed : pk.getFourPrincipalAttacks()) {
				pk.addIdAttack(ataChosed.getId());
			}

		}

		String[] pkByPkIA = allPkIA.split(",");

		// Add Pokemon to IA
		for (String PkID : pkByPkIA) {

			Optional<Pokemon> pkOpt;

			pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(PkID)).findFirst();

			if (pkOpt.isPresent()) {

				// Creates a new instance of Pokemon in memory (otherwise there are problems of
				// duplications)
				IA.addPokemon(new Pokemon(pkOpt.get()));

			}
		}

		// Sets first Pokemon to combat for IA
		IA.setPkCombatting(IA.getPokemon().get(0));

		for (Pokemon pk : IA.getPokemon()) {

//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 7).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 9).findFirst().get());
			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 19).findFirst().get());
//			pk.addAttacks(pk.getSpecialAttacks().stream().filter(af -> af.getId() == 16).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 23).findFirst().get());
			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 18).findFirst().get());

			// Adds the Ids of attacks chosen in a list
			for (Attack ataChosed : IA.getPkCombatting().getFourPrincipalAttacks()) {

				IA.getPkCombatting().addIdAttack(ataChosed.getId());
			}
		}

		// Sets Pokemon facing to each other
		player.setPkFacing(IA.getPokemon().get(0));
		IA.setPkFacing(player.getPokemon().get(0));

		IA.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);
		player.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);

	}
}