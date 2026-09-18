package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;

public class FixedDamageEffect implements AttackEffect {
	private final float fixedDamage;
	private final AttackResolutionService attackResolutionService;

	public FixedDamageEffect(float fixedDamage, AttackResolutionService attackResolutionService) {
		this.fixedDamage = fixedDamage;
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		result.addDamage(fixedDamage);
		attackResolutionService.resolveDamage(ctx, result);

		return result;
	}
}
