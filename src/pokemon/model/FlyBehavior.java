package pokemon.model;

import pokemon.interfce.TwoTurnAttackBehavior;

public class FlyBehavior implements TwoTurnAttackBehavior {

	@Override
    public boolean isCharging(Pokemon attacker) {
		// If not flagged as charging, then next action will be "to charge"
        return attacker.getIsChargingAttackForNextRound();
    }

    @Override
    public void onCharge(Pokemon attacker, Pokemon defender) {
    	// Flag Pokemon as started to charge the attack (he is flying)
        System.out.println(attacker.getName() + " voló muy alto.");
        attacker.setIsChargingAttackForNextRound(true);
    }

    @Override
    public void onExecute(Pokemon attacker, Pokemon defender, boolean alreadyChecked) {
    	// When executing : realize the final attack using PkVPk
        // Puedes decidir pasar true/false a getProbabilityOfAttackingAndAttack según ya se haya
        // validado la precisión afuera. Nosotros llamaremos directamente a getAttackEffect
        // desde una nueva instancia de PkVPk para mantener compatibilidad.
        System.out.println(attacker.getName() + " usó vuelo");

        // Execute the attack effect
        //PkVPk engine = new PkVPk(attacker, defender);
        
        // Pass to true cause already on the execution phase of charged attack
        //engine.doAttackEffect();
        
        // Clean charging flag
        attacker.setIsChargingAttackForNextRound(false);

        // Flag alreadyUsedFly to avoid Pokemon attacking do the first movement again before Pokemon defending
        attacker.setAlreadyUsedTwoTurnAttackBehavior(false);
    }

}
