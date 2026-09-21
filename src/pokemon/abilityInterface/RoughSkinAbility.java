package pokemon.abilityInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class RoughSkinAbility extends AbilityEffect {
	public RoughSkinAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		// 98_Magic_Guard annuls secondary damage effects
		if (attackCtx.getAttacker().hasMagicGuardAbility())
			return true;

		// Attack must make contact
		if (!attackCtx.getAttack().makesContact())
			return true;

		// Attack must make damage
		if (!attackResult.hasDealtDamage())
			return true;

		// Return damage to attacker
		float attackerInitialPs = attackCtx.getAttacker().getInitialPs();

		// Removes 6,25% of initial PS
		float damage = attackerInitialPs * (1f - 0.625f);
		attackCtx.getAttacker().setPs(Math.max(attackCtx.getAttacker().getPs() - damage, 0));

		System.out.println(
				attackCtx.getAttacker().getName() + " fue dañado por la habilidad Piel tosca del Pokémon rival");

		return true;
	}
}
