package pokemon.model;

import pokemon.enums.Weather;

public class AttackContext {
	public final Pokemon attacker;
	public final Pokemon defender;
	public final Player attackingPlayer;
	public final Player defendingPlayer;
	public final Attack attack;
	public float power;
	public float precision;
	public final Weather weather;
	public final boolean isWeatherSuppressed;
	public final boolean isMistEffectActivated;
	public TurnContext turnContext;

	public AttackContext(Pokemon attacker, Pokemon defender, Player attackingPlayer, Player defendingPlayer,
			Attack attack, Weather weather, boolean isWeatherSuppressed, boolean isMistEffectActivated,
			boolean isCriticalAttack) {

		this.attacker = attacker;
		this.defender = defender;
		this.attackingPlayer = attackingPlayer;
		this.defendingPlayer = defendingPlayer;
		this.attack = attack;
		this.weather = weather;
		this.isWeatherSuppressed = isWeatherSuppressed;
		this.isMistEffectActivated = isMistEffectActivated;
		this.power = attack.getPower();
		this.precision = attack.getPrecision();
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
}
