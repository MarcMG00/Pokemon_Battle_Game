package pokemon.abilityInterface;

import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class DownloadAbility implements AbilityEffect {
	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon owner, Pokemon defender) {

		if (battleCtx.getStatService().getEffectiveDefense(defender) < battleCtx.getStatService()
				.getEffectiveSpecialDefense(defender)) {
			owner.setAttackStage(Math.min(owner.getAttackStage() + 1, 6));
			owner.setIsAttackBoostedFromDownloadAbility(true);
			System.out.println("El ataque de " + owner.getName() + " aumentó gracias a su habilidad Descarga");
		} else {
			owner.setSpecialAttackStage(Math.min(owner.getSpecialAttackStage() + 1, 6));
			System.out.println("El ataque especial de " + owner.getName() + " aumentó gracias a su habilidad Descarga");
		}
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx, Pokemon owner) {
		// Reinitialize the activation of ability => reduce one level the stat increased
		if (owner.getIsAttackBoostedFromDownloadAbility()) {
			owner.setAttackStage(Math.max(owner.getAttackStage() - 1, -6));
		} else {
			owner.setSpecialAttackStage(Math.max(owner.getSpecialAttackStage() - 1, -6));
		}
		owner.setIsAttackBoostedFromDownloadAbility(false);
	}
}
