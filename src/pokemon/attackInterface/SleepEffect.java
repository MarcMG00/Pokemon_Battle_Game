package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.HelperService;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class SleepEffect implements AttackEffect {
	private final int minTurns;
	private final int maxTurns;
	private final HelperService helperService;

	public SleepEffect(HelperService helperService, int minTurns, int maxTurns) {
		this.minTurns = minTurns;
		this.maxTurns = maxTurns;
		this.helperService = helperService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		Pokemon attacker = ctx.getAttacker();
		Pokemon defender = ctx.getDefender();

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.getAttack().getName());

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		if (defender.hasActiveEphemeralStatus(StatusConditions.ASLEEP)) {
			System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " ya está dormido");
			return result;
		}

		int turns = helperService.randomTurnsAbilitiesConditions(minTurns, maxTurns, ctx);
		State asleep = new State(StatusConditions.ASLEEP, turns + 1);

		ctx.getStatusService().trySetEphemeralStatus(asleep, defender, StatusConditions.ASLEEP, ctx.getAttack());

		return result;
	}
}
