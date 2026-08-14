package pokemon.abilityInterface;

import java.util.ArrayList;

import pokemon.enums.Weather;
import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;
import pokemon.model.PokemonType;

public class ColorChangeAbility extends AbilityEffect {
	public ColorChangeAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void afterAttack(BattleContext battleCtx, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double percentageFlinch, boolean isACriticAttack, Weather weather, boolean isWeatherSuppressed) {
		// Movement has to do damage
		if (dmg <= 0f)
			return;

		// Defender doesn't have to be substitute
		if (defender.hasSubstitute())
			return;

		// Attacker doesn't have to have 125_Sheer_force
		if (attacker.getAbilitySelected() != null && attacker.hasSheerForceAbility())
			return;

		// Movement type
		PokemonType moveType = attack.getPkType();
		if (moveType == null)
			return;

		// Change type
		ArrayList<PokemonType> types = new ArrayList<>();
		types.add(moveType);
		defender.setTypes(types);

		System.out.println(defender.getName() + " cambió su tipo a " + moveType.getName() + " gracias a Cambio Color!");
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx) {
		// Reinitialize types (ex : Kecleon change types during combat)
		owner.setTypes(owner.getInitialTypes());

		System.out.println(owner.getName() + " volvió a su(s) tipo(s) normal(es)");
	}
}
