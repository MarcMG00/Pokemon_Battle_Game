package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.State;

public class StaticAbility implements AbilityEffect {
	private static final double PARALYSIS_CHANCE = 0.30;

	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		if (attackCtx.getDefender().hasStaticAbility())
			return true;

		if (attackCtx.getAttacker().hasActiveStatusCondition(StatusConditions.PARALYZED))
			return true;

		// Attack must make contact
		if (!attackCtx.getAttack().makesContact() || attackResult.getDamage() <= 0f)
			return true;

		// Probability
		if (Math.random() >= PARALYSIS_CHANCE)
			return true;

		// Try to apply paralysis
		attackCtx.getStatusService().trySetStatus(attackCtx.getAttacker(), new State(StatusConditions.PARALYZED), null,
				false, attackCtx.getAttack());
		System.out.println(attackCtx.getAttacker().getName()
				+ " fue paralizado por la habilidad electricidad estática del Pokémon rival");

		return true;
	}
}
