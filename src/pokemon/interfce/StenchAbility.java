package pokemon.interfce;

import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class StenchAbility implements AbilityEffect {

	private static final double FLINCH_CHANCE = 0.10;

	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack) {

		// 1️ - The attack has to do damage
		if (!defender.getHasReceivedDamage() && defender.getDamageReceived() != 0)
			return;

		// 2 - The attack has not to have already flinch
		if (attack.getCanBeFlinched())
			return;

		// 3 - The defender can be flinched TODO >> when ability or some attack
		// forbidden to defender to be flinched
		// if (!defender.canBeFlinched())
		// return;

		// 4 - The defender has to attack in second place
		if (defender.getEffectiveSpeed() <= attacker.getEffectiveSpeed())
			return;

		// 5 - Probability to be flinched
		if (Math.random() < FLINCH_CHANCE) {
			defender.setHasRetreated(true);
			System.out.println(defender.getName() + " retrocedió por el 'Hedor'!");
		}
	}
}