package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class ShedSkinAbility implements AbilityEffect {
	private static final double REMOVE_STATE_CHANCE = 0.90d;

	@Override
	public void beforeEndOfTurn(Game game, Pokemon owner) {

		// Remove some status conditions and ephemeral status before it does effect
		if (owner.getStatusCondition().getStatusCondition() == StatusConditions.FROZEN
				|| owner.getStatusCondition().getStatusCondition() == StatusConditions.BURNED
				|| owner.getStatusCondition().getStatusCondition() == StatusConditions.PARALYZED
				|| owner.getStatusCondition().getStatusCondition() == StatusConditions.POISONED
				|| owner.getStatusCondition().getStatusCondition() == StatusConditions.BADLY_POISONED
				|| owner.getEphemeralStates().stream()
						.anyMatch(e -> e.getStatusCondition() == StatusConditions.ASLEEP)) {
			owner.setStatusCondition(new State(StatusConditions.NO_STATUS));

			owner.getEphemeralStates().stream().filter(e -> e.getStatusCondition() == StatusConditions.ASLEEP)
					.findFirst().ifPresent(e -> owner.getEphemeralStates().remove(e));

			System.out.println(owner.getName() + " se curó de algunos de sus estados gracias a su habilidad Mudar");
		}
	}
}
