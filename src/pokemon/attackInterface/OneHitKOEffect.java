package pokemon.attackInterface;

import pokemon.model.Ability;
import pokemon.model.AttackContext;
import pokemon.model.AttackResolutionService;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class OneHitKOEffect implements AttackEffect {
	private final AttackResolutionService attackResolutionService;

	public OneHitKOEffect(AttackResolutionService attackResolutionService) {
		this.attackResolutionService = attackResolutionService;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		Pokemon defender = ctx.getDefender();
		Ability abilityDefender = defender.getAbilitySelected();

		float dmg = 0f;

		// One-Hit KO => Pokemon facing dies instantly (depending on conditions)
		if (ctx.getDefender().hasSturdyAbility() && !abilityDefender.alreadyUsedOnEnter()
				&& ctx.getDefender().hasMaxPS()) {
			dmg = defender.getPs() - 1f;

			abilityDefender.setAlreadyUsedOnEnter(true);
			System.out.println(ctx.getDefender().getName() + " (Id:" + ctx.getDefender().getId()
					+ "), se quedó a un PS gracias a la habilidad Robustez");
		} else {
			dmg = defender.getPs();

			System.out.println(ctx.getDefender().getName() + " (Id:" + ctx.getDefender().getId()
					+ "), se debilitó de un golpe con el ataque fulminante");
		}

		result.addDamage(dmg);

		attackResolutionService.resolveDamage(ctx, result);

		return result;
	}
}
