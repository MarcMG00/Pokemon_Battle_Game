package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public abstract class AbilityEffect {
	protected final Pokemon owner;

	protected AbilityEffect(Pokemon owner) {
		this.owner = owner;
	}

	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
	}

	public void onSwitchOut(BattleContext battleCtx) {
	}

	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Attack attack) {
		return true; // true = continues the attack
	}

	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		return true; // true = continues the attack (for example on multiple hits effect - Weak
						// armor)
	}

	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
	}

	public void beforeEndOfTurn(BattleContext battleCtx) {

	}

	public void endOfTurn(BattleContext battleCtx) {
	}

	public void duringBattle(BattleContext battleCtx, Pokemon attacker) {
	}
}