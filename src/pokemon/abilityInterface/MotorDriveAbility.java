package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class MotorDriveAbility extends AbilityEffect {
	public MotorDriveAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Attack attack) {
		// Only electric movements
		if (!attack.getType().equals("ELECTRICO"))
			return true;

		// Attack has to do damage
		if (attack.getPower() <= 0)
			return true;

		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + attack.getName());

		System.out.println(owner.getName() + " absorbió la electricidad gracias a la habilidad Electromotor");

		// +1 on speed stage
		if (owner.getSpeedStage() < 6) {
			owner.setSpeedStage(Math.min(owner.getSpeedStage() + 1, 6));
			System.out.println(owner.getName() + " subió 1 nivel su velocidad");
		} else
			System.out.println(owner.getName() + " no puede subir más su velocidad");

		// Cancel damage and effects of the attack
		return false;
	}
}
