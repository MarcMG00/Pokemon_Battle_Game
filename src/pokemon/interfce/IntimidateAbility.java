package pokemon.interfce;

import pokemon.model.Ability;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class IntimidateAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(Game game, Pokemon owner, Pokemon defender) {

		Ability targetAbility = defender.getAbilitySelected();

		// Check immunity (Oblivious, Own tempo, etc.)
		if (targetAbility != null && (targetAbility.getId() == 12 || targetAbility.getId() == 20 || targetAbility.getId() == 29 || targetAbility.getId() == 39)) {
			System.out.println(defender.getName() + " no se intimidó gracias a " + targetAbility.getName());
			return;
		}

		// Mist blocks stat reduction
		if (game.getMistIsActivated()) {
			System.out.println("La neblina protege a " + defender.getName() + " de Intimidación");
			return;
		}

		// Apply attack stage reduction
		defender.setAttackStage(Math.min(defender.getAttackStage() - 1, -6));

		System.out.println(owner.getName() + " intimidó a " + defender.getName());
		System.out.println("El ataque de " + defender.getName() + " bajó");
	}
}
