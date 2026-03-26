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
		
		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		ctx.attack.setPp(ctx.attack.getPp() - 1);
		ctx.defender.setPs(ctx.defender.getPs() - fixedDamage);
		
		result.addDamage(fixedDamage);
		return result;
	}
}
