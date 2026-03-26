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
		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		AttackResult result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		if (!ctx.attacker.hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)) {
			int turns = helperService.randomInt(minTurns, maxTurns);

			System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")"
					+ " usará el mismo ataque durante " + turns + " turnos.");

			State trappedByOwnAttack = new State(StatusConditions.TRAPPEDBYOWNATTACK, turns + 1);
			ctx.defender.addEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK, trappedByOwnAttack);

			// Only removes PP when choosing the attack
			ctx.attack.setPp(ctx.attack.getPp() - 1);
		}

		ctx.defender.setPs(ctx.defender.getPs() - dmg);

		return result;
	}
}
