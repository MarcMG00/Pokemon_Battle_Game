package pokemon.model;

import pokemon.enums.Weather;

public class AttackContext {
	public final Pokemon attacker;
	public final Pokemon defender;
	public final Player attackingPlayer;
	public final Player defendingPlayer;
	public final Attack attack;
	public final Weather weather;
	public final boolean isWeatherSuppressed;
	public final boolean isMistEffectActivated;

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
	}
}
