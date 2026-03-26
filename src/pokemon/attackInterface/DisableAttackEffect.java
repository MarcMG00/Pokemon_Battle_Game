package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.Attack;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.State;

public class DisableAttackEffect implements AttackEffect {
	private int minTurns;
	private int maxTurns;

	public DisableAttackEffect(int minTurns, int maxTurns) {
		this.minTurns = minTurns;
		this.maxTurns = maxTurns;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		Attack disable = ctx.attacker.getNextMovement();
		Attack lastAttack = ctx.defender.getLastUsedAttack();

		disable.setPp(disable.getPp() - 1);

		// If rival hasn't used yet an attack => fails
		if (lastAttack == null || lastAttack.getId() == 0) {
			System.out.println("¡Pero no surtió efecto!");
			return result;
		}

		if (ctx.defender.hasActiveStatusCondition(StatusConditions.DISABLE))
			ctx.defender.setStatusCondition(new State());

		int turns = ctx.helperService.randomInt(minTurns, maxTurns);

		State attackDisabled = new State(StatusConditions.DISABLE, turns + 1);
		attackDisabled.setAttackDisabled(lastAttack);
		ctx.defender.setStatusCondition(attackDisabled);

		System.out.println(
				ctx.defender.getName() + " no podrá usar " + lastAttack.getName() + " por " + turns + " turnos");

		return result;
	}
}
