package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class StaticAbility implements AbilityEffect {
	private static final double PARALYSIS_CHANCE = 0.30;

	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch) {

		// Attack must make contact
		if (!attack.getMakesContact())
			return;

		// Probability
		if (Math.random() >= PARALYSIS_CHANCE)
			return;

		// Try to apply paralysis
		attacker.trySetStatus(new State(StatusConditions.PARALYZED), null);
		System.out.println(
				attacker.getName() + " fue paralizado por la habilidad electricidad estática del Pokémon rival");
	}
}
