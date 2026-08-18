package pokemon.abilityInterface;

import pokemon.model.AttackAnalyzer;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class AnticipationAbility extends AbilityEffect {
	public AnticipationAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		// Notifies to Pokemon entering that rival has at least one attack of type "One
		// hit KO" or a super effective move
		if (AttackAnalyzer.hasDangerousAttack(defender) || AttackAnalyzer.hasSuperEffectiveAttack(defender, owner))
			System.out.println(owner.getName() + " se estremeció (habilidad Anticipación)");
	}
}
