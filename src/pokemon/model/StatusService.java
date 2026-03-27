package pokemon.model;

import java.util.ArrayList;
import java.util.List;

import pokemon.enums.AttackCategory;
import pokemon.enums.StatusConditions;

public class StatusService {
	private final BattleContext battleCtx;

	public StatusService(BattleContext battleCtx) {
		this.battleCtx = battleCtx;
	}

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
	public void evaluateStartTurnStatuses(Pokemon playerPk, Pokemon iaPk, TurnContext turnCtx) {
		if (shouldEvaluateStatus(playerPk))
			evaluateStatusStartOfTurn(playerPk, turnCtx);

		if (shouldEvaluateStatus(iaPk))
			evaluateStatusStartOfTurn(iaPk, turnCtx);
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
	public void evaluateStatusStartOfTurn(Pokemon pk, TurnContext ctx) {
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
	public List<Player> applyTurnStatusReductions() {
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
}
