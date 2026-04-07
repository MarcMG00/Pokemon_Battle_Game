package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;

import pokemon.enums.Weather;

public class BattleContext {
	private final Player player;
	private final Player ia;

	private Weather weather = Weather.NONE;
	private boolean isWeatherSuppressed = false;

	private boolean isMistActive = false;
	private int nbTurnsMistActive = 0;

	private HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes;
	private ArrayList<PokemonType> types;

	public BattleContext(Player player, Player ia,
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes, ArrayList<PokemonType> types) {
		this.player = player;
		this.ia = ia;
		this.effectPerTypes = effectPerTypes;
		this.types = types;
	}

	public Player getPlayer() {
		return player;
	}

	public Player getIa() {
		return ia;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public boolean isWeatherSuppressed() {
		return isWeatherSuppressed;
	}

	public void setWeatherSuppressed(boolean weatherSuppressed) {
		isWeatherSuppressed = weatherSuppressed;
	}

	public boolean isMistActive() {
		return isMistActive;
	}

	public void setMistActive(boolean mistActive) {
		isMistActive = mistActive;
	}

	public int getNbTurnsMistActive() {
		return nbTurnsMistActive;
	}

	public void setNbTurnsMistActive(int nbTurnsMistActive) {
		this.nbTurnsMistActive = nbTurnsMistActive;
	}

	public HashMap<String, HashMap<String, ArrayList<PokemonType>>> getEffectPerTypes() {
		return effectPerTypes;
	}

	public ArrayList<PokemonType> getTypes() {
		return types;
	}
}
