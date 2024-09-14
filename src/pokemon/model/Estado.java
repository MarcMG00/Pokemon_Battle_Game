package pokemon.model;

import pokemon.enums.EstadosEnum;

public class Estado {

	private EstadosEnum estadoEnum;
	private boolean puedeMoverseEstadoPersistente;
	private boolean puedeMoverseEstadoEfimero;
	private int numTurnos;
	private float reduccionPs;

	public Estado() {
		this.estadoEnum = EstadosEnum.SIN_ESTADO;
		this.puedeMoverseEstadoPersistente = true;
		this.numTurnos = 0;
		this.reduccionPs = 0;
	}

	public Estado(EstadosEnum estadoEnum, int numTurnos) {
		this.estadoEnum = estadoEnum;
		this.puedeMoverseEstadoPersistente = true;
		this.puedeMoverseEstadoEfimero = true;
		this.numTurnos = numTurnos;
		this.reduccionPs = 0;
	}
	
	public Estado(EstadosEnum estadoEnum) {
		this.estadoEnum = estadoEnum;
		this.puedeMoverseEstadoPersistente = true;
		this.puedeMoverseEstadoEfimero = true;
		this.numTurnos = 0;
		this.reduccionPs = 0;
	}

	public EstadosEnum getEstadoEnum() {
		return estadoEnum;
	}

	public void setEstadoEnum(EstadosEnum estadoEnum) {
		this.estadoEnum = estadoEnum;
	}

	public boolean isPuedeMoverseEstadoPersistente() {
		return puedeMoverseEstadoPersistente;
	}

	public void setPuedeMoverseEstadoPersistente(boolean puedeMoverseEstadoPersistente) {
		this.puedeMoverseEstadoPersistente = puedeMoverseEstadoPersistente;
	}

	public boolean isPuedeMoverseEstadoEfimero() {
		return puedeMoverseEstadoEfimero;
	}

	public void setPuedeMoverseEstadoEfimero(boolean puedeMoverseEstadoEfimero) {
		this.puedeMoverseEstadoEfimero = puedeMoverseEstadoEfimero;
	}

	public int getNumTurnos() {
		return numTurnos;
	}

	public void setNumTurnos(int numTurnos) {
		this.numTurnos = numTurnos;
	}

	public float getReduccionPs() {
		return reduccionPs;
	}

	public void setReduccionPs(float reduccionPs) {
		this.reduccionPs = reduccionPs;
	}

	// Sets the amount of damage, mobility and effects at the end/beginning of each
	// attack
	public void doEffectEstadoPersistente(EstadosEnum estadoEnum) {

		int randomProbaDeshacerse;
		int randomProbaAtacar;

		switch (estadoEnum) {
		case CONFUSO:
			break;
		case ATRAPADO:
			break;
		case CANTO_MORTAL:
			break;
		case CONGELADO:
			if (this.getNumTurnos() == 0) {
				this.setEstadoEnum(EstadosEnum.SIN_ESTADO);
				this.setPuedeMoverseEstadoPersistente(true);
				
				System.out.println("Se terminó número de turnos congelado, ya puede volver a atacar");
			} else {
				randomProbaDeshacerse = (int) (Math.random() * 100);
				// Only can be thawed if proba <= 20%
				if (randomProbaDeshacerse <= 20) {
					this.setNumTurnos(0);
					this.setEstadoEnum(EstadosEnum.SIN_ESTADO);
					this.setPuedeMoverseEstadoPersistente(true);
					
					System.out.println("Se ha descongelado (proba sup a 20) : " + randomProbaDeshacerse);
				} else {
					this.setPuedeMoverseEstadoPersistente(false);
					this.setNumTurnos(this.getNumTurnos() - 1);
					
					System.out.println("No se ha descongelado (proba inf a 20) : " + randomProbaDeshacerse);
					System.out.println("Faltan " + this.getNumTurnos() + " turnos");
				}
			}
			break;
		case DEBILITADO:
			break;
		case DORMIDO:
			break;
		case DRENADO:
			break;
		case ENAMORADO:
			break;
		case ENVENENADO:
			break;
		case GRAVEMENTE_ENVENENADO:
			break;
		case MALDITO:
			break;
		case PARALIZADO:
			if (this.getNumTurnos() == 0) {
				this.setEstadoEnum(EstadosEnum.SIN_ESTADO);
				this.setPuedeMoverseEstadoPersistente(true);
				
				System.out.println("Ya no está paralizado");
			} else {
				randomProbaAtacar = (int) (Math.random() * 100);
				// Only can attack if proba <= 25%
				if (randomProbaAtacar <= 25) {
					this.setPuedeMoverseEstadoPersistente(true);
					this.setNumTurnos(this.getNumTurnos() - 1);
					System.out.println("Faltan " + this.getNumTurnos() + " turnos (paralizado pero puede atacar): " + randomProbaAtacar);
				} else {
					this.setPuedeMoverseEstadoPersistente(false);
					this.setNumTurnos(this.getNumTurnos() - 1);
					System.out.println("Faltan " + this.getNumTurnos() + " turnos (sigue paralizado): " + randomProbaAtacar);
				}
			}
			break;
		case QUEMADO:
			// In all cases, can attack. This is just a reminder in case of problems
			if(this.getNumTurnos() == 0) {
				this.setEstadoEnum(EstadosEnum.SIN_ESTADO);
				this.setPuedeMoverseEstadoPersistente(true);
			}
			else {
				this.setPuedeMoverseEstadoPersistente(true);
				this.setNumTurnos(this.getNumTurnos() - 1);
			}
			break;
		case SIN_ESTADO:
			this.setPuedeMoverseEstadoPersistente(true);
			break;
		}
	}

}
