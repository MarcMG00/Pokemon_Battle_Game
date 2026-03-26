package pokemon.model;

import pokemon.enums.Weather;

public class AttackContext {
	public Pokemon attacker;
	public Pokemon defender;
	public Player attackingPlayer;
	public Player defendingPlayer;

	public Attack attack;

	public Weather weather;
	public boolean isWeatherSuppressed;
	public boolean isMistEffectActivated;

	public boolean isCriticalAttack;

	public HelperService helperService;

	public AttackContext(Pokemon attacker, Pokemon defender, Player attackingPlayer, Player defendingPlayer,
			Attack attack, Weather weather, boolean isWeatherSuppressed, boolean isMistEffectActivated,
			boolean isCriticalAttack, HelperService helperService) {

		this.attacker = attacker;
		this.defender = defender;
		this.attackingPlayer = attackingPlayer;
		this.defendingPlayer = defendingPlayer;
		this.attack = attack;
		this.weather = weather;
		this.isWeatherSuppressed = isWeatherSuppressed;
		this.isMistEffectActivated = isMistEffectActivated;
		this.isCriticalAttack = isCriticalAttack;
		this.helperService = helperService;
	}
}
