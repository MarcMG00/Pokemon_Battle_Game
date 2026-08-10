package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public interface AbilityEffect {
	default void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
	}

	default void onSwitchOut(BattleContext battleCtx, Pokemon owner) {
	}

	default void beforeAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack) {
	}

	default boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack) {
		return true; // true = continues the attack
	}

	default boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		return true; // true = continues the attack (for example on multiple hits effect - Weak
						// armor)
	}

	default void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
	}

	default void beforeEndOfTurn(BattleContext battleCtx, Pokemon owner) {

	}

	default void endOfTurn(BattleContext battleCtx, Pokemon owner) {
	}

	default void duringBattle(BattleContext battleCtx, Pokemon owner, Pokemon defender) {
	}
}