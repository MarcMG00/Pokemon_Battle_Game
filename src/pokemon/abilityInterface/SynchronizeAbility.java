package pokemon.abilityInterface;

import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;
import pokemon.model.State;

public class SynchronizeAbility extends AbilityEffect {
	public SynchronizeAbility(Pokemon owner) {
		super(owner);
	}
	
	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
		// Attacks that counter ability (Misty terrain / Safeguard)
		if (attack.getId() == 581 || attack.getId() == 219)
			return;

		// Already has a status
		if (attacker.hasStatusCondition())
			return;

		// Poisoned status
		if (defender.hasActiveStatusCondition(StatusConditions.POISONED)
				|| defender.hasActiveStatusCondition(StatusConditions.BADLY_POISONED)) {
			System.out.println(attacker.getName() + " fue envenenado por la habilidad Sincronía del Pokémon rival");
			attacker.setStatusCondition(new State(StatusConditions.POISONED));
			return;
		}

		// Burned status
		if (defender.hasActiveStatusCondition(StatusConditions.BURNED)) {
			System.out.println(attacker.getName() + " fue quemado por la habilidad Sincronía del Pokémon rival");
			attacker.setStatusCondition(new State(StatusConditions.BURNED));
			return;
		}

		// Paralyzed status
		if (defender.hasActiveStatusCondition(StatusConditions.PARALYZED)) {
			System.out.println(attacker.getName() + " fue paralizado por la habilidad Sincronía del Pokémon rival");
			attacker.setStatusCondition(new State(StatusConditions.PARALYZED));
			return;
		}
	}
}
