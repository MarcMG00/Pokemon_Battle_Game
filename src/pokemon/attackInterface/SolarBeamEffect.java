package pokemon.attackInterface;

import pokemon.enums.Weather;
import pokemon.model.AttackContext;
import pokemon.model.AttackResult;
import pokemon.model.DamageService;
import pokemon.model.Pokemon;

public class SolarBeamEffect extends ChargeAttackEffect {

	public SolarBeamEffect(DamageService damageService) {
		super(damageService);
	}

	@Override
	public AttackResult execute(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();
		AttackResult result = new AttackResult();

		// No charge if sun
		if (ctx.getWeather() == Weather.SUN && !ctx.isWeatherSuppressed()) {
			System.out.println(
					attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.getAttack().getName());

			result = damageService.doDamage(ctx);
			float dmg = result.getDamage();

			// Ensure we don't keep charging state if we were prevented from attacking
			attacker.setIsChargingAttackForNextRound(false);
			ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

			ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);

			ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

			return result;
		}

		// First turn charge
		if (!attacker.isChargingAttackForNextRound()) {
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " se prepara para "
					+ ctx.getAttack().getName());

			attacker.setIsChargingAttackForNextRound(true);
			return result;
		}

		// Second turn attack
		System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + ctx.getAttack().getName());

		// Weather reduces power
		if (!ctx.isWeatherSuppressed() && (ctx.getWeather() == Weather.RAIN || ctx.getWeather() == Weather.HAIL
				|| ctx.getWeather() == Weather.SANDSTORM))
			ctx.setPower(ctx.getAttack().getPower() / 2);

		result = damageService.doDamage(ctx);
		float dmg = result.getDamage();

		// Ensure we don't keep charging state
		attacker.setIsChargingAttackForNextRound(false);
		ctx.getAttack().setPp(ctx.getAttack().getPp() - 1);

		ctx.getDefender().setPs(ctx.getDefender().getPs() - dmg);

		ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);

		return result;
	}

}
