package pokemon.interfce;

import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public interface AbilityEffect {
	default void onBattleStart(Game game, Pokemon owner) {
	}

	default void onSwitchIn(Game game, Pokemon owner) {
	}

	default void beforeAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack) {
	}

	default void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch) {
	}

	default void endOfTurn(Game game, Pokemon owner) {
	}
}