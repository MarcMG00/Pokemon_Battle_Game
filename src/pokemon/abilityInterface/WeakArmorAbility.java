package pokemon.abilityInterface;

import pokemon.enums.StatType;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class WeakArmorAbility implements AbilityEffect {
	public boolean onHit(AttackContext attackCtx, AttackResult attackResult, double percentageFlinch) {
		// Defender must have the current ability
		if (!attackCtx.getDefender().hasWeakArmorAbility())
			return true;

		// Attack must be physical and defender must have received damage
		if (!attackCtx.getAttack().makesContact() || attackResult.getDamage() <= 0f)
			return true;

		// Defense decreases by 1 (if needed)
		if (attackCtx.getDefender().getDefenseStage() > 1) {
			attackCtx.getDefender().setStageValueStats(StatType.DEFENSE, 1, true);
			System.out.println(StatType.DEFENSE.name() + " de " + attackCtx.getDefender().getName() + " (Id:"
					+ attackCtx.getDefender().getId() + ")" + " bajó!");
		}

		// Speed increases by 1 (if needed)
		if (attackCtx.getDefender().getSpeedStage() < 6) {
			attackCtx.getDefender().setStageValueStats(StatType.SPEED, 1, false);
			System.out.println(StatType.SPEED.name() + " de " + attackCtx.getDefender().getName() + " (Id:"
					+ attackCtx.getDefender().getId() + ")" + " subió!");
		}

		return false;
	}
}
