package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class MistEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó Neblina");

		if (ctx.isMistEffectActivated)
			System.out.println("No tuvo ningún efecto ya que está en uso");

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		return result;
	}
}
