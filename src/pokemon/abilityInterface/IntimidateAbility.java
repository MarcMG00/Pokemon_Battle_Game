package pokemon.abilityInterface;

import pokemon.enums.StatType;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;
import pokemon.model.StatService;

public class IntimidateAbility extends AbilityEffect {
	public IntimidateAbility(Pokemon owner) {
		super(owner);
		this.statService = new StatService();
	}

	private final StatService statService;

	@Override
	public void onSwitchIn(BattleContext battleCtx, Pokemon defender) {

		System.out.println(owner.getName() + " intimidó a " + defender.getName());

		// Check immunity (Oblivious, Own tempo, etc.)
		if (statService.isIntimidateImmune(defender)) {
			System.out.println(
					defender.getName() + " no se intimidó gracias a " + defender.getAbilitySelected().getName());
			return;
		}

		// Mist blocks stat reduction
		if (battleCtx.isMistActive()) {
			System.out.println("La neblina protege a " + defender.getName() + " de Intimidación");
			return;
		}

		if (defender.getStage(StatType.ATTACK) <= -6) {
			System.out.println("El ataque de " + defender.getName() + " no puede bajar más");
			return;
		}

		defender.setStageValueStats(StatType.ATTACK, 1, true);

		System.out.println("El ataque de " + defender.getName() + " bajó");

		// 128_Defiant ability increases by 2 the attack for each stat reduced
		if (defender.hasDefiantAbility()) {
			if (defender.getStage(StatType.ATTACK) < 6) {
				defender.setStageValueStats(StatType.ATTACK, 2, false);
				System.out.println(defender.getName() + " aumentó mucho su ataque gracias a su habilidad Competitivo");
			}
		}
	}
}
