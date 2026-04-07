package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;
import pokemon.model.HelperService;
import pokemon.model.State;

public class TrappedEffect implements AttackEffect {
	private final DamageService damageService;
	private final HelperService helperService;

	public TrappedEffect(HelperService helperService, DamageService damageService) {
		this.damageService = damageService;
		this.helperService = helperService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		// Check if the Pokemon facing doesn't have the status Trapped (is a status that
		// can be accumulated with other ephemeral status)
		if (!ctx.getDefender().hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {
			System.out.println(ctx.getDefender().getName() + " quedó atrapado");

			int nbTurnsHoldingStatus = helperService.randomInt(4, 5);
			State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus + 1);
			ctx.getDefender().addEphemeralStatus(StatusConditions.TRAPPED, trapped);
		}

		ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);
		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		return result;
	}
}
