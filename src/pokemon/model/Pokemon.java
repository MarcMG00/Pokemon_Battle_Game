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

	public boolean isChargingAttackForNextRound() {
		return isChargingAttackForNextRound;
	}

	public void setIsChargingAttackForNextRound(boolean isChargingAttackForNextRound) {
		this.isChargingAttackForNextRound = isChargingAttackForNextRound;
	}

	public boolean canAttack() {
		return canAttack;
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}

	public boolean hasUsedMinimize() {
		return hasUsedMinimize;
	}

	public void setHasUsedMinimize(boolean hasUsedMinimize) {
		this.hasUsedMinimize = hasUsedMinimize;
	}

	public boolean hasRetreated() {
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

	public boolean canDonAnythingNextRound() {
		return canDonAnythingNextRound;
	}

	public void setCanDonAnythingNextRound(boolean canDonAnythingNextRound) {
		this.canDonAnythingNextRound = canDonAnythingNextRound;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean hasReceivedDamage() {
		return hasReceivedDamage;
	}

	// Only applied for attacks of "Contact" type
	public void setHasReceivedDamage(boolean hasReceivedDamage) {
		this.hasReceivedDamage = hasReceivedDamage;
	}

	public float getDamageReceived() {
		return damageReceived;
	}

	public void setDamageReceived(float damageReceived) {
		this.damageReceived = damageReceived;
	}

	public boolean isDraining() {
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

	public boolean justEnteredBattle() {
		return justEnteredBattle;
	}

	public void setJustEnteredBattle(boolean justEnteredBattle) {
		this.justEnteredBattle = justEnteredBattle;
	}

	public boolean hasSubstitute() {
		return hasSubstitute;
	}

	public void setHasSubstitute(boolean hasSubstitute) {
		this.hasSubstitute = hasSubstitute;
	}

	public boolean isFireBoostActive() {
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

	public boolean isLevitating() {
		return isLevitating;
	}

	public void setIsLevitating(boolean isLevitating) {
		this.isLevitating = isLevitating;
	}

	public Sex getSex() {
		return sex;
	}

	public boolean isAttackBoostedFromDownloadAbility() {
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
		return this.getStatusCondition().getStatusCondition() != StatusConditions.NO_STATUS
				// Get asleep state (because it has a number of turns, it works like an
				// ephemeral status, but it's a normal status condition)
				|| hasActiveEphemeralStatus(StatusConditions.ASLEEP);
	}

	// -----------------------------
	// Check if has a specific status condition
	// -----------------------------
	public boolean hasActiveStatusCondition(StatusConditions status) {
		return hasStatusCondition() && this.getStatusCondition().getStatusCondition() == status;
	}

	// -----------------------------
	// Check if has ephemeral status
	// -----------------------------
	public boolean hasEphemeralStatus() {
		return !this.getEphemeralStatuses().isEmpty()
				// ASLEEP is a status condition
				&& (this.getEphemeralStatuses().size() == 1 ? !hasActiveEphemeralStatus(StatusConditions.ASLEEP)
						: true);
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
	public boolean isFainted() {
		return this.getPs() <= 0 || this.getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED;
	}

	// -----------------------------
	// Restart stats after some attacks... (cause not accumulated)
	// -----------------------------
	public void restartParametersEffectInitialTurn() {
		this.setJustEnteredBattle(false);
	}

	// -----------------------------
	// Restart stats after some attacks... (cause not accumulated)
	// -----------------------------
	public void restartParametersEffectEndTurn() {
		this.setCanAttack(true);
		this.setHasRetreated(false);

		// Reset damage received
		this.setHasReceivedDamage(false);
		this.setDamageReceived(0f);
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
		if (hasMagicGuardAbility() && !this.getNextMovement().isStruggle())
			return false;

		if (hasInnerFocusAbility()) {
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
	// Don't allow to attack
	// -----------------------------
	public void denyAttack() {
		this.setCanAttack(false);
	}

	// -----------------------------
	// Allow to attack
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
		return atk != null && atk.getPp() > 0;
	}

	// -----------------------------
	// Check if any attack from Pokemon has PP remaining
	// -----------------------------
	public boolean hasAnyPPLeft() {
		return this.getFourPrincipalAttacks().stream().anyMatch(a -> a.getPp() > 0);
	}

	// -----------------------------
	// Check if current PS are at or below one third from max PS
	// -----------------------------
	public boolean isPSAtOrBelowOneThird() {
		return this.getPs() < this.getInitialPs() / 3;
	}

	public boolean hasMaxPS() {
		return this.getPs() >= this.getInitialPs();
	}

	// -----------------------------
	// Check if Pokemon has 1_Stench ability
	// -----------------------------
	public boolean hasStenchAbility() {
		return this.getAbilitySelected().getId() == 1;
	}

	// -----------------------------
	// Check if Pokemon has 4_Battle_armor ability
	// -----------------------------
	public boolean hasBattleArmorAbility() {
		return this.getAbilitySelected().getId() == 4;
	}

	// -----------------------------
	// Check if Pokemon has 5_Sturdy ability
	// -----------------------------
	public boolean hasSturdyAbility() {
		return this.getAbilitySelected().getId() == 5;
	}

	// -----------------------------
	// Check if Pokemon has 6_Damp ability
	// -----------------------------
	public boolean hasDampAbility() {
		return this.getAbilitySelected().getId() == 6;
	}

	// -----------------------------
	// Check if Pokemon has 7_Limber ability
	// -----------------------------
	public boolean hasLimberAbility() {
		return this.getAbilitySelected().getId() == 7;
	}

	// -----------------------------
	// Check if Pokemon has 8_Sand_veil ability
	// -----------------------------
	public boolean hasSandVeilAbility() {
		return this.getAbilitySelected().getId() == 8;
	}

	// -----------------------------
	// Check if Pokemon has 9_Static ability
	// -----------------------------
	public boolean hasStaticAbility() {
		return this.getAbilitySelected().getId() == 9;
	}

	// -----------------------------
	// Check if Pokemon has 12_Oblivious ability
	// -----------------------------
	public boolean hasObliviousAbility() {
		return this.getAbilitySelected().getId() == 12;
	}

	// -----------------------------
	// Check if Pokemon has 13_Cloud_nine ability
	// -----------------------------
	public boolean hasCloudNineAbility() {
		return this.getAbilitySelected().getId() == 13;
	}

	// -----------------------------
	// Check if Pokemon has 14_Compound_eyes ability
	// -----------------------------
	public boolean hasCompoundEyesAbility() {
		return this.getAbilitySelected().getId() == 14;
	}

	// -----------------------------
	// Check if Pokemon has 15_Insomnia ability
	// -----------------------------
	public boolean hasInsomniaAbility() {
		return this.getAbilitySelected().getId() == 15;
	}

	// -----------------------------
	// Check if Pokemon has 17_Immunity ability
	// -----------------------------
	public boolean hasImmunityAbility() {
		return this.getAbilitySelected().getId() == 17;
	}

	// -----------------------------
	// Check if Pokemon has 19_SHield_dust ability
	// -----------------------------
	public boolean hasShieldDustAbility() {
		return this.getAbilitySelected().getId() == 19;
	}

	// -----------------------------
	// Check if Pokemon has 20_Own_tempo ability
	// -----------------------------
	public boolean hasOwnTempoAbility() {
		return this.getAbilitySelected().getId() == 20;
	}

	// -----------------------------
	// Check if Pokemon has 21_Suction_cups ability
	// -----------------------------
	public boolean hasSuctionCupsAbility() {
		return this.getAbilitySelected().getId() == 21;
	}

	// -----------------------------
	// Check if Pokemon has 23_Shadow_tag ability
	// -----------------------------
	public boolean hasShadowTagAbility() {
		return this.getAbilitySelected().getId() == 23;
	}

	// -----------------------------
	// Check if Pokemon has 24_Rough_skin ability
	// -----------------------------
	public boolean hasRoughSkinAbility() {
		return this.getAbilitySelected().getId() == 24;
	}

	// -----------------------------
	// Check if Pokemon has 26_Levitate ability
	// -----------------------------
	public boolean hasLevitateAbility() {
		return this.getAbilitySelected().getId() == 26;
	}

	// -----------------------------
	// Check if Pokemon has 27_Effect_spore ability
	// -----------------------------
	public boolean hasEffectSporeAbility() {
		return this.getAbilitySelected().getId() == 27;
	}

	// -----------------------------
	// Check if Pokemon has 29_Clear_body ability
	// -----------------------------
	public boolean hasClearBodyAbility() {
		return this.getAbilitySelected().getId() == 29;
	}

	// -----------------------------
	// Check if Pokemon has 32_Serene_grace ability
	// -----------------------------
	public boolean hasSereneGraceAbility() {
		return this.getAbilitySelected().getId() == 32;
	}

	// -----------------------------
	// Check if Pokemon has 33_Swift_swim ability
	// -----------------------------
	public boolean hasSwiftSwimAbility() {
		return this.getAbilitySelected().getId() == 33;
	}

	// -----------------------------
	// Check if Pokemon has 34_Chlorophyll ability
	// -----------------------------
	public boolean hasChlorophyllAbility() {
		return this.getAbilitySelected().getId() == 34;
	}

	// -----------------------------
	// Check if Pokemon has 35_Illuminate ability
	// -----------------------------
	public boolean hasIlluminateAbility() {
		return this.getAbilitySelected().getId() == 35;
	}

	// -----------------------------
	// Check if Pokemon has 36_Trace ability
	// -----------------------------
	public boolean hasTraceAbility() {
		return this.getAbilitySelected().getId() == 36;
	}

	// -----------------------------
	// Check if Pokemon has 37_Huge_power ability
	// -----------------------------
	public boolean hasHugePowerAbility() {
		return this.getAbilitySelected().getId() == 37;
	}

	// -----------------------------
	// Check if Pokemon has 38_Poison_point ability
	// -----------------------------
	public boolean hasPoisonPointAbility() {
		return this.getAbilitySelected().getId() == 38;
	}

	// -----------------------------
	// Check if Pokemon has 39_Inner_Focus ability
	// -----------------------------
	public boolean hasInnerFocusAbility() {
		return this.getAbilitySelected().getId() == 39;
	}

	// -----------------------------
	// Check if Pokemon has 40_Magma_armor ability
	// -----------------------------
	public boolean hasMagmaArmorAbility() {
		return this.getAbilitySelected().getId() == 40;
	}

	// -----------------------------
	// Check if Pokemon has 41_Water_vail ability
	// -----------------------------
	public boolean hasWaterVailAbility() {
		return this.getAbilitySelected().getId() == 41;
	}

	// -----------------------------
	// Check if Pokemon has 42_Magnet_pull ability
	// -----------------------------
	public boolean hasMagnetPullAbility() {
		return this.getAbilitySelected().getId() == 42;
	}

	// -----------------------------
	// Check if Pokemon has 44_Rain_dish ability
	// -----------------------------
	public boolean hasRainDishAbility() {
		return this.getAbilitySelected().getId() == 44;
	}

	// -----------------------------
	// Check if Pokemon has 47_Thick_fat ability
	// -----------------------------
	public boolean hasThickFatAbility() {
		return this.getAbilitySelected().getId() == 47;
	}

	// -----------------------------
	// Check if Pokemon has 48_Early_bird ability
	// -----------------------------
	public boolean hasEarlyBirdAbility() {
		return this.getAbilitySelected().getId() == 48;
	}

	// -----------------------------
	// Check if Pokemon has 49_Flame_body ability
	// -----------------------------
	public boolean hasFlameBodtyAbility() {
		return this.getAbilitySelected().getId() == 49;
	}

	// -----------------------------
	// Check if Pokemon has 51_Keen_eye ability
	// -----------------------------
	public boolean hasKeenEyeAbility() {
		return this.getAbilitySelected().getId() == 51;
	}

	// -----------------------------
	// Check if Pokemon has 52_Hyoer_cutter ability
	// -----------------------------
	public boolean hasHyperCutterAbility() {
		return this.getAbilitySelected().getId() == 52;
	}

	// -----------------------------
	// Check if Pokemon has 54_Truant ability
	// -----------------------------
	public boolean hasTruantAbility() {
		return this.getAbilitySelected().getId() == 54;
	}

	// -----------------------------
	// Check if Pokemon has 55_Hustle ability
	// -----------------------------
	public boolean hasHustleAbility() {
		return this.getAbilitySelected().getId() == 55;
	}

	// -----------------------------
	// Check if Pokemon has 56_Cute_charm ability
	// -----------------------------
	public boolean hasCuteCharmAbility() {
		return this.getAbilitySelected().getId() == 56;
	}

	// -----------------------------
	// Check if Pokemon has 57_Plus ability
	// -----------------------------
	public boolean hasPlusAbility() {
		return this.getAbilitySelected().getId() == 57;
	}

	// -----------------------------
	// Check if Pokemon has 58_Minus ability
	// -----------------------------
	public boolean hasMinusAbility() {
		return this.getAbilitySelected().getId() == 58;
	}

	// -----------------------------
	// Check if Pokemon has 59_Forecast ability
	// -----------------------------
	public boolean hasForecastAbility() {
		return this.getAbilitySelected().getId() == 59;
	}

	// -----------------------------
	// Check if Pokemon has 61_Shed_skin ability
	// -----------------------------
	public boolean hasShedSkinAbility() {
		return this.getAbilitySelected().getId() == 61;
	}

	// -----------------------------
	// Check if Pokemon has 62_Guts ability
	// -----------------------------
	public boolean hasGutsAbility() {
		return this.getAbilitySelected().getId() == 62;
	}

	// -----------------------------
	// Check if Pokemon has 63_Marvel_scale ability
	// -----------------------------
	public boolean hasMarvelScaleAbility() {
		return this.getAbilitySelected().getId() == 63;
	}

	// -----------------------------
	// Check if Pokemon has 64_Liquid_Ooze ability
	// -----------------------------
	public boolean hasLiquidOozeAbility() {
		return this.getAbilitySelected().getId() == 64;
	}

	// -----------------------------
	// Check if Pokemon has 65_Overgrow ability
	// -----------------------------
	public boolean hasOvergrowAbility() {
		return this.getAbilitySelected().getId() == 65;
	}

	// -----------------------------
	// Check if Pokemon has 66_Blaze ability
	// -----------------------------
	public boolean hasBlazeAbility() {
		return this.getAbilitySelected().getId() == 66;
	}

	// -----------------------------
	// Check if Pokemon has 67_Torrent ability
	// -----------------------------
	public boolean hasTorrentAbility() {
		return this.getAbilitySelected().getId() == 67;
	}

	// -----------------------------
	// Check if Pokemon has 68_Swarm ability
	// -----------------------------
	public boolean hasSwarmAbility() {
		return this.getAbilitySelected().getId() == 68;
	}

	// -----------------------------
	// Check if Pokemon has 69_Rock_head ability
	// -----------------------------
	public boolean hasRockHeadAbility() {
		return this.getAbilitySelected().getId() == 69;
	}

	// -----------------------------
	// Check if Pokemon has 71_Arena_trap ability
	// -----------------------------
	public boolean hasArenaTrapAbility() {
		return this.getAbilitySelected().getId() == 71;
	}

	// -----------------------------
	// Check if Pokemon has 72_Vital_spirit ability
	// -----------------------------
	public boolean hasVitalSpiritAbility() {
		return this.getAbilitySelected().getId() == 72;
	}

	// -----------------------------
	// Check if Pokemon has 73_White_smoke ability
	// -----------------------------
	public boolean hasWhiteSmokeAbility() {
		return this.getAbilitySelected().getId() == 73;
	}

	// -----------------------------
	// Check if Pokemon has 74_Pure_power ability
	// -----------------------------
	public boolean hasPurePowerAbility() {
		return this.getAbilitySelected().getId() == 74;
	}

	// -----------------------------
	// Check if Pokemon has 75_Shell_armor ability
	// -----------------------------
	public boolean hasShellArmorAbility() {
		return this.getAbilitySelected().getId() == 75;
	}

	// -----------------------------
	// Check if Pokemon has 76_Air_lock ability
	// -----------------------------
	public boolean hasAirLockAbility() {
		return this.getAbilitySelected().getId() == 76;
	}

	// -----------------------------
	// Check if Pokemon has 77_Tangled_feet ability
	// -----------------------------
	public boolean hasTangledFeetAbility() {
		return this.getAbilitySelected().getId() == 77;
	}

	// -----------------------------
	// 77_Tangled_feet ability duplicates evasion by 2 if confused
	// -----------------------------
	public boolean isTangledFeetActive() {
		return hasTangledFeetAbility() && hasActiveEphemeralStatus(StatusConditions.CONFUSED);
	}

	// -----------------------------
	// Check if Pokemon has 79_Rivalry ability
	// -----------------------------
	public boolean hasRivalryAbility() {
		return this.getAbilitySelected().getId() == 79;
	}

	// -----------------------------
	// Check if Pokemon has 80_Steadfast ability
	// -----------------------------
	public boolean hasSteadfastAbility() {
		return this.getAbilitySelected().getId() == 80;
	}

	// -----------------------------
	// Check if Pokemon has 81_Snow_cloak ability
	// -----------------------------
	public boolean hasSnowCloakAbility() {
		return this.getAbilitySelected().getId() == 81;
	}

	// -----------------------------
	// Check if Pokemon has 85_HeatProof ability
	// -----------------------------
	public boolean hasHeatProofAbility() {
		return this.getAbilitySelected().getId() == 85;
	}

	// -----------------------------
	// Check if Pokemon has 86_Simple ability
	// -----------------------------
	public boolean hasSimpleAbility() {
		return this.getAbilitySelected().getId() == 86;
	}

	// -----------------------------
	// Check if Pokemon has 87_Dry_skin ability
	// -----------------------------
	public boolean hasDrySkinAbility() {
		return this.getAbilitySelected().getId() == 87;
	}

	// -----------------------------
	// Check if Pokemon has 89_Iron_fist ability
	// -----------------------------
	public boolean hasIronFistAbility() {
		return this.getAbilitySelected().getId() == 89;
	}

	// -----------------------------
	// Check if Pokemon has 90_Poison_heal ability
	// -----------------------------
	public boolean hasPoisonHealAbility() {
		return this.getAbilitySelected().getId() == 90;
	}

	// -----------------------------
	// Check if Pokemon has 91_Adaptability ability
	// -----------------------------
	public boolean hasAdaptabilityAbility() {
		return this.getAbilitySelected().getId() == 91;
	}

	// -----------------------------
	// Check if Pokemon has 92_Skill_link ability
	// -----------------------------
	public boolean hasSkillLinkAbility() {
		return this.getAbilitySelected().getId() == 92;
	}

	// -----------------------------
	// Check if Pokemon has 94_Solar_power ability
	// -----------------------------
	public boolean hasSolarPowerAbility() {
		return this.getAbilitySelected().getId() == 94;
	}

	// -----------------------------
	// Check if Pokemon has 95_Quick_feet ability
	// -----------------------------
	public boolean hasQuickFeetAbility() {
		return this.getAbilitySelected().getId() == 95;
	}

	// -----------------------------
	// Check if Pokemon has 96_Normalize ability
	// -----------------------------
	public boolean hasNormalizeAbility() {
		return this.getAbilitySelected().getId() == 96;
	}

	// -----------------------------
	// Check if Pokemon has 97_Sniper ability
	// -----------------------------
	public boolean hasSniperAbility() {
		return this.getAbilitySelected().getId() == 97;
	}

	// -----------------------------
	// Check if Pokemon has 98_Magic_Guard ability
	// -----------------------------
	public boolean hasMagicGuardAbility() {
		return this.getAbilitySelected().getId() == 98;
	}

	// -----------------------------
	// Check if Pokemon has 99_No_guard ability
	// -----------------------------
	public boolean hasNoGuardAbility() {
		return this.getAbilitySelected().getId() == 99;
	}

	// -----------------------------
	// Check if Pokemon has 100_Stall ability
	// -----------------------------
	public boolean hasStallAbility() {
		return this.getAbilitySelected().getId() == 100;
	}

	// -----------------------------
	// Check if Pokemon has 101_Technician ability
	// -----------------------------
	public boolean hasTechnicianAbility() {
		return this.getAbilitySelected().getId() == 101;
	}

	// -----------------------------
	// Check if Pokemon has 102_Leaf_guard ability
	// -----------------------------
	public boolean hasLeafGuardAbility() {
		return this.getAbilitySelected().getId() == 102;
	}

	// -----------------------------
	// Check if Pokemon has 105_Super_lock ability
	// -----------------------------
	public boolean hasSuperLockAbility() {
		return this.getAbilitySelected().getId() == 105;
	}

	// -----------------------------
	// Check if Pokemon has 109_Unaware ability
	// -----------------------------
	public boolean hasUnawareAbility() {
		return this.getAbilitySelected().getId() == 109;
	}

	// -----------------------------
	// Check if Pokemon has 110_Tinted_lens ability
	// -----------------------------
	public boolean hasTintedLensAbility() {
		return this.getAbilitySelected().getId() == 110;
	}

	// -----------------------------
	// Check if Pokemon has 111_Filter ability
	// -----------------------------
	public boolean hasFilterAbility() {
		return this.getAbilitySelected().getId() == 111;
	}

	// -----------------------------
	// Check if Pokemon has 113_Scrappy ability
	// -----------------------------
	public boolean hasScrappyAbility() {
		return this.getAbilitySelected().getId() == 113;
	}

	// -----------------------------
	// Check if Pokemon has 116_Solid_rock ability
	// -----------------------------
	public boolean hasSolidRockAbility() {
		return this.getAbilitySelected().getId() == 116;
	}

	// -----------------------------
	// Check if Pokemon has 120_Reckless ability
	// -----------------------------
	public boolean hasRecklessAbility() {
		return this.getAbilitySelected().getId() == 120;
	}

	// -----------------------------
	// Check if Pokemon has 122_Flowe_gift ability
	// -----------------------------
	public boolean hasFlowerGiftAbility() {
		return this.getAbilitySelected().getId() == 122;
	}

	// -----------------------------
	// Check if Pokemon has 123_Bad_dreams ability
	// -----------------------------
	public boolean hasBadDreamsAbility() {
		return this.getAbilitySelected().getId() == 123;
	}

	// -----------------------------
	// Check if Pokemon has 125_Sheer_force ability
	// -----------------------------
	public boolean hasSheerForceAbility() {
		return this.getAbilitySelected().getId() == 125;
	}

	// -----------------------------
	// Check if Pokemon has 126_Contrary ability
	// -----------------------------
	public boolean hasContraryAbility() {
		return this.getAbilitySelected().getId() == 126;
	}

	// -----------------------------
	// Check if Pokemon has 128_Defiant ability
	// -----------------------------
	public boolean hasDefiantAbility() {
		return this.getAbilitySelected().getId() == 128;
	}

	// -----------------------------
	// Check if Pokemon has 129_Defeatist ability
	// -----------------------------
	public boolean hasDefeatistAbility() {
		return this.getAbilitySelected().getId() == 129;
	}

	// -----------------------------
	// 129_Deafeatist ability reduces attack by 50% if PS under 50% of initial PS
	// -----------------------------
	public boolean isDefeatistActive() {
		return hasDefeatistAbility() && getPs() <= getInitialPs() / 2;
	}

	// -----------------------------
	// Check if Pokemon has 130_Cursed_body ability
	// -----------------------------
	public boolean hasCursedBodyAbility() {
		return this.getAbilitySelected().getId() == 130;
	}

	// -----------------------------
	// Check if Pokemon has 133_Weak_armor ability
	// -----------------------------
	public boolean hasWeakArmorAbility() {
		return this.getAbilitySelected().getId() == 133;
	}

	// -----------------------------
	// Check if Pokemon has 135_Multiscale ability
	// -----------------------------
	public boolean hasMultiscaleAbility() {
		return this.getAbilitySelected().getId() == 135;
	}

	// -----------------------------
	// Check if Pokemon has 142_Overcoat ability
	// -----------------------------
	public boolean hasOvercoatAbility() {
		return this.getAbilitySelected().getId() == 142;
	}

	// -----------------------------
	// Check if Pokemon has 146_Sand_rash ability
	// -----------------------------
	public boolean hasSandRashAbility() {
		return this.getAbilitySelected().getId() == 146;
	}

	// -----------------------------
	// Check if Pokemon has 159_Sand_force ability
	// -----------------------------
	public boolean hasSandForceAbility() {
		return this.getAbilitySelected().getId() == 159;
	}

	// -----------------------------
	// Check if Pokemon has 165_Aroma_veil ability
	// -----------------------------
	public boolean hasAromaVeilAbility() {
		return this.getAbilitySelected().getId() == 165;
	}
}
