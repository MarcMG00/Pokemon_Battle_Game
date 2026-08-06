package pokemon.model;

import java.util.ArrayList;
import java.util.List;

import pokemon.enums.AttackCategory;
import pokemon.enums.SecondaryEffectType;
import pokemon.enums.Weather;

public class Attack {

	// ==================================== FIELDS
	// ====================================

	private int id;
	private String name;
	private String type;
	private ArrayList<String> bases;
	private float power;
	private float initialPower;
	private int pp;
	private float precision;
	private float initialPrecision;
	private String effect;
	private PokemonType strTypeToPkType;
	private float effectivenessAgainstPkFacing;
	private float bonus;
	private AttackCategory category;
	private List<Integer> canHitWhileInvulnerable = new ArrayList<>();
	private boolean canRecieveDamage;
	private boolean isOneHitKO;
	private boolean makesContact;
	private List<SecondaryEffect> secondaryEffects = new ArrayList<>();
	private boolean alwaysHits;
	private boolean ignoresAccuracy;
	private Weather guaranteedWeather;
	private boolean forceChange;
	private boolean isPunchMove;
	private boolean isSelfDestruction;
	private boolean isAppliedToAttacker;

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
		this.isOneHitKO = false;
		this.makesContact = false;
		this.canHitWhileInvulnerable = new ArrayList<>();
		this.alwaysHits = false;
		this.ignoresAccuracy = false;
		this.guaranteedWeather = Weather.NONE;
		this.forceChange = false;
		this.isPunchMove = false;
		this.category = AttackCategory.NORMAL;
		this.isSelfDestruction = false;
		this.isAppliedToAttacker = false;
	}

	public Attack(int id, String name, String type, float power, int pp, float precision, String effect) {
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
		this.isOneHitKO = false;
		this.makesContact = false;
		this.canHitWhileInvulnerable = new ArrayList<>();
		this.secondaryEffects = new ArrayList<>();
		this.alwaysHits = false;
		this.ignoresAccuracy = false;
		this.guaranteedWeather = Weather.NONE;
		this.forceChange = false;
		this.isPunchMove = false;
		this.category = AttackCategory.NORMAL;
		this.isSelfDestruction = false;
		this.isAppliedToAttacker = false;
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
		this.isOneHitKO = attack.isOneHitKO;
		this.makesContact = attack.makesContact;
		this.canHitWhileInvulnerable = attack.canHitWhileInvulnerable;
		this.secondaryEffects = attack.secondaryEffects;
		this.alwaysHits = attack.alwaysHits;
		this.ignoresAccuracy = attack.ignoresAccuracy;
		this.guaranteedWeather = attack.guaranteedWeather;
		this.forceChange = attack.forceChange;
		this.isPunchMove = attack.isPunchMove;
		this.category = attack.category;
		this.isSelfDestruction = attack.isSelfDestruction;
		this.isAppliedToAttacker = attack.isAppliedToAttacker;
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

	public float getPower() {
		return power;
	}

	public void setPower(float power) {
		this.power = power;
	}

	public int getPp() {
		return pp;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public float getPrecision() {
		return precision;
	}

	public void setPrecision(float precision) {
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

	public float getInitialPower() {
		return initialPower;
	}

	public void setInitialPower(float initialPower) {
		this.initialPower = initialPower;
	}

	public float getInitialPrecision() {
		return initialPrecision;
	}

	public void setInitialPrecision(float initialPrecision) {
		this.initialPrecision = initialPrecision;
	}

	public boolean isOneHitKO() {
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

	public List<SecondaryEffect> getSecondaryEffects() {
		return secondaryEffects;
	}

	public void setSecondaryEffectsNull() {
		this.secondaryEffects = null;
	}

	public boolean alwaysHits() {
		return alwaysHits;
	}

	public void setAlwaysHits(boolean alwaysHits) {
		this.alwaysHits = alwaysHits;
	}

	public boolean isIgnoresAccuracy() {
		return ignoresAccuracy;
	}

	public void setIgnoresAccuracy(boolean ignoresAccuracy) {
		this.ignoresAccuracy = ignoresAccuracy;
	}

	public Weather getGuaranteedWeather() {
		return guaranteedWeather;
	}

	public void setGuaranteedWeather(Weather guaranteedWeather) {
		this.guaranteedWeather = guaranteedWeather;
	}

	public boolean isForceChange() {
		return forceChange;
	}

	public void setForceChange(boolean forceChange) {
		this.forceChange = forceChange;
	}

	public boolean isPunchMove() {
		return isPunchMove;
	}

	public void setPunchMove(boolean isPunchMove) {
		this.isPunchMove = isPunchMove;
	}

	public boolean isSelfDestruction() {
		return isSelfDestruction;
	}

	public void setSelfDestruction(boolean isSelfDestruction) {
		this.isSelfDestruction = isSelfDestruction;
	}

	public boolean isAppliedToAttacker() {
		return isAppliedToAttacker;
	}

	public void setAppliedToAttacker(boolean isAppliedToAttacker) {
		this.isAppliedToAttacker = isAppliedToAttacker;
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
	// Check if attack always hits under the specific weather
	// -----------------------------
	public boolean alwaysHeatsUnderWeather(Weather weather) {
		return this.getGuaranteedWeather() != Weather.NONE && this.getGuaranteedWeather() == weather;
	}

	// -----------------------------
	// Add secondary effect
	// -----------------------------
	public void addSecondaryEffect(SecondaryEffect effect) {
		this.getSecondaryEffects().add(effect);
	}

	// -----------------------------
	// Check if has a secondary effect
	// -----------------------------
	public boolean hasSecondaryEffect() {
		return this.getSecondaryEffects() != null;
	}

	// -----------------------------
	// Check if has a specific secondary effect
	// -----------------------------
	public boolean hasActiveSecondaryEffect(SecondaryEffectType effectType) {
		return this.getSecondaryEffects() != null
				&& this.getSecondaryEffects().stream().anyMatch(e -> e.getType() == effectType);
	}

	// -----------------------------
	// Get all the secondary effects from a effect type
	// -----------------------------
	public List<SecondaryEffect> getSecondaryEffectsOfType(SecondaryEffectType effectType) {
		return this.getSecondaryEffects().stream().filter(e -> e.getType() == effectType).toList();
	}

	// -----------------------------
	// Check if attack is Steel type
	// -----------------------------
	public boolean isSteelType() {
		return this.getStrTypeToPkType().getId() == 1;
	}

	// -----------------------------
	// Check if attack is Water type
	// -----------------------------
	public boolean isWaterType() {
		return this.getStrTypeToPkType().getId() == 2;
	}

	// -----------------------------
	// Check if attack is Bug type
	// -----------------------------
	public boolean isBugType() {
		return this.getStrTypeToPkType().getId() == 3;
	}

	// -----------------------------
	// Check if attack is Dragon type
	// -----------------------------
	public boolean isDragonType() {
		return this.getStrTypeToPkType().getId() == 4;
	}

	// -----------------------------
	// Check if attack is Steel type
	// -----------------------------
	public boolean isElectricType() {
		return this.getStrTypeToPkType().getId() == 5;
	}

	// -----------------------------
	// Check if attack is Ghost type
	// -----------------------------
	public boolean isGhostType() {
		return this.getStrTypeToPkType().getId() == 6;
	}

	// -----------------------------
	// Check if attack is Fire type
	// -----------------------------
	public boolean isFireType() {
		return this.getStrTypeToPkType().getId() == 7;
	}

	// -----------------------------
	// Check if attack is Fairy type
	// -----------------------------
	public boolean isFairyType() {
		return this.getStrTypeToPkType().getId() == 8;
	}

	// -----------------------------
	// Check if attack is Ice type
	// -----------------------------
	public boolean isIceType() {
		return this.getStrTypeToPkType().getId() == 9;
	}

	// -----------------------------
	// Check if attack is Fighting type
	// -----------------------------
	public boolean isFightingType() {
		return this.getStrTypeToPkType().getId() == 10;
	}

	// -----------------------------
	// Check if attack is Normal type
	// -----------------------------
	public boolean isNormalType() {
		return this.getStrTypeToPkType().getId() == 11;
	}

	// -----------------------------
	// Check if attack is Grass type
	// -----------------------------
	public boolean isGrassType() {
		return this.getStrTypeToPkType().getId() == 12;
	}

	// -----------------------------
	// Check if attack is Psychic type
	// -----------------------------
	public boolean isPsychicType() {
		return this.getStrTypeToPkType().getId() == 13;
	}

	// -----------------------------
	// Check if attack is Rock type
	// -----------------------------
	public boolean isRockType() {
		return this.getStrTypeToPkType().getId() == 14;
	}

	// -----------------------------
	// Check if attack is Dark type
	// -----------------------------
	public boolean isDarkType() {
		return this.getStrTypeToPkType().getId() == 15;
	}

	// -----------------------------
	// Check if attack is Ground type
	// -----------------------------
	public boolean isGroundType() {
		return this.getStrTypeToPkType().getId() == 16;
	}

	// -----------------------------
	// Check if attack is Poison type
	// -----------------------------
	public boolean isPoisonType() {
		return this.getStrTypeToPkType().getId() == 17;
	}

	// -----------------------------
	// Check if attack is Flying type
	// -----------------------------
	public boolean isFlyingType() {
		return this.getStrTypeToPkType().getId() == 18;
	}

	// -----------------------------
	// Check if attack is 19_Fly
	// -----------------------------
	public boolean isFly() {
		return this.getId() == 19;
	}

	// -----------------------------
	// Check if attack is 54_Mist
	// -----------------------------
	public boolean isMist() {
		return this.getId() == 54;
	}

	// -----------------------------
	// Check if attack is 76_Solar_beam
	// -----------------------------
	public boolean isSolarBeam() {
		return this.getId() == 76;
	}

	// -----------------------------
	// Check if attack is 165_Struggle
	// -----------------------------
	public boolean isStruggle() {
		return this.getId() == 165;
	}
}
