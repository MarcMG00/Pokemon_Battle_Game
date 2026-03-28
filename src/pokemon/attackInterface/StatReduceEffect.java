package pokemon.attackInterface;

import pokemon.enums.StatType;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.Pokemon;

public class StatReduceEffect implements AttackEffect {
	private final StatType stat;
	private final int stages;

	public StatReduceEffect(StatType stat, int stages) {
		this.stat = stat;
		this.stages = stages;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		Pokemon target = ctx.getDefender();

		System.out.println(
				ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó " + ctx.getAttack().getName());

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		// Mist
		if (ctx.isMistActive()) {
			System.out.println("No se pueden bajar estadísticas debido a Neblina");
			return result;
		}

		// 29_Clear_Body
		if (target.getAbilitySelected().getId() == 29) {
			System.out.println(
					"Las estadísticas no pueden bajar por la habilidad " + target.getAbilitySelected().getName());
			return result;
		}

		// Precision
		if (stat == StatType.PRECISION
				&& (target.getAbilitySelected().getId() == 35 || target.getAbilitySelected().getId() == 51)) {
			System.out.println("La precisión no puede bajar por la habilidad " + target.getAbilitySelected().getName());
			return result;
		}

		if (target.getStage(stat) <= -6) {
			System.out.println(stat + " de " + target.getName() + " no puede bajar más");
			return result;
		}

		System.out.println(target.getName() + " bajó su " + stat);
		target.setStageValueStats(stat, stages, true);

		return result;
	}
}
