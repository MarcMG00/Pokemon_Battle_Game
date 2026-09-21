package pokemon.abilityInterface;

import pokemon.enums.StatType;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class SpeedBoostAbility extends AbilityEffect {
	public SpeedBoostAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void endOfTurn(BattleContext battleCtx) {
		if (owner.getSpeedStage() >= 6)
			System.out.println(
					"La velocidad de " + owner.getName() + " (Id:" + owner.getId() + ")" + " no puede subir más!");
		else {
			owner.setStageValueStats(StatType.SPEED, 1, false);
			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")" + " aumentó su Velocidad!");
		}
	}
}
