package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import pokemon.enums.AttackCategory;
import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
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
	private PkVPk battleVS;
	private ScrappingWeb scrappingWeb;
	private WritterData writterData;
	private ReaderData readerData;
	private boolean mistIsActivated;
	private int nbTurnsMistActive;
	private Weather currentWeather = Weather.NONE;
	private boolean isWeatherSuppressed;

	private Player player;
	private IAPlayer IA;

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
		this.battleVS = new PkVPk();
		this.scrappingWeb = new ScrappingWeb();
		this.writterData = new WritterData();
		this.readerData = new ReaderData();
		this.mistIsActivated = false;
		this.nbTurnsMistActive = 0;
		this.isWeatherSuppressed = false;
	}

	// ==================================== GETTERS/SETTERS
	// ====================================

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

	public IAPlayer getIA() {
		return IA;
	}

	public void setIA(IAPlayer IA) {
		this.IA = IA;
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

	public boolean getMistIsActivated() {
		return mistIsActivated;
	}

	public void setMistIsActivated(boolean mistIsActivated) {
		this.mistIsActivated = mistIsActivated;
	}

	public int getNbTurnsMistActive() {
		return nbTurnsMistActive;
	}

	public void setNbTurnsMistActive(int nbTurnsMistActive) {
		this.nbTurnsMistActive = nbTurnsMistActive;
	}

	public Weather getCurrentWeather() {
		return this.currentWeather;
	}

	public void setCurrentWeather(Weather currentWeather) {
		this.currentWeather = currentWeather;
	}

	public boolean getisWeatherSuppressed() {
		return isWeatherSuppressed;
	}

	public void setIsWeatherSuppressed(boolean isWeatherSuppressed) {
		this.isWeatherSuppressed = isWeatherSuppressed;
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

	// -----------------------------
	// Start choice of Pokemon
	// -----------------------------
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
						+ "Para escoger los Pokémon, utiliza el formato : número,número,número,número,número,número => ej : 31,45,3,69,500,666");

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
			pkOpt = this.getPokemon().stream().filter(pk -> pk.getId() == Integer.parseInt(PkID)).findFirst();

			if (pkOpt.isPresent()) {

				this.getPlayer().addPokemon(pkOpt.get());

			} else {

				System.out.println("El número marcado no está en la lista : " + PkID);
				System.out.println("Tendrás que volver a escoger tus Pokémon (reinicia el juego)");
			}
		}

		// Pokemon machine choices
		this.getIA().IAPokemonChoice(this.getPlayer().getPokemon(), this.getPokemonPerType(), this.getEffectPerTypes());

		// Add attacks to each Pokemon list
		this.getPlayer().addAttacksForEachPokemon();
		this.getIA().addAttacksForEachPokemon();

		// Select an ability for each Pokemon list
		this.getPlayer().selectAbilityForEachPokemon(this.getAbilities());
		this.getIA().selectAbilityForEachPokemon(this.getAbilities());

		// Sets first Pokemon chosen for the combat
		this.getPlayer().setPkCombatting(this.getPlayer().getPokemon().get(0));
		this.getIA().setPkCombatting(this.getIA().getPokemon().get(0));

		this.getPlayer().setPkFacing(this.getIA().getPkCombatting());
		this.getIA().setPkFacing(this.getPlayer().getPkCombatting());

		System.out.println("Player");

		// Shows Pokemon from player
		for (Pokemon p : this.getPlayer().getPokemon()) {

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
		for (Pokemon p : this.getIA().getPokemon()) {

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

		for (Attack a : this.getIA().getPkCombatting().getFourPrincipalAttacks()) {

			System.out.println(a.getStrTypeToPkType().getName());
		}

		// Put attacks into different lists to determine damage from attacks
		this.getIA().orderAttacksFromDammageLevelPokemon(this.getEffectPerTypes());
		this.getPlayer().orderAttacksFromDammageLevelPokemon(this.getEffectPerTypes());

		// IA Prepares best attack against Pokemon player
		this.getIA().prepareBestAttackIA(this.getPlayer().getPkCombatting());

		System.out.println("Next attack from machine :");
		System.out.println(this.getIA().getPkCombatting().getNextMovement().getName() + " - "
				+ this.getIA().getPkCombatting().getNextMovement().getStrTypeToPkType().getName());
	}

	// -----------------------------
	// Main battle (start battle)
	// -----------------------------
	public void startBattle() {

		int nbRound = 1;
		Scanner sc = new Scanner(System.in);

		// Apply some abilities first
		applyIntimidationOnBattleStart(this.getPlayer().getPkCombatting(), this.getIA().getPkCombatting());

		// Puts abilities (for example if weather) at the beginning
		applyEntryAbilities();

		while (this.getIA().getPokemon().size() >= 1 && this.getPlayer().getPokemon().size() >= 1) {

			System.out.println("----------------------------------");
			System.out.println("Let's start round nº : " + nbRound);
			System.out.println("----------------------------------");

			Pokemon pkPlayer = this.getPlayer().getPkCombatting();

			// Get if Pokemon is charging an attack (two turns)
			boolean playerIsCharging = pkPlayer.getIsChargingAttackForNextRound();

			// Gets if Pokemon is trapped by his own attack (ex : Thrash, etc.)
			boolean playerIsTrappedByOwnAttack = pkPlayer.getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPEDBYOWNATTACK && e.getNbTurns() > 0);

			// If charging or trapped by his own attack, cannot do choices
			int attackChoice = (playerIsCharging || playerIsTrappedByOwnAttack) ? 1 : getPlayerChoice(sc);

			if (attackChoice == 1) {
				handleAttackTurn(sc);
			} else {
				if (this.getIA().getPkCombatting().getAbilitySelected().getId() == 23) {
					System.out.println("No puedes cambiar de Pokémon a causa de la habilidad Sombra trampa del rival");
					attackChoice = -1; // show again options : attack/change
					// Stay in the same round
					nbRound--;
				} else {
					boolean cancelled = !handleChangeTurn(sc);

					if (cancelled) {
						System.out.println("Cambio cancelado. Regresando al menú principal...");
						attackChoice = -1; // show again options : attack/change
						// Stay in the same round
						nbRound--;
					}
				}

			}

			// Get next round
			nbRound++;
		}
	}

	// -----------------------------
	// Handle attacks from the turn
	// -----------------------------
	private void handleAttackTurn(Scanner sc) {

		int attackId = 0;

		// Gets if is trapped by his own attack (ex : thrash, etc.)
		boolean trappedByOwnAttack = this.getPlayer().getPkCombatting().getEphemeralStates().stream()
				.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPEDBYOWNATTACK && e.getNbTurns() > 0);

		// If not charging or trapped by his own attack => normal choice of move
		if (!this.getPlayer().getPkCombatting().getIsChargingAttackForNextRound() && !trappedByOwnAttack) {
			if (!this.getPlayer().hasAnyPPLeft(this.getPlayer().getPkCombatting())) {
				System.out
						.println(this.getPlayer().getPkCombatting().getName() + " no tiene más PPs en ningún ataque.");
				System.out.println(this.getPlayer().getPkCombatting().getName() + " usó Forcejeo!");
				attackId = 165;
			} else {
				attackId = getValidAttackId(sc, this.getPlayer());
			}
		} else if (trappedByOwnAttack) {
			// Use the same movement trapped
			attackId = this.getPlayer().getPkCombatting().getNextMovement().getId();
			System.out.println(this.getPlayer().getPkCombatting().getName() + " está furioso y continúa atacando con "
					+ this.getPlayer().getPkCombatting().getNextMovement().getName() + "!");
		} else if (this.getPlayer().getPkCombatting().getIsChargingAttackForNextRound()) {
			// charging: respect current nextMovement (first turn was set before)
			attackId = this.getPlayer().getPkCombatting().getNextMovement().getId();
		}

		printPokemonStates();

		// Determine charged-attack second-turn flags (local)
		boolean playerSecondTurnOfCharged = this.getPlayer().getPkCombatting().getNextMovement() != null
				&& this.getPlayer().getPkCombatting().getNextMovement().getCategory() == AttackCategory.CHARGED
				&& this.getPlayer().getPkCombatting().getIsChargingAttackForNextRound();

		boolean IASecondTurnOfCharged = this.getIA().getPkCombatting().getNextMovement() != null
				&& this.getIA().getPkCombatting().getNextMovement().getCategory() == AttackCategory.CHARGED
				&& this.getIA().getPkCombatting().getIsChargingAttackForNextRound();

		// Evaluate normal statuses (these set canMoveStatusCondition and other
		// permanent changes)
		// For charged attacks *first turn* we intentionally skip statuses checks —
		// that's the rule:
		// first turn of a charging, executes regardless of states conditions, the
		// second turn is checked.
		if ((playerSecondTurnOfCharged
				|| this.getPlayer().getPkCombatting().getNextMovement().getCategory() != AttackCategory.CHARGED)
				&& this.getPlayer().getPkCombatting().getCanDonAnythingNextRound()) {
			evaluateStatusStartOfTurn(this.getPlayer().getPkCombatting());
		}
		if ((IASecondTurnOfCharged
				|| this.getIA().getPkCombatting().getNextMovement().getCategory() != AttackCategory.CHARGED)
				&& this.getIA().getPkCombatting().getCanDonAnythingNextRound()) {
			evaluateStatusStartOfTurn(this.getIA().getPkCombatting());
		}

		// Prepare player chosen attack (this sets nextMovement etc.)
		if (this.getPlayer().getPkCombatting().getCanDonAnythingNextRound()
				&& !this.getPlayer().getPkCombatting().getIsChargingAttackForNextRound()) {
			this.getPlayer().prepareBestAttackPlayer(attackId, this.getIA().getPkCombatting());
		}

		// IA can decide to change Pokemon only if it's not charging
		if (this.getIA().getPkCombatting().getCanDonAnythingNextRound()
				&& !this.getIA().getPkCombatting().getIsChargingAttackForNextRound()
				&& this.getPlayer().getPkCombatting().getAbilitySelected().getId() != 23) {
			tryIAChange();
		}

		// Prepare IA (select move) only if not charging
		if (!IASecondTurnOfCharged && this.getIA().getPkCombatting().getCanDonAnythingNextRound()) {
			prepareIAIfPossible(this.getIA().getPkCombatting());
		}

		// Execute the attack sequence (ordering uses current canAttack and speed)
		handleNormalAttackSequence(sc);

		// At the end of the turn we must perform end-of-turn ephemeral effects and
		// reduce counters and parameters
		reduceNumberTurnsEffects(this.getPlayer(), this.getIA());
		reduceNumberTurnsEffects(this.getIA(), this.getPlayer());

		// Apply ability effect on end of turn
		applyEndTurnAbility(this.getPlayer().getPkCombatting());
		applyEndTurnAbility(this.getIA().getPkCombatting());

		this.getPlayer().getPkCombatting().restartParametersEffect();
		this.getIA().getPkCombatting().restartParametersEffect();

		reduceNbTurnsMistActive();
	}

	/*
	 * ------------------------- Helper: Evaluate states BEFORE ordering "who should
	 * attack first" decision. Some states decrease their turn at the beginning of
	 * the turn and apply effects for example when paralyzed, it reduces speed
	 * -------------------------
	 */
	private void evaluateStatusStartOfTurn(Pokemon pk) {
		pk.doFrozenEffect();
		pk.doBurnedEffectStartTurn();
		pk.doParalyzedEffect();
	}

	/*
	 * ------------------------- Helper: Evaluate states BEFORE attacking. Some
	 * states influence the probability of attacking, for example when confused,
	 * paralyzed, etc. -------------------------
	 */
	private void canAttackEvaluatingAllStatesToAttack(Pokemon pk) {
		pk.canAttackFrozen();
		pk.checkCanMoveParalyzed();
		pk.canAttackParalyzed();
		boolean canAttackConfused = pk.canAttackConfused();
		boolean canAttackAsleep = pk.doAsleepEffect();

		boolean canAttack = pk.getCanAttack() && canAttackConfused && canAttackAsleep;

		pk.setCanAttack(canAttack);
	}

	/*
	 * ------------------------- Helper: Reduce number of turns remaining on states
	 * -------------------------
	 */
	private void reduceNumberTurnsEffects(Player playerAttacker, Player playerDefender) {

		// Normal status
		playerAttacker.getPkCombatting().doBurnedEffectEndTurn();
		playerAttacker.getPkCombatting().doPoisonedEffectEndTurn();
		// Ephemeral status
		playerAttacker.getPkCombatting().doTrappedEffect();
		playerAttacker.getPkCombatting().putConfusedStateIfNeeded();
		playerAttacker.getPkCombatting().reduceDisabledAttackTurn();
		playerAttacker.getPkCombatting().doDrainedAllTurnsEffect();

		// Get PS from drained rival Pokemon
		if (playerAttacker.getPkCombatting().getIsDraining()) {
			// Get drained all turns state from defender
			State drainedAllTurnsState = playerDefender.getPkCombatting().getEphemeralStates().stream()
					.filter(e -> e.getStatusCondition() == StatusConditions.DRAINEDALLTURNS).findFirst().orElse(null);

			// Only can drain if it's not the same turn attacking with the draining attack
			// (Leech seed..)
			if (drainedAllTurnsState.getNbTurns() != 0) {
				playerAttacker.getPkCombatting().doDrainedAllTurnsBeneficiaryEffect();

			}
		}
		// Force switch if (for example), after getting drained, has no more PS
		if (playerAttacker.getPkCombatting().getStatusCondition()
				.getStatusCondition() == StatusConditions.DEBILITATED) {
			handleForcedSwitch(playerAttacker);
		}
	}

	// -----------------------------
	// Handle attack from IA when player is changing the Pokemon
	// -----------------------------
	private boolean handleChangeTurn(Scanner sc) {

		Pokemon pkIA = this.getIA().getPkCombatting();

		if (this.getPlayer().getPkCombatting().getCanDonAnythingNextRound()) {
			boolean changed = changePokemon(sc);

			if (!changed)
				return false; // player cancelled the change (return to start options)
		} else {
			System.out.println(
					this.getPlayer().getPkCombatting().getName() + " (" + this.getPlayer().getPkCombatting().getId()
							+ ") " + "no puede cambiarse  este turno a causa de algún ataque o estado");
			this.getPlayer().getPkCombatting().setCanDonAnythingNextRound(true);
		}

		Pokemon pkPlayer = this.getPlayer().getPkCombatting();

		evaluateStatusStartOfTurn(pkIA);
		canAttackEvaluatingAllStatesToAttack(pkIA);

		if (pkIA.getCanDonAnythingNextRound()) {
			prepareIAIfPossible(pkIA);

			handleChangeSequence(sc); // only IA attacks
		} else {
			System.out.println(pkIA.getName() + " (" + pkIA.getId() + ") " + "debe recuperarse a causa de "
					+ pkIA.getLastUsedAttack().getName());

			pkIA.setCanDonAnythingNextRound(true);
		}

		// If defender has to change because of "Whirlwind" or "Roar", etc.
		if (this.getPlayer().getIsForceSwitchPokemon()) {
			handleForcedSwitch(this.getPlayer());
		}

		// Remove drained ALL SATUS state (cause player changed)
		clearDrainEffects(pkPlayer, pkIA);

		reduceNumberTurnsEffects(this.getIA(), this.getPlayer());

		applyEndTurnAbility(pkPlayer);
		applyEndTurnAbility(pkIA);

		pkPlayer.restartParametersEffect();
		pkIA.restartParametersEffect();

		reduceNbTurnsMistActive();

		return true;
	}

	// -----------------------------
	// Prepare attack from IA if can attack (after checking status conditions from
	// the beginning of the turn)
	// -----------------------------
	private void prepareIAIfPossible(Pokemon pkIA) {

		if (pkIA.getIsChargingAttackForNextRound()) {
			return; // if charging an attack (like fly), cannot choose another attack
		}

		this.getIA().prepareBestAttackIA(this.getPlayer().getPkCombatting());
		// this.getIA().prepareBestAttackPlayer(this.,
		// this.getPlayer().getPkCombatting());
	}

	// -----------------------------
	// Get the player choice (attack or change Pokemon)
	// -----------------------------
	private int getPlayerChoice(Scanner sc) {
		System.out.println("Quieres atacar (1) o cambiar de Pokémon (2) :");
		int choice = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");
		return choice;
	}

	// -----------------------------
	// Check validity of attack id from player Pokemon
	// -----------------------------
	private int getValidAttackId(Scanner sc, Player player) {

		// Only choose if not recovering from an attack
		if (player.getPkCombatting().getCanDonAnythingNextRound()) {

			System.out.println("Escoge un ataque :");
			this.getPlayer().printAttacksFromPokemonCombating();

			int attackId = sc.nextInt();
			sc.useDelimiter(";|\r?\n|\r");

			// While it's not a valid attack or doesn't have PP
			while (true) {

				// 1. Checks that the Pokemon has the attack chosen
				if (!player.getPkCombatting().getFourIdAttacks().contains(attackId)) {
					System.out.println("Escoge un ataque que tenga el Pokémon.");
				} else {

					// 2. Get the attack
					Attack atk = this.getPlayer().getPkCombatting().getNextMovementById(attackId);

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
		// Sets the same last attack
		else {
			return this.getPlayer().getPkCombatting().getLastUsedAttack().getId();
		}
	}

	// -----------------------------
	// Print Pokemon states (for debug)
	// -----------------------------
	private void printPokemonStates() {
		// Normal status player
		System.out.println(ANSI_YELLOW + "Estado normal del Pokémon del jugador : "
				+ this.getPlayer().getPkCombatting().getStatusCondition().getStatusCondition() + ANSI_RESET);

		// Ephemeral status player
		System.out.println(ANSI_YELLOW + "Estados efímeros del Pokémon del jugador : " + ANSI_RESET);
		for (State status : this.getPlayer().getPkCombatting().getEphemeralStates()) {
			System.out.println(ANSI_YELLOW + status.getStatusCondition() + ANSI_RESET);
		}

		// Normal status IA
		System.out.println(ANSI_YELLOW + "Estado normal del Pokémon de la máquina : "
				+ this.getIA().getPkCombatting().getStatusCondition().getStatusCondition() + ANSI_RESET);

		// Ephemeral status IA
		System.out.println(ANSI_YELLOW + "Estados efímeros del Pokémon de la máquina : " + ANSI_RESET);
		for (State status : this.getIA().getPkCombatting().getEphemeralStates()) {
			System.out.println(ANSI_YELLOW + status.getStatusCondition() + ANSI_RESET);
		}
	}

	// -----------------------------
	// Player attack first
	// -----------------------------
	private boolean playerCanAttackFirst() {
		return this.getPlayer().getPkCombatting().getCanAttack() && this.getPlayer().getPkCombatting()
				.getEffectiveSpeed() >= this.getIA().getPkCombatting().getEffectiveSpeed();
	}

	// -----------------------------
	// Handle normal attack sequence
	// -----------------------------
	private void handleNormalAttackSequence(Scanner sc) {
		boolean playerFirst = playerCanAttackFirst();

		System.out.println(ANSI_RED + "Velocidad normal jugador : " + this.getPlayer().getPkCombatting().getSpeed()
				+ " / Velocidad efectiva : " + this.getPlayer().getPkCombatting().getEffectiveSpeed() + ANSI_RESET);
		System.out.println(ANSI_RED + "Velocidad normal IA : " + this.getIA().getPkCombatting().getSpeed()
				+ " / Velocidad efectiva : " + this.getIA().getPkCombatting().getEffectiveSpeed() + ANSI_RESET);

		Player first = playerFirst ? this.getPlayer() : this.getIA();
		Player second = playerFirst ? this.getIA() : this.getPlayer();

		// 1. Get order of players
		boolean turnShouldEnd = attackAndCheckIfTurnEnds(first, second, sc);

		// 2. Second player attacks if turn can continue
		if (!turnShouldEnd) {
			attackAndCheckIfTurnEnds(second, first, sc);
		}

		// 3. Reset the flinch/retreat
		this.getIA().getPkCombatting().setHasRetreated(false);
		this.getPlayer().getPkCombatting().setHasRetreated(false);
	}

	// -----------------------------
	// Check if Pokemon can attack + do retaliation
	// -----------------------------
	private boolean attackAndCheckIfTurnEnds(Player attacker, Player defender, Scanner sc) {

		// If retreated, cannot attack, but turn continues
		if (attacker.getPkCombatting().getHasRetreated()) {
			System.out.println(attacker.getPkCombatting().getName() + " retrocedió.");
			return false; // turn continues
		}

		// If Pokemon is debilitated, force change and ends turn
		if (attacker.getPkCombatting().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			clearDrainEffects(attacker.getPkCombatting(), defender.getPkCombatting());
			checkForcedPokemonChange(sc);
			return true; // turn ends
		}

		// If attacker forces to change because of "Whirlwind" or "Roar", etc.
		if (attacker.getIsForceSwitchPokemon()) {
			handleForcedSwitch(attacker);
			return true;
		}

		canAttackEvaluatingAllStatesToAttack(attacker.getPkCombatting());

		if (!attacker.getPkCombatting().getCanAttack()) {
			// Ensure we don't keep charging state if we were prevented from attacking
			attacker.getPkCombatting().setIsChargingAttackForNextRound(false);
			// Maybe he is confused, so he cannot attack, etc. => ends his turn
			return false;
		}

		if (attacker.getPkCombatting().getCanDonAnythingNextRound()) {
			// Execute attack for attacker
			if (attacker == this.getPlayer()) {
				handlePlayerRetaliation();
			} else {
				handleIARetaliation();
			}
		} else {
			System.out.println(attacker.getPkCombatting().getName() + " (" + attacker.getPkCombatting().getId() + ") "
					+ "debe recuperarse a causa de " + attacker.getPkCombatting().getLastUsedAttack().getName());

			attacker.getPkCombatting().setCanDonAnythingNextRound(true);
		}

		// After an attack, if defender was charging a charged attack (like Fly) but is
		// prevented (cannot attack),
		// we must clear the charging flag so that on the next turn we don't remain
		// stuck in charge state.
		// We do this *after* the attack resolution and only if defender cannot attack
		// now.
		Pokemon defenderPk = defender.getPkCombatting();
		if (defenderPk.getIsChargingAttackForNextRound() && defenderPk.getNextMovement() != null
				&& defenderPk.getNextMovement().getCategory() == AttackCategory.CHARGED && !defenderPk.getCanAttack()) {

			// Cancel the charging state: defender will no longer be invulnerable next turn.
			defenderPk.setIsChargingAttackForNextRound(false);
		}

		// If defender must change because of "Whirlwind" or "Roar", etc.
		if (defender.getIsForceSwitchPokemon()) {
			handleForcedSwitch(defender);
			return true;
		}

		// If defender got debilitated during this attack -> force change and end turn
		if (defender.getPkCombatting().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			clearDrainEffects(attacker.getPkCombatting(), defender.getPkCombatting());
			checkForcedPokemonChange(sc);
			return true;
		}

		return false; // turn continues
	}

	// -----------------------------
	// Handle change sequence
	// -----------------------------
	private void handleChangeSequence(Scanner sc) {
		handleIARetaliation();
		checkForcedPokemonChange(sc);
	}

	// -----------------------------
	// Player handle attack
	// -----------------------------
	private void handlePlayerRetaliation() {

		Pokemon pkPlayer = this.getPlayer().getPkCombatting();

		if (pkPlayer.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED) {

			boolean isWeatherSuppressed = this.getisWeatherSuppressed();

			PkVPk battleVS = new PkVPk(this.getPlayer(), this.getIA(), this.getCurrentWeather(), isWeatherSuppressed);
			this.setBattleVS(battleVS);

			if (pkPlayer.getCanAttack()) {

				// Get probability of attacking (we already checked for status conditions. Now
				// we do it for evasion/accuracy)
				this.getBattleVS().getProbabilityOfAttacking(this.getCurrentWeather());

				// Check again cause maybe there are attacks like "Whirlwind" meanwhile Pokemon
				// facing is invulnerable, etc.
				if (pkPlayer.getCanAttack()) {

					System.out.println(ANSI_GREEN + "Pokemon player can attack" + ANSI_RESET);

					this.getBattleVS().doAttackEffect(this.getCurrentWeather(), this.getMistIsActivated());
					pkPlayer.setLastUsedAttack(pkPlayer.getNextMovement());

					// Sets Mist effect activated
					if (pkPlayer.getNextMovement().getId() == 54 && !this.getMistIsActivated()) {
						this.setMistIsActivated(true);
						this.setNbTurnsMistActive(5);
					}
				}
			} else {
				System.out.println(ANSI_RED + "Pokemon player cannot attack" + ANSI_RESET);
				pkPlayer.setIsChargingAttackForNextRound(false);
			}
		} else {
			pkPlayer.removeStates();
			System.out.println(ANSI_RED + "Pokemon player is debilitated" + ANSI_RESET);
		}
	}

	// -----------------------------
	// IA handle attack
	// -----------------------------
	private void handleIARetaliation() {

		Pokemon pkIA = this.getIA().getPkCombatting();

		if (pkIA.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED) {

			boolean isWeatherSuppressed = this.getisWeatherSuppressed();

			PkVPk battleVS = new PkVPk(this.getIA(), this.getPlayer(), this.getCurrentWeather(), isWeatherSuppressed);
			this.setBattleVS(battleVS);

			if (pkIA.getCanAttack()) {

				// Get probability of attacking (we already checked for status conditions. Now
				// we do it for evasion/accuracy)
				this.getBattleVS().getProbabilityOfAttacking(this.getCurrentWeather());

				// Check again cause maybe there are attacks like "Whirlwind" meanwhile Pokemon
				// facing is invulnerable, etc.
				if (pkIA.getCanAttack()) {

					System.out.println(ANSI_GREEN + "Pokemon IA can attack" + ANSI_RESET);

					this.getBattleVS().doAttackEffect(this.getCurrentWeather(), this.getMistIsActivated());
					pkIA.setLastUsedAttack(pkIA.getNextMovement());

					// Sets Mist effect activated
					if (pkIA.getNextMovement().getId() == 54 && !this.getMistIsActivated()) {
						this.setMistIsActivated(true);
						this.setNbTurnsMistActive(5);
					}
				}

			} else {
				System.out.println(ANSI_RED + "Pokemon IA cannot attack" + ANSI_RESET);
				pkIA.setIsChargingAttackForNextRound(false);
			}
		} else {
			pkIA.removeStates();
			System.out.println(ANSI_RED + "Pokemon IA is debilitated" + ANSI_RESET);
		}
	}

	// -----------------------------
	// Check if needed to chose a new Pokemon (ex : combating Pokemon dies from
	// burning in final turn while flying, etc.)
	// -----------------------------
	private void checkForcedPokemonChange(Scanner sc) {

		// Player dies
		if (this.getPlayer().getPkCombatting().getStatusCondition()
				.getStatusCondition() == StatusConditions.DEBILITATED) {
			System.out.println(this.getPlayer().getPkCombatting().getName() + " fue derrotado.");
			System.out.println("¿Qué Pokémon deberías escoger?");

			boolean changed = false;
			while (!changed) {
				changed = changePokemon(sc); // chose a new Pokemon (mandatory)
			}
		}

		// IA dies
		if (this.getIA().getPkCombatting().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			this.getIA().getPkCombatting().removeStates();
			System.out.println(this.getIA().getPkCombatting().getName() + " fue derrotado.");

			Pokemon newIA = this.getIA().decideBestChangePokemon(this.getPlayer().getPkCombatting(),
					this.getEffectPerTypes());

			// If decideBestChangePokemon returns null => choose the first Pokemon available
			if (newIA == null) {
				newIA = this.getIA().getPokemon().stream()
						.filter(pk -> pk.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED)
						.findFirst().get();
			}

			// Remove ability effect (ex : 13 Cloud Nine)
			applyExitAbilityOnSwitch(this.getIA().getPkCombatting());
			// Reinitialize some stats before changing
			this.getIA().getPkCombatting().setAttackStage(0);
			this.getIA().getPkCombatting().setSpecialAttackStage(0);
			this.getIA().getPkCombatting().setPrecisionPoints(0);
			this.getIA().getPkCombatting().setDefenseStage(0);
			this.getIA().getPkCombatting().setSpecialDefenseStage(0);
			this.getIA().getPkCombatting().setLastUsedAttack(new Attack());
			this.getIA().getPkCombatting().getAbilitySelected().setAlreadyUsedOnEnter(false);

			System.out.println("IA eligió a " + newIA.getName() + " (Id:" + newIA.getId() + ")");

			this.getIA().setPkCombatting(newIA);
			this.getIA().setPkFacing(this.getPlayer().getPkCombatting());

			// Update weather ability if any
			applyEntryAbilityOnSwitch(newIA, this.getPlayer().getPkCombatting());

			this.getPlayer().setPkFacing(this.getIA().getPkCombatting());
			refreshAttackOrders();
			this.getIA().prepareBestAttackIA(this.getPlayer().getPkCombatting());
		}
	}

	// -----------------------------
	// Change Pokemon
	// -----------------------------
	private boolean changePokemon(Scanner sc) {

		while (true) {
			System.out.println("\n--- Cambio de Pokémon ---");
			this.getPlayer().printPokemonInfo();
			System.out.println("Escribe el ID del Pokémon a usar o '0' para cancelar : ");

			int id = sc.nextInt();
			sc.useDelimiter(";|\r?\n|\r");

			if (id == 0) {
				return false; // cancel change
			}

			// Not allowed to chose the Pokemon combating (and not debilitated)
			if (this.getPlayer().getPkCombatting().getId() == id && this.getPlayer().getPkCombatting()
					.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED) {
				System.out.println("Ese Pokémon ya está combatiendo. Escoge otro.");
				continue;
			}

			Optional<Pokemon> opt = this.getPlayer().getPokemon().stream().filter(p -> p.getId() == id).findFirst();

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

			// Remove ability effect (ex : 13 Cloud Nine)
			applyExitAbilityOnSwitch(this.getPlayer().getPkCombatting());
			// Reinitialize some stats
			this.getPlayer().getPkCombatting().setAttackStage(0);
			this.getPlayer().getPkCombatting().setSpecialAttackStage(0);
			this.getPlayer().getPkCombatting().setPrecisionPoints(0);
			this.getPlayer().getPkCombatting().setDefenseStage(0);
			this.getPlayer().getPkCombatting().setSpecialDefenseStage(0);
			this.getPlayer().getPkCombatting().setLastUsedAttack(new Attack());
			this.getPlayer().getPkCombatting().getAbilitySelected().setAlreadyUsedOnEnter(false);

			Pokemon selected = opt.get();

			System.out.println("Jugador eligió a " + selected.getName());

			// Update Pokemon combating
			selected.setJustEnteredBattle(true);
			this.getPlayer().setPkCombatting(selected);

			// Update facing Pokemon
			this.getPlayer().setPkFacing(this.getIA().getPkCombatting());
			this.getIA().setPkFacing(this.getPlayer().getPkCombatting());

			// Update weather ability if any
			applyEntryAbilityOnSwitch(this.getPlayer().getPkCombatting(), this.getIA().getPkCombatting());

			refreshAttackOrders();

			return true; // change successfully
		}
	}

	// -----------------------------
	// Try IA to change Pokemon. Return true if IA changed Pokemon. If return false,
	// will attack normally
	// -----------------------------
	private boolean tryIAChange() {
		// 15% of probability to change Pokemon
		int randomNumber = (int) (Math.random() * 100) + 1;
		if (randomNumber > 15) {
			System.out.println("IA no cambiará (probabilidad muy baja)");
			return false; // don't change
		}

		// Check from others Pokemon from the team to see a potential better option
		Pokemon changeTo = this.getIA().decideBestChangePokemon(this.getPlayer().getPkCombatting(),
				this.getEffectPerTypes());

		if (changeTo == null) {
			System.out.println("IA no tiene un mejor Pokémon al que cambiar");
			return false; // doesn't exists a better option
		}

		// Remove ability effect (ex : 13 Cloud Nine)
		applyExitAbilityOnSwitch(this.getIA().getPkCombatting());
		// Reinitialize some stats
		this.getIA().getPkCombatting().setAttackStage(0);
		this.getIA().getPkCombatting().setSpecialAttackStage(0);
		this.getIA().getPkCombatting().setPrecisionPoints(0);
		this.getIA().getPkCombatting().setDefenseStage(0);
		this.getIA().getPkCombatting().setSpecialDefenseStage(0);
		this.getIA().getPkCombatting().setLastUsedAttack(new Attack());
		this.getIA().getPkCombatting().getAbilitySelected().setAlreadyUsedOnEnter(false);

		// Do Pokemon change => update Pokemon comabting from IA, etc.
		System.out.println("IA cambió a " + changeTo.getName());
		this.getIA().setPkCombatting(changeTo);
		this.getIA().getPkCombatting().setJustEnteredBattle(true);

		// Update Pokemon facing for each player
		this.getIA().setPkFacing(this.getPlayer().getPkCombatting());
		this.getPlayer().setPkFacing(this.getIA().getPkCombatting());

		refreshAttackOrders();

		return true;
	}

	// -----------------------------
	// Put attacks from damage level
	// -----------------------------
	private void refreshAttackOrders() {
		this.getIA().orderAttacksFromDammageLevelPokemon(this.getEffectPerTypes());
		this.getPlayer().orderAttacksFromDammageLevelPokemon(this.getEffectPerTypes());
	}

	// -----------------------------
	// Handle if a player has to change to another random Pokemon because of
	// "Whirlwind" or "Roar", etc.
	// -----------------------------
	private void handleForcedSwitch(Player defender) {

		Pokemon pkCombating = defender.getPkCombatting();
		Pokemon pkFacing = defender.getPkFacing();

		clearDrainEffects(pkCombating, pkFacing);
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

		System.out.println(defender.getPkCombatting().getName() + " fue expulsado por "
				+ defender.getPkFacing().getNextMovement().getName() + ".");

		// Remove ability effect (ex : 13 Cloud Nine)
		applyExitAbilityOnSwitch(defender.getPkCombatting());
		// Reinitialize some stats
		defender.getPkCombatting().setAttackStage(0);
		defender.getPkCombatting().setSpecialAttackStage(0);
		defender.getPkCombatting().setPrecisionPoints(0);
		defender.getPkCombatting().setDefenseStage(0);
		defender.getPkCombatting().setSpecialDefenseStage(0);
		defender.getPkCombatting().setLastUsedAttack(new Attack());
		defender.getPkCombatting().getAbilitySelected().setAlreadyUsedOnEnter(false);

		boolean isPlayer = defender == this.getPlayer();
		System.out
				.println((isPlayer ? "Jugador" : "IA") + " envía a " + newPk.getName() + " (Id:" + newPk.getId() + ")");

		newPk.setJustEnteredBattle(true);
		defender.setPkCombatting(newPk);

		// Update weather ability if any
		applyEntryAbilityOnSwitch(newPk, this.getIA().getPkCombatting());

		// Update Pokemon facing etc.
		if (defender == this.getPlayer()) {
			this.getIA().setPkFacing(newPk);
			this.getPlayer().setPkFacing(IA.getPkCombatting());
		} else {
			this.getPlayer().setPkFacing(newPk);
			this.getIA().setPkFacing(this.getPlayer().getPkCombatting());
		}

		defender.setForceSwitchPokemon(false);
	}

	// -----------------------------
	// Reduce number of turns of Mist effect
	// -----------------------------
	private void reduceNbTurnsMistActive() {
		if (this.getMistIsActivated()) {
			this.setNbTurnsMistActive(this.getNbTurnsMistActive() - 1);

			if (this.getNbTurnsMistActive() <= 0) {
				this.setMistIsActivated(false);
				System.out.println("La neblina se disipó!");
			} else {
				System.out
						.println("Faltan " + this.getNbTurnsMistActive() + " turnos para que la neblina se fuerara XD");
			}
		}
	}

	// -----------------------------
	// helper method => clear DRAINED ALL TURNS effects for both Pokemon
	// -----------------------------
	private void clearDrainEffects(Pokemon pkA, Pokemon pkB) {
		pkA.setIsDraining(false);
		pkB.setIsDraining(false);
		pkA.removeStates();
		pkB.removeStates();
	}

	// -----------------------------
	// Sets the weather ability on first combat (if any)
	// -----------------------------
	private void applyEntryAbilities() {

		Pokemon p1 = this.getPlayer().getPkCombatting();
		Pokemon p2 = this.getIA().getPkCombatting();

		Ability a1 = p1.getAbilitySelected();
		Ability a2 = p2.getAbilitySelected();

		// Abilities that put a weather
		Ability weatherA1 = (a1 != null && a1.getIsWeatherType()) ? a1 : null;
		Ability weatherA2 = (a2 != null && a2.getIsWeatherType()) ? a2 : null;

		if (weatherA1 != null || weatherA2 != null) {

			if (weatherA1 != null && weatherA2 == null) {
				weatherA1.getEffect().onBattleStart(this, p1);

			} else if (weatherA2 != null && weatherA1 == null) {
				weatherA2.getEffect().onBattleStart(this, p2);

			} else {
				// Slower Pokemon wins if both have weather abilities
				Pokemon slower = p1.getSpeed() <= p2.getSpeed() ? p1 : p2;
				slower.getAbilitySelected().getEffect().onBattleStart(this, slower);
			}
		}

		// Weather can be suppressed if 13_Cloud_Nine
		if (a1 != null && a1.getId() == 13) {
			a1.getEffect().onBattleStart(this, p1);
		}
		if (a2 != null && a2.getId() == 13) {
			a2.getEffect().onBattleStart(this, p2);
		}
	}

	// -----------------------------
	// Sets the ability during changes (forced or manual) (if any)
	// -----------------------------
	private void applyEntryAbilityOnSwitch(Pokemon entering, Pokemon defender) {
		Ability ability = entering.getAbilitySelected();
		if (ability == null || ability.getId() == 5000)
			return;

		// Intimidate, etc. (first abilities to apply)
		if (ability.getId() == 22) {
			ability.getEffect().onSwitchIn(this, entering, defender);
		}

		// Sets weather
		if (ability.getIsWeatherType()) {
			ability.getEffect().onBattleStart(this, entering);
		}

		// Suppress weather if 13_Cloud_Nine
		if (ability.getId() == 13) {
			ability.getEffect().onBattleStart(this, entering);
		}
	}

	// -----------------------------
	// Sets the weather ability during changes (forced or manual) (if any)
	// -----------------------------
	private void applyEndTurnAbility(Pokemon pk) {
		Ability ability = pk.getAbilitySelected();
		if (ability == null || ability.getId() == 5000 || pk.getJustEnteredBattle())
			return;

		ability.getEffect().endOfTurn(this, pk);
	}

	// -----------------------------
	// Remove abilities effects before changing to new pokemon (ex : remove 13 Cloud
	// Nine)
	// -----------------------------
	private void applyExitAbilityOnSwitch(Pokemon leaving) {
		Ability ability = leaving.getAbilitySelected();

		if (ability == null || ability.getId() == 5000)
			return;

		// if (ability != null && (ability.getId() == 13 || ability.getId() == 16)) {
		ability.getEffect().onSwitchOut(this, leaving);
		// }
	}

	// -----------------------------
	// Do 22_Intimidate ability
	// -----------------------------
	private void applyIntimidationOnBattleStart(Pokemon p1, Pokemon p2) {

		boolean p1Intimidate = p1.hasAbility(22);
		boolean p2Intimidate = p2.hasAbility(22);

		if (!p1Intimidate && !p2Intimidate)
			return;

		if (p1Intimidate && !p2Intimidate) {
			p1.getAbilitySelected().getEffect().onSwitchIn(this, p1, p2);
		} else if (p2Intimidate && !p1Intimidate) {
			p2.getAbilitySelected().getEffect().onSwitchIn(this, p2, p1);
		} else {
			// Speed comparison
			Pokemon slower = p1.getSpeed() <= p2.getSpeed() ? p1 : p2;
			Pokemon faster = p1.getSpeed() >= p2.getSpeed() ? p1 : p2;
			slower.getAbilitySelected().getEffect().onSwitchIn(this, slower, faster);
		}
	}

	// -----------------------------
	// Tests for attacks (466 Electivire, 398 Staraptor, 6 Charizard, 127 Pinsir,
	// 123 Scyther, 16 Pidgey, 95 Onix, 523 Zebstrika, 106 Hitmonlee)
	// -----------------------------
	public void doTest() {
		// Sets the same Pk
		String allPkPlayer = "51,51,51";
		String allPkIA = "381,381,381";

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
			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 28).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 27).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 22).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 29).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 5).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 33).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 10).findFirst().get());
//			pk.addAttacks(pk.getSpecialAttacks().stream().filter(af -> af.getId() == 84).findFirst().get());

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

			pkOpt = this.getPokemon().stream().filter(pk -> pk.getId() == Integer.parseInt(PkID)).findFirst();

			if (pkOpt.isPresent()) {

				// Creates a new instance of Pokemon in memory (otherwise there are problems of
				// duplications)
				this.getIA().addPokemon(new Pokemon(pkOpt.get()));

			}
		}

		// Sets first Pokemon to combat for IA
		this.getIA().setPkCombatting(this.getIA().getPokemon().get(0));

		for (Pokemon pk : this.getIA().getPokemon()) {

//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 7).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 5).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 9).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 19).findFirst().get());
//			pk.addAttacks(pk.getSpecialAttacks().stream().filter(af -> af.getId() == 53).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 23).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 28).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 28).findFirst().get());
			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 15).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 5).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 18).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 17).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 15).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 17).findFirst().get());
//			pk.addAttacks(pk.getOtherAttacks().stream().filter(af -> af.getId() == 47).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 33).findFirst().get());

			// Adds the Ids of attacks chosen in a list
			for (Attack ataChosed : this.getIA().getPkCombatting().getFourPrincipalAttacks()) {

				this.getIA().getPkCombatting().addIdAttack(ataChosed.getId());
			}
		}

		// Select an ability for each Pokemon list
		this.getPlayer().selectAbilityForEachPokemon(this.getAbilities());
		this.getIA().selectAbilityForEachPokemon(this.getAbilities());

		// Sets Pokemon facing to each other
		this.getPlayer().setPkFacing(this.getIA().getPokemon().get(0));
		this.getIA().setPkFacing(this.getPlayer().getPokemon().get(0));

		this.getIA().orderAttacksFromDammageLevelPokemon(this.getEffectPerTypes());
		this.getPlayer().orderAttacksFromDammageLevelPokemon(this.getEffectPerTypes());
	}
}