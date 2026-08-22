package pokemon.attackInterface;

import pokemon.enums.StatType;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class StatReduceEffect implements AttackEffect {
	private final StatType stat;
	private int stages;

	public StatReduceEffect(StatType stat, int stages) {
		this.stat = stat;
		this.stages = stages;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		Pokemon target = ctx.getDefender();

		boolean isReduceStatStage = true;

		// 126_Contrary ability reverse the increase or reduce stat stage
		if (target.hasContraryAbility())
			isReduceStatStage = false;

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		// Mist
		if (ctx.isMistActive()) {
			System.out.println("No se pueden bajar las estadísticas debido a Neblina");
			return result;
		}

		// 29_Clear_Body / 73_White_Smoke abilities cannot be reduced stats
		if (target.hasClearBodyAbility() || target.hasWhiteSmokeAbility()) {
			System.out.println(
					"Las estadísticas no pueden bajar por la habilidad " + target.getAbilitySelected().getName());
			return result;
		}

		// ATTACK
		// 52_Hyper_Cutter cannot reduce attack
		if (target.hasHyperCutterAbility() && stat == StatType.ATTACK) {
			System.out.println("El ataque no puede bajar por la habilidad " + target.getAbilitySelected().getName());
			return result;
		}

		// DEFENSE
		// 145_Big_Pecks cannot reduce Defense
		if (target.hasBigPecksAbility() && stat == StatType.DEFENSE) {
			System.out.println("La defensa no puede bajar por la habilidad " + target.getAbilitySelected().getName());
			return result;
		}

		// PRECISION
		// 35_Illuminate and 51_Keen_eye cannot be reduced precision
		if (stat == StatType.PRECISION && (target.hasIlluminateAbility() || target.hasKeenEyeAbility())) {
			System.out.println("La precisión no puede bajar por la habilidad " + target.getAbilitySelected().getName());
			return result;
		}

		if (target.getStage(stat) <= -6) {
			System.out.println(stat + " de " + target.getName() + " no puede bajar más!");
			return result;
		}

		stages *= ctx.getStatService().applyModifiersNbStage(target, isReduceStatStage);
		target.setStageValueStats(stat, stages, isReduceStatStage);

		System.out.println(
				isReduceStatStage ? target.getName() + " bajó su " + stat : target.getName() + " aumentó su " + stat);

		// 128_Defiant ability increases by 2 the attack for each stat reduced
		if (isReduceStatStage && target.hasDefiantAbility()) {
			if (target.getStage(StatType.ATTACK) < 6) {
				target.setStageValueStats(StatType.ATTACK, 2, false);
				System.out.println(target.getName() + " aumentó mucho su ataque gracias a su habilidad Competitivo");
			}
		}

		return result;
	}
}
