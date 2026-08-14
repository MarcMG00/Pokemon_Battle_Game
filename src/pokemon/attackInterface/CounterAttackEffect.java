package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class CounterAttackEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();
		AttackResult result = new AttackResult();

		if (attacker.hasReceivedDamage()) {
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó Contraataque");

			float dmg = attacker.getDamageReceived() * 2f;
			result.addDamage(dmg);

			ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

			ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);

			ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

			System.out.println("Damage to Pokemon facing (" + ctx.getDefender().getName() + " (Id:"
					+ ctx.getDefender().getId() + ")" + ") : " + dmg);
		} else
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")"
					+ " no puede usar Contraataque ya que no recibió ningún ataque físico este turno");

		return result;
	}
}
