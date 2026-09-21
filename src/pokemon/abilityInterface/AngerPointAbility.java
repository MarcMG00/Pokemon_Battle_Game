package pokemon.abilityInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class AngerPointAbility extends AbilityEffect {
	public AngerPointAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {

		// The attack has to be critical
		if (!attackResult.isCriticalAttack())
			return true;

		// Attack stage is already at the max
		if (owner.getAttackStage() >= 6)
			return true;

		// Puts max stage for attack
		owner.setAttackStage(6);
		System.out.println(owner.getName() + " aumentó su ataque al máximo gracias a su habilidad Irascible");

		return true;
	}
}
