package pokemon.model;

import java.util.ArrayList;
import java.util.List;

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
	// Check if trapped by own attack
	// -----------------------------
	public boolean isTrappedByOwnAttack(Pokemon pk) {
		return pk.hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)
				&& pk.getEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK).getNbTurns() > 0;
	}

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
				&& pk.getNextMovement().getCategory() == AttackCategory.CHARGED && pk.getIsChargingAttackForNextRound();

		boolean normalAttack = pk.getNextMovement() != null
				&& pk.getNextMovement().getCategory() != AttackCategory.CHARGED;

		return (secondTurnCharged || normalAttack) && pk.getCanDonAnythingNextRound();
	}

	// -----------------------------
	// Helper: Evaluate states BEFORE ordering "who should attack first" decision.
	// Some states decrease their turn at the beginning of the turn and apply
	// effects for example when paralyzed, it reduces speed
	// -----------------------------
	public void evaluateStatusStartOfTurn(Pokemon pk) {
		doFrozenEffectStartTurn(pk);
	}

	// -----------------------------
	// Clear DRAINED ALL TURNS effects for both Pokemon
	// -----------------------------
	public void clearDrainEffects(Pokemon pkA, Pokemon pkB) {
		pkA.setIsDraining(false);
		pkB.setIsDraining(false);
		removeStates(pkA);
		removeStates(pkB);
	}

	// -----------------------------
	// Helper: Evaluate states BEFORE attacking. Some states influence the
	// probability of attacking, for example when confused, paralyzed, etc.
	// -----------------------------
	public boolean canAttackEvaluatingAllStatesToAttack(Pokemon pk) {
		canAttackFrozenStartTurn(pk);
		checkCanMoveParalyzed(pk);
		canAttackParalyzedStartTurn(pk);
		boolean canAttackConfused = canAttackConfusedStartTurn(pk);
		boolean canAttackAsleep = doAsleepEffectStartTurn(pk);

		boolean canAttack = pk.getCanAttack() && canAttackConfused && canAttackAsleep;
		pk.setCanAttack(canAttack);

		return canAttack;
	}

	// -----------------------------
	// Do status conditions end of turn effects
	// -----------------------------
	public List<Player> applyTurnStatusReductions(BattleContext battleCtx) {
		List<Player> faintedPlayers = new ArrayList<>();

		if (reduceNumberTurnsEffects(battleCtx.getPlayer(), battleCtx.getIa()).isAttackerFainted())
			faintedPlayers.add(battleCtx.getPlayer());

		if (reduceNumberTurnsEffects(battleCtx.getIa(), battleCtx.getPlayer()).isAttackerFainted())
			faintedPlayers.add(battleCtx.getIa());

		reduceDrainedAllTurnsEffects(battleCtx.getPlayer(), battleCtx.getIa());
		reduceDrainedAllTurnsEffects(battleCtx.getIa(), battleCtx.getPlayer());

		return faintedPlayers;
	}

	// -----------------------------
	// Helper: Reduce number of turns remaining on states
	// -----------------------------
	public StatusResult reduceNumberTurnsEffects(Player playerAttacker, Player playerDefender) {
		// Normal status
		doBurnedEffectEndTurn(playerAttacker.getPkCombatting());
		doPoisonedEffectEndTurn(playerAttacker.getPkCombatting());
		doAsleepEffectEndTurn(playerAttacker.getPkCombatting(), playerDefender.getPkCombatting());
		// Ephemeral status
		doTrappedEffect(playerAttacker.getPkCombatting());
		putConfusedStateIfNeeded(playerAttacker.getPkCombatting());
		reduceDisabledAttackTurn(playerAttacker.getPkCombatting());
		doDrainedAllTurnsEffect(playerAttacker.getPkCombatting(), playerDefender.getPkCombatting());

		// Get PS from drained rival Pokemon
		if (playerAttacker.getPkCombatting().getIsDraining()) {
			// Get drained all turns state from defender
			State drainedAllTurnsStatus = playerDefender.getPkCombatting()
					.getEphemeralStatus(StatusConditions.DRAINEDALLTURNS);

			// Only can drain if it's not the same turn attacking with the draining attack
			// (Leech seed..)
			if (drainedAllTurnsStatus.getNbTurns() != 0)
				doDrainedAllTurnsBeneficiaryEffect(playerAttacker.getPkCombatting(), playerDefender.getPkCombatting());
		}

		// Force switch if (for example), after getting drained, has no more PS
		return new StatusResult(playerAttacker.getPkCombatting().isFainted());
	}

	// -----------------------------
	// Reduce DrainedAllTruns status in last (because it doesn't start on the first
	// turn it was drained)
	// -----------------------------
	public void reduceDrainedAllTurnsEffects(Player playerAttacker, Player playerDefender) {
		startDrainedAllTurnsEffect(playerAttacker.getPkCombatting());
	}

	// -----------------------------
	// Try to put normal status on Pokemon facing
	// -----------------------------
	public void trySetStatus(Pokemon pk, State newState, Weather weather, boolean isWeatherSuppressed,
			Attack attackAttacker) {
		boolean canBeFrozen = weather != Weather.SUN;
		boolean isSunny = weather == Weather.SUN;

		// 102_Leaf_Guard
		if (pk.hasLeafGuardAbility() && isSunny) {
			System.out.println(pk.getName()
					+ " no puede verse afectado por problemas de estado persistentes dada su habilidad Defensa hoja");
			return;
		}

		if (pk.getAbilitySelected() != null) {
			// 19_Shield_Dust doesn't allow to get secondary effects
			if (attackAttacker.hasSecondaryEffect() && pk.hasShieldDustAbility()) {
				System.out.println(pk.getName()
						+ " no puede verse afectado por problemas de estado secundarios dada su habilidad Polvo escudo");
				return;
			}
		}

		// Already has a status
		if (pk.hasStatusCondition())
			return;

		switch (newState.getStatusCondition()) {
		case PARALYZED:
			// Limber ability prevents paralysis
			if (pk.hasLimberAbility()) {
				System.out.println(pk.getName() + " evitó la parálisis gracias a Flexibilidad");
				return;
			} else
				System.out.println(pk.getName() + " fue paralizado");
			break;
		case POISONED:
			// 17_Immunity ability
			if (pk.hasImmunityAbility()) {
				System.out.println(pk.getName() + " no puede envenenarse dada su habilidad Inmunidad");
				return;
			} else
				System.out.println(pk.getName() + " fue envenenado");
			break;
		case BADLY_POISONED:
			break;
		case FROZEN:
			// 40_Magma_Armor ability
			if (pk.hasMagmaArmorAbility()) {
				System.out.println(pk.getName() + " no puede ser congelado dada su habilidad Escudo magma");
				return;
			}

			// Sun forbids to froze
			if (weather == Weather.SUN) {
				System.out.println(pk.getName() + " no puede ser congelado por el tiempo soleado");
				return;
			}

			// Pokemon is ice type
			if (pk.getTypes().stream().anyMatch(t -> t.getId() == 9)) {
				System.out.println(pk.getName() + " no puede ser congelado ya que es de tipo hielo");
				return;
			}

			if (canBeFrozen && !isWeatherSuppressed && pk.getTypes().stream().noneMatch(t -> t.getId() == 9)) {
				System.out.println(pk.getName() + " fue congelado");
			} else {
				System.out.println(pk.getName()
						+ " no puede ser congelado por otra razón no mencionada todavía (no debería entrar)");
				return;
			}
			break;
		case BURNED:
			// 41_Water_Veil ability
			if (pk.hasWaterVailAbility()) {
				System.out.println(pk.getName() + " no puede ser quemado dada su habilidad Velo agua");
				return;
			}

			// Fire Pokemon cannot be burned
			if (pk.getTypes().stream().anyMatch(t -> t.getId() == 7)) {
				System.out.println(pk.getName() + " no puede ser quemado ya que es de tipo fuego");
				return;
			} else
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
		// 19_Shield_Dust doesn't allow to get secondary effects
		if (attackAttacker.hasSecondaryEffect() && pk.hasShieldDustAbility()) {
			System.out.println(pk.getName()
					+ " no puede verse afectado por problemas de estado secundarios dada su habilidad Polvo escudo");
			return;
		}

		switch (status) {
		// ASLEEP state works like an ephemeral status, but it's a normal status
		// condition)
		case ASLEEP:
			if (pk.hasInsomniaAbility() || pk.hasVitalSpiritAbility()) {
				System.out.println(
						pk.getName() + " no puede dormirse dada su habilidad " + pk.getAbilitySelected().getName());
				return;
			} else
				System.out.println(pk.getName() + " se quedó dormido");
			break;
		case CONFUSED:
			if (pk.hasOwnTempoAbility()) {
				System.out.println(pk.getName() + " no puede confundirse dada su habilidad Ritmo propio");
				return;
			} else
				System.out.println(pk.getName() + " está confuso");
			break;
		case INFATUATED:
			if (pk.hasObliviousAbility()) {
				System.out.println(pk.getName() + " no puede enamorarse dada su habilidad Despiste");
				return;
			} else
				System.out.println(pk.getName() + " se enamoró del Pokémon rival");
			break;
		default:
			break;
		}
		pk.addEphemeralStatus(state.getStatusCondition(), state);
	}

	// -----------------------------
	// Gets if Pokemon can attack because of FROZEN state (check start of the turn
	// after applying effect of Frozen)
	// -----------------------------
	public void canAttackFrozenStartTurn(Pokemon pk) {
		if (pk.hasActiveStatusCondition(StatusConditions.FROZEN)) {
			if (pk.getStatusCondition().getCanMoveStatusCondition())
				pk.setCanAttack(true);
			else
				pk.setCanAttack(false);
		}
	}

	// -----------------------------
	// Gets if Pokemon can attack because of PARALYZED state (check start of the
	// turn)
	// -----------------------------
	public void canAttackParalyzedStartTurn(Pokemon pk) {
		if (pk.hasActiveStatusCondition(StatusConditions.PARALYZED)) {
			if (pk.getStatusCondition().getCanMoveStatusCondition()) {
				pk.setCanAttack(true);
				System.out.println(ANSI_CYAN + pk.getName() + " => paralizado - puede atacar" + ANSI_RESET);
			} else {
				pk.setCanAttack(false);
				System.out.println(ANSI_CYAN + pk.getName() + " => paralizado - no puede atacar" + ANSI_RESET);
			}
		}
	}

	// -----------------------------
	// Gets if Pokemon can attack because of CONFUSION state (check start of the
	// turn)
	// -----------------------------
	public boolean canAttackConfusedStartTurn(Pokemon pk) {
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
					pk.setPs(pk.getPs() - damage);

					if (pk.isFainted()) {
						pk.setStatusCondition(new State(StatusConditions.DEBILITATED));
						System.out.println(pk.getName() + " quedó debilitado por la confusión!");
					}

					canAttackConfused = false; // received damage or dies => cannot continue
				} else
					System.out.println(pk.getName() + " está confuso...");
			}
		}
		return canAttackConfused;
	}

	// -----------------------------
	// Do effect from FROZEN state (start of the turn before checking if can attack)
	// -----------------------------
	public void doFrozenEffectStartTurn(Pokemon pk) {
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
				frozenStatus.setCanMoveStatusCondition(false);
				// Adds +10% each turn not thawed
				frozenStatus.setPercentToBeDefrosted(frozenStatus.getPercentToBeDefrosted() + 10);

				System.out.println(ANSI_CYAN + pk.getName() + " => congelado - no puede atacar" + ANSI_RESET);
			}
		}
	}

	// -----------------------------
	// Do effect from BURNED state (end of the turn)
	// -----------------------------
	public void doBurnedEffectEndTurn(Pokemon pk) {
		// 98_Magic_Guard annuls secondary damage effects
		if (pk.hasMagicGuardAbility())
			return;

		if (pk.hasActiveStatusCondition(StatusConditions.BURNED)) {
			// Reduces current PS by 6.25%
			float reducePs = pk.getInitialPs() * 0.0625f;

			// 85_Heatproof ability reduces to half the burned effect
			if (pk.hasHeatProofAbility())
				reducePs /= 2;

			pk.setPs(pk.getPs() - reducePs);

			System.out.println(pk.getName() + " se resiente de la quemadura XD - PS actuales : " + pk.getPs());

			if (pk.isFainted())
				pk.setStatusCondition(new State(StatusConditions.DEBILITATED));
		}
	}

	// -----------------------------
	// Check can move from PARALYZED state (only before attacking)
	// -----------------------------
	public void checkCanMoveParalyzed(Pokemon pk) {
		if (pk.hasActiveStatusCondition(StatusConditions.PARALYZED)) {
			State paralyzedStatus = pk.getStatusCondition();

			int attackProbability = (int) (Math.random() * 100);

			if (attackProbability <= 25)
				paralyzedStatus.setCanMoveStatusCondition(true);
			else
				paralyzedStatus.setCanMoveStatusCondition(false);
		}
	}

	// -----------------------------
	// Do effect from POISONED state (end of the turn)
	// -----------------------------
	public void doPoisonedEffectEndTurn(Pokemon pk) {
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
				pk.setPs(pk.getPs() - reducePs);

				System.out.println(pk.getName() + " está envenenado - PS actuales : " + pk.getPs());

				if (pk.isFainted())
					pk.setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
		}
	}

	// -----------------------------
	// Do effect from ASLEEP state (start of the turn)
	// -----------------------------
	public boolean doAsleepEffectStartTurn(Pokemon pk) {
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
	// Apply confusion damage (only start of the turn)
	// -----------------------------
	public float doConfusedDammageStartTurn(Pokemon pk) {
		// There is a random variation when attacking (the total damage is not the same
		// every time)
		int randomVariation = (int) ((Math.random() * (100 - 85)) + 85);

		float dmg = 0;

		// Apply damage
		dmg = ((((40f + 2f) * (40f * (pk.getAttack() / pk.getDef()))) / 50f) + 2f) * (randomVariation / 100f);

		return dmg;
	}

	// -----------------------------
	// Do effect from TRAPPED state (end of the turn)
	// -----------------------------
	private void doTrappedEffect(Pokemon pk) {
		// 98_Magic_Guard annuls secondary damage effects
		if (pk.hasMagicGuardAbility())
			return;

		if (pk.hasActiveEphemeralStatus(StatusConditions.TRAPPED)) {
			State trappedStatus = pk.getEphemeralStatus(StatusConditions.TRAPPED);

			trappedStatus.setNbTurns(trappedStatus.getNbTurns() - 1);

			if (trappedStatus.getNbTurns() <= 0) {
				pk.removeEphemeralStatus(StatusConditions.TRAPPED);
				System.out.println(pk.getName() + " ya no está atrapado!");
			} else {
				// Reduces 12,5% from his initial PS
				float reducePs = pk.getInitialPs() * 0.125f;
				pk.setPs(pk.getPs() - reducePs);

				System.out.println(pk.getName() + " está atado y recibe daño");

				if (pk.getPs() <= 0)
					pk.setStatusCondition(new State(StatusConditions.DEBILITATED));
			}
		}
	}

	// -----------------------------
	// Do effect from DRAINED ALL TURNS state (end of the turn) => affects to enemy
	// -----------------------------
	public void doDrainedAllTurnsEffect(Pokemon attacker, Pokemon defender) {
		// 98_Magic_Guard annuls secondary damage effects
		if (attacker.hasMagicGuardAbility())
			return;

		if (attacker.hasActiveEphemeralStatus(StatusConditions.DRAINEDALLTURNS)) {
			State drainedAllTurnsStaus = attacker.getEphemeralStatus(StatusConditions.DRAINEDALLTURNS);

			// Turn number "0" allows to avoid applying effect the first turn
			if (drainedAllTurnsStaus.getNbTurns() != 0) {
				// Cannot be drained if defender has the ability 64_Liquid_Ooze
				if (!defender.hasLiquidOozeAbility()) {
					// Reduces 12,5% from his initial PS
					float reducePs = attacker.getInitialPs() * 0.125f;
					attacker.setPs(attacker.getPs() - reducePs);

					System.out.println(
							attacker.getName() + " está drenado y recibe daño; PS restantes : " + attacker.getPs());

					if (attacker.isFainted())
						attacker.setStatusCondition(new State(StatusConditions.DEBILITATED));
				}
			}
		}
	}

	// -----------------------------
	// Reduce nb turns from DRAINED ALL TURNS state (end of the turn) => affects to
	// enemy
	// -----------------------------
	private void startDrainedAllTurnsEffect(Pokemon pk) {
		// Turn number "0" allows to avoid applying effect the first turn
		if (pk.hasActiveEphemeralStatus(StatusConditions.DRAINEDALLTURNS)) {
			State drainedAllTurnsStaus = pk.getEphemeralStatus(StatusConditions.DRAINEDALLTURNS);

			if (drainedAllTurnsStaus.getNbTurns() == 0)
				drainedAllTurnsStaus.setNbTurns(1);
		}

	}

	// -----------------------------
	// Do effect from DRAINED ALL TURNS state (end of the turn) => benefits to
	// Pokemon doing the attack
	// -----------------------------
	private void doDrainedAllTurnsBeneficiaryEffect(Pokemon attacker, Pokemon defender) {
		if (defender.hasActiveEphemeralStatus(StatusConditions.DRAINEDALLTURNS)) {
			State drainedAllTurnsStatusDefender = defender.getEphemeralStatuses().get(StatusConditions.DRAINEDALLTURNS);

			if (defender.hasLiquidOozeAbility()) {
				// Reduces 12,5% from his initial PS
				float reducePs = attacker.getInitialPs() * 0.125f;
				attacker.setPs(attacker.getPs() - reducePs);

				System.out.println(attacker.getName()
						+ " perdió PS al intentar drenar al rival dada la habilidad rival Viscosecreción; PS restantes : "
						+ attacker.getPs());
			} else {
				if (drainedAllTurnsStatusDefender.getNbTurns() != 0) {
					// Increases 12,5% from his initial PS
					float increasePS = attacker.getInitialPs() * 0.125f;
					attacker.setPs(attacker.getPs() + increasePS);

					System.out.println(attacker.getName() + " se curó gracias al efecto activo de Drenadoras");
				}
			}
		}
	}

	// -----------------------------
	// Remove DRAINED ALL TURNS state
	// -----------------------------
	private void removeDrainedAllTurns(Pokemon pk) {
		if (pk.hasActiveEphemeralStatus(StatusConditions.DRAINEDALLTURNS)) {
			pk.setIsDraining(false);
			pk.removeEphemeralStatus(StatusConditions.TRAPPED);
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
	// Check and put CONFUSION state if needed (ex : thrash attack (TRAPPED BY OWN
	// ATTACK state)) (end of the
	// turn)
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
	// Do effect from ASLEEP state (end of the turn)
	// -----------------------------
	public void doAsleepEffectEndTurn(Pokemon attacker, Pokemon defender) {
		if (applyBadDreamsAbility(attacker, defender))
			return;
	}

	// -----------------------------
	// Apply Bad Dreams ability if needed (end of the turn)
	// -----------------------------
	public boolean applyBadDreamsAbility(Pokemon attacker, Pokemon defender) {

		// 123_Bad_dreams ability => needs opponent to be asleep
		if (attacker.hasBadDreamsAbility() && defender.hasActiveEphemeralStatus(StatusConditions.ASLEEP)) {
			// Reduces current PS by 1/8 from max PS
			float reducePs = defender.getInitialPs() * 0.125f;
			defender.setPs(defender.getPs() - reducePs);

			System.out.println(
					defender.getName() + " sufre daño a causa de la habilidad Mal Sueño de " + attacker.getName());

			if (defender.isFainted())
				defender.setStatusCondition(new State(StatusConditions.DEBILITATED));

			return true;
		}

		return false;
	}

	// -----------------------------
	// Remove states when changing or dying a Pokemon
	// -----------------------------
	public void removeStates(Pokemon pk) {
		removeDrainedAllTurns(pk);
	}

}
