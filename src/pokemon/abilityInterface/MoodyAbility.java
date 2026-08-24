package pokemon.abilityInterface;

import java.util.ArrayList;
import java.util.Random;

import pokemon.enums.StatType;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class MoodyAbility extends AbilityEffect {
	private final Random random = new Random();

	public MoodyAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void endOfTurn(BattleContext battleCtx) {
		// ==========================================================
		// 1 Gets all the stats that can be boosted
		// 0 = Attack
		// 1 = Special Attack
		// 2 = Defense
		// 3 = Special Defense
		// 4 = Speed
		// 5 = Precision
		// 6 = Evasion
		// ==========================================================
		ArrayList<Integer> statsCanIncrease = new ArrayList<>();

		for (int stat = 0; stat < 7; stat++) {
			if (getStage(owner, stat) < 6)
				statsCanIncrease.add(stat);
		}

		// ==========================================================
		// 2 If all stats are at +6, only can be decreased one
		// ==========================================================
		if (statsCanIncrease.isEmpty()) {
			ArrayList<Integer> statsCanDecrease = new ArrayList<>();

			for (int stat = 0; stat < 7; stat++) {
				if (getStage(owner, stat) > -6)
					statsCanDecrease.add(stat);
			}

			if (!statsCanDecrease.isEmpty()) {
				int statToDecrease = statsCanDecrease.get(random.nextInt(statsCanDecrease.size()));

				owner.setStageValueStats(getStatType(statToDecrease), 1, true);
				System.out.println(owner.getName() + "(" + owner.getId() + ") " + " bajó " + getStatName(statToDecrease)
						+ " a causa de Veleta.");
			}

			return;
		}

		// ==========================================================
		// 3 Gets randomly the stat to increase
		// ==========================================================
		int statToIncrease = statsCanIncrease.get(random.nextInt(statsCanIncrease.size()));

		int currentStage = getStage(owner, statToIncrease);

		// Increase 2 levels => if is at +5, only increase by 1
		int increaseAmount = Math.min(2, 6 - currentStage);

		owner.setStageValueStats(getStatType(statToIncrease), increaseAmount, false);
		System.out.println(owner.getName() + "(" + owner.getId() + ") " + " subió " + getStatName(statToIncrease)
				+ " en " + increaseAmount + " nivel(es) gracias a Veleta.");

		// ==========================================================
		// 4 Gets a different stat to decrease
		// ==========================================================
		ArrayList<Integer> statsCanDecrease = new ArrayList<>();

		for (int stat = 0; stat < 7; stat++) {
			if (stat == statToIncrease)
				continue;

			if (getStage(owner, stat) > -6)
				statsCanDecrease.add(stat);
		}

		// ==========================================================
		// 5 Gets randomly the stat to decrease
		// ==========================================================
		if (!statsCanDecrease.isEmpty()) {
			int statToDecrease = statsCanDecrease.get(random.nextInt(statsCanDecrease.size()));

			owner.setStageValueStats(getStatType(statToDecrease), 1, true);
			System.out.println(owner.getName() + "(" + owner.getId() + ") " + " bajó " + getStatName(statToDecrease)
					+ " a causa de Veleta.");
		}
	}

	// ==============================================================
	// Gets stat stage depending on random number
	// ==============================================================
	private int getStage(Pokemon pokemon, int stat) {
		switch (stat) {
		case 0:
			return pokemon.getAttackStage();
		case 1:
			return pokemon.getSpecialAttackStage();
		case 2:
			return pokemon.getDefenseStage();
		case 3:
			return pokemon.getSpecialDefenseStage();
		case 4:
			return pokemon.getSpeedStage();
		case 5:
			return pokemon.getPrecisionStage();
		case 6:
			return pokemon.getEvasionStage();
		default:
			return pokemon.getAttackStage(); // just in case
		}
	}

	// ==============================================================
	// Gets stat type depending on random number
	// ==============================================================
	private StatType getStatType(int stat) {
		switch (stat) {
		case 0:
			return StatType.ATTACK;
		case 1:
			return StatType.SPECIAL_ATTACK;
		case 2:
			return StatType.DEFENSE;
		case 3:
			return StatType.SPECIAL_DEFENSE;
		case 4:
			return StatType.SPEED;
		case 5:
			return StatType.PRECISION;
		case 6:
			return StatType.EVASION;
		default:
			return StatType.ATTACK; // just in case
		}
	}

	// ==============================================================
	// Shows stat name on the logs
	// ==============================================================
	private String getStatName(int stat) {
		switch (stat) {
		case 0:
			return "Ataque";
		case 1:
			return "Ataque Especial";
		case 2:
			return "Defensa";
		case 3:
			return "Defensa Especial";
		case 4:
			return "Velocidad";
		case 5:
			return "Precisión";
		case 6:
			return "Evasión";
		default:
			return "Ataque por defecto"; // just in case
		}
	}
}
