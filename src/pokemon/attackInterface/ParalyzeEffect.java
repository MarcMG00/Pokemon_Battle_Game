package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class ParalyzeEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();
		
		Pokemon attacker = ctx.attacker;
		Pokemon defender = ctx.defender;

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.attack.getName());

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		if (defender.getStatusCondition().getStatusCondition() == StatusConditions.PARALYZED) {
			System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " ya está paralizado");
			return result;
		}

		defender.trySetStatus(new State(StatusConditions.PARALYZED), ctx.weather, ctx.isWeatherSuppressed, ctx.attack);
		
		return result;
	}

}
