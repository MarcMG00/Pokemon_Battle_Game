package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;

public class RecoilDamageIfFailsEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;

	public RecoilDamageIfFailsEffect(AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		// Informative : don't handle here recoil attack to attacker if fails (it is
		// handled on AccuracyService)

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		// 120_Reckless rises power by 20%
		if (ctx.getAttacker().hasRecklessAbility())
			ctx.setPower(ctx.getPower() * 1.2f);

		AttackResult result = attackResolutionService.resolveHit(ctx);

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		return result;
	}

}
