package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class WaterAbsorbAbility extends AbilityEffect {
	public WaterAbsorbAbility(Pokemon owner) {
		super(owner);
	}

	private static final float HEAL_PERCENT = 0.25f;

	@Override
	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Attack attack) {
		// Only water movements
		if (!attack.getType().equals("AGUA"))
			return true;

		// Attack has to do damage
		if (attack.getPower() <= 0)
			return true;

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + attack.getName());

		System.out.println(owner.getName() + " absorbió la agua gracias a la habilidad Absorbe agua");

		// Heals 25% of max PS
		float heal = owner.getInitialPs() * HEAL_PERCENT;
		owner.setPs(Math.min(owner.getPs() + heal, owner.getInitialPs()));

		System.out.println(owner.getName() + " recuperó " + heal + " PS");

		// Cancel damage and effects of the attack
		return false;
	}
}
