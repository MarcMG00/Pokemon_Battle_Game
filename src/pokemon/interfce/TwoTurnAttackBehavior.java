package pokemon.interfce;

import pokemon.model.Pokemon;

public interface TwoTurnAttackBehavior {
	/**
     * Indicates the Pokemon is still on "is charging attack" mode
     * The implementation can read the flags from the Pokemon (isChargingAttackForNextRound, alreadyUsedFly,...)
     */
    boolean isCharging(Pokemon attacker);

    /**
     * Behavior when the attacker initiates the charged attack (first turn)
     * Update flags from Pokemon (isChargingAttackForNextRound, alreadyUsedFly, etc.)
     */
    void onCharge(Pokemon attacker, Pokemon defender);

    /**
     * Behavior when the attacker initiates the charged attack (second turn).
     * Do the final attack execution (llamar a PkVPk o similar) and clean flags.
     *
     * @param attacker Pokemon attacker
     * @param defender Pokemon defending
     * @param alreadyChecked true if precision/evasion where validated before calling it
     */
    void onExecute(Pokemon attacker, Pokemon defender, boolean alreadyChecked);
}
