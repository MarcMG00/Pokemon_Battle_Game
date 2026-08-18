package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class WonderGuardAbility extends AbilityEffect {
	public WonderGuardAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Attack attack) {
		// Only super effective attacks (> 1f)
		if (attack.getEffectivenessAgainstPkFacing() > 1f) {
			System.out.println(
					"efectividad del ataque " + attack.getName() + " " + attack.getEffectivenessAgainstPkFacing());
			return true;
		}

		System.out.println(owner.getName() + " no puede ser dañado por " + attack.getName()
				+ " ya que no es supereficaz (gracias a la habilidad Superguarda)");

		return false; // cannot be attacked
	}
}
