package pokemon.attackInterface;

import pokemon.enums.StatType;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public class StatBoostEffect implements AttackEffect {
	private StatType stat;
	private int stages;

	public StatBoostEffect(StatType stat, int stages) {
		this.stat = stat;
		this.stages = stages;
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();
		
		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		if (ctx.attacker.getAttackStage() >= 6) {
			System.out.println(stat.name() + " de " + ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")"
					+ " no puede subir más!");
			return result;
		}

		System.out
				.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " aumentó su " + stat.name());
		ctx.attacker.setStageValueStats(stat, stages, false);
		ctx.attack.setPp(ctx.attack.getPp() - 1);
		
		return result;
	}
}
