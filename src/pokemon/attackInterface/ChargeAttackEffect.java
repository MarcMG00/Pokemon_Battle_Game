package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class ChargeAttackEffect implements AttackEffect {
	protected final DamageService damageService;

	public ChargeAttackEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		// If not charging => first turn charge the attack
		if (!ctx.attacker.getIsChargingAttackForNextRound()) {
			// This attack requires to charge first time for one round
			System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")"
					+ " se prepara para realizar " + ctx.attack.getName());

			ctx.attacker.setIsChargingAttackForNextRound(true);
			return result;
		}

		// Apply damage => second turn
		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.attacker.setIsChargingAttackForNextRound(false);
		ctx.attack.setPp(ctx.attack.getPp() - 1);

		ctx.defender.setPs(ctx.defender.getPs() - dmg);

		return result;
	}
}
