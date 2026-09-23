package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class WeightDamageEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;

	public WeightDamageEffect(AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon defender = ctx.getDefender();
		System.out
				.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó Patada baja");

		// Set power of the attack depending on the weight of the Pokemon facing
		if (defender.getWeight() < 10)
			ctx.setPower(20);
		else if (defender.getWeight() >= 10 && defender.getWeight() < 25)
			ctx.setPower(40);
		else if (defender.getWeight() >= 25 && defender.getWeight() < 50)
			ctx.setPower(60);
		else if (defender.getWeight() >= 50 && defender.getWeight() < 100)
			ctx.setPower(80);
		else if (defender.getWeight() >= 100 && defender.getWeight() < 200)
			ctx.setPower(100);
		else
			ctx.setPower(120);

		AttackResult result = attackResolutionService.resolveHit(ctx);

		ctx.consumePP();

		return result;
	}

}
