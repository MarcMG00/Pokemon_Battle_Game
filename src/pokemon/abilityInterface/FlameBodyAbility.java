package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.State;

public class FlameBodyAbility implements AbilityEffect {
	private static final double BURNED_CHANCE = 0.30;

	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		if (!attackCtx.getDefender().hasFlameBodtyAbility())
			return true;

		if (attackCtx.getAttacker().hasActiveStatusCondition(StatusConditions.BURNED))
			return true;

		// Attack must make contact
		if (!attackCtx.getAttack().makesContact() || attackResult.getDamage() <= 0f)
			return true;

		// Probability
		if (Math.random() >= BURNED_CHANCE)
			return true;

		// Try to apply burned
		attackCtx.getStatusService().trySetStatus(attackCtx.getAttacker(), new State(StatusConditions.BURNED), null,
				false, attackCtx.getAttack());
		System.out.println(
				attackCtx.getAttacker().getName() + " fue quemado por la habilidad Cuerpo llama del Pokémon rival");

		return true;
	}
}
