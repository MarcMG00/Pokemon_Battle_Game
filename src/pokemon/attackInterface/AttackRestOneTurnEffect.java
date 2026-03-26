package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class AttackRestOneTurnEffect implements AttackEffect {
	private final DamageService damageService;

	public AttackRestOneTurnEffect(DamageService damageService) {
		this.damageService = damageService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();
		
		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		float dmg = damageService.doDammage(ctx);

		ctx.attack.setPp(ctx.attack.getPp() - 1);
		ctx.defender.setPs(ctx.defender.getPs() - dmg);

		// Pokemon combating cannot do anything next round
		ctx.attacker.setCanDonAnythingNextRound(false);
		
		result.addDamage(dmg);
		return result;
	}
}
