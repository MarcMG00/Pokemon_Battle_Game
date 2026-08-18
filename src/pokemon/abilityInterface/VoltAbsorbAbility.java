package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class VoltAbsorbAbility extends AbilityEffect {
	public VoltAbsorbAbility(Pokemon owner) {
		super(owner);
	}

	private static final float HEAL_PERCENT = 0.25f;

	@Override
	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Attack attack) {
		// Only electric movements
		if (!attack.getType().equals("ELECTRICO"))
			return true;

		// Attack has to do damage
		if (attack.getPower() <= 0)
			return true;

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + attack.getName());

		System.out.println(owner.getName() + " absorbió la electricidad gracias a la habilidad Absorbe electricidad");

		// Heals 25% of max PS
		if (!owner.hasMaxPS()) {
			float heal = owner.getInitialPs() * HEAL_PERCENT;
			owner.setPs(Math.min(owner.getPs() + heal, owner.getInitialPs()));
			System.out.println(owner.getName() + " recuperó " + heal + " PS");
		} else
			System.out.println(owner.getName() + " no puede recuperar más PS");

		// Cancel damage and effects of the attack
		return false;
	}
}
