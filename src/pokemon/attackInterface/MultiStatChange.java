package pokemon.attackInterface;

import java.util.Map;

import pokemon.enums.StatType;
import pokemon.enums.Weather;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class MultiStatChange implements AttackEffect {
	private final Map<StatType, Integer> statBoosts;

	public MultiStatChange(Map<StatType, Integer> statBoosts) {
		this.statBoosts = statBoosts;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		int modifier = 1;

		boolean isReduceStatStage = false;

		// 126_Contrary ability reverse the increase or reduce stat stage
		if (ctx.getAttacker().hasContraryAbility())
			isReduceStatStage = true;

		// Growth special case
		if (ctx.getWeather() == Weather.SUN && !ctx.isWeatherSuppressed())
			modifier = 2;

		for (Map.Entry<StatType, Integer> entry : statBoosts.entrySet()) {
			StatType stat = entry.getKey();
			int stages = entry.getValue() * modifier;

			if (ctx.getAttacker().getStage(stat) >= 6) {
				System.out.println(stat + " de " + ctx.getAttacker().getName() + " no puede subir más!");
				continue;
			}

			stages *= ctx.getStatService().applyModifiersNbStage(ctx.getAttacker(), isReduceStatStage);
			ctx.getAttacker().setStageValueStats(stat, stages, isReduceStatStage);
			System.out.println(isReduceStatStage
					? ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " bajó su " + stat
					: ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " aumentó su " + stat);
		}

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		return result;
	}
}
