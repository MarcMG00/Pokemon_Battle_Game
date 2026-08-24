package pokemon.abilityInterface;

import pokemon.model.Attack;
import pokemon.model.BattleContext;
import pokemon.model.Pokemon;

public class OvercoatAbility extends AbilityEffect {
	public OvercoatAbility(Pokemon owner) {
		super(owner);
	}

	public boolean beforeDamage(BattleContext battleCtx, Pokemon attacker, Attack attack) {
		// Not affected by attacks of Spore kind
		if (attack.isPoisonPowder() || attack.isStunSpore() || attack.isSleepPowder() || attack.isSpore()
				|| attack.isCottonSpore() || attack.isRagePowder() || attack.isPowder() || attack.isMagicPowder()) {
			System.out.println(attacker.getName() + " (Id:" + attacker.getId() + ")" + " usó " + attack.getName());

			System.out.println(
					owner.getName() + " no se ve afectado por ataques de tipo espora gracias a su habilidad Funda");

			return false;
		}

		return true;
	}

}
