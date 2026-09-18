package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;

public class IgnoreMinimizeEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;

	public IgnoreMinimizeEffect(AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		// If Pokemon facing has used minimize, set power base of the attack x2
		if (ctx.getDefender().hasUsedMinimize())
			ctx.setPower(ctx.getAttack().getPower() * 2);

		AttackResult result = attackResolutionService.resolveHit(ctx);

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		return result;
	}
}
