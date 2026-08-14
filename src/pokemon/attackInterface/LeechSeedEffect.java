package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.State;

public class LeechSeedEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó Drenadoras");

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		// Doesn't affect to grass type
		if (ctx.getDefender().getTypes().stream().filter(t -> t.isGrassType()).findAny().isPresent()) {
			System.out.println(ctx.getDefender().getName() + " no puede estar drenado ya que es de tipo planta");
			return result;
		}

		ctx.getAttacker().setIsDraining(true);

		// Cannot be accumulated
		if (!ctx.getDefender().hasActiveEphemeralStatus(StatusConditions.DRAINEDALLTURNS)) {
			System.out.println(ctx.getDefender().getName() + " ya está drenado");
			return result;
		}

		System.out.println(ctx.getDefender().getName() + " (Id:" + ctx.getDefender().getId() + ")" + " fue drenado");

		State drainedAllTurns = new State(StatusConditions.DRAINEDALLTURNS, 0);
		ctx.getDefender().addEphemeralStatus(StatusConditions.DRAINEDALLTURNS, drainedAllTurns);

		return result;
	}

}
