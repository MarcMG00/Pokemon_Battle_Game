package pokemon.attackInterface;

import pokemon.enums.Weather;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;

public class SolarBeamEffect extends ChargeAttackEffect {

	public SolarBeamEffect(DamageService damageService) {
		super(damageService);
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		AttackResult result = new AttackResult();

		// No charge if sun
		if (ctx.weather == Weather.SUN && !ctx.isWeatherSuppressed) {
			System.out.println(
					ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

			float dmg = damageService.doDammage(ctx);

			ctx.attacker.setIsChargingAttackForNextRound(false);
			ctx.attack.setPp(ctx.attack.getPp() - 1);

			ctx.defender.setPs(ctx.defender.getPs() - dmg);

			result.addDamage(dmg);
			return result;
		}

		// First turn charge
		if (!ctx.attacker.getIsChargingAttackForNextRound()) {

			System.out.println(ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " se prepara para "
					+ ctx.attack.getName());

			ctx.attacker.setIsChargingAttackForNextRound(true);
			return result;
		}

		// Second turn attack
		System.out.println(
				ctx.attacker.getName() + " (Id:" + ctx.attacker.getId() + ")" + " usó " + ctx.attack.getName());

		// Weather reduces power
		if (!ctx.isWeatherSuppressed
				&& (ctx.weather == Weather.RAIN || ctx.weather == Weather.HAIL || ctx.weather == Weather.SANDSTORM)) {

			ctx.attack.setPower(ctx.attack.getPower() / 2);
		}

		float dmg = damageService.doDammage(ctx);

		ctx.attacker.setIsChargingAttackForNextRound(false);
		ctx.attack.setPp(ctx.attack.getPp() - 1);

		ctx.defender.setPs(ctx.defender.getPs() - dmg);

		result.addDamage(dmg);
		return result;
	}

}
