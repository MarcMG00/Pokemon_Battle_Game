package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.HelperService;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class ConfusedEffect implements AttackEffect {
	private final int minTurns;
	private final int maxTurns;
	private final HelperService helperService;

	public ConfusedEffect(HelperService helperService, int minTurns, int maxTurns) {
		this.minTurns = minTurns;
		this.maxTurns = maxTurns;
		this.helperService = helperService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		Pokemon attacker = ctx.attacker;
		Pokemon defender = ctx.defender;

		System.out.println(ctx.attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.attack.getName());

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		if (defender.hasActiveEphemeralStatus(StatusConditions.CONFUSED)) {
			System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " ya está confundido");
			return result;
		}

		if (!ctx.defender.trySetEphemeralStatus(StatusConditions.CONFUSED, ctx.attack))
			return result;

		int turns = helperService.randomInt(minTurns, maxTurns);
		System.out.println(defender.getName() + " está confuso por " + turns + " turnos");

		State confused = new State(StatusConditions.CONFUSED, turns + 1);
		ctx.defender.addEphemeralStatus(StatusConditions.CONFUSED, confused);

		return result;
	}

}
