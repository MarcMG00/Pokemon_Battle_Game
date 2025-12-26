package pokemon.interfce;

import pokemon.model.Game;
import pokemon.model.Pokemon;

public class SpeedBoostAbility implements AbilityEffect {
	@Override
	public void endOfTurn(Game game, Pokemon owner) {

		if (owner.getSpeedStage() >= 6) {
			System.out.println(
					"La velocidad de " + owner.getName() + " (Id:" + owner.getId() + ")" + " no puede subir más!");
		} else {
			owner.setSpeedStage(Math.min(owner.getSpeedStage() + 1, 6));
			System.out.println(owner.getName() + " (Id:" + owner.getId() + ")" + " aumentó su Velocidad!");
		}
	}
}
