package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class AbsorbEffect implements AttackEffect {
	private final DamageService damageService;

	public AbsorbEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		ctx.defender.setPs(ctx.defender.getPs() - dmg);

		// Pokemon combating gets or loses health
		if (ctx.defender.getAbilitySelected().getId() == 64) {
			// The half of damage done
			ctx.attacker.setPs(ctx.attacker.getPs() - (dmg / 2f));
			System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")"
					+ " perdió PS al intentar drenar al rival dada la habilidad Viscosecreción");

		} else {
			if (ctx.attacker.getPs() != ctx.attacker.getInitialPs()) {
				// The half of damage done
				ctx.attacker.setPs(ctx.attacker.getPs() + (dmg / 2f));

				// If more PS received than initial PS, put the max limit at initial PS
				if (ctx.attacker.getPs() >= ctx.attacker.getInitialPs()) {
					ctx.attacker.setPs(ctx.attacker.getInitialPs());
				}
			}
		}
		return result;
	}
}
