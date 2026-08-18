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

		ctx.getDefender().setPs(Math.max(ctx.getDefender().getPs() - dmg, 0));

		// Pokemon combating gets or loses health
		if (ctx.getDefender().hasLiquidOozeAbility()) {
			// The half of damage done
			attacker.setPs(Math.max(attacker.getPs() - (dmg / 2f), 0));
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
					+ " perdió PS al intentar drenar al rival dada la habilidad Viscosecreción");

		} else {
			if (!attacker.hasMaxPS()) {
				// The half of damage done
				attacker.setPs(Math.min(attacker.getPs() + (dmg / 2f), attacker.getInitialPs()));
			}
		}
		return result;
	}
}
