package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class CuteCharmAbility extends AbilityEffect {
	public CuteCharmAbility(Pokemon owner) {
		super(owner);
	}
	
	private static final double INFATUEATED_CHANCE = 0.30;

	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		if (!attackCtx.getDefender().hasCuteCharmAbility())
			return true;

		if (attackCtx.getDefender().getSex() == attackCtx.getAttacker().getSex())
			return true;

		if (attackCtx.getAttacker().hasActiveEphemeralStatus(StatusConditions.INFATUATED))
			return true;

		// Attack must make contact
		if (!attackCtx.getAttack().makesContact() || attackResult.getDamage() <= 0f)
			return true;

		// Probability
		if (Math.random() >= INFATUEATED_CHANCE)
			return true;

		int nbTurnsHoldingStatus = 1 + (int) (Math.random() * (7 - 1 + 1));
		State infatuated = new State(StatusConditions.INFATUATED, nbTurnsHoldingStatus + 1);

		// Try to apply infatuated
		attackCtx.getStatusService().trySetEphemeralStatus(infatuated, attackCtx.getAttacker(),
				StatusConditions.INFATUATED, attackCtx.getAttack());

		System.out.println(
				attackCtx.getAttacker().getName() + " cayó enamorado por la habilidad Gran encanto del Pokémon rival");

		return true;
	}
}
