package pokemon.model;

import pokemon.enums.EstadosEnum;

public class Estado {

	private EstadosEnum estadoEnum;
	private boolean puedeMoverse;
	private int numTurnos;
	private float reduccionPs;
	
	public Estado() {
		this.estadoEnum = EstadosEnum.SIN_ESTADO;
		this.puedeMoverse = true;
		this.numTurnos = 0;
		this.reduccionPs = 0;
	}
	
	public Estado(EstadosEnum estadoEnum, int numTurnos) {
		this.estadoEnum = estadoEnum;
		this.puedeMoverse = true;
		this.numTurnos = 0;
		this.reduccionPs = 0;
	}

	public EstadosEnum getEstadoEnum() {
		return estadoEnum;
	}

	public void setEstadoEnum(EstadosEnum estadoEnum) {
		this.estadoEnum = estadoEnum;
	}

	public boolean isPuedeMoverse() {
		return puedeMoverse;
	}

	public void setPuedeMoverse(boolean puedeMoverse) {
		this.puedeMoverse = puedeMoverse;
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

	
	
}
