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
	// Allow or deny to attack depending on accuracy of the attack
	// -----------------------------
	public void resolveAccuracyAttack(AttackContext ctx) {
		Weather weather = ctx.getWeather();
		Pokemon attacker = ctx.getAttacker();
		Pokemon defender = ctx.getDefender();
		Attack attackAttacker = ctx.getAttack();
		Attack attackDefender = defender.getNextMovement();
		boolean isAttackerCharging = attacker.isChargingAttackForNextRound();
		boolean isDefenderCharging = defender.isChargingAttackForNextRound();

		boolean canHitInvulnerable = attackDefender == null
				|| attackAttacker.canHitWhileInvulnerable().contains(attackDefender.getId());

		// Reset allowing to attack if doesn't enter in any case (will be checked with
		// accuracy calculs)
		attacker.denyAttack();

		// 1 - Check if an attack is not disabled (attacks disabled cannot be used, even
		// for charged attacks, they are instantly disabled)
		if (attackIsDisabled(attacker, attackAttacker))
			return;

		// 2 - Resolve the attack if forces Pokemon change
		if (resolvedAttackForcesChange(attacker, defender, attackAttacker, canHitInvulnerable, isDefenderCharging))
			return;

		// 3 - Special cases that ignore precision
		if (resolvedAttackIgnoresPrecision(weather, attacker, defender, attackAttacker, canHitInvulnerable,
				isDefenderCharging))
			return;

		// 4 - Charged attack (first turn)
		if (resolvedChargingAttack(attacker, attackAttacker, isAttackerCharging))
			return;

		// 5 - Check for 99_No_Guard ability (always hits)
		if (PokemonHaveNoGuardAbility(attacker, defender))
			return;

		float accuracyFactor = calculateAccuracyFactor(ctx, attacker, defender, attackAttacker);

		resolveAccuracyByContext(attacker, defender, attackAttacker, accuracyFactor, canHitInvulnerable,
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
	// Check if last attack from Pokemon combating used is disabled
	// -----------------------------
	private boolean attackIsDisabled(Pokemon attacker, Attack attackUsed) {
		if (attacker.hasActiveEphemeralStatus(StatusConditions.DISABLE)) {
			State disableStatus = attacker.getEphemeralStatus(StatusConditions.DISABLE);

			if (disableStatus.getAttackDisabled().getId() == attackUsed.getId()) {
				System.out
						.println(attacker.getName() + " intentó usar " + attackUsed.getName() + ", pero está anulado!");
				return true;
			}
		}
		return false;
	}

	// -----------------------------
	// Check for attacks that force Pokemon change
	// -----------------------------
	private boolean resolvedAttackForcesChange(Pokemon attacker, Pokemon defender, Attack atkAttacker,
			boolean canHitInvulnerable, boolean isDefenderCharging) {
		// 18_Whirlwind / 46_Roar
		if (atkAttacker.forcesChange()) {
			if (isDefenderCharging && !canHitInvulnerable) {
				System.out.println(attacker.getName() + " usó " + atkAttacker.getName() + ", pero " + defender.getName()
						+ " evitó el ataque (invulnerable).");
			} else {
				// Attacks that force Pokemon change don't have precision, so they can be used
				// directly
				attacker.allowAttack();
				System.out.println(ANSI_PURPLE + "Los ataques de cambio nunca fallan" + ANSI_RESET);
			}
			return true;
		}
		return false;
	}

	// -----------------------------
	// Check for attacks that have 100% of precision
	// -----------------------------
	private boolean resolvedAttackIgnoresPrecision(Weather weather, Pokemon attacker, Pokemon defender,
			Attack atkAttacker, boolean canHitInvulnerable, boolean isDefenderCharging) {
		if (atkAttacker.alwaysHits()) {
			attacker.allowAttack();
			return true;
		}

		if (atkAttacker.getPrecision() == 0f) {
			attacker.allowAttack();
			System.out.println(ANSI_PURPLE
					+ "El ataque no tiene precisión, con lo cual se puede aplicar directamente (no es necesario verificar condiciones). "
					+ attacker.getName() + " usará " + atkAttacker.getName() + ANSI_RESET);
			return true;
		}

		if (atkAttacker.alwaysHeatsUnderWeather(weather)) {
			attacker.allowAttack();
			return true;
		}
		return false;
	}

	// -----------------------------
	// Check for charged attack
	// -----------------------------
	private boolean resolvedChargingAttack(Pokemon attacker, Attack atkAttacker, boolean isAttackerCharging) {
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
		modifyPrecisionByWeather(ctx);
		modifyPrecisionByAbility(ctx);

		if (atkAttacker.isOneHitKO())
			// don't take into account Pokemon levels (cause all are on the same lvl)
			accuracyFactor = 1f;
		// If attack can flinch Pokemon + (23_Stomp/ 27_Rolling kick/ 29_Headbutt/
		// 44_Bite) + defender
		// is minimized
		else if (atkAttacker.hasActiveSecondaryEffect(SecondaryEffectType.FLINCH) && defender.hasUsedMinimize())
			accuracyFactor = (ctx.getPrecision() / 100f)
					* (getEvasionOrAccuracy(ctx, attacker, 1, defenderHasUnaware) / 1f);
		// Other attacks
		else
			accuracyFactor = (ctx.getPrecision() / 100f) * (getEvasionOrAccuracy(ctx, attacker, 1, defenderHasUnaware)
					/ getEvasionOrAccuracy(ctx, defender, 2, attackerHasUnaware));

		return accuracyFactor;
	}

	// -----------------------------
	// Apply accuracy depending on different Pokemon situations during the attack
	// (normal attacks, charging attacks)
	// -----------------------------
	private void resolveAccuracyByContext(Pokemon attacker, Pokemon defender, Attack atkAttacker, float accuracyFactor,
			boolean canHitInvulnerable, boolean isAttackerCharging, boolean isDefenderCharging) {

		// BLOC 1 : NORMAL ATTACK - DEFENDER IS NOT CHARGING AN ATTACK
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
	// Calculates normal accuracy
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

			// Some attacks that can fail, hurt the attacker (Jump kick, etc.)
			if (atk.canRecieveDamage()) {
				float attackerInitialPs = attacker.getInitialPs();

				float recoil = attackerInitialPs / 2f;

				attacker.setPs(Math.max(attacker.getPs() - recoil, 0));

				System.out.println(attacker.getName()
						+ " se dañó a si mismo jajajaji. (Patada salto, Patada Salto Alta, Patada Hacha, Plancha Voltaica)");
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
			// Ensure we don't keep charging state
			attacker.setIsChargingAttackForNextRound(false);

			System.out.println(attacker.getName() + " usó " + atk.getName() + ". " + defender.getName() + " (Id:"
					+ defender.getId() + ")" + " evitó el ataque jijijija. " + code);
		}
	}

	// -----------------------------
	// Check if 99_No_Guard ability is in game
	// -----------------------------
	private boolean PokemonHaveNoGuardAbility(Pokemon attacker, Pokemon defender) {
		// 99_No_Guard allows to attack every time (whether is the defender or the
		// attacker that have the ability)
		if (attacker.hasNoGuardAbility() || defender.hasNoGuardAbility()) {
			System.out.println(ANSI_PURPLE + attacker.getName()
					+ " puede atacar gracias a la habilidad Indefenso en juego" + ANSI_RESET);
			attacker.allowAttack();
			return true;
		}
		return false;
	}

	// -----------------------------
	// Change attacks depending on weather
	// -----------------------------
	private void modifyPrecisionByWeather(AttackContext ctx) {
		if (ctx.getWeather() == Weather.SUN)
			if (ctx.getAttack().isThunder())
				ctx.setPrecision(50f);
	}

	// -----------------------------
	// Modify precision of attacks depending on abilities, etc.
	// -----------------------------
	private void modifyPrecisionByAbility(AttackContext ctx) {
		// ATTACKER
		// 14_Compound_Eyes ability rises precision by 30%
		if (ctx.getAttacker().hasCompoundEyesAbility()) {
			ctx.multiplyPrecision(1.3f);
			System.out
					.println(ANSI_PURPLE + ctx.getAttacker().getName() + " aumentó su precisión gracias a su habilidad "
							+ ctx.getAttacker().getAbilitySelected().getName() + ANSI_RESET);
		}

		// 55_Hustle ability reduces precision by 20%
		if (ctx.getAttack().getBases().contains("fisico") && ctx.getAttacker().hasHustleAbility()) {
			ctx.multiplyPrecision(0.8f);
			System.out
					.println(ANSI_PURPLE + ctx.getAttacker().getName() + " redujo su precisión a causa de su habilidad "
							+ ctx.getAttacker().getAbilitySelected().getName() + ANSI_RESET);
		}

		// DEFENDER
		// 77_Tangled_Feed duplicates evasion by 2 if confused
		if (ctx.getDefender().isTangledFeetActive()) {
			ctx.multiplyPrecision(0.5f);
			System.out.println(ANSI_PURPLE + ctx.getDefender().getName() + " aumentó su evasión gracias a su habilidad "
					+ ctx.getDefender().getAbilitySelected().getName() + ANSI_RESET);
		}

		// 81_Snow_Cloak sets 20% more of evasion if it's snowing
		if (ctx.getWeather() == Weather.HAIL && ctx.getDefender().hasSnowCloakAbility()) {
			ctx.multiplyPrecision(0.8f);
			System.out.println(ANSI_PURPLE + ctx.getDefender().getName() + " aumentó su evasión gracias a su habilidad "
					+ ctx.getDefender().getAbilitySelected().getName() + ANSI_RESET);
		}

		// 147_Wonder_skin ability reduces precision by 2 if attacks has secondary
		// effects to defender or support to attacker, etc. (only for attacks that are
		// state type against defender)
		if (ctx.getDefender().hasWonderSkinAbility() && ctx.getAttack().getBases().contains("otros")
				&& ctx.getAttack().getPrecision() > 50f && ctx.getAttack().isStateAttackAgainstPkFacing()) {
			ctx.multiplyPrecision(0.5f);
			System.out
					.println(ANSI_PURPLE + ctx.getAttacker().getName() + " redujo su precisión a causa de la habilidad "
							+ ctx.getDefender().getAbilitySelected().getName() + " del Pokémon rival" + ANSI_RESET);
		}
	}
}
