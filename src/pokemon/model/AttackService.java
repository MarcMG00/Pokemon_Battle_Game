package pokemon.model;

import java.util.HashMap;
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
import pokemon.attackInterface.MultiStatChange;
import pokemon.attackInterface.OneHitKOEffect;
import pokemon.attackInterface.ParalyzeEffect;
import pokemon.attackInterface.PoisonEffect;
import pokemon.attackInterface.RecoilDamageEffect;
import pokemon.attackInterface.RecoilDamageIfFailsEffect;
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

	private final BattleContext battleCtx;
	private final StatusService statusService;
	private final AbilityService abilityService;
	private final WeatherService weatherService;
	private final SwitchPokemonService switchPokemonService;
	private final StatService statService;
	private DamageService damageService;
	private Map<Integer, AttackEffect> attackEffects = new HashMap<>();
	private HelperService helperService;
	private AccuracyService accuracyService;

	public AttackService(BattleContext battleCtx) {
		this.battleCtx = battleCtx;
		this.statusService = new StatusService();
		this.abilityService = new AbilityService();
		this.weatherService = new WeatherService(battleCtx);
		this.switchPokemonService = new SwitchPokemonService(battleCtx);
		this.statService = new StatService();
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

		// Recoil damage if attacks fails effect
		AttackEffect recoilDamageIfFails = new RecoilDamageIfFailsEffect(damageService);
		attackEffects.put(26, recoilDamageIfFails); // Patada salto/Jump kick (tested)

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
		attackEffects.put(74, new MultiStatChange(growthStats)); // Desarrollo/Growth (tested)

		// Rayo solar/Solar beam (tested)
		attackEffects.put(76, new SolarBeamEffect(damageService));

		// Poison effect
		attackEffects.put(77, new PoisonEffect()); // Polvo veneno/Poison powder (tested)

		// Paralyze effect
		attackEffects.put(78, new ParalyzeEffect()); // Paralizador/Stun spore (tested)
		attackEffects.put(86, new ParalyzeEffect()); // Onda trueno/Thunder wave (tested)

		// Attack and remove constant PS from initial attacker Pokemon
		attackEffects.put(165, new FixedRecoilDamageEffect(damageService)); // Forcejeo/Struggle (tested)
	}

	// -----------------------------
	// Get the player choice (attack or Pokemon switch)
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

		Pokemon pkPlayer = battleCtx.getPkPlayer();
		// Informative : don't stock IA Pokemon in a var because it may switch, so pass
		// through the IAPlayer instead

		TurnContext turnCtx = buildTurnContext();
		// Apply stats effect before turn starts (reduce speed if paralyzed, etc.)
		applyModifierStatsPokemon(pkPlayer, turnCtx);
		applyModifierStatsPokemon(battleCtx.getPkIA(), turnCtx);
		weatherService.applyStatsFromWeather(turnCtx);

		// Informative : Some status conditions can be removed before attacking (for
		// example Frozen), so check at the beginning of the turn
		statusService.evaluateStartTurnStatuses(pkPlayer, battleCtx.getPkIA());

		// PLAYER ACTIONS
		preparePlayerPokemonAttack(attackId);

		// IA ACTIONS
		tryIAPokemonSwitchIfPossible();
		if (battleCtx.getPkIA().canDonAnythingNextRound()) {
			prepareIAPokemonAttack();
		} else
			handleIANotAbleToAct(battleCtx.getPkIA());

		// Execute the attack sequence (ordering uses current canAttack and speed)
		handleNormalAttackSequence(sc, pkPlayer, battleCtx.getPkIA(), turnCtx);

		// POST ATTACK EFFECTS
		// From here, apply effects (from abilities, status conditions, weather, etc.)
		// within the speed from each Pokemon (even for draining status)
		boolean playerAttacksFirst = playerAttacksFirst(pkPlayer, battleCtx.getPkIA(), turnCtx);

		if (playerAttacksFirst) {
			statusService.handleDrainingStatusEffects(pkPlayer, battleCtx.getPkIA());
			statusService.handleDrainingStatusEffects(battleCtx.getPkIA(), pkPlayer);
		} else {
			statusService.handleDrainingStatusEffects(battleCtx.getPkIA(), pkPlayer);
			statusService.handleDrainingStatusEffects(pkPlayer, battleCtx.getPkIA());
		}
		// Informative : Begins draining state if needed => doesn't apply the first turn
		// when the attack is used, so put after draining effect
		statusService.startDrainingEffectIfNeeded(battleCtx);

		// Apply abilities before end of turn (some abilities can neutralize the effect
		// of status conditions, etc.)
		abilityService.applyAbilitiesBeforeEndTurn(battleCtx, playerAttacksFirst);
		// Apply effects from status condtions / ephemeral statuses
		statusService.applyTurnStatusReductions(battleCtx);
		// Apply abilities at the end of turn (if boost some stats, etc.)
		abilityService.applyEndTurnAbilitiesIfNeeded(battleCtx, playerAttacksFirst);

		if (!battleCtx.isWeatherSuppressed())
			weatherService.applyWeatherEffects(sc);

		// Reset some paramenters
		resetParametersEffectEndTurn();

		// Switch Pokemon if needed => Pokemon normally are switch at the end of the
		// turn context. So if one has fainted, ask for switching at the end.
		// Informative : If player is forced to switch, normally it is handled during
		// the method "handleNormalAttackSequence"
		switchPokemonService.switchPokemonAfterEndTurnIfNeeded(battleCtx.getPlayer(), sc);
		switchPokemonService.switchPokemonAfterEndTurnIfNeeded(battleCtx.getIa(), sc);
	}

	// -----------------------------
	// Build Turn context
	// -----------------------------
	private TurnContext buildTurnContext() {
		return new TurnContext();
	}

	// -----------------------------
	// Apply instantly stats modifiers to Pokemon (get speed, etc.) before starting
	// checks
	// -----------------------------
	private void applyModifierStatsPokemon(Pokemon pk, TurnContext turnCtx) {
		turnCtx.setSpeed(pk, statService.getEffectiveSpeed(pk));
	}

	// -----------------------------
	// Prepare next attack for player
	// -----------------------------
	private int choosePlayerAttack(Scanner sc) {
		Pokemon pk = battleCtx.getPkPlayer();

		if (isForcedAttack(pk))
			return handleForcedAttack(pk);

		if (!pk.hasAnyPPLeft())
			return handleStruggle(pk);

		return askPlayerForAttack(sc, battleCtx.getPlayer());
	}

	// -----------------------------
	// Check if can select next attack
	// -----------------------------
	private boolean isForcedAttack(Pokemon pk) {
		return pk.isChargingAttackForNextRound() || statusService.isTrappedByOwnAttack(pk);
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
	// Select 165_Struggle as attack if no more PP remaining in any attack
	// -----------------------------
	private int handleStruggle(Pokemon pk) {
		System.out.println(pk.getName() + " no tiene más PPs en ningún ataque.");
		System.out.println(pk.getName() + " tendrá que usar Forcejeo!");
		return 165;
	}

	// -----------------------------
	// Select an attack
	// -----------------------------
	private int askPlayerForAttack(Scanner sc, Player player) {
		if (!player.getPkCombatting().canDonAnythingNextRound())
			return player.getPkCombatting().getLastUsedAttack().getId();

		System.out.println("Escoge un ataque :");
		player.printAttacksFromPokemonCombating();

		return readValidAttack(sc, player);
	}

	// -----------------------------
	// Check validity of attack id from Pokemon player
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
		// NORMAL STATUSES player
		System.out.println(ANSI_YELLOW + "Estado normal del Pokémon del jugador : "
				+ battleCtx.getPkPlayer().getStatusCondition().getStatusCondition() + ANSI_RESET);

		// EPHEMERAL STATUSES PLAYER
		System.out.println(ANSI_YELLOW + "Estados efímeros del Pokémon del jugador : " + ANSI_RESET);
		if (battleCtx.getPkPlayer().hasEphemeralStatus()) {
			System.out.println(ANSI_YELLOW + "[" + ANSI_RESET);
			for (Map.Entry<StatusConditions, State> entry : battleCtx.getPkPlayer().getEphemeralStatuses().entrySet())
				System.out.println(ANSI_YELLOW + entry.getKey() + ANSI_RESET);

			System.out.println(ANSI_YELLOW + "]" + ANSI_RESET);
		}

		// NORMAL STATUSES IA
		System.out.println(ANSI_YELLOW + "Estado normal del Pokémon de la máquina : "
				+ battleCtx.getPkIA().getStatusCondition().getStatusCondition() + ANSI_RESET);

		// EPHEMERAL STATUSES IA
		System.out.println(ANSI_YELLOW + "Estados efímeros del Pokémon de la máquina : " + ANSI_RESET);
		if (battleCtx.getPkIA().hasEphemeralStatus()) {
			System.out.println(ANSI_YELLOW + "[" + ANSI_RESET);
			for (Map.Entry<StatusConditions, State> entry : battleCtx.getPkIA().getEphemeralStatuses().entrySet())
				System.out.println(ANSI_YELLOW + entry.getKey() + ANSI_RESET);

			System.out.println(ANSI_YELLOW + "]" + ANSI_RESET);
		}
	}

	// -----------------------------
	// Prepare player chosen attack (sets nextMovement etc.)
	// -----------------------------
	private void preparePlayerPokemonAttack(int attackId) {
		Pokemon playerPk = battleCtx.getPkPlayer();

		if (playerPk.canDonAnythingNextRound() && !playerPk.isChargingAttackForNextRound())
			AttackAnalyzer.prepareBestAttackPlayer(battleCtx.getPlayer(), attackId, battleCtx.getPkIA());
	}

	// -----------------------------
	// IA can decide to change Pokemon only if it's not charging an attack
	// -----------------------------
	private void tryIAPokemonSwitchIfPossible() {
		Pokemon iaPk = battleCtx.getPkIA();

		if (!iaPk.canDonAnythingNextRound() || iaPk.isChargingAttackForNextRound())
			return;

		if (abilityService.isBlockedByMagnetPull(battleCtx, true))
			return;

		if (abilityService.isBlockedByArenaTrap(battleCtx, true))
			return;

		if (battleCtx.getPkPlayer().hasShadowTagAbility()) {
			System.out.println("No puede cambiar de Pokémon a causa de Sombra trampa del Pokémon rival");
			return;
		}

		if (iaPk.hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {
			System.out.println("No puede cambiar de Pokémon ya que está atrapado (bajo un efecto o ataque)");
			return;
		}

		if (iaPk.hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)) {
			System.out.println("No puede cambiar de Pokémon ya que está atrapado (bajo su propio ataque)");
			return;
		}

		switchPokemonService.tryIAPokemonSwitch();
	}

	// -----------------------------
	// Prepare IA (select move) only if not charging an attack
	// -----------------------------
	private void prepareIAPokemonAttack() {
		Pokemon iaPk = battleCtx.getPkIA();

		boolean isPkChargingAttack = iaPk.getNextMovement() != null
				&& iaPk.getNextMovement().getCategory() == AttackCategory.CHARGED
				&& iaPk.isChargingAttackForNextRound();

		if (!isPkChargingAttack)
			AttackAnalyzer.prepareBestAttackIA(battleCtx.getIa(), battleCtx.getPkPlayer());
	}

	// -----------------------------
	// Handle normal attack sequence (both players attack)
	// -----------------------------
	private void handleNormalAttackSequence(Scanner sc, Pokemon playerPk, Pokemon iaPk, TurnContext turnCtx) {
		boolean playerAttacksFirst = playerAttacksFirst(playerPk, iaPk, turnCtx);

		System.out.println(ANSI_RED + "Velocidad normal jugador : " + playerPk.getSpeed() + " / Velocidad efectiva : "
				+ turnCtx.getSpeed(playerPk) + ANSI_RESET);
		System.out.println(ANSI_RED + "Velocidad normal IA : " + iaPk.getSpeed() + " / Velocidad efectiva : "
				+ turnCtx.getSpeed(iaPk) + ANSI_RESET);

		// 1. Get order of players
		Player attacker = playerAttacksFirst ? battleCtx.getPlayer() : battleCtx.getIa();
		Player defender = playerAttacksFirst ? battleCtx.getIa() : battleCtx.getPlayer();

		// 2. First player attacks
		boolean turnShouldEnd = attackAndCheckIfTurnEnds(attacker, defender, sc, turnCtx);

		// 3. Second player attacks if turn can continue
		if (!turnShouldEnd)
			attackAndCheckIfTurnEnds(defender, attacker, sc, turnCtx);
	}

	// -----------------------------
	// Player attack first
	// -----------------------------
	private boolean playerAttacksFirst(Pokemon playerPk, Pokemon iaPk, TurnContext ctx) {
		if (!playerPk.canAttack())
			return false;

		int playerPriority = abilityService.getSpeedPriorityModifier(playerPk);
		int iaPriority = abilityService.getSpeedPriorityModifier(iaPk);

		if (playerPriority != iaPriority)
			return playerPriority > iaPriority;

		return ctx.getSpeed(playerPk) >= ctx.getSpeed(iaPk);
	}

	// -----------------------------
	// Check if Pokemon can attack + do retaliation
	// -----------------------------
	private boolean attackAndCheckIfTurnEnds(Player attacker, Player defender, Scanner sc, TurnContext turnCtx) {
		// 1. Check early exit conditions (retreat / dead / forced switch)
		if (handlePreAttackInterruptions(attacker, defender, sc))
			return true;

		// 2. Evaluate if attacker can attack
		if (!canAttackerAct(attacker))
			return false;

		// 3. Execute attack or recovery
		executeAttackPhase(attacker, turnCtx);

		// After an attack, if defender was charging a charged attack (like Fly) but is
		// prevented (cannot attack),
		// we must clear the charging flag so that on the next turn we don't remain
		// stuck in charge state.
		// We do this *after* the attack resolution and only if defender cannot attack
		// now.
		// 4. Handle post attack effects on defender
		if (handleDefenderPostAttackConsequences(attacker, defender, sc))
			return true;

		return false; // turn continues
	}

	// -----------------------------
	// Handle all conditions that can prevent the attack BEFORE it happens
	// -----------------------------
	private boolean handlePreAttackInterruptions(Player attacker, Player defender, Scanner sc) {
		Pokemon pkAttacker = attacker.getPkCombatting();

		// If retreated, Pokemon cannot attack
		// Put informative message
		if (pkAttacker.hasRetreated() && !pkAttacker.hasFainted()) {
			System.out.println(pkAttacker.getName() + " retrocedió.");
			return true;
		}

		// If attacker forces to change because of "Whirlwind" or "Roar", etc.
		if (attacker.isForcedSwitchPokemon()) {
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

		statusService.canAttackEvaluatingAllStatesToAttack(pk);

		if (!pk.canAttack()) {
			// Ensure we don't keep charging state if we were prevented from attacking
			pk.setIsChargingAttackForNextRound(false);
			return false;
		}
		return true;
	}

	// -----------------------------
	// Execute attack or recovery depending on Pokemon state
	// -----------------------------
	private void executeAttackPhase(Player attacker, TurnContext turnCtx) {
		Pokemon pk = attacker.getPkCombatting();

		if (pk.canDonAnythingNextRound()) {
			if (attacker == battleCtx.getPlayer())
				handleRetaliation(battleCtx.getPlayer(), battleCtx.getIa(), turnCtx);
			else
				handleRetaliation(battleCtx.getIa(), battleCtx.getPlayer(), turnCtx);
		} else
			handleRecoveryTurn(pk);
	}

	// -----------------------------
	// Handle cases where Pokemon cannot act this turn (recharge, ability, etc.)
	// -----------------------------
	private void handleRecoveryTurn(Pokemon pk) {
		if (pk.hasTruantAbility())
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
	private boolean handleDefenderPostAttackConsequences(Player attacker, Player defender, Scanner sc) {
		Pokemon defenderPk = defender.getPkCombatting();

		// Cancel charging state if needed
		resetChargingStateIfNeeded(defenderPk);

		// If defender must change because of "Whirlwind" or "Roar", etc.
		if (defender.isForcedSwitchPokemon()) {
			switchPokemonService.handleForcedSwitch(defender);
			return true;
		}

		return false;
	}

	// -----------------------------
	// After an attack, if defender was charging a charged attack (like Fly) but
	// cannot attack anymore, we cancel the charging state
	// -----------------------------
	private void resetChargingStateIfNeeded(Pokemon pk) {
		if (pk.isChargingAttackForNextRound() && pk.getNextMovement() != null
				&& pk.getNextMovement().getCategory() == AttackCategory.CHARGED && !pk.canAttack())
			pk.setIsChargingAttackForNextRound(false);
	}

	// -----------------------------
	// Check if needed to chose a new Pokemon (ex : combating Pokemon dies from
	// burning in final turn while flying, etc.)
	// -----------------------------
	private void handleIASwitchIfNeeded(Scanner sc) {
		// IA is debilitated
		if (battleCtx.getPkIA().hasFainted())
			handleIAPokemonDefeated();
	}

	// -----------------------------
	// Select new Pokemon from IA
	// -----------------------------
	private void handleIAPokemonDefeated() {
		Pokemon pkIA = battleCtx.getPkIA();
		Pokemon pkPlayer = battleCtx.getPkPlayer();

		statusService.removeStates(pkIA);
		// Remove some states from Pokemon remaining in the field
		statusService.removeDrainingState(pkPlayer);

		System.out.println(pkIA.getName() + " fue derrotado.");

		Pokemon pkEnteringIA = switchPokemonService.decideBestChangePokemon(battleCtx.getIa(), pkPlayer,
				battleCtx.getEffectPerTypes());

		if (pkEnteringIA == null)
			pkEnteringIA = battleCtx.getIa().getPokemon().stream().filter(pk -> !pk.hasFainted()).findFirst().get();

		switchPokemonService.resetPokemonBeforeSwitch(pkIA);

		System.out.println("IA eligió a " + pkEnteringIA.getName() + " (Id:" + pkEnteringIA.getId() + ")");

		pkEnteringIA.setJustEnteredBattle(true);
		battleCtx.getIa().setPkCombatting(pkEnteringIA);

		switchPokemonService.updatePkFacingAfterSwitch();

		abilityService.applyAbilityOnSwitchInIfNeeded(battleCtx, pkEnteringIA, pkPlayer);

		switchPokemonService.refreshAttackOrders();
	}

	// -----------------------------
	// Handle retaliation (generic for player or IA)
	// -----------------------------
	private void handleRetaliation(Player attacker, Player defender, TurnContext turnCtx) {
		Pokemon pkAttacker = attacker.getPkCombatting();
		Pokemon pkDefender = defender.getPkCombatting();

		if (pkAttacker.hasFainted()) {
			handleDebilitatedPokemon(pkAttacker, pkDefender, attacker == battleCtx.getPlayer());
			return;
		}

		AttackContext ctx = initializeBattle(attacker, defender);
		ctx.setTurnContext(turnCtx);

		if (!pkAttacker.canAttack()) {
			handleCannotAttack(pkAttacker, attacker == battleCtx.getPlayer());
			return;
		}

		resolveAccuracyAttack(ctx, attacker == battleCtx.getPlayer());
	}

	// -----------------------------
	// Remove states if Pokemon is debilitated
	// -----------------------------
	private void handleDebilitatedPokemon(Pokemon pkAttacker, Pokemon pkDefender, boolean isPlayer) {
		statusService.removeStates(pkAttacker);
		// Remove some states from Pokemon remaining in the field
		statusService.removeDrainingState(pkDefender);

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
	// Reset some parameters if cannot attack + informative message
	// -----------------------------
	private void handleCannotAttack(Pokemon pk, boolean isPlayer) {
		System.out.println(ANSI_RED + "Pokemon " + (isPlayer ? "player" : "IA") + " cannot attack" + ANSI_RESET);
		// Ensure we don't keep charging state if we were prevented from attacking
		pk.setIsChargingAttackForNextRound(false);
	}

	// -----------------------------
	// Resolve attack
	// -----------------------------
	private void resolveAccuracyAttack(AttackContext ctx, boolean isPlayer) {
		// Get probability of attacking (we already checked for status conditions. Now
		// we do it for evasion/accuracy)
		accuracyService.resolveAccuracyAttack(ctx);

		// Check again cause maybe there are attacks like "Whirlwind" meanwhile Pokemon
		// facing is invulnerable, etc.
		if (!ctx.getAttacker().canAttack())
			return;

		System.out.println(ANSI_GREEN + "Pokemon " + (isPlayer ? "player" : "IA") + " can attack" + ANSI_RESET);

		executeAttackEffect(ctx);
	}

	// -----------------------------
	// Apply attack from attacker (principal damage)
	// -----------------------------
	private void executeAttackEffect(AttackContext ctx) {
		Ability abilityDefender = ctx.getDefender().getAbilitySelected();

		// Some abilities allow to not to do damage (ex : Volt absorb)
		if (abilityDefender != null) {
			boolean continueAttack = abilityDefender.getEffect().beforeDamage(null, ctx.getAttacker(), ctx.getAttack());

			if (!continueAttack) {
				ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);
				return; // cancel attack
			}
		}

		AttackEffect effect = attackEffects.get(ctx.getAttack().getId());

		if (effect == null)
			throw new IllegalStateException("No effect defined for attack " + ctx.getAttack().getId());

		// Cannot attack if attack type doesn't affect to rival (and is not an attack
		// used to attacker, for instance that allows to boost his stats)
		if (!isEffectiveAttack(ctx)) {
			System.out.println(ctx.getAttack().getName() + " no afecta al " + ctx.getDefender().getName() + " (Id:"
					+ ctx.getDefender().getId() + ")" + " rival");
		} else {
			// Gets the attack effect and apply damage
			AttackResult result = effect.execute(ctx);

			ctx.getAttacker().setLastUsedAttack(ctx.getAttack());

			handlePostAttackRetaliation(ctx, result);

			applyMistIfNeeded(ctx.getAttacker());
		}
	}

	// -----------------------------
	// Check if attack has effect to rival of applied to attacker
	// -----------------------------
	private boolean isEffectiveAttack(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();
		Attack attack = ctx.getAttack();

		// Defender
		Pokemon defender = ctx.getDefender();
		boolean isDefenderGhostType = defender.getTypes().stream().anyMatch(t -> t.isGhostType());

		// Principal effect of the attack
		boolean attackNoEffectToRival = attacker.getNoEffectAttacks().stream()
				.anyMatch(a -> a.getId() == attack.getId());
		boolean isAttackAppliedToAttacker = attack.isAppliedToAttacker();

		// 1. Can use attack if attacker applies it on itself (to boost stats, etc.)
		if (isAttackAppliedToAttacker)
			return true;

		// 2. 113_Scrappy ability => normal and fight type affects to ghost type
		if (attackNoEffectToRival && attacker.hasScrappyAbility() && isDefenderGhostType
				&& (attack.isNormalType() || attack.isFightingType())) {
			System.out.println(ctx.getAttacker().getName() + " puede atacar dada su habilidad Intrépido");

			return true;
		}

		// 3. Last condition => nothing applied, just check if doesn't affect at all
		if (attackNoEffectToRival)
			return false;

		return true;
	}

	// -----------------------------
	// Build attack context to realize the attack
	// -----------------------------
	private AttackContext buildContext(Player attacker, Player defender) {
		return new AttackContext(attacker, defender, battleCtx.getWeather(), battleCtx.isWeatherSuppressed(),
				battleCtx.isMistActive());
	}

	// -----------------------------
	// Apply vars + abilities + check if Pokemon are debilitated after realizing the
	// attack
	// -----------------------------
	private void handlePostAttackRetaliation(AttackContext ctx, AttackResult result) {
		if (result.hasDealtDamage()) {
			float dmg = result.getDamage();

			if (ctx.getAttacker().getPhysicalAttacks() != null && ctx.getAttacker().getPhysicalAttacks().stream()
					.anyMatch(a -> a.getId() == ctx.getAttack().getId())) {
				ctx.getDefender().setDamageReceived(dmg);
			}

			ctx.getDefender().setHasReceivedDamage(true);

			applyAttackSecondaryEffects(ctx, result, dmg);
		}

		abilityService.applyAbilityAfterDamageIfNeeded(ctx.getAttacker(), ctx.getDefender(), ctx.getAttack(),
				result.getDamage(), result.isCriticalAttack(), ctx.getWeather(), ctx.isWeatherSuppressed());

		// System.out.println("PS actuales de " + ctx.getDefender().getName() + " : " +
		// ctx.getDefender().getPs());
		if (ctx.getDefender().hasFainted())
			ctx.getDefender().setStatusCondition(new State(StatusConditions.DEBILITATED));

		// System.out.println("PS actuales de " + ctx.getAttacker().getName() + " : " +
		// ctx.getAttacker().getPs());
		if (ctx.getAttacker().hasFainted())
			ctx.getAttacker().setStatusCondition(new State(StatusConditions.DEBILITATED));
	}

	// -----------------------------
	// Do secondary effects from attacks (set status conditions, flinch, etc)
	// -----------------------------
	private void applyAttackSecondaryEffects(AttackContext ctx, AttackResult result, float dmg) {
		Ability abilityAttacker = ctx.getAttacker().getAbilitySelected();

		double probabilityGettingStatus = Math.random();
		int nbTurnsHoldingStatus;

		if (ctx.getAttack().getSecondaryEffects() == null)
			return;

		// 125_Sheer_force doesn't apply secondary effects (only on opponent)
		if (ctx.getAttacker().hasSheerForceAbility()) {
			System.out.println(ctx.getDefender().getName()
					+ " no sufrió ningún efecto secundario dada la habildiad del atacante : "
					+ abilityAttacker.getName());
			return;
		}

		for (SecondaryEffect effect : ctx.getAttack().getSecondaryEffects()) {
			double finalProbability = getFinalSecondaryEffectProbability(effect, ctx.getAttacker());

			if (probabilityGettingStatus > finalProbability)
				continue;

			switch (effect.getType()) {
			case STATUS_CONDITION:
				statusService.trySetStatus(ctx.getDefender(), new State(effect.getStatus()), ctx.getWeather(),
						ctx.isWeatherSuppressed(), ctx.getAttack());
				break;
			case EPHEMERAL_STATUS:
				StatusConditions status = effect.getStatus();
				if (!ctx.getDefender().hasActiveEphemeralStatus(status)) {
					nbTurnsHoldingStatus = helperService.randomTurnsAbilitiesConditions(1, 7, ctx);
					State state = new State(status, nbTurnsHoldingStatus + 1);

					ctx.getStatusService().trySetEphemeralStatus(state, ctx.getDefender(), status, ctx.getAttack());
				}
				break;
			case FLINCH:
				if (!ctx.getDefender().canBeFlinched())
					break;

				if (ctx.getAttacker().hasStenchAbility())
					abilityAttacker.getEffect().afterAttack(null, ctx.getAttacker(), ctx.getDefender(), ctx.getAttack(),
							dmg, effect.getProbability(), result.isCriticalAttack(), ctx.getWeather(),
							ctx.isWeatherSuppressed());
				else {
					ctx.getDefender().setHasRetreated(true);
					ctx.getDefender().setCanAttack(false);
				}
				break;
			case STAT_DROP:
				statService.reduceStatStage(ctx.getDefender(), effect.getStat(), effect.getStages(),
						ctx.isMistActive());
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

		if (attacker.hasSereneGraceAbility())
			probability *= 2;

		return Math.min(probability, 1.0); // never > 100%
	}

	// -----------------------------
	// Apply mist effect after attacking (if needed)
	// -----------------------------
	private void applyMistIfNeeded(Pokemon pk) {
		if (pk.getNextMovement().isMist() && !battleCtx.isMistActive()) {
			battleCtx.setMistActive(true);
			battleCtx.setNbTurnsMistActive(6);
		}
	}

	// -----------------------------
	// Reset parameters from Pokemon
	// -----------------------------
	private void resetParametersEffectEndTurn() {
		battleCtx.getPkPlayer().restartParametersEffectEndTurn();
		battleCtx.getPkIA().restartParametersEffectEndTurn();
	}

	// -----------------------------
	// Handle attack from IA when player is changing the Pokemon
	// -----------------------------
	public boolean handlePlayerSwitchIAAttacks(Scanner sc) {
		if (isPokemonForbidenToSwitch(sc))
			return true;

		Pokemon pkIA = battleCtx.getPkIA();

		TurnContext turnCtx = buildTurnContext();

		// Modify stats from IA Pokemon
		applyModifierStatsPokemon(pkIA, turnCtx);
		weatherService.applyStatsFromWeather(turnCtx);

		// Evaluate all the status conditions / ephemeral statuses (to determine if can
		// attack)
		statusService.evaluateStatusStartOfTurn(pkIA);
		statusService.canAttackEvaluatingAllStatesToAttack(pkIA);

		if (pkIA.canDonAnythingNextRound()) {
			prepareIAPokemonAttack();
			handleIAAttacksSequence(sc, turnCtx); // only IA attacks
		} else
			handleIANotAbleToAct(pkIA);

		// => Switching Pokemon it's only done at the end of the turn and after applying
		// effects => Only way to switch Pokemon is if it's forced to change because of
		// "Whirlwind" or "Roar", etc.
		if (battleCtx.getPlayer().isForcedSwitchPokemon())
			switchPokemonService.handleForcedSwitch(battleCtx.getPlayer());

		handlePlayerSwitchIAAttacksEndTurnSequence(sc);

		return false;
	}

	// -----------------------------
	// Check if Player can switch Pokemon / or if Pokemon switch is a valid Pokemon
	// -----------------------------
	private boolean isPokemonForbidenToSwitch(Scanner sc) {
		if (abilityService.isBlockedByMagnetPull(battleCtx, false))
			return true;

		if (abilityService.isBlockedByArenaTrap(battleCtx, false))
			return true;

		if (battleCtx.getPkIA().hasShadowTagAbility()) {
			System.out.println("No puedes cambiar de Pokémon a causa de Sombra trampa del Pokémon rival");
			return true;
		}

		if (battleCtx.getPkPlayer().hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {
			System.out.println("No puedes cambiar de Pokémon ya que está atrapado (bajo un efecto o ataque)");
			return true;
		}

		if (battleCtx.getPkPlayer().hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)) {
			System.out.println("No puedes cambiar de Pokémon ya que está atrapado (bajo su propio ataque)");
			return true;
		}

		if (!handlePlayerChange(sc))
			return true;

		return false;
	}

	// -----------------------------
	// Handle player change
	// -----------------------------
	private boolean handlePlayerChange(Scanner sc) {
		if (battleCtx.getPkPlayer().canDonAnythingNextRound()) {
			boolean changed = switchPokemonService.changePokemon(sc);

			if (!changed)
				return false;

		} else {
			System.out.println(battleCtx.getPkPlayer().getName() + " (" + battleCtx.getPkPlayer().getId() + ") "
					+ (battleCtx.getPkPlayer().hasTruantAbility()
							? "no puede cambiarse este turno a causa de "
									+ battleCtx.getPkPlayer().getAbilitySelected().getName()
							: "no puede cambiarse este turno a causa de algún ataque o estado"));

			battleCtx.getPkPlayer().setCanDonAnythingNextRound(true);
		}
		return true;
	}

	// -----------------------------
	// Handle change sequence
	// -----------------------------
	private void handleIAAttacksSequence(Scanner sc, TurnContext turnCtx) {
		handleRetaliation(battleCtx.getIa(), battleCtx.getPlayer(), turnCtx);
		handleIASwitchIfNeeded(sc);
	}

	// -----------------------------
	// Handle if IA can do anything this turn (because of abilities or is a rest
	// turn, etc.) + informative message
	// -----------------------------
	private void handleIANotAbleToAct(Pokemon pkIA) {
		if (pkIA.hasTruantAbility())
			System.out.println(pkIA.getName() + " (" + pkIA.getId() + ") " + "no puede atacar o cambiarse a causa de "
					+ pkIA.getAbilitySelected().getName());
		else
			System.out.println(pkIA.getName() + " (" + pkIA.getId() + ") " + "debe recuperarse a causa de "
					+ pkIA.getLastUsedAttack().getName());

		pkIA.setCanDonAnythingNextRound(true);
	}

	// -----------------------------
	// Apply end turn effects status conditions, abilities, etc. (only when handling
	// Player switching manually - only IA attacks)
	// -----------------------------
	private void handlePlayerSwitchIAAttacksEndTurnSequence(Scanner sc) {
		// Only apply "before end turn" ability for IA if hasn't just entered
		abilityService.applyIAAbilitiesBeforeEndTurnIfNeeded(battleCtx);

		// STATUS CONDITIONS
		// Player
		statusService.handleStatusConditionsEndTurn(battleCtx.getPkPlayer(), battleCtx.getPkIA());
		// IA
		statusService.handleStatusConditionsEndTurn(battleCtx.getPkIA(), battleCtx.getPkPlayer());
		// Informative : Draining effects are not applied because they are removed when
		// switching Pokemon

		// Only apply "end turn" ability for IA (because Player has switch) if hasn't
		// just entered
		abilityService.applyIAEndTurnAbilitiesIfNeeded(battleCtx);

		// Apply weather effects for both players
		if (!battleCtx.isWeatherSuppressed())
			weatherService.applyWeatherEffects(sc);

		// Reset parameters for both players (just in case)
		resetParametersEffectEndTurn();

		// Switch Pokemon if needed => only switch at the end of the turns (even if
		// Pokemon have fainted during abilities before the end of the turn because of
		// some status effects, etc.)
		switchPokemonService.switchPokemonAfterEndTurnIfNeeded(battleCtx.getPlayer(), sc);
		switchPokemonService.switchPokemonAfterEndTurnIfNeeded(battleCtx.getIa(), sc);
	}
}
