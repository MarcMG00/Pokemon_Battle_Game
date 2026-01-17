package pokemon.interfce;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class SynchronizeAbility implements AbilityEffect {
	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, Weather weather, boolean isWeatherSuppressed) {

		// Attacks that counter ability (Misty terrain / Safeguard)
		if (attack.getId() == 581 || attack.getId() == 219)
			return;

		// Already has a status
		if (attacker.getStatusCondition().getStatusCondition() != StatusConditions.NO_STATUS)
			return;

		// Poisoned status
		if (defender.getStatusCondition().getStatusCondition() == StatusConditions.POISONED) {

			System.out.println(attacker.getName() + " fue envenenado por la habilidad Sincronía del Pokémon rival");
			attacker.setStatusCondition(new State(StatusConditions.POISONED));
			return;
		}

		// Burned status
		if (defender.getStatusCondition().getStatusCondition() == StatusConditions.BURNED) {

			System.out.println(attacker.getName() + " fue quemado por la habilidad Sincronía del Pokémon rival");
			attacker.setStatusCondition(new State(StatusConditions.BURNED));
			return;
		}

		// Paralyzed status
		if (defender.getStatusCondition().getStatusCondition() == StatusConditions.PARALYZED) {

			System.out.println(attacker.getName() + " fue paralizado por la habilidad Sincronía del Pokémon rival");
			attacker.setStatusCondition(new State(StatusConditions.PARALYZED));
			return;
		}
	}
}
