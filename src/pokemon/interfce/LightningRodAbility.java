package pokemon.interfce;

import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class LightningRodAbility implements AbilityEffect {
	@Override
	public boolean beforeDamage(Game game, Pokemon attacker, Pokemon defender, Attack attack) {

		// Only electric movements
		if (!attack.getType().equals("ELECTRICO")) {
			return true;
		}

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + attack.getName());

		System.out.println(defender.getName() + " paró la electricidad gracias a la habilidad Pararrayos");

		// Special attack
		if (defender.getSpecialAttackStage() >= 6) {
			System.out.println("El ataque especial de " + defender.getName() + " (Id:" + defender.getId() + ")"
					+ " no puede subir más!");
		} else {
			defender.setSpecialAttackStage(Math.min(defender.getSpecialAttackStage() + 1, 6));
			System.out.println(defender.getName() + " (Id:" + defender.getId() + ")" + " aumentó su Ataque especial!");
		}

		// Cancel damage and effects of the attack
		return false;
	}
}
