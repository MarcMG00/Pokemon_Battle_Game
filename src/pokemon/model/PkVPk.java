package pokemon.model;

import pokemon.enums.AttackCategory;
import pokemon.enums.StatusConditions;

public class PkVPk {
	private Pokemon pkCombatting;
	private Pokemon pkFacing;

	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	private static final String ANSI_RESET = "\u001B[0m";

	public PkVPk(Pokemon pkAttacking, Pokemon pkFacing) {
		this.pkCombatting = pkAttacking;
		this.pkFacing = pkFacing;
	}

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

	// Knows the evasion or accuracy for the Pokemon selected (1 is for accuracy, 2
	// is for evasion)
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
			resultEvAcu = 3 / 9;
			break;
		case -5:
			resultEvAcu = 3 / 8;
			break;
		case -4:
			resultEvAcu = 3 / 7;
			break;
		case -3:
			resultEvAcu = 3 / 6;
			break;
		case -2:
			resultEvAcu = 3 / 5;
			break;
		case -1:
			resultEvAcu = 3 / 4;
			break;
		case 1:
			resultEvAcu = 4 / 3;
			break;
		case 2:
			resultEvAcu = 5 / 3;
			break;
		case 3:
			resultEvAcu = 6 / 3;
			break;
		case 4:
			resultEvAcu = 7 / 3;
			break;
		case 5:
			resultEvAcu = 8 / 3;
			break;
		case 6:
			resultEvAcu = 9 / 3;
			break;
		}

		return resultEvAcu;
	}

	// Gets the probability of attacking
	public void getProbabilityOfAttacking() {

		boolean isAttackerCharging = this.getPkCombatting().getIsChargingAttackForNextRound();
		boolean isDefenderCharging = this.getPkFacing().getIsChargingAttackForNextRound();

		Attack atkAttacker = this.getPkCombatting().getNextMovement();
		Attack atkDefender = this.getPkFacing().getNextMovement();

		boolean canHitInvulnerable = atkAttacker.getCanHitWhileInvulnerable().contains(atkDefender.getId());

		float accuracyFactor = 0f;

		// Guillotina
		if (atkAttacker.getId() == 12) {
			accuracyFactor = (atkAttacker.getPrecision() / 100f); // don't take into account Pokemon levels (cause all are on the same lvl)
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
			atk.setPp(atk.getPp() - 1);
			attacker.setCanAttack(false);

			System.out.println(attacker.getName() + " usó " + atk.getName() + ". " + defender.getName()
					+ " evitó el ataque jijijija. " + code);
		}
	}

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

	// Gets the attack effect and apply damage
	public void doAttackEffect() {

		float dmg = 0;
		boolean isCritic;
		int nbTimesAttack;
		int nbTurnsHoldingStatus;
		int probabilityGettingStatus;
		int highProbabilityCritic;
		int setBaseDmgFromBegining;

		switch (this.getPkCombatting().getNextMovement().getId()) {
		// Destructor (tested)
		case 1:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Destructor");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Golpe kárate (tested)
		case 2:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Golpe kárate");

			dmg = doDammage();

			highProbabilityCritic = (int) (Math.random() * 100);

			// 10/100 of probabilities to have a critic attack
			if (highProbabilityCritic <= 40) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Doble bofetón (tested)
		case 3:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Doble bofetón");

			dmg = doDammage();

			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			System.out.println(" nº de veces : " + nbTimesAttack);

			dmg = dmg * nbTimesAttack;
			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño cometa (tested)
		case 4:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño cometa");

			dmg = doDammage();

			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			System.out.println(" nº de veces : " + nbTimesAttack);

			dmg = dmg * nbTimesAttack;

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Megapuño (tested)
		case 5:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Megapuño");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Día de pago (tested)
		case 6:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Día de pago");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño fuego (tested)
		case 7:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño fuego");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			probabilityGettingStatus = (int) (Math.random() * 100);

			System.out.println("proba de quemar : " + probabilityGettingStatus);

			if (probabilityGettingStatus <= 10) {

				// Check if the Pokemon facing isn't without any status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

					nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);

					State burned = new State(StatusConditions.BURNED, nbTurnsHoldingStatus);

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

		// Puño hielo (tested)
		case 8:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño hielo");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
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

						State frozen = new State(StatusConditions.FROZEN, nbTurnsHoldingStatus);

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

		// Puño trueno (tested)
		case 9:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Puño trueno");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			// Possibility of paralyzing Pokemon facing if is not already paralyzed and has
			// not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS
					&& !(this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.PARALYZED)) {

				probabilityGettingStatus = (int) (Math.random() * 100);

				System.out.println("proba de paralizar : " + probabilityGettingStatus);

				if (probabilityGettingStatus <= 10) {

					nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);

					State paralyzed = new State(StatusConditions.PARALYZED, nbTurnsHoldingStatus);

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

		// Arañazo (tested)
		case 10:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Arañazo");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Agarre (tested)
		case 11:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Agarre");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Guillotina (tested)
		case 12:
			System.out.println(this.getPkCombatting().getName() + " usó Guillotina");

			// One-Hit KO => Pokemon facing dies instantly
			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);
			this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));

			break;

		// Viento cortante (tested)
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

		// Corte (tested)
		case 15:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Corte");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Tornado (tested)
		case 16:
			// Gets the power from the beginning (to avoid variations after attacking)
			setBaseDmgFromBegining = this.getPkCombatting().getNextMovement().getPower();

			this.getPkCombatting().getNextMovement().setPower(this.getPkCombatting().getNextMovement().getPower() * 2);

			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Tornado");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			// Puts again the same power base as the beginning
			this.getPkCombatting().getNextMovement().setPower(setBaseDmgFromBegining);

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Ataque ala (tested)
		case 17:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Ataque ala");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Vuelo (tested)
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

		// Atadura
		case 20:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Atadura");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			// Check if the Pokemon facing doesn't have the estado Atrapado
			if ((this.getPkFacing().getEphemeralStates().stream()
					.filter(e -> e.getStatusCondition() == StatusConditions.TRAPPED).findAny().get()) != null) {

				nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 4)) + 4);

				System.out.println(this.getPkCombatting().getName() + " quedó atrapado");

				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus);

				this.getPkFacing().addEstadoEfimero(trapped);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Atizar (tested)
		case 21:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Atizar");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Látigo cepa (tested)
		case 22:
			System.out.println(this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")"
					+ " usó Látigo cepa");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMovement().setPp(this.getPkCombatting().getNextMovement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Pisotón
		case 23:
			System.out.println(
					this.getPkCombatting().getName() + " (Id:" + this.getPkCombatting().getId() + ")" + " usó Pisotón");
			break;
		}
	}

	// Apply damage
	public float doDammage() {

		// There is a random variation when attacking (the total damage is not the same
		// every time)
		int randomVariation;

		randomVariation = (int) ((Math.random() * (100 - 85)) + 85);

		boolean isSpecialAttack = this.getPkCombatting().getNextMovement().getBases().contains("especial");

		float dmg = 0;

		if (isSpecialAttack) {

			// Apply special damage
			dmg = 0.01f * this.getPkCombatting().getNextMovement().getBonus()
					* this.getPkCombatting().getNextMovement().getEffectivenessAgainstPkFacing() * randomVariation
					* (((0.2f * 100 + 1) * this.getPkCombatting().getSpecialAttack()
							* this.getPkCombatting().getNextMovement().getPower())
							/ (25 * this.getPkFacing().getSpecialDefense()) + 2);

			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + " (Id:"
					+ this.getPkFacing().getId() + ")" + ") : " + dmg);

			// Apply normal damage
		} else {

			dmg = 0.01f * this.getPkCombatting().getNextMovement().getBonus()
					* this.getPkCombatting().getNextMovement().getEffectivenessAgainstPkFacing() * randomVariation
					* (((0.2f * 100 + 1) * this.getPkCombatting().getAttack()
							* this.getPkCombatting().getNextMovement().getPower()) / (25 * this.getPkFacing().getDef())
							+ 2);

			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + " (Id:"
					+ this.getPkFacing().getId() + ")" + ") : " + dmg);
		}

		return dmg;
	}

	public void doEffectOther() {

	}

	// Gets if an attack is critic (x2 of damage)
	public boolean getCriticity() {
		boolean isCritic = false;

		int randomCritic = (int) (Math.random() * 100);

		// 10/100 of probability to have a critic attack
		if (randomCritic <= 10) {

			isCritic = true;
		}

		return isCritic;
	}

}
