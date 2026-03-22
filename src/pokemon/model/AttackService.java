package pokemon.model;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import pokemon.enums.AttackCategory;
import pokemon.enums.StatusConditions;

public class AttackService {
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_RESET = "\u001B[0m";

	private final Game game;
	private final StatusService statusService;
	private final AbilityService abilityService;
	private final WeatherService weatherService;
	private final SwitchPokemonService switchPokemonService;

	public AttackService(Game game) {
		this.game = game;
		this.statusService = new StatusService(game);
		this.abilityService = new AbilityService(game);
		this.weatherService = new WeatherService(game);
		this.switchPokemonService = new SwitchPokemonService(game);
	}

	// -----------------------------
	// Get the player choice (attack or change Pokemon)
	// -----------------------------
	public int getPlayerChoice(Scanner sc) {
		System.out.println("Quieres atacar (1) o cambiar de Pokémon (2) :");
		int choice = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");
		return choice;
	}

	// -----------------------------
	// Handle attacks from the turn
	// -----------------------------
	public void handleAttackTurn(Scanner sc) {
		int attackId = choosePlayerAttack(sc);

		printPokemonStates();

		statusService.evaluateStartTurnStatuses();

		preparePlayerAttack(attackId);

		tryIAChangeIfPossible();

		prepareIAAttack();

		// Execute the attack sequence (ordering uses current canAttack and speed)
		handleNormalAttackSequence(sc);

		abilityService.applyAbilitiesBeforeEndTurn();

		List<Player> fainted = statusService.applyTurnStatusReductions();
		for (Player p : fainted)
			switchPokemonService.handleForcedSwitch(p);

		abilityService.applyEndTurnAbilities();

		resetTurnParameters();

		weatherService.applyWeatherEffects(sc);
	}

	// -----------------------------
	// Prepare next attack for player
	// -----------------------------
	private int choosePlayerAttack(Scanner sc) {
		Pokemon pk = game.getPlayer().getPkCombatting();

		if (isForcedAttack(pk))
			return handleForcedAttack(pk);

		if (!hasAnyPPLeft(pk))
			return handleStruggle(pk);

		return askPlayerForAttack(sc, game.getPlayer());
	}

	// -----------------------------
	// Check if can select next attack
	// -----------------------------
	private boolean isForcedAttack(Pokemon pk) {
		return pk.getIsChargingAttackForNextRound() || statusService.isTrappedByOwnAttack(pk);
	}

	// -----------------------------
	// Return same attack
	// -----------------------------
	private int handleForcedAttack(Pokemon pk) {
		if (statusService.isTrappedByOwnAttack(pk))
			System.out.println(
					pk.getName() + " está furioso y continúa atacando con " + pk.getNextMovement().getName() + "!");

		return pk.getNextMovement().getId();
	}

	// -----------------------------
	// Check if PP remaining on attacks from Pokemon
	// -----------------------------
	private boolean hasAnyPPLeft(Pokemon pk) {
		return game.getPlayer().hasAnyPPLeft(pk);
	}

	// -----------------------------
	// Select Struggle as attack if no more PP remaining in any attack
	// -----------------------------
	private int handleStruggle(Pokemon pk) {
		System.out.println(pk.getName() + " no tiene más PPs en ningún ataque.");
		System.out.println(pk.getName() + " usó Forcejeo!");
		return 165;
	}

	// -----------------------------
	// Select an attack
	// -----------------------------
	private int askPlayerForAttack(Scanner sc, Player player) {
		if (!player.getPkCombatting().getCanDonAnythingNextRound()) {
			return player.getPkCombatting().getLastUsedAttack().getId();
		}

		System.out.println("Escoge un ataque :");
		player.printAttacksFromPokemonCombating();

		return readValidAttack(sc, player);
	}

	// -----------------------------
	// Check validity of attack id from player Pokemon
	// -----------------------------
	private int readValidAttack(Scanner sc, Player player) {
		while (true) {
			int attackId = sc.nextInt();
			sc.useDelimiter(";|\r?\n|\r");

			if (!hasAttack(player, attackId)) {
				System.out.println("Escoge un ataque que tenga el Pokémon.");
				continue;
			}

			if (!hasPP(player, attackId)) {
				System.out.println("No tienes más PP para este ataque. Escoge otro.");
				continue;
			}

			return attackId;
		}
	}

	// -----------------------------
	// Check if Pokemon has the attack chosen
	// -----------------------------
	private boolean hasAttack(Player player, int attackId) {
		return player.getPkCombatting().getFourIdAttacks().contains(attackId);
	}

	// -----------------------------
	// Check attack chosen has PP remaining
	// -----------------------------
	private boolean hasPP(Player player, int attackId) {
		Attack atk = player.getPkCombatting().getNextMovementById(attackId);
		return atk.getPp() > 0;
	}

	// -----------------------------
	// Print Pokemon states (for debug)
	// -----------------------------
	private void printPokemonStates() {
		// Normal status player
		System.out.println(ANSI_YELLOW + "Estado normal del Pokémon del jugador : "
				+ game.getPlayer().getPkCombatting().getStatusCondition().getStatusCondition() + ANSI_RESET);

		// Ephemeral status player
		System.out.println(ANSI_YELLOW + "Estados efímeros del Pokémon del jugador : " + ANSI_RESET);
		if (game.getPlayer().getPkCombatting().hasEphemeralStatus()) {
			System.out.println(ANSI_YELLOW + "[" + ANSI_RESET);
			for (Map.Entry<StatusConditions, State> entry : game.getPlayer().getPkCombatting().getEphemeralStatuses()
					.entrySet())
				System.out.println(ANSI_YELLOW + entry.getKey() + ANSI_RESET);

			System.out.println(ANSI_YELLOW + "]" + ANSI_RESET);
		}

		// Normal status IA
		System.out.println(ANSI_YELLOW + "Estado normal del Pokémon de la máquina : "
				+ game.getIA().getPkCombatting().getStatusCondition().getStatusCondition() + ANSI_RESET);
		// Ephemeral status IA
		System.out.println(ANSI_YELLOW + "Estados efímeros del Pokémon de la máquina : " + ANSI_RESET);
		if (game.getIA().getPkCombatting().hasEphemeralStatus()) {
			System.out.println(ANSI_YELLOW + "[" + ANSI_RESET);
			for (Map.Entry<StatusConditions, State> entry : game.getIA().getPkCombatting().getEphemeralStatuses()
					.entrySet())
				System.out.println(ANSI_YELLOW + entry.getKey() + ANSI_RESET);

			System.out.println(ANSI_YELLOW + "]" + ANSI_RESET);
		}
	}

	// -----------------------------
	// Prepare player chosen attack (sets nextMovement etc.)
	// -----------------------------
	private void preparePlayerAttack(int attackId) {
		Pokemon playerPk = game.getPlayer().getPkCombatting();

		if (playerPk.getCanDonAnythingNextRound() && !playerPk.getIsChargingAttackForNextRound())
			game.getPlayer().prepareBestAttackPlayer(attackId, game.getIA().getPkCombatting());
	}

	// -----------------------------
	// IA can decide to change Pokemon only if it's not charging an attack
	// -----------------------------
	private void tryIAChangeIfPossible() {
		Pokemon iaPk = game.getIA().getPkCombatting();

		if (iaPk.getCanDonAnythingNextRound() && !iaPk.getIsChargingAttackForNextRound()
				&& game.getPlayer().getPkCombatting().getAbilitySelected().getId() != 23)
			switchPokemonService.tryIAChange();
	}

	// -----------------------------
	// Prepare IA (select move) only if not charging an attack
	// -----------------------------
	private void prepareIAAttack() {
		Pokemon iaPk = game.getIA().getPkCombatting();

		boolean iaSecondTurnCharged = iaPk.getNextMovement() != null
				&& iaPk.getNextMovement().getCategory() == AttackCategory.CHARGED
				&& iaPk.getIsChargingAttackForNextRound();

		if (!iaSecondTurnCharged && iaPk.getCanDonAnythingNextRound())
			prepareIAIfPossible(iaPk);
	}

	// -----------------------------
	// Prepare attack from IA if can attack (after checking status conditions from
	// the beginning of the turn)
	// -----------------------------
	private void prepareIAIfPossible(Pokemon pkIA) {
		if (pkIA.getIsChargingAttackForNextRound())
			return; // if charging an attack (like fly), cannot choose another attack

		game.getIA().prepareBestAttackIA(game.getPlayer().getPkCombatting());
	}

	// -----------------------------
	// Handle normal attack sequence
	// -----------------------------
	private void handleNormalAttackSequence(Scanner sc) {
		boolean playerFirst = playerCanAttackFirst();

		System.out.println(ANSI_RED + "Velocidad normal jugador : " + game.getPlayer().getPkCombatting().getSpeed()
				+ " / Velocidad efectiva : " + game.getPlayer().getPkCombatting().getEffectiveSpeed() + ANSI_RESET);
		System.out.println(ANSI_RED + "Velocidad normal IA : " + game.getIA().getPkCombatting().getSpeed()
				+ " / Velocidad efectiva : " + game.getIA().getPkCombatting().getEffectiveSpeed() + ANSI_RESET);

		Player first = playerFirst ? game.getPlayer() : game.getIA();
		Player second = playerFirst ? game.getIA() : game.getPlayer();

		// 1. Get order of players
		boolean turnShouldEnd = attackAndCheckIfTurnEnds(first, second, sc);

		// 2. Second player attacks if turn can continue
		if (!turnShouldEnd) {
			attackAndCheckIfTurnEnds(second, first, sc);
		}

		// 3. Reset the flinch/retreat
		game.getIA().getPkCombatting().setHasRetreated(false);
		game.getPlayer().getPkCombatting().setHasRetreated(false);
	}

	// -----------------------------
	// Player attack first
	// -----------------------------
	private boolean playerCanAttackFirst() {
		return game.getPlayer().getPkCombatting().getCanAttack() && game.getPlayer().getPkCombatting()
				.getEffectiveSpeed() >= game.getIA().getPkCombatting().getEffectiveSpeed();
	}

	// -----------------------------
	// Check if Pokemon can attack + do retaliation
	// -----------------------------
	private boolean attackAndCheckIfTurnEnds(Player attacker, Player defender, Scanner sc) {
		// 1. Check early exit conditions (retreat / dead / forced switch)
		if (handlePreAttackInterruptions(attacker, defender, sc))
			return true;

		// 2. Evaluate if attacker can attack
		if (!canAttackerAct(attacker))
			return false;

		// 3. Execute attack or recovery
		executeAttackPhase(attacker);

		// After an attack, if defender was charging a charged attack (like Fly) but is
		// prevented (cannot attack),
		// we must clear the charging flag so that on the next turn we don't remain
		// stuck in charge state.
		// We do this *after* the attack resolution and only if defender cannot attack
		// now.
		// 4. Handle post attack effects on defender
		if (handlePostAttackConsequences(attacker, defender, sc))
			return true;

		return false; // turn continues
	}

	// -----------------------------
	// Handle all conditions that can prevent the attack BEFORE it happens
	// -----------------------------
	private boolean handlePreAttackInterruptions(Player attacker, Player defender, Scanner sc) {
		Pokemon pk = attacker.getPkCombatting();

		// If retreated, cannot attack, but turn continues
		if (pk.getHasRetreated()) {
			System.out.println(pk.getName() + " retrocedió.");
			return false;
		}

		// If Pokemon is debilitated, force change and ends turn
		if (pk.isDebilitated()) {
			statusService.clearDrainEffects(pk, defender.getPkCombatting());
			checkForcedPokemonChange(sc);
			return true;
		}

		// If attacker forces to change because of "Whirlwind" or "Roar", etc.
		if (attacker.getIsForceSwitchPokemon()) {
			switchPokemonService.handleForcedSwitch(attacker);
			return true;
		}
		return false;
	}

	// -----------------------------
	// Evaluate all states to determine if attacker can attack
	// -----------------------------
	private boolean canAttackerAct(Player attacker) {
		Pokemon pk = attacker.getPkCombatting();

		boolean canAttack = statusService.canAttackEvaluatingAllStatesToAttack(pk);

		if (!canAttack) {
			// Ensure we don't keep charging state if we were prevented from attacking
			pk.setIsChargingAttackForNextRound(false);
			return false;
		}
		return true;
	}

	// -----------------------------
	// Execute attack or recovery depending on Pokemon state
	// -----------------------------
	private void executeAttackPhase(Player attacker) {
		Pokemon pk = attacker.getPkCombatting();

		if (pk.getCanDonAnythingNextRound()) {
			if (attacker == game.getPlayer())
				handleRetaliation(game.getPlayer(), game.getIA());
			else
				handleRetaliation(game.getIA(), game.getPlayer());
		} else
			handleRecoveryTurn(pk);
	}

	// -----------------------------
	// Handle cases where Pokemon cannot act this turn (recharge, ability, etc.)
	// -----------------------------
	private void handleRecoveryTurn(Pokemon pk) {
		if (pk.getAbilitySelected().getId() == 54)
			System.out.println(pk.getName() + " (" + pk.getId() + ") no puede atacar o cambiarse a causa de "
					+ pk.getAbilitySelected().getName());
		else
			System.out.println(pk.getName() + " (" + pk.getId() + ") debe recuperarse a causa de "
					+ pk.getLastUsedAttack().getName());

		pk.setCanDonAnythingNextRound(true);
	}

	// -----------------------------
	// Handle all consequences AFTER attack resolution
	// -----------------------------
	private boolean handlePostAttackConsequences(Player attacker, Player defender, Scanner sc) {
		Pokemon defenderPk = defender.getPkCombatting();

		// Cancel charging state if needed
		resetChargingStateIfNeeded(defenderPk);

		// If defender must change because of "Whirlwind" or "Roar", etc.
		if (defender.getIsForceSwitchPokemon()) {
			switchPokemonService.handleForcedSwitch(defender);
			return true;
		}

		// If defender got debilitated during this attack -> force change and end turn
		if (defenderPk.isDebilitated()) {
			statusService.clearDrainEffects(attacker.getPkCombatting(), defenderPk);
			checkForcedPokemonChange(sc);
			return true;
		}
		return false;
	}

	// -----------------------------
	// After an attack, if defender was charging a charged attack (like Fly) but
	// cannot attack anymore, we cancel the charging state
	// -----------------------------
	private void resetChargingStateIfNeeded(Pokemon pk) {
		if (pk.getIsChargingAttackForNextRound() && pk.getNextMovement() != null
				&& pk.getNextMovement().getCategory() == AttackCategory.CHARGED && !pk.getCanAttack())
			pk.setIsChargingAttackForNextRound(false);
	}

	// -----------------------------
	// Check if needed to chose a new Pokemon (ex : combating Pokemon dies from
	// burning in final turn while flying, etc.)
	// -----------------------------
	private void checkForcedPokemonChange(Scanner sc) {
		// Player dies
		if (game.getPlayer().getPkCombatting().isDebilitated())
			handlePlayerPokemonDefeated(sc);

		// IA dies
		if (game.getIA().getPkCombatting().isDebilitated())
			handleIAPokemonDefeated();
	}

	// -----------------------------
	// Select new Pokemon from player
	// -----------------------------
	private void handlePlayerPokemonDefeated(Scanner sc) {
		System.out.println(game.getPlayer().getPkCombatting().getName() + " fue derrotado.");
		System.out.println("¿Qué Pokémon deberías escoger?");

		boolean changed = false;

		while (!changed)
			changed = switchPokemonService.changePokemon(sc);
	}

	// -----------------------------
	// Select new Pokemon from IA
	// -----------------------------
	private void handleIAPokemonDefeated() {
		Pokemon pkIA = game.getIA().getPkCombatting();

		pkIA.removeStates();

		System.out.println(pkIA.getName() + " fue derrotado.");

		Pokemon newIA = game.getIA().decideBestChangePokemon(game.getPlayer().getPkCombatting(),
				game.getEffectPerTypes());

		if (newIA == null)
			newIA = game.getIA().getPokemon().stream().filter(pk -> !pk.isDebilitated()).findFirst().get();

		switchPokemonService.resetPokemonBeforeSwitch(pkIA);

		System.out.println("IA eligió a " + newIA.getName() + " (Id:" + newIA.getId() + ")");

		game.getIA().setPkCombatting(newIA);

		switchPokemonService.updatePkFacingAfterSwitch();

		abilityService.applyEntryAbilityOnSwitch(newIA, game.getPlayer().getPkCombatting());

		switchPokemonService.refreshAttackOrders();

		game.getIA().prepareBestAttackIA(game.getPlayer().getPkCombatting());
	}

	// -----------------------------
	// Handle retaliation (generic for player or IA)
	// -----------------------------
	private void handleRetaliation(Player attacker, Player defender) {
		Pokemon pkAttacker = attacker.getPkCombatting();

		if (pkAttacker.isDebilitated()) {
			handleDebilitatedPokemon(pkAttacker, attacker == game.getPlayer());
			return;
		}

		initializeBattle(attacker, defender);

		if (!pkAttacker.getCanAttack()) {
			handleCannotAttack(pkAttacker, attacker == game.getPlayer());
			return;
		}

		resolveAttack(pkAttacker, attacker == game.getPlayer());
	}

	// -----------------------------
	// Remove states if Pokemon is debilitated
	// -----------------------------
	private void handleDebilitatedPokemon(Pokemon pk, boolean isPlayer) {
		pk.removeStates();

		System.out.println(ANSI_RED + "Pokemon " + (isPlayer ? "player" : "IA") + " is debilitated" + ANSI_RESET);
	}

	// -----------------------------
	// Initialize new battle (Pk vs Pk)
	// -----------------------------
	private void initializeBattle(Player attacker, Player defender) {
		boolean isWeatherSuppressed = game.getisWeatherSuppressed();

		PkVPk battleVS = new PkVPk(attacker, defender, game.getCurrentWeather(), isWeatherSuppressed);
		game.setBattleVS(battleVS);
	}

	// -----------------------------
	// Reset some parameters if cannot attack
	// -----------------------------
	private void handleCannotAttack(Pokemon pk, boolean isPlayer) {
		System.out.println(ANSI_RED + "Pokemon " + (isPlayer ? "player" : "IA") + " cannot attack" + ANSI_RESET);

		pk.setIsChargingAttackForNextRound(false);
	}

	// -----------------------------
	// Resolve attack
	// -----------------------------
	private void resolveAttack(Pokemon pkAttacker, boolean isPlayer) {
		// Get probability of attacking (we already checked for status conditions. Now
		// we do it for evasion/accuracy)
		game.getBattleVS().resolveAttack();

		// Check again cause maybe there are attacks like "Whirlwind" meanwhile Pokemon
		// facing is invulnerable, etc.
		if (!pkAttacker.getCanAttack()) {
			return;
		}

		System.out.println(ANSI_GREEN + "Pokemon " + (isPlayer ? "player" : "IA") + " can attack" + ANSI_RESET);

		executeAttackEffect(pkAttacker);
	}

	// -----------------------------
	// Apply attack from attacker (principal damage)
	// -----------------------------
	private void executeAttackEffect(Pokemon pkAttacker) {
		game.getBattleVS().doAttackEffect(game.getCurrentWeather(), game.getMistIsActivated());

		pkAttacker.setLastUsedAttack(pkAttacker.getNextMovement());

		applyMistIfNeeded(pkAttacker);
	}

	// -----------------------------
	// Apply mist effect after attacking (if needed)
	// -----------------------------
	private void applyMistIfNeeded(Pokemon pk) {
		if (pk.getNextMovement().getId() == 54 && !game.getMistIsActivated()) {
			game.setMistIsActivated(true);
			game.setNbTurnsMistActive(5);
		}
	}

	// -----------------------------
	// Reset parameters from Pokemon
	// -----------------------------
	private void resetTurnParameters() {
		game.getPlayer().getPkCombatting().restartParametersEffect();
		game.getIA().getPkCombatting().restartParametersEffect();
	}

	// -----------------------------
	// Handle attack from IA when player is changing the Pokemon
	// -----------------------------
	public boolean handleChangeTurn(Scanner sc) {
		if (abilityService.isBlockedByMagnetPull(false))
			return false;

		if (abilityService.isBlockedByArenaTrap(false))
			return false;

		if (!handlePlayerChange(sc))
			return false;

		Pokemon pkIA = game.getIA().getPkCombatting();

		statusService.evaluateStatusStartOfTurn(pkIA);
		statusService.canAttackEvaluatingAllStatesToAttack(pkIA);

		if (pkIA.getCanDonAnythingNextRound()) {
			prepareIAIfPossible(pkIA);
			handleChangeSequence(sc); // only IA attacks
		} else
			handleIANotAbleToAct(pkIA);

		// If defender has to change because of "Whirlwind" or "Roar", etc.
		if (game.getPlayer().getIsForceSwitchPokemon())
			switchPokemonService.handleForcedSwitch(game.getPlayer());

		handleEndTurnSequence(sc);

		return true;
	}

	// -----------------------------
	// Handle player change
	// -----------------------------
	private boolean handlePlayerChange(Scanner sc) {
		if (game.getPlayer().getPkCombatting().getCanDonAnythingNextRound()) {
			boolean changed = switchPokemonService.changePokemon(sc);

			if (!changed)
				return false;

		} else {
			System.out.println(game.getPlayer().getPkCombatting().getName() + " ("
					+ game.getPlayer().getPkCombatting().getId() + ") "
					+ (game.getPlayer().getPkCombatting().getAbilitySelected().getId() == 54
							? "no puede cambiarse este turno a causa de "
									+ game.getPlayer().getPkCombatting().getAbilitySelected().getName()
							: "no puede cambiarse este turno a causa de algún ataque o estado"));

			game.getPlayer().getPkCombatting().setCanDonAnythingNextRound(true);
		}
		return true;
	}

	// -----------------------------
	// Handle change sequence
	// -----------------------------
	private void handleChangeSequence(Scanner sc) {
		handleRetaliation(game.getIA(), game.getPlayer());
		checkForcedPokemonChange(sc);
	}

	// -----------------------------
	// Handle if IA can do anything this turn (because of abilities or is a rest
	// turn, etc.)
	// -----------------------------
	private void handleIANotAbleToAct(Pokemon pkIA) {
		if (pkIA.getAbilitySelected().getId() == 54)
			System.out.println(pkIA.getName() + " (" + pkIA.getId() + ") " + "no puede atacar o cambiarse a causa de "
					+ pkIA.getAbilitySelected().getName());
		else
			System.out.println(pkIA.getName() + " (" + pkIA.getId() + ") " + "debe recuperarse a causa de "
					+ pkIA.getLastUsedAttack().getName());

		pkIA.setCanDonAnythingNextRound(true);
	}

	// -----------------------------
	// Apply end turn effects, abilities, etc. (only when handling change turn)
	// -----------------------------
	private void handleEndTurnSequence(Scanner sc) {
		abilityService.applyAbilitiesBeforeEndTurn();

		statusService.reduceNumberTurnsEffects(game.getIA(), game.getPlayer());
		
		statusService.reduceDrainedAllTurnsEffects(game.getIA(), game.getPlayer());
		game.getPlayer().getPkCombatting().doDrainedAllTurnsEffect(game.getIA().getPkCombatting());
		statusService.reduceDrainedAllTurnsEffects(game.getPlayer(), game.getIA());

		abilityService.applyEndTurnAbilities();

		resetTurnParameters();

		weatherService.applyWeatherEffects(sc);
	}
}
