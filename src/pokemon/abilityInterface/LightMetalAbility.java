package pokemon.abilityInterface;

import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class LightMetalAbility extends AbilityEffect {
	public LightMetalAbility(Pokemon owner) {
		super(owner);
	}
	
	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		// Divides the weight by 2
		owner.setWeight(owner.getWeight() / 2);
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx) {
		// Reset to initial weight
		owner.setWeight(owner.getWeight() * 2);
	}
}
