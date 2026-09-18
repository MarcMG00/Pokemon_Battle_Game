package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;

public class SimpleDamageEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;

	public SimpleDamageEffect(AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		AttackResult result = attackResolutionService.resolveHit(ctx);

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		attackResolutionService.handleAfterMoveUsed(ctx);

		return result;
	}
}
