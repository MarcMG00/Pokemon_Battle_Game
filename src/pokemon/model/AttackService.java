package pokemon.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import pokemon.attackInterface.AbsorbEffect;
import pokemon.attackInterface.AttackEffect;
import pokemon.attackInterface.AttackRestOneTurnEffect;
import pokemon.attackInterface.ChargeAttackEffect;
import pokemon.attackInterface.ConditionalPowerEffect;
import pokemon.attackInterface.ConfusedEffect;
import pokemon.attackInterface.CounterAttackEffect;
import pokemon.attackInterface.DisableAttackEffect;
import pokemon.attackInterface.FixedDamageEffect;
import pokemon.attackInterface.FixedRecoilDamageEffect;
import pokemon.attackInterface.ForceSwitchEffect;
import pokemon.attackInterface.IgnoreMinimizeEffect;
import pokemon.attackInterface.LeechSeedEffect;
import pokemon.attackInterface.MistEffect;
import pokemon.attackInterface.MultiHitEffect;
import pokemon.attackInterface.MultiStatBoostEffect;
import pokemon.attackInterface.OneHitKOEffect;
import pokemon.attackInterface.ParalyzeEffect;
import pokemon.attackInterface.PoisonEffect;
import pokemon.attackInterface.RecoilDamageEffect;
import pokemon.attackInterface.SimpleDamageEffect;
import pokemon.attackInterface.SleepEffect;
import pokemon.attackInterface.SolarBeamEffect;
import pokemon.attackInterface.StatBoostEffect;
import pokemon.attackInterface.StatReduceEffect;
import pokemon.attackInterface.TrappedByOwnAttackEffect;
import pokemon.attackInterface.TrappedEffect;
import pokemon.attackInterface.WeightDamageEffect;
import pokemon.enums.AttackCategory;
import pokemon.enums.StatType;
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
	private DamageService damageService;
	private Map<Integer, AttackEffect> attackEffects = new HashMap<>();
	private HelperService helperService;
	private AccuracyService accuracyService;

	public AttackService(Game game) {
		this.game = game;
		this.statusService = new StatusService(game);
		this.abilityService = new AbilityService(game);
		this.weatherService = new WeatherService(game);
		this.switchPokemonService = new SwitchPokemonService(game);
		this.damageService = new DamageService();
		this.helperService = new HelperService();
		this.accuracyService = new AccuracyService();
		initAttackEffects();
	}

	private void initAttackEffects() {
		// Simple damage attacks
		AttackEffect simpleDamage = new SimpleDamageEffect(damageService);
		attackEffects.put(1, simpleDamage); // Destructor/Pound (tested)
		attackEffects.put(2, simpleDamage); // Golpe kárate/Karate chop (tested)
		attackEffects.put(5, simpleDamage); // Megapuño/Mega punch (tested)
		attackEffects.put(6, simpleDamage); // Día de pago/Pay day (tested)
		attackEffects.put(7, simpleDamage); // Puño fuego/Fire punch (tested)
		attackEffects.put(8, simpleDamage); // Puño hielo/Ice punch (tested)
		attackEffects.put(9, simpleDamage); // Puño trueno/Thunder punch (tested)
		attackEffects.put(10, simpleDamage); // Arañazo/Scratch (tested)
		attackEffects.put(11, simpleDamage); // Agarre/Vise grip (tested)
		attackEffects.put(15, simpleDamage); // Corte/Cut (tested)
		attackEffects.put(17, simpleDamage); // Ataque ala/Wing attack (tested)
		attackEffects.put(21, simpleDamage); // Atizar/Slam (tested)
		attackEffects.put(22, simpleDamage); // Látigo cepa/Vine whip (tested)
		attackEffects.put(25, simpleDamage); // Megapatada/Mega kick (tested)
		attackEffects.put(26, simpleDamage); // Patada salto/Jump kick (tested)
		attackEffects.put(27, simpleDamage); // Patada giro/Rolling kick (tested)
		attackEffects.put(29, simpleDamage); // Golpe cabeza/Headbutt (tested)
		attackEffects.put(30, simpleDamage); // Cornada/Horn attack (tested)
		attackEffects.put(33, simpleDamage); // Placaje/Tackle (tested)
		attackEffects.put(34, simpleDamage); // Golpe cuerpo/Body slam (tested)
		attackEffects.put(40, simpleDamage); // Picotazo veneno/Poison sting (tested)
		attackEffects.put(44, simpleDamage); // Mordisco/Bite (tested)
		attackEffects.put(51, simpleDamage); // Acido/Acid (tested)
		attackEffects.put(52, simpleDamage); // Ascuas/Ember (tested)
		attackEffects.put(53, simpleDamage); // Lanzallamas/FlameThrower (tested)
		attackEffects.put(55, simpleDamage); // Pistola agua/Water gun (tested)
		attackEffects.put(56, simpleDamage); // Hidrobomba/Hydro Pump (tested)
		attackEffects.put(58, simpleDamage); // Rayo hielo/Ice beam (tested)
		attackEffects.put(59, simpleDamage); // Ventisca/Blizzard (tested)
		attackEffects.put(60, simpleDamage); // Psicorrayo/Psybeam (tested)
		attackEffects.put(61, simpleDamage); // Rayo burbuja/Bubble beam (tested)
		attackEffects.put(62, simpleDamage); // Rayo aurora/Aurora beam (tested)
		attackEffects.put(64, simpleDamage); // Picotazo/Peck (tested)
		attackEffects.put(65, simpleDamage); // Pico taladro/Drill peck (tested)
		attackEffects.put(70, simpleDamage); // Fuerza/Strength (tested)
		attackEffects.put(75, simpleDamage); // Hoja afilada/Razor leaf (tested)
		attackEffects.put(84, simpleDamage); // Impactrueno/Thunder shock (tested)
		attackEffects.put(85, simpleDamage); // Rayo/Thunderbolt (tested)
		attackEffects.put(87, simpleDamage); // Trueno/Thunder (tested)

		// Multi-hit attacks (normal damage)
		attackEffects.put(3, new MultiHitEffect(helperService, damageService, 1, 5)); // Doble bofetón/Double slap
																						// (tested)
		attackEffects.put(4, new MultiHitEffect(helperService, damageService, 1, 5)); // Puño cometa/Comet punch
																						// (tested)
		attackEffects.put(24, new MultiHitEffect(helperService, damageService, 2, 2)); // Doble patada/Double kick
																						// (tested)
		attackEffects.put(31, new MultiHitEffect(helperService, damageService, 1, 5)); // Ataque furia/Fury attack
																						// (tested)
		attackEffects.put(41, new MultiHitEffect(helperService, damageService, 2, 2)); // Doble ataque/Twineedle
																						// (tested)
		attackEffects.put(42, new MultiHitEffect(helperService, damageService, 1, 5)); // Pin misil/Pin missile (tested)

		// Charge attacks
		AttackEffect chargeAttackDamage = new ChargeAttackEffect(damageService);
		attackEffects.put(13, chargeAttackDamage); // Viento cortante/Razor wind (tested)
		attackEffects.put(19, chargeAttackDamage); // Vuelo/Fly (tested)

		// One hit KO
		attackEffects.put(12, new OneHitKOEffect()); // Guillotina/Guillotine (tested)
		attackEffects.put(32, new OneHitKOEffect()); // Perforador/Horn drill (tested)

		// Buffs stats
		attackEffects.put(14, new StatBoostEffect(StatType.ATTACK, 2)); // Danza espada/Swords dance (tested)

		// Rise power if charging an attack and can hit while invulnerable
		attackEffects.put(16, new ConditionalPowerEffect(damageService, 2f)); // Tornado/Gust (tested)
		attackEffects.put(57, new ConditionalPowerEffect(damageService, 2f)); // Surf/Surf (tested)

		// Forced switch
		attackEffects.put(18, new ForceSwitchEffect()); // Remolino/Whirlwind (tested)
		attackEffects.put(46, new ForceSwitchEffect()); // Rugido/Roar (tested)

		// Trapped effect
		AttackEffect trappedDamage = new TrappedEffect(helperService, damageService);
		attackEffects.put(20, trappedDamage); // Atadura/Bind (tested)
		attackEffects.put(35, trappedDamage); // Constricción/Wrap (tested)
		attackEffects.put(83, trappedDamage); // Giro fuego/Fire spin (tested)

		// Ignore minimize effect (normal damage)
		AttackEffect ignoreMinimizeDamage = new IgnoreMinimizeEffect(damageService);
		attackEffects.put(23, ignoreMinimizeDamage); // Pisotón/Stomp (tested)

		// Reduce stats
		attackEffects.put(28, new StatReduceEffect(StatType.PRECISION, 1)); // Ataque arena /Sand attack (tested)
		attackEffects.put(39, new StatReduceEffect(StatType.DEFENSE, 1)); // Látigo/Tail Whip (tested)
		attackEffects.put(43, new StatReduceEffect(StatType.DEFENSE, 1)); // Malicioso/Leer (tested)
		attackEffects.put(45, new StatReduceEffect(StatType.ATTACK, 1)); // Gruñido/Growl (tested)
		attackEffects.put(81, new StatReduceEffect(StatType.SPEED, 1)); // Disparo démora/String shot (tested)

		// Recoil damage effect
		attackEffects.put(36, new RecoilDamageEffect(damageService, 0.25f)); // Derribo/Take down (tested)
		attackEffects.put(38, new RecoilDamageEffect(damageService, 0.33f)); // Doble filo/Dobule-Edge (tested)
		attackEffects.put(66, new RecoilDamageEffect(damageService, 0.25f)); // Sumisión/Submission (tested)

		// Trapped by own attack effect
		attackEffects.put(37, new TrappedByOwnAttackEffect(helperService, damageService, 2, 5)); // Saña/Thrash (tested)
		attackEffects.put(80, new TrappedByOwnAttackEffect(helperService, damageService, 2, 5)); // Danza pétalo/Petal
																									// dance (tested)

		// Sleep effect
		attackEffects.put(47, new SleepEffect(helperService, 1, 7)); // Canto/Sing (tested)
		attackEffects.put(79, new SleepEffect(helperService, 1, 7)); // Somnífero/Sleep powder (tested)

		// Confused effect
		attackEffects.put(48, new ConfusedEffect(helperService, 1, 7)); // Supersónico/Supersonic (tested)

		// Fixed damage effect
		attackEffects.put(49, new FixedDamageEffect(20f)); // Bomba sónica/Sonic boom (tested)
		attackEffects.put(69, new FixedDamageEffect(100f)); // Sísmico/Seismic toss (tested)
		attackEffects.put(82, new FixedDamageEffect(40f)); // Furia dragón/Dragon rage (tested)

		// Anulación/Disable (tested)
		attackEffects.put(50, new DisableAttackEffect(helperService, 4, 7));

		// Neblina/Mist (tested)
		attackEffects.put(54, new MistEffect());

		// Rest one turn after attack (with damage)
		AttackEffect attackRestOneTourDamage = new AttackRestOneTurnEffect(damageService);
		attackEffects.put(63, attackRestOneTourDamage); // Hiperrayo/Hyper beam (tested)

		// Damage depending on weight
		AttackEffect weightDamage = new WeightDamageEffect(damageService);
		attackEffects.put(67, weightDamage); // Patada baja/Low kick (tested)

		// Contraataque/Counter (tested)
		attackEffects.put(68, new CounterAttackEffect());

		// Do damage and absorb PS effect
		AttackEffect absorbDamage = new AbsorbEffect(damageService);
		attackEffects.put(71, absorbDamage); // Absorber/Absorb (tested)
		attackEffects.put(72, absorbDamage); // Megaagotar/Mega drain (tested)

		// Drenadoras/Leech seed (tested)
		attackEffects.put(73, new LeechSeedEffect());

		// Multi buffs stats
		Map<StatType, Integer> growthStats = new HashMap<>();
		growthStats.put(StatType.ATTACK, 1);
		growthStats.put(StatType.SPECIAL_ATTACK, 1);
		attackEffects.put(74, new MultiStatBoostEffect(growthStats)); // Desarrollo/Growth (tested)

		// Rayo solar/Solar beam (tested)
		attackEffects.put(76, new SolarBeamEffect(damageService));

		// Poison effect
		attackEffects.put(77, new PoisonEffect()); // Polvo veneno/Poison powder (tested)

		// Paralyze effect
		attackEffects.put(78, new ParalyzeEffect()); // Paralizador/Stun spore (tested)
		attackEffects.put(86, new ParalyzeEffect()); // Onda trueno/Thunder wave (tested)

		// Attack and remove constant PS from initial attacker Pokemon
		// (Forcejeo/Struggle, etc.)
		attackEffects.put(165, new FixedRecoilDamageEffect(damageService));
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

			if (!player.getPkCombatting().hasAttack(attackId)) {
				System.out.println("Escoge un ataque que tenga el Pokémon.");
				continue;
			}

			if (!player.getPkCombatting().hasPP(attackId)) {
				System.out.println("No tienes más PP para este ataque. Escoge otro.");
				continue;
			}
			return attackId;
		}
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

		AttackContext ctx = initializeBattle(attacker, defender);

		if (!pkAttacker.getCanAttack()) {
			handleCannotAttack(pkAttacker, attacker == game.getPlayer());
			return;
		}

		resolveAttack(ctx, attacker == game.getPlayer());
	}

	// -----------------------------
	// Remove states if Pokemon is debilitated
	// -----------------------------
	private void handleDebilitatedPokemon(Pokemon pk, boolean isPlayer) {
		pk.removeStates();

		System.out.println(ANSI_RED + "Pokemon " + (isPlayer ? "player" : "IA") + " is debilitated" + ANSI_RESET);
	}

	// -----------------------------
	// Initialize new battle (Attack context)
	// -----------------------------
	private AttackContext initializeBattle(Player attacker, Player defender) {
		AttackContext ctx = buildContext(attacker, defender);
		return ctx;
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
	private void resolveAttack(AttackContext ctx, boolean isPlayer) {

		// Get probability of attacking (we already checked for status conditions. Now
		// we do it for evasion/accuracy)
		accuracyService.resolveAttack(ctx);

		// Check again cause maybe there are attacks like "Whirlwind" meanwhile Pokemon
		// facing is invulnerable, etc.
		if (!ctx.attacker.getCanAttack())
			return;

		System.out.println(ANSI_GREEN + "Pokemon " + (isPlayer ? "player" : "IA") + " can attack" + ANSI_RESET);

		executeAttackEffect(ctx);
	}

	// -----------------------------
	// Apply attack from attacker (principal damage)
	// -----------------------------
	private void executeAttackEffect(AttackContext ctx) {
		Ability abilityDefender = ctx.attacker.getAbilitySelected();

		// Some abilities allows to not to do damage (ex : Volt absorb)
		if (abilityDefender != null) {
			boolean continueAttack = abilityDefender.getEffect().beforeDamage(null, ctx.attacker, ctx.defender,
					ctx.attack);

			if (!continueAttack) {
				ctx.attack.setPp(ctx.attack.getPp() - 1);
				return; // cancel attack
			}
		}

		AttackEffect effect = attackEffects.get(ctx.attack.getId());

		if (effect == null)
			throw new IllegalStateException("No effect defined for attack " + ctx.attack.getId());

		// Gets the attack effect and apply damage
		AttackResult result = effect.execute(ctx);

		applyAfterAttack(ctx, result);

		ctx.attacker.setLastUsedAttack(ctx.attacker.getNextMovement());

		applyMistIfNeeded(ctx.attacker);
	}

	// -----------------------------
	// Build attack context to realize the attack
	// -----------------------------
	private AttackContext buildContext(Player attacker, Player defender) {
		return new AttackContext(attacker.getPkCombatting(), attacker.getPkFacing(), attacker, defender,
				attacker.getPkCombatting().getNextMovement(), game.getCurrentWeather(), game.getisWeatherSuppressed(),
				game.getMistIsActivated(), false);
	}

	// -----------------------------
	// Apply vars + abilities + check if Pokemon are debilitated after realizing the
	// attack
	// -----------------------------
	private void applyAfterAttack(AttackContext ctx, AttackResult result) {
		if (result.hasDealtDamage()) {
			float dmg = result.getDamage();

			if (ctx.attacker.getPhysicalAttacks() != null
					&& ctx.attacker.getPhysicalAttacks().stream().anyMatch(a -> a.getId() == ctx.attack.getId()))
				ctx.defender.setDamageReceived(dmg);

			ctx.defender.setHasReceivedDamage(true);

			applySecondaryEffects(ctx, result, dmg);
		}

		reinitializeAttackStats(ctx.attack);
		ctx.attacker.reinitializeStatsAfterAttack();

		abilityService.applyAbilityAfterDamage(ctx.attacker, ctx.defender, ctx.attack, result.getDamage(),
				result.isCriticalAttack(), ctx.weather, ctx.isWeatherSuppressed);

		if (ctx.defender.getPs() <= 0)
			ctx.defender.setStatusCondition(new State(StatusConditions.DEBILITATED));

		if (ctx.attacker.getPs() <= 0)
			ctx.attacker.setStatusCondition(new State(StatusConditions.DEBILITATED));
	}

	// -----------------------------
	// Do secondary effects from attacks (set status conditions, flinch, etc)
	// -----------------------------
	private void applySecondaryEffects(AttackContext ctx, AttackResult result, float dmg) {
		Ability abilityAttacker = ctx.attacker.getAbilitySelected();

		double probabilityGettingStatus = Math.random();
		int nbTurnsHoldingStatus;

		if (ctx.attack.getSecondaryEffects() == null)
			return;

		for (SecondaryEffect effect : ctx.attack.getSecondaryEffects()) {
			double finalProbability = getFinalSecondaryEffectProbability(effect, ctx.attacker);

			if (probabilityGettingStatus > finalProbability)
				continue;

			switch (effect.getType()) {
			case STATUS_CONDITION:
				ctx.defender.trySetStatus(new State(effect.getStatus()), ctx.weather, ctx.isWeatherSuppressed,
						ctx.attack);
				break;
			case EPHEMERAL_STATUS:
				StatusConditions status = effect.getStatus();

				if (!ctx.defender.trySetEphemeralStatus(status, ctx.attack))
					break;

				if (!ctx.defender.hasActiveEphemeralStatus(status)) {

					nbTurnsHoldingStatus = helperService.randomTurnsAbilitiesConditions(1, 7, ctx);
					State state = new State(status, nbTurnsHoldingStatus + 1);

					ctx.defender.addEphemeralStatus(status, state);

					System.out.println(ctx.defender.getName() + " estará " + status.getMessage() + " por "
							+ nbTurnsHoldingStatus + " turnos");
				}
				break;
			case FLINCH:
				if (!ctx.defender.canBeFlinched())
					break;

				if (abilityAttacker != null && abilityAttacker.getId() == 1) {
					abilityAttacker.getEffect().afterAttack(null, ctx.attacker, ctx.defender, ctx.attack, dmg,
							effect.getProbability(), result.isCriticalAttack(), ctx.weather, ctx.isWeatherSuppressed);
				} else
					ctx.defender.setHasRetreated(true);
				break;
			case STAT_DROP:
				ctx.defender.reduceStatStage(effect.getStat(), effect.getStages(), ctx.isMistEffectActivated);
				break;
			default:
				break;
			}
		}
	}

	// -----------------------------
	// Gets the probability of having the secondary effect
	// -----------------------------
	private double getFinalSecondaryEffectProbability(SecondaryEffect effect, Pokemon attacker) {
		double probability = effect.getProbability();

		Ability ability = attacker.getAbilitySelected();
		if (ability != null && ability.getId() == 32) {
			probability *= 2;
		}

		return Math.min(probability, 1.0); // never > 100%
	}

	// -----------------------------
	// Reset parameters from attacks (to avoid problems each turn)
	// -----------------------------
	private void reinitializeAttackStats(Attack attack) {
		attack.setPrecision(attack.getInitialPrecision());
		attack.setPower(attack.getInitialPower());
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
