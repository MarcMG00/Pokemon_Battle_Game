package pokemon.model;

import java.util.ArrayList;

public class PokemonType {
	
	// ==================================== FIELDS
	// ====================================
	
	private int id;
	private String name;
	private ArrayList<Integer> pktDoLotDamage;
	private ArrayList<Integer> pktRecieveLotDamage;
	private ArrayList<Integer> pktDoLowDamage;
	private ArrayList<Integer> pktReceiveLowDamage;
	private ArrayList<Integer> noEffect;
	
	// ==================================== CONSTRUCTORS
	// ====================================
	
	public PokemonType() {
		super();
		this.id = 0;
		this.name = "";
		this.pktDoLotDamage = new ArrayList<>();
		this.pktRecieveLotDamage = new ArrayList<>();
		this.pktDoLowDamage = new ArrayList<>();
		this.pktReceiveLowDamage = new ArrayList<>();
		this.noEffect = new ArrayList<>();
	}
	
	public PokemonType(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.pktDoLotDamage = new ArrayList<>();
		this.pktRecieveLotDamage = new ArrayList<>();
		this.pktDoLowDamage = new ArrayList<>();
		this.pktReceiveLowDamage = new ArrayList<>();
		this.noEffect = new ArrayList<>();
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

	public ArrayList<Integer> getPktDoLotDamage() {
		return pktDoLotDamage;
	}

	public void setPktDoLotDamage(ArrayList<Integer> pktDoLotDamage) {
		this.pktDoLotDamage = pktDoLotDamage;
	}

	public ArrayList<Integer> getPktRecieveLotDamage() {
		return pktRecieveLotDamage;
	}

	public void setPktRecieveLotDamage(ArrayList<Integer> pktRecieveLotDamage) {
		this.pktRecieveLotDamage = pktRecieveLotDamage;
	}

	public ArrayList<Integer> getPktDoLowDamage() {
		return pktDoLowDamage;
	}

	public void setPktDoLowDamage(ArrayList<Integer> pktDoLowDamage) {
		this.pktDoLowDamage = pktDoLowDamage;
	}

	public ArrayList<Integer> getPktReceiveLowDamage() {
		return pktReceiveLowDamage;
	}

	public void setPktReceiveLowDamage(ArrayList<Integer> pktReceiveLowDamage) {
		this.pktReceiveLowDamage = pktReceiveLowDamage;
	}

	public ArrayList<Integer> getNoEffect() {
		return noEffect;
	}

	public void setNoEffect(ArrayList<Integer> noEffect) {
		this.noEffect = noEffect;
	}
	
	// -----------------------------
		// Check if Pokemon type is Steel type
		// -----------------------------
		public boolean isSteelType() {
			return this.getId() == 1;
		}

		// -----------------------------
		// Check if Pokemon type is Water type
		// -----------------------------
		public boolean isWaterType() {
			return this.getId() == 2;
		}

		// -----------------------------
		// Check if Pokemon type is Bug type
		// -----------------------------
		public boolean isBugType() {
			return this.getId() == 3;
		}

		// -----------------------------
		// Check if Pokemon type is Dragon type
		// -----------------------------
		public boolean isDragonType() {
			return this.getId() == 4;
		}

		// -----------------------------
		// Check if Pokemon type is Steel type
		// -----------------------------
		public boolean isElectricType() {
			return this.getId() == 5;
		}

		// -----------------------------
		// Check if Pokemon type is Ghost type
		// -----------------------------
		public boolean isGhostType() {
			return this.getId() == 6;
		}

		// -----------------------------
		// Check if Pokemon type is Fire type
		// -----------------------------
		public boolean isFireType() {
			return this.getId() == 7;
		}

		// -----------------------------
		// Check if Pokemon type is Fairy type
		// -----------------------------
		public boolean isFairyType() {
			return this.getId() == 8;
		}

		// -----------------------------
		// Check if Pokemon type is Ice type
		// -----------------------------
		public boolean isIceType() {
			return this.getId() == 9;
		}

		// -----------------------------
		// Check if Pokemon type is Fighting type
		// -----------------------------
		public boolean isFightingType() {
			return this.getId() == 10;
		}

		// -----------------------------
		// Check if Pokemon type is Normal type
		// -----------------------------
		public boolean isNormalType() {
			return this.getId() == 11;
		}

		// -----------------------------
		// Check if Pokemon type is Grass type
		// -----------------------------
		public boolean isGrassType() {
			return this.getId() == 12;
		}

		// -----------------------------
		// Check if Pokemon type is Psychic type
		// -----------------------------
		public boolean isPsychicType() {
			return this.getId() == 13;
		}

		// -----------------------------
		// Check if Pokemon type is Rock type
		// -----------------------------
		public boolean isRockType() {
			return this.getId() == 14;
		}

		// -----------------------------
		// Check if Pokemon type is Dark type
		// -----------------------------
		public boolean isDarkType() {
			return this.getId() == 15;
		}

		// -----------------------------
		// Check if Pokemon type is Ground type
		// -----------------------------
		public boolean isGroundType() {
			return this.getId() == 16;
		}

		// -----------------------------
		// Check if Pokemon type is Poison type
		// -----------------------------
		public boolean isPoisonType() {
			return this.getId() == 17;
		}

		// -----------------------------
		// Check if Pokemon type is Flying type
		// -----------------------------
		public boolean isFlyingType() {
			return this.getId() == 18;
		}
}
