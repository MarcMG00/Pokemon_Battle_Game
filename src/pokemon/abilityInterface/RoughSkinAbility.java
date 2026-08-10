package pokemon.abilityInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class RoughSkinAbility implements AbilityEffect {
	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		if (attackCtx.getDefender().hasRoughSkinAbility())
			return true;

		// 98_Magic_Guard annuls secondary damage effects
		if (attackCtx.getAttacker().hasMagicGuardAbility())
			return true;

		// Attack must make contact
		if (!attackCtx.getAttack().makesContact() || attackResult.getDamage() <= 0f)
			return true;

		// Return damage to attacker
		float attackerInitialPs = attackCtx.getAttacker().getInitialPs();
		// Removes 6,25% of initial PS
		float damage = attackerInitialPs * (1f - 0.625f);
		attackCtx.getAttacker().setPs(attackCtx.getAttacker().getPs() - damage);

		System.out.println(
				attackCtx.getAttacker().getName() + " fue dañado por la habilidad Piel tosca del Pokémon rival");

		return true;
	}
}
