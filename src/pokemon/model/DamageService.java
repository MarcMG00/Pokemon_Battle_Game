package pokemon.model;

import pokemon.enums.Weather;

public class DamageService {
	private static class CriticalResult {
		float damage;
		boolean isCritical;

		CriticalResult(float damage, boolean isCritical) {
			this.damage = damage;
			this.isCritical = isCritical;
		}
	}

	// -----------------------------
	// Apply damage
	// -----------------------------
	public AttackResult doDamage(AttackContext ctx) {
		AttackResult result = new AttackResult();

		Pokemon attacker = ctx.attacker;
		Pokemon defender = ctx.defender;
		Attack attack = ctx.attack;
		float power = ctx.getPower();

		float modifiedPower = calculateModifiedPower(attacker, defender, attack, power);
		float baseDamage = calculateBaseDamage(attacker, defender, attack, modifiedPower, ctx);
		CriticalResult critResult = applyCriticalIfNeeded(attacker, attack, baseDamage, ctx);

		float damageAfterCrit = critResult.damage;
		float finalDamage = applyDefensiveAbilities(defender, attacker, attack, damageAfterCrit);

		System.out.println("Damage to " + defender.getName() + " (Id:" + defender.getId() + ")" + " : " + finalDamage);

		result.addDamage(finalDamage);
		result.setCritical(critResult.isCritical);
		return result;
	}

	// -----------------------------
	// Modify power depending on abilities from attacker
	// -----------------------------
	private float calculateModifiedPower(Pokemon attacker, Pokemon defender, Attack attack, float power) {
		Ability ability = attacker.getAbilitySelected();

		power *= applyPhysicalAbilities(ability, attack);
		power *= applyBoostAbilitiesFromReducedPS(attacker, ability, attack);
		power *= applyRivalry(attacker, defender, ability);
		power *= applyIronFist(ability, attack);
		power *= applyAdaptable(attacker, ability, attack);

		return power;
	}

	// -----------------------------
	// Apply physical abilities (physical attacks)
	// -----------------------------
	private float applyPhysicalAbilities(Ability ability, Attack attack) {
		// 37_Huge_Power/ 74_Pure_Power
		if ((ability.getId() == 37 || ability.getId() == 74) && attack.getBases().contains("fisico"))
			return 2f;

		return 1f;
	}

	// -----------------------------
	// Apply booster abilities (activated when some remaining PS)
	// -----------------------------
	private float applyBoostAbilitiesFromReducedPS(Pokemon attacker, Ability ability, Attack attack) {
		if (attacker.getPs() > attacker.getInitialPs() / 3)
			return 1f;

		int abilityId = ability.getId();
		int attackType = attack.getStrTypeToPkType().getId();

		if ((abilityId == 65 && attackType == 12) || // 65_Overgrow (grass)
				(abilityId == 66 && attackType == 7) || // 66_Blaze (fire)
				(abilityId == 67 && attackType == 2) || // 67_Torrent (water)
				(abilityId == 68 && attackType == 3)) { // 68_Swarm (bug)

			System.out.println(attacker.getName() + " potenciado por habilidad " + ability.getName());
			return 1.5f;
		}
		return 1f;
	}

	// -----------------------------
	// Apply 79_Rivalry ability
	// -----------------------------
	private float applyRivalry(Pokemon attacker, Pokemon defender, Ability ability) {
		if (ability.getId() != 79)
			return 1f;

		if (attacker.getSex() == defender.getSex())
			return 1.25f;
		else
			return 0.75f;
	}

	// -----------------------------
	// Apply 89_Iron_Fist ability
	// -----------------------------
	private float applyIronFist(Ability ability, Attack attack) {
		if (ability.getId() != 89)
			return 1f;

		if (attack.isPunchMove())
			return 1.2f;

		return 1f;
	}

	// -----------------------------
	// Apply 91_Adaptable ability
	// -----------------------------
	private float applyAdaptable(Pokemon attacker, Ability ability, Attack attack) {
		if (ability.getId() == 91 && attacker.getTypes().contains(attack.getStrTypeToPkType()))
			return 1.75f;

		return 1f;
	}

	// -----------------------------
	// Calculate base damage
	// -----------------------------
	private float calculateBaseDamage(Pokemon attacker, Pokemon defender, Attack attack, float modifiedPower,
			AttackContext ctx) {
		boolean isSpecial = attack.getBases().contains("especial");
		float randomVariation = (float) (85 + Math.random() * 15);
		float weatherModifier = getWeatherModifier(ctx);

		float attackStat = isSpecial ? attacker.getEffectiveSpecialAttack() : attacker.getEffectiveAttack();

		float defenseStat = isSpecial ? defender.getEffectiveSpecialDefense() : defender.getEffectiveDefense();

		float base = (((0.2f * 100f + 1f) * attackStat * modifiedPower) / (25f * defenseStat) + 2f);

		float damage = 0.01f * attack.getBonus() * attack.getEffectivenessAgainstPkFacing() * weatherModifier
				* randomVariation * base;

		// 18_Flash_Fire boost ability
		if (attacker.getIsFireBoostActive() && attack.getStrTypeToPkType().getId() == 7)
			damage *= 1.5f;

		return damage;
	}

	// -----------------------------
	// Apply critical damage by probabilities
	// -----------------------------
	private CriticalResult applyCriticalIfNeeded(Pokemon attacker, Attack attack, float damage, AttackContext ctx) {
		Ability ability = attacker.getAbilitySelected();
		boolean isCrit;

		if (attack.getId() == 13)
			isCrit = getHighCriticity30(ctx);
		else if (attack.getId() == 2 || attack.getId() == 75)
			isCrit = getHighCriticity40(ctx);
		else
			isCrit = getCriticity(ctx);

		if (!isCrit)
			return new CriticalResult(damage, false);

		System.out.println("Fue un golpe crítico");

		if (ability.getId() == 97) // 97_Sniper ability does *3 damage
			return new CriticalResult(damage * 3f, true);

		return new CriticalResult(damage * 2f, true);
	}

	// -----------------------------
	// Apply defensive abilities
	// -----------------------------
	private float applyDefensiveAbilities(Pokemon defender, Pokemon attacker, Attack attack, float damage) {
		Ability defAbility = defender.getAbilitySelected();
		int abilityId = defAbility.getId();
		int attackTypeId = attack.getStrTypeToPkType().getId();

		boolean isFire = attackTypeId == 7;
		boolean isIce = attackTypeId == 9;

		// 5_Sturdy ability cannot be defeated by one hit KO or by one attack if PS are
		// on max
		if (abilityId == 5 && !defAbility.getAlreadyUsedOnEnter() && defender.getInitialPs() == defender.getPs()
				&& damage >= defender.getPs()) {
			defAbility.setAlreadyUsedOnEnter(true);
			return defender.getInitialPs() - 1f;
		}

		// 47_Thick_Fat ability/ 85_Heatproof reduces damage by 2 (only if attack type
		// it's fire or ice type)
		if ((abilityId == 47 && (isFire || isIce)) || (abilityId == 85 && isFire))
			return damage / 2f;

		// 87_Dry_Skin ability with a fire attack, do 25% more damage
		if (abilityId == 87 && attack.getStrTypeToPkType().getId() == 7)
			return damage * 1.25f;

		return damage;
	}

	// -----------------------------
	// Adds multiplier depending on weather of the game
	// -----------------------------
	public float getWeatherModifier(AttackContext ctx) {

		Weather weather = ctx.weather;
		boolean isWeatherSuppresed = ctx.isWeatherSuppressed;

		if (isWeatherSuppresed)
			return 1.0f;

		if (weather == Weather.RAIN) {
			if (ctx.attack.getStrTypeToPkType().getId() == 2) // Water
				return 1.5f;
			if (ctx.attack.getStrTypeToPkType().getId() == 7) // Fire
				return 0.5f;
		}

		if (weather == Weather.SUN) {
			if (ctx.attack.getStrTypeToPkType().getId() == 7) // Fire
				return 1.5f;
			if (ctx.attack.getStrTypeToPkType().getId() == 2) // Water
				return 0.5f;
		}
		return 1.0f;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage) => 10% of probabilities
	// -----------------------------
	public boolean getCriticity(AttackContext ctx) {
		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 10) {
			return this.canReceiveCriticalAttacks(ctx);
		}
		return false;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage) => 30% of probabilities
	// -----------------------------
	public boolean getHighCriticity30(AttackContext ctx) {
		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 30) {
			return this.canReceiveCriticalAttacks(ctx);
		}
		return false;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage) => 40% of probabilities
	// -----------------------------
	public boolean getHighCriticity40(AttackContext ctx) {
		int randomCritic = (int) (Math.random() * 100);

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 40) {
			return this.canReceiveCriticalAttacks(ctx);
		}
		return false;
	}

	// -----------------------------
	// Check if defender can receive critical attacks
	// -----------------------------
	public boolean canReceiveCriticalAttacks(AttackContext ctx) {
		// 4_Battle_Armor / 75_Shell_Armor cannot recieve critic damage because of
		// ability
		if (ctx.defender.getAbilitySelected().getId() == 4 || ctx.defender.getAbilitySelected().getId() == 75) {
			System.out.println(ctx.defender.getName() + " no puede recibir ataques críticos dada su habilidad "
					+ ctx.defender.getAbilitySelected().getName());
			return false;
		}
		return true;
	}
}
