package pokemon.model;

import pokemon.enums.StatusConditions;

public class State {

	private StatusConditions statusCondition;
	private boolean canMoveEphemeralState;
	private boolean canMoveStatusCondition;
	private int nbTurns;
	private float PSReduction;

	public State() {
		this.statusCondition = StatusConditions.NO_STATUS;
		this.canMoveEphemeralState = true;
		this.nbTurns = 0;
		this.PSReduction = 0;
	}

	public State(StatusConditions estadoEnum, int numTurnos) {
		this.statusCondition = estadoEnum;
		this.canMoveEphemeralState = true;
		this.canMoveStatusCondition = true;
		this.nbTurns = numTurnos;
		this.PSReduction = 0;
	}
	
	public State(StatusConditions estadoEnum) {
		this.statusCondition = estadoEnum;
		this.canMoveEphemeralState = true;
		this.canMoveStatusCondition = true;
		this.nbTurns = 0;
		this.PSReduction = 0;
	}

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

	// Sets the amount of damage, mobility and effects at the end/beginning of each turn
	public void doEffectStatusCondition(StatusConditions status) {

		int getRidOfStatusProbability = (int) (Math.random() * 100);
		int attackProbability = (int) (Math.random() * 100);

		switch (status) {
		case CONFUSED:
			break;
		case TRAPPED:
			break;
		case PERISH_SONG:
			break;
		case FROZEN:
			if (this.getNbTurns() == 0) {
				
				this.setStatusCondition(StatusConditions.NO_STATUS);
				this.setCanMoveEphemeralState(true);
				
				System.out.println("Se terminó el número de turnos congelado, ya puede volver a atacar");
				
			} else {
				
				// Only can be thawed if probability <= 20%
				if (getRidOfStatusProbability <= 20) {
					
					this.setNbTurns(0);
					this.setStatusCondition(StatusConditions.NO_STATUS);
					this.setCanMoveEphemeralState(true);
					
					System.out.println("Se descongeló (proba sup a 20) : " + getRidOfStatusProbability);
					
				} else {
					
					this.setCanMoveEphemeralState(false);
					this.setNbTurns(this.getNbTurns() - 1);
					
					System.out.println("No se descongeló (proba inf a 20) : " + getRidOfStatusProbability);
					System.out.println("Faltan " + this.getNbTurns() + " turnos");
					
				}
			}
			break;
		case DEBILITATED:
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
				this.setCanMoveEphemeralState(true);
				
				System.out.println("Ya no está paralizado");
				
			} else {
				
				// Only can attack if proba <= 25%
				if (attackProbability <= 25) {
					
					this.setCanMoveEphemeralState(true);
					this.setNbTurns(this.getNbTurns() - 1);
					
					System.out.println("Faltan " + this.getNbTurns() + " turnos (paralizado pero puede atacar): " + attackProbability);
					
				} else {
					
					this.setCanMoveEphemeralState(false);
					this.setNbTurns(this.getNbTurns() - 1);
					
					System.out.println("Faltan " + this.getNbTurns() + " turnos (sigue paralizado): " + attackProbability);
					
				}
			}
			break;
		case BURNED:
			// In all cases, can attack. This is just a reminder in case of problems
			if(this.getNbTurns() == 0) {
				
				this.setStatusCondition(StatusConditions.NO_STATUS);
				this.setCanMoveEphemeralState(true);
				
			}
			else {
				
				this.setCanMoveEphemeralState(true);
				this.setNbTurns(this.getNbTurns() - 1);
				
			}
			break;
		case NO_STATUS:
			this.setCanMoveEphemeralState(true);
			break;
		}
	}
}
