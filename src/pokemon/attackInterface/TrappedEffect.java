package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;
import pokemon.model.HelperService;
import pokemon.model.State;

public class TrappedEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;
	private final HelperService helperService;

	public TrappedEffect(HelperService helperService, AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
		this.helperService = helperService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		AttackResult result = attackResolutionService.resolveHit(ctx);

		// Check if the Pokemon facing doesn't have the status Trapped
		if (!ctx.getDefender().hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {
			System.out.println(ctx.getDefender().getName() + " quedó atrapado");

			int nbTurnsHoldingStatus = helperService.randomInt(4, 5);
			State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);
			ctx.getDefender().addEphemeralStatus(StatusConditions.TRAPPED, trapped);
		}

		ctx.consumePP();

		return result;
	}
}
