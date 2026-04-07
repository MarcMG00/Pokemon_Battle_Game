package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class SimpleDamageEffect implements AttackEffect {
	private final DamageService damageService;

	public SimpleDamageEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);
		ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);

		return result;
	}
}
