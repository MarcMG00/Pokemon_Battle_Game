package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class AbsorbEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;
	private static final float DRAIN_PERCENTAGE = 0.5f;

	public AbsorbEffect(AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.getAttack().getName());

		AttackResult result = attackResolutionService.resolveHit(ctx);

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		applyDrain(ctx, result);

		return result;
	}

	// -----------------------------
	// Pokemon combating gets or loses health
	// -----------------------------
	private void applyDrain(AttackContext ctx, AttackResult result) {
		Pokemon attacker = ctx.getAttacker();
		Pokemon defender = ctx.getDefender();

		float drainAmount = result.getDamage() * DRAIN_PERCENTAGE;

		if (defender.hasLiquidOozeAbility()) {
			attacker.setPs(Math.max(attacker.getPs() - drainAmount, 0));

			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
					+ " perdió PS al intentar drenar al rival dada la habilidad Viscosecreción");

			return;
		}

		attacker.setPs(Math.min(attacker.getPs() + drainAmount, attacker.getInitialPs()));
	}
}
