package pokemon.model;

import pokemon.enums.AttackCategory;
import pokemon.enums.SecondaryEffectType;
import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;

public class AccuracyService {

	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	private static final String ANSI_RESET = "\u001B[0m";

	private final StatService statService;

	public AccuracyService() {
		this.statService = new StatService();
	}

	// -----------------------------
	// Gets the probability of attacking
	// -----------------------------
	public void resolveAttack(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();
		Pokemon defender = ctx.getDefender();
		Attack atkAttacker = ctx.getAttack();
		Attack atkDefender = defender.getNextMovement();
		boolean isAttackerCharging = attacker.getIsChargingAttackForNextRound();
		boolean isDefenderCharging = defender.getIsChargingAttackForNextRound();

		boolean canHitInvulnerable = atkDefender == null
				|| atkAttacker.getCanHitWhileInvulnerable().contains(atkDefender.getId());

		// 1 - Check if an attack is not disabled (attacks disabled cannot be used, even
		// for charged attacks, they are instantly disabled)
		if (!canUseAttack(ctx, attacker, atkAttacker)) {
			return;
		}

		// 2 - Special cases that ignore precision
		if (handleForcedChangeOrIgnorePrecision(ctx, attacker, defender, atkAttacker, canHitInvulnerable,
				isDefenderCharging)) {
			return;
		}

		// 3 - Charged attack (first turn)
		if (handleChargeStart(attacker, atkAttacker, isAttackerCharging)) {
			return;
		}

		// 4 - Check for 99_Magic_Guard ability (always hits)
		if (ApplyNoGuardAbility(attacker, defender))
			return;

		// Reset CanAttack if doesn't enter in any case
		attacker.denyAttack();

		float accuracyFactor = calculateAccuracyFactor(ctx, attacker, defender, atkAttacker);

		resolveAccuracyByContext(attacker, defender, atkAttacker, accuracyFactor, canHitInvulnerable,
				isAttackerCharging, isDefenderCharging);
	}

	// -----------------------------
	// Knows the evasion or accuracy for the Pokemon selected (1 is for accuracy, 2
	// is for evasion)
	// -----------------------------
	public float getEvasionOrAccuracy(AttackContext ctx, Pokemon pk, int t, boolean ignoreStage) {
		int evAcu = 0;
		float resultEvAcu = 1f;

		switch (t) {
		case 1:
			evAcu = statService.getEffectivePrecision(pk, ignoreStage);
			break;
		case 2:
			evAcu = statService.getEffectiveEvasion(pk, ignoreStage);
			break;
		}

		switch (evAcu) {
		case -6:
			resultEvAcu = 3f / 9f;
			break;
		case -5:
			resultEvAcu = 3f / 8f;
			break;
		case -4:
			resultEvAcu = 3f / 7f;
			break;
		case -3:
			resultEvAcu = 3f / 6f;
			break;
		case -2:
			resultEvAcu = 3f / 5f;
			break;
		case -1:
			resultEvAcu = 3f / 4f;
			break;
		case 1:
			resultEvAcu = 4f / 3f;
			break;
		case 2:
			resultEvAcu = 5f / 3f;
			break;
		case 3:
			resultEvAcu = 6f / 3f;
			break;
		case 4:
			resultEvAcu = 7f / 3f;
			break;
		case 5:
			resultEvAcu = 8f / 3f;
			break;
		case 6:
			resultEvAcu = 9f / 3f;
			break;
		}

		return resultEvAcu;
	}

	// -----------------------------
	// Check if attack is not disabled
	// -----------------------------
	private boolean canUseAttack(AttackContext ctx, Pokemon attacker, Attack atkAttacker) {
		if (isAttackDisabled(ctx)) {
			System.out.println(
					attacker.getName() + " intentó usar " + ctx.getAttack().getName() + ", pero está anulado!");
			attacker.denyAttack();
			return false;
		}
		return true;
	}

	// -----------------------------
	// Check for attacks that force changes or has 100% of precision
	// -----------------------------
	private boolean handleForcedChangeOrIgnorePrecision(AttackContext ctx, Pokemon attacker, Pokemon defender,
			Attack atkAttacker, boolean canHitInvulnerable, boolean isDefenderCharging) {
		// 18_Whirlwind / 46_Roar / 54_Mist
		if (atkAttacker.isForceChange() || atkAttacker.getId() == 54) {
			if (isDefenderCharging && !canHitInvulnerable) {
				attacker.denyAttack();
				System.out.println(attacker.getName() + " usó " + atkAttacker.getName() + ", pero " + defender.getName()
						+ " evitó el ataque (invulnerable).");
			} else
				attacker.allowAttack();
			return true;
		}

		if (atkAttacker.alwaysHits()) {
			attacker.allowAttack();
			return true;
		}

		if (atkAttacker.alwaysHeatsUnderWeather(ctx.getWeather())) {
			attacker.allowAttack();
			return true;
		}
		return false;
	}

	// -----------------------------
	// Check for charged attack
	// -----------------------------
	private boolean handleChargeStart(Pokemon attacker, Attack atkAttacker, boolean isAttackerCharging) {
		if (atkAttacker.getCategory() == AttackCategory.CHARGED && !isAttackerCharging) {
			System.out.println(ANSI_PURPLE + "Probability - Starting a charged attack" + ANSI_RESET);

			attacker.allowAttack();
			System.out.println(ANSI_PURPLE + attacker.getName() + " utilizará " + atkAttacker.getName()
					+ " - comienza a cargar el ataque. (bloc 3)" + ANSI_RESET);
			return true;
		}
		return false;
	}

	// -----------------------------
	// Calculates accuracy (check if doesn't fails)
	// -----------------------------
	private float calculateAccuracyFactor(AttackContext ctx, Pokemon attacker, Pokemon defender, Attack atkAttacker) {
		float accuracyFactor = 0f;
		// 109_Unaware ability => ignores stages from stats (but no modifiers on
		// abilities)
		boolean attackerHasUnaware = attacker.hasUnawareAbility();
		boolean defenderHasUnaware = defender.hasUnawareAbility();

		// Methods to modify precision of attack, evasion, etc.
		checkWeatherEffectsForAttacks(ctx);
		checkStatsForAttacks(ctx);

		if (atkAttacker.isOneHitKO())
			// don't take into account Pokemon levels (cause all are on the same lvl)
			accuracyFactor = 1f;
		// Retreat Pokemon (23_Stomp/ 27_Rolling kick/ 29_Headbutt/ 44_Bite) + defender
		// is minimized
		else if (atkAttacker.hasActiveSecondaryEffect(SecondaryEffectType.FLINCH) && defender.getHasUsedMinimize())
			accuracyFactor = (ctx.getPrecision() / 100f)
					* (getEvasionOrAccuracy(ctx, attacker, 1, defenderHasUnaware) / 1f);
		// Other attacks
		else
			accuracyFactor = (ctx.getPrecision() / 100f) * (getEvasionOrAccuracy(ctx, attacker, 1, defenderHasUnaware)
					/ getEvasionOrAccuracy(ctx, defender, 2, attackerHasUnaware));

		return accuracyFactor;
	}

	// -----------------------------
	// Apply accuracy depending on different cases
	// -----------------------------
	private void resolveAccuracyByContext(Pokemon attacker, Pokemon defender, Attack atkAttacker, float accuracyFactor,
			boolean canHitInvulnerable, boolean isAttackerCharging, boolean isDefenderCharging) {

		// BLOC 1 : NORMAL ATTACK
		if (atkAttacker.getCategory() == AttackCategory.NORMAL && !isDefenderCharging) {
			handleNormalAccuracyCheck(accuracyFactor, atkAttacker, attacker, defender, "(bloc 1)");
			return;
		}

		// BLOC 2 : NORMAL ATTACK - CAN HIT INVULNERABLE POKEMON
		if (canHitInvulnerable && isDefenderCharging) {
			handleNormalAccuracyCheck(accuracyFactor, atkAttacker, attacker, defender, "(bloc 2)");
			return;
		}

		// BLOC 3 : SECOND TURN (FROM CHARGING ATTACK) - DEFENDER NOT CHARGING
		if (isAttackerCharging && !isDefenderCharging) {
			handleChargedAttackExecution(accuracyFactor, atkAttacker, attacker, defender, "(bloc 3)");
			return;
		}

		// BLOC 4 : SECOND TURN (FROM CHARGING ATTACK) - DEFENDER IS CHARGING
		if (isAttackerCharging && isDefenderCharging) {
			attacker.setIsChargingAttackForNextRound(false);
			return;
		}

		// BLOC 5 : OTHER ATTACK AGAINST INVULNERABLE (CANNOT DO DAMMAGE)
		if (!canHitInvulnerable && isDefenderCharging) {
			attacker.setIsChargingAttackForNextRound(false);
			System.out.println(attacker.getName() + " usó " + atkAttacker.getName() + ", pero " + defender.getName()
					+ " evitó el ataque (invulnerable). jijijija");
			return;
		}
	}

	// -----------------------------
	// Handle normal accuracy
	// -----------------------------
	private void handleNormalAccuracyCheck(float accuracyFactor, Attack atk, Pokemon attacker, Pokemon defender,
			String code) {
		if (accuracyFactor >= 1f) {
			attacker.allowAttack();
			return;
		}

		int rand = (int) (Math.random() * 100);

		if (rand / 100f <= accuracyFactor)
			attacker.allowAttack();
		else {
			System.out.println("accuracy : " + rand / 100f + "(random) => " + accuracyFactor + " (true accuracy)");
			atk.setPp(atk.getPp() - 1);
			attacker.denyAttack();

			System.out.println(attacker.getName() + " usó " + atk.getName() + ". " + defender.getName()
					+ " evitó el ataque jijijija. " + code);

			// Some attacks that can fail, hurts the attacker (Jump kick, etc.)
			if (atk.getCanRecieveDamage()) {
				float attackerInitialPs = attacker.getInitialPs();

				float recoil = attackerInitialPs / 2f;

				attacker.setPs(attacker.getPs() - recoil);

				System.out.println(attacker.getName()
						+ " se dañó a si mismo jajajaji. (Patada salto, Patada Salto Alta,  Patada Hacha, Plancha Voltaica)");
			}
		}
	}

	// -----------------------------
	// Handle second turn of a charged attack
	// -----------------------------
	private void handleChargedAttackExecution(float accuracyFactor, Attack atk, Pokemon attacker, Pokemon defender,
			String code) {
		if (accuracyFactor >= 1f) {
			attacker.allowAttack();
			return;
		}

		int rand = (int) (Math.random() * 100);

		if (rand / 100f <= accuracyFactor)
			attacker.allowAttack();
		else {
			atk.setPp(atk.getPp() - 1);
			attacker.denyAttack();
			attacker.setIsChargingAttackForNextRound(false);

			System.out.println(attacker.getName() + " usó " + atk.getName() + ". " + defender.getName() + " (Id:"
					+ defender.getId() + ")" + " evitó el ataque jijijija. " + code);
		}
	}

	// -----------------------------
	// Check if 99_No_Guard ability is in game
	// -----------------------------
	private boolean ApplyNoGuardAbility(Pokemon attacker, Pokemon defender) {
		// 99_No_Guard allows to attack every time (whether is the defender or the
		// attacker that has the ability)
		if (attacker.hasNoGuardAbility() || defender.hasNoGuardAbility()) {
			System.out.println(attacker.getName() + " puede atacar gracias a la habilidad Indefenso en juego");
			attacker.allowAttack();
			return true;
		}
		return false;
	}

	// -----------------------------
	// Gets if last attack from Pokemon combating used is disabled
	// -----------------------------
	public boolean isAttackDisabled(AttackContext ctx) {
		Pokemon attacker = ctx.getAttacker();
		if (attacker.hasActiveStatusCondition(StatusConditions.DISABLE)) {
			State disableStatus = attacker.getStatusCondition();

			if (disableStatus.getAttackDisabled() == ctx.getAttack())
				return true;
		}
		return false;
	}

	// -----------------------------
	// Change attacks depending on weather
	// -----------------------------
	private void checkWeatherEffectsForAttacks(AttackContext ctx) {
		if (ctx.getWeather() == Weather.SUN)
			if (ctx.getAttack().getId() == 87)
				ctx.setPrecision(50f);
	}

	// -----------------------------
	// Change attacks depending on abilities, etc.
	// -----------------------------
	private void checkStatsForAttacks(AttackContext ctx) {
		// ATTACKER
		// 14_Compound_Eyes ability rises precision by 30%
		if (ctx.getAttacker().hasCompoundEyesAbility())
			ctx.setPrecision(ctx.getPrecision() * 1.3f);

		// 55_Hustle ability reduces precision by 20%
		if (ctx.getAttack().getBases().contains("fisico") && ctx.getAttacker().hasHustleAbility())
			ctx.setPrecision(ctx.getPrecision() * 0.8f);

		// DEFENDER
		// 77_Tangled_Feed duplicates evasion by 2 if confused
		if (ctx.getDefender().isTagledFeetActive()) {
			ctx.setPrecision(ctx.getPrecision() / 2f);
			System.out.println(ctx.getDefender().getName() + " aumentó su evasión gracias a su habilidad "
					+ ctx.getDefender().getAbilitySelected().getName());
		}

		// 81_Snow_Cloak sets 20% more of evasion if it's snowing
		if (ctx.getWeather() == Weather.HAIL && ctx.getDefender().hasSnowCloakAbility()) {
			ctx.setPrecision(ctx.getPrecision() * 0.8f);
			System.out.println(ctx.getDefender().getName() + " aumentó su evasión gracias a su habilidad "
					+ ctx.getDefender().getAbilitySelected().getName());
		}
	}
}
