package pokemon.model;

import pokemon.enums.StatusConditions;

public class State {

	// ==================================== FIELDS
	// ====================================

	private StatusConditions statusCondition;
	private boolean canMoveStatusCondition;
	private int nbTurns;
	private int percentToBeDefrosted;

	// ==================================== CONSTRUCTORS
	// ====================================

	public State() {
		this.statusCondition = StatusConditions.NO_STATUS;
		this.nbTurns = 0;
		this.percentToBeDefrosted = 10;
	}

	public State(StatusConditions estadoEnum, int numTurnos) {
		this.statusCondition = estadoEnum;
		this.canMoveStatusCondition = true;
		this.nbTurns = numTurnos;
		this.percentToBeDefrosted = 10;
	}

	public State(StatusConditions estadoEnum) {
		this.statusCondition = estadoEnum;
		this.canMoveStatusCondition = true;
		this.nbTurns = 0;
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

	public int getPercentToBeDefrosted() {
		return percentToBeDefrosted;
	}

	public void setPercentToBeDefrosted(int percentToBeDefrosted) {
		this.percentToBeDefrosted = percentToBeDefrosted;
	}
}
