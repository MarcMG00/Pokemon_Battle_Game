package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;

public class ConditionalPowerEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;
	private final float multiplier;

	public ConditionalPowerEffect(AttackResolutionService attackResolutionService, float multiplier) {
		this.attackResolutionService = attackResolutionService;
		this.multiplier = multiplier;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		// Some attacks can get the double of power if charging an attack and are
		// invulnerable
		if (ctx.getDefender().isChargingAttackForNextRound()
				&& ctx.getAttack().canHitWhileInvulnerable().contains(ctx.getDefender().getNextMovement().getId()))
			ctx.setPower(ctx.getPower() * multiplier);

		AttackResult result = attackResolutionService.resolveHit(ctx);

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		return result;
	}
}
