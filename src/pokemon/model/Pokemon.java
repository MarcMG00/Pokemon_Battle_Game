package pokemon.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

import pokemon.enums.Sex;
import pokemon.enums.StatType;
import pokemon.enums.StatusConditions;

public class Pokemon {

	// ==================================== FIELDS
	// ====================================

	private int id;
	private String name;
	private float initialPs;
	private float initialAttack;
	private float initialDef;
	private float initialSpeed;
	private float initialSpecialAttack;
	private float initialSpecialDefense;
	private float ps;
	private float attack;
	private float def;
	private float speed;
	private float specialAttack;
	private float specialDefense;
	private ArrayList<Ability> normalAbilities;
	private ArrayList<Ability> hiddenAbilities;
	private ArrayList<PokemonType> types;
	private ArrayList<PokemonType> initialTypes;
	private ArrayList<Attack> physicalAttacks;
	private ArrayList<Attack> specialAttacks;
	private ArrayList<Attack> otherAttacks;
	private ArrayList<Attack> fourPrincipalAttacks;
	private Attack nextMovement;
	private ArrayList<Attack> lotDamageAttacks;
	private ArrayList<Attack> normalAttacks;
	private ArrayList<Attack> lowAttacks;
	private ArrayList<Attack> noEffectAttacks;
	private int precisionPoints;
	private int evasionPoints;
	private State statusCondition;
	private Map<StatusConditions, State> ephemeralStatuses;
	private boolean isChargingAttackForNextRound;
	private boolean canAttack;
	private boolean hasUsedMinimize;
	private boolean hasRetreated;
	private int attackStage;
	private int specialAttackStage;
	private int defenseStage;
	private int specialDefenseStage;
	private int speedStage;
	private Attack lastUsedAttack;
	private boolean canDonAnythingNextRound;
	private int weight;
	private boolean hasReceivedDamage;
	private float damageReceived; // Used for physical attacks (because of some abilities, etc.)
	private boolean isDraining;
	private Ability AbilitySelected; // main ability that will used only to compare abilities (for example 36_Calc)
	private boolean justEnteredBattle;
	private boolean hasSubstitute;
	private boolean isFireBoostActive;
	private Ability currentAbility; // ability that will be used to do effects
	private Player owner;
	private boolean isLevitating;
	private Sex sex;
	private boolean isAttackBoostedFromDownloadAbility;

	// ==================================== CONSTRUCTORS
	// ====================================

	public Pokemon() {
		this.id = 0;
		this.name = "";
		this.ps = 0;
		this.attack = 0;
		this.def = 0;
		this.speed = 0;
		this.specialAttack = 0;
		this.specialDefense = 0;
		this.initialPs = 0;
		this.initialAttack = 0;
		this.initialDef = 0;
		this.initialSpeed = 0;
		this.initialSpecialAttack = 0;
		this.initialSpecialDefense = 0;
		this.normalAbilities = new ArrayList<>();
		this.hiddenAbilities = new ArrayList<>();
		this.types = new ArrayList<>();
		this.physicalAttacks = new ArrayList<>();
		this.specialAttacks = new ArrayList<>();
		this.otherAttacks = new ArrayList<>();
		this.fourPrincipalAttacks = new ArrayList<>();
		this.nextMovement = new Attack();
		this.lotDamageAttacks = new ArrayList<>();
		this.normalAttacks = new ArrayList<>();
		this.lowAttacks = new ArrayList<>();
		this.noEffectAttacks = new ArrayList<>();
		this.precisionPoints = 0;
		this.evasionPoints = 0;
		this.isChargingAttackForNextRound = false;
		this.canAttack = true;
		this.hasUsedMinimize = false;
		this.hasRetreated = false;
		this.attackStage = 0;
		this.specialAttackStage = 0;
		this.defenseStage = 0;
		this.specialDefenseStage = 0;
		this.speedStage = 0;
		this.lastUsedAttack = new Attack();
		this.canDonAnythingNextRound = true;
		this.weight = 1 + (int) (Math.random() * (350 - 1 + 1));
		this.hasReceivedDamage = false;
		this.damageReceived = 0;
		this.isDraining = false;
		this.AbilitySelected = new Ability();
		this.justEnteredBattle = false;
		this.hasSubstitute = false;
		this.initialTypes = new ArrayList<>();
		this.isFireBoostActive = false;
		this.currentAbility = new Ability();
		this.isLevitating = false;
		this.sex = Sex.random();
		this.isAttackBoostedFromDownloadAbility = false;
		this.statusCondition = new State();
		this.ephemeralStatuses = new EnumMap<>(StatusConditions.class);
	}

	public Pokemon(int id, String name, float ps, float attack, float def, float speed, float specialAttack,
			float specialDefense) {
		this.id = id;
		this.name = name;
		this.ps = ps;
		this.attack = attack;
		this.def = def;
		this.speed = speed;
		this.specialAttack = specialAttack;
		this.specialDefense = specialDefense;
		this.initialPs = ps;
		this.initialAttack = speed;
		this.initialDef = def;
		this.initialSpeed = speed;
		this.initialSpecialAttack = specialAttack;
		this.initialSpecialDefense = specialDefense;
		this.normalAbilities = new ArrayList<>();
		this.hiddenAbilities = new ArrayList<>();
		this.types = new ArrayList<>();
		this.physicalAttacks = new ArrayList<>();
		this.specialAttacks = new ArrayList<>();
		this.otherAttacks = new ArrayList<>();
		this.fourPrincipalAttacks = new ArrayList<>();
		this.nextMovement = new Attack();
		this.lotDamageAttacks = new ArrayList<>();
		this.normalAttacks = new ArrayList<>();
		this.lowAttacks = new ArrayList<>();
		this.noEffectAttacks = new ArrayList<>();
		this.precisionPoints = 0;
		this.evasionPoints = 0;
		this.isChargingAttackForNextRound = false;
		this.canAttack = true;
		this.hasUsedMinimize = false;
		this.hasRetreated = false;
		this.attackStage = 0;
		this.specialAttackStage = 0;
		this.defenseStage = 0;
		this.specialDefenseStage = 0;
		this.speedStage = 0;
		this.lastUsedAttack = new Attack();
		this.canDonAnythingNextRound = true;
		this.weight = 1 + (int) (Math.random() * (350 - 1 + 1));
		this.hasReceivedDamage = false;
		this.damageReceived = 0;
		this.isDraining = false;
		this.AbilitySelected = new Ability();
		this.justEnteredBattle = false;
		this.hasSubstitute = false;
		this.initialTypes = new ArrayList<>();
		this.isFireBoostActive = false;
		this.currentAbility = new Ability();
		this.isLevitating = false;
		this.sex = Sex.random();
		this.isAttackBoostedFromDownloadAbility = false;
		this.statusCondition = new State();
		this.ephemeralStatuses = new EnumMap<>(StatusConditions.class);
	}

	// Constructor to set same Pokemon in a different memory space (otherwise, some
	// duplications for the same objects)
	public Pokemon(Pokemon pokemon) {
		this.id = pokemon.id;
		this.name = pokemon.name;
		this.ps = pokemon.initialPs;
		this.attack = pokemon.initialAttack;
		this.def = pokemon.initialDef;
		this.speed = pokemon.initialSpeed;
		this.specialAttack = pokemon.initialSpecialAttack;
		this.specialDefense = pokemon.initialSpecialDefense;
		this.initialPs = ps;
		this.initialAttack = attack;
		this.initialDef = def;
		this.initialSpeed = speed;
		this.initialSpecialAttack = specialAttack;
		this.initialSpecialDefense = specialDefense;
		this.normalAbilities = pokemon.normalAbilities;
		this.hiddenAbilities = pokemon.hiddenAbilities;
		this.types = pokemon.types;

		this.physicalAttacks = (ArrayList<Attack>) pokemon.physicalAttacks.stream().map(Attack::new)
				.collect(Collectors.toList());
		this.specialAttacks = (ArrayList<Attack>) pokemon.specialAttacks.stream().map(Attack::new)
				.collect(Collectors.toList());
		this.otherAttacks = (ArrayList<Attack>) pokemon.otherAttacks.stream().map(Attack::new)
				.collect(Collectors.toList());
		this.fourPrincipalAttacks = new ArrayList<>(); // starts empty

		this.nextMovement = null;
		this.lotDamageAttacks = (ArrayList<Attack>) pokemon.lotDamageAttacks.stream().map(Attack::new)
				.collect(Collectors.toList());
		this.normalAttacks = (ArrayList<Attack>) pokemon.normalAttacks.stream().map(Attack::new)
				.collect(Collectors.toList());
		this.lowAttacks = (ArrayList<Attack>) pokemon.lowAttacks.stream().map(Attack::new).collect(Collectors.toList());
		this.noEffectAttacks = (ArrayList<Attack>) pokemon.noEffectAttacks.stream().map(Attack::new)
				.collect(Collectors.toList());

		this.precisionPoints = 0;
		this.evasionPoints = 0;

		this.isChargingAttackForNextRound = false;
		this.canAttack = true;
		this.hasUsedMinimize = false;
		this.hasRetreated = false;
		this.attackStage = 0;
		this.specialAttackStage = 0;
		this.defenseStage = 0;
		this.specialDefenseStage = 0;
		this.speedStage = 0;
		this.lastUsedAttack = null;
		this.canDonAnythingNextRound = true;
		this.weight = 1 + (int) (Math.random() * (350 - 1 + 1));
		this.hasReceivedDamage = false;
		this.damageReceived = 0f;
		this.isDraining = false;
		this.AbilitySelected = pokemon.AbilitySelected;
		this.justEnteredBattle = false;
		this.hasSubstitute = false;
		this.initialTypes = pokemon.initialTypes;
		this.isFireBoostActive = false;
		this.currentAbility = pokemon.AbilitySelected != null ? new Ability(pokemon.AbilitySelected) : null;
		this.owner = pokemon.owner;
		this.isLevitating = false;
		this.sex = Sex.random();
		this.isAttackBoostedFromDownloadAbility = false;
		this.statusCondition = new State();
		this.ephemeralStatuses = new EnumMap<>(StatusConditions.class);
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

	public float getPs() {
		return ps;
	}

	public void setPs(float ps) {
		this.ps = ps;
	}

	public float getAttack() {
		return attack;
	}

	public float getDef() {
		return def;
	}

	public float getSpeed() {
		return speed;
	}

	public float getSpecialAttack() {
		return specialAttack;
	}

	public float getSpecialDefense() {
		return specialDefense;
	}

	public float getInitialPs() {
		return initialPs;
	}

	public float getInitialAttack() {
		return initialAttack;
	}

	public float getInitialDef() {
		return initialDef;
	}

	public float getInitialSpeed() {
		return initialSpeed;
	}

	public float getInitialSpecialAttack() {
		return initialSpecialAttack;
	}

	public float getInitialSpecialDefense() {
		return initialSpecialDefense;
	}

	public ArrayList<Ability> getNormalAbilities() {
		return normalAbilities;
	}

	public ArrayList<Ability> getHiddenAbilities() {
		return hiddenAbilities;
	}

	public ArrayList<PokemonType> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PokemonType> types) {
		this.types = types;
	}

	public ArrayList<PokemonType> getInitialTypes() {
		return initialTypes;
	}

	public ArrayList<Attack> getPhysicalAttacks() {
		return physicalAttacks;
	}

	public ArrayList<Attack> getSpecialAttacks() {
		return specialAttacks;
	}

	public ArrayList<Attack> getOtherAttacks() {
		return otherAttacks;
	}

	public ArrayList<Attack> getFourPrincipalAttacks() {
		return fourPrincipalAttacks;
	}

	public void setFourPrincipalAttacks(ArrayList<Attack> fourPrincipalAttacks) {
		this.fourPrincipalAttacks = fourPrincipalAttacks;
	}

	public Attack getNextMovement() {
		return nextMovement;
	}

	public void setNextMovement(Attack nextMovement) {
		this.nextMovement = nextMovement;
	}

	public ArrayList<Attack> getLotDamageAttacks() {
		return lotDamageAttacks;
	}

	public void setLotDamageAttacks(ArrayList<Attack> lotDamageAttacks) {
		this.lotDamageAttacks = lotDamageAttacks;
	}

	public ArrayList<Attack> getNormalAttacks() {
		return normalAttacks;
	}

	public void setNormalAttacks(ArrayList<Attack> normalAttacks) {
		this.normalAttacks = normalAttacks;
	}

	public ArrayList<Attack> getLowAttacks() {
		return lowAttacks;
	}

	public void setLowAttacks(ArrayList<Attack> lowAttacks) {
		this.lowAttacks = lowAttacks;
	}

	public ArrayList<Attack> getNoEffectAttacks() {
		return noEffectAttacks;
	}

	public void setNoEffectAttacks(ArrayList<Attack> noEffectAttacks) {
		this.noEffectAttacks = noEffectAttacks;
	}

	public int getPrecisionStage() {
		return precisionPoints;
	}

	public void setPrecisionStage(int precisionPoints) {
		this.precisionPoints = precisionPoints;
	}

	public int getEvasionStage() {
		return evasionPoints;
	}

	public void setEvasionStage(int evasionPoints) {
		this.evasionPoints = evasionPoints;
	}

	public State getStatusCondition() {
		return statusCondition;
	}

	public void setStatusCondition(State statusCondition) {
		this.statusCondition = statusCondition;
	}

	public Map<StatusConditions, State> getEphemeralStatuses() {
		return ephemeralStatuses;
	}

	public boolean getIsChargingAttackForNextRound() {
		return isChargingAttackForNextRound;
	}

	public void setIsChargingAttackForNextRound(boolean isChargingAttackForNextRound) {
		this.isChargingAttackForNextRound = isChargingAttackForNextRound;
	}

	public boolean getCanAttack() {
		return canAttack;
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}

	public boolean getHasUsedMinimize() {
		return hasUsedMinimize;
	}

	public void setHasUsedMinimize(boolean hasUsedMinimize) {
		this.hasUsedMinimize = hasUsedMinimize;
	}

	public boolean getHasRetreated() {
		return hasRetreated;
	}

	public void setHasRetreated(boolean hasRetreated) {
		this.hasRetreated = hasRetreated;
	}

	public int getAttackStage() {
		return attackStage;
	}

	public void setAttackStage(int attackStage) {
		this.attackStage = attackStage;
	}

	public int getSpecialAttackStage() {
		return specialAttackStage;
	}

	public void setSpecialAttackStage(int specialAttackStage) {
		this.specialAttackStage = specialAttackStage;
	}

	public int getDefenseStage() {
		return defenseStage;
	}

	public void setDefenseStage(int defenseStage) {
		this.defenseStage = defenseStage;
	}

	public int getSpecialDefenseStage() {
		return specialDefenseStage;
	}

	public void setSpecialDefenseStage(int specialDefenseStage) {
		this.specialDefenseStage = specialDefenseStage;
	}

	public int getSpeedStage() {
		return speedStage;
	}

	public void setSpeedStage(int speedStage) {
		this.speedStage = speedStage;
	}

	public Attack getLastUsedAttack() {
		return lastUsedAttack;
	}

	public void setLastUsedAttack(Attack lastUsedAttack) {
		this.lastUsedAttack = lastUsedAttack;
	}

	public boolean getCanDonAnythingNextRound() {
		return canDonAnythingNextRound;
	}

	public void setCanDonAnythingNextRound(boolean canDonAnythingNextRound) {
		this.canDonAnythingNextRound = canDonAnythingNextRound;
	}

	public int getWeight() {
		return weight;
	}

	public boolean getHasReceivedDamage() {
		return hasReceivedDamage;
	}

	public void setHasReceivedDamage(boolean hasReceivedDamage) {
		this.hasReceivedDamage = hasReceivedDamage;
	}

	public float getDamageReceived() {
		return damageReceived;
	}

	public void setDamageReceived(float damageReceived) {
		this.damageReceived = damageReceived;
	}

	public boolean getIsDraining() {
		return isDraining;
	}

	public void setIsDraining(boolean isDraining) {
		this.isDraining = isDraining;
	}

	public Ability getAbilitySelected() {
		return currentAbility;
	}

	public void setAbilitySelected(Ability currentAbility) {
		this.currentAbility = currentAbility;
	}

	public boolean getJustEnteredBattle() {
		return justEnteredBattle;
	}

	public void setJustEnteredBattle(boolean justEnteredBattle) {
		this.justEnteredBattle = justEnteredBattle;
	}

	public boolean getHasSubstitute() {
		return hasSubstitute;
	}

	public void setHasSubstitute(boolean hasSubstitute) {
		this.hasSubstitute = hasSubstitute;
	}

	public boolean getIsFireBoostActive() {
		return isFireBoostActive;
	}

	public void setIsFireBoostActive(boolean isFireBoostActive) {
		this.isFireBoostActive = isFireBoostActive;
	}

	public Ability getBaseAbility() {
		return AbilitySelected;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setBaseAbility(Ability abilitySelected) {
		this.AbilitySelected = abilitySelected;
	}

	public boolean getIsLevitating() {
		return isLevitating;
	}

	public void setIsLevitating(boolean isLevitating) {
		this.isLevitating = isLevitating;
	}

	public Sex getSex() {
		return sex;
	}

	public boolean getIsAttackBoostedFromDownloadAbility() {
		return isAttackBoostedFromDownloadAbility;
	}

	public void setIsAttackBoostedFromDownloadAbility(boolean isAttackBoostedFromDownloadAbility) {
		this.isAttackBoostedFromDownloadAbility = isAttackBoostedFromDownloadAbility;
	}

	// ==================================== METHODS
	// ====================================

	// Adds abilities to Pokemon
	public void addNormalAbility(Ability ablty) {
		this.normalAbilities.add(ablty);
	}

	// Adds hidden abilities to Pokemon
	public void addHiddenAbility(Ability hidAblty) {
		this.hiddenAbilities.add(hidAblty);
	}

	// Adds types to Pokemon
	public void addType(PokemonType pt) {
		this.types.add(pt);
	}

	// Adds initial types to Pokemon
	public void addInitialType(PokemonType pt) {
		this.initialTypes.add(pt);
	}

	// Adds physical attacks to Pokemon
	public void addPhysicalAttack(Attack phAtck) {
		this.physicalAttacks.add(phAtck);
	}

	// Adds special attacks to Pokemon
	public void addSpecialAttack(Attack spAtck) {
		this.specialAttacks.add(spAtck);
	}

	// Adds other attacks to Pokemon
	public void addOtherAttack(Attack otAtck) {
		this.otherAttacks.add(otAtck);
	}

	// Adds the four principal attacks to Pokemon
	public void addAttacks(Attack attack) {
		this.fourPrincipalAttacks.add(attack);
	}

	// -----------------------------
	// Check if has normal status conditions
	// -----------------------------
	public boolean hasStatusCondition() {
		return this.getStatusCondition().getStatusCondition() != StatusConditions.NO_STATUS;
	}

	// -----------------------------
	// Check if has a specific status condition
	// -----------------------------
	public boolean hasActiveStatusCondition(StatusConditions status) {
		return this.getStatusCondition().getStatusCondition() == status;
	}

	// -----------------------------
	// Check if has ephemeral status
	// -----------------------------
	public boolean hasEphemeralStatus() {
		return !this.getEphemeralStatuses().isEmpty();
	}

	// -----------------------------
	// Check if has a specific ephemeral status
	// -----------------------------
	public boolean hasActiveEphemeralStatus(StatusConditions status) {
		return this.getEphemeralStatuses().containsKey(status);
	}

	// -----------------------------
	// Put ephemeral status to map
	// -----------------------------
	public void addEphemeralStatus(StatusConditions status, State state) {
		this.getEphemeralStatuses().put(status, state);
	}

	// -----------------------------
	// Remove ephemeral status from map
	// -----------------------------
	public void removeEphemeralStatus(StatusConditions status) {
		this.getEphemeralStatuses().remove(status);
	}

	// -----------------------------
	// Get a specific ephemeral status from map
	// -----------------------------
	public State getEphemeralStatus(StatusConditions status) {
		return this.getEphemeralStatuses().get(status);
	}

	// -----------------------------
	// Check if is debilitated
	// -----------------------------
	public boolean isDebilitated() {
		return this.getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED;
	}

	// -----------------------------
	// Restart stats after some attacks... (cause not accumulated)
	// -----------------------------
	public void restartParametersEffect() {
		// Can move and attack
		this.getStatusCondition().setCanMoveStatusCondition(true);
		this.setCanAttack(true);

		// Reset damage received
		this.setHasReceivedDamage(false);
		this.setDamageReceived(0f);
		this.setJustEnteredBattle(false);
	}

	// -----------------------------
	// Get Attack by Id
	// -----------------------------
	public Attack getNextMovementById(int id) {
		return this.getFourPrincipalAttacks().stream().filter(a -> a.getId() == id).findFirst().orElse(null);
	}

	// -----------------------------
	// Check if have some ability
	// -----------------------------
	public boolean hasAbility(int abilityId) {
		return this.getAbilitySelected().getId() == abilityId;
	}

	// -----------------------------
	// Check if can be flinched
	// -----------------------------
	public boolean canBeFlinched() {
		// 98_Magic_Guard annuls secondary damage effects (only by struggle attack)
		if (this.getAbilitySelected().getId() == 98 && this.getNextMovement().getId() != 165)
			return false;

		if (this.getAbilitySelected().getId() == 39) {
			System.out.println(this.getName() + " (Id:" + this.getId() + ")"
					+ " no pudo retroceder dada su habilidad Fuerza mental");
			return false;
		}
		return true;
	}

	// -----------------------------
	// Get stage from specific stat
	// -----------------------------
	public int getStage(StatType stat) {
		switch (stat) {
		case ATTACK:
			return getAttackStage();
		case SPECIAL_ATTACK:
			return getSpecialAttackStage();
		case DEFENSE:
			return getDefenseStage();
		case SPECIAL_DEFENSE:
			return getSpecialDefenseStage();
		case PRECISION:
			return getPrecisionStage();
		case SPEED:
			return getSpeedStage();
		default:
			throw new IllegalArgumentException("Unknown stat " + stat);
		}
	}

	// -----------------------------
	// Set stage value on stats
	// -----------------------------
	public void setStageValueStats(StatType statType, int nbStage, boolean isStatDrop) {
		switch (statType) {
		case ATTACK:
			if (isStatDrop)
				this.setAttackStage(Math.max(this.getAttackStage() - nbStage, -6));
			else
				this.setAttackStage(Math.min(this.getAttackStage() + nbStage, 6));
			break;
		case SPECIAL_ATTACK:
			if (isStatDrop)
				this.setSpecialAttackStage(Math.max(this.getSpecialAttackStage() - nbStage, -6));
			else
				this.setSpecialAttackStage(Math.min(this.getSpecialAttackStage() + nbStage, 6));
			break;
		case DEFENSE:
			if (isStatDrop)
				this.setDefenseStage(Math.max(this.getDefenseStage() - nbStage, -6));
			else
				this.setDefenseStage(Math.min(this.getDefenseStage() + nbStage, 6));
			break;
		case SPECIAL_DEFENSE:
			if (isStatDrop)
				this.setSpecialDefenseStage(Math.max(this.getSpecialDefenseStage() - nbStage, -6));
			else
				this.setSpecialDefenseStage(Math.min(this.getSpecialDefenseStage() + nbStage, 6));
			break;
		case PRECISION:
			if (isStatDrop)
				this.setPrecisionStage(Math.max(this.getPrecisionStage() - nbStage, -6));
			else
				this.setPrecisionStage(Math.min(this.getPrecisionStage() + nbStage, 6));
			break;
		case SPEED:
			if (isStatDrop)
				this.setSpeedStage(Math.max(this.getSpeedStage() - nbStage, -6));
			else
				this.setSpeedStage(Math.min(this.getSpeedStage() + nbStage, 6));
			break;
		default:
			break;
		}
	}

	// -----------------------------
	// Don't allow to attack (PkVsPk)
	// -----------------------------
	public void denyAttack() {
		this.setCanAttack(false);
	}

	// -----------------------------
	// Allow to attack (PkVsPk)
	// -----------------------------
	public void allowAttack() {
		this.setCanAttack(true);
	}

	// -----------------------------
	// Check if Pokemon has the attack chosen
	// -----------------------------
	public boolean hasAttack(int attackId) {
		return this.getFourPrincipalAttacks().stream().anyMatch(a -> a.getId() == attackId);
	}

	// -----------------------------
	// Check attack chosen has PP remaining
	// -----------------------------
	public boolean hasPP(int attackId) {
		Attack atk = this.getNextMovementById(attackId);
		return atk.getPp() > 0;
	}

	// -----------------------------
	// Check if any attack from Pokemon has PP remaining
	// -----------------------------
	public boolean hasAnyPPLeft() {
		return this.getFourPrincipalAttacks().stream().anyMatch(a -> a.getPp() > 0);
	}
}
