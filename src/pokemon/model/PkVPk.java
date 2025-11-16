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

		// Get if Pokemon are charging an attack
		boolean isAttackerChargingAttack = this.getPkCombatting().getIsChargingAttackForNextRound();
		boolean isDefenderChargingAttack = this.getPkFacing().getIsChargingAttackForNextRound();

		// Get next movement from Pokemon
		Attack atkAttacker = this.getPkCombatting().getNextMouvement();
		Attack atkDefender = this.getPkFacing().getNextMouvement();

		// Get if defender is using a special attack and can be hit meanwhile
		boolean canHitInvulnerable = atkAttacker.getCanHitWhileInvulnerable().contains(atkDefender.getId());

		// Calculates "accuracy factor"
		float a = (atkAttacker.getPrecision() / 100.0f)
				* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));

		// ------------------------------------------------------
		// Normal attack
		// ------------------------------------------------------
		// Normal conditions from both
		if (atkAttacker.getCategory() == AttackCategory.NORMAL && !isDefenderChargingAttack) {

			System.out.println(ANSI_PURPLE + "Probability of attacking - Normal condition" + ANSI_RESET);

			// Can attack
			if (a >= 1.0f) {

				this.getPkCombatting().setCanAttack(true);
			} else {

				int randomEfectivity = (int) (Math.random() * 100);

				// Can attack
				if ((randomEfectivity / 100.0f) <= a) {

					this.getPkCombatting().setCanAttack(true);

					// fail -> reduce PP and notify
				} else {

					this.getPkCombatting().getNextMouvement()
							.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

					this.getPkCombatting().setCanAttack(false);

					System.out.println(this.getPkCombatting().getName() + " usó " + atkAttacker.getName() + ". "
							+ this.getPkFacing().getName() + " evitó el ataque jijijija. (1)" + ANSI_RESET);
				}
			}
		}

		// ------------------------------------------------------
		// Normal attack
		// ------------------------------------------------------
		// Combatant will use a charged attack
		if (atkAttacker.getCategory() == AttackCategory.CHARGED && !isAttackerChargingAttack) {

			System.out.println(
					ANSI_PURPLE + "Probability of attacking - Combatant will use a charged attack" + ANSI_RESET);

			this.getPkCombatting().setCanAttack(true);

			System.out.println(ANSI_PURPLE + this.getPkCombatting().getName() + " utilizará " + atkAttacker.getName()
					+ " - prepara un ataque cargado" + ANSI_RESET);
		}

		// ------------------------------------------------------
		// Normal attack (can hit special attack)
		// ------------------------------------------------------
		// Apply an attack from combatant that can hit defender (while defender is
		// charging) => ex : "Tornado" can hit "Vuelo"
		else if (canHitInvulnerable && isDefenderChargingAttack) {

			System.out.println(
					ANSI_PURPLE + "Probability of attacking - Apply normal attack that can hit (defender is charging)"
							+ ANSI_RESET);

			// Can attack
			if (a >= 1.0f) {

				this.getPkCombatting().setCanAttack(true);
			} else {

				int randomEfectivity = (int) (Math.random() * 100);

				// Can attack
				if ((randomEfectivity / 100.0f) <= a) {

					this.getPkCombatting().setCanAttack(true);

					// fail -> reduce PP and notify
				} else {

					this.getPkCombatting().getNextMouvement()
							.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

					this.getPkCombatting().setCanAttack(false);

					System.out.println(ANSI_PURPLE + this.getPkCombatting().getName() + " usó " + atkAttacker.getName()
							+ ". " + this.getPkFacing().getName() + " evitó el ataque jijijija. (2)" + ANSI_RESET);
				}
			}
		}

		// ------------------------------------------------------
		// Special attack (two turns)
		// ------------------------------------------------------
		// Apply charged attack from combatant (defender not charging)
		else if (isAttackerChargingAttack && !isDefenderChargingAttack) {

			System.out.println(ANSI_PURPLE
					+ "Probability of attacking - Charged attack from combatant (defender not charging)" + ANSI_RESET);

			// Can attack
			if (a >= 1.0f) {

				this.getPkCombatting().setCanAttack(true);
			} else {

				int randomEfectivity = (int) (Math.random() * 100);

				// Can attack
				if ((randomEfectivity / 100.0f) <= a) {

					this.getPkCombatting().setCanAttack(true);

					// fail -> reduce PP and notify
				} else {

					this.getPkCombatting().getNextMouvement()
							.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

					this.getPkCombatting().setCanAttack(false);

					this.getPkCombatting().setIsChargingAttackForNextRound(false);

					System.out.println(ANSI_PURPLE + this.getPkCombatting().getName() + " usó " + atkAttacker.getName()
							+ ". " + this.getPkFacing().getName() + " evitó el ataque jijijija. (3)" + ANSI_RESET);
				}
			}
		}

		// ------------------------------------------------------
		// Special attack (two turns)
		// ------------------------------------------------------
		// Apply charged attack from combatant (defender is charging)
		else if (isAttackerChargingAttack && isDefenderChargingAttack) {

			System.out.println(ANSI_PURPLE
					+ "Probability of attacking - Charged attack from combatant (defender is charging)" + ANSI_RESET);

			this.getPkCombatting().setCanAttack(false);

			this.getPkCombatting().setIsChargingAttackForNextRound(false);

			// Pokemon defender avoided the attack
			System.out.println(ANSI_PURPLE + this.getPkCombatting().getName() + " usó " + atkAttacker.getName() + ". "
					+ this.getPkFacing().getName() + " evitó el ataque jijijija. (4)" + ANSI_RESET);
		}

		// ------------------------------------------------------
		// Special attack (two turns)
		// ------------------------------------------------------
		// Apply other attack from combatant (defender is charging) => avoid the attack
		else if (!canHitInvulnerable && isDefenderChargingAttack) {

			System.out.println(ANSI_PURPLE
					+ "Probability of attacking - Apply other attack from combatant (defender is charging) => avoid the attack"
					+ ANSI_RESET);

			this.getPkCombatting().setCanAttack(false);

			this.getPkCombatting().setIsChargingAttackForNextRound(false);

			// Pokemon defender avoided the attack
			System.out.println(ANSI_PURPLE + this.getPkCombatting().getName() + " usó " + atkAttacker.getName() + ". "
					+ this.getPkFacing().getName() + " evitó el ataque jijijija. (5)" + ANSI_RESET);
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

		switch (this.getPkCombatting().getNextMouvement().getId()) {
		// Destructor (tested)
		case 1:
			System.out.println(this.getPkCombatting().getName() + " usó Destructor");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Golpe kárate (tested)
		case 2:
			System.out.println(this.getPkCombatting().getName() + " usó Golpe kárate");

			dmg = doDammage();

			highProbabilityCritic = (int) (Math.random() * 100);

			// 10/100 of probabilities to have a critic attack
			if (highProbabilityCritic <= 40) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Doble bofetón (tested)
		case 3:
			System.out.println(this.getPkCombatting().getName() + " usó Doble bofetón");

			dmg = doDammage();

			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			System.out.println(" nº de veces : " + nbTimesAttack);

			dmg = dmg * nbTimesAttack;
			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño cometa (tested)
		case 4:
			System.out.println(this.getPkCombatting().getName() + " usó Puño cometa");

			dmg = doDammage();

			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);

			System.out.println(" nº de veces : " + nbTimesAttack);

			dmg = dmg * nbTimesAttack;

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Megapuño (tested)
		case 5:
			System.out.println(this.getPkCombatting().getName() + " usó Megapuño");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Día de pago (tested)
		case 6:
			System.out.println(this.getPkCombatting().getName() + " usó Día de pago");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño fuego (tested)
		case 7:
			System.out.println(this.getPkCombatting().getName() + " usó Puño fuego");

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

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño hielo
		case 8:
			System.out.println(this.getPkCombatting().getName() + " usó Puño hielo");

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

					if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {

						nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);

						State frozen = new State(StatusConditions.FROZEN, nbTurnsHoldingStatus);

						this.getPkFacing().setStatusCondition(frozen);

						System.out.println(this.getPkCombatting().getName() + " fue congelado por "
								+ nbTurnsHoldingStatus + " turnos");
					}
				}
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Puño trueno (tested)
		case 9:
			System.out.println(this.getPkCombatting().getName() + " usó Puño trueno");

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

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Arañazo (tested)
		case 10:
			System.out.println(this.getPkCombatting().getName() + " usó Arañazo");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Agarre (tested)
		case 11:
			System.out.println(this.getPkCombatting().getName() + " usó Agarre");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Guillotina
		case 12:
			System.out.println(this.getPkCombatting().getName() + " usó Guillotina");
			break;

		// Viento cortante
		case 13:
			if (!this.getPkCombatting().getIsChargingAttackForNextRound()) {

				// This attack requires to charge first time for one round
				System.out.println(this.getPkCombatting().getName() + " se prepara para Viento cortante");

				this.getPkCombatting().setIsChargingAttackForNextRound(true);

			} else {

				System.out.println(this.getPkCombatting().getName() + " usó Viento cortante");

				dmg = doDammage();

				highProbabilityCritic = (int) (Math.random() * 100);

				// 10/100 of probabilities to have a critic attack
				if (highProbabilityCritic <= 40) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
				}

				// Pokemon is no more charging an attack
				this.getPkCombatting().setIsChargingAttackForNextRound(false);

				this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Corte (tested)
		case 15:
			System.out.println(this.getPkCombatting().getName() + " usó Corte");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Tornado (tested)
		case 16:
			// Gets the power from the beginning (to avoid variations after attacking)
			setBaseDmgFromBegining = this.getPkCombatting().getNextMouvement().getPower();

			this.getPkCombatting().getNextMouvement()
					.setPower(this.getPkCombatting().getNextMouvement().getPower() * 2);

			System.out.println(this.getPkCombatting().getName() + " usó Tornado");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			// Puts again the same power base as the beginning
			this.getPkCombatting().getNextMouvement().setPower(setBaseDmgFromBegining);

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Ataque ala (tested)
		case 17:
			System.out.println(this.getPkCombatting().getName() + " usó Ataque ala");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Vuelo
		case 19:
			// If not charging => first turn charge the attack
			if (!this.getPkCombatting().getIsChargingAttackForNextRound()) {

				// This attack requires to charge first time for one round
				System.out.println(this.getPkCombatting().getName() + " voló muy alto");

				this.getPkCombatting().setIsChargingAttackForNextRound(true);

				// Apply damage => second turn
			} else {

				System.out.println(this.getPkCombatting().getName() + " usó Vuelo");

				dmg = doDammage();

				isCritic = getCriticity();

				if (isCritic) {

					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
				}

				// Pokemon is no more charging an attack
				this.getPkCombatting().setIsChargingAttackForNextRound(false);

				this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

				this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

				if (this.getPkFacing().getPs() <= 0) {

					this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				}
			}
			break;

		// Atadura
		case 20:
			System.out.println(this.getPkCombatting().getName() + " usó Atadura");

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
			System.out.println(this.getPkCombatting().getName() + " usó Atizar");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Látigo cepa (tested)
		case 22:
			System.out.println(this.getPkCombatting().getName() + " usó Látigo cepa");

			dmg = doDammage();

			isCritic = getCriticity();

			if (isCritic) {

				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);

			if (this.getPkFacing().getPs() <= 0) {

				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
			break;

		// Pisotón
		case 23:
			System.out.println(this.getPkCombatting().getName() + " usó Pisotón");
			break;
		}
	}

	// Apply damage
	public float doDammage() {

		// There is a random variation when attacking (the total damage is not the same
		// every time)
		int randomVariation;

		randomVariation = (int) ((Math.random() * (100 - 85)) + 85);

		boolean isSpecialAttack = this.getPkCombatting().getNextMouvement().getBases().contains("especial");

		float dmg = 0;

		if (isSpecialAttack) {

			// Apply special damage
			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonus()
					* this.getPkCombatting().getNextMouvement().getEffectivenessAgainstPkFacing() * randomVariation
					* (((0.2f * 100 + 1) * this.getPkCombatting().getSpecialAttack()
							* this.getPkCombatting().getNextMouvement().getPower())
							/ (25 * this.getPkFacing().getSpecialDefense()) + 2);

			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + ") : " + dmg);

			// Apply normal damage
		} else {

			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonus()
					* this.getPkCombatting().getNextMouvement().getEffectivenessAgainstPkFacing() * randomVariation
					* (((0.2f * 100 + 1) * this.getPkCombatting().getAttack()
							* this.getPkCombatting().getNextMouvement().getPower()) / (25 * this.getPkFacing().getDef())
							+ 2);

			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + ") : " + dmg);
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
