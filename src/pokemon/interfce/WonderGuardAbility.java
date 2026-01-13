package pokemon.interfce;

import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class WonderGuardAbility implements AbilityEffect {
	@Override
	public boolean beforeDamage(Game game, Pokemon attacker, Pokemon defender, Attack attack) {

		// Only super effective attacks (> 1f)
		if (attack.getEffectivenessAgainstPkFacing() > 1f) {
			System.out.println("efectividad del ataque " + attack.getName()
			+ " " + attack.getEffectivenessAgainstPkFacing());
			return true;
		}

		System.out.println(defender.getName() + " no puede ser dañado por " + attack.getName()
				+ " ya que no es supereficaz (gracias a la habilidad Superguarda)");

		return false; // cannot be attacked
	}
}
