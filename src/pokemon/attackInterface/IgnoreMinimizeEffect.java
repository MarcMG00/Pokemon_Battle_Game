package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class IgnoreMinimizeEffect implements AttackEffect {
	private final DamageService damageService;

	public IgnoreMinimizeEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		// If Pokemon facing has used minimize, set power base of the attack x2
		if (ctx.getDefender().getHasUsedMinimize())
			ctx.setPower(ctx.getAttack().getPower() * 2);

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);
		ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);

		ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

		return result;
	}
}
