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

	// -----------------------------
	// Generates a base Id to differentiate the same Pokemon (in the same/different
	// team)
	// -----------------------------
	public int generatePokemonInstanceId(Player player, Player ia, int baseId) {
		int count = 0;

		for (Pokemon pk : player.getPokemon()) {
			if (pk.getBaseId() == baseId)
				count++;
		}

		for (Pokemon pk : ia.getPokemon()) {
			if (pk.getBaseId() == baseId)
				count++;
		}

		if (count == 0)
			return baseId;

		return baseId * 1000 + count;
	}
}
