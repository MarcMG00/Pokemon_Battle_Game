package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;
import pokemon.model.State;

public class TrappedEffect implements AttackEffect {
	private final DamageService damageService;

	public TrappedEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		float dmg = damageService.doDammage(ctx);

		// Check if the Pokemon facing doesn't have the status Trapped (is a status that
		// can be accumulated with other ephemeral status)
		if (!ctx.defender.hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {
			System.out.println(ctx.defender.getName() + " quedó atrapado");

			int nbTurnsHoldingStatus = ctx.helperService.randomInt(4, 5);
			State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);
			ctx.defender.addEphemeralStatus(StatusConditions.TRAPPED, trapped);
		}

		ctx.defender.setPs(ctx.defender.getPs() - dmg);
		ctx.attack.setPp(ctx.attack.getPp() - 1);

		result.addDamage(dmg);
		return result;
	}
}
