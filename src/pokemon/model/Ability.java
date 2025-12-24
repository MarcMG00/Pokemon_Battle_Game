package pokemon.model;

import pokemon.interfce.AbilityEffect;

public class Ability {

	// ==================================== FIELDS
	// ====================================

	private int id;
	private String name;
	private String description;
	private AbilityEffect effect;
	private boolean isWeatherType;

	// ==================================== CONSTRUCTORS
	// ====================================

	public Ability() {
		super();
		this.id = 5000;
		this.name = "";
		this.description = "";
		this.effect = new AbilityEffect() {
		};
		this.isWeatherType = false;
	}

	public Ability(int id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.effect = new AbilityEffect() {
		};
		this.isWeatherType = false;
	}

	// ==================================== GETTERS/SETTERS
	// ====================================

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AbilityEffect getEffect() {
		return effect;
	}

	public void setEffect(AbilityEffect effect) {
		this.effect = effect;
	}

	public boolean getIsWeatherType() {
		return isWeatherType;
	}

	public void setIsWeatherType(boolean isWeatherType) {
		this.isWeatherType = isWeatherType;
	}
}
