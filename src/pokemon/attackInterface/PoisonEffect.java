package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class PoisonEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		Pokemon attacker = ctx.getAttacker();
		Pokemon defender = ctx.getDefender();

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.getAttack().getName());

		ctx.consumePP();

		if (defender.hasActiveStatusCondition(StatusConditions.POISONED)
				|| defender.hasActiveStatusCondition(StatusConditions.BADLY_POISONED)) {
			System.out.println(defender.getName() + " (Id:" + defender.getId() + ")"
					+ " ya está envenenado/gravemente envenenado");
			return result;
		}

		ctx.getStatusService().trySetStatusCondition(defender, new State(StatusConditions.POISONED), ctx.getWeather(),
				ctx.isWeatherSuppressed(), ctx.getAttack());

		return result;
	}

}
