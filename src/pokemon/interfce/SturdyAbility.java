package pokemon.interfce;

import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class SturdyAbility implements AbilityEffect {

	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch) {

		// Cannot be defeated by one hit KO or by one attack if PS are on max
		if (attack.getIsOneHitKO() || (defender.getInitialPs() == defender.getPs() && dmg >= defender.getPs())) {
			defender.setPs(1f);
		}
	}
}
