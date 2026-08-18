package pokemon.abilityInterface;

import java.util.ArrayList;

import pokemon.enums.Weather;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;
import pokemon.model.PokemonType;

public class ForecastAbility extends AbilityEffect {
	public ForecastAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		// Only change type if ability is Forecast
		if (!owner.hasForecastAbility())
			return;

		// Weather hasn't to be suppressed
		if (battleCtx.isWeatherSuppressed())
			return;

		Weather actualWeather = battleCtx.getWeather();
		ArrayList<PokemonType> types = battleCtx.getTypes();

		ArrayList<PokemonType> newType = new ArrayList<>();

		// Change Pokemon types depending on Weather
		switch (actualWeather) {
		case RAIN:
			newType.add(types.stream().filter(t -> t.getId() == 2).findFirst().get());
			owner.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Agua gracias a su habilidad Predicción");
			break;
		case SUN:
			newType.add(types.stream().filter(t -> t.getId() == 7).findFirst().get());
			owner.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Fuego gracias a su habilidad Predicción");
			break;
		case HAIL:
			newType.add(types.stream().filter(t -> t.getId() == 9).findFirst().get());
			owner.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Hielo gracias a su habilidad Predicción");
			break;
		default:
			break;
		}
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx) {
		// Reset types
		owner.setTypes(owner.getInitialTypes());
	}

	@Override
	public void duringBattle(BattleContext battleCtx, Pokemon attacker) {
		// Only change type if ability is Forecast
		if (!owner.hasTraceAbility() && !attacker.hasForecastAbility())
			return;

		// Weather hasn't to be suppressed
		if (battleCtx.isWeatherSuppressed())
			return;

		Weather actualWeather = battleCtx.getWeather();
		ArrayList<PokemonType> types = battleCtx.getTypes();

		ArrayList<PokemonType> newType = new ArrayList<>();

		// Change Pokemon types depending on Weather
		switch (actualWeather) {
		case RAIN:
			newType.add(types.stream().filter(t -> t.getId() == 2).findFirst().get());
			owner.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Agua gracias a su habilidad Predicción");
			break;
		case SUN:
			newType.add(types.stream().filter(t -> t.getId() == 7).findFirst().get());
			owner.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Fuego gracias a su habilidad Predicción");
			break;
		case HAIL:
			newType.add(types.stream().filter(t -> t.getId() == 9).findFirst().get());
			owner.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Hielo gracias a su habilidad Predicción");
			break;
		default:
			break;
		}
	}
}
