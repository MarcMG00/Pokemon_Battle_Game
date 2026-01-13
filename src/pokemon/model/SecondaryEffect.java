package pokemon.model;

import pokemon.enums.SecondaryEffectType;
import pokemon.enums.StatType;
import pokemon.enums.StatusConditions;

public class SecondaryEffect {
	// ==================================== FIELDS
	// ====================================

	private SecondaryEffectType type;
	private StatusConditions status; // BURN, PARALYSIS, etc
	private StatType stat;
	private int stages;
	private double probability;

	// ==================================== CONSTRUCTORS
	// ====================================

	public SecondaryEffect(SecondaryEffectType type, StatusConditions status, double probability) {
		this.type = type;
		this.status = status;
		this.probability = probability;
	}

	public SecondaryEffect(SecondaryEffectType type, double probability) {
		this.type = type;
		this.probability = probability;
	}

	public SecondaryEffect(SecondaryEffectType type, StatType stat, int stages, double probability) {
		this.type = type;
		this.status = StatusConditions.NO_STATUS;
		this.stat = stat;
		this.probability = probability;
		this.stages = stages;
	}
	
	public SecondaryEffect() {
		this.type = SecondaryEffectType.NONE;
		this.status = StatusConditions.NO_STATUS;
		this.stat = StatType.NONE;
		this.probability = 0d;
		this.stages = 0;
	}

	// ==================================== GETTERS/SETTERS
	// ====================================

	public StatusConditions getStatus() {
		return status;
	}

	public void setStatus(StatusConditions status) {
		this.status = status;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public SecondaryEffectType getType() {
		return type;
	}

	public void setType(SecondaryEffectType type) {
		this.type = type;
	}

	public StatType getStat() {
		return stat;
	}

	public void setStat(StatType stat) {
		this.stat = stat;
	}

	public int getStages() {
		return stages;
	}

	public void setStages(int stages) {
		this.stages = stages;
	}
}
