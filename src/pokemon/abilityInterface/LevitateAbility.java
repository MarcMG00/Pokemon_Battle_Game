package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class LevitateAbility implements AbilityEffect {
	@Override
	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack) {
		// Only movements that are not Ground type
		if (!attack.isGroundType())
			return true;

		System.out.println(defender.getName()
				+ " no puede ser atacado por movimientos de tipo tierra dada su habilidad Levitación");
		return false; // cannot be attacked
	}
}
