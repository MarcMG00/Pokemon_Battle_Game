package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class MotorDriveAbility implements AbilityEffect {
	@Override
	public boolean beforeDamage(Game game, Pokemon attacker, Pokemon defender, Attack attack) {

		// Only electric movements
		if (!attack.getType().equals("ELECTRICO"))
			return true;

		// Attack has to do damage
		if (attack.getPower() <= 0)
			return true;

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + attack.getName());

		System.out.println(defender.getName() + " absorbió la electricidad gracias a la habilidad Electromotor");

		// +1 on speed stage
		if (defender.getSpeedStage() < 6) {
			defender.setSpeedStage(Math.max(defender.getSpeedStage() + 1, 6));
			System.out.println(defender.getName() + " subió 1 nivel su velocidad");
		} else
			System.out.println(defender.getName() + " no puede subir más su velocidad su velocidad");

		// Cancel damage and effects of the attack
		return false;
	}
}
