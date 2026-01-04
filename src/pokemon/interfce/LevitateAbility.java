package pokemon.interfce;

import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class LevitateAbility implements AbilityEffect {
	@Override
	public boolean beforeDamage(Game game, Pokemon attacker, Pokemon defender, Attack attack) {

		// Only movements that are not Ground type
		if (attack.getStrTypeToPkType().getId() != 16) {
			return true;
		}

		System.out.println(defender.getName()
				+ " no puede ser atacado por movimientos de tipo tierra dada su habilidad Levitación");
		return false; // cannot be attacked
	}
}
