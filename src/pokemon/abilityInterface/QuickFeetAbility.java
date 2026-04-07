package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class QuickFeetAbility implements AbilityEffect {
	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
		if (defender.getAbilitySelected().getId() != 95)
			return;

		// If defender got a status condition and not already activated => increase by
		// 50% the speed
		if (defender.hasStatusCondition() || defender.hasEphemeralStatus())
			System.out.println(defender.getName()
					+ " aumentó su velocidad de 50% ya que sufrió un problema de estado (habilidad Pies rápidos)");
	}

	@Override
	public void endOfTurn(BattleContext battleCtx, Pokemon owner) {
		// If Pokemon has no more any kind of statuses => set again initial speed
		if (owner.hasStatusCondition() || owner.hasEphemeralStatus())
			System.out.println(owner.getName()
					+ " regresó a su velocidad normal ya que no tiene ningún problema de estado (habilidad Pies rápidos)");
	}
}
