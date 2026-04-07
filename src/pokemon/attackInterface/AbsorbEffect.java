package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;
import pokemon.model.Pokemon;

public class AbsorbEffect implements AttackEffect {
	private final DamageService damageService;

	public AbsorbEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.getAttack().getName());

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);

		// Pokemon combating gets or loses health
		if (ctx.getDefender().getAbilitySelected().getId() == 64) {
			// The half of damage done
			attacker.setPs(attacker.getPs() - (dmg / 2f));
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
					+ " perdió PS al intentar drenar al rival dada la habilidad Viscosecreción");

		} else {
			if (attacker.getPs() != attacker.getInitialPs()) {
				// The half of damage done
				attacker.setPs(attacker.getPs() + (dmg / 2f));

				// If more PS received than initial PS, put the max limit at initial PS
				if (attacker.getPs() >= attacker.getInitialPs()) {
					attacker.setPs(attacker.getInitialPs());
				}
			}
		}
		return result;
	}
}
