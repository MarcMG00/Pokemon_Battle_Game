package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class FixedRecoilDamageEffect implements AttackEffect {
	private final DamageService damageService;

	public FixedRecoilDamageEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.getDefender().setPs(Math.max(ctx.getDefender().getPs() - dmg, 0));
		// Pokemon combating receives 25% of damage from his initial PS
		ctx.getAttacker()
				.setPs(Math.max(ctx.getAttacker().getInitialPs() - (ctx.getAttacker().getInitialPs() * 0.25f), 0));

		ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

		return result;
	}

}
