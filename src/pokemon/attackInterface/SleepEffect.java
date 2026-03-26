package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class SleepEffect implements AttackEffect {
	private final int minTurns;
	private final int maxTurns;

	public SleepEffect(int minTurns, int maxTurns) {
		this.minTurns = minTurns;
		this.maxTurns = maxTurns;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		Pokemon attacker = ctx.attacker;
		Pokemon defender = ctx.defender;

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.attack.getName());

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		if (defender.hasActiveEphemeralStatus(StatusConditions.ASLEEP)) {
			System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " ya está dormido");
			return result;
		}

		if (!defender.trySetEphemeralStatus(StatusConditions.ASLEEP, ctx.attack))
			return result;

		int turns = ctx.helperService.randomTurnsAbilitiesConditions(minTurns, maxTurns, ctx);
		System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " cayó en un sueño profundo por "
				+ turns + " turnos");

		State asleep = new State(StatusConditions.ASLEEP, turns + 1);
		defender.addEphemeralStatus(StatusConditions.ASLEEP, asleep);

		return result;
	}
}
