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
		AttackResult result = new AttackResult();

		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		// Some attacks can get the double of power if charging an attack and are
		// invulnerable
		if (ctx.defender.getIsChargingAttackForNextRound()
				&& ctx.attack.getCanHitWhileInvulnerable().contains(ctx.defender.getNextMovement().getId()))
			ctx.attack.setPower(ctx.attack.getPower() * multiplier);

		float dmg = damageService.doDammage(ctx);

		ctx.attack.setPp(ctx.attack.getPp() - 1);
		ctx.defender.setPs(ctx.defender.getPs() - dmg);

		result.addDamage(dmg);
		return result;
	}
}
