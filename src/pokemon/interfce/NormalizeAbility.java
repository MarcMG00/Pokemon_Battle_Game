package pokemon.interfce;

import pokemon.model.Attack;
import pokemon.model.Game;
import pokemon.model.Pokemon;

public class NormalizeAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(Game game, Pokemon owner, Pokemon defender) {

		if (!owner.getIsUsingAbility()) {
			// Pass each attack to NORMAL type
			for (Attack attack : owner.getFourPrincipalAttacks()) {
				attack.setType("NORMAL");
				// Set the type of the attack to his Pokemon type instead of a string
				attack.transformStrTypeToPokemonType(game.getTypes());

				// Increase power to 20% more
				attack.setPower(attack.getPower() * 1.2f);
			}

			// Let applied the ability all the time (even if changin the Pokemon)
			owner.setIsUsingAbility(true);

			System.out.println(
					owner.getName() + " transformó todos sus ataques a tipo NORMAL dada su habilidad Normalidad");
		}
	}
}
