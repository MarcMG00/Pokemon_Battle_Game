package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class QuickFeetAbility extends AbilityEffect {
	public QuickFeetAbility(Pokemon owner) {
		super(owner);
	}
	
	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
		if (!defender.hasQuickFeetAbility())
			return;

		// If defender got a status condition => informative
		// message
		if (defender.hasStatusCondition() || defender.hasEphemeralStatus())
			System.out.println(defender.getName()
					+ " aumentó su velocidad de 50% ya que sufrió un problema de estado (habilidad Pies rápidos)");
	}
}
