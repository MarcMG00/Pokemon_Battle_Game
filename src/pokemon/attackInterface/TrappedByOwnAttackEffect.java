package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;
import pokemon.model.HelperService;
import pokemon.model.State;

public class TrappedByOwnAttackEffect implements AttackEffect {
	private final DamageService damageService;
	private final HelperService helperService;
	private final int minTurns;
	private final int maxTurns;

	public TrappedByOwnAttackEffect(HelperService helperService, DamageService damageService, int minTurns,
			int maxTurns) {
		this.damageService = damageService;
		this.helperService = helperService;
		this.minTurns = minTurns;
		this.maxTurns = maxTurns;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		if (!ctx.getAttacker().hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)) {
			int turns = helperService.randomInt(minTurns, maxTurns);

			System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")"
					+ " usará el mismo ataque durante " + turns + " turnos.");

			State trappedByOwnAttack = new State(StatusConditions.TRAPPEDBYOWNATTACK, turns + 1);
			ctx.getDefender().addEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK, trappedByOwnAttack);

			// Only removes PP when choosing the attack
			ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);
		}

		ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);

		ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

		return result;
	}
}
