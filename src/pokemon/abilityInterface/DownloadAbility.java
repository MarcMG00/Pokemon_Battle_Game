package pokemon.abilityInterface;

import pokemon.enums.StatType;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class DownloadAbility extends AbilityEffect {
	public DownloadAbility(Pokemon owner) {
		super(owner);
	}

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {
		if (battleCtx.getStatService().getEffectiveDefense(defender, false) < battleCtx.getStatService()
				.getEffectiveSpecialDefense(defender, false, battleCtx.getWeather())) {
			owner.setStageValueStats(StatType.ATTACK, 1, false);
			System.out.println("El ataque de " + owner.getName() + " aumentó gracias a su habilidad Descarga");
			owner.setIsAttackBoostedFromDownloadAbility(true);
		} else {
			owner.setStageValueStats(StatType.SPECIAL_ATTACK, 1, false);
			System.out.println("El ataque especial de " + owner.getName() + " aumentó gracias a su habilidad Descarga");
		}
	}

	@Override
	public void onSwitchOut(BattleContext battleCtx) {
		// Reinitialize the activation of ability => reduce one level the stat increased
		if (owner.isAttackBoostedFromDownloadAbility()) {
			owner.setStageValueStats(StatType.ATTACK, 1, true);
		} else {
			owner.setStageValueStats(StatType.SPECIAL_ATTACK, 1, true);
		}

		owner.setIsAttackBoostedFromDownloadAbility(false);
	}
}
