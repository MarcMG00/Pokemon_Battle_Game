package pokemon.attackInterface;

import pokemon.model.Attack;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.AttackService;
import pokemon.model.Pokemon;

public class MimicEffect implements AttackEffect {
	private final AttackService attackService;

	public MimicEffect(AttackService attackService) {
		this.attackService = attackService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();
		Pokemon defender = ctx.getDefender();

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ") usó " + ctx.getAttack().getName());

		AttackResult result = new AttackResult();

		Attack lastAttack = ctx.getDefender().getLastUsedAttack();
		// No previous move
		if (lastAttack == null || lastAttack.getId() == 0) {
			System.out.println(ctx.getAttacker().getName() + " no pudo copiar ningún movimiento");
			return result;
		}

		if (lastAttack.isMimic() && !defender.isMimicking()) {
			System.out.println(ctx.getAttacker().getName() + " no puede copiar Mimético");
			return result;
		}

		ctx.consumePP();

		// Create a copy of the opponent's move
		Attack copiedAttack = attackService.createAttackInstance(lastAttack.getId());

		// Informative: copiedAttack is NOT the real move of the defender (it's a new
		// instance)
		attacker.setCopiedAttack(copiedAttack);
		System.out.println(attacker.getName() + " copió " + copiedAttack.getName());

		return result;
	}
}