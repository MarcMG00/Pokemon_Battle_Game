package pokemon.model;

public class PokemonType {
	
	// ==================================== FIELDS
	// ====================================
	
	private int id;
	private String name;
	private float xLowDamage;
	private float xNormalDamage;
	private float xHighDamage;
	
	// ==================================== CONSTRUCTORS
	// ====================================
	
	public PokemonType() {
		super();
		this.id = 0;
		this.name = "";
		this.xLowDamage = 0f;
		this.xHighDamage = 2f;
		this.xNormalDamage = 1.5f;
	}
	
	public PokemonType(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.xLowDamage = 0f;
		this.xHighDamage = 2f;
		this.xNormalDamage = 1.5f;
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

	public float getXLowDamage() {
		return xLowDamage;
	}

	public void setXLowDamage(float xLowDamage) {
		this.xLowDamage = xLowDamage;
	}

	public float getXHighDamage() {
		return xHighDamage;
	}

	public void setXHighDamage(float xHighDamage) {
		this.xHighDamage = xHighDamage;
	}

	public float getXNormalDamage() {
		return xNormalDamage;
	}

	public void setXNormalDamage(float xNormalDamage) {
		this.xNormalDamage = xNormalDamage;
	}
}
