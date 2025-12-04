package pokemon.model;

import java.util.ArrayList;

import pokemon.enums.StatusConditions;

public class State {

	// ==================================== FIELDS
	// ====================================

	private StatusConditions statusCondition;
	private boolean canMoveEphemeralState;
	private boolean canMoveStatusCondition;
	private int nbTurns;
	private float PSReduction;
	private int percentToBeDefrosted;

	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_RESET = "\u001B[0m";

	// ==================================== CONSTRUCTORS
	// ====================================

	public State() {
		this.statusCondition = StatusConditions.NO_STATUS;
		this.canMoveEphemeralState = true;
		this.nbTurns = 0;
		this.PSReduction = 0;
		this.percentToBeDefrosted = 10;
	}

	public State(StatusConditions estadoEnum, int numTurnos) {
		this.statusCondition = estadoEnum;
		this.canMoveEphemeralState = true;
		this.canMoveStatusCondition = true;
		this.nbTurns = numTurnos;
		this.PSReduction = 0;
		this.percentToBeDefrosted = 10;
	}

	public State(StatusConditions estadoEnum) {
		this.statusCondition = estadoEnum;
		this.canMoveEphemeralState = true;
		this.canMoveStatusCondition = true;
		this.nbTurns = 0;
		this.PSReduction = 0;
		this.percentToBeDefrosted = 10;
	}

	// ==================================== GETTERS/SETTERS
	// ====================================

	public StatusConditions getStatusCondition() {
		return statusCondition;
	}

	public void setStatusCondition(StatusConditions estadoEnum) {
		this.statusCondition = estadoEnum;
	}

	public boolean getCanMoveEphemeralState() {
		return canMoveEphemeralState;
	}

	public void setCanMoveEphemeralState(boolean puedeMoverseEstadoPersistente) {
		this.canMoveEphemeralState = puedeMoverseEstadoPersistente;
	}

	public boolean getCanMoveStatusCondition() {
		return canMoveStatusCondition;
	}

	public void setCanMoveStatusCondition(boolean puedeMoverseEstadoEfimero) {
		this.canMoveStatusCondition = puedeMoverseEstadoEfimero;
	}

	public int getNbTurns() {
		return nbTurns;
	}

	public void setNbTurns(int nbTurns) {
		this.nbTurns = nbTurns;
	}

	public float getPSReduction() {
		return PSReduction;
	}

	public void setPSReduction(float PSReduction) {
		this.PSReduction = PSReduction;
	}

	public int getPercentToBeDefrosted() {
		return percentToBeDefrosted;
	}

	public void setPercentToBeDefrosted(int percentToBeDefrosted) {
		this.percentToBeDefrosted = percentToBeDefrosted;
	}

	// ==================================== METHODS
	// ====================================

	// -----------------------------
	// Sets the amount of damage, mobility and effects at the end/beginning of each
	// turn
	// -----------------------------
	public void doEffectStatusCondition(StatusConditions status) {

		int getRidOfStatusProbability = (int) (Math.random() * 100);
		int attackProbability = (int) (Math.random() * 100);

		switch (status) {
		case PERISH_SONG:
			break;
		case FROZEN:
			if (this.getNbTurns() == 0) {

				this.setStatusCondition(StatusConditions.NO_STATUS);
				this.setCanMoveStatusCondition(true);
				this.setPercentToBeDefrosted(10);

				System.out.println(
						ANSI_CYAN + "Se terminó el número de turnos congelado, ya puede volver a atacar" + ANSI_RESET);

			} else {

				// Only can be thawed if probability <= 10% (at the beginning) => after each
				// turn, it goes to +10%
				if (getRidOfStatusProbability <= this.getPercentToBeDefrosted()) {

					this.setNbTurns(0);
					this.setStatusCondition(StatusConditions.NO_STATUS);
					this.setCanMoveStatusCondition(true);
					this.setPercentToBeDefrosted(10);

					System.out.println(ANSI_CYAN + "Se descongeló (proba inf a " + this.getPercentToBeDefrosted()
							+ ") : " + getRidOfStatusProbability + ANSI_RESET);

				} else {

					this.setNbTurns(this.getNbTurns() - 1);
					this.setCanMoveStatusCondition(false);
					this.setPercentToBeDefrosted(this.getPercentToBeDefrosted() + 10);

					System.out.println(ANSI_CYAN + "No se descongeló (proba sup a " + this.getPercentToBeDefrosted()
							+ ") : " + getRidOfStatusProbability + ANSI_RESET);
					System.out.println(ANSI_CYAN + "Faltan " + this.getNbTurns()
							+ " turnos (congelado - no puede atacar)" + ANSI_RESET);
				}
			}
			break;
		case ASLEEP:
			break;
		case SEEDED:
			break;
		case INFATUATED:
			break;
		case POISONED:
			break;
		case BADLY_POISONED:
			break;
		case CURSED:
			break;
		case PARALYZED:
			if (this.getNbTurns() == 0) {

				this.setStatusCondition(StatusConditions.NO_STATUS);
				this.setCanMoveStatusCondition(true);

				System.out.println(ANSI_CYAN + "Ya no está paralizado" + ANSI_RESET);

			} else {

				// Only can attack if proba <= 25%
				if (attackProbability <= 25) {

					this.setNbTurns(this.getNbTurns() - 1);
					this.setCanMoveStatusCondition(true);

					System.out.println(ANSI_CYAN + "Faltan " + this.getNbTurns()
							+ " turnos (paralizado - puede atacar): " + ANSI_RESET);

				} else {

					this.setNbTurns(this.getNbTurns() - 1);
					this.setCanMoveStatusCondition(false);

					System.out.println(ANSI_CYAN + "Faltan " + this.getNbTurns()
							+ " turnos (paralizado - no puede atacar): " + ANSI_RESET);
				}
			}
			break;
		case BURNED:
			// In all cases, can attack. This is just a reminder in case of problems
			if (this.getNbTurns() == 0) {

				this.setStatusCondition(StatusConditions.NO_STATUS);
				this.setCanMoveStatusCondition(true);

			} else {

				this.setNbTurns(this.getNbTurns() - 1);
				this.setCanMoveStatusCondition(true);
			}
			break;
		case NO_STATUS:
			this.setCanMoveStatusCondition(true);
			break;
		case DEBILITATED:
			break;
		default:
			break;
		}
	}

	public boolean doEffectEphemeralsCondition(ArrayList<State> statuses, boolean alreadyChecked) {

		boolean canAttack = true;

		int attackProbability = (int) (Math.random() * 100);

		for (State status : statuses) {

			switch (status.getStatusCondition()) {
			case CONFUSED:
				if (status.getNbTurns() == 0) {

					status.setCanMoveEphemeralState(true);

				} else {
					// Only can attack if proba <= 50%
					if (attackProbability <= 50) {

						// Only decrease turns if not already checked
						if (!alreadyChecked) {
							status.setNbTurns(status.getNbTurns() - 1);
						}
						status.setCanMoveEphemeralState(true);

						System.out.println(ANSI_CYAN + "Faltan " + status.getNbTurns()
								+ " turnos (confuso - puede atacar): " + ANSI_RESET);

					} else {

						if (!alreadyChecked) {
							status.setNbTurns(status.getNbTurns() - 1);
						}
						status.setCanMoveEphemeralState(false);

						System.out.println(ANSI_CYAN + "Faltan " + status.getNbTurns()
								+ " turnos (confuso - no puede atacar): " + ANSI_RESET);
					}
				}

				if (!status.getCanMoveEphemeralState()) {
					canAttack = false; // If one state prevents movement → false
				}

				break;
			case TRAPPED:
				// In all cases, can attack. This is just a reminder in case of problems
				if (status.getNbTurns() == 0) {

					status.setCanMoveEphemeralState(true);

				} else {

					if (!alreadyChecked) {
						status.setNbTurns(status.getNbTurns() - 1);
					}
					status.setCanMoveEphemeralState(true);
				}
				break;
			case PERISH_SONG:
				break;
			case SEEDED:
				break;
			case INFATUATED:
				break;
			case CURSED:
				break;
			case TRAPPEDBYOWNATTACK:
				if (status.getNbTurns() == 0) {

					// this.setStatusCondition(StatusConditions.NO_STATUS);
					status.setCanMoveEphemeralState(true);

				} else {
					if (!alreadyChecked) {
						status.setNbTurns(status.getNbTurns() - 1);
					}
					status.setCanMoveEphemeralState(true);
				}
				System.out
						.println(ANSI_CYAN + "Turnos restantes con mismo ataque : " + status.getNbTurns() + ANSI_RESET);
				break;
			case NO_STATUS:
				status.setCanMoveEphemeralState(true);
				break;
			case DEBILITATED:
				// Just in case in future these conditions block attacking
				if (!status.getCanMoveEphemeralState()) {
					canAttack = false;
				}
				break;
			default:
				break;
			}
		}

		return canAttack;
	}
}
