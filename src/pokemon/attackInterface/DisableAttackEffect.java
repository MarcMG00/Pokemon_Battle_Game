package pokemon.attackInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.Attack;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.HelperService;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class DisableAttackEffect implements AttackEffect {
	private int minTurns;
	private int maxTurns;
	private final HelperService helperService;

	public DisableAttackEffect(HelperService helperService, int minTurns, int maxTurns) {
		this.minTurns = minTurns;
		this.maxTurns = maxTurns;
		this.helperService = helperService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon defender = ctx.getDefender();
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		Attack disable = ctx.getAttacker().getNextMovement();
		Attack lastAttack = defender.getLastUsedAttack();

		disable.setPp(disable.getPp() - 1);

		// If rival hasn't used yet an attack => fails
		if (lastAttack == null || lastAttack.getId() == 0) {
			System.out.println("¡Pero no surtió efecto!");
			return result;
		}

		// Reinitialize the disable effect if already applied
		if (defender.hasActiveEphemeralStatus(StatusConditions.DISABLE))
			defender.removeEphemeralStatus(StatusConditions.DISABLE);

		int turns = helperService.randomInt(minTurns, maxTurns);

		State attackDisabled = new State(StatusConditions.DISABLE, turns + 1);
		attackDisabled.setAttackDisabled(lastAttack);
		defender.addEphemeralStatus(StatusConditions.DISABLE, attackDisabled);

		System.out.println(defender.getName() + " no podrá usar " + lastAttack.getName() + " por " + turns + " turnos");

		return result;
	}
}
