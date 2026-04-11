package pokemon.model;

import java.util.ArrayList;
import java.util.List;

import pokemon.enums.AttackCategory;
import pokemon.enums.StatusConditions;
import pokemon.enums.Weather;

public class StatusService {
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
		pk.doFrozenEffect();
	}

	// -----------------------------
	// Clear DRAINED ALL TURNS effects for both Pokemon
	// -----------------------------
	public void clearDrainEffects(Pokemon pkA, Pokemon pkB) {
		pkA.setIsDraining(false);
		pkB.setIsDraining(false);
		pkA.removeStates();
		pkB.removeStates();
	}

	// -----------------------------
	// Helper: Evaluate states BEFORE attacking. Some states influence the
	// probability of attacking, for example when confused, paralyzed, etc.
	// -----------------------------
	public boolean canAttackEvaluatingAllStatesToAttack(Pokemon pk) {
		pk.canAttackFrozen();
		pk.checkCanMoveParalyzed();
		pk.canAttackParalyzed();
		boolean canAttackConfused = pk.canAttackConfused();
		boolean canAttackAsleep = pk.doAsleepEffect();

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
		playerAttacker.getPkCombatting().doBurnedEffectEndTurn();
		playerAttacker.getPkCombatting().doPoisonedEffectEndTurn();
		// Ephemeral status
		playerAttacker.getPkCombatting().doTrappedEffect();
		playerAttacker.getPkCombatting().putConfusedStateIfNeeded();
		playerAttacker.getPkCombatting().reduceDisabledAttackTurn();
		playerAttacker.getPkCombatting().doDrainedAllTurnsEffect(playerDefender.getPkCombatting());

		// Get PS from drained rival Pokemon
		if (playerAttacker.getPkCombatting().getIsDraining()) {
			// Get drained all turns state from defender
			State drainedAllTurnsStatus = playerDefender.getPkCombatting()
					.getEphemeralStatus(StatusConditions.DRAINEDALLTURNS);

			// Only can drain if it's not the same turn attacking with the draining attack
			// (Leech seed..)
			if (drainedAllTurnsStatus.getNbTurns() != 0)
				playerAttacker.getPkCombatting().doDrainedAllTurnsBeneficiaryEffect(playerDefender.getPkCombatting());
		}

		// Force switch if (for example), after getting drained, has no more PS
		return new StatusResult(playerAttacker.getPkCombatting().isDebilitated());
	}

	// -----------------------------
	// Reduce DrainedAllTruns status in last (because it doesn't start on the first
	// turn it was drained)
	// -----------------------------
	public void reduceDrainedAllTurnsEffects(Player playerAttacker, Player playerDefender) {
		playerAttacker.getPkCombatting().startDrainedAllTurnsEffect();
	}

	// -----------------------------
	// Try to put normal status on Pokemon facing
	// -----------------------------
	public void trySetStatus(Pokemon pk, State newState, Weather weather, boolean isWeatherSuppressed,
			Attack attackAttacker) {
		boolean canBeFrozen = weather != Weather.SUN;
		boolean isSunny = weather == Weather.SUN;
		Ability ability = pk.getAbilitySelected();

		// 102_Leaf_Guard
		if (ability.getId() == 102 && isSunny) {
			System.out.println(pk.getName()
					+ " no puede verse afectado por problemas de estado persistentes dada su habilidad Defensa hoja");
			return;
		}

		if (ability != null) {
			// 19_Shield_Dust doesn't allow to get secondary effects
			if (attackAttacker.hasSecondaryEffect() && ability.getId() == 19) {
				System.out.println(pk.getName()
						+ " no puede verse afectado por problemas de estado secundarios dada su habilidad Polvo escudo");
				return;
			}
		}

		// Get asleep state (because it has a number of turns, it works like an
		// ephemeral status, but it's a normal status condition)
		// Already has a status
		if (pk.hasStatusCondition() || pk.hasActiveEphemeralStatus(StatusConditions.ASLEEP))
			return;

		switch (newState.getStatusCondition()) {
		case PARALYZED:
			// Limber ability prevents paralysis
			if (pk.getAbilitySelected().getId() == 7) {
				System.out.println(pk.getName() + " evitó la parálisis gracias a Flexibilidad");
				return;
			} else
				System.out.println(pk.getName() + " fue paralizado");
			break;
		case POISONED:
			// 17_Immunity ability
			if (pk.getAbilitySelected().getId() == 17) {
				System.out.println(pk.getName() + " no puede envenenarse dada su habilidad Inmunidad");
				return;
			} else
				System.out.println(pk.getName() + " fue envenenado");
			break;
		case BADLY_POISONED:
			break;
		case FROZEN:
			// 40_Magma_Armor ability
			if (pk.getAbilitySelected().getId() == 40) {
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
			// 41_Water_Vell ability
			if (pk.getAbilitySelected().getId() == 41) {
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
		Ability ability = pk.getAbilitySelected();
		if (ability == null)
			return;

		// 19_Shield_Dust doesn't allow to get secondary effects
		if (attackAttacker.hasSecondaryEffect() && ability.getId() == 19) {
			System.out.println(pk.getName()
					+ " no puede verse afectado por problemas de estado secundarios dada su habilidad Polvo escudo");
			return;
		}

		switch (status) {
		case ASLEEP:
			// 15_Insomnia, 72_Vital_Spirit
			if (ability.getId() == 15 || ability.getId() == 72) {
				System.out.println(
						pk.getName() + " no puede dormirse dada su habilidad " + pk.getAbilitySelected().getName());
				return;
			} else
				System.out.println(pk.getName() + " se quedó dormido");
			break;
		case CONFUSED:
			// 20_Own_Tempo
			if (ability.getId() == 20) {
				System.out.println(pk.getName() + " no puede confundirse dada su habilidad Ritmo propio");
				return;
			} else
				System.out.println(pk.getName() + " está confuso");
			break;
		case INFATUATED:
			// 12_Oblivious
			if (ability.getId() == 12) {
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
}
