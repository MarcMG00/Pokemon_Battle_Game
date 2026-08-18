package pokemon.abilityInterface;

import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class HeavyMetalAbility extends AbilityEffect {
	public HeavyMetalAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		// Duplicates the weight by 2
		owner.setWeight(owner.getWeight() * 2);
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx) {
		// Reset to initial weight
		owner.setWeight(owner.getWeight() / 2);
	}
}
