package pokemon.model;

import java.util.ArrayList;

import pokemon.enums.StatusConditions;

public class Pokemon {
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
	private ArrayList<Attack> physicalAttacks;
	private ArrayList<Attack> specialAttacks;
	private ArrayList<Attack> otherAttacks;
	private ArrayList<Attack> fourPrincipalAttacks;
	private Attack nextMovement;
	private ArrayList<Attack> lotDamageAttacks;
	private ArrayList<Attack> normalAttacks;
	private ArrayList<Attack> lowAttacks;
	private ArrayList<Attack> notEffectAttacks;
	private ArrayList<Integer> fourIdAttacks;
	private int precisionPoints;
	private int evasionPoints;
	private State statusCondition;
	private ArrayList<State> ephemeralStates;
	public boolean isChargingAttackForNextRound;
	public boolean canAttack;
	public boolean alreadyUsedTwoTurnAttackBehavior;
	public boolean hasUsedMinimize;
	public boolean hasRetreated;

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
		this.notEffectAttacks = new ArrayList<>();
		this.fourIdAttacks = new ArrayList<>();
		this.precisionPoints = 0;
		this.evasionPoints = 0;
		this.statusCondition = new State();
		this.ephemeralStates = new ArrayList<>();
		this.isChargingAttackForNextRound = false;
		this.canAttack = true;
		this.alreadyUsedTwoTurnAttackBehavior = false;
		this.hasUsedMinimize = false;
		this.hasRetreated = false;
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
		this.notEffectAttacks = new ArrayList<>();
		this.fourIdAttacks = new ArrayList<>();
		this.precisionPoints = 0;
		this.evasionPoints = 0;
		this.statusCondition = new State();
		this.ephemeralStates = new ArrayList<>();
		this.isChargingAttackForNextRound = false;
		this.canAttack = true;
		this.alreadyUsedTwoTurnAttackBehavior = false;
		this.hasUsedMinimize = false;
		this.hasRetreated = false;
	}

	// Constructor to set same Pokemon in a different memory space (otherwise, some
	// duplications for the same objects)
	public Pokemon(Pokemon pokemon) {
		this.id = pokemon.id;
		this.name = pokemon.name;
		this.ps = pokemon.ps;
		this.attack = pokemon.attack;
		this.def = pokemon.def;
		this.speed = pokemon.speed;
		this.specialAttack = pokemon.specialAttack;
		this.specialDefense = pokemon.specialDefense;
		this.initialPs = ps;
		this.initialAttack = attack;
		this.initialDef = def;
		this.initialSpeed = speed;
		this.initialSpecialAttack = specialAttack;
		this.initialSpecialDefense = specialDefense;
		this.normalAbilities = pokemon.normalAbilities;
		this.hiddenAbilities = pokemon.hiddenAbilities;
		this.types = pokemon.types;

		this.physicalAttacks = new ArrayList<>(pokemon.physicalAttacks);
		this.specialAttacks = new ArrayList<>(pokemon.specialAttacks);
		this.otherAttacks = new ArrayList<>(pokemon.otherAttacks);
		this.fourPrincipalAttacks = new ArrayList<>(); // starts empty
		this.fourIdAttacks = new ArrayList<>();

		this.nextMovement = pokemon.nextMovement;
		this.lotDamageAttacks = new ArrayList<>(pokemon.lotDamageAttacks);
		this.normalAttacks = new ArrayList<>(pokemon.normalAttacks);
		this.lowAttacks = new ArrayList<>(pokemon.lowAttacks);
		this.notEffectAttacks = new ArrayList<>(pokemon.notEffectAttacks);

		this.precisionPoints = 0;
		this.evasionPoints = 0;
		this.statusCondition = pokemon.statusCondition;
		this.ephemeralStates = new ArrayList<>(pokemon.ephemeralStates);

		this.isChargingAttackForNextRound = pokemon.isChargingAttackForNextRound;
		this.canAttack = pokemon.canAttack;
		this.alreadyUsedTwoTurnAttackBehavior = pokemon.alreadyUsedTwoTurnAttackBehavior;
		this.hasUsedMinimize = pokemon.hasUsedMinimize;
		this.hasRetreated = pokemon.hasRetreated;
	}

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

	public void setPs(float f) {
		this.ps = f;
	}

	public float getAttack() {
		return attack;
	}

	public void setAttack(float attack) {
		this.attack = attack;
	}

	public float getDef() {
		return def;
	}

	public void setDef(float def) {
		this.def = def;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpecialAttack() {
		return specialAttack;
	}

	public void setSpecialAttack(float specialAttack) {
		this.specialAttack = specialAttack;
	}

	public float getSpecialDefense() {
		return specialDefense;
	}

	public void setSpecialDefense(float specialDefense) {
		this.specialDefense = specialDefense;
	}

	public float getInitialPs() {
		return initialPs;
	}

	public void setInitialPs(float initialPs) {
		this.initialPs = initialPs;
	}

	public float getInitialAttack() {
		return initialAttack;
	}

	public void setInitialAttack(float initialAttack) {
		this.initialAttack = initialAttack;
	}

	public float getInitialDef() {
		return initialDef;
	}

	public void setInitialDef(float initialDef) {
		this.initialDef = initialDef;
	}

	public float getInitialSpeed() {
		return initialSpeed;
	}

	public void setInitialSpeed(float initialSpeed) {
		this.initialSpeed = initialSpeed;
	}

	public float getInitialSpecialAttack() {
		return initialSpecialAttack;
	}

	public void setInitialSpecialAttack(float initialSpecialAttack) {
		this.initialSpecialAttack = initialSpecialAttack;
	}

	public float getInitialSpecialDefense() {
		return initialSpecialDefense;
	}

	public void setInitialSpecialDefense(float initialSpecialDefense) {
		this.initialSpecialDefense = initialSpecialDefense;
	}

	public ArrayList<Ability> getNormalAbilities() {
		return normalAbilities;
	}

	public void setNormalAbilities(ArrayList<Ability> normalAbilities) {
		this.normalAbilities = normalAbilities;
	}

	public ArrayList<Ability> getHiddenAbilities() {
		return hiddenAbilities;
	}

	public void setHiddenAbilities(ArrayList<Ability> hiddenAbilities) {
		this.hiddenAbilities = hiddenAbilities;
	}

	public ArrayList<PokemonType> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PokemonType> types) {
		this.types = types;
	}

	public ArrayList<Attack> getPhysicalAttacks() {
		return physicalAttacks;
	}

	public void setPhysicalAttacks(ArrayList<Attack> physicalAttacks) {
		this.physicalAttacks = physicalAttacks;
	}

	public ArrayList<Attack> getSpecialAttacks() {
		return specialAttacks;
	}

	public void setSpecialAttacks(ArrayList<Attack> specialAttacks) {
		this.specialAttacks = specialAttacks;
	}

	public ArrayList<Attack> getOtherAttacks() {
		return otherAttacks;
	}

	public void setOtherAttacks(ArrayList<Attack> otherAttacks) {
		this.otherAttacks = otherAttacks;
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

	public ArrayList<Attack> getNotEffectAttacks() {
		return notEffectAttacks;
	}

	public void setNotEffectAttacks(ArrayList<Attack> notEffectAttacks) {
		this.notEffectAttacks = notEffectAttacks;
	}

	public ArrayList<Integer> getFourIdAttacks() {
		return fourIdAttacks;
	}

	public void setFourIdAttacks(ArrayList<Integer> fourIdAttacks) {
		this.fourIdAttacks = fourIdAttacks;
	}

	public int getPrecisionPoints() {
		return precisionPoints;
	}

	public void setPrecisionPoints(int precisionPoints) {
		this.precisionPoints = precisionPoints;
	}

	public int getEvasionPoints() {
		return evasionPoints;
	}

	public void setEvasionPoints(int evasionPoints) {
		this.evasionPoints = evasionPoints;
	}

	public State getStatusCondition() {
		return statusCondition;
	}

	public void setStatusCondition(State statusCondition) {
		this.statusCondition = statusCondition;
	}

	public ArrayList<State> getEphemeralStates() {
		return ephemeralStates;
	}

	public void setEphemeralStates(ArrayList<State> ephemeralStates) {
		this.ephemeralStates = ephemeralStates;
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

	public boolean getAlreadyUsedTwoTurnAttackBehavior() {
		return alreadyUsedTwoTurnAttackBehavior;
	}

	public void setAlreadyUsedTwoTurnAttackBehavior(boolean alreadyUsedTwoTurnAttackBehavior) {
		this.alreadyUsedTwoTurnAttackBehavior = alreadyUsedTwoTurnAttackBehavior;
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

	// Adds the four final attacks Ids to Pokemon
	public void addIdAttack(Integer idAtck) {
		this.fourIdAttacks.add(idAtck);
	}

	// Adds a ephemeral state to Pokemon
	public void addEstadoEfimero(State ephState) {
		this.ephemeralStates.add(ephState);
	}

	// Restart stats after some attacks... (cause not accumulated)
	public void restartParametersEffect() {

		switch (this.getStatusCondition().getStatusCondition()) {
		case TRAPPED:
			break;
		case PERISH_SONG:
			break;
		case CONFUSED:
			break;
		case FROZEN:
			break;
		case DEBILITATED:
			break;
		case ASLEEP:
			break;
		case SEEDED:
			break;
		case INFATUATED:
			break;
		case POISONED:
			break;
		case BADLY_POISONED:
			break;
		case CURSED:
			break;
		case PARALYZED:
			this.setSpeed(this.getInitialSpeed());
			break;
		case BURNED:
			this.setAttack(this.getInitialAttack());
			break;
		case NO_STATUS:
			break;
		default:
			break;

		}
	}

	// Modify conditions of Pokemon depending on States
	public void checkEffectsStatusCondition(boolean isStartTurn) {
		float reducePs = 0;

		if (!(this.getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS)) {

			switch (this.getStatusCondition().getStatusCondition()) {
			case CONFUSED:
				break;
			case PERISH_SONG:
				break;
			// Can move or not
			case FROZEN:
				if (isStartTurn) {
					// Can attack at the moment cause he is not frozen anymore
					if (this.getStatusCondition().getCanMoveStatusCondition()) {

						this.setCanAttack(true);

					} else {

						this.setCanAttack(false);
					}
				}
				break;
			case ASLEEP:
				break;
			case SEEDED:
				break;
			case INFATUATED:
				break;
			case POISONED:
				break;
			case BADLY_POISONED:
				break;
			case CURSED:
				break;
			// Modifies speed of Pokemon if can attack (reduces by 50%)
			case PARALYZED:
				if (isStartTurn) {
					if (this.getStatusCondition().getCanMoveEphemeralState()) {

						this.setSpeed((this.getSpeed() * 50) / 100);

						this.setCanAttack(true);

					} else {

						this.setCanAttack(false);
					}
				}
				break;
			// Reduces current PS by 6.25% and damage by 50%
			case BURNED:
				if (isStartTurn) {

					this.setAttack(this.getAttack() / 2);

					this.setCanAttack(true);

				} else {

					reducePs = this.getPs() * 0.0625f;

					this.setPs(this.getPs() - reducePs);

					// Reset parameters (when a Pokemon is burned, at the end of the round, he will
					// lose PS, so we will pass directly through the method)
					if (this.getAttack() != this.getInitialAttack()) {

						this.restartParametersEffect();
					}

					this.setCanAttack(true);

					System.out.println(
							this.getName() + " se resiente de la quemadura XD - PS actuales : " + this.getPs());
				}
				break;
			case NO_STATUS:
				break;
			case DEBILITATED:
				break;
			default:
				break;
			}
		}

		// Trapped
		if ((this.getEphemeralStates().stream().anyMatch(e -> e.getStatusCondition() == StatusConditions.TRAPPED))) {
			if (!isStartTurn) {
				// Reduces 12,5% from his actual PS remaining
				reducePs = this.getPs() * 0.125f;

				this.setPs(this.getPs() - reducePs);

				this.setCanAttack(true);

				System.out.println(this.getName() + " está atado - PS actuales : " + this.getPs());
			}
		}
	}
}
