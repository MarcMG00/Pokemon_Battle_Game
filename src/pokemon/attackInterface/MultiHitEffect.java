package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class MultiHitEffect implements AttackEffect {
	private final DamageService damageService;
	private int minHits;
	private int maxHits;

	public MultiHitEffect(DamageService damageService, int minHits, int maxHits) {
		this.minHits = minHits;
		this.maxHits = maxHits;
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		int hits = ctx.attacker.getAbilitySelected().getId() == 92 ? maxHits
				: ctx.helperService.randomInt(minHits, maxHits);

		float totalDamage = 0;

		for (int i = 0; i < hits; i++) {
			totalDamage += damageService.doDammage(ctx);
		}
		result.addDamage(totalDamage);

		System.out.println("Golpeó " + hits + " veces");

		ctx.attack.setPp(ctx.attack.getPp() - 1);
		ctx.defender.setPs(ctx.defender.getPs() - totalDamage);
		
		return result;
	}
}
