package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class MistEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó Neblina");

		if (ctx.isMistActive())
			System.out.println("No tuvo ningún efecto ya que está en uso");

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		return result;
	}
}
