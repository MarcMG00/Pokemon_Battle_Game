package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class CounterAttackEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		if (ctx.attacker.getHasReceivedDamage()) {
			System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó Contraataque");

			float dmg = ctx.attacker.getDamageReceived() * 2f;
			result.addDamage(dmg);

			ctx.attack.setPp(ctx.attack.getPp() - 1);

			ctx.defender.setPs(ctx.defender.getPs() - dmg);

			System.out.println("Damage to Pokemon facing (" + ctx.defender.getName() + " (Id:" + ctx.defender.getId()
					+ ")" + ") : " + dmg);
		} else
			System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")"
					+ " no puede usar Contraataque ya que no recibió ningún ataque físico este turno");
		
		return result;
	}
}
