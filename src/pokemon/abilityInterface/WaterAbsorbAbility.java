package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class WaterAbsorbAbility implements AbilityEffect {
	private static final float HEAL_PERCENT = 0.25f;

	@Override
	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack) {
		// Only water movements
		if (!attack.getType().equals("AGUA"))
			return true;

		// Attack has to do damage
		if (attack.getPower() <= 0)
			return true;

		System.out.println(defender.getName() + " absorbió la agua gracias a la habilidad Absorbe agua");

		// Heals 25% of max PS
		float heal = defender.getInitialPs() * HEAL_PERCENT;
		defender.setPs(Math.min(defender.getPs() + heal, defender.getInitialPs()));

		System.out.println(defender.getName() + " recuperó " + heal + " PS");

		// Cancel damage and effects of the attack
		return false;
	}
}
