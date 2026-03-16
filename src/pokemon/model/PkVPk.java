package pokemon.model;

import pokemon.enums.AttackCategory;
import pokemon.enums.SecondaryEffectType;
import pokemon.enums.StatType;
import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;

public class PkVPk {

	// ==================================== FIELDS
	// ====================================

	private Pokemon pkCombatting;
	private Pokemon pkFacing;
	private Player attacker;
	private Player defender;
	private Weather weather;
	private boolean isWeatherSuppressed;
	private boolean isACriticAttack;

	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	private static final String ANSI_RESET = "\u001B[0m";

	// ==================================== CONSTRCUTORS
	// ====================================

	public PkVPk(Player attacker, Player defender, Weather weather, boolean isWeatherSuppressed) {
		this.attacker = attacker;
		this.defender = defender;
		this.pkCombatting = attacker.getPkCombatting();
		this.pkFacing = defender.getPkCombatting();
		this.weather = weather;
		this.isWeatherSuppressed = isWeatherSuppressed;
		this.isACriticAttack = false;
	}

	public PkVPk() {
		this.attacker = new Player();
		this.defender = new Player();
		this.pkCombatting = new Pokemon();
		this.pkFacing = new Pokemon();
		this.weather = Weather.NONE;
		this.isWeatherSuppressed = false;
		this.isACriticAttack = false;
	}

	// ==================================== GETTERS/SETTERS
	// ====================================

	public Pokemon getPkCombatting() {
		return pkCombatting;
	}

	public void setPkCombatting(Pokemon pkCombatting) {
		this.pkCombatting = pkCombatting;
	}

	public Pokemon getPkFacing() {
		return pkFacing;
	}

	public void setPkFacing(Pokemon pkFacing) {
		this.pkFacing = pkFacing;
	}

	public Player getAttacker() {
		return attacker;
	}

	public void setAttacker(Player attacker) {
		this.attacker = attacker;
	}

	public Player getDefender() {
		return defender;
	}

	public void setDefender(Player defender) {
		this.defender = defender;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public boolean getIsWeatherSuppressed() {
		return isWeatherSuppressed;
	}

	public void setIsWeatherSuppressed(boolean isWeatherSuppressed) {
		this.isWeatherSuppressed = isWeatherSuppressed;
	}

	public boolean getIsACriticAttack() {
		return isACriticAttack;
	}

	public void setIsACriticAttack(boolean isACriticAttack) {
		this.isACriticAttack = isACriticAttack;
	}

	// ==================================== METHODS
	// ====================================

	// -----------------------------
	// Knows the evasion or accuracy for the Pokemon selected (1 is for accuracy, 2
	// is for evasion)
	// -----------------------------
	public float getEvasionOrAccuracy(Pokemon pk, int t) {

		int evAcu = 0;
		float resultEvAcu = 1;

		switch (t) {
		case 1:
			evAcu = pk.getEffectivePrecision();
			break;
		case 2:
			evAcu = pk.getEffectiveEvasion();

			// 80_Snow_Cloak sets 20% more of evasion
			if (this.getPkFacing().getAbilitySelected().getId() == 80)
				evAcu *= 1.2f;
			break;
		}

		switch (evAcu) {
		case -6:
			resultEvAcu = 3f / 9f;
			break;
		case -5:
			resultEvAcu = 3f / 8f;
			break;
		case -4:
			resultEvAcu = 3f / 7f;
			break;
		case -3:
			resultEvAcu = 3f / 6f;
			break;
		case -2:
			resultEvAcu = 3f / 5f;
			break;
		case -1:
			resultEvAcu = 3f / 4f;
			break;
		case 1:
			resultEvAcu = 4f / 3f;
			break;
		case 2:
			resultEvAcu = 5f / 3f;
			break;
		case 3:
			resultEvAcu = 6f / 3f;
			break;
		case 4:
			resultEvAcu = 7f / 3f;
			break;
		case 5:
			resultEvAcu = 8f / 3f;
			break;
		case 6:
			resultEvAcu = 9f / 3f;
			break;
		}

		return resultEvAcu;
	}

	// -----------------------------
	// Gets the probability of attacking
	// -----------------------------
	public void resolveAttack() {
		Pokemon attacker = this.getPkCombatting();
		Pokemon defender = this.getPkFacing();
		Attack atkAttacker = attacker.getNextMovement();
		Attack atkDefender = defender.getNextMovement();
		boolean isAttackerCharging = attacker.getIsChargingAttackForNextRound();
		boolean isDefenderCharging = defender.getIsChargingAttackForNextRound();

		boolean canHitInvulnerable = atkDefender == null
				|| atkAttacker.getCanHitWhileInvulnerable().contains(atkDefender.getId());

		// 1 - Check if an attack is not disabled (attacks disabled cannot be used, even
		// for charged attacks, they are instantly disabled)
		if (!canUseAttack(attacker, atkAttacker)) {
			return;
		}

		// 2 - Special cases that ignore precision
		if (handleForcedChangeOrIgnorePrecision(attacker, defender, atkAttacker, canHitInvulnerable,
				isDefenderCharging)) {
			return;
		}

		// 3 - Charged attack (first turn)
		if (handleChargeStart(attacker, atkAttacker, isAttackerCharging)) {
			return;
		}

		// Reset CanAttack if doesn't enter in any case
		attacker.denyAttack();

		float accuracyFactor = calculateAccuracyFactor(attacker, defender, atkAttacker);

		resolveAccuracyByContext(attacker, defender, atkAttacker, accuracyFactor, canHitInvulnerable,
				isAttackerCharging, isDefenderCharging);
	}

	// -----------------------------
	// Check if attack is not disabled
	// -----------------------------
	private boolean canUseAttack(Pokemon attacker, Attack atkAttacker) {
		if (isAttackDisabled()) {
			System.out.println(attacker.getName() + " intentó usar " + attacker.getNextMovement().getName()
					+ ", pero está anulado!");
			attacker.denyAttack();
			return false;
		}
		return true;
	}

	// -----------------------------
	// Check for attacks that force changes or has 100% of precision
	// -----------------------------
	private boolean handleForcedChangeOrIgnorePrecision(Pokemon attacker, Pokemon defender, Attack atkAttacker,
			boolean canHitInvulnerable, boolean isDefenderCharging) {
		// 18_Whirlwind / 46_Roar / 54_Mist
		if (atkAttacker.isForceChange() || atkAttacker.getId() == 54) {
			if (isDefenderCharging && !canHitInvulnerable) {
				attacker.denyAttack();
				System.out.println(attacker.getName() + " usó " + atkAttacker.getName() + ", pero " + defender.getName()
						+ " evitó el ataque (invulnerable).");
			} else
				attacker.allowAttack();
			return true;
		}

		if (atkAttacker.alwaysHits()) {
			attacker.allowAttack();
			return true;
		}

		if (atkAttacker.alwaysHeatsUnderWeather(this.getWeather())) {
			attacker.allowAttack();
			return true;
		}

		return false;
	}

	// -----------------------------
	// Check for charged attack
	// -----------------------------
	private boolean handleChargeStart(Pokemon attacker, Attack atkAttacker, boolean isAttackerCharging) {
		if (atkAttacker.getCategory() == AttackCategory.CHARGED && !isAttackerCharging) {
			System.out.println(ANSI_PURPLE + "Probability - Starting a charged attack" + ANSI_RESET);

			attacker.allowAttack();
			System.out.println(ANSI_PURPLE + attacker.getName() + " utilizará " + atkAttacker.getName()
					+ " - comienza a cargar el ataque. (bloc 3)" + ANSI_RESET);
			return true;
		}
		return false;
	}

	// -----------------------------
	// Calculates accuracy (check if doesn't fails)
	// -----------------------------
	private float calculateAccuracyFactor(Pokemon attacker, Pokemon defender, Attack atkAttacker) {
		float accuracyFactor = 0f;

		// Methods to modify precision of attack, evasion, etc.
		checkWeatherEffectsForAttacks(this.getWeather(), atkAttacker);
		attacker.checkStatsForAttacks(atkAttacker);

		if (atkAttacker.isOneHitKO())
			// don't take into account Pokemon levels (cause all are on the same lvl)
			accuracyFactor = 1f;
		// Retreat Pokemon (23_Stomp/ 27_Rolling kick/ 29_Headbutt/ 44_Bite) + defender
		// is minimized
		else if (atkAttacker.hasActiveSecondaryEffect(SecondaryEffectType.FLINCH) && defender.getHasUsedMinimize())
			accuracyFactor = (atkAttacker.getPrecision() / 100f) * (getEvasionOrAccuracy(attacker, 1) / 1f);
		// Other attacks
		else
			accuracyFactor = (atkAttacker.getPrecision() / 100f)
					* (getEvasionOrAccuracy(attacker, 1) / getEvasionOrAccuracy(defender, 2));

		return accuracyFactor;
	}

	// -----------------------------
	// Apply accuracy depending on different cases
	// -----------------------------
	private void resolveAccuracyByContext(Pokemon attacker, Pokemon defender, Attack atkAttacker, float accuracyFactor,
			boolean canHitInvulnerable, boolean isAttackerCharging, boolean isDefenderCharging) {

		if (ApplyNoGuardAbility(attacker, defender))
			return;

		// BLOC 1 : NORMAL ATTACK
		if (atkAttacker.getCategory() == AttackCategory.NORMAL && !isDefenderCharging) {
			handleNormalAccuracyCheck(accuracyFactor, atkAttacker, attacker, defender, "(bloc 1)");
			return;
		}

		// BLOC 2 : NORMAL ATTACK - CAN HIT INVULNERABLE POKEMON
		if (canHitInvulnerable && isDefenderCharging) {
			handleNormalAccuracyCheck(accuracyFactor, atkAttacker, attacker, defender, "(bloc 2)");
			return;
		}

		// BLOC 3 : SECOND TURN (FROM CHARGING ATTACK) - DEFENDER NOT CHARGING
		if (isAttackerCharging && !isDefenderCharging) {
			handleChargedAttackExecution(accuracyFactor, atkAttacker, attacker, defender, "(bloc 3)");
			return;
		}

		// BLOC 4 : SECOND TURN (FROM CHARGING ATTACK) - DEFENDER IS CHARGING
		if (isAttackerCharging && isDefenderCharging) {
			attacker.setIsChargingAttackForNextRound(false);
			return;
		}

		// BLOC 5 : OTHER ATTACK AGAINST INVULNERABLE (CANNOT DO DAMMAGE)
		if (!canHitInvulnerable && isDefenderCharging) {
			attacker.setIsChargingAttackForNextRound(false);
			return;
		}
	}

	// -----------------------------
	// Handle normal accuracy
	// -----------------------------
	private void handleNormalAccuracyCheck(float accuracyFactor, Attack atk, Pokemon attacker, Pokemon defender,
			String code) {

		if (accuracyFactor >= 1f) {
			attacker.allowAttack();
			return;
		}

		int rand = (int) (Math.random() * 100);

		if (rand / 100f <= accuracyFactor)
			attacker.allowAttack();
		else {
			System.out.println("accuracy : " + rand / 100f + "(random) => " + accuracyFactor + " (true accuracy)");
			atk.setPp(atk.getPp() - 1);
			attacker.denyAttack();

			System.out.println(attacker.getName() + " usó " + atk.getName() + ". " + defender.getName()
					+ " evitó el ataque jijijija. " + code);

			// Some attacks that can fail, hurts the attacker (Jump kick, etc.)
			if (attacker.getNextMovement().getCanRecieveDamage()) {
				float attackerInitialPs = attacker.getInitialPs();

				float recoil = attackerInitialPs / 2f;

				attacker.setPs(attacker.getPs() - recoil);

				System.out.println(attacker.getName() + " se dañó a si mismo jajajaji. (Patada salto)");
			}
		}
	}

	// -----------------------------
	// Handle second turn of a charged attack
	// -----------------------------
	private void handleChargedAttackExecution(float accuracyFactor, Attack atk, Pokemon attacker, Pokemon defender,
			String code) {

		if (accuracyFactor >= 1f) {
			attacker.allowAttack();
			return;
		}

		int rand = (int) (Math.random() * 100);

		if (rand / 100f <= accuracyFactor)
			attacker.allowAttack();
		else {
			atk.setPp(atk.getPp() - 1);
			attacker.denyAttack();
			attacker.setIsChargingAttackForNextRound(false);

			System.out.println(attacker.getName() + " usó " + atk.getName() + ". " + defender.getName() + " (Id:"
					+ defender.getId() + ")" + " evitó el ataque jijijija. " + code);
		}
	}

	// -----------------------------
	// Check if 99_No_Guard ability is in game
	// -----------------------------
	private boolean ApplyNoGuardAbility(Pokemon attacker, Pokemon defender) {
		// 99_No_Guard allows to attack every time (whether is the defender or the
		// attacker that has the ability)
		if (attacker.getAbilitySelected().getId() == 99 || defender.getAbilitySelected().getId() == 99) {
			System.out.println(attacker.getName() + " puede atacar gracias a la habilidad Indefenso en juego");
			attacker.allowAttack();
			return true;
		}

		return false;
	}

	// -----------------------------
	// Gets the attack effect and apply damage
	// -----------------------------
	public void doAttackEffect(Weather weather, boolean isMistEffectActivated) {
		Pokemon attacker = this.getPkCombatting();
		Pokemon defender = this.getPkFacing();

		Attack attackAttacker = this.getPkCombatting().getNextMovement();

		float dmg = 0f;
		float dmgToSum = 0f;

		int nbTimesAttack;

		int nbTurnsHoldingStatus;

		float recoil = 0f;

		Ability abilityAttacker = attacker.getAbilitySelected();
		Ability abilityDefender = defender.getAbilitySelected();

		int modifierWeather = 1;

		boolean isWeatherSuppressed = this.getIsWeatherSuppressed();

		// Some abilities allows to not to do damage (ex : Volt absorb)
		if (abilityDefender != null) {
			boolean continueAttack = abilityDefender.getEffect().beforeDamage(null, attacker, defender, attackAttacker);

			if (!continueAttack) {
				attackAttacker.setPp(attackAttacker.getPp() - 1);
				return; // cancel attack
			}
		}

		switch (attackAttacker.getId()) {
		// Destructor/Pound (tested)
		case 1:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Destructor");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Golpe kárate/Karate chop (tested)
		case 2:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Golpe kárate");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Doble bofetón/Double slap (tested)
		case 3:
			// 92_Skill_Link do max of multiple attacks
			if (abilityAttacker.getId() == 92)
				nbTimesAttack = 5;
			else
				nbTimesAttack = getRandomInt(1, 5, null);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Doble bofetón");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Doble bofetón : " + nbTimesAttack);

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmgToSum);
			break;

		// Puño cometa/Comet punch (tested)
		case 4:
			// 92_Skill_Link do max of multiple attacks
			if (abilityAttacker.getId() == 92)
				nbTimesAttack = 5;
			else
				nbTimesAttack = getRandomInt(1, 5, null);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Puño cometa");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Puño cometa : " + nbTimesAttack);

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmgToSum);
			break;

		// Megapuño/Mega punch (tested)
		case 5:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Megapuño");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Día de pago/Pay day (tested)
		case 6:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Día de pago");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Puño fuego/Fire punch (tested)
		case 7:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Puño fuego");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Puño hielo/Ice punch (tested)
		case 8:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Puño hielo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Puño trueno/Thunder punch (tested)
		case 9:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Puño trueno");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Arañazo/Scratch (tested)
		case 10:
			System.out.println(attacker + " (Id:" + attacker.getId() + ")" + " usó Arañazo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Agarre/Vise grip (tested)
		case 11:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Agarre");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Guillotina/Guillotine (tested)
		case 12:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Guillotina");

			// One-Hit KO => Pokemon facing dies instantly (depending on conditions)
			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (abilityDefender.getId() == 5 && !abilityDefender.getAlreadyUsedOnEnter()
					&& (attackAttacker.isOneHitKO())) {
				defender.setPs(1f);
				abilityDefender.setAlreadyUsedOnEnter(true);
				System.out.println(defender.getName() + " (Id:" + defender.getId()
						+ "), se quedó a un PS gracias a la habilidad Robustez");
			} else {
				defender.setPs(0f);
				System.out.println(defender.getName() + " (Id:" + defender.getId()
						+ "), se debilitó de un golpe con el ataque fulminante");
			}

			break;

		// Viento cortante/Razor wind (tested)
		case 13:
			// If not charging => first turn charge the attack
			if (!attacker.getIsChargingAttackForNextRound()) {

				// This attack requires to charge first time for one round
				System.out.println(
						attacker.getName() + " (Id:" + attacker.getId() + ")" + " se prepara para Viento cortante");

				attacker.setIsChargingAttackForNextRound(true);

				// Apply damage => second turn
			} else {

				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Viento cortante");

				dmg = doDammage();

				// Pokemon is no more charging an attack
				attacker.setIsChargingAttackForNextRound(false);

				attackAttacker.setPp(attackAttacker.getPp() - 1);
			}

			defender.setPs(defender.getPs() - dmg);
			break;

		// Danza espada/Swords dance (tested)
		case 14:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Danza espada");

			if (attacker.getAttackStage() >= 6) {
				System.out.println("El ataque de " + attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " no puede subir más!");
			} else {
				attacker.setStageValueStats(StatType.ATTACK, 2, false);
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " aumentó mucho su Ataque!");
			}

			attackAttacker.setPp(attackAttacker.getPp() - 1);
			break;

		// Corte/Cut (tested)
		case 15:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Corte");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Tornado/Gust (tested)
		case 16:
			if (attackAttacker.getCanHitWhileInvulnerable().contains(defender.getNextMovement().getId())) {
				attackAttacker.setPower(attackAttacker.getPower() * 2);
			}

			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Tornado");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Ataque ala/Wing attack (tested)
		case 17:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Ataque ala");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Remolino/Whirlwind
		case 18:
			System.out.println(attacker.getName() + " usó Remolino");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			// 21_Suction_Cups doesn't allow to force change
			if (defender.getAbilitySelected().getId() == 21) {
				System.out
						.println(defender.getName() + " no puede ser forzado por el cambio dada su habilidad Ventosas");
				break;
			}

			// If rival has no more Pokemon => it doesn't matter, but no fail
			if (!this.getDefender().hasAvailableSwitch()) {
				System.out.println("Pero " + this.getPkFacing().getName() + " no tiene más Pokémon para cambiar.");
				break;
			}

			// Force change
			this.getDefender().setForceSwitchPokemon(true);

			System.out.println("¡" + defender.getName() + " fue arrastrado y obligado a retirarse!");
			break;

		// Vuelo/Fly (tested)
		case 19:
			// If not charging => first turn charge the attack
			if (!attacker.getIsChargingAttackForNextRound()) {

				// This attack requires to charge first time for one round
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " voló muy alto");

				attacker.setIsChargingAttackForNextRound(true);

				// Apply damage => second turn
			} else {

				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Vuelo");

				dmg = doDammage();

				// Pokemon is no more charging an attack
				attacker.setIsChargingAttackForNextRound(false);

				attackAttacker.setPp(attackAttacker.getPp() - 1);

				defender.setPs(defender.getPs() - dmg);
			}
			break;

		// Atadura/Bind (tested)
		case 20:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Atadura");

			dmg = doDammage();

			// Check if the Pokemon facing doesn't have the status Trapped (is a status that
			// can be accumulated with other ephemeral status)
			if (!defender.hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {
				nbTurnsHoldingStatus = getRandomInt(4, 5, StatusConditions.TRAPPED);
				System.out.println(this.getPkFacing().getName() + " quedó atrapado");

				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);
				defender.addEphemeralStatus(StatusConditions.TRAPPED, trapped);
			}

			defender.setPs(defender.getPs() - dmg);

			attackAttacker.setPp(attackAttacker.getPp() - 1);
			break;

		// Atizar/Slam (tested)
		case 21:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Atizar");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Látigo cepa/Vine whip (tested)
		case 22:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Látigo cepa");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Pisotón/Stomp (tested)
		case 23:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Pisotón");

			// If Pokemon facing has used minimize, set power base of the attack x2
			if (defender.getHasUsedMinimize()) {
				attackAttacker.setPower(attackAttacker.getPower() * 2);
			}

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Doble patada/Double kick (tested)
		case 24:
			// Attacks 2 times
			nbTimesAttack = 2;

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Doble patada");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmgToSum);
			break;

		// Megapatada/Mega kick (tested)
		case 25:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Megapatada");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Patada salto/Jump kick (tested)
		case 26:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Patada salto");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Patada giro/Rolling kick (tested)
		case 27:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Patada giro");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Ataque arena /Sand attack (tested)
		case 28:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Ataque arena");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (abilityDefender.getId() == 29) {
				System.out.println("Las estats de " + defender.getName() + " (Id:" + defender.getId() + ")"
						+ " no pueden bajar dada su la habilidad " + abilityDefender.getName());
				break;
			}

			if (!isMistEffectActivated) {
				// 35_Illuminate/ 51_Keen_Eye ability
				if (defender.getAbilitySelected().getId() == 35 || defender.getAbilitySelected().getId() == 51) {
					System.out.println("La precisión de " + defender.getName() + " (Id:" + defender.getId() + ")"
							+ " no puede bajar dada su habilidad" + defender.getAbilitySelected().getName());
					break;
				}

				if (defender.getPrecisionStage() <= -6) {
					System.out.println("La precisión de " + defender.getName() + " (Id:" + defender.getId() + ")"
							+ " no puede bajar más!");
				} else {
					defender.setStageValueStats(StatType.PRECISION, 1, true);
					System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " bajó su precisión!");
				}
			} else {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}

			break;

		// Golpe cabeza/Headbutt (tested)
		case 29:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Golpe cabeza");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Cornada/Horn attack (tested)
		case 30:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Cornada");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Ataque furia/Fury attack (tested)
		case 31:
			// 92_Skill_Link do max of multiple attacks
			if (abilityAttacker.getId() == 92)
				nbTimesAttack = 5;
			else
				nbTimesAttack = getRandomInt(1, 5, null);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Ataque furia");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Ataque furia : " + nbTimesAttack);

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmgToSum);
			break;

		// Perforador/Horn drill (tested)
		case 32:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Perforador");

			// One-Hit KO => Pokemon facing dies instantly (depending on conditions)
			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (abilityDefender.getId() == 5 && !abilityDefender.getAlreadyUsedOnEnter()
					&& (attackAttacker.isOneHitKO())) {
				defender.setPs(1f);
				abilityDefender.setAlreadyUsedOnEnter(true);
				System.out.println(defender.getName() + " (Id:" + defender.getId()
						+ "), se quedó a un PS gracias a la habilidad Robustez");
			} else {
				defender.setPs(0f);
				System.out.println(defender.getName() + " (Id:" + defender.getId()
						+ "), se debilitó de un golpe con el ataque fulminante");
			}
			break;

		// Placaje/Tackle (tested)
		case 33:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Placaje");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Golpe cuerpo/Body slam (tested)
		case 34:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Golpe cuerpo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Constricción/Wrap (tested)
		case 35:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Constricción");

			dmg = doDammage();

			// Check if the Pokemon facing doesn't have the status Trapped (is a status that
			// can be accumulated with other ephemeral status)
			if (!defender.hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {

				nbTurnsHoldingStatus = getRandomInt(4, 5, StatusConditions.TRAPPED);
				System.out.println(defender.getName() + " quedó atrapado");

				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);
				defender.addEphemeralStatus(StatusConditions.TRAPPED, trapped);
			}

			defender.setPs(defender.getPs() - dmg);

			attackAttacker.setPp(attackAttacker.getPp() - 1);
			break;

		// Derribo/Take down (tested)
		case 36:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Derribo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);

			// 69_Rock_Head ability is not affected by recoil
			if (attacker.getAbilitySelected().getId() == 69) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId()
						+ ") no sufrió daño por el retroceso gracias a su habilidad "
						+ attacker.getAbilitySelected().getName());
			} else {
				// ---- RECOIL ----
				recoil = dmg * 0.25f;

				attacker.setPs(attacker.getPs() - recoil);

				System.out.println(attacker.getName() + " (Id:" + attacker.getId()
						+ ") se dañó a sí mismo por el retroceso (" + recoil + ")");
			}
			break;

		// Saña/Thrash (tested)
		case 37:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Saña");

			dmg = doDammage();

			if (!attacker.hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)) {

				nbTurnsHoldingStatus = getRandomInt(2, 5, StatusConditions.TRAPPEDBYOWNATTACK);
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " usará el mismo ataque durante " + nbTurnsHoldingStatus + " turnos.");

				State trappedByOwnAttack = new State(StatusConditions.TRAPPEDBYOWNATTACK, nbTurnsHoldingStatus + 1);
				defender.addEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK, trappedByOwnAttack);
				;

				// Only removes PP when choosing the attack
				attackAttacker.setPp(attackAttacker.getPp() - 1);
			}

			defender.setPs(defender.getPs() - dmg);
			break;

		// Doble filo/Dobule-Edge (tested)
		case 38:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Doble filo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);

			// 69_Rock_Head ability is not affected by recoil
			if (attacker.getAbilitySelected().getId() == 69) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId()
						+ ") no sufrió daño por el retroceso gracias a su habilidad "
						+ attacker.getAbilitySelected().getName());
			} else {
				// ---- RECOIL ----
				recoil = dmg * 0.25f;

				attacker.setPs(attacker.getPs() - recoil);

				System.out.println(attacker.getName() + " (Id:" + attacker.getId()
						+ ") se dañó a sí mismo por el retroceso (" + recoil + ")");
			}
			break;

		// Látigo/Tail Whip (tested)
		case 39:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Látigo");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (abilityDefender.getId() == 29) {
				System.out.println("Las estats de " + defender.getName() + " (Id:" + defender.getId() + ")"
						+ " no pueden bajar dada su la habilidad " + abilityDefender.getName());
				break;
			}

			if (!isMistEffectActivated) {
				if (defender.getDefenseStage() <= -6) {
					System.out.println("La defensa de " + defender.getName() + " (Id:" + defender.getId() + ")"
							+ " no puede bajar más!");
				} else {
					defender.setStageValueStats(StatType.DEFENSE, 1, true);
					System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " bajó su defensa!");
				}
			} else {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}

			break;

		// Picotazo veneno/Poison sting (tested)
		case 40:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Picotazo veneno");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);

			break;

		// Doble ataque/Twineedle (tested)
		case 41:
			// Attacks 2 times
			nbTimesAttack = 2;

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Doble ataque");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmgToSum);

			break;

		// Pin misil/Pin missile (tested)
		case 42:
			// 92_Skill_Link do max of multiple attacks
			if (abilityAttacker.getId() == 92)
				nbTimesAttack = 5;
			else
				nbTimesAttack = getRandomInt(1, 5, null);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Pin misil");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Pin misil : " + nbTimesAttack);

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmgToSum);
			break;

		// Malicioso/Leer (tested)
		case 43:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Malicioso");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (abilityDefender.getId() == 29) {
				System.out.println("Las estats de " + defender.getName() + " (Id:" + defender.getId() + ")"
						+ " no pueden bajar dada su la habilidad " + abilityDefender.getName());
				break;
			}

			if (!isMistEffectActivated) {
				if (defender.getDefenseStage() <= -6) {
					System.out.println("La defensa de " + defender.getName() + " (Id:" + defender.getId() + ")"
							+ " no puede bajar más!");
				} else {
					defender.setStageValueStats(StatType.DEFENSE, 1, true);
					System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " bajó su defensa!");
				}
			} else {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}
			break;

		// Mordisco/Bite (tested)
		case 44:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Mordisco");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Gruñido/Growl (tested)
		case 45:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Gruñido");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (abilityDefender.getId() == 29) {
				System.out.println("Las estats de " + defender.getName() + " (Id:" + defender.getId() + ")"
						+ " no pueden bajar dada su la habilidad " + abilityDefender.getName());
				break;
			}

			// 52_Hyper_Cutter ability
			if (abilityDefender.getId() == 52) {
				System.out.println("El ataque de " + defender.getName() + " (Id:" + defender.getId() + ")"
						+ " no puede bajar dada su " + abilityDefender.getName());
				break;
			}

			if (!isMistEffectActivated) {
				if (defender.getAttackStage() <= -6) {
					System.out.println("El ataque de " + defender.getName() + " (Id:" + defender.getId() + ")"
							+ " no puede bajar más!");
				} else {
					defender.setStageValueStats(StatType.ATTACK, 1, true);
					System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " bajó su ataque!");
				}
			} else {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}
			break;

		// Rugido/Roar (tested)
		case 46:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Rugido");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			// 21_Suction_Cups doesn't allow to force change
			if (defender.getAbilitySelected().getId() == 21) {
				System.out
						.println(defender.getName() + " no puede ser forzado por el cambio dada su habilidad Ventosas");
				break;
			}

			// If rival has no more Pokemon remaining, it doesn't matter => only fails the
			// attack
			if (!this.getDefender().hasAvailableSwitch()) {
				System.out.println("Pero " + this.getPkFacing().getName() + " no tiene más Pokémon para cambiar.");
				break;
			}

			// Force change
			this.getDefender().setForceSwitchPokemon(true);

			System.out.println("¡" + defender.getName() + " fue arrastrado y obligado a retirarse!");
			break;

		// Canto/Sing (tested)
		case 47:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Canto");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (!defender.trySetEphemeralStatus(StatusConditions.ASLEEP, attackAttacker))
				break;

			// Check if the Pokemon facing doesn't have the status Asleep (is a status that
			// can be accumulated with other ephemeral status)
			if (!defender.hasActiveEphemeralStatus(StatusConditions.ASLEEP)) {
				nbTurnsHoldingStatus = getRandomInt(1, 7, StatusConditions.ASLEEP);

				System.out.println(
						defender.getName() + " cayó en un sueño profundo por " + nbTurnsHoldingStatus + " turnos");

				State asleep = new State(StatusConditions.ASLEEP, nbTurnsHoldingStatus + 1);
				defender.addEphemeralStatus(StatusConditions.ASLEEP, asleep);
			} else {
				System.out.println(defender.getName() + " ya está dormido!");
			}
			break;

		// Supersónico/Supersonic (tested)
		case 48:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Supersónico");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (!defender.trySetEphemeralStatus(StatusConditions.CONFUSED, attackAttacker))
				break;

			// Check if the Pokemon facing doesn't have the status Confused (is a status
			// that
			// can be accumulated with other ephemeral status)
			if (!defender.hasActiveEphemeralStatus(StatusConditions.CONFUSED)) {

				nbTurnsHoldingStatus = getRandomInt(1, 7, StatusConditions.CONFUSED);

				System.out.println(defender.getName() + " está confuso por " + nbTurnsHoldingStatus + " turnos");

				State confused = new State(StatusConditions.CONFUSED, nbTurnsHoldingStatus + 1);
				defender.addEphemeralStatus(StatusConditions.CONFUSED, confused);
			} else
				System.out.println(defender.getName() + " ya está confuso!");
			break;

		// Bomba sónica/Sonic boom (tested)
		case 49:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Bomba sónica");

			dmg = 20f;

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Anulación/Disable (tested)
		case 50:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Anulación");

			Attack disable = attacker.getNextMovement();
			Attack lastAttack = defender.getLastUsedAttack();

			disable.setPp(disable.getPp() - 1);

			// If rival hasn't used yet an attack => fails
			if (lastAttack == null || lastAttack.getId() == 0) {
				System.out.println("¡Pero no surtió efecto!");
				break;
			}

			if (defender.hasActiveStatusCondition(StatusConditions.DISABLE))
				defender.setStatusCondition(new State());

			nbTurnsHoldingStatus = getRandomInt(4, 7, null);

			State attackDisabled = new State(StatusConditions.DISABLE, nbTurnsHoldingStatus + 1);
			attackDisabled.setAttackDisabled(lastAttack);
			defender.setStatusCondition(attackDisabled);

			System.out.println(defender.getName() + " no podrá usar " + lastAttack.getName() + " por "
					+ nbTurnsHoldingStatus + " turnos");
			break;

		// Acido/Acid (tested)
		case 51:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Ácido");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Ascuas/Ember (tested)
		case 52:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Ascuas");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Lanzallamas/FlameThrower (tested)
		case 53:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Lanzallamas");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Neblina/Mist (tested)
		case 54:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Neblina");

			if (isMistEffectActivated) {
				System.out.println("No tuvo ningún efecto ya que está en uso");
			}

			attackAttacker.setPp(attackAttacker.getPp() - 1);
			break;

		// Pistola agua/Water gun (tested)
		case 55:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Pistola agua");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Hidrobomba/Hydro Pump (tested)
		case 56:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Hidrobomba");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Surf/Surf (tested)
		case 57:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Surf");

			// If Pokemon facing has used Dive, set power base of the attack x2
			if (defender.getIsChargingAttackForNextRound() && defender.getNextMovement().getId() == 291) {
				attackAttacker.setPower(attackAttacker.getPower() * 2);
			}

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Rayo hielo/Ice beam (tested)
		case 58:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Rayo hielo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Ventisca/Blizzard (tested)
		case 59:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Ventisca");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Psicorrayo/Psybeam (tested)
		case 60:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Psicorrayo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Rayo burbuja/Bubble beam (tested)
		case 61:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Rayo burbuja");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Rayo aurora/Aurora beam (tested)
		case 62:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Rayo aurora");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Hiperrayo/Hyper beam (tested)
		case 63:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Hiperrayo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);

			// Pokemon combating cannot do anything next round
			attacker.setCanDonAnythingNextRound(false);
			break;

		// Picotazo/Peck (tested)
		case 64:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Picotazo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Pico taladro/Drill peck (tested)
		case 65:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Pico taladro");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Sumisión/Submission (tested)
		case 66:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Sumisión");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);

			// 69_Rock_Head ability is not affected by recoil
			if (attacker.getAbilitySelected().getId() == 69) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId()
						+ ") no sufrió daño por el retroceso gracias a su habilidad "
						+ attacker.getAbilitySelected().getName());
			} else {
				// ---- RECOIL ----
				recoil = dmg * 0.25f;

				attacker.setPs(attacker.getPs() - recoil);

				System.out.println(attacker.getName() + " (Id:" + attacker.getId()
						+ ") se dañó a sí mismo por el retroceso (" + recoil + ")");
			}
			break;

		// Patada baja/Low kick (tested)
		case 67:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Patada baja");

			// Set power of the attack depending on the weight of the Pokemon facing
			if (defender.getWeight() < 10) {
				attackAttacker.setPower(20);
			} else if (defender.getWeight() >= 10 && defender.getWeight() < 25) {
				attackAttacker.setPower(40);
			} else if (defender.getWeight() >= 25 && defender.getWeight() < 50) {
				attackAttacker.setPower(60);
			} else if (defender.getWeight() >= 50 && defender.getWeight() < 100) {
				attackAttacker.setPower(80);
			} else if (defender.getWeight() >= 100 && defender.getWeight() < 200) {
				attackAttacker.setPower(100);
			} else {
				attackAttacker.setPower(120);
			}

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Contraataque/Counter (tested)
		case 68:
			if (attacker.getHasReceivedDamage()) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Contraataque");

				dmg = attacker.getDamageReceived() * 2f;

				attackAttacker.setPp(attackAttacker.getPp() - 1);

				defender.setPs(defender.getPs() - dmg);

				System.out.println("Damage to Pokemon facing (" + defender.getName() + " (Id:" + defender.getId() + ")"
						+ ") : " + dmg);
			} else {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " no puede usar Contraataque ya que no recibió ningún ataque físico este turno");
			}
			break;

		// Sísmico/Seismic toss (tested)
		case 69:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Sísmico");

			// This attack only apply the same amount on damage points as the level of the
			// pokemon using it (all the Pokemon in the game are on the level 100)
			dmg = 100f;

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);

			System.out.println("Damage to Pokemon facing (" + defender.getName() + " (Id:" + defender.getId() + ")"
					+ ") : " + dmg);
			break;

		// Fuerza/Strength (tested)
		case 70:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Fuerza");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Absorber/Absorb (tested)
		case 71:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Absorber");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);

			// Pokemon combating gets or loses health
			if (defender.getAbilitySelected().getId() == 64) {
				// The half of damage done
				attacker.setPs(attacker.getPs() - (dmg / 2f));
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " perdió PS al intentar drenar al rival dada la habilidad Viscosecreción");

			} else {
				if (attacker.getPs() != attacker.getInitialPs()) {

					// The half of damage done
					attacker.setPs(attacker.getPs() + (dmg / 2f));

					// If more PS received than initial PS, put the max limit at initial PS
					if (attacker.getPs() >= attacker.getInitialPs()) {
						attacker.setPs(attacker.getInitialPs());
					}
				}
			}
			break;

		// Megaagotar/Mega drain (tested)
		case 72:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Megaagotar");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);

			// Pokemon combating gets or loses health
			if (defender.getAbilitySelected().getId() == 64) {
				// The half of damage done
				attacker.setPs(attacker.getPs() - (dmg / 2f));
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " perdió PS al intentar drenar al rival dada su habilidad Viscosecreción");

			} else {
				if (attacker.getPs() != attacker.getInitialPs()) {

					// The half of damage done
					attacker.setPs(attacker.getPs() + (dmg / 2f));

					// If more PS received than initial PS, put the max limit at initial PS
					if (attacker.getPs() >= attacker.getInitialPs()) {
						attacker.setPs(attacker.getInitialPs());
					}
				}
			}
			break;

		// Drenadoras/Leech seed (tested)
		case 73:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Drenadoras");

			// Doesn't affect to grass type
			if (!defender.getTypes().stream().filter(t -> t.getId() == 12).findAny().isPresent()) {

				attacker.setIsDraining(true);

				if (!defender.hasActiveEphemeralStatus(StatusConditions.DRAINEDALLTURNS)) {
					System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " fue drenado");

					State drainedAllTurns = new State(StatusConditions.DRAINEDALLTURNS, 0);
					this.getPkFacing().addEphemeralStatus(StatusConditions.DRAINEDALLTURNS, drainedAllTurns);
				} else
					System.out.println(defender.getName() + " ya está drenado");
			} else
				System.out.println(defender.getName() + " no puede estar drenado ya que es de tipo planta");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			break;

		// Desarrollo/Growth (tested)
		case 74:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Desarrollo");

			// Add +2 if adequate weather (and it's not suppressed)
			if (weather == Weather.SUN && !isWeatherSuppressed) {
				modifierWeather = 2;
			}

			// Normal attack
			if (attacker.getAttackStage() >= 6) {
				System.out.println("El ataque de " + attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " no puede subir más!");
			} else {
				attacker.setStageValueStats(StatType.ATTACK, modifierWeather, false);
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " aumentó su Ataque!");
			}

			// Special attack
			if (attacker.getSpecialAttackStage() >= 6) {
				System.out.println("El ataque especial de " + attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " no puede subir más!");
			} else {
				attacker.setStageValueStats(StatType.SPECIAL_ATTACK, modifierWeather, false);
				System.out.println(
						attacker.getName() + " (Id:" + attacker.getId() + ")" + " aumentó su Ataque especial!");
			}

			attackAttacker.setPp(attackAttacker.getPp() - 1);
			break;

		// Hoja afilada/Razor leaf (tested)
		case 75:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó hoja afilada");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Rayo solar/Solar beam(tested)
		case 76:
			if (weather == Weather.SUN && !isWeatherSuppressed) {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Rayo solar");

				dmg = doDammage();

				// Pokemon is no more charging an attack
				attacker.setIsChargingAttackForNextRound(false);

				attackAttacker.setPp(attackAttacker.getPp() - 1);

				defender.setPs(defender.getPs() - dmg);
			} else {
				// If not charging => first turn charge the attack
				if (!attacker.getIsChargingAttackForNextRound()) {

					// This attack requires to charge first time for one round
					System.out.println(
							attacker.getName() + " (Id:" + attacker.getId() + ")" + " se prepara para Rayo solar");

					attacker.setIsChargingAttackForNextRound(true);

					// Apply damage => second turn
				} else {

					System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Rayo solar");

					// Depending on weather it has less power
					if (!isWeatherSuppressed
							&& (weather == Weather.RAIN || weather == Weather.HAIL || weather == Weather.SANDSTORM)) {
						attackAttacker.setPower(attackAttacker.getPower() / 2);
					}

					dmg = doDammage();

					// Pokemon is no more charging an attack
					attacker.setIsChargingAttackForNextRound(false);

					attackAttacker.setPp(attackAttacker.getPp() - 1);

					defender.setPs(defender.getPs() - dmg);
				}
			}
			break;

		// Polvo veneno/Poison powder (tested)
		case 77:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Polvo veneno");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (!defender.trySetEphemeralStatus(StatusConditions.POISONED, attackAttacker)) {
				break;
			}

			// Possibility of poisoning Pokemon facing if is not already poisoned and has
			// not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

				State poisoned = new State(StatusConditions.POISONED);

				defender.setStatusCondition(poisoned);

				System.out.println(defender.getName() + " fue envenenado");
			} else {
				System.out.println(defender.getName() + " ya está envenenado");
			}
			break;

		// Paralizador/Stun spore (tested)
		case 78:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Paralizador");

			// Possibility of paralyzing the Pokemon facing if is not already pralyzed and
			// has not a Status
			if (defender.getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {
				defender.trySetStatus(new State(StatusConditions.PARALYZED), weather, isWeatherSuppressed,
						attackAttacker);
			} else {
				System.out.println(defender.getName() + " ya está paralizado");
			}

			attackAttacker.setPp(attackAttacker.getPp() - 1);
			break;

		// Somnífero/Sleep powder (tested)
		case 79:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Somnífero");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (!defender.trySetEphemeralStatus(StatusConditions.ASLEEP, attackAttacker))
				break;

			// Check if the Pokemon facing doesn't have the status Asleep (is a status that
			// can be accumulated with other ephemeral status)
			if (!defender.hasActiveEphemeralStatus(StatusConditions.ASLEEP)) {
				nbTurnsHoldingStatus = getRandomInt(1, 7, StatusConditions.ASLEEP);

				System.out.println(
						defender.getName() + " cayó en un sueño profundo por " + nbTurnsHoldingStatus + " turnos");

				State asleep = new State(StatusConditions.ASLEEP, nbTurnsHoldingStatus + 1);
				defender.addEphemeralStatus(StatusConditions.ASLEEP, asleep);
			} else
				System.out.println(defender.getName() + " ya está dormido!");
			break;

		// Danza pétalo/Petal dance (tested)
		case 80:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Danza pétalo");

			dmg = doDammage();

			if (!attacker.hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)) {
				nbTurnsHoldingStatus = getRandomInt(2, 5, StatusConditions.TRAPPEDBYOWNATTACK);

				System.out.println(
						attacker.getName() + " usará el mismo ataque durante " + nbTurnsHoldingStatus + " turnos.");

				State trappedByOwnAttack = new State(StatusConditions.TRAPPEDBYOWNATTACK, nbTurnsHoldingStatus + 1);
				defender.addEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK, trappedByOwnAttack);
			}

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Disparo démora/String shot (tested)
		case 81:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Diapro démora");

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			if (abilityDefender.getId() == 29) {
				System.out.println("Las estats de " + defender.getName() + " (Id:" + defender.getId() + ")"
						+ " no pueden bajar dada su la habilidad " + abilityDefender.getName());
				break;
			}

			if (!isMistEffectActivated) {
				if (defender.getSpeedStage() <= -6) {
					System.out.println("La velocidad de " + defender.getName() + " (Id:" + defender.getId() + ")"
							+ " no puede bajar más!");
				} else {
					defender.setStageValueStats(StatType.SPEED, 1, false);
					System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " bajó su velocidad!");
				}
			} else {
				System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}
			break;

		// Furia dragón/Dragon rage (tested)
		case 82:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Furia dragón");

			dmg = 40f;

			defender.setPs(defender.getPs() - dmg);

			attackAttacker.setPp(attackAttacker.getPp() - 1);
			break;

		// Giro fuego/Fire spin (tested)
		case 83:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Giro fuego");

			dmg = doDammage();

			// Check if the Pokemon facing doesn't have the status Trapped (is a status that
			// can be accumulated with other ephemeral status)
			if (!defender.hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {
				nbTurnsHoldingStatus = getRandomInt(4, 5, StatusConditions.TRAPPED);

				System.out.println(defender.getName() + " quedó atrapado");

				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);
				defender.addEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK, trapped);
			}

			defender.setPs(defender.getPs() - dmg);

			attackAttacker.setPp(attackAttacker.getPp() - 1);
			break;

		// Impactrueno/Thunder shock (tested)
		case 84:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Impactrueno");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Rayo/Thunderbolt (tested)
		case 85:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Rayo");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Onda trueno/Thunder wave (tested)
		case 86:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Onda trueno");

			// Possibility of paralyzing the Pokemon facing if is not already pralyzed and
			// has not a Status
			if (defender.getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {
				defender.trySetStatus(new State(StatusConditions.PARALYZED), weather, isWeatherSuppressed,
						attackAttacker);
			} else {
				System.out.println(defender.getName() + " ya está paralizado");
			}

			attackAttacker.setPp(attackAttacker.getPp() - 1);
			break;

		// Trueno/Thunder (tested)
		case 87:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Trueno");

			dmg = doDammage();

			attackAttacker.setPp(attackAttacker.getPp() - 1);

			defender.setPs(defender.getPs() - dmg);
			break;

		// Forcejeo/Struggle
		case 165:
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Forcejeo");

			dmg = doDammage();

			defender.setPs(defender.getPs() - dmg);

			// Pokemon combating receives 25% of damage from his initial PS
			attacker.setPs(attacker.getInitialPs() - (attacker.getInitialPs() * 0.25f));
			break;
		}

		if (dmg != 0f || dmgToSum != 0f) {
			// Set damage from physical attack => used for attacks like "Counter", etc.
			if (dmg != 0f && attacker.getPhysicalAttacks() != null
					&& attacker.getPhysicalAttacks().stream().anyMatch(a -> a.getId() == attackAttacker.getId()))
				defender.setDamageReceived(dmg);
			else if (attacker.getPhysicalAttacks() != null
					&& attacker.getPhysicalAttacks().stream().anyMatch(a -> a.getId() == attackAttacker.getId()))
				defender.setDamageReceived(dmgToSum);

			defender.setHasReceivedDamage(true);

			// Apply secondary effects (status conditions, ephemeral status, flinch, reduce
			// stats...)
			applySecondaryEffects(attackAttacker, attacker, defender, weather, isWeatherSuppressed,
					dmg != 0f ? dmg : dmgToSum, isMistEffectActivated);
		}

		reinitializeAttackStats(attackAttacker);
		attacker.reinitializeStatsAfterAttack();

		// Apply abilities after attacking
		applyAbilityAfterDamage(attacker, defender, attackAttacker, dmg, weather, isWeatherSuppressed);

		if (defender.getPs() <= 0)
			defender.setStatusCondition(new State(StatusConditions.DEBILITATED));

		// Get status debilitated for Pokemon combating
		if (attacker.getPs() <= 0)
			attacker.setStatusCondition(new State(StatusConditions.DEBILITATED));

		this.setIsACriticAttack(false);
	}

	// -----------------------------
	// Apply damage
	// -----------------------------
	public float doDammage() {
		Pokemon attacker = this.getPkCombatting();
		Pokemon defender = this.getPkFacing();
		Attack attack = attacker.getNextMovement();

		float modifiedPower = calculateModifiedPower(attacker, defender, attack);

		float baseDamage = calculateBaseDamage(attacker, defender, attack, modifiedPower);
		float damageAfterCrit = applyCriticalIfNeeded(attacker, attack, baseDamage);
		float finalDamage = applyDefensiveAbilities(defender, attacker, attack, damageAfterCrit);

		System.out.println("Damage to " + defender.getName() + " : " + finalDamage);

		return finalDamage;
	}

	// -----------------------------
	// Modify power depending on abilities from attacker
	// -----------------------------
	private float calculateModifiedPower(Pokemon attacker, Pokemon defender, Attack attack) {
		float power = attack.getPower();
		Ability ability = attacker.getAbilitySelected();

		power *= applyPhysicalAbilities(ability, attack);
		power *= applyBoostAbilitiesFromReducedPS(attacker, ability, attack);
		power *= applyRivalry(attacker, defender, ability);
		power *= applyIronFist(ability, attack);
		power *= applyAdaptable(attacker, ability, attack);

		return power;
	}

	// -----------------------------
	// Apply physical abilities (physical attacks)
	// -----------------------------
	private float applyPhysicalAbilities(Ability ability, Attack attack) {
		// 37_Huge_Power/ 74_Pure_Power
		if ((ability.getId() == 37 || ability.getId() == 74) && attack.getBases().contains("fisico"))
			return 2f;

		return 1f;
	}

	// -----------------------------
	// Apply booster abilities (activated when some remaining PS)
	// -----------------------------
	private float applyBoostAbilitiesFromReducedPS(Pokemon attacker, Ability ability, Attack attack) {
		if (attacker.getPs() > attacker.getInitialPs() / 3)
			return 1f;

		int abilityId = ability.getId();
		int attackType = attack.getStrTypeToPkType().getId();

		if ((abilityId == 65 && attackType == 12) || // 65_Overgrow (grass)
				(abilityId == 66 && attackType == 7) || // 66_Blaze (fire)
				(abilityId == 67 && attackType == 2) || // 67_Torrent (water)
				(abilityId == 68 && attackType == 3)) { // 68_Swarm (bug)

			System.out.println(attacker.getName() + " potenciado por habilidad " + ability.getName());
			return 1.5f;
		}

		return 1f;
	}

	// -----------------------------
	// Apply 79_Rivalry ability
	// -----------------------------
	private float applyRivalry(Pokemon attacker, Pokemon defender, Ability ability) {
		if (ability.getId() != 79)
			return 1f;

		if (attacker.getSex() == defender.getSex())
			return 1.25f;
		else
			return 0.75f;
	}

	// -----------------------------
	// Apply 89_Iron_Fist ability
	// -----------------------------
	private float applyIronFist(Ability ability, Attack attack) {
		if (ability.getId() != 89)
			return 1f;

		if (attack.isPunchMove())
			return 1.2f;

		return 1f;
	}

	// -----------------------------
	// Apply 91_Adaptable ability
	// -----------------------------
	private float applyAdaptable(Pokemon attacker, Ability ability, Attack attack) {
		if (ability.getId() == 91 && attacker.getTypes().contains(attack.getStrTypeToPkType()))
			return 1.75f;

		return 1f;
	}

	// -----------------------------
	// Calculate base damage
	// -----------------------------
	private float calculateBaseDamage(Pokemon attacker, Pokemon defender, Attack attack, float power) {
		boolean isSpecial = attack.getBases().contains("especial");
		float randomVariation = (float) (85 + Math.random() * 15);
		float weatherModifier = getWeatherModifier(attack);

		float attackStat = isSpecial ? attacker.getEffectiveSpecialAttack() : attacker.getEffectiveAttack();

		float defenseStat = isSpecial ? defender.getEffectiveSpecialDefense() : defender.getEffectiveDefense();

		float base = (((0.2f * 100f + 1f) * attackStat * power) / (25f * defenseStat) + 2f);

		float damage = 0.01f * attack.getBonus() * attack.getEffectivenessAgainstPkFacing() * weatherModifier
				* randomVariation * base;

		// 18_Flash_Fire boost ability
		if (attacker.getIsFireBoostActive() && attack.getStrTypeToPkType().getId() == 7)
			damage *= 1.5f;

		return damage;
	}

	// -----------------------------
	// Apply critical damage by probabilities
	// -----------------------------
	private float applyCriticalIfNeeded(Pokemon attacker, Attack attack, float damage) {
		Ability ability = attacker.getAbilitySelected();
		boolean isCrit;

		if (attack.getId() == 13)
			isCrit = getHighCriticity30();
		else if (attack.getId() == 2 || attack.getId() == 75)
			isCrit = getHighCriticity40();
		else
			isCrit = getCriticity();

		if (!isCrit)
			return damage;

		if (ability.getId() == 97) // 97_Sniper ability does *3 damage
			return damage * 3f;

		return damage * 2f;
	}

	// -----------------------------
	// Apply defensive abilities
	// -----------------------------
	private float applyDefensiveAbilities(Pokemon defender, Pokemon attacker, Attack attack, float damage) {
		Ability defAbility = defender.getAbilitySelected();
		int abilityId = defAbility.getId();
		int attackTypeId = attack.getStrTypeToPkType().getId();

		boolean isFire = attackTypeId == 7;
		boolean isIce = attackTypeId == 9;

		// 5_Sturdy ability cannot be defeated by one hit KO or by one attack if PS are
		// on max
		if (abilityId == 5 && !defAbility.getAlreadyUsedOnEnter() && defender.getInitialPs() == defender.getPs()
				&& damage >= defender.getPs()) {
			defAbility.setAlreadyUsedOnEnter(true);
			return defender.getInitialPs() - 1f;
		}

		// 47_Thick_Fat ability/ 85_Heatproof reduces damage by 2 (only if attack type
		// it's fire or ice type)
		if ((abilityId == 47 && (isFire || isIce)) || (abilityId == 85 && isFire))
			return damage / 2f;

		// 87_Dry_Skin ability with a fire attack, do 25% more damage
		if (abilityId == 87 && attack.getStrTypeToPkType().getId() == 7)
			return damage * 1.25f;

		return damage;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage) => 10% of probabilities
	// -----------------------------
	public boolean getCriticity() {
		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 10) {
			this.setIsACriticAttack(true);
			return this.canReceiveCriticalAttacks();
		}

		return false;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage) => 30% of probabilities
	// -----------------------------
	public boolean getHighCriticity30() {
		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 30) {
			this.setIsACriticAttack(true);
			return this.canReceiveCriticalAttacks();
		}

		return false;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage) => 40% of probabilities
	// -----------------------------
	public boolean getHighCriticity40() {
		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 40) {
			this.setIsACriticAttack(true);
			return this.canReceiveCriticalAttacks();
		}

		return false;
	}

	// -----------------------------
	// Check if defender can receive critical attacks
	// -----------------------------
	public boolean canReceiveCriticalAttacks() {
		// 4_Battle_Armor / 75_Shell_Armor cannot recieve critic damage because of
		// ability
		if (this.getPkFacing().getAbilitySelected().getId() == 4
				|| this.getPkFacing().getAbilitySelected().getId() == 75) {
			System.out.println(this.getPkFacing().getName() + " no puede recibir ataques críticos dada su habilidad "
					+ this.getPkFacing().getAbilitySelected().getName());
			return false;
		}
		return true;
	}

	// -----------------------------
	// Gets number of turns for a state or number of attacks
	// -----------------------------
	public int getRandomInt(int min, int max, StatusConditions status) {
		int nbTurnsHoldingStatus = min + (int) (Math.random() * (max - min + 1));

		// 48_Early_Bird ability
		if (status == StatusConditions.ASLEEP && this.getPkFacing().getAbilitySelected().getId() == 48) {
			nbTurnsHoldingStatus = nbTurnsHoldingStatus / 2;
			System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId()
					+ "), se quedará dormido la mitad de turnos gracias a su habilidad Madrugar");
		}

		return nbTurnsHoldingStatus;
	}

	// -----------------------------
	// Gets if last attack from Pokemon combating used is disabled
	// -----------------------------
	public boolean isAttackDisabled() {
		Pokemon attacker = this.getPkCombatting();
		if (this.getPkCombatting().hasActiveStatusCondition(StatusConditions.DISABLE)) {
			State disableStatus = attacker.getStatusCondition();

			if (disableStatus.getAttackDisabled() == attacker.getNextMovement())
				return true;
		}
		return false;
	}

	// -----------------------------
	// Adds multiplier depending on weather of the game
	// -----------------------------
	public float getWeatherModifier(Attack attack) {

		Weather weather = this.getWeather();
		boolean isWeatherSuppresed = this.getIsWeatherSuppressed();

		if (isWeatherSuppresed)
			return 1.0f;

		if (weather == Weather.RAIN) {
			if (attack.getStrTypeToPkType().getId() == 2) // Water
				return 1.5f;
			if (attack.getStrTypeToPkType().getId() == 7) // Fire
				return 0.5f;
		}

		if (weather == Weather.SUN) {
			if (attack.getStrTypeToPkType().getId() == 7) // Fire
				return 1.5f;
			if (attack.getStrTypeToPkType().getId() == 2) // Water
				return 0.5f;
		}

		return 1.0f;
	}

	// -----------------------------
	// Change attacks depending on weather
	// -----------------------------
	private void checkWeatherEffectsForAttacks(Weather weather, Attack attack) {
		if (weather == Weather.SUN)
			if (attack.getId() == 87)
				attack.setPrecision(50);
	}

	// -----------------------------
	// Reset parameters from attacks (to avoid problems each turn)
	// -----------------------------
	private void reinitializeAttackStats(Attack attack) {
		attack.setPrecision(attack.getInitialPrecision());
		attack.setPower(attack.getInitialPower());
	}

	// -----------------------------
	// Do ability effect after attacking (for defender)
	// -----------------------------
	private void applyAbilityAfterDamage(Pokemon attacker, Pokemon defender, Attack attack, float dmg, Weather weather,
			boolean isWeatherSuppressed) {

		// Attacker ability
		Ability attackerAbility = attacker.getAbilitySelected();

		// 54_Truant ability (can't do anything next round)
		if (attackerAbility != null && attackerAbility.getId() == 54) {
			System.out.println(attacker.getName() + " (" + attacker.getId() + ") "
					+ "no popdrá atacar o cambiarse en el siguiente turno a causa de "
					+ attacker.getAbilitySelected().getName());
			attacker.setCanDonAnythingNextRound(false);
		}

		// Damage must be done
		if (dmg <= 0)
			return;

		// Defender ability
		Ability defenderAbility = defender.getAbilitySelected();
		if (defenderAbility != null) {
			defenderAbility.getEffect().afterAttack(null, attacker, defender, attack, dmg, 0d,
					this.getIsACriticAttack(), weather, isWeatherSuppressed);
		}
	}

	// -----------------------------
	// Do secondary effects from attacks (set status conditions, flinch, etc)
	// -----------------------------
	private void applySecondaryEffects(Attack attack, Pokemon attacker, Pokemon defender, Weather weather,
			boolean isWeatherSuppressed, float damage, boolean isMistEffectActivated) {
		Ability abilityAttacker = attacker.getAbilitySelected();

		double probabilityGettingStatus = Math.random();
		int nbTurnsHoldingStatus;

		if (attack.getSecondaryEffects() == null)
			return;

		for (SecondaryEffect effect : attack.getSecondaryEffects()) {
			double finalProbability = getFinalSecondaryEffectProbability(effect, attacker);

			if (probabilityGettingStatus > finalProbability)
				continue;

			switch (effect.getType()) {
			case STATUS_CONDITION:
				defender.trySetStatus(new State(effect.getStatus()), weather, isWeatherSuppressed, attack);
				break;
			case EPHEMERAL_STATUS:
				StatusConditions status = effect.getStatus();

				if (!defender.trySetEphemeralStatus(status, attack))
					break;

				if (!defender.hasActiveEphemeralStatus(status)) {

					nbTurnsHoldingStatus = getRandomInt(1, 7, status);
					State state = new State(status, nbTurnsHoldingStatus + 1);

					defender.addEphemeralStatus(status, state);

					System.out.println(defender.getName() + " estará " + status.getMessage() + " por "
							+ nbTurnsHoldingStatus + " turnos");
				}
				break;
			case FLINCH:
				if (!defender.canBeFlinched())
					break;

				if (abilityAttacker != null && abilityAttacker.getId() == 1) {
					abilityAttacker.getEffect().afterAttack(null, attacker, defender, attack, damage,
							effect.getProbability(), this.getIsACriticAttack(), weather, isWeatherSuppressed);
				} else
					defender.setHasRetreated(true);
				break;
			case STAT_DROP:
				defender.modifyStatStage(effect.getStat(), effect.getStages(), isMistEffectActivated);
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
}