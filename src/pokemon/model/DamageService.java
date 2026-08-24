package pokemon.model;

import pokemon.enums.Weather;

public class DamageService {

	private final StatService statService;

	public DamageService() {
		this.statService = new StatService();
	}

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

		Pokemon attacker = ctx.getAttacker();
		Pokemon defender = ctx.getDefender();
		Attack attack = ctx.getAttack();
		float power = ctx.getPower();

		float modifiedPower = calculateModifiedPowerByAbility(attacker, defender, attack, power);
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
	private float calculateModifiedPowerByAbility(Pokemon attacker, Pokemon defender, Attack attack, float power) {
		power *= applyPhysicalAbilities(attacker, attack);
		power *= applyBoostAbilitiesFromReducedPS(attacker, attack);
		power *= applyRivalry(attacker, defender);
		power *= applyIronFist(attacker, attack);
		power *= applyAdaptable(attacker, attack);
		power *= applyPowerDependingPowerAttack(attacker, attack);
		power *= applyPowerAttackModifiers(attacker, attack);

		return power;
	}

	// -----------------------------
	// Apply physical abilities (physical attacks)
	// -----------------------------
	private float applyPhysicalAbilities(Pokemon attacker, Attack attack) {
		if (attack.getBases().contains("fisico") && (attacker.hasHugePowerAbility() || attacker.hasPurePowerAbility()))
			return 2f;

		return 1f;
	}

	// -----------------------------
	// Apply booster abilities (activated when some remaining PS)
	// -----------------------------
	private float applyBoostAbilitiesFromReducedPS(Pokemon attacker, Attack attack) {
		if (!attacker.isPSAtOrBelowOneThird())
			return 1f;

		if (attacker.hasOvergrowAbility()) // 65_Overgrow (grass)
			return boostIfType(attacker, attack, attack.isGrassType());

		if (attacker.hasBlazeAbility()) // 66_Blaze (fire)
			return boostIfType(attacker, attack, attack.isFireType());

		if (attacker.hasTorrentAbility()) // 67_Torrent (water)
			return boostIfType(attacker, attack, attack.isWaterType());

		if (attacker.hasSwarmAbility()) // 68_Swarm (bug)
			return boostIfType(attacker, attack, attack.isBugType());

		return 1f;
	}

	// -----------------------------
	// Only boost attack if same type
	// -----------------------------
	private float boostIfType(Pokemon attacker, Attack attack, boolean attackSameType) {
		if (!attackSameType)
			return 1f;

		System.out.println(attacker.getName() + " potenciado por habilidad " + attacker.getAbilitySelected().getName());
		return 1.5f;
	}

	// -----------------------------
	// Apply 79_Rivalry ability
	// -----------------------------
	private float applyRivalry(Pokemon attacker, Pokemon defender) {
		if (!attacker.hasRivalryAbility())
			return 1f;

		if (attacker.getSex() == defender.getSex())
			return 1.25f;
		else
			return 0.75f;
	}

	// -----------------------------
	// Apply 89_Iron_Fist ability
	// -----------------------------
	private float applyIronFist(Pokemon attacker, Attack attack) {
		if (!attacker.hasIronFistAbility())
			return 1f;

		if (attack.isPunchMove())
			return 1.2f;

		return 1f;
	}

	// -----------------------------
	// Apply 91_Adaptable ability
	// -----------------------------
	private float applyAdaptable(Pokemon attacker, Attack attack) {
		if (attacker.hasAdaptabilityAbility() && attacker.getTypes().contains(attack.getPkType()))
			return 1.75f;

		return 1f;
	}

	// -----------------------------
	// Apply general abilities concerning the attack of the Pokemon
	// -----------------------------
	private float applyPowerAttackModifiers(Pokemon attacker, Attack attack) {
		// 96_Normalize increase power 20% more
		if (attacker.hasNormalizeAbility())
			return 1.2f;

		// 125_Sheer_force rises power by 30% if attack has secondary effects
		if (attacker.hasSheerForceAbility() && attack.hasSecondaryEffect())
			// Increase power 30% more
			return 1.3f;

		return 1f;
	}

	// -----------------------------
	// Apply abilities depending on power level of the attack
	// -----------------------------
	private float applyPowerDependingPowerAttack(Pokemon attacker, Attack attack) {
		// 101_Technician ability
		if (attacker.hasTechnicianAbility() && attack.getPower() <= 60f)
			return 1.5f;

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
		// 109_Unaware ability => ignores stages from stats (but no modifiers on
		// abilities)
		boolean attackerHasUnaware = attacker.hasUnawareAbility();
		boolean defenderHasUnaware = defender.hasUnawareAbility();

		float attackStat = isSpecial
				? statService.getEffectiveSpecialAttack(attacker, defenderHasUnaware, ctx.getWeather())
				: statService.getEffectiveAttack(attacker, defenderHasUnaware, ctx.getWeather());

		float defenseStat = isSpecial
				? statService.getEffectiveSpecialDefense(defender, attackerHasUnaware, ctx.getWeather())
				: statService.getEffectiveDefense(defender, attackerHasUnaware);

		float base = (((0.2f * 100f + 1f) * attackStat * modifiedPower) / (25f * defenseStat) + 2f);

		float damage = 0.01f * attack.getBonus() * attack.getEffectivenessAgainstPkFacing() * weatherModifier
				* randomVariation * base;

		// 18_Flash_Fire boost ability
		if (attacker.isFireBoostActive() && attack.isFireType())
			damage *= 1.5f;

		return damage;
	}

	// -----------------------------
	// Apply critical damage by probabilities
	// -----------------------------
	private CriticalResult applyCriticalIfNeeded(Pokemon attacker, Attack attack, float damage, AttackContext ctx) {
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

		if (attacker.hasSniperAbility()) // 97_Sniper ability does *3 damage
			return new CriticalResult(damage * 3f, true);

		return new CriticalResult(damage * 2f, true);
	}

	// -----------------------------
	// Apply defensive abilities
	// -----------------------------
	private float applyDefensiveAbilities(Pokemon defender, Pokemon attacker, Attack attack, float damage) {
		// REDUCE DAMAGE
		// 47_Thick_Fat ability/ 85_Heatproof reduces damage by 2 (only if attack type
		// it's fire or ice type)
		if ((defender.hasThickFatAbility() && (attack.isFireType() || attack.isIceType()))
				|| (defender.hasHeatProofAbility() && attack.isFireType()))
			return damage / 2f;

		// 135_Multiscale reduces by 2 the damage if has all the PS
		// Informative : doesn't affect to attacks with fixed damage
		if (defender.hasMultiscaleAbility() && defender.hasMaxPS())
			return damage / 2f;

		// INCREASE DAMAGE
		// 87_Dry_Skin ability with a fire attack, do 25% more damage
		if (defender.hasDrySkinAbility() && attack.isFireType())
			return damage * 1.25f;

		return damage;
	}

	// -----------------------------
	// Adds multiplier depending on weather of the game
	// -----------------------------
	public float getWeatherModifier(AttackContext ctx) {
		Weather weather = ctx.getWeather();
		boolean isWeatherSuppresed = ctx.isWeatherSuppressed();

		if (isWeatherSuppresed)
			return 1.0f;

		if (weather == Weather.RAIN) {
			if (ctx.getAttack().isWaterType()) // Water
				return 1.5f;
			if (ctx.getAttack().isFireType()) // Fire
				return 0.5f;
		}

		if (weather == Weather.SUN) {
			if (ctx.getAttack().isFireType()) // Fire
				return 1.5f;
			if (ctx.getAttack().isWaterType()) // Water
				return 0.5f;
		}
		return 1.0f;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage) => 10% of probabilities
	// -----------------------------
	public boolean getCriticity(AttackContext ctx) {
		double randomCritic = Math.random() * 100d;

		randomCritic *= getCriticalIndexIfNeeded(ctx.getAttacker());

		// 10% of probabilities to have a critic attack
		if (randomCritic <= 10)
			return this.canReceiveCriticalAttacks(ctx);

		return false;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage) => 30% of probabilities
	// -----------------------------
	public boolean getHighCriticity30(AttackContext ctx) {
		double randomCritic = Math.random() * 100d;

		randomCritic *= getCriticalIndexIfNeeded(ctx.getAttacker());

		// 30% of probabilities to have a critic attack
		if (randomCritic <= 30)
			return this.canReceiveCriticalAttacks(ctx);

		return false;
	}

	// -----------------------------
	// Gets if an attack is critic (x2 of damage) => 40% of probabilities
	// -----------------------------
	public boolean getHighCriticity40(AttackContext ctx) {
		double randomCritic = Math.random() * 100d;

		randomCritic *= getCriticalIndexIfNeeded(ctx.getAttacker());

		// 40% of probabilities to have a critic attack
		if (randomCritic <= 40)
			return this.canReceiveCriticalAttacks(ctx);

		return false;
	}

	// -----------------------------
	// Rises probability of getting a critical attack if needed
	// -----------------------------
	public double getCriticalIndexIfNeeded(Pokemon attacker) {
		// 105_Super_Lock rises by 12,5% the probability of getting a critical attack
		// (index 1)
		if (attacker.hasSuperLockAbility())
			return 1d + (12.5d / 100d);

		return 1d;
	}

	// -----------------------------
	// Check if defender can receive critical attacks
	// -----------------------------
	public boolean canReceiveCriticalAttacks(AttackContext ctx) {
		// 4_Battle_Armor / 75_Shell_Armor cannot receive critic damage because of
		// ability
		if (ctx.getDefender().hasBattleArmorAbility() || ctx.getDefender().hasShellArmorAbility()) {
			System.out.println(ctx.getDefender().getName() + " no puede recibir ataques críticos dada su habilidad "
					+ ctx.getDefender().getAbilitySelected().getName());
			return false;
		}
		return true;
	}
}
