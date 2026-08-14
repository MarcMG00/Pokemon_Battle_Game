package pokemon.abilityInterface;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class AftermathAbility extends AbilityEffect {
	public AftermathAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {

		// Defender needs to be debilitated + attacker hasn't to have 006_Damp ability +
		// defender needs to receive a physical attack
		if (!defender.isFainted() || !defender.hasReceivedDamage() || attacker.hasDampAbility()
				|| !attack.makesContact())
			return;

		// Remove 25% of max PS from defender
		float removePS = attacker.getInitialPs() * 0.25f;
		attacker.setPs(attacker.getPs() - removePS);

		System.out.println(attacker.getName() + " sufrió daño dada la habilidad Detonación de " + defender.getName());
	}
}
