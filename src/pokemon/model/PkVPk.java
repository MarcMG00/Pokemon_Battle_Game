package pokemon.model;

import pokemon.enums.AttackCategory;
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

	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	private static final String ANSI_RESET = "\u001B[0m";

	// ==================================== CONSTRCUTORS
	// ====================================

	public PkVPk(Player attacker, Player defender, Weather weather) {
		this.attacker = attacker;
		this.defender = defender;
		this.pkCombatting = attacker.getPkCombatting();
		this.pkFacing = defender.getPkCombatting();
		this.weather = weather;
	}

	public PkVPk() {
		this.attacker = new Player();
		this.defender = new Player();
		this.pkCombatting = new Pokemon();
		this.pkFacing = new Pokemon();
		this.weather = Weather.NONE;
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
			evAcu = pk.getPrecisionPoints();
			break;
		case 2:
			evAcu = pk.getEvasionPoints();
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
	public void getProbabilityOfAttacking(Weather weather) {

		boolean isAttackerCharging = this.getPkCombatting().getIsChargingAttackForNextRound();
		boolean isDefenderCharging = this.getPkFacing().getIsChargingAttackForNextRound();

		Attack atkAttacker = this.getPkCombatting().getNextMovement();
		Attack atkDefender = this.getPkFacing().getNextMovement();

		boolean canHitInvulnerable = atkAttacker.getCanHitWhileInvulnerable().contains(atkDefender.getId());

		float accuracyFactor = 0f;

		checkAbilitiesEffectsForAttacks(weather, atkAttacker);

		// -----------------------------
		// Check if an attack is not disabled (attacks disabled cannot be used, even for
		// charged attacks, they are instantly disabled)
		// -----------------------------
		if (isAttackDisabled(this.getPkCombatting(), this.getPkCombatting().getNextMovement())) {
			System.out.println(this.getPkCombatting().getName() + " intentó usar "
					+ this.getPkCombatting().getNextMovement().getName() + ", pero está anulado!");

			this.getPkCombatting().setCanAttack(false);
			return;
		}

		// -----------------------------
		// WHIRLWIND (REMOLINO) / ROAR (RUGIDO) / MIST (NEBLINA)
		// -----------------------------
		if (atkAttacker.getId() == 18 || atkAttacker.getId() == 46 || atkAttacker.getId() == 54) {

			// If Pokemon facing is invulnerable, cannot do the attack
			if (isDefenderCharging && !canHitInvulnerable) {
				this.getPkCombatting().setCanAttack(false);
				System.out.println(this.getPkCombatting().getName() + " usó " + atkAttacker.getName() + ", pero "
						+ this.getPkFacing().getName() + " evitó el ataque (invulnerable).");
			} else {
				// Asserts all the time
				this.getPkCombatting().setCanAttack(true);
			}

			return;
		}

		// For almost all the attacks from "otros", it has 100% of accuracy
		if (atkAttacker.getId() == 14 || atkAttacker.getId() == 74) {
			this.getPkCombatting().setCanAttack(true);
			return;
		}

		// Some attacks with adequate weather hit all the time (Thunder, etc.)
		if (weather == Weather.RAIN && atkAttacker.getId() == 87) {
			this.getPkCombatting().setCanAttack(true);
			return;
		}

		// -----------------------------
		// "Struggle" attack has 100% of precision (used when no more PPs remaining on
		// other attacks, etc.)
		// -----------------------------
		if (atkAttacker.getId() == 165) {
			this.getPkCombatting().setCanAttack(true);
			return;
		}

		// -----------------------------
		// GUILLOTINE (GUILLOTINA) / HORN DRILL (PERFORADOR)
		// -----------------------------
		if (atkAttacker.getId() == 12 || atkAttacker.getId() == 32) {
			accuracyFactor = (atkAttacker.getPrecision() / atkAttacker.getPrecision()); // don't take into account
																						// Pokemon levels (cause all
			// are on the same lvl)
		}
		// -----------------------------
		// RETREAT POKEMON (STOMP/ ROLLING KICK/ HEADBUTT/ BITE)
		// -----------------------------
		else if ((atkAttacker.getId() == 23 || atkAttacker.getId() == 27 || atkAttacker.getId() == 29
				|| atkAttacker.getId() == 44) && this.getPkFacing().getHasUsedMinimize()) {
			accuracyFactor = (atkAttacker.getPrecision() / 100f)
					* (getEvasionOrAccuracy(this.getPkCombatting(), 1) / 1f);
		}
		// Other attacks
		else {
			accuracyFactor = (atkAttacker.getPrecision() / 100f)
					* (getEvasionOrAccuracy(this.getPkCombatting(), 1) / getEvasionOrAccuracy(this.getPkFacing(), 2));
		}

		// Reset CanAttack if doesn't enter in any case
		this.getPkCombatting().setCanAttack(false);

		// -----------------------------
		// BLOC 1 : NORMAL ATTACK
		// -----------------------------
		if (atkAttacker.getCategory() == AttackCategory.NORMAL && !isDefenderCharging) {
			System.out.println(ANSI_PURPLE + "Probability - Normal attack (defender not charging)" + ANSI_RESET);

			handleNormalAccuracyCheck(accuracyFactor, atkAttacker, this.getPkCombatting(), this.getPkFacing(),
					"(bloc 1)");
			return;
		}

		// -----------------------------
		// BLOC 2 : NORMAL ATTACK - CAN HIT INVULNERABLE POKEMON
		// -----------------------------
		if (canHitInvulnerable && isDefenderCharging) {
			System.out
					.println(ANSI_PURPLE + "Probability - Normal attack can hit while defender charging" + ANSI_RESET);

			handleNormalAccuracyCheck(accuracyFactor, atkAttacker, this.getPkCombatting(), this.getPkFacing(),
					"(bloc 2)");
			return;
		}

		// -----------------------------
		// BLOC 3 : FIRST TURN FOR (FROM CHARGING ATTACK)
		// -----------------------------
		if (atkAttacker.getCategory() == AttackCategory.CHARGED && !isAttackerCharging) {
			System.out.println(ANSI_PURPLE + "Probability - Starting a charged attack" + ANSI_RESET);

			pkCombatting.setCanAttack(true);
			System.out.println(ANSI_PURPLE + this.getPkCombatting().getName() + " utilizará " + atkAttacker.getName()
					+ " - comienza a cargar el ataque. (bloc 3)" + ANSI_RESET);
			return;
		}

		// -----------------------------
		// BLOC 4 : SECOND TURN (FROM CHARGING ATTACK) - DEFENDER NOT CHARGING
		// -----------------------------
		if (isAttackerCharging && !isDefenderCharging) {
			System.out.println(
					ANSI_PURPLE + "Probability - Charged attack execution (defender not charging)" + ANSI_RESET);

			handleChargedAttackExecution(accuracyFactor, atkAttacker, this.getPkCombatting(), this.getPkFacing(),
					"(bloc 4)");
			return;
		}

		// -----------------------------
		// BLO 5 : SECOND TURN (FROM CHARGING ATTACK) - DEFENDER IS CHARGING
		// -----------------------------
		if (isAttackerCharging && isDefenderCharging) {
			System.out.println(ANSI_PURPLE + "Probability - Charged vs Charged (attack avoided)" + ANSI_RESET);

			pkCombatting.setCanAttack(false);
			pkCombatting.setIsChargingAttackForNextRound(false);

			System.out.println(this.getPkCombatting().getName() + " usó " + atkAttacker.getName() + ". "
					+ this.getPkFacing().getName() + " evitó el ataque jijijija. (bloc 5)");
			return;
		}

		// -----------------------------
		// BLOC 6 : OTHER ATTACK AGAINST INVULNERABLE (CANNOT DO DAMMAGE)
		// -----------------------------
		if (!canHitInvulnerable && isDefenderCharging) {

			System.out.println(ANSI_PURPLE + "Probability - Normal attack fails vs invulnerable defender" + ANSI_RESET);

			pkCombatting.setCanAttack(false);
			pkCombatting.setIsChargingAttackForNextRound(false);

			System.out.println(this.getPkCombatting().getName() + " usó " + atkAttacker.getName() + ". "
					+ this.getPkFacing().getName() + " evitó el ataque jijijija. (bloc 6)");
		}
	}

	// -----------------------------
	// Handle normal accuracy
	// -----------------------------
	private void handleNormalAccuracyCheck(float accuracyFactor, Attack atk, Pokemon attacker, Pokemon defender,
			String code) {

		if (accuracyFactor >= 1f) {
			attacker.setCanAttack(true);
			return;
		}

		int rand = (int) (Math.random() * 100);

		if (rand / 100f <= accuracyFactor) {
			attacker.setCanAttack(true);
		} else {
			System.out.println("accuracy : " + rand / 100f + "(random) => " + accuracyFactor + " (true accuracy)");
			atk.setPp(atk.getPp() - 1);
			attacker.setCanAttack(false);

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
			attacker.setCanAttack(true);
			return;
		}

		int rand = (int) (Math.random() * 100);

		if (rand / 100f <= accuracyFactor) {
			attacker.setCanAttack(true);
		} else {
			atk.setPp(atk.getPp() - 1);
			attacker.setCanAttack(false);
			attacker.setIsChargingAttackForNextRound(false);

			System.out.println(attacker.getName() + " usó " + atk.getName() + ". " + defender.getName() + " (Id:"
					+ defender.getId() + ")" + " evitó el ataque jijijija. " + code);
		}
	}

	// -----------------------------
	// Gets the attack effect and apply damage
	// -----------------------------
	public void doAttackEffect(Weather weather, boolean isMistEffectActivated) {

		float dmg = 0f;
		float dmgToSum = 0f;
		int nbTimesAttack;
		int nbTurnsHoldingStatus;
		int probabilityGettingStatus;
		double randomRetreat = Math.random();
		float recoil = 0f;
		boolean reduceDefRival = false;
		boolean reduceSpeedRival = false;
		boolean reduceAttackRival = false;
		Ability abilityAttacker = this.getPkCombatting().getAbilitySelected();
		Ability abilityDefender = this.getPkFacing().getAbilitySelected();
		int modifierWeather = 1;
		boolean canBeFrozen = weather != Weather.SUN;

		switch (this.getPkCombatting().getNextMovement().getId()) {
		// Destructor/Pound (tested)
		case 1:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Destructor");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Golpe kárate/Karate chop (tested)
		case 2:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Golpe kárate");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Doble bofetón/Double slap (tested)
		case 3:
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Doble bofetón");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Doble bofetón : " + nbTimesAttack);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);
			break;

		// Puño cometa/Comet punch (tested)
		case 4:
			nbTimesAttack = getRandomInt(1, 5);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Puño cometa");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Puño cometa : " + nbTimesAttack);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);
			break;

		// Megapuño/Mega punch (tested)
		case 5:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Megapuño");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Día de pago/Pay day (tested)
		case 6:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Día de pago");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Puño fuego/Fire punch (tested)
		case 7:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño fuego");

			dmg = doDammage();

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de quemar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 10) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

					State burned = new State(StatusConditions.BURNED);

					this.getPkFacing().setStatusCondition(burned);

					System.out.println(this.getPkFacing().getName() + " fue quemado");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Puño hielo/Ice punch (tested)
		case 8:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño hielo");

			dmg = doDammage();

			// Ice Pokemon cannot be frozen
			if (canBeFrozen
					&& this.getPkFacing().getTypes().stream().filter(t -> t.getId() == 9).findAny().get() == null) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				System.out.println("proba de congelar : " + probabilityGettingStatus);

				// 10% of probabilities to be frozen
				if (probabilityGettingStatus <= 10) {

					if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

						State frozen = new State(StatusConditions.FROZEN);

						this.getPkFacing().setStatusCondition(frozen);

						System.out.println(this.getPkCombatting().getName() + " fue congelado");
					}
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Puño trueno/Thunder punch (tested)
		case 9:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño trueno");

			dmg = doDammage();

			// Possibility of paralyzing Pokemon facing if is not already paralyzed and has
			// not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				System.out.println("proba de paralizar : " + probabilityGettingStatus);

				if (probabilityGettingStatus <= 10) {
					this.getPkFacing().trySetStatus(new State(StatusConditions.PARALYZED));
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Arañazo/Scratch (tested)
		case 10:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Arañazo");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Agarre/Vise grip (tested)
		case 11:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Agarre");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Guillotina/Guillotine (tested)
		case 12:
			System.out.println(this.getPkCombatting().getName() + " usó Guillotina");

			// One-Hit KO => Pokemon facing dies instantly (depending on conditions)
			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			if (this.getPkFacing().getAbilitySelected().getId() == 5
					&& !this.getPkFacing().getAbilitySelected().getAlreadyUsedOnEnter()
					&& (this.getPkCombatting().getNextMovement().getIsOneHitKO())) {
				this.getPkFacing().setPs(1f);
				this.getPkFacing().getAbilitySelected().setAlreadyUsedOnEnter(true);
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId()
						+ "), se quedó a un PS gracias a la habilidad Robustez");
			} else {
				this.getPkFacing().setPs(0f);
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId()
						+ "), se debilitó de un golpe con el ataque fulminante");
			}

			break;

		// Viento cortante/Razor wind (tested)
		case 13:
			// If not charging => first turn charge the attack
			if (!this.getPkCombatting().getIsChargingAttackForNextRound()) {

				// This attack requires to charge first time for one round
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " se prepara para Viento cortante");

				this.getPkCombatting().setIsChargingAttackForNextRound(true);

				// Apply damage => second turn
			} else {

				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Viento cortante");

				dmg = doDammage();

				// Pokemon is no more charging an attack
				this.getPkCombatting().setIsChargingAttackForNextRound(false);

				this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Danza espada/Swords dance (tested)
		case 14:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Danza espada");

			if (this.getPkCombatting().getAttackStage() >= 6) {
				System.out.println("El ataque de " + this.getPkCombatting().getName() + " (Id:"
						+ this.getPkCombatting().getId() + ")" + " no puede subir más!");
			} else {
				this.getPkCombatting().setAttackStage(Math.min(this.getPkCombatting().getAttackStage() + 2, 6));
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " aumentó mucho su Ataque!");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Corte/Cut (tested)
		case 15:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Corte");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Tornado/Gust (tested)
		case 16:
			if (this.getPkCombatting().getNextMovement().getCanHitWhileInvulnerable()
					.contains(this.getPkFacing().getNextMovement().getId())) {
				this.getPkCombatting().getNextMovement()
						.setPower(this.getPkCombatting().getNextMovement().getPower() * 2);
			}

			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Tornado");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Ataque ala/Wing attack (tested)
		case 17:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Ataque ala");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Remolino/Whirlwind
		case 18:
			System.out.println(this.getPkCombatting().getName() + " usó Remolino");

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			// If rival has no more Pokemon => it doesn't matter, but no fail
			if (!this.getDefender().hasAvailableSwitch()) {
				System.out.println("Pero " + this.getPkFacing().getName() + " no tiene más Pokémon para cambiar.");
				break;
			}

			// Force change
			this.getDefender().setForceSwitchPokemon(true);

			System.out.println("¡" + this.getPkFacing().getName() + " fue arrastrado y obligado a retirarse!");
			break;
		// Vuelo/Fly (tested)
		case 19:
			// If not charging => first turn charge the attack
			if (!this.getPkCombatting().getIsChargingAttackForNextRound()) {

				// This attack requires to charge first time for one round
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " voló muy alto");

				this.getPkCombatting().setIsChargingAttackForNextRound(true);

				// Apply damage => second turn
			} else {

				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Vuelo");

				dmg = doDammage();

				// Pokemon is no more charging an attack
				this.getPkCombatting().setIsChargingAttackForNextRound(false);

				this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

				this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			}
			break;

		// Atadura/Bind (tested)
		case 20:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Atadura");

			dmg = doDammage();

			// Check if the Pokemon facing doesn't have the status Trapped (is a status that
			// can be accumulated with other ephemeral status)
			if (!(this.getPkFacing().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPED))) {

				nbTurnsHoldingStatus = getRandomInt(4, 5);

				System.out.println(this.getPkFacing().getName() + " quedó atrapado");

				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);

				this.getPkFacing().addEstadoEfimero(trapped);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Atizar/Slam (tested)
		case 21:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Atizar");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Látigo cepa/Vine whip (tested)
		case 22:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Látigo cepa");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Pisotón/Stomp (tested)
		case 23:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Pisotón");

			// If Pokemon facing has used minimize, set power base of the attack x2
			if (this.getPkFacing().getHasUsedMinimize()) {
				this.getPkCombatting().getNextMovement()
						.setPower(this.getPkCombatting().getNextMovement().getPower() * 2);
			}

			dmg = doDammage();

			// If Pokemon attacking has abilidty "Stench", puts additional percentage on
			// flinch probability
			// But Pokemon facing doesn't have to have a counter ability
			if (abilityAttacker != null && abilityAttacker.getId() == 1 && abilityDefender.getId() != 39) {
				abilityAttacker.getEffect().afterAttack(null, this.getPkCombatting(), this.getPkFacing(),
						this.getPkCombatting().getNextMovement(), dmg,
						this.getPkCombatting().getNextMovement().getPercentageFlinched());
			}
			// Otherwise applies flinch from the attack
			else if (abilityDefender.getId() != 39) {
				// 30% of probabilities to retreat Pokemon facing
				if (randomRetreat <= this.getPkCombatting().getNextMovement().getPercentageFlinched()) {
					this.getPkFacing().setHasRetreated(true);
				}
			} else {
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no puedo retroceder dada su habilidad (u otro factor)");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Doble patada/Double kick (tested)
		case 24:
			// Attacks 2 times
			nbTimesAttack = 2;

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Doble patada");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);
			break;

		// Megapatada/Mega kick (tested)
		case 25:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Megapatada");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Patada salto/Jump kick (tested)
		case 26:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Patada salto");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Patada giro/Rolling kick (tested)
		case 27:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Patada giro");

			dmg = doDammage();

			// If Pokemon attacking has abilidty "Stench", puts additional percentage on
			// flinch probability
			// But Pokemon facing doesn't have to have a counter ability
			if (abilityAttacker != null && abilityAttacker.getId() == 1 && abilityDefender.getId() != 39) {
				abilityAttacker.getEffect().afterAttack(null, this.getPkCombatting(), this.getPkFacing(),
						this.getPkCombatting().getNextMovement(), dmg,
						this.getPkCombatting().getNextMovement().getPercentageFlinched());
			}
			// Otherwise applies flinch from the attack
			else if (abilityDefender.getId() != 39) {
				// 30% of probabilities to retreat Pokemon facing
				if (randomRetreat <= this.getPkCombatting().getNextMovement().getPercentageFlinched()) {
					this.getPkFacing().setHasRetreated(true);
				}
			} else {
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no puedo retroceder dada su habilidad (u otro factor)");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Ataque arena /Sand attack (tested)
		case 28:
			if (!isMistEffectActivated) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Ataque arena");

				if (this.getPkFacing().getPrecisionPoints() <= -6) {
					System.out.println("La precisión de " + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
				} else {
					this.getPkFacing().setPrecisionPoints(Math.max(this.getPkFacing().getPrecisionPoints() - 1, -6));
					System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
							+ " bajó su precisión!");
				}
			} else {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			break;

		// Golpe cabeza/Headbutt (tested)
		case 29:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Golpe cabeza");

			dmg = doDammage();

			// If Pokemon attacking has abilidty "Stench", puts additional percentage on
			// flinch probability
			// But Pokemon facing doesn't have to have a counter ability
			if (abilityAttacker != null && abilityAttacker.getId() == 1 && abilityDefender.getId() != 39) {
				abilityAttacker.getEffect().afterAttack(null, this.getPkCombatting(), this.getPkFacing(),
						this.getPkCombatting().getNextMovement(), dmg,
						this.getPkCombatting().getNextMovement().getPercentageFlinched());
			}
			// Otherwise applies flinch from the attack
			else if (abilityDefender.getId() != 39) {
				// 30% of probabilities to retreat Pokemon facing
				if (randomRetreat <= this.getPkCombatting().getNextMovement().getPercentageFlinched()) {
					this.getPkFacing().setHasRetreated(true);
				}
			} else {
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no puedo retroceder dada su habilidad (u otro factor)");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Cornada/Horn attack (tested)
		case 30:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Cornada");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Ataque furia/Fury attack (tested)
		case 31:
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Ataque furia");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Ataque furia : " + nbTimesAttack);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);
			break;

		// Perforador/Horn drill (tested)
		case 32:
			System.out.println(this.getPkCombatting().getName() + " usó Perforador");

			// One-Hit KO => Pokemon facing dies instantly (depending on conditions)
			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			if (this.getPkFacing().getAbilitySelected().getId() == 5
					&& !this.getPkFacing().getAbilitySelected().getAlreadyUsedOnEnter()
					&& (this.getPkCombatting().getNextMovement().getIsOneHitKO())) {
				this.getPkFacing().setPs(1f);
				this.getPkFacing().getAbilitySelected().setAlreadyUsedOnEnter(true);
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId()
						+ "), se quedó a un PS gracias a la habilidad Robustez");
			} else {
				this.getPkFacing().setPs(0f);
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId()
						+ "), se debilitó de un golpe con el ataque fulminante");
			}
			break;

		// Placaje/Tackle (tested)
		case 33:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Placaje");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Golpe cuerpo/Body slam (tested)
		case 34:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Golpe cuerpo");

			dmg = doDammage();

			// Possibility of paralyzing Pokemon facing if is not already paralyzed and has
			// not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				System.out.println("proba de paralizar : " + probabilityGettingStatus);

				if (probabilityGettingStatus <= 30) {
					this.getPkFacing().trySetStatus(new State(StatusConditions.PARALYZED));
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Constricción/Wrap (tested)
		case 35:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Constricción");

			dmg = doDammage();

			// Check if the Pokemon facing doesn't have the status Trapped (is a status that
			// can be accumulated with other ephemeral status)
			if (!(this.getPkFacing().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPED))) {

				nbTurnsHoldingStatus = getRandomInt(4, 5);

				System.out.println(this.getPkFacing().getName() + " quedó atrapado");

				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);

				this.getPkFacing().addEstadoEfimero(trapped);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Derribo/Take down (tested)
		case 36:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Derribo");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// ---- RECOIL ----
			recoil = dmg * 0.25f;

			this.getPkCombatting().setPs(this.getPkCombatting().getPs() - recoil);

			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId()
					+ ") se dañó a sí mismo por el retroceso (" + recoil + ")");
			break;

		// Saña/Thrash (tested)
		case 37:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Saña");

			dmg = doDammage();

			if (!(this.getPkCombatting().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPEDBYOWNATTACK))) {

				nbTurnsHoldingStatus = getRandomInt(2, 5);

				System.out.println(this.getPkCombatting().getName() + " usará el mismo ataque durante "
						+ nbTurnsHoldingStatus + " turnos.");

				State trappedByOwnAttack = new State(StatusConditions.TRAPPEDBYOWNATTACK, nbTurnsHoldingStatus + 1);

				this.getPkCombatting().addEstadoEfimero(trappedByOwnAttack);

				// Only removes PP when choosing the attack
				this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Doble filo/Dobule-Edge (tested)
		case 38:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Doble filo");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// ---- RECOIL ----
			recoil = dmg * 0.33f;

			this.getPkCombatting().setPs(this.getPkCombatting().getPs() - recoil);

			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId()
					+ ") se dañó a sí mismo por el retroceso (" + recoil + ")");
			break;

		// Látigo/Tail Whip (tested)
		case 39:
			if (!isMistEffectActivated) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Látigo");

				if (this.getPkFacing().getDefenseStage() <= -6) {
					System.out.println("La defensa de " + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
				} else {
					this.getPkFacing().setDefenseStage(Math.max(this.getPkFacing().getDefenseStage() - 1, -6));
					System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
							+ " bajó su defensa!");
				}
			} else {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Picotazo veneno/Poison sting (tested)
		case 40:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Picotazo veneno");

			dmg = doDammage();

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de envenenar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 30) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

					State poisoned = new State(StatusConditions.POISONED);

					this.getPkFacing().setStatusCondition(poisoned);

					System.out.println(this.getPkFacing().getName() + " fue envenenado");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Doble ataque/Twineedle (tested)
		case 41:
			// Attacks 2 times
			nbTimesAttack = 2;

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Doble ataque");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de envenenar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 20) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

					State poisoned = new State(StatusConditions.POISONED);

					this.getPkFacing().setStatusCondition(poisoned);

					System.out.println(this.getPkFacing().getName() + " fue envenenado");
				}
			}
			break;

		// Pin misil/Pin missile (tested)
		case 42:
			nbTimesAttack = getRandomInt(1, 5);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Pin misil");

				dmg = doDammage();

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Pin misil : " + nbTimesAttack);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);
			break;

		// Malicioso/Leer (tested)
		case 43:
			if (!isMistEffectActivated) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Malicioso");

				if (this.getPkFacing().getDefenseStage() <= -6) {
					System.out.println("La defensa de " + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
				} else {
					this.getPkFacing().setDefenseStage(Math.max(this.getPkFacing().getDefenseStage() - 1, -6));
					System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
							+ " bajó su defensa!");
				}
			} else {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Mordisco/Bite (tested)
		case 44:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Mordisco");

			dmg = doDammage();

			// If Pokemon attacking has abilidty "Stench", puts additional percentage on
			// flinch probability
			// But Pokemon facing doesn't have to have a counter ability
			if (abilityAttacker != null && abilityAttacker.getId() == 1 && abilityDefender.getId() != 39) {
				abilityAttacker.getEffect().afterAttack(null, this.getPkCombatting(), this.getPkFacing(),
						this.getPkCombatting().getNextMovement(), dmg,
						this.getPkCombatting().getNextMovement().getPercentageFlinched());
			}
			// Otherwise applies flinch from the attack
			else if (abilityDefender.getId() != 39) {
				// 30% of probabilities to retreat Pokemon facing
				if (randomRetreat <= this.getPkCombatting().getNextMovement().getPercentageFlinched()) {
					this.getPkFacing().setHasRetreated(true);
				}
			} else {
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no puedo retroceder dada su habilidad (u otro factor)");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Gruñido/Growl (tested)
		case 45:
			if (!isMistEffectActivated) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Gruñido");

				if (this.getPkFacing().getAttackStage() <= -6) {
					System.out.println("El ataque de " + this.getPkCombatting().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
				} else {
					this.getPkFacing().setAttackStage(Math.min(this.getPkFacing().getAttackStage() - 1, -6));
					System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
							+ " bajó su defensa!");
				}
			} else {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Rugido/Roar (tested)
		case 46:
			System.out.println(this.getPkCombatting().getName() + " usó Rugido");

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			// If rival has no more Pokemon remaining, it doesn't matter => only fails the
			// attack
			if (!this.getDefender().hasAvailableSwitch()) {
				System.out.println("Pero " + this.getPkFacing().getName() + " no tiene más Pokémon para cambiar.");
				break;
			}

			// Force change
			this.getDefender().setForceSwitchPokemon(true);

			System.out.println("¡" + this.getPkFacing().getName() + " fue arrastrado y obligado a retirarse!");
			break;

		// Canto/Sing (tested)
		case 47:
			System.out.println(this.getPkCombatting().getName() + " usó Canto");

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			// Check if the Pokemon facing doesn't have the status Asleep (is a status that
			// can be accumulated with other ephemeral status)
			if (!(this.getPkFacing().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.ASLEEP))) {

				nbTurnsHoldingStatus = getRandomInt(1, 7);

				System.out.println(this.getPkFacing().getName() + " cayó en un sueño profundo por "
						+ nbTurnsHoldingStatus + " turnos");

				State asleep = new State(StatusConditions.ASLEEP, nbTurnsHoldingStatus + 1);

				this.getPkFacing().addEstadoEfimero(asleep);
			} else {
				System.out.println(this.getPkFacing().getName() + " ya está dormido!");
			}
			break;

		// Supersónico/Supersonic (tested)
		case 48:
			System.out.println(this.getPkCombatting().getName() + " usó Supersónico");

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			// Check if the Pokemon facing doesn't have the status Confused (is a status
			// that
			// can be accumulated with other ephemeral status)
			if (!(this.getPkFacing().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.CONFUSED))) {

				nbTurnsHoldingStatus = getRandomInt(1, 7);

				System.out.println(
						this.getPkFacing().getName() + " está confuso por " + nbTurnsHoldingStatus + " turnos");

				State confused = new State(StatusConditions.CONFUSED, nbTurnsHoldingStatus + 1);

				this.getPkFacing().addEstadoEfimero(confused);
			} else {
				System.out.println(this.getPkFacing().getName() + " ya está confuso!");
			}
			break;

		// Bomba sónica/Sonic boom (tested)
		case 49:
			System.out.println(this.getPkCombatting().getName() + " usó Bomba sónica");

			dmg = 20f;

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Anulación/Disable (tested)
		case 50:
			System.out.println(this.getPkCombatting().getName() + " usó Anulación");

			Attack disable = this.getPkCombatting().getNextMovement();
			Attack lastAttack = this.getPkFacing().getLastUsedAttack();

			disable.setPp(disable.getPp() - 1);

			// If rival hasn't yet used an attack => fails
			if (lastAttack == null || lastAttack.getId() == 0) {
				System.out.println("¡Pero no surtió efecto!");
				break;
			}

			// Gets if existing a disable state (cause needs to be replaced)
			State previousDisableState = this.getPkFacing().getEphemeralStates().stream()
					.filter(e -> e.getStatusCondition() == StatusConditions.DISABLE).findFirst().orElse(null);

			if (previousDisableState != null) {
				this.getPkFacing().getEphemeralStates().remove(previousDisableState);
			}

			nbTurnsHoldingStatus = getRandomInt(4, 7);

			State attackDisabled = new State(StatusConditions.DISABLE, nbTurnsHoldingStatus + 1);
			attackDisabled.setAttackDisabled(lastAttack);

			this.getPkFacing().addEstadoEfimero(attackDisabled);

			System.out.println(this.getPkFacing().getName() + " no podrá usar " + lastAttack.getName() + " por "
					+ nbTurnsHoldingStatus + " turnos");
			break;

		// Acido/Acid (tested)
		case 51:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Ácido");

			dmg = doDammage();

			// 10% of probabilities to reduce the special defense
			reduceDefRival = Math.random() <= 0.10;

			if (reduceDefRival) {
				if (!isMistEffectActivated) {
					if (this.getPkFacing().getSpecialDefenseStage() <= -6) {
						System.out.println("La defensa especial de " + this.getPkFacing().getName() + " (Id:"
								+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
					} else {
						this.getPkFacing()
								.setSpecialDefenseStage(Math.max(this.getPkFacing().getSpecialDefenseStage() - 1, -6));
						System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
								+ " bajó su defensa especial!");
					}
				} else {
					System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
							+ " no pudo bajar las estadísticas a causa de Neblina");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Ascuas/Ember (tested)
		case 52:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Ascuas");

			dmg = doDammage();

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de quemar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 10) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

					State burned = new State(StatusConditions.BURNED);

					this.getPkFacing().setStatusCondition(burned);

					System.out.println(this.getPkFacing().getName() + " fue quemado");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Lanzallamas/FlameThrower (tested)
		case 53:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Lanzallamas");

			dmg = doDammage();

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de quemar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 10) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

					State burned = new State(StatusConditions.BURNED);

					this.getPkFacing().setStatusCondition(burned);

					System.out.println(this.getPkFacing().getName() + " fue quemado");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Neblina/Mist (tested)
		case 54:
			System.out.println(this.getPkCombatting().getName() + " usó Neblina");

			if (isMistEffectActivated) {
				System.out.println("No tuvoo ningún efecto ya que está en uso");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Pistola agua/Water gun (tested)
		case 55:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Pistola agua");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Hidrobomba/Hydro Pump (tested)
		case 56:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Hidrobomba");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Surf/Surf (tested)
		case 57:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Surf");

			// If Pokemon facing has used Dive, set power base of the attack x2
			if (this.getPkFacing().getIsChargingAttackForNextRound()
					&& this.getPkFacing().getNextMovement().getId() == 291) {
				this.getPkCombatting().getNextMovement()
						.setPower(this.getPkCombatting().getNextMovement().getPower() * 2);
			}

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Rayo hielo/Ice beam (tested)
		case 58:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Rayo hielo");

			dmg = doDammage();

			// Ice Pokemon cannot be frozen
			if (canBeFrozen
					&& this.getPkFacing().getTypes().stream().filter(t -> t.getId() == 9).findAny().get() == null) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				System.out.println("proba de congelar : " + probabilityGettingStatus);

				// 10% of probabilities to be frozen
				if (probabilityGettingStatus <= 10) {

					if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

						State frozen = new State(StatusConditions.FROZEN);

						this.getPkFacing().setStatusCondition(frozen);

						System.out.println(this.getPkCombatting().getName() + " fue congelado");
					}
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Ventisca/Blizzard (tested)
		case 59:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Ventisca");

			dmg = doDammage();

			// Ice Pokemon cannot be frozen
			if (canBeFrozen
					&& this.getPkFacing().getTypes().stream().filter(t -> t.getId() == 9).findAny().get() == null) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				System.out.println("proba de congelar : " + probabilityGettingStatus);

				// 10% of probabilities to be frozen
				if (probabilityGettingStatus <= 10) {

					if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

						State frozen = new State(StatusConditions.FROZEN);

						this.getPkFacing().setStatusCondition(frozen);

						System.out.println(this.getPkCombatting().getName() + " fue congelado");
					}
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Psicorrayo/Psybeam (tested)
		case 60:
			System.out.println(this.getPkCombatting().getName() + " usó Psicorrayo");

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			// Check if the Pokemon facing doesn't have the status Confused (is a status
			// that
			// can be accumulated with other ephemeral status)
			if (!(this.getPkFacing().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.CONFUSED))) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				// 10% of probabilities to be confused
				if (probabilityGettingStatus <= 10) {
					nbTurnsHoldingStatus = getRandomInt(1, 7);

					System.out.println(
							this.getPkFacing().getName() + " está confuso por " + nbTurnsHoldingStatus + " turnos");

					State confused = new State(StatusConditions.CONFUSED, nbTurnsHoldingStatus + 1);

					this.getPkFacing().addEstadoEfimero(confused);
				}
			}
			break;

		// Rayo burbuja/Bubble beam (tested)
		case 61:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Rayo burbuja");

			dmg = doDammage();

			// 10% of probabilities to reduce the speed
			reduceSpeedRival = Math.random() <= 0.10;

			if (reduceSpeedRival) {
				if (!isMistEffectActivated) {
					if (this.getPkFacing().getSpeedStage() <= -6) {
						System.out.println("La velocidad de " + this.getPkFacing().getName() + " (Id:"
								+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
					} else {
						this.getPkFacing().setSpeedStage(Math.max(this.getPkFacing().getSpeedStage() - 1, -6));
						System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
								+ " bajó su velocidad!");
					}
				} else {
					System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
							+ " no pudo bajar las estadísticas a causa de Neblina");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Rayo aurora/Aurora beam (tested)
		case 62:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Rayo aurora");

			dmg = doDammage();

			// 10% of probabilities to reduce the attack
			reduceAttackRival = Math.random() <= 0.10;

			if (reduceAttackRival) {
				if (!isMistEffectActivated) {
					if (this.getPkFacing().getAttackStage() <= -6) {
						System.out.println("El ataque de " + this.getPkFacing().getName() + " (Id:"
								+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
					} else {
						this.getPkFacing().setAttackStage(Math.max(this.getPkFacing().getAttackStage() - 1, -6));
						System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
								+ " bajó su ataque!");
					}
				} else {
					System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
							+ " no pudo bajar las estadísticas a causa de Neblina");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Hiperrayo/Hyper beam (tested)
		case 63:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Hiperrayo");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// Pokemon combating cannot do anything next round
			this.getPkCombatting().setCanDonAnythingNextRound(false);

			break;

		// Picotazo/Peck (tested)
		case 64:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Picotazo");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Pico taladro/Drill peck (tested)
		case 65:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Pico taladro");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Sumisión/Submission (tested)
		case 66:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Sumisión");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// ---- RECOIL ----
			recoil = dmg * 0.25f;

			this.getPkCombatting().setPs(this.getPkCombatting().getPs() - recoil);

			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId()
					+ ") se dañó a sí mismo por el retroceso (" + recoil + ")");
			break;

		// Patada baja/Low kick (tested)
		case 67:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Patada baja");

			// Set power of the attack depending on the weight of the Pokemon facing
			if (this.getPkFacing().getWeight() < 10) {
				this.getPkCombatting().getNextMovement().setPower(20);
			} else if (this.getPkFacing().getWeight() >= 10 && this.getPkFacing().getWeight() < 25) {
				this.getPkCombatting().getNextMovement().setPower(40);
			} else if (this.getPkFacing().getWeight() >= 25 && this.getPkFacing().getWeight() < 50) {
				this.getPkCombatting().getNextMovement().setPower(60);
			} else if (this.getPkFacing().getWeight() >= 50 && this.getPkFacing().getWeight() < 100) {
				this.getPkCombatting().getNextMovement().setPower(80);
			} else if (this.getPkFacing().getWeight() >= 100 && this.getPkFacing().getWeight() < 200) {
				this.getPkCombatting().getNextMovement().setPower(100);
			} else {
				this.getPkCombatting().getNextMovement().setPower(120);
			}

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Contraataque/Counter (tested)
		case 68:
			if (this.getPkCombatting().getHasReceivedDamage()) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Contraataque");

				dmg = this.getPkCombatting().getDamageReceived() * 2f;

				this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

				this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

				System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			} else {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no puede usar Contraataque ya que no recibió ningún ataque físico este turno");
			}
			break;

		// Sísmico/Seismic toss (tested)
		case 69:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Sísmico");

			// This attack only apply the same amount on damage points as the level of the
			// pokemon using it (all the Pokemon in the game are on the level 100)
			dmg = 100f;

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + " (Id:"
					+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			break;

		// Fuerza/Strength (tested)
		case 70:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Fuerza");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Absorber/Absorb (tested)
		case 71:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Absorber");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// Pokemon combating gets health
			if (this.getPkCombatting().getPs() != this.getPkCombatting().getInitialPs()) {

				// The half of damage done
				this.getPkCombatting().setPs(this.getPkCombatting().getPs() + (dmg / 2f));

				// If more PS received than initial PS, put the max limit at initial PS
				if (this.getPkCombatting().getPs() >= this.getPkCombatting().getInitialPs()) {
					this.getPkCombatting().setPs(this.getPkCombatting().getInitialPs());
				}
			}

			break;

		// Megaagotar/Mega drain (tested)
		case 72:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Megaagotar");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// Pokemon combating gets health
			if (this.getPkCombatting().getPs() != this.getPkCombatting().getInitialPs()) {

				// The half of damage done
				this.getPkCombatting().setPs(this.getPkCombatting().getPs() + (dmg / 2f));

				// If more PS received than initial PS, put the max limit at initial PS
				if (this.getPkCombatting().getPs() >= this.getPkCombatting().getInitialPs()) {
					this.getPkCombatting().setPs(this.getPkCombatting().getInitialPs());
				}
			}
			break;

		// Drenadoras/Leech seed (tested)
		case 73:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Drenadoras");

			// Doesn't affect to grass type
			if (!this.getPkFacing().getTypes().stream().filter(t -> t.getId() == 12).findAny().isPresent()) {

				this.getPkCombatting().setIsDraining(true);

				if (!(this.getPkFacing().getEphemeralStates().stream()
						.anyMatch(e -> e.getStatusCondition() == StatusConditions.DRAINEDALLTURNS))) {

					System.out.println(this.getPkFacing().getName() + " fue drenado");

					State drainedAllTurns = new State(StatusConditions.DRAINEDALLTURNS, 0);

					this.getPkFacing().addEstadoEfimero(drainedAllTurns);
				} else {
					System.out.println(this.getPkFacing().getName() + " ya está drenado");
				}
			} else {
				System.out.println(this.getPkFacing().getName() + "no puede estar drenado ya que es de tipo planta");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			break;

		// Desarrollo/Growth (tested)
		case 74:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Desarrollo");

			// Add +2 if adequate weather
			if (weather == Weather.SUN) {
				modifierWeather = 2;
			}

			// Normal attack
			if (this.getPkCombatting().getAttackStage() >= 6) {
				System.out.println("El ataque de " + this.getPkCombatting().getName() + " (Id:"
						+ this.getPkCombatting().getId() + ")" + " no puede subir más!");
			} else {
				this.getPkCombatting()
						.setAttackStage(Math.min(this.getPkCombatting().getAttackStage() + modifierWeather, 6));
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " aumentó su Ataque!");
			}

			// Special attack
			if (this.getPkCombatting().getSpecialAttackStage() >= 6) {
				System.out.println("El ataque especial de " + this.getPkCombatting().getName() + " (Id:"
						+ this.getPkCombatting().getId() + ")" + " no puede subir más!");
			} else {
				this.getPkCombatting().setSpecialAttackStage(
						Math.min(this.getPkCombatting().getSpecialAttackStage() + modifierWeather, 6));
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " aumentó su Ataque especial!");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Hoja afilada/Razor leaf (tested)
		case 75:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó hoja afilada");

			dmg = doDammage();

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Rayo solar/Solar beam(tested)
		case 76:
			if (weather == Weather.SUN) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Rayo solar");

				dmg = doDammage();

				// Pokemon is no more charging an attack
				this.getPkCombatting().setIsChargingAttackForNextRound(false);

				this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

				this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			} else {
				// If not charging => first turn charge the attack
				if (!this.getPkCombatting().getIsChargingAttackForNextRound()) {

					// This attack requires to charge first time for one round
					System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
							+ " se prepara para Rayo solar");

					this.getPkCombatting().setIsChargingAttackForNextRound(true);

					// Apply damage => second turn
				} else {

					System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
							+ " usó Rayo solar");

					// Depending on weather it has less power
					if (weather == Weather.RAIN || weather == Weather.HAIL || weather == Weather.SANDSTORM) {
						this.getPkCombatting().getNextMovement()
								.setPower(this.getPkCombatting().getNextMovement().getPower() / 2);
					}

					dmg = doDammage();

					// Pokemon is no more charging an attack
					this.getPkCombatting().setIsChargingAttackForNextRound(false);

					this.getPkCombatting().getNextMovement()
							.setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

					this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
				}
			}
			break;

		// Polvo veneno/Poison powder (tested)
		case 77:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Polvo veneno");

			// Possibility of poisoning Pokemon facing if is not already poisoned and has
			// not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

				State poisoned = new State(StatusConditions.POISONED);

				this.getPkFacing().setStatusCondition(poisoned);

				System.out.println(this.getPkFacing().getName() + " fue envenenado");
			} else {
				System.out.println(this.getPkFacing().getName() + " ya está envenenado");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Paralizador/Stun spore (tested)
		case 78:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Paralizador");

			// Possibility of paralyzing the Pokemon facing if is not already pralyzed and
			// has not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {
				this.getPkFacing().trySetStatus(new State(StatusConditions.PARALYZED));
			} else {
				System.out.println(this.getPkFacing().getName() + " ya está paralizado");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Somnífero/Sleep powder (tested)
		case 79:
			System.out.println(this.getPkCombatting().getName() + " usó Somnífero");

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			// Check if the Pokemon facing doesn't have the status Asleep (is a status that
			// can be accumulated with other ephemeral status)
			if (!(this.getPkFacing().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.ASLEEP))) {

				nbTurnsHoldingStatus = getRandomInt(1, 7);

				System.out.println(this.getPkFacing().getName() + " cayó en un sueño profundo por "
						+ nbTurnsHoldingStatus + " turnos");

				State asleep = new State(StatusConditions.ASLEEP, nbTurnsHoldingStatus + 1);

				this.getPkFacing().addEstadoEfimero(asleep);
			} else {
				System.out.println(this.getPkFacing().getName() + " ya está dormido!");
			}
			break;

		// Danza pétalo/Petal dance (tested)
		case 80:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Danza pétalo");

			dmg = doDammage();

			if (!(this.getPkCombatting().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPEDBYOWNATTACK))) {

				nbTurnsHoldingStatus = getRandomInt(2, 5);

				System.out.println(this.getPkCombatting().getName() + " usará el mismo ataque durante "
						+ nbTurnsHoldingStatus + " turnos.");

				State trappedByOwnAttack = new State(StatusConditions.TRAPPEDBYOWNATTACK, nbTurnsHoldingStatus + 1);

				this.getPkCombatting().addEstadoEfimero(trappedByOwnAttack);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Disparo démora/String shot (tested)
		case 81:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Diapro démora");

			if (!isMistEffectActivated) {
				if (this.getPkFacing().getSpeedStage() <= -6) {
					System.out.println("La velocidad de " + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
				} else {
					this.getPkFacing().setSpeedStage(Math.max(this.getPkFacing().getSpeedStage() - 1, -6));
					System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
							+ " bajó su velocidad!");
				}
			} else {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " no pudo bajar las estadísticas a causa de Neblina");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Furia dragón/Dragon rage (tested)
		case 82:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Furia dragón");

			dmg = 40f;

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Giro fuego/Fire spin (tested)
		case 83:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Giro fuego");

			dmg = doDammage();

			// Check if the Pokemon facing doesn't have the status Trapped (is a status that
			// can be accumulated with other ephemeral status)
			if (!(this.getPkFacing().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPED))) {

				nbTurnsHoldingStatus = getRandomInt(4, 5);

				System.out.println(this.getPkFacing().getName() + " quedó atrapado");

				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);

				this.getPkFacing().addEstadoEfimero(trapped);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Impactrueno/Thunder shock (tested)
		case 84:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Impactrueno");

			dmg = doDammage();

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de paralizar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 10) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {
					this.getPkFacing().trySetStatus(new State(StatusConditions.PARALYZED));
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Rayo/Thunderbolt (tested)
		case 85:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Rayo");

			dmg = doDammage();

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de paralizar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 10) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {
					this.getPkFacing().trySetStatus(new State(StatusConditions.PARALYZED));
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Onda trueno/Thunder wave (tested)
		case 86:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Onda trueno");

			// Possibility of paralyzing the Pokemon facing if is not already pralyzed and
			// has not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {
				this.getPkFacing().trySetStatus(new State(StatusConditions.PARALYZED));
			} else {
				System.out.println(this.getPkFacing().getName() + " ya está paralizado");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Trueno/Thunder (tested)
		case 87:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Trueno");

			dmg = doDammage();

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de paralizar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 10) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {
					this.getPkFacing().trySetStatus(new State(StatusConditions.PARALYZED));
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			break;

		// Forcejeo/Struggle
		case 165:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Forcejeo");

			dmg = doDammage();

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// Pokemon combating receives 25% of damage from his initial PS
			this.getPkCombatting()
					.setPs(this.getPkCombatting().getInitialPs() - (this.getPkCombatting().getInitialPs() * 0.25f));
			break;
		}

		if (dmg != 0f || dmgToSum != 0f) {
			// Set damage from physical attack => used for attacks like "Counter", etc.
			if (dmg != 0 && this.getPkCombatting().getPhysicalAttacks() != null
					&& this.getPkCombatting().getPhysicalAttacks().stream()
							.anyMatch(a -> a.getId() == this.getPkCombatting().getNextMovement().getId()))

			{
				this.getPkFacing().setDamageReceived(dmg);
			} else if (this.getPkCombatting().getPhysicalAttacks() != null
					&& this.getPkCombatting().getPhysicalAttacks().stream()
							.anyMatch(a -> a.getId() == this.getPkCombatting().getNextMovement().getId())) {
				this.getPkFacing().setDamageReceived(dmgToSum);
			}
			this.getPkFacing().setHasReceivedDamage(true);
		}

		reinitializeAttackStats(this.getPkCombatting().getNextMovement());

		if (this.getPkFacing().getPs() <= 0) {

			this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
		}

		// Get status debilitated for Pokemon combating
		if (this.getPkCombatting().getPs() <= 0) {

			this.getPkCombatting().setStatusCondition(new State(StatusConditions.DEBILITATED));
		}
	}

	// -----------------------------
	// Apply damage
	// -----------------------------
	public float doDammage() {

		// There is a random variation when attacking (the total damage is not the same
		// every time)
		int randomVariation = (int) ((Math.random() * (100 - 85)) + 85);

		// Weather can affect on effectiveness of the attack
		float weatherModifier = getWeatherModifier(this.getPkCombatting().getNextMovement());

		boolean isSpecialAttack = this.getPkCombatting().getNextMovement().getBases().contains("especial");

		// Attack from attacker
		Attack attack = this.getPkCombatting().getNextMovement();

		float dmg = 0;

		if (isSpecialAttack) {
			// Apply special damage
			dmg = 0.01f * attack.getBonus() * attack.getEffectivenessAgainstPkFacing() * weatherModifier
					* randomVariation
					* (((0.2f * 100f + 1f) * this.getPkCombatting().getEffectiveSpecialAttack() * attack.getPower())
							/ (25f * this.getPkFacing().getEffectiveSpecialDefense()) + 2f);

			// Apply normal damage
		} else {

			dmg = 0.01f * attack.getBonus() * attack.getEffectivenessAgainstPkFacing() * weatherModifier
					* randomVariation
					* (((0.2f * 100f + 1f) * this.getPkCombatting().getEffectiveAttack() * attack.getPower())
							/ (25f * this.getPkFacing().getEffectiveDefense()) + 2f);
		}

		System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + " (Id:"
				+ this.getPkFacing().getId() + ")" + ") : " + dmg);

		// If it's critic, damage x2
		if (attack.getId() == 13) {
			boolean isHighCritic30 = getHighCriticity30();

			// 30/100 of probabilities to have a critic attack
			if (isHighCritic30) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico - 30");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}
		} else if (attack.getId() == 2 || attack.getId() == 75) {
			boolean isHighCritic40 = getHighCriticity40();

			// 40/100 of probabilities to have a critic attack
			if (isHighCritic40) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico - 40");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}
		} else {
			boolean isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}
		}

		// Cannot be defeated by one hit KO or by one attack if PS are on max (5_Sturdy
		// ability)
		if (this.getPkFacing().getAbilitySelected().getId() == 5
				&& !this.getPkFacing().getAbilitySelected().getAlreadyUsedOnEnter()
				&& (this.getPkFacing().getInitialPs() == this.getPkFacing().getPs()
						&& dmg >= this.getPkFacing().getPs())) {
			dmg = this.getPkFacing().getInitialPs() - 1f;
			this.getPkFacing().getAbilitySelected().setAlreadyUsedOnEnter(true);
			System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId()
					+ "), se quedó a un PS gracias a la habilidad Robustez - check en doDammage()");
		}

		return dmg;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage)
	// -----------------------------
	public boolean getCriticity() {
		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 10) {
			// Cannot recieve critic damage because of ability "Battle armor"
			if (this.getPkFacing().getAbilitySelected().getId() == 4) {
				this.getPkFacing().getAbilitySelected().getEffect().onAttack(this.getPkCombatting(),
						this.getPkFacing());
				return false;
			}
			return true;
		}

		return false;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage)
	// -----------------------------
	public boolean getHighCriticity30() {
		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 30) {
			// Cannot recieve critic damage because of ability "Battle armor"
			if (this.getPkFacing().getAbilitySelected().getId() == 4) {
				this.getPkFacing().getAbilitySelected().getEffect().onAttack(this.getPkCombatting(),
						this.getPkFacing());
				return false;
			}
			return true;
		}

		return false;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage)
	// -----------------------------
	public boolean getHighCriticity40() {
		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 40) {
			// Cannot recieve critic damage because of ability "Battle armor"
			if (this.getPkFacing().getAbilitySelected().getId() == 4) {
				this.getPkFacing().getAbilitySelected().getEffect().onAttack(this.getPkCombatting(),
						this.getPkFacing());
				return false;
			}
			return true;
		}

		return false;
	}

	// -----------------------------
	// Gets number of turns for a state or number of attacks
	// -----------------------------
	public static int getRandomInt(int min, int max) {
		return min + (int) (Math.random() * (max - min + 1));
	}

	// -----------------------------
	// Gets if last attack from Pokemon combating used is disabled
	// -----------------------------
	public boolean isAttackDisabled(Pokemon pk, Attack selectedAttack) {

		State disableState = pk.getEphemeralStates().stream()
				.filter(e -> e.getStatusCondition() == StatusConditions.DISABLE).findFirst().orElse(null);

		if (disableState != null) {
			if (disableState.getAttackDisabled() == pk.getNextMovement()) {
				return true;
			}
		}
		return false;
	}

	// -----------------------------
	// Adds multiplier depending on weather of the game
	// -----------------------------
	public float getWeatherModifier(Attack attack) {

		Weather weather = this.getWeather();

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
	// Change attacks depending on abilities
	// -----------------------------
	private void checkAbilitiesEffectsForAttacks(Weather weather, Attack attack) {
		if (weather == Weather.SUN) {
			if (attack.getId() == 87) {
				attack.setPrecision(50);
			}
		}
	}

	// -----------------------------
	// Reset parameters from attacks (to avoid problems each turn)
	// -----------------------------
	private void reinitializeAttackStats(Attack attack) {
		attack.setPrecision(attack.getInitialPrecision());
		attack.setPower(attack.getInitialPower());
	}
}