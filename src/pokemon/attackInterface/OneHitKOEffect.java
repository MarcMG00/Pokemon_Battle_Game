package pokemon.attackInterface;

import pokemon.model.Ability;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class OneHitKOEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		Ability abilityDefender = ctx.getDefender().getAbilitySelected();

		float dmg = 0f;

		// One-Hit KO => Pokemon facing dies instantly (depending on conditions)
		if (abilityDefender.getId() == 5 && !abilityDefender.getAlreadyUsedOnEnter()) {
			ctx.getDefender().setPs(1f);
			abilityDefender.setAlreadyUsedOnEnter(true);
			dmg = ctx.getDefender().getPs() - 1f;

			System.out.println(ctx.getDefender().getName() + " (Id:" + ctx.getDefender().getId()
					+ "), se quedó a un PS gracias a la habilidad Robustez");
		} else {
			ctx.getDefender().setPs(0f);
			dmg = ctx.getDefender().getPs();

			System.out.println(ctx.getDefender().getName() + " (Id:" + ctx.getDefender().getId()
					+ "), se debilitó de un golpe con el ataque fulminante");
		}

		ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

		result.addDamage(dmg);
		return result;
	}
}
