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
}
