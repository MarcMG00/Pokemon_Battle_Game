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
		AttackResult result = new AttackResult();

		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		// If Pokemon facing has used minimize, set power base of the attack x2
		if (ctx.defender.getHasUsedMinimize())
			ctx.attack.setPower(ctx.attack.getPower() * 2);

		float dmg = damageService.doDammage(ctx);

		ctx.attack.setPp(ctx.attack.getPp() - 1);
		ctx.defender.setPs(ctx.defender.getPs() - dmg);

		result.addDamage(dmg);
		return result;
	}
}
