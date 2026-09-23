package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;

public class AttackRestOneTurnEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;

	public AttackRestOneTurnEffect(AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		AttackResult result = attackResolutionService.resolveHit(ctx);

		ctx.consumePP();

		// Pokemon combating cannot do anything next round
		ctx.getAttacker().setCanDonAnythingNextRound(false);

		return result;
	}
}
