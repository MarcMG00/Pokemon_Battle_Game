package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class ForceSwitchEffect implements AttackEffect {

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		// 21_Suction_Cups doesn't allow to force change
		if (ctx.getDefender().hasSuctionCupsAbility()) {
			System.out.println(
					ctx.getDefender().getName() + " no puede ser forzado por el cambio dada su habilidad Ventosas");
			return result;
		}

		// If rival has no more Pokemon => it doesn't matter, but no fail
		if (!ctx.getDefendingPlayer().hasAvailableSwitch()) {
			System.out.println("Pero " + ctx.getDefender().getName() + " no tiene más Pokémon para cambiar.");
			return result;
		}

		// Force change
		ctx.getDefendingPlayer().setForcedSwitchPokemon(true);

		System.out.println("¡" + ctx.getDefender().getName() + " fue arrastrado y obligado a retirarse!");
		return result;
	}

}
