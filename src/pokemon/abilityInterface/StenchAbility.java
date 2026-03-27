package pokemon.abilityInterface;

import pokemon.enums.SecondaryEffectType;
import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class StenchAbility implements AbilityEffect {

	private static final double FLINCH_CHANCE = 0.10d;

	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
		// 1️ - The attack has to do damage
		if (dmg == 0f)
			return;

		// 2 - Adds probability to flinch if attack already can flinch
		if (!attack.hasActiveSecondaryEffect(SecondaryEffectType.FLINCH))
			return;

		// 3 - The defender can be flinched
		// 98_Magic_Guard annuls secondary damage effects
		if (defender.getAbilitySelected().getId() == 98)
			return;

		// 4 - The defender can be intimidated
		if (defender.getAbilitySelected().getId() == 39) {
			System.out.println(
					defender.getName() + " no se intimidó gracias a " + defender.getAbilitySelected().getName());
			return;
		}

		// 5 - Probability to be flinched
		if (Math.random() < percentageFlinch + FLINCH_CHANCE) {
			defender.setHasRetreated(true);
			System.out.println(defender.getName() + " retrocedió por el 'Hedor'! (sumado) : "
					+ (percentageFlinch + FLINCH_CHANCE) + " prob");
		}
	}
}