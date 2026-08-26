package pokemon.model;

import pokemon.enums.StatusConditions;

public class State {

	// ==================================== FIELDS
	// ====================================

	private StatusConditions statusCondition;
	private int nbTurns;
	private int percentToBeDefrosted;
	private Attack attackDisabled;
	private int toxicCounter;

	// ==================================== CONSTRUCTORS
	// ====================================

	public State() {
		this.statusCondition = StatusConditions.NO_STATUS;
		this.nbTurns = 0;
		this.percentToBeDefrosted = 10;
		this.attackDisabled = new Attack();
		this.toxicCounter = 0;
	}

	public State(StatusConditions estadoEnum, int numTurnos) {
		this.statusCondition = estadoEnum;
		this.nbTurns = numTurnos;
		this.percentToBeDefrosted = 10;
		this.attackDisabled = new Attack();
		this.toxicCounter = 0;
	}

	public State(StatusConditions estadoEnum) {
		this.statusCondition = estadoEnum;
		this.nbTurns = 0;
		this.percentToBeDefrosted = 10;
		this.attackDisabled = new Attack();
		this.toxicCounter = 0;
	}

	// ==================================== GETTERS/SETTERS
	// ====================================

	public StatusConditions getStatusCondition() {
		return statusCondition;
	}

	public void setStatusCondition(StatusConditions estadoEnum) {
		this.statusCondition = estadoEnum;
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

	public Attack getAttackDisabled() {
		return attackDisabled;
	}

	public void setAttackDisabled(Attack attackDisabled) {
		this.attackDisabled = attackDisabled;
	}

	public int getToxicCounter() {
		return toxicCounter;
	}

	public void setToxicCounter(int toxicCounter) {
		this.toxicCounter = toxicCounter;
	}
	
	public void incrementToxicCounter() {
	    toxicCounter++;
	}
}
