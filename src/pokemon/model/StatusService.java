package pokemon.model;

import pokemon.enums.AttackCategory;
import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;

public class StatusService {
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_RESET = "\u001B[0m";

	// -----------------------------
	// Evaluate Pokemon in combat if need to apply status condition before attacking
	// -----------------------------
	public void evaluateStartTurnStatuses(Pokemon playerPk, Pokemon iaPk) {
		if (shouldEvaluateStatus(playerPk))
			evaluateStatusStartOfTurn(playerPk);

		if (shouldEvaluateStatus(iaPk))
			evaluateStatusStartOfTurn(iaPk);
	}

	// -----------------------------
	// Get conditions to apply conditions from status condition on Pokemon
	// -----------------------------
	private boolean shouldEvaluateStatus(Pokemon pk) {
		boolean secondTurnCharged = pk.getNextMovement() != null
				&& pk.getNextMovement().getCategory() == AttackCategory.CHARGED && pk.isChargingAttackForNextRound();

		boolean normalAttack = pk.getNextMovement() != null
				&& pk.getNextMovement().getCategory() != AttackCategory.CHARGED;

		return (secondTurnCharged || normalAttack) && pk.canDonAnythingNextRound();
	}

	// -----------------------------
	// Some states decrease their turn at the beginning of the turn and apply
	// effects for example when frozen, it can remove the status before attacking
	// -----------------------------
	public void evaluateStatusStartOfTurn(Pokemon pk) {
		doFrozenEffectStartTurn(pk);
	}

	// -----------------------------
	// Do effect from FROZEN state (start of the turn before checking if can attack)
	// -----------------------------
	private void doFrozenEffectStartTurn(Pokemon pk) {
		if (pk.hasActiveStatusCondition(StatusConditions.FROZEN)) {
			State frozenStatus = pk.getStatusCondition();

			int getRidOfStatusProbability = (int) (Math.random() * 100);

			// Only can be thawed if probability <= 10% (at the beginning) => after each
			// turn, it goes to +10%
			if (getRidOfStatusProbability <= frozenStatus.getPercentToBeDefrosted()) {
				pk.setStatusCondition(new State());
				System.out.println(ANSI_CYAN + pk.getName() + " se descongeló! (probabilidad inferior a "
						+ frozenStatus.getPercentToBeDefrosted() + ") : " + getRidOfStatusProbability + ANSI_RESET);
			} else {
				// Adds +10% each turn not thawed
				frozenStatus.setPercentToBeDefrosted(frozenStatus.getPercentToBeDefrosted() + 10);

				System.out.println(ANSI_CYAN + pk.getName() + " => congelado - no puede atacar" + ANSI_RESET);
			}
		}
	}

	// -----------------------------
	// Evaluate states BEFORE attacking. Some states influence the probability of
	// attacking, for example when confused, paralyzed, etc.
	// -----------------------------
	public void canAttackEvaluatingAllStatesToAttack(Pokemon pk) {
		boolean canAttackFrozen = canAttackFrozenStartTurn(pk);
		boolean canAttackParalyzed = canAttackParalyzedStartTurn(pk);
		boolean canAttackConfused = canAttackConfusedStartTurn(pk);
		boolean canAttackAsleep = canAttackAsleepStartTurn(pk);

		boolean canAttack = canAttackFrozen && canAttackParalyzed && canAttackConfused && canAttackAsleep;
		pk.setCanAttack(canAttack);
	}

	// -----------------------------
	// Gets if Pokemon can attack because of FROZEN state (check start of the turn
	// after applying effect of Frozen)
	// -----------------------------
	private boolean canAttackFrozenStartTurn(Pokemon pk) {
		boolean canAttack = true;

		if (pk.hasActiveStatusCondition(StatusConditions.FROZEN))
			canAttack = false;

		return canAttack;
	}

	// -----------------------------
	// Check can move from PARALYZED state (only before attacking)
	// -----------------------------
	private boolean canAttackParalyzedStartTurn(Pokemon pk) {
		boolean canAttack = true;

		if (pk.hasActiveStatusCondition(StatusConditions.PARALYZED)) {
			int attackProbability = (int) (Math.random() * 100);

			if (attackProbability > 25) {
				canAttack = false;
				System.out.println(ANSI_CYAN + pk.getName() + " => paralizado - no puede atacar" + ANSI_RESET);
			} else {
				System.out.println(ANSI_CYAN + pk.getName() + " => paralizado - puede atacar" + ANSI_RESET);
			}
		}

		return canAttack;
	}

	// -----------------------------
	// Gets if Pokemon can attack because of CONFUSION state (check start of the
	// turn)
	// -----------------------------
	private boolean canAttackConfusedStartTurn(Pokemon pk) {
		boolean canAttackConfused = true;

		if (pk.hasActiveEphemeralStatus(StatusConditions.CONFUSED)) {
			State confusedStatus = pk.getEphemeralStatus(StatusConditions.CONFUSED);
			confusedStatus.setNbTurns(confusedStatus.getNbTurns() - 1);

			if (confusedStatus.getNbTurns() <= 0) {
				pk.removeEphemeralStatus(StatusConditions.CONFUSED);
				System.out.println(pk.getName() + " ya no está confuso!");
			} else {
				// 50% of probabilities to attack
				boolean hurtsItself = Math.random() < 0.50;

				if (hurtsItself) {
					System.out.println(pk.getName() + " está confuso...");
					System.out.println(pk.getName() + " está tan confuso que se hace dañó a sí mismo!");

					// Standard damage with a power of 40 points
					float damage = doConfusedDammageStartTurn(pk);
					pk.setPs(Math.max(pk.getPs() - damage, 0));

					if (pk.hasFainted()) {
						pk.setStatusCondition(new State(StatusConditions.DEBILITATED));
						System.out.println(pk.getName() + " quedó debilitado por la confusión!");
					}

					canAttackConfused = false; // received damage or dies => cannot continue
				} else {
					System.out.println(pk.getName() + " está confuso... (pero podrá atacar)");
				}
			}
		}
		return canAttackConfused;
	}

	// -----------------------------
	// Apply confusion damage (only start of the turn)
	// -----------------------------
	private float doConfusedDammageStartTurn(Pokemon pk) {
		// There is a random variation when attacking (the total damage is not the same
		// every time)
		int randomVariation = (int) ((Math.random() * (100 - 85)) + 85);

		float dmg = 0;

		// Apply damage
		dmg = ((((40f + 2f) * (40f * (pk.getAttack() / pk.getDef()))) / 50f) + 2f) * (randomVariation / 100f);

		return dmg;
	}

	// -----------------------------
	// Do effect from ASLEEP state (start of the turn)
	// -----------------------------
	private boolean canAttackAsleepStartTurn(Pokemon pk) {
		boolean canAttack = true;

		if (pk.hasActiveEphemeralStatus(StatusConditions.ASLEEP)) {
			State asleepStatus = pk.getEphemeralStatus(StatusConditions.ASLEEP);
			asleepStatus.setNbTurns(asleepStatus.getNbTurns() - 1);

			if (asleepStatus.getNbTurns() <= 0) {
				pk.removeEphemeralStatus(StatusConditions.ASLEEP);
				System.out.println(pk.getName() + " se despertó!");
			} else {
				// 1/nbTurns probabilities to wake up
				double wakeUpProbability = Math.random();

				if (wakeUpProbability <= 1 / asleepStatus.getNbTurns()) {
					pk.removeEphemeralStatus(StatusConditions.ASLEEP);
					System.out.println(pk.getName() + " se despertó!");
				} else {
					System.out.println(pk.getName() + " está dormido y no puede atacar");
					canAttack = false;
				}
			}
		}
		return canAttack;
	}

	// -----------------------------
	// Do status conditions end of turn effects
	// -----------------------------
	public void applyTurnStatusReductions(BattleContext battleCtx) {
		handleStatusConditionsEndTurn(battleCtx.getPkPlayer(), battleCtx.getPkIA());
		handleStatusConditionsEndTurn(battleCtx.getPkIA(), battleCtx.getPkPlayer());
	}

	// -----------------------------
	// Reduce number of turns remaining on status conditions/ ephemeral statuses
	// (end of the turn) => only apply statuses that do damage to attacker. Check if
	// Pokemon has fainted (and so avoid hurting rival Pokemon with next conditions)
	// -----------------------------
	public void handleStatusConditionsEndTurn(Pokemon pkAttacker, Pokemon pkDefender) {
		// Some status conditions
		doBurnedEffectEndTurn(pkAttacker);
		doPoisonedEffectEndTurn(pkAttacker);
		doBadlyPoisonedEffectEndTurn(pkAttacker);
		doAsleepEffectEndTurn(pkAttacker, pkDefender);

		// Some ephemeral statuses
		doTrappedEffect(pkAttacker);
		putConfusedStateIfNeeded(pkAttacker);
		reduceDisabledAttackTurn(pkAttacker);
	}

	// -----------------------------
	// Do effect from BURNED state (end of the turn)
	// -----------------------------
	private void doBurnedEffectEndTurn(Pokemon pk) {
		// 98_Magic_Guard annuls secondary damage effects
		if (pk.hasMagicGuardAbility())
			return;

		if (pk.hasActiveStatusCondition(StatusConditions.BURNED)) {
			// Reduces current PS by 6.25%
			float reducePs = pk.getInitialPs() * 0.0625f;

			// 85_Heatproof ability reduces to half the burned effect
			if (pk.hasHeatProofAbility())
				reducePs /= 2;

			pk.setPs(Math.max(pk.getPs() - reducePs, 0));

			System.out.println(pk.getName() + " se resiente de la quemadura XD - PS actuales : " + pk.getPs());

			if (pk.hasFainted())
				pk.setStatusCondition(new State(StatusConditions.DEBILITATED));
		}
	}

	// -----------------------------
	// Do effect from POISONED state (end of the turn)
	// -----------------------------
	private void doPoisonedEffectEndTurn(Pokemon pk) {
		// 98_Magic_Guard annuls secondary damage effects
		if (pk.hasMagicGuardAbility())
			return;

		if (pk.hasActiveStatusCondition(StatusConditions.POISONED)) {
			// 90_Poison_Heal ability heals 12,5% of initial PS
			if (pk.hasPoisonHealAbility()) {
				float healsPs = pk.getInitialPs() * 0.125f;
				pk.setPs(Math.min(pk.getPs() + healsPs, pk.getInitialPs()));

				System.out.println(pk.getName() + " está envenenado, pero recuperó PS gracias a su habilidad "
						+ pk.getAbilitySelected().getName());
			} else {
				// Reduces current PS by 6.25%
				float reducePs = pk.getInitialPs() * 0.0625f;
				pk.setPs(Math.max(pk.getPs() - reducePs, 0));

				System.out.println(pk.getName() + " está envenenado - PS actuales : " + pk.getPs());

				if (pk.hasFainted())
					pk.setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
		}
	}

	// -----------------------------
	// Do effect from BADLY_POISONED state (end of the turn)
	// -----------------------------
	private void doBadlyPoisonedEffectEndTurn(Pokemon pk) {
		State state = pk.getStatusCondition();
		int toxicCounter = state.getToxicCounter();

		if (pk.hasActiveStatusCondition(StatusConditions.BADLY_POISONED)) {
			// 98_Magic_Guard annuls secondary damage effects
			if (!pk.hasMagicGuardAbility()) {
				float damage = Math.max(1, pk.getInitialPs() * toxicCounter / 16);

				pk.setPs(Math.max(0, pk.getPs() - damage));

				System.out.println(pk.getName() + " está gravemente envenenado - PS actuales : " + pk.getPs());
			}
		}

		state.incrementToxicCounter();

		if (pk.hasFainted())
			pk.setStatusCondition(new State(StatusConditions.DEBILITATED));
	}

	// -----------------------------
	// Do effect from ASLEEP state (end of the turn)
	// -----------------------------
	public void doAsleepEffectEndTurn(Pokemon attacker, Pokemon defender) {
		applyBadDreamsAbility(attacker, defender);
	}

	// -----------------------------
	// Apply Bad Dreams ability if needed (end of the turn)
	// -----------------------------
	private void applyBadDreamsAbility(Pokemon attacker, Pokemon defender) {
		// 123_Bad_dreams ability => needs opponent to be asleep
		if (attacker.hasBadDreamsAbility() && defender.hasActiveEphemeralStatus(StatusConditions.ASLEEP)) {
			// Reduces current PS by 1/8 from max PS
			float reducePs = defender.getInitialPs() * 0.125f;
			defender.setPs(Math.max(defender.getPs() - reducePs, 0));

			System.out.println(
					defender.getName() + " sufre daño a causa de la habilidad Mal Sueño de " + attacker.getName());

			if (defender.hasFainted())
				defender.setStatusCondition(new State(StatusConditions.DEBILITATED));
		}
	}

	// -----------------------------
	// Do effect from TRAPPED state (end of the turn)
	// -----------------------------
	private void doTrappedEffect(Pokemon pk) {
		State trappedStatus = pk.getEphemeralStatus(StatusConditions.TRAPPED);

		if (pk.hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {
			// 98_Magic_Guard annuls secondary damage effects
			if (!pk.hasMagicGuardAbility()) {
				// Reduces 12,5% from his initial PS
				float reducePs = pk.getInitialPs() * 0.125f;
				pk.setPs(Math.max(pk.getPs() - reducePs, 0));

				System.out.println(pk.getName() + " está atado y recibe daño");
			}
		}

		trappedStatus.setNbTurns(trappedStatus.getNbTurns() - 1);

		if (trappedStatus.getNbTurns() <= 0) {
			pk.removeEphemeralStatus(StatusConditions.TRAPPED);
			System.out.println(pk.getName() + " ya no está atrapado!");
		}

		if (pk.getPs() <= 0)
			pk.setStatusCondition(new State(StatusConditions.DEBILITATED));
	}

	// -----------------------------
	// Check and put CONFUSION state if needed (ex : thrash attack (TRAPPED BY OWN
	// ATTACK state)) (end of the turn)
	// -----------------------------
	private void putConfusedStateIfNeeded(Pokemon pk) {
		if (pk.hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)) {
			State trappedByOwnAttackStatus = pk.getEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK);

			trappedByOwnAttackStatus.setNbTurns(trappedByOwnAttackStatus.getNbTurns() - 1);

			if (trappedByOwnAttackStatus.getNbTurns() <= 0) {
				pk.removeEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK);
				System.out.println(pk.getName() + " ya no está atrapado por su propio ataque!");

				// Puts CONFUSED state because of trapped by his own attack state finished
				if (!pk.hasActiveEphemeralStatus(StatusConditions.CONFUSED)) {
					// Random number between 2 and 3
					int nbTurnsHoldingStatus = ((int) (Math.random() * 2) + 2);

					System.out.println(pk.getName()
							+ " se siente confuso (a causa de usar el mismo ataque). Estará confuso durante "
							+ nbTurnsHoldingStatus + " turnos.");

					State confused = new State(StatusConditions.CONFUSED, nbTurnsHoldingStatus + 1);
					pk.addEphemeralStatus(StatusConditions.CONFUSED, confused);
				}
			}
		}
	}

	// -----------------------------
	// Reduce turn from DISABLE state (end of the turn)
	// -----------------------------
	private void reduceDisabledAttackTurn(Pokemon pk) {
		if (pk.hasActiveEphemeralStatus(StatusConditions.DISABLE)) {
			State disabledLastaAttackStatus = pk.getEphemeralStatus(StatusConditions.DISABLE);

			disabledLastaAttackStatus.setNbTurns(disabledLastaAttackStatus.getNbTurns() - 1);

			if (disabledLastaAttackStatus.getNbTurns() <= 0) {
				pk.removeEphemeralStatus(StatusConditions.DISABLE);
				System.out.println(pk.getName() + " ya puede volver a usar "
						+ disabledLastaAttackStatus.getAttackDisabled().getName());
			} else
				System.out.println(pk.getName() + " no puede usar todavía "
						+ disabledLastaAttackStatus.getAttackDisabled().getName());
		}
	}

	// -----------------------------
	// Start draining state if needed
	// -----------------------------
	public void startDrainingEffectIfNeeded(BattleContext battleCtx) {
		startDrainedAllTurnsEffect(battleCtx.getPkPlayer());
		startDrainedAllTurnsEffect(battleCtx.getPkIA());
	}

	// -----------------------------
	// Reduce DrainedAllTruns status in last (because it doesn't start on the first
	// turn it was drained)
	// -----------------------------
	private void startDrainedAllTurnsEffect(Pokemon Pokemon) {
		// Turn number "0" allows to avoid applying effect the first turn
		if (Pokemon.hasActiveEphemeralStatus(StatusConditions.DRAINEDALLTURNS)) {
			State drainedAllTurnsStaus = Pokemon.getEphemeralStatus(StatusConditions.DRAINEDALLTURNS);

			if (drainedAllTurnsStaus.getNbTurns() == 0)
				drainedAllTurnsStaus.setNbTurns(1);
		}
	}

	// -----------------------------
	// Apply effects from Draining/ drained state
	// -----------------------------
	public void handleDrainingStatusEffects(Pokemon attacker, Pokemon defender) {
		// Attacker may fainted before the possibility to drain
		if (attacker.hasFainted())
			return;

		// Get PS from drained rival Pokemon
		if (attacker.isDraining()) {
			// Get drained all turns state from defender
			State drainedAllTurnsStatus = defender.getEphemeralStatus(StatusConditions.DRAINEDALLTURNS);

			// Only can drain if it's not the same turn attacking with the draining attack
			// (Leech seed..)
			if (drainedAllTurnsStatus.getNbTurns() != 0)
				doDrainedAllTurnsBeneficiaryEffect(attacker, defender);
		}
	}

	// -----------------------------
	// Do effect from DRAINED ALL TURNS state (end of the turn) => benefits to
	// Pokemon doing the attack
	// -----------------------------
	private void doDrainedAllTurnsBeneficiaryEffect(Pokemon attacker, Pokemon defender) {
		// 98_Magic_Guard annuls secondary damage effects
		if (attacker.hasMagicGuardAbility())
			return;

		if (defender.hasActiveEphemeralStatus(StatusConditions.DRAINEDALLTURNS) && attacker.isDraining()) {
			State drainedAllTurnsStatusDefender = defender.getEphemeralStatuses().get(StatusConditions.DRAINEDALLTURNS);

			if (defender.hasLiquidOozeAbility()) {
				// Reduces 12,5% from his initial PS
				float reducePs = attacker.getInitialPs() * 0.125f;
				attacker.setPs(Math.max(attacker.getPs() - reducePs, 0));

				System.out.println(attacker.getName()
						+ " perdió PS al intentar drenar al rival dada la habilidad rival Viscosecreción; PS restantes : "
						+ attacker.getPs());
			} else {
				if (drainedAllTurnsStatusDefender.getNbTurns() != 0) {
					// Increases 12,5% from his initial PS
					float increasePS = attacker.getInitialPs() * 0.125f;
					attacker.setPs(Math.min(attacker.getPs() + increasePS, attacker.getInitialPs()));

					System.out.println(attacker.getName() + " se curó gracias al efecto activo de Drenadoras");
				}
			}
		}
	}

	// -----------------------------
	// Try to put normal status on Pokemon facing
	// -----------------------------
	public void trySetStatusCondition(Pokemon pk, State newState, Weather weather, boolean isWeatherSuppressed,
			Attack attackAttacker) {
		boolean canBeFrozen = weather != Weather.SUN;

		// Check general abilities
		if (cannotHaveStatusConditionByAbility(pk, weather, attackAttacker))
			return;

		// Already has a status
		if (alreadyHasStatusCondition(pk, newState))
			return;

		switch (newState.getStatusCondition()) {
		case PARALYZED:
			if (isParalysisImmuneByAbility(pk))
				return;

			System.out.println(pk.getName() + " fue paralizado");
			break;
		case POISONED:
			if (isPoisonImmuneByAbility(pk))
				return;

			System.out.println(pk.getName() + " fue envenenado");
			break;
		case BADLY_POISONED:
			if (isPoisonImmuneByAbility(pk))
				return;

			System.out.println(pk.getName() + " fue intoxicado (gravemente envenenado)");
			break;
		case FROZEN:
			if (isFrozenImmuneByAbility(pk, weather))
				return;

			resetFireBoostIfNeeded(pk, canBeFrozen, isWeatherSuppressed);

			if (canBeFrozen && !isWeatherSuppressed)
				System.out.println(pk.getName() + " fue congelado");
			else
				System.out.println(pk.getName()
						+ " no puede ser congelado (no conocemos todavía el porqué - no debería entrar aquí)");
			break;
		case BURNED:
			if (isFireImmuneByAbility(pk))
				return;

			System.out.println(pk.getName() + " fue quemado");
			break;
		case DISABLE:
			break;
		default:
			break;
		}

		pk.setStatusCondition(newState);
	}

	// -----------------------------
	// Try to put ephemeral status on Pokemon facing
	// -----------------------------
	public void trySetEphemeralStatus(State state, Pokemon pk, StatusConditions status, Attack attackAttacker) {
		if (isShieldDustActive(pk, attackAttacker))
			return;

		switch (status) {
		// ASLEEP state works like an ephemeral status, but it's a normal status
		// condition)
		case ASLEEP:
			if (isAsleepImmuneByAbility(pk))
				return;

			System.out.println(pk.getName() + " se quedó dormido");
			break;
		case CONFUSED:
			if (isConfusedImmuneByAbility(pk))
				return;

			System.out.println(pk.getName() + " está confuso");
			break;
		case INFATUATED:
			if (isInfatuatedImmuneByAbility(pk))
				return;

			System.out.println(pk.getName() + " se enamoró del Pokémon rival");
			break;
		default:
			break;
		}

		pk.addEphemeralStatus(state.getStatusCondition(), state);
	}

	// -----------------------------
	// Check if Pokemon can hava a status condition - check with general abilities
	// -----------------------------
	private boolean cannotHaveStatusConditionByAbility(Pokemon pk, Weather weather, Attack attackAttacker) {
		boolean isSunny = weather == Weather.SUN;

		if (pk.hasLeafGuardAbility() && isSunny) {
			System.out.println(pk.getName()
					+ " no puede verse afectado por problemas de estado persistentes dada su habilidad Defensa hoja");
			return true;
		}

		if (isShieldDustActive(pk, attackAttacker))
			return true;

		return false;
	}

	// -----------------------------
	// Check if 19_Shield_Dust is active
	// -----------------------------
	private boolean isShieldDustActive(Pokemon pk, Attack attackAttacker) {
		// 19_Shield_Dust doesn't allow to get secondary effects
		if (attackAttacker.hasSecondaryEffect() && pk.hasShieldDustAbility()) {
			System.out.println(pk.getName()
					+ " no puede verse afectado por problemas de estado secundarios dada su habilidad Polvo escudo");
			return true;
		}

		return false;
	}

	// -----------------------------
	// Check if Pokemon already has a status condition
	// -----------------------------
	private boolean alreadyHasStatusCondition(Pokemon pk, State newState) {
		// Pokemon can be intoxicated ("is one level above Poison")
		if (pk.hasActiveStatusCondition(StatusConditions.POISONED)
				&& newState.getStatusCondition() == StatusConditions.BADLY_POISONED)
			return false;

		if (pk.hasStatusCondition())
			return true;

		return false;
	}

	// -----------------------------
	// Check if Pokemon can be paralyzed
	// -----------------------------
	private boolean isParalysisImmuneByAbility(Pokemon pk) {
		if (pk.hasLimberAbility()) {
			System.out.println(pk.getName() + " evitó la parálisis gracias a Flexibilidad");
			return true;
		}

		return false;
	}

	// -----------------------------
	// Check if Pokemon can be poisoned/intoxicated
	// -----------------------------
	private boolean isPoisonImmuneByAbility(Pokemon pk) {
		if (pk.hasImmunityAbility()) {
			System.out.println(pk.getName() + " no puede envenenarse dada su habilidad Inmunidad");
			return true;
		}

		return false;
	}

	// -----------------------------
	// Check if Pokemon can be frozen
	// -----------------------------
	private boolean isFrozenImmuneByAbility(Pokemon pk, Weather weather) {
		if (pk.hasMagmaArmorAbility()) {
			System.out.println(pk.getName() + " no puede ser congelado dada su habilidad Escudo magma");
			return true;
		}

		// Sun forbids to froze
		if (weather == Weather.SUN) {
			System.out.println(pk.getName() + " no puede ser congelado por el tiempo soleado");
			return true;
		}

		// Pokemon is ice type
		if (pk.getTypes().stream().anyMatch(t -> t.isIceType())) {
			System.out.println(pk.getName() + " no puede ser congelado ya que es de tipo hielo");
			return true;
		}

		return false;
	}

	// -----------------------------
	// Reset Fire boost ability - Frozen conditions
	// -----------------------------
	private void resetFireBoostIfNeeded(Pokemon pk, boolean canBeFrozen, boolean isWeatherSuppressed) {
		// Some abilities like Flash fire, disables the effect
		if (canBeFrozen && !isWeatherSuppressed)
			pk.setIsFireBoostActive(false);
	}

	// -----------------------------
	// Check if Pokemon can be burned
	// -----------------------------
	private boolean isFireImmuneByAbility(Pokemon pk) {
		if (pk.hasWaterVailAbility()) {
			System.out.println(pk.getName() + " no puede ser quemado dada su habilidad Velo agua");
			return true;
		}

		// Fire Pokemon cannot be burned
		if (pk.getTypes().stream().anyMatch(t -> t.isFireType())) {
			System.out.println(pk.getName() + " no puede ser quemado ya que es de tipo fuego");
			return true;
		}

		return false;
	}

	// -----------------------------
	// Check if Pokemon can be asleep
	// -----------------------------
	private boolean isAsleepImmuneByAbility(Pokemon pk) {
		if (pk.hasInsomniaAbility() || pk.hasVitalSpiritAbility()) {
			System.out.println(
					pk.getName() + " no puede dormirse dada su habilidad " + pk.getAbilitySelected().getName());
			return true;
		}

		return false;
	}

	// -----------------------------
	// Check if Pokemon can be confused
	// -----------------------------
	private boolean isConfusedImmuneByAbility(Pokemon pk) {
		if (pk.hasOwnTempoAbility()) {
			System.out.println(pk.getName() + " no puede confundirse dada su habilidad Ritmo propio");
			return true;
		}

		return false;
	}

	// -----------------------------
	// Check if Pokemon can be infatuated
	// -----------------------------
	private boolean isInfatuatedImmuneByAbility(Pokemon pk) {
		if (pk.hasObliviousAbility()) {
			System.out.println(pk.getName() + " no puede enamorarse dada su habilidad Despiste");
			return true;
		}

		return false;
	}

	// -----------------------------
	// Remove some states when switching a Pokemon or a Pokemon has fainted
	// -----------------------------
	public void removeStates(Pokemon pk) {
		removeDrainingState(pk);

		// Trapped normally is removed when a Pokemon has fainted
		// But if for any case Pokemon has switched alive, remove the status
		pk.removeEphemeralStatus(StatusConditions.TRAPPED);
		pk.removeEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK);
	}

	// -----------------------------
	// Remove DRAINED ALL TURNS state
	// -----------------------------
	public void removeDrainingState(Pokemon pk) {
		pk.removeEphemeralStatus(StatusConditions.DRAINEDALLTURNS);
		pk.setIsDraining(false);
	}
}
