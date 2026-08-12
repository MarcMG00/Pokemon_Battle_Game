package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class StormDrainAbility extends AbilityEffect {
	public StormDrainAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Attack attack) {
		// Only water movements
		if (!attack.getType().equals("AGUA"))
			return true;

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + attack.getName());
		System.out.println(owner.getName() + " paró la electricidad gracias a la habilidad Colector");

		// Rises the special attack one point
		if (owner.getSpecialAttackStage() >= 6)
			System.out.println("El ataque especial de " + owner.getName() + " (Id:" + owner.getId() + ")"
					+ " no puede subir más!");
		else {
			owner.setSpecialAttackStage(Math.min(owner.getSpecialAttackStage() + 1, 6));
			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")" + " aumentó su Ataque especial!");
		}

		// Cancel damage and effects of the attack
		return false;
	}
}
