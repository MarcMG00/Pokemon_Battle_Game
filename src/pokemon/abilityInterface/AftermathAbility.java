package pokemon.abilityInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class AftermathAbility extends AbilityEffect {
	public AftermathAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		// Defender needs to be debilitated by the attack
		if (!owner.hasFainted())
			return true;

		// Attack has dealt damage
		if (!attackResult.hasDealtDamage())
			return true;

		// defender needs to receive an attack that makes contact
		if (!attackCtx.getAttack().makesContact())
			return true;

		// attacker hasn't to have 006_Damp ability
		if (attackCtx.getAttacker().hasDampAbility())
			return true;

		// Remove 25% from his max PS
		float removePS = attackCtx.getAttacker().getInitialPs() * 0.25f;
		attackCtx.getAttacker().setPs(Math.max(attackCtx.getAttacker().getPs() - removePS, 0));

		System.out.println(
				attackCtx.getAttacker().getName() + " sufrió daño dada la habilidad Detonación de " + owner.getName());

		return false;
	}
}
