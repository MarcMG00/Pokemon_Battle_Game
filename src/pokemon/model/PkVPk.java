package pokemon.model;

import pokemon.enums.AttackCategory;
import pokemon.enums.StatusConditions;

public class PkVPk {

	// ==================================== FIELDS
	// ====================================

	private Pokemon pkCombatting;
	private Pokemon pkFacing;
	private Player attacker;
	private Player defender;

	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	private static final String ANSI_RESET = "\u001B[0m";

	// ==================================== CONSTRCUTORS
	// ====================================

	public PkVPk(Player attacker, Player defender) {
		this.attacker = attacker;
		this.defender = defender;
		this.pkCombatting = attacker.getPkCombatting();
		this.pkFacing = defender.getPkCombatting();
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
	public void getProbabilityOfAttacking() {

		boolean isAttackerCharging = this.getPkCombatting().getIsChargingAttackForNextRound();
		boolean isDefenderCharging = this.getPkFacing().getIsChargingAttackForNextRound();

		Attack atkAttacker = this.getPkCombatting().getNextMovement();
		Attack atkDefender = this.getPkFacing().getNextMovement();

		boolean canHitInvulnerable = atkAttacker.getCanHitWhileInvulnerable().contains(atkDefender.getId());

		float accuracyFactor = 0f;

		// -----------------------------
		// WHIRLWIND / REMOLINO
		// -----------------------------
		if (atkAttacker.getId() == 18) {

			// If Pokemon facing is invulnerable, cannot do the attack
			if (isDefenderCharging && !canHitInvulnerable) {
				this.getPkCombatting().setCanAttack(false);
				System.out.println(pkCombatting.getName() + " usó Remolino, pero " + pkFacing.getName()
						+ " evitó el ataque (invulnerable).");
			} else {
				// Acierta siempre
				this.getPkCombatting().setCanAttack(true);
			}

			return;
		}

		// For almost all attacks from "otros", it has 100% of accuracy
		if (atkAttacker.getBases().contains("otros")) {
			this.getPkCombatting().setCanAttack(true);
			return;
		}

		// "Struggle" attack has 100% of precision (used when no more PPs remaining on
		// other attacks, etc.)
		if (atkAttacker.getId() == 165) {
			this.getPkCombatting().setCanAttack(true);
			return;
		}

		// Guillotina/Perforador
		if (atkAttacker.getId() == 12 || atkAttacker.getId() == 32) {
			accuracyFactor = (atkAttacker.getPrecision() / 100f); // don't take into account Pokemon levels (cause all
																	// are on the same lvl)
		}
		// Pisotón
		else if ((atkAttacker.getId() == 23 || atkAttacker.getId() == 27 || atkAttacker.getId() == 29
				|| atkAttacker.getId() == 44) && this.getPkFacing().getHasUsedMinimize()) {
			accuracyFactor = (atkAttacker.getPrecision() / 100f) * (getEvasionOrAccuracy(pkCombatting, 1) / 1f);
		}
		// Other attacks
		else {
			accuracyFactor = (atkAttacker.getPrecision() / 100f)
					* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
		}

		// Reset CanAttack if doesn't enter in any case
		this.getPkCombatting().setCanAttack(false);

		// -----------------------------
		// BLOC 1 : NORMAL ATTACK
		// -----------------------------
		if (atkAttacker.getCategory() == AttackCategory.NORMAL && !isDefenderCharging) {
			System.out.println(ANSI_PURPLE + "Probability - Normal attack (defender not charging)" + ANSI_RESET);

			handleNormalAccuracyCheck(accuracyFactor, atkAttacker, pkCombatting, pkFacing, "(bloc 1)");
			return;
		}

		// -----------------------------
		// BLOC 2 : NORMAL ATTACK - CAN HIT INVULNERABLE POKEMON
		// -----------------------------
		if (canHitInvulnerable && isDefenderCharging) {
			System.out
					.println(ANSI_PURPLE + "Probability - Normal attack can hit while defender charging" + ANSI_RESET);

			handleNormalAccuracyCheck(accuracyFactor, atkAttacker, pkCombatting, pkFacing, "(bloc 2)");
			return;
		}

		// -----------------------------
		// BLOC 3 : FIRST TURN FOR (FROM CHARGING ATTACK)
		// -----------------------------
		if (atkAttacker.getCategory() == AttackCategory.CHARGED && !isAttackerCharging) {
			System.out.println(ANSI_PURPLE + "Probability - Starting a charged attack" + ANSI_RESET);

			pkCombatting.setCanAttack(true);
			System.out.println(ANSI_PURPLE + pkCombatting.getName() + " utilizará " + atkAttacker.getName()
					+ " - comienza a cargar el ataque. (bloc 3)" + ANSI_RESET);
			return;
		}

		// -----------------------------
		// BLOC 4 : SECOND TURN (FROM CHARGING ATTACK) - DEFENDER NOT CHARGING
		// -----------------------------
		if (isAttackerCharging && !isDefenderCharging) {
			System.out.println(
					ANSI_PURPLE + "Probability - Charged attack execution (defender not charging)" + ANSI_RESET);

			handleChargedAttackExecution(accuracyFactor, atkAttacker, pkCombatting, pkFacing, "(bloc 4)");
			return;
		}

		// -----------------------------
		// BLO 5 : SECOND TURN (FROM CHARGING ATTACK) - DEFENDER IS CHARGING
		// -----------------------------
		if (isAttackerCharging && isDefenderCharging) {
			System.out.println(ANSI_PURPLE + "Probability - Charged vs Charged (attack avoided)" + ANSI_RESET);

			pkCombatting.setCanAttack(false);
			pkCombatting.setIsChargingAttackForNextRound(false);

			System.out.println(pkCombatting.getName() + " usó " + atkAttacker.getName() + ". " + pkFacing.getName()
					+ " evitó el ataque jijijija. (bloc 5)");
			return;
		}

		// -----------------------------
		// BLOC 6 : OTHER ATTACK AGAINST INVULNERABLE (CANNOT DO DAMMAGE)
		// -----------------------------
		if (!canHitInvulnerable && isDefenderCharging) {

			System.out.println(ANSI_PURPLE + "Probability - Normal attack fails vs invulnerable defender" + ANSI_RESET);

			pkCombatting.setCanAttack(false);
			pkCombatting.setIsChargingAttackForNextRound(false);

			System.out.println(pkCombatting.getName() + " usó " + atkAttacker.getName() + ". " + pkFacing.getName()
					+ " evitó el ataque jijijija. (bloc 6)");
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
	public void doAttackEffect() {

		float dmg = 0f;
		float dmgToSum = 0f;
		boolean isCritic;
		int nbTimesAttack;
		int nbTurnsHoldingStatus;
		int probabilityGettingStatus;
		int highProbabilityCritic;
		int setBaseDmgFromBegining;
		int randomRetreat = 0;
		float defenderInitialPs = 0f;
		float recoil = 0f;

		switch (this.getPkCombatting().getNextMovement().getId()) {
		// Destructor/Pound (tested)
		case 1:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Destructor");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Golpe kárate/Karate chop (tested)
		case 2:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Golpe kárate");

			dmg = doDammage();

			highProbabilityCritic = (int) (Math.random() * 100);

			// 40/100 of probabilities to have a critic attack
			if (highProbabilityCritic <= 40) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Doble bofetón/Double slap (tested)
		case 3:
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Doble bofetón");

				dmg = doDammage();

				isCritic = getCriticity();

				if (isCritic) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
					System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + ") : " + dmg);
				}

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Doble bofetón : " + nbTimesAttack);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño cometa/Comet punch (tested)
		case 4:
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Puño cometa");

				dmg = doDammage();

				isCritic = getCriticity();

				if (isCritic) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
					System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + ") : " + dmg);
				}

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Puño cometa : " + nbTimesAttack);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Megapuño/Mega punch (tested)
		case 5:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Megapuño");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Día de pago/Pay day (tested)
		case 6:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Día de pago");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}
			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño fuego/Fire punch (tested)
		case 7:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño fuego");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de quemar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 10) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

					nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);

					State burned = new State(StatusConditions.BURNED, nbTurnsHoldingStatus + 1);

					this.getPkFacing().setStatusCondition(burned);

					System.out.println(
							this.getPkFacing().getName() + " fue quemado por " + nbTurnsHoldingStatus + " turnos");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño hielo/Ice punch (tested)
		case 8:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño hielo");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			// Ice Pokemon cannot be frozen
			if (this.pkFacing.getTypes().stream().filter(t -> t.getId() == 9).findAny().get() == null) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				System.out.println("proba de congelar : " + probabilityGettingStatus);

				// 10% of probabilities to be frozen
				if (probabilityGettingStatus <= 10) {

					if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS
							&& !(this.getPkFacing().getStatusCondition()
									.getStatusCondition() == StatusConditions.FROZEN)) {

						nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);

						State frozen = new State(StatusConditions.FROZEN, nbTurnsHoldingStatus + 1);

						this.getPkFacing().setStatusCondition(frozen);

						System.out.println(this.getPkCombatting().getName() + " fue congelado por "
								+ nbTurnsHoldingStatus + " turnos");
					}
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño trueno/Thunder punch (tested)
		case 9:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño trueno");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			// Possibility of paralyzing Pokemon facing if is not already paralyzed and has
			// not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS
					&& !(this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.PARALYZED)) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				System.out.println("proba de paralizar : " + probabilityGettingStatus);

				if (probabilityGettingStatus <= 10) {

					nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);

					State paralyzed = new State(StatusConditions.PARALYZED, nbTurnsHoldingStatus + 1);

					this.getPkFacing().setStatusCondition(paralyzed);

					System.out.println(
							this.getPkFacing().getName() + " fue paralizado por " + nbTurnsHoldingStatus + " turnos");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Arañazo/Scratch (tested)
		case 10:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Arañazo");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Agarre/Vise grip (tested)
		case 11:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Agarre");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Guillotina/Guillotine (tested)
		case 12:
			System.out.println(this.getPkCombatting().getName() + " usó Guillotina");

			// One-Hit KO => Pokemon facing dies instantly
			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));

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

				highProbabilityCritic = (int) (Math.random() * 100);

				// 30/100 of probabilities to have a critic attack
				if (highProbabilityCritic <= 30) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
					System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + ") : " + dmg);
				}

				// Pokemon is no more charging an attack
				this.getPkCombatting().setIsChargingAttackForNextRound(false);

				this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
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

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Tornado/Gust (tested)
		case 16:
			// Gets the power from the beginning (to avoid variations after attacking)
			setBaseDmgFromBegining = this.getPkCombatting().getNextMovement().getPower();

			if (this.getPkCombatting().getNextMovement().getCanHitWhileInvulnerable()
					.contains(this.getPkFacing().getNextMovement().getId())) {
				this.getPkCombatting().getNextMovement()
						.setPower(this.getPkCombatting().getNextMovement().getPower() * 2);
			}

			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Tornado");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			// Puts again the same power base as the beginning
			this.getPkCombatting().getNextMovement().setPower(setBaseDmgFromBegining);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Ataque ala/Wing attack (tested)
		case 17:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Ataque ala");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Remolino/Whirlwind
		case 18:
			System.out.println(pkCombatting.getName() + " usó Remolino!");

			// Reducir PP
			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			// Si el rival no tiene más Pokémon -> no pasa nada (pero no falla)
			if (!this.getDefender().hasAvailableSwitch()) {
				System.out.println("Pero " + pkFacing.getName() + " no tiene más Pokémon para cambiar.");
				break;
			}

			// Force change
			this.getDefender().setForceSwitchPokemon(true);

			System.out.println("¡" + pkFacing.getName() + " fue arrastrado y obligado a retirarse!");
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

				isCritic = getCriticity();

				if (isCritic) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
					System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + ") : " + dmg);
				}

				// Pokemon is no more charging an attack
				this.getPkCombatting().setIsChargingAttackForNextRound(false);

				this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

				this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

				if (this.getPkFacing().getPs() <= 0) {

					this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				}
			}
			break;

		// Atadura/Bind (tested)
		case 20:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Atadura");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}
			// Check if the Pokemon facing doesn't have the status Trapped (is a status that
			// can be accumulated with other ephemeral status)
			if (!(this.getPkFacing().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPED))) {

				nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 4)) + 4);

				System.out.println(this.getPkFacing().getName() + " quedó atrapado");

				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);

				this.getPkFacing().addEstadoEfimero(trapped);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// It removes 12,5% of the initial PS every turn is trapped
			defenderInitialPs = this.getPkFacing().getInitialPs();
			dmg = defenderInitialPs * 0.125f;
			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Atizar/Slam (tested)
		case 21:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Atizar");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Látigo cepa/Vine whip (tested)
		case 22:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Látigo cepa");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Pisotón/Stomp (tested)
		case 23:
			// Gets the power from the beginning (to avoid variations after attacking)
			setBaseDmgFromBegining = this.getPkCombatting().getNextMovement().getPower();

			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Pisotón");

			// If Pokemon facing has used minimize, set power base of the attack x2
			if (this.getPkFacing().getHasUsedMinimize()) {
				this.getPkCombatting().getNextMovement()
						.setPower(this.getPkCombatting().getNextMovement().getPower() * 2);
			}

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			randomRetreat = (int) (Math.random() * 100);

			// 30% of probabilities to retreat Pokemon facing
			if (randomRetreat <= 30) {

				this.getPkFacing().setHasRetreated(true);
			}

			// Puts again the same power base as the beginning
			this.getPkCombatting().getNextMovement().setPower(setBaseDmgFromBegining);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Doble patada/Double kick (tested)
		case 24:
			// Attacks 2 times
			nbTimesAttack = 2;

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Doble patada");

				dmg = doDammage();

				isCritic = getCriticity();

				if (isCritic) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
					System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + ") : " + dmg);
				}

				dmgToSum += dmg;
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Megapatada/Mega kick (tested)
		case 25:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Megapatada");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Patada salto/Jump kick (TODO >> No Pokemon has actually)
		case 26:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Patada salto");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Patada giro/Rolling kick (tested)
		case 27:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Patada giro");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			randomRetreat = (int) (Math.random() * 100);

			// 30% of probabilities to retreat Pokemon facing
			if (randomRetreat <= 30) {

				this.getPkFacing().setHasRetreated(true);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Ataque arena /Sand attack (tested)
		case 28:
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

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			break;

		// Golpe cabeza/Headbutt (tested)
		case 29:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Golpe cabeza");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			randomRetreat = (int) (Math.random() * 100);

			// 30% of probabilities to retreat Pokemon facing
			if (randomRetreat <= 30) {

				this.getPkFacing().setHasRetreated(true);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Cornada/Horn attack (tested)
		case 30:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Cornada");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Ataque furia/Fury attack (tested)
		case 31:
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Ataque furia");

				dmg = doDammage();

				isCritic = getCriticity();

				if (isCritic) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
					System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + ") : " + dmg);
				}

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Ataque furia : " + nbTimesAttack);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Perforador/Horn drill (tested)
		case 32:
			System.out.println(this.getPkCombatting().getName() + " usó Perforador");

			// One-Hit KO => Pokemon facing dies instantly
			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));

			break;

		// Placaje/Tackle (tested)
		case 33:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Placaje");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Golpe cuerpo/Body slam (tested)
		case 34:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Golpe cuerpo");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			// Possibility of paralyzing Pokemon facing if is not already paralyzed and has
			// not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS
					&& !(this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.PARALYZED)) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				System.out.println("proba de paralizar : " + probabilityGettingStatus);

				if (probabilityGettingStatus <= 30) {

					nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);

					State paralyzed = new State(StatusConditions.PARALYZED, nbTurnsHoldingStatus + 1);

					this.getPkFacing().setStatusCondition(paralyzed);

					System.out.println(
							this.getPkFacing().getName() + " fue paralizado por " + nbTurnsHoldingStatus + " turnos");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Constricción/Wrap (tested)
		case 35:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Constricción");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}
			// Check if the Pokemon facing doesn't have the status Trapped (is a status that
			// can be accumulated with other ephemeral status)
			if (!(this.getPkFacing().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPED))) {

				nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 4)) + 4);

				System.out.println(this.getPkFacing().getName() + " quedó atrapado");

				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);

				this.getPkFacing().addEstadoEfimero(trapped);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// It removes 12,5% of the initial PS every turn is trapped
			defenderInitialPs = this.getPkFacing().getInitialPs();
			dmg = defenderInitialPs * 0.125f;
			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Derribo/Take down (tested)
		case 36:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Derribo");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}

			// ---- RECOIL ----
			recoil = dmg * 0.25f;

			this.getPkCombatting().setPs(this.getPkCombatting().getPs() - recoil);

			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId()
					+ ") se dañó a sí mismo por el retroceso (" + recoil + ")");

			// Check if attacker debilitated by recoil
			if (this.getPkCombatting().getPs() <= 0) {
				this.getPkCombatting().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Saña/Thrash
		case 37:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Saña");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			if (!(this.getPkCombatting().getEphemeralStates().stream()
					.anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPEDBYOWNATTACK))) {

				nbTurnsHoldingStatus = (int) ((Math.random() * (3 - 2)) + 2);

				System.out.println(this.getPkCombatting().getName() + " usará el mismo ataque durante "
						+ nbTurnsHoldingStatus + " turnos.");

				State trappedByOwnAttack = new State(StatusConditions.TRAPPEDBYOWNATTACK, nbTurnsHoldingStatus + 1);

				this.getPkCombatting().addEstadoEfimero(trappedByOwnAttack);
			}
			break;

		// Doble filo/Dobule-Edge (tested)
		case 38:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Doble filo");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}

			// ---- RECOIL ----
			recoil = dmg * 0.33f;

			this.getPkCombatting().setPs(this.getPkCombatting().getPs() - recoil);

			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId()
					+ ") se dañó a sí mismo por el retroceso (" + recoil + ")");

			// Check if attacker debilitated by recoil
			if (this.getPkCombatting().getPs() <= 0) {
				this.getPkCombatting().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Látigo/Tail Whip (tested)
		case 39:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Látigo");

			if (this.getPkFacing().getDefenseStage() <= -6) {
				System.out.println("La defensa de " + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
			} else {
				this.getPkFacing().setDefenseStage(Math.max(this.getPkFacing().getDefenseStage() - 1, -6));
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
						+ " bajó su defensa!");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			break;

		// Picotazo veneno/Poison sting (tested)
		case 40:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Picotazo veneno");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de envenenar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 30) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

					nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);

					State poisoned = new State(StatusConditions.POISONED, nbTurnsHoldingStatus + 1);

					this.getPkFacing().setStatusCondition(poisoned);

					System.out.println(
							this.getPkFacing().getName() + " fue envenenado por " + nbTurnsHoldingStatus + " turnos");
				}
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Doble ataque/Twineedle (tested)
		case 41:
			// Attacks 2 times
			nbTimesAttack = 2;

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Doble ataque");

				dmg = doDammage();

				isCritic = getCriticity();

				if (isCritic) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
					System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + ") : " + dmg);
				}

				dmgToSum += dmg;
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de envenenar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 20) {

				// Check if the Pokemon facing has no status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

					nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);

					State poisoned = new State(StatusConditions.POISONED, nbTurnsHoldingStatus + 1);

					this.getPkFacing().setStatusCondition(poisoned);

					System.out.println(
							this.getPkFacing().getName() + " fue envenenado por " + nbTurnsHoldingStatus + " turnos");
				}
			}

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Pin misil/Pin missile (tested)
		case 42:
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			for (int i = 0; i < nbTimesAttack; i++) {
				System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
						+ " usó Pin misil");

				dmg = doDammage();

				isCritic = getCriticity();

				if (isCritic) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
					System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
							+ this.getPkFacing().getId() + ")" + ") : " + dmg);
				}

				dmgToSum += dmg;
			}

			System.out.println("nº de veces que se usó Pin misil : " + nbTimesAttack);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmgToSum);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Malicioso/Leer (tested)
		case 43:
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

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			break;

		// Mordisco/Bite (tested)
		case 44:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Mordisco");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			randomRetreat = (int) (Math.random() * 100);

			// 30% of probabilities to retreat Pokemon facing
			if (randomRetreat <= 30) {

				this.getPkFacing().setHasRetreated(true);
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Gruñido/Growl (tested)
		case 45:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Gruñido");

			if (this.getPkFacing().getAttackStage() <= -6) {
				System.out.println("El ataque de " + this.getPkCombatting().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + " no puede bajar más!");
			} else {
				this.getPkFacing().setAttackStage(Math.min(this.getPkFacing().getAttackStage() - 1, -6));
				System.out.println(this.getPkFacing().getName() + " (Id:" + this.getPkFacing().getId() + ")"
						+ " bajó su defensa!");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			break;

		// Forcejeo/Struggle
		case 165:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Forcejeo");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				System.out.println("Damage to Pokemon facing with critic (" + this.getPkFacing().getName() + " (Id:"
						+ this.getPkFacing().getId() + ")" + ") : " + dmg);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			// Pokemon combating receives 25% of damage from his PS remaining
			this.getPkCombatting()
					.setPs(this.getPkCombatting().getInitialPs() - (this.getPkCombatting().getInitialPs() * 0.25f));

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}

			// Get status debilitated for Pokemon combating
			if (this.getPkCombatting().getPs() <= 0) {

				this.getPkCombatting().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;
		}
	}

	// -----------------------------
	// Apply damage
	// -----------------------------
	public float doDammage() {

		// There is a random variation when attacking (the total damage is not the same
		// every time)
		int randomVariation = (int) ((Math.random() * (100 - 85)) + 85);

		boolean isSpecialAttack = this.getPkCombatting().getNextMovement().getBases().contains("especial");

		float dmg = 0;

//		System.out.println(ANSI_PURPLE + "Bonus : " + this.getPkCombatting().getNextMovement().getBonus() + ANSI_RESET);
//		System.out.println(ANSI_PURPLE + "Effectiveness : " + this.getPkCombatting().getNextMovement().getEffectivenessAgainstPkFacing() + ANSI_RESET);
//		System.out.println(ANSI_PURPLE + "Effective attack : " + this.getPkCombatting().getEffectiveAttack() + ANSI_RESET);
//		System.out.println(ANSI_PURPLE + "Power : " + this.getPkCombatting().getNextMovement().getPower() + ANSI_RESET);
//		System.out.println(ANSI_PURPLE + "Def Pk facing : " + this.getPkFacing().getDef() + ANSI_RESET);
//		System.out.println(ANSI_PURPLE + "Variation : " + randomVariation + ANSI_RESET);

		if (isSpecialAttack) {
			// Apply special damage
			dmg = 0.01f * this.getPkCombatting().getNextMovement().getBonus()
					* this.getPkCombatting().getNextMovement().getEffectivenessAgainstPkFacing() * randomVariation
					* (((0.2f * 100f + 1f) * this.getPkCombatting().getEffectiveSpecialAttack()
							* this.getPkCombatting().getNextMovement().getPower())
							/ (25f * this.getPkFacing().getSpecialDefense()) + 2f);

			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + " (Id:"
					+ this.getPkFacing().getId() + ")" + ") : " + dmg);

			// Apply normal damage
		} else {

			dmg = 0.01f * this.getPkCombatting().getNextMovement().getBonus()
					* this.getPkCombatting().getNextMovement().getEffectivenessAgainstPkFacing() * randomVariation
					* (((0.2f * 100f + 1f) * this.getPkCombatting().getEffectiveAttack()
							* this.getPkCombatting().getNextMovement().getPower())
							/ (25f * this.getPkFacing().getEffectiveDefense()) + 2f);

			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + " (Id:"
					+ this.getPkFacing().getId() + ")" + ") : " + dmg);
		}

		return dmg;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage)
	// -----------------------------
	public boolean getCriticity() {
		boolean isCritic = false;

		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 10) {

			isCritic = true;
		}

		return isCritic;
	}
}
