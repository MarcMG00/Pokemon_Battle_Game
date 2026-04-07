package pokemon.attackInterface;

import java.util.Map;

import pokemon.enums.StatType;
import pokemon.enums.Weather;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class MultiStatBoostEffect implements AttackEffect {
	private final Map<StatType, Integer> statBoosts;

	public MultiStatBoostEffect(Map<StatType, Integer> statBoosts) {
		this.statBoosts = statBoosts;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		int modifier = 1;

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

			ctx.getAttacker().setStageValueStats(stat, stages, false);
			System.out.println(
					ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " aumentó su " + stat);
		}

		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		return result;
	}
}
