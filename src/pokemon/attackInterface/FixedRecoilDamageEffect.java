package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;

public class FixedRecoilDamageEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;

	public FixedRecoilDamageEffect(AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		AttackResult result = attackResolutionService.resolveHit(ctx);

		// Pokemon combating receives 25% of damage from his initial PS
		ctx.getAttacker()
				.setPs(Math.max(ctx.getAttacker().getInitialPs() - (ctx.getAttacker().getInitialPs() * 0.25f), 0));

		return result;
	}

}
