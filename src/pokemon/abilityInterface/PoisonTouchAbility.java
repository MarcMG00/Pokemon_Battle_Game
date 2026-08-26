package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class PoisonTouchAbility extends AbilityEffect {
	public PoisonTouchAbility(Pokemon owner) {
		super(owner);
	}

	private static final double POISONED_CHANCE = 0.30;

	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		if (attackCtx.getAttacker().hasActiveStatusCondition(StatusConditions.POISONED)
				|| attackCtx.getAttacker().hasActiveStatusCondition(StatusConditions.BADLY_POISONED))
			return true;

		// Attack must make contact
		if (!attackCtx.getAttack().makesContact() || attackResult.getDamage() <= 0f)
			return true;

		// Probability
		if (Math.random() >= POISONED_CHANCE)
			return true;

		// Try to apply poison
		attackCtx.getStatusService().trySetStatusCondition(attackCtx.getAttacker(), new State(StatusConditions.POISONED), null,
				false, attackCtx.getAttack());
		System.out.println(
				attackCtx.getAttacker().getName() + " fue envenenado por la habilidad Punto Tóxico del Pokémon rival");

		return true;
	}
}
