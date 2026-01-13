package pokemon.interfce;

import java.util.ArrayList;

import pokemon.model.Ability;
import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;
import pokemon.model.PokemonType;

public class ColorChangeAbility implements AbilityEffect {
	@Override
	public void afterAttack(Game game, Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			double precentageFlinch) {

		// Movement has to do damage
		if (dmg <= 0f)
			return;

		// Defender doesn't have to be substitute
		if (defender.getHasSubstitute())
			return;

		// Attacker doesn't have to have Sheer force
		Ability atkAbility = attacker.getAbilitySelected();
		if (atkAbility != null && atkAbility.getId() == 125)
			return;

		// Movement type
		PokemonType moveType = attack.getStrTypeToPkType();
		if (moveType == null)
			return;

		// Change type
		ArrayList<PokemonType> types = new ArrayList<>();
		types.add(moveType);
		defender.setTypes(types);

		System.out.println(defender.getName() + " cambió su tipo a " + moveType.getName() + " gracias a Cambio Color!");
	}

	@Override
	public void onSwitchOut(Game game, Pokemon owner) {

		// Reinitialize types (ex : Kecleon change types during combat)
		owner.setTypes(owner.getInitialTypes());

		System.out.println(owner.getName() + " volvió a su(s) tipo(s) normal(es)");
	}
}
