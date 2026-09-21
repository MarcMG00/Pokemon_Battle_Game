package pokemon.abilityInterface;

import pokemon.enums.StatType;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class WeakArmorAbility extends AbilityEffect {
	public WeakArmorAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		// Attack must be physical and defender must have received damage
		if (!attackCtx.getAttack().makesContact() || attackResult.hasDealtDamage())
			return true;

		// Defense decreases by 1 (if needed)
		if (owner.getDefenseStage() > -6) {
			owner.setStageValueStats(StatType.DEFENSE, 1, true);
			System.out.println(StatType.DEFENSE.name() + " de " + owner.getName() + " (Id:" + owner.getId() + ")"
					+ " bajó! a causa de Armadura frágil");
		}

		// Speed increases by 1 (if needed)
		if (owner.getSpeedStage() < 6) {
			owner.setStageValueStats(StatType.SPEED, 1, false);
			System.out.println(StatType.SPEED.name() + " de " + owner.getName() + " (Id:" + owner.getId() + ")"
					+ " subió! a causa de Armadura frágil");
		}

		return false;
	}
}
