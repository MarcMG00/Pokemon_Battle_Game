package pokemon.interfce;

import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class StenchAbility implements AbilityEffect {

	private static final double FLINCH_CHANCE = 0.10d;

	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch) {

		// 1️ - The attack has to do damage
		if (dmg == 0f)
			return;

		// 2 - Adds probability to flinch if attack already can flinch
		if (attack.getPercentageFlinched() == 0)
			return;

		// 3 - The defender can be flinched
		if (defender.getAbilitySelected().getId() == 39) {
			System.out.println(
					defender.getName() + " no se intimidó gracias a " + defender.getAbilitySelected().getName());
			return;
		}

		// 5 - Probability to be flinched
		if (Math.random() < percentageFlinch + FLINCH_CHANCE) {
			defender.setHasRetreated(true);
			System.out.println(defender.getName() + " retrocedió por el 'Hedor'! (sumado) : "
					+ (percentageFlinch + FLINCH_CHANCE) + " prob");
		}
	}
}