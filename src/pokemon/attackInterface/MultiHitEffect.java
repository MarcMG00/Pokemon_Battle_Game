package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;
import pokemon.model.HelperService;

public class MultiHitEffect implements AttackEffect {
	private final DamageService damageService;
	private final HelperService helperService;
	private int minHits;
	private int maxHits;

	public MultiHitEffect(HelperService helperService, DamageService damageService, int minHits, int maxHits) {
		this.minHits = minHits;
		this.maxHits = maxHits;
		this.damageService = damageService;
		this.helperService = helperService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		int hits = ctx.getAttacker().hasSkillLinkAbility() ? maxHits : helperService.randomInt(minHits, maxHits);

		float totalDamage = 0;

		for (int i = 0; i < hits; i++) {
			AttackResult hitResult = damageService.doDamage(ctx);
			totalDamage += hitResult.getDamage();
		}
		result.addDamage(totalDamage);

		System.out.println("Golpeó " + hits + " veces");

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);
		ctx.getDefender().setPs(ctx.getDefender().getPs() - totalDamage);

		return result;
	}
}
