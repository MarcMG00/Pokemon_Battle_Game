package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class RecoilDamageEffect implements AttackEffect {
	private final DamageService damageService;
	private final float recoilPercent;

	public RecoilDamageEffect(DamageService damageService, float recoilPercent) {
		this.damageService = damageService;
		this.recoilPercent = recoilPercent;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		float dmg = damageService.doDammage(ctx);

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		ctx.defender.setPs(ctx.defender.getPs() - dmg);

		// 69_Rock_Head ability is not affected by recoil
		if (ctx.attacker.getAbilitySelected().getId() == 69) {
			System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId()
					+ ") no sufrió daño por retroceso gracias a su habilidad "
					+ ctx.attacker.getAbilitySelected().getName());
			return result;
		}

		float recoil = dmg * recoilPercent;

		ctx.attacker.setPs(ctx.attacker.getPs() - recoil);

		System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ") sufrió daño por retroceso ("
				+ recoil + ")");

		result.addDamage(dmg);
		return result;
	}
}
