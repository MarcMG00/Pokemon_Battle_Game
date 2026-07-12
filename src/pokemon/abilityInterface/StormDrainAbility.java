package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class StormDrainAbility implements AbilityEffect {
	@Override
	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack) {
		// Only water movements
		if (!attack.getType().equals("AGUA"))
			return true;

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + attack.getName());
		System.out.println(defender.getName() + " paró la electricidad gracias a la habilidad Colector");

		// Rises the special attack one point
		if (defender.getSpecialAttackStage() >= 6)
			System.out.println("El ataque especial de " + defender.getName() + " (Id:" + defender.getId() + ")"
					+ " no puede subir más!");
		else {
			defender.setSpecialAttackStage(Math.min(defender.getSpecialAttackStage() + 1, 6));
			System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " aumentó su Ataque especial!");
		}

		// Cancel damage and effects of the attack
		return false;
	}
}
