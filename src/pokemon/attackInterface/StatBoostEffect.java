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

		System.out.println(ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " usó "
				+ ctx.getAttack().getName());

		if (ctx.getAttacker().getAttackStage() >= 6) {
			System.out.println(stat.name() + " de " + ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId()
					+ ")" + " no puede subir más!");
			return result;
		}

		System.out.println(
				ctx.getAttacker().getName() + " (Id:" + ctx.getAttacker().getId() + ")" + " aumentó su " + stat.name());
		ctx.getAttacker().setStageValueStats(stat, stages, false);
		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		return result;
	}
}
