package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class RecoilDamageEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;
	private final float recoilPercent;

	public RecoilDamageEffect(AttackResolutionService attackResolutionService, float recoilPercent) {
		this.attackResolutionService = attackResolutionService;
		this.recoilPercent = recoilPercent;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.getAttack().getName());

		// 120_Reckless rises power by 20%
		if (ctx.getAttacker().hasRecklessAbility())
			ctx.setPower(ctx.getPower() * 1.2f);

		AttackResult result = attackResolutionService.resolveHit(ctx);

		ctx.consumePP();

		// 69_Rock_Head ability is not affected by recoil
		if (attacker.hasRockHeadAbility()) {
			System.out.println(attacker.getName() + " (Id:" + attacker.getId()
					+ ") no sufrió daño por retroceso gracias a su habilidad "
					+ attacker.getAbilitySelected().getName());
			return result;
		}

		float recoil = result.getDamage() * recoilPercent;
		attacker.setPs(Math.max(attacker.getPs() - recoil, 0));

		System.out.println(
				attacker.getName() + " (Id:" + attacker.getId() + ") sufrió daño por retroceso (" + recoil + ")");

		return result;
	}
}
