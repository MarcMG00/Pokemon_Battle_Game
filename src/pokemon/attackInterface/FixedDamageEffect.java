package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class FixedDamageEffect implements AttackEffect {
	private final float fixedDamage;

	public FixedDamageEffect(float fixedDamage) {
		this.fixedDamage = fixedDamage;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);
		ctx.getDefender().setPs(ctx.getDefender().getPs() - fixedDamage);

		result.addDamage(fixedDamage);
		return result;
	}
}
