package pokemon.model;

import java.util.ArrayList;
import java.util.List;

import pokemon.enums.AttackCategory;

public class Attack {

	// ==================================== FIELDS
	// ====================================

	private int id;
	private String name;
	private String type;
	private ArrayList<String> bases;
	private int power;
	private int pp;
	private int precision;
	private String effect;
	private PokemonType strTypeToPkType;
	private float effectivenessAgainstPkFacing;
	private float bonus;
	private AttackCategory category = AttackCategory.NORMAL;
	private List<Integer> canHitWhileInvulnerable = new ArrayList<>();
	private boolean canRecieveDamage;
	private boolean canBeFlinched;

	// ==================================== CONSTRUCTORS
	// ====================================

	public Attack() {
		super();
		this.id = 0;
		this.name = "";
		this.type = "";
		this.bases = new ArrayList<>();
		this.power = 0;
		this.pp = 0;
		this.precision = 0;
		this.effect = "";
		this.strTypeToPkType = new PokemonType();
		this.effectivenessAgainstPkFacing = 0;
		this.bonus = 0;
		this.canRecieveDamage = false;
		this.canBeFlinched = false;
	}

	public Attack(int id, String name, String type, int power, int pp, int precision, String effect) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.bases = new ArrayList<>();
		this.power = power;
		this.pp = pp;
		this.precision = precision;
		this.effect = effect;
		this.strTypeToPkType = new PokemonType();
		this.effectivenessAgainstPkFacing = 0;
		this.bonus = 0;
		this.canRecieveDamage = false;
		this.canBeFlinched = false;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<String> getBases() {
		return bases;
	}

	public void setBases(ArrayList<String> bases) {
		this.bases = bases;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getPp() {
		return pp;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public PokemonType getStrTypeToPkType() {
		return strTypeToPkType;
	}

	public void setStrTypeToPkType(PokemonType strTypeToPkType) {
		this.strTypeToPkType = strTypeToPkType;
	}

	public float getEffectivenessAgainstPkFacing() {
		return effectivenessAgainstPkFacing;
	}

	public void setEffectivenessAgainstPkFacing(float effectivenessAgainstPkFacing) {
		this.effectivenessAgainstPkFacing = effectivenessAgainstPkFacing;
	}

	public float getBonus() {
		return bonus;
	}

	public void setBonus(float bonus) {
		this.bonus = bonus;
	}

	// Add type of base to an Attack
	public void addBase(String base) {
		this.bases.add(base);
	}

	public AttackCategory getCategory() {
		return category;
	}

	public void setCategory(AttackCategory category) {
		this.category = category;
	}

	public List<Integer> getCanHitWhileInvulnerable() {
		return canHitWhileInvulnerable;
	}

	public void setCanHitWhileInvulnerable(List<Integer> canHitWhileInvulnerable) {
		this.canHitWhileInvulnerable = canHitWhileInvulnerable;
	}

	public boolean getCanRecieveDamage() {
		return canRecieveDamage;
	}

	public void setCanRecieveDamage(boolean canRecieveDamage) {
		this.canRecieveDamage = canRecieveDamage;
	}
	
	public boolean getCanBeFlinched() {
		return canBeFlinched;
	}

	public void setCanBeFlinched(boolean canBeFlinched) {
		this.canBeFlinched = canBeFlinched;
	}

	// ==================================== METHODS
	// ====================================

	// -----------------------------
	// Set the type of the attack to his Pokemon type instead of a string
	// -----------------------------
	public void transformStrTypeToPokemonType(ArrayList<PokemonType> types) {
		this.setStrTypeToPkType(types.stream().filter(pk -> pk.getName().equals(this.getType())).findFirst().get());
	}

	// -----------------------------
	// Check if can hit while target is invulnerable
	// -----------------------------
	public boolean canHitWhileTargetInvulnerable(int targetAttackId) {
		return this.getCanHitWhileInvulnerable() != null && this.getCanHitWhileInvulnerable().contains(targetAttackId);
	}
}
