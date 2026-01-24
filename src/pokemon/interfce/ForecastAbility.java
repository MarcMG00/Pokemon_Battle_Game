package pokemon.interfce;

import java.util.ArrayList;

import pokemon.enums.Weather;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.PokemonType;

public class ForecastAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(Game game, Pokemon owner, Pokemon defender) {
		// Only change type of defender if ability is Forecast
		if (owner.getAbilitySelected().getId() != 59)
			return;

		// Weather hasn't to be suppressed
		if (game.getisWeatherSuppressed())
			return;

		Weather actualWeather = game.getCurrentWeather();
		ArrayList<PokemonType> types = game.getTypes();

		ArrayList<PokemonType> newType = new ArrayList<>();

		// Change Pokemon types depending on Weather
		switch (actualWeather) {
		case RAIN:
			newType.add(types.stream().filter(t -> t.getId() == 2).findFirst().get());
			defender.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Agua gracias a su habilidad Predicción");
			break;
		case SUN:
			newType.add(types.stream().filter(t -> t.getId() == 7).findFirst().get());
			defender.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Fuego gracias a su habilidad Predicción");
			break;
		case HAIL:
			newType.add(types.stream().filter(t -> t.getId() == 9).findFirst().get());
			defender.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Hielo gracias a su habilidad Predicción");
			break;
		default:
			break;
		}
	}

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {
		// Reset types
		owner.setTypes(owner.getInitialTypes());
	}

	@Override
	public void duringBattle(Game game, Pokemon owner, Pokemon defender) {

		// Only change type of defender if ability is Forecast
		if (owner.getAbilitySelected().getId() != 59)
			return;

		// Weather hasn't to be suppressed
		if (game.getisWeatherSuppressed())
			return;

		Weather actualWeather = game.getCurrentWeather();
		ArrayList<PokemonType> types = game.getTypes();

		ArrayList<PokemonType> newType = new ArrayList<>();

		// Change Pokemon types depending on Weather
		switch (actualWeather) {
		case RAIN:
			newType.add(types.stream().filter(t -> t.getId() == 2).findFirst().get());
			defender.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Agua gracias a su habilidad Predicción");
			break;
		case SUN:
			newType.add(types.stream().filter(t -> t.getId() == 7).findFirst().get());
			defender.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Fuego gracias a su habilidad Predicción");
			break;
		case HAIL:
			newType.add(types.stream().filter(t -> t.getId() == 9).findFirst().get());
			defender.setTypes(newType);
			System.out.println(owner.getName() + " cambió a tipo Hielo gracias a su habilidad Predicción");
			break;
		default:
			break;
		}
	}
}
