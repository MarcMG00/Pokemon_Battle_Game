package pokemon.model;

import java.util.Scanner;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;

public class WeatherService {
	private final BattleContext battleCtx;
	private final StatusService statusService;
	private final SwitchPokemonService switchPokemonService;
	private final AbilityService abilityService;

	public WeatherService(BattleContext battleCtx) {
		this.battleCtx = battleCtx;
		this.statusService = new StatusService();
		this.switchPokemonService = new SwitchPokemonService(battleCtx);
		this.abilityService = new AbilityService();
	}

	// -----------------------------
	// Sets the weather ability on first combat (if any)
	// -----------------------------
	public void applyEntryWeatherAbilities() {
		Pokemon p1 = battleCtx.getPlayer().getPkCombatting();
		Pokemon p2 = battleCtx.getIa().getPkCombatting();

		applyWeatherAbility(p1, p2);
		applyWeatherAbility(p2, p1);

		applyWeatherSuppression(p1, p2);
		applyWeatherSuppression(p2, p1);
	}

	// -----------------------------
	// Use only weather abilities
	// -----------------------------
	private void applyWeatherAbility(Pokemon pk1, Pokemon pk2) {
		Ability a1 = pk1.getAbilitySelected();
		Ability a2 = pk2.getAbilitySelected();

		Ability weatherA1 = isWeatherAbility(a1) ? a1 : null;
		Ability weatherA2 = isWeatherAbility(a2) ? a2 : null;

		if (weatherA1 == null && weatherA2 == null)
			return;

		if (weatherA1 != null && weatherA2 == null) {
			weatherA1.getEffect().onSwitchIn(battleCtx, pk1, pk2);
			return;
		}

		if (weatherA2 != null && weatherA1 == null) {
			weatherA2.getEffect().onSwitchIn(battleCtx, pk2, pk1);
			return;
		}

		// Both have weather → slower wins
		Pokemon slower = pk1.getSpeed() <= pk2.getSpeed() ? pk1 : pk2;
		Pokemon faster = pk1.getSpeed() > pk2.getSpeed() ? pk1 : pk2;

		slower.getAbilitySelected().getEffect().onSwitchIn(battleCtx, slower, faster);
	}

	// -----------------------------
	// Suppress weather ability by 13_Cloud_Nine or 76_Air_Lock
	// -----------------------------
	private void applyWeatherSuppression(Pokemon attacker, Pokemon defender) {
		Ability ability = attacker.getAbilitySelected();

		if (ability == null)
			return;

		if (ability.getId() == 13 || ability.getId() == 76) {
			ability.getEffect().onSwitchIn(battleCtx, attacker, defender);
		}
	}

	// -----------------------------
	// Check if ability is wetaher type
	// -----------------------------
	private boolean isWeatherAbility(Ability ability) {
		return ability != null && ability.getIsWeatherType();
	}

	// -----------------------------
	// Apply modifying stats from weather
	// -----------------------------
	public void applyStatsFromWeather(TurnContext turnCtx) {
		Weather weather = battleCtx.getWeather();

		for (Pokemon pk : turnCtx.getPokemons()) {
			Ability ability = pk.getAbilitySelected();

			if (ability == null || ability.getId() == 5000)
				continue;

			// 33_Swift_Swim
			if (ability.getId() == 33 && weather == Weather.RAIN)
				turnCtx.multiplySpeed(pk, 2f);

			// 34_Chlorophyll
			if (ability.getId() == 34 && weather == Weather.SUN)
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
		default:
			break;
		}
	}

	// -----------------------------
	// Sandstorm effect
	// -----------------------------
	private void applySandstormEffect(Pokemon pokemon) {
		Ability ability = pokemon.getAbilitySelected();

		if (isImmuneToSandstormByType(pokemon))
			return;

		if (isImmuneToSandstormByAbility(ability)) {
			System.out.println(pokemon.getName() + " no se ve afectado por la tormenta de arena dada su habilidad "
					+ ability.getName());
			return;
		}

		applyDamageByPercentage(pokemon, 0.0625f, pokemon.getName() + " ha sido zarandeado por la tormenta de arena");
	}

	// -----------------------------
	// Sun effect
	// -----------------------------
	private void applySunEffect(Pokemon pokemon) {
		Ability ability = pokemon.getAbilitySelected();

		if (ability == null)
			return;

		// 87_Dry_Skin & 94_Solar_Power
		if (ability.getId() == 87 || ability.getId() == 94)
			applyDamageByPercentage(pokemon, 0.125f, pokemon.getName() + " (Id:" + pokemon.getId()
					+ "), recibie daño dada su habilidad " + ability.getName() + " (hace SOL)");
	}

	// -----------------------------
	// Rain effect
	// -----------------------------
	private void applyRainEffect(Pokemon pokemon) {
		Ability ability = pokemon.getAbilitySelected();

		if (ability == null)
			return;

		// 87_Dry_Skin
		if (ability.getId() == 87)
			applyHealByPercentage(pokemon, 0.125f, pokemon.getName() + " (Id:" + pokemon.getId()
					+ "), recupera PS dada su habilidad " + ability.getName() + " (está LLOVIENDO)");
	}

	// -----------------------------
	// Apply damage based on % of max HP
	// -----------------------------
	private void applyDamageByPercentage(Pokemon pokemon, float percentage, String message) {
		float amount = pokemon.getInitialPs() * percentage;
		pokemon.setPs(pokemon.getPs() - amount);

		System.out.println(message);
	}

	// -----------------------------
	// Apply heal based on % of max HP
	// -----------------------------
	private void applyHealByPercentage(Pokemon pokemon, float percentage, String message) {
		float amount = pokemon.getInitialPs() * percentage;
		pokemon.setPs(pokemon.getPs() + amount);

		System.out.println(message);
	}

	// -----------------------------
	// Check sandstorm immunity by type
	// -----------------------------
	private boolean isImmuneToSandstormByType(Pokemon pokemon) {
		return pokemon.getTypes().stream().anyMatch(t -> t.getId() == 1 || t.getId() == 14 || t.getId() == 16);
	}

	// -----------------------------
	// Check sandstorm immunity by ability
	// -----------------------------
	private boolean isImmuneToSandstormByAbility(Ability ability) {
		return ability != null && (ability.getId() == 8 || ability.getId() == 98 || ability.getId() == 159
				|| ability.getId() == 142 || ability.getId() == 146);
	}

	// -----------------------------
	// Apply weather effects at the end of the turn
	// -----------------------------
	public void applyWeatherEffects(Scanner sc) {
		applyStatsFromWeatherEndOfTurn(battleCtx.getPlayer().getPkCombatting());
		checkDebilitatedAfterEndTurn(battleCtx.getPlayer(), sc);

		applyStatsFromWeatherEndOfTurn(battleCtx.getIa().getPkCombatting());
		checkDebilitatedAfterEndTurn(battleCtx.getIa(), sc);

		reduceNbTurnsMistActive();
	}

	// -----------------------------
	// Check if a Pokemon fainted due to end-of-turn effects (weather, poison, burn)
	// -----------------------------
	private boolean checkDebilitatedAfterEndTurn(Player owner, Scanner sc) {
		Pokemon pk = owner.getPkCombatting();

		if (pk.getPs() >= 0)
			return false;

		// Mark as debilitated
		pk.setStatusCondition(new State(StatusConditions.DEBILITATED));

		System.out.println(pk.getName() + " fue debilitado.");

		// Force clean of drain effects because one of the Pokemon have died (so it
		// doesn't matter the order of Pokemon)
		statusService.clearDrainEffects(battleCtx.getPlayer().getPkCombatting(), battleCtx.getIa().getPkCombatting());

		// Force switch
		if (owner == battleCtx.getPlayer()) {
			System.out.println("¿Qué Pokémon deberías escoger?");
			boolean changed = false;
			while (!changed)
				changed = switchPokemonService.changePokemon(sc);
		} else {
			Pokemon newIA = owner.decideBestChangePokemon(battleCtx.getPlayer().getPkCombatting(),
					battleCtx.getEffectPerTypes());

			if (newIA == null)
				newIA = owner.getPokemon().stream().filter(p -> !p.isDebilitated()).findFirst().orElse(null);

			if (newIA != null) {
				switchPokemonService.resetPokemonBeforeSwitch(owner.getPkCombatting());

				statusService.removeStates(owner.getPkCombatting());

				System.out.println("IA envía a " + newIA.getName());

				newIA.setJustEnteredBattle(false);
				owner.setPkCombatting(newIA);

				abilityService.applyEntryAbilityOnSwitch(battleCtx, newIA, battleCtx.getPlayer().getPkCombatting());

				battleCtx.getPlayer().setPkFacing(newIA);
				owner.setPkFacing(battleCtx.getPlayer().getPkCombatting());

				switchPokemonService.refreshAttackOrders();
			}
		}
		return true;
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
