package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class WeightDamageEffect implements AttackEffect {
	private final DamageService damageService;

	public WeightDamageEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó Patada baja");

		// Set power of the attack depending on the weight of the Pokemon facing
		if (ctx.defender.getWeight() < 10)
			ctx.attack.setPower(20);
		else if (ctx.defender.getWeight() >= 10 && ctx.defender.getWeight() < 25)
			ctx.attack.setPower(40);
		else if (ctx.defender.getWeight() >= 25 && ctx.defender.getWeight() < 50)
			ctx.attack.setPower(60);
		else if (ctx.defender.getWeight() >= 50 && ctx.defender.getWeight() < 100)
			ctx.attack.setPower(80);
		else if (ctx.defender.getWeight() >= 100 && ctx.defender.getWeight() < 200)
			ctx.attack.setPower(100);
		else
			ctx.attack.setPower(120);

		float dmg = damageService.doDammage(ctx);

		ctx.attack.setPp(ctx.attack.getPp() - 1);
		ctx.defender.setPs(ctx.defender.getPs() - dmg);

		result.addDamage(dmg);
		return result;
	}

}
