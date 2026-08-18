package pokemon.model;

import java.util.Scanner;

import pokemon.enums.Weather;

public class WeatherService {
	private final BattleContext battleCtx;

	public WeatherService(BattleContext battleCtx) {
		this.battleCtx = battleCtx;
	}

	// -----------------------------
	// Suppress weather ability (ex : 13_Cloud_Nine or 76_Air_Lock)
	// -----------------------------
	public void applyWeatherSuppressionIfNeeded(BattleContext battleCtx) {
		Pokemon pkPlayer = battleCtx.getPkPlayer();
		Pokemon pkIA = battleCtx.getPkIA();

		boolean suppressed = pkPlayer.hasCloudNineAbility() || pkPlayer.hasAirLockAbility()
				|| pkIA.hasCloudNineAbility() || pkIA.hasAirLockAbility();

		battleCtx.setWeatherSuppressed(suppressed);
	}

	// -----------------------------
	// Apply modifying stats from weather
	// -----------------------------
	public void applyStatsFromWeather(TurnContext turnCtx) {
		Weather weather = battleCtx.getWeather();

		for (Pokemon pk : turnCtx.getPokemons()) {
			// 33_Swift_Swim
			if (pk.hasSwiftSwimAbility() && weather == Weather.RAIN)
				turnCtx.multiplySpeed(pk, 2f);

			// 34_Chlorophyll
			if (pk.hasChlorophyllAbility() && weather == Weather.SUN)
				turnCtx.multiplySpeed(pk, 2f);
		}
	}

	// -----------------------------
	// Apply modifying stats from weather (end of turn)
	// -----------------------------
	private void applyStatsFromWeatherEndOfTurn(Pokemon pokemon) {
		Weather weather = battleCtx.getWeather();

		switch (weather) {
		case SANDSTORM:
			applySandstormEffect(pokemon);
			break;
		case SUN:
			applySunEffect(pokemon);
			break;
		case RAIN:
			applyRainEffect(pokemon);
			break;
		case HAIL:
			applyHailEffect(pokemon);
			break;
		default:
			break;
		}
	}

	// -----------------------------
	// Sandstorm effect
	// -----------------------------
	private void applySandstormEffect(Pokemon pokemon) {
		// BOOST
		if (isImmuneToSandstormByType(pokemon))
			return;

		if (isImmuneToSandstormByAbility(pokemon)) {
			System.out.println(pokemon.getName() + " no se ve afectado por la tormenta de arena dada su habilidad "
					+ pokemon.getAbilitySelected().getName());
			return;
		}

		// DAMAGE
		applyDamageByPercentage(pokemon, 0.0625f, pokemon.getName() + " ha sido zarandeado por la tormenta de arena");
	}

	// -----------------------------
	// Sun effect
	// -----------------------------
	private void applySunEffect(Pokemon pokemon) {
		// DAMAGE
		if (pokemon.hasDrySkinAbility() || pokemon.hasSolarPowerAbility())
			applyDamageByPercentage(pokemon, 0.125f, pokemon.getName() + " (Id:" + pokemon.getId()
					+ "), recibie daño dada su habilidad " + pokemon.getAbilitySelected().getName() + " (hace SOL)");
	}

	// -----------------------------
	// Rain effect
	// -----------------------------
	private void applyRainEffect(Pokemon pokemon) {
		// BOOST
		if (pokemon.hasDrySkinAbility())
			applyHealByPercentage(pokemon, 0.125f,
					pokemon.getName() + " (Id:" + pokemon.getId() + "), recupera PS dada su habilidad "
							+ pokemon.getAbilitySelected().getName() + " (está LLOVIENDO)");

		if (pokemon.hasRainDishAbility())
			applyHealByPercentage(pokemon, 0.0625f,
					pokemon.getName() + " (Id:" + pokemon.getId() + "), recupera PS dada su habilidad "
							+ pokemon.getAbilitySelected().getName() + " (está LLOVIENDO)");
	}

	// -----------------------------
	// Hail effect
	// -----------------------------
	private void applyHailEffect(Pokemon pokemon) {
		// BOOST
		if (isImmuneToHailByType(pokemon))
			return;

		if (isImmuneToHailByAbility(pokemon)) {
			System.out.println(pokemon.getName() + " (Id:" + pokemon.getId()
					+ "), no sufrió daño de Granizo dada su habilidad " + pokemon.getAbilitySelected().getName());
			return;
		}

		// DAMAGE
		applyDamageByPercentage(pokemon, 0.0625f, pokemon.getName() + " ha sido zarandeado por el granizo");
	}

	// -----------------------------
	// Apply damage based on % of max HP
	// -----------------------------
	private void applyDamageByPercentage(Pokemon pokemon, float percentage, String message) {
		float amount = pokemon.getInitialPs() * percentage;
		pokemon.setPs(Math.max(pokemon.getPs() - amount, 0));

		System.out.println(message);
	}

	// -----------------------------
	// Apply heal based on % of max HP
	// -----------------------------
	private void applyHealByPercentage(Pokemon pokemon, float percentage, String message) {
		float amount = pokemon.getInitialPs() * percentage;
		pokemon.setPs(Math.min(pokemon.getPs() + amount, pokemon.getInitialPs()));

		System.out.println(message);
	}

	// -----------------------------
	// Check sandstorm immunity by type
	// -----------------------------
	private boolean isImmuneToSandstormByType(Pokemon pokemon) {
		return pokemon.getTypes().stream().anyMatch(t -> t.isSteelType() || t.isRockType() || t.isGroundType());
	}

	// -----------------------------
	// Check sandstorm immunity by ability
	// -----------------------------
	private boolean isImmuneToSandstormByAbility(Pokemon pk) {
		return pk.hasSandVeilAbility() || pk.hasMagicGuardAbility() || pk.hasSandForceAbility()
				|| pk.hasOvercoatAbility() || pk.hasSandRashAbility();
	}

	// -----------------------------
	// Check hail immunity by type
	// -----------------------------
	private boolean isImmuneToHailByType(Pokemon pokemon) {
		return pokemon.getTypes().stream().anyMatch(t -> t.isIceType());
	}

	// -----------------------------
	// Check hail immunity by ability
	// -----------------------------
	private boolean isImmuneToHailByAbility(Pokemon pk) {
		return pk.hasSnowCloakAbility() || pk.hasMagicGuardAbility();
	}

	// -----------------------------
	// Apply weather effects at the end of the turn (both players)
	// -----------------------------
	public void applyWeatherEffects(Scanner sc) {
		if (!battleCtx.getPkPlayer().isFainted())
			applyStatsFromWeatherEndOfTurn(battleCtx.getPkPlayer());

		if (!battleCtx.getPkIA().isFainted())
			applyStatsFromWeatherEndOfTurn(battleCtx.getPkIA());

		reduceNbTurnsMistActive();
	}

	// -----------------------------
	// Reduce number of turns of Mist effect
	// -----------------------------
	private void reduceNbTurnsMistActive() {
		if (battleCtx.isMistActive()) {
			battleCtx.setNbTurnsMistActive(battleCtx.getNbTurnsMistActive() - 1);

			if (battleCtx.getNbTurnsMistActive() <= 0) {
				battleCtx.setMistActive(false);
				System.out.println("La neblina se disipó!");
			} else
				System.out.println(
						"Faltan " + battleCtx.getNbTurnsMistActive() + " turnos para que la neblina se fuerara XD");
		}
	}
}
