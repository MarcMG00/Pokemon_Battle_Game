package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class CounterAttackEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;

	public CounterAttackEffect(AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();
		AttackResult result = new AttackResult();

		// Only use this attack if has received damage
		if (!attacker.hasReceivedDamage()) {
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
					+ " no puede usar Contraataque ya que no recibió ningún ataque físico este turno");
			return result;
		}

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Contraataque");

		float dmg = attacker.getDamageReceived() * 2f;
		result.addDamage(dmg);

		ctx.consumePP();

		attackResolutionService.resolveDamage(ctx, result);

		System.out.println("Damage to Pokemon facing (" + ctx.getDefender().getName() + " (Id:"
				+ ctx.getDefender().getId() + ")" + ") : " + dmg);

		return result;
	}
}
