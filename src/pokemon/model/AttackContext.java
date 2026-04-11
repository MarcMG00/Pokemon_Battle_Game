package pokemon.model;

import pokemon.enums.Weather;

public class AttackContext {
	private final Pokemon attacker;
	private final Pokemon defender;
	private final Player attackingPlayer;
	private final Player defendingPlayer;
	private final Attack attack;
	private float power;
	private float precision;
	private Weather weather;
	private boolean isWeatherSuppressed;
	private boolean isMistActive;
	private TurnContext turnContext;
	private final StatusService statusService;
	private final StatService statService;

	public AttackContext(Pokemon attacker, Pokemon defender, Player attackingPlayer, Player defendingPlayer,
			Attack attack, Weather weather, boolean isWeatherSuppressed, boolean isMistActive,
			boolean isCriticalAttack) {

		this.attacker = attacker;
		this.defender = defender;
		this.attackingPlayer = attackingPlayer;
		this.defendingPlayer = defendingPlayer;
		this.attack = attack;
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
}
