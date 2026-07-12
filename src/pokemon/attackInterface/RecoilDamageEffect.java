package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;
import pokemon.model.Pokemon;

public class RecoilDamageEffect implements AttackEffect {
	private final DamageService damageService;
	private final float recoilPercent;

	public RecoilDamageEffect(DamageService damageService, float recoilPercent) {
		this.damageService = damageService;
		this.recoilPercent = recoilPercent;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.getAttack().getName());

		// 120_Reckless rises power by 20%
		if (ctx.getAttacker().getAbilitySelected().getId() == 120)
			ctx.setPower(ctx.getPower() * 1.2f);

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);

		// 69_Rock_Head ability is not affected by recoil
		if (attacker.getAbilitySelected().getId() == 69) {
			System.out.println(attacker.getName() + " (Id:" + attacker.getId()
					+ ") no sufrió daño por retroceso gracias a su habilidad "
					+ attacker.getAbilitySelected().getName());
			return result;
		}

		float recoil = dmg * recoilPercent;

		attacker.setPs(attacker.getPs() - recoil);

		System.out.println(
				attacker.getName() + " (Id:" + attacker.getId() + ") sufrió daño por retroceso (" + recoil + ")");

		return result;
	}
}
