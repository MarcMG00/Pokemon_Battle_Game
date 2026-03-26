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

		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		int modifier = 1;

		// Growth special case
		if (ctx.weather == Weather.SUN && !ctx.isWeatherSuppressed)
			modifier = 2;

		for (Map.Entry<StatType, Integer> entry : statBoosts.entrySet()) {

			StatType stat = entry.getKey();
			int stages = entry.getValue() * modifier;

			if (ctx.attacker.getStage(stat) >= 6) {
				System.out.println(stat + " de " + ctx.attacker.getName() + " no puede subir más!");
				continue;
			}

			ctx.attacker.setStageValueStats(stat, stages, false);
			System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " aumentó su " + stat);
		}

		ctx.attack.setPp(ctx.attack.getPp() - 1);

		return result;
	}
}
