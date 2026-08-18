package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class RecoilDamageIfFailsEffect implements AttackEffect {

	private final DamageService damageService;

	public RecoilDamageIfFailsEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		// 120_Reckless rises power by 20%
		if (ctx.getAttacker().hasRecklessAbility())
			ctx.setPower(ctx.getPower() * 1.2f);

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);
		ctx.getDefender().setPs(Math.max(ctx.getDefender().getPs() - dmg, 0));

		ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

		return result;
	}

}
