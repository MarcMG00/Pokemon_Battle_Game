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
	private int initialPower;
	private int pp;
	private int precision;
	private int initialPrecision;
	private String effect;
	private PokemonType strTypeToPkType;
	private float effectivenessAgainstPkFacing;
	private float bonus;
	private AttackCategory category = AttackCategory.NORMAL;
	private List<Integer> canHitWhileInvulnerable = new ArrayList<>();
	private boolean canRecieveDamage;
	private double percentageFlinch;
	private boolean isOneHitKO;
	private boolean makesContact;
	private boolean hasSecondaryEffect;
	private boolean reduceStats;
	private List<SecondaryEffect> secondaryEffects = new ArrayList<>();

	// ==================================== CONSTRUCTORS
	// ====================================

	public Attack() {
		super();
		this.id = 0;
		this.name = "";
		this.type = "";
		this.bases = new ArrayList<>();
		this.power = 0;
		this.initialPower = 0;
		this.pp = 0;
		this.precision = 0;
		this.initialPrecision = 0;
		this.effect = "";
		this.strTypeToPkType = new PokemonType();
		this.effectivenessAgainstPkFacing = 0;
		this.bonus = 0;
		this.canRecieveDamage = false;
		this.percentageFlinch = 0d;
		this.isOneHitKO = false;
		this.makesContact = false;
		this.hasSecondaryEffect = false;
		this.reduceStats = false;
	}

	public Attack(int id, String name, String type, int power, int pp, int precision, String effect) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.bases = new ArrayList<>();
		this.power = power;
		this.initialPower = power;
		this.pp = pp;
		this.precision = precision;
		this.initialPrecision = precision;
		this.effect = effect;
		this.strTypeToPkType = new PokemonType();
		this.effectivenessAgainstPkFacing = 0;
		this.bonus = 0;
		this.canRecieveDamage = false;
		this.percentageFlinch = 0d;
		this.isOneHitKO = false;
		this.makesContact = false;
		this.hasSecondaryEffect = false;
		this.reduceStats = false;
	}
	
	public Attack(Attack attack) {
		super();
		this.id = attack.id;
		this.name = attack.name;
		this.type = attack.type;
		this.bases = attack.bases;
		this.power = attack.power;
		this.initialPower = attack.initialPower;
		this.pp = attack.pp;
		this.precision = attack.precision;
		this.initialPrecision = attack.initialPrecision;
		this.effect = attack.effect;
		this.strTypeToPkType = attack.strTypeToPkType;
		this.effectivenessAgainstPkFacing = attack.effectivenessAgainstPkFacing;
		this.bonus = attack.bonus;
		this.canRecieveDamage = attack.canRecieveDamage;
		this.percentageFlinch = attack.percentageFlinch;
		this.isOneHitKO = attack.isOneHitKO;
		this.makesContact = attack.makesContact;
		this.hasSecondaryEffect = attack.hasSecondaryEffect;
		this.reduceStats = attack.reduceStats;
		this.canHitWhileInvulnerable = attack.canHitWhileInvulnerable;
		this.secondaryEffects = attack.secondaryEffects;
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

	public double getPercentageFlinched() {
		return percentageFlinch;
	}

	public void setPercentageFlinched(double percentageFlinch) {
		this.percentageFlinch = percentageFlinch;
	}

	public int getInitialPower() {
		return initialPower;
	}

	public void setInitialPower(int initialPower) {
		this.initialPower = initialPower;
	}

	public int getInitialPrecision() {
		return initialPrecision;
	}

	public void setInitialPrecision(int initialPrecision) {
		this.initialPrecision = initialPrecision;
	}

	public boolean getIsOneHitKO() {
		return isOneHitKO;
	}

	public void setIsOneHitKO(boolean isOneHitKO) {
		this.isOneHitKO = isOneHitKO;
	}

	public boolean getMakesContact() {
		return makesContact;
	}

	public void setMakesContact(boolean makesContact) {
		this.makesContact = makesContact;
	}

	public boolean getHasSecondaryEffect() {
		return hasSecondaryEffect;
	}

	public void setHasSecondaryEffect(boolean hasSecondaryEffect) {
		this.hasSecondaryEffect = hasSecondaryEffect;
	}

	public boolean getReduceStats() {
		return reduceStats;
	}

	public void setReduceStats(boolean reduceStats) {
		this.reduceStats = reduceStats;
	}

	public List<SecondaryEffect> getSecondaryEffects() {
		return secondaryEffects;
	}
	
	public void setSecondaryEffectsNull() {
		this.secondaryEffects = null;
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

	// -----------------------------
	// Add secondary effect
	// -----------------------------
	public void addSecondaryEffect(SecondaryEffect secondaryEffect) {
		this.getSecondaryEffects().add(secondaryEffect);
	}
}
