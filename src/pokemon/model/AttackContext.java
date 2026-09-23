package pokemon.model;

import pokemon.enums.Weather;

public class AttackContext {
	private final Pokemon attacker;
	private final Pokemon defender;
	private final Player attackingPlayer;
	private final Player defendingPlayer;
	private Attack attack; // attack used
	private Attack ppSource; // attack that really consumes the PP (102_Mimic...)
	private float power;
	private float precision;
	private Weather weather;
	private boolean isWeatherSuppressed;
	private boolean isMistActive;
	private TurnContext turnContext;
	private final StatusService statusService;
	private final StatService statService;

	public AttackContext(Player attackingPlayer, Player defendingPlayer, Weather weather, boolean isWeatherSuppressed,
			boolean isMistActive) {
		this.attacker = attackingPlayer.getPkCombatting();
		this.defender = defendingPlayer.getPkCombatting();
		this.attackingPlayer = attackingPlayer;
		this.defendingPlayer = defendingPlayer;
		setEffectiveAttack(attackingPlayer.getPkCombatting());
		this.weather = weather;
		this.isWeatherSuppressed = isWeatherSuppressed;
		this.isMistActive = isMistActive;
		this.power = attack.getPower();
		this.precision = attack.getPrecision();
		this.statusService = new StatusService();
		this.statService = new StatService();
	}

	public float getPower() {
		return power;
	}

	public void setPower(float power) {
		this.power = power;
	}

	public float getPrecision() {
		return precision;
	}

	public void setPrecision(float precision) {
		this.precision = precision;
	}

	public TurnContext getTurnContext() {
		return turnContext;
	}

	public void setTurnContext(TurnContext turnContext) {
		this.turnContext = turnContext;
	}

	public Pokemon getAttacker() {
		return attacker;
	}

	public Pokemon getDefender() {
		return defender;
	}

	public Player getAttackingPlayer() {
		return attackingPlayer;
	}

	public Player getDefendingPlayer() {
		return defendingPlayer;
	}

	public Attack getAttack() {
		return attack;
	}

	public void setAttack(Attack attack) {
		this.attack = attack;
	}

	public void setEffectiveAttack(Pokemon attacker) {
		Attack selectedAttack = attacker.getNextMovement();

		Attack effectiveAttack = attacker.isMimicking() ? attacker.getCopiedAttack() : selectedAttack;

		this.setAttack(effectiveAttack);
		this.setPpSource(selectedAttack);
	}

	public Attack getPpSource() {
		return ppSource;
	}

	public void setPpSource(Attack ppSource) {
		this.ppSource = ppSource;
	}

	public Weather getWeather() {
		return weather;
	}

	public boolean isWeatherSuppressed() {
		return isWeatherSuppressed;
	}

	public boolean isMistActive() {
		return isMistActive;
	}

	public StatusService getStatusService() {
		return statusService;
	}

	public StatService getStatService() {
		return statService;
	}

	public void multiplyPrecision(float value) {
		this.precision *= value;
	}

	public void consumePP() {
		if (ppSource != null && ppSource.getPp() > 0)
			ppSource.setPp(ppSource.getPp() - 1);
	}
}