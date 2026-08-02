package pokemon.model;

public class HelperService {
	// -----------------------------
	// Gets random int (number of attacks or put normal states)
	// -----------------------------
	public int randomInt(int min, int max) {
		return min + (int) (Math.random() * (max - min + 1));
	}

	// -----------------------------
	// Gets number of turns (applying abilities conditions)
	// -----------------------------
	public int randomTurnsAbilitiesConditions(int min, int max, AttackContext ctx) {
		int nbTurnsHoldingStatus = randomInt(min, max);

		// 48_Early_Bird ability
		if (ctx.getDefender().hasEarlyBirdAbility()) {
			nbTurnsHoldingStatus = nbTurnsHoldingStatus / 2;
			System.out.println(ctx.getDefender().getName() + " (Id:" + ctx.getDefender().getId()
					+ "), se quedará dormido la mitad de turnos gracias a su habilidad Madrugar");
		}

		return nbTurnsHoldingStatus;
	}
}
