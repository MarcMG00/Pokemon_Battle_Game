package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;
import pokemon.model.HelperService;
import pokemon.model.State;

public class TrappedByOwnAttackEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;
	private final HelperService helperService;
	private final int minTurns;
	private final int maxTurns;

	public TrappedByOwnAttackEffect(HelperService helperService, AttackResolutionService attackResolutionService,
			int minTurns, int maxTurns) {
		this.attackResolutionService = attackResolutionService;
		this.helperService = helperService;
		this.minTurns = minTurns;
		this.maxTurns = maxTurns;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		AttackResult result = attackResolutionService.resolveHit(ctx);

		if (!ctx.getAttacker().hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)) {
			int turns = helperService.randomInt(minTurns, maxTurns);

			System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")"
					+ " usará el mismo ataque durante " + turns + " turnos.");

			State trappedByOwnAttack = new State(StatusConditions.TRAPPEDBYOWNATTACK, turns + 1);
			ctx.getDefender().addEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK, trappedByOwnAttack);

			// Only removes PP when choosing the attack
			ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);
		}

		return result;
	}
}
