package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class ConditionalPowerEffect implements AttackEffect {
	private final DamageService damageService;
	private final float multiplier;

	public ConditionalPowerEffect(DamageService damageService, float multiplier) {
		this.damageService = damageService;
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

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);
		ctx.getDefender().setPs(Math.max(ctx.getDefender().getPs() - dmg, 0));

		ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

		return result;
	}
}
