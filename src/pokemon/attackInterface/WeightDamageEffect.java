package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;
import pokemon.model.StatService;

public class WeightDamageEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;
	private final StatService statService;

	public WeightDamageEffect(AttackResolutionService attackResolutionService, StatService statService) {
		this.attackResolutionService = attackResolutionService;
		this.statService = statService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon defender = ctx.getDefender();
		System.out
				.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó Patada baja");

		int defenderWeight = statService.getEffectiveWeight(defender);

		// Set power of the attack depending on the weight of the Pokemon facing
		if (defenderWeight < 10)
			ctx.setPower(20);
		else if (defenderWeight >= 10 && defenderWeight < 25)
			ctx.setPower(40);
		else if (defenderWeight >= 25 && defenderWeight < 50)
			ctx.setPower(60);
		else if (defenderWeight >= 50 && defenderWeight < 100)
			ctx.setPower(80);
		else if (defenderWeight >= 100 && defenderWeight < 200)
			ctx.setPower(100);
		else
			ctx.setPower(120);

		AttackResult result = attackResolutionService.resolveHit(ctx);

		ctx.consumePP();

		return result;
	}

}
