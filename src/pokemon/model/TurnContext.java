package pokemon.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TurnContext {
	public Map<Pokemon, TurnPokemonStats> stats = new HashMap<>();

	public TurnPokemonStats getStats(Pokemon pk) {
		return stats.computeIfAbsent(pk, k -> new TurnPokemonStats());
	}

	public float getSpeed(Pokemon pk) {
		return getStats(pk).speed;
	}

	public void setSpeed(Pokemon pk, float speed) {
		getStats(pk).speed = speed;
	}

	public void multiplySpeed(Pokemon pk, float multiplier) {
		setSpeed(pk, getSpeed(pk) * multiplier);
	}

	public Set<Pokemon> getPokemons() {
		return stats.keySet();
	}
}
