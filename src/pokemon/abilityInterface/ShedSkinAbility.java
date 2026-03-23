package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class ShedSkinAbility implements AbilityEffect {
	private static final double REMOVE_STATE_CHANCE = 0.30d;

	@Override
	public void beforeEndOfTurn(Game game, Pokemon owner) {

		if (Math.random() <= REMOVE_STATE_CHANCE) {
			// Remove some status conditions and ephemeral status before it does effect
			if (owner.hasActiveStatusCondition(StatusConditions.FROZEN)
					|| owner.hasActiveStatusCondition(StatusConditions.BURNED)
					|| owner.hasActiveStatusCondition(StatusConditions.PARALYZED)
					|| owner.hasActiveStatusCondition(StatusConditions.POISONED)
					|| owner.hasActiveStatusCondition(StatusConditions.BADLY_POISONED)
					|| owner.hasActiveEphemeralStatus(StatusConditions.ASLEEP)) {
				owner.setStatusCondition(new State());
				owner.removeEphemeralStatus(StatusConditions.ASLEEP);

				System.out.println(owner.getName() + " se curó de algunos de sus estados gracias a su habilidad Mudar");
			}
		}
	}
}
