package pokemon.attackInterface;

import pokemon.model.Ability;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class OneHitKOEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		Ability abilityDefender = ctx.defender.getAbilitySelected();

		float dmg = 0f;

		// One-Hit KO => Pokemon facing dies instantly (depending on conditions)
		if (abilityDefender.getId() == 5 && !abilityDefender.getAlreadyUsedOnEnter()) {
			ctx.defender.setPs(1f);
			abilityDefender.setAlreadyUsedOnEnter(true);
			dmg = ctx.defender.getPs() - 1f;

			System.out.println(ctx.defender.getName() + " (Id:" + ctx.defender.getId()
					+ "), se quedó a un PS gracias a la habilidad Robustez");
		} else {
			ctx.defender.setPs(0f);
			dmg = ctx.defender.getPs();

			System.out.println(ctx.defender.getName() + " (Id:" + ctx.defender.getId()
					+ "), se debilitó de un golpe con el ataque fulminante");
		}

		result.addDamage(dmg);
		return result;
	}
}
