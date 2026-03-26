package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class FixedRecoilDamageEffect implements AttackEffect {
	private final DamageService damageService;

	public FixedRecoilDamageEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.defender.setPs(ctx.defender.getPs() - dmg);
		// Pokemon combating receives 25% of damage from his initial PS
		ctx.attacker.setPs(ctx.attacker.getInitialPs() - (ctx.attacker.getInitialPs() * 0.25f));

		return result;
	}

}
