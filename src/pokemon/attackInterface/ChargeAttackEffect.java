package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;
import pokemon.model.Pokemon;

public class ChargeAttackEffect implements AttackEffect {
	protected final DamageService damageService;

	public ChargeAttackEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();

		AttackResult result = new AttackResult();

		// If not charging => first turn charge the attack
		if (!attacker.isChargingAttackForNextRound()) {
			// This attack requires to charge first time for one round
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " se prepara para realizar "
					+ ctx.getAttack().getName());

			attacker.setIsChargingAttackForNextRound(true);
			return result;
		}

		// Apply damage => second turn
		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.getAttack().getName());

		result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		// Ensure we don't keep charging state
		attacker.setIsChargingAttackForNextRound(false);
		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);

		ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

		return result;
	}
}
