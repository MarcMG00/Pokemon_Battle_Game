package pokemon.interfce;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class QuickFeetAbility implements AbilityEffect {
	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
		if (defender.getAbilitySelected().getId() != 95)
			return;

		// If defender got a status condition and not already activated => increase by
		// 50% the speed
		if (!defender.getIsUsingAbility() && (defender.hasStatusCondition() || defender.hasEphemeralStatus())) {
			defender.setSpeed(defender.getSpeed() * 1.5f);
			defender.setIsUsingAbility(true);
			System.out.println(defender.getName()
					+ " aumentó su velocidad de 50% ya que sufrió un problema de estado (habilidad Pies rápidos)");
		}
	}

	@Override
	public void endOfTurn(Game game, Pokemon owner) {
		// If Pokemon has no more any kind of statuses => set again initial speed
		if (owner.getIsUsingAbility() && (owner.hasStatusCondition() || owner.hasEphemeralStatus())) {
			owner.setSpeed(owner.getInitialSpeed());
			owner.setIsUsingAbility(false);
			System.out.println(owner.getName()
					+ " regresó a su velocidad normal ya que no tiene ningún problema de estado (habilidad Pies rápidos)");
		}
	}
}
