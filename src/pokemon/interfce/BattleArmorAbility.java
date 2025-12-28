package pokemon.interfce;

import pokemon.model.Pokemon;

public class BattleArmorAbility implements AbilityEffect {
	@Override
	public void onAttack(Pokemon attacker, Pokemon defender) {
		System.out.println(defender.getName()
				+ " no puede ser dañado por un golpe crítico gracias a la habilidad 'Armadura batalla'");
	}

}
