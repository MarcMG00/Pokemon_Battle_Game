package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.State;

public class LeechSeedEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó Drenadoras");

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		// Doesn't affect to grass type
		if (ctx.defender.getTypes().stream().filter(t -> t.getId() == 12).findAny().isPresent()) {
			System.out.println(ctx.defender.getName() + " no puede estar drenado ya que es de tipo planta");
			return result;
		}

		ctx.attacker.setIsDraining(true);

		// Cannot be accumulated
		if (!ctx.defender.hasActiveEphemeralStatus(StatusConditions.DRAINEDALLTURNS)) {
			System.out.println(ctx.defender.getName() + " ya está drenado");
			return result;
		}

		System.out.println(ctx.defender.getName() + " (Id:" + ctx.defender.getId() + ")" + " fue drenado");

		State drainedAllTurns = new State(StatusConditions.DRAINEDALLTURNS, 0);
		ctx.defender.addEphemeralStatus(StatusConditions.DRAINEDALLTURNS, drainedAllTurns);

		return result;
	}

}
