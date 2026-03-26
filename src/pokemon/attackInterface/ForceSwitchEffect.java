package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class ForceSwitchEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();
		
		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		// 21_Suction_Cups doesn't allow to force change
		if (ctx.defender.getAbilitySelected().getId() == 21) {
			System.out
					.println(ctx.defender.getName() + " no puede ser forzado por el cambio dada su habilidad Ventosas");
			return result;
		}

		// If rival has no more Pokemon => it doesn't matter, but no fail
		if (!ctx.defendingPlayer.hasAvailableSwitch()) {
			System.out.println("Pero " + ctx.defender.getName() + " no tiene más Pokémon para cambiar.");
			return result;
		}

		// Force change
		ctx.defendingPlayer.setForceSwitchPokemon(true);

		System.out.println("¡" + ctx.defender.getName() + " fue arrastrado y obligado a retirarse!");
		return result;
	}

}
