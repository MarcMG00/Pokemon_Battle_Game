package pokemon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pokemon.enums.Weather;

public class AbilityService {
	// -----------------------------
	// Selected an ability to each Pokemon from player
	// -----------------------------
	public void selectAbilityForEachPokemon(Player owner, ArrayList<Ability> abilities) {
		Random rand = new Random();

		for (Pokemon pk : owner.getPokemon()) {
			List<Ability> possibleAbilities = new ArrayList<>();

			// 1️ - Decides if takes hidden ability (20%)
			boolean tryHidden = rand.nextInt(100) < 20;

			if (tryHidden && pk.getHiddenAbilities() != null && !pk.getHiddenAbilities().isEmpty())
				possibleAbilities.addAll(pk.getHiddenAbilities());

			// 2️ - If empty hidden abilities or no random int => takes normal ability
			if (possibleAbilities.isEmpty() && pk.getNormalAbilities() != null && !pk.getNormalAbilities().isEmpty())
				possibleAbilities.addAll(pk.getNormalAbilities());

			// 3️ - Just in case if Pokemon has no abilities
			if (possibleAbilities.isEmpty() && pk.getHiddenAbilities() != null && !pk.getHiddenAbilities().isEmpty()) {
				possibleAbilities.addAll(pk.getHiddenAbilities());
			} else if (possibleAbilities.isEmpty()) {
				System.out.println(pk.getName() + " no tiene habilidades (ni normales, ni ocultas).");
				possibleAbilities.addAll(pk.getHiddenAbilities());
				continue;
			}

			// 4️ - Gets a random ability from his abilities
			Ability abilityFromPokemon = possibleAbilities.get(rand.nextInt(possibleAbilities.size()));

			// 5️ - Gets the real ability from global list
			Ability finalAbility = abilities.stream().filter(a -> a.getId() == abilityFromPokemon.getId()).findFirst()
					.orElse(null);

			if (finalAbility == null) {
				System.out.println("Habilidad no encontrada en catálogo global para " + pk.getName() + " (id="
						+ abilityFromPokemon.getId() + ")");
				continue;
			}

			Ability finalAbilityDeepCopy = new Ability(finalAbility);
			pk.setAbilitySelected(finalAbilityDeepCopy);
			pk.setBaseAbility(finalAbilityDeepCopy);

			System.out.println(
					pk.getName() + " (Id:" + pk.getId() + ")" + " obtuvo la habilidad: " + finalAbility.getName());
		}
	}

	// -----------------------------
	// Do start abilities (that are not weather type)
	// -----------------------------
	public void applyAbilities(BattleContext battleCtx, Pokemon p1, Pokemon p2) {
		boolean p1HasWeatherType = p1.getAbilitySelected().getIsWeatherType();
		boolean p2HasWeatherType = p2.getAbilitySelected().getIsWeatherType();

		if (p1HasWeatherType && p2HasWeatherType)
			return;

		if (!p1HasWeatherType)
			p1.getAbilitySelected().getEffect().onSwitchIn(battleCtx, p1, p2);

		if (!p2HasWeatherType)
			p2.getAbilitySelected().getEffect().onSwitchIn(battleCtx, p2, p1);
	}

	// -----------------------------
	// 42_Magnet_Pull ability doesn't allow to change Pokemon that are steel type
	// -----------------------------
	public boolean isBlockedByMagnetPull(BattleContext battleCtx, boolean isPlayer) {
		Player player = isPlayer ? battleCtx.getIa() : battleCtx.getPlayer();
		Pokemon pk = isPlayer ? battleCtx.getPlayer().getPkCombatting() : battleCtx.getIa().getPkCombatting();

		if (pk.hasMagnetPullAbility() && player.getPkCombatting().getTypes().stream().anyMatch(t -> t.getId() == 1)) {
			System.out.println(player.getPkCombatting().getName() + " (" + player.getPkCombatting().getId()
					+ ") no puede cambiarse a causa de la habilidad Imán del Pokémon rival");

			return true;
		}
		return false;
	}

	// -----------------------------
	// 71_Arena_Trap ability doesn't allow to change Pokemon (only if attacker is
	// not Fly type or has not the ability levitate or is not levitating)
	// -----------------------------
	public boolean isBlockedByArenaTrap(BattleContext battleCtx, boolean isPlayer) {
		Player player = isPlayer ? battleCtx.getIa() : battleCtx.getPlayer();
		Pokemon pk = isPlayer ? battleCtx.getPlayer().getPkCombatting() : battleCtx.getIa().getPkCombatting();

		if (pk.hasArenaTrapAbility() && (!player.getPkCombatting().getTypes().stream().anyMatch(t -> t.getId() == 18)
				|| player.getPkCombatting().hasLevitateAbility() || player.getPkCombatting().getIsLevitating())) {
			System.out.println(player.getPkCombatting().getName() + " (" + player.getPkCombatting().getId()
					+ ") no puede cambiarse a causa de la habilidad Trampa arena del Pokémon rival");

			return true;
		}
		return false;
	}

	// -----------------------------
	// Sets the ability during changes (forced or manual) (if any)
	// -----------------------------
	public void applyEntryAbilityOnSwitch(BattleContext battleCtx, Pokemon entering, Pokemon defender) {
		Ability abilityEntering = entering.getAbilitySelected();
		Ability abilityDefendering = defender.getAbilitySelected();

		if (abilityEntering == null || abilityEntering.getId() == 5000)
			return;

		abilityEntering.getEffect().onSwitchIn(battleCtx, entering, defender);

		// For example for 59_Foceast ability
		// If 36_Trace (copies ability) => needs to be applied
		abilityDefendering.getEffect().duringBattle(battleCtx, defender, entering);
	}

	// -----------------------------
	// Remove abilities effects before changing to new pokemon (ex : remove 13 Cloud
	// Nine)
	// -----------------------------
	public void applyExitAbilityOnSwitch(BattleContext battleCtx, Pokemon leaving) {
		Ability ability = leaving.getBaseAbility();

		if (ability == null || ability.getId() == 5000)
			return;

		ability.getEffect().onSwitchOut(battleCtx, leaving);
	}

	// -----------------------------
	// Apply abilities before the end of the turn
	// -----------------------------
	public void applyAbilitiesBeforeEndTurn(BattleContext battleCtx) {
		applyBeforeEndTurnAbility(battleCtx, true);
		applyBeforeEndTurnAbility(battleCtx, false);
	}

	// -----------------------------
	// Apply ability before end of turn
	// -----------------------------
	private void applyBeforeEndTurnAbility(BattleContext battleCtx, boolean isPlayer) {
		Pokemon pk = isPlayer ? battleCtx.getPlayer().getPkCombatting() : battleCtx.getIa().getPkCombatting();
		Ability ability = pk.getAbilitySelected();
		if (ability == null || ability.getId() == 5000 || (pk.getJustEnteredBattle() && !pk.hasShedSkinAbility()))
			return;

		ability.getEffect().beforeEndOfTurn(battleCtx, pk);
	}

	// -----------------------------
	// Apply abilities at the end of turn
	// -----------------------------
	public void applyEndTurnAbilities(BattleContext battleCtx) {
		applyEndTurnAbility(battleCtx, true);
		applyEndTurnAbility(battleCtx, false);
	}

	// -----------------------------
	// Apply ability on end turn
	// -----------------------------
	private void applyEndTurnAbility(BattleContext battleCtx, boolean isPlayer) {
		Pokemon pk = isPlayer ? battleCtx.getPlayer().getPkCombatting() : battleCtx.getIa().getPkCombatting();
		Ability ability = pk.getAbilitySelected();

		if (ability == null || ability.getId() == 5000 || (pk.getJustEnteredBattle() && !pk.hasRainDishAbility()))
			return;

		ability.getEffect().endOfTurn(battleCtx, pk);
	}

	// -----------------------------
	// Do ability effect after attacking
	// -----------------------------
	public void applyAbilityAfterDamage(Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			boolean isCriticalAttack, Weather weather, boolean isWeatherSuppressed) {

		// Attacker ability
		Ability attackerAbility = attacker.getAbilitySelected();

		// 54_Truant ability (can't do anything next round)
		if (attackerAbility != null && attackerAbility.getId() == 54) {
			System.out.println(attacker.getName() + " (" + attacker.getId() + ") "
					+ "no popdrá atacar o cambiarse en el siguiente turno a causa de "
					+ attacker.getAbilitySelected().getName());
			attacker.setCanDonAnythingNextRound(false);
		}

		// Damage must be done
		if (dmg <= 0)
			return;

		// Defender ability
		Ability defenderAbility = defender.getAbilitySelected();
		if (defenderAbility != null) {
			defenderAbility.getEffect().afterAttack(null, attacker, defender, attack, dmg, 0d, isCriticalAttack,
					weather, isWeatherSuppressed);
		}
	}

	// -----------------------------
	// Get priority points from speed (allows to know first Pokemon attacking)
	// -----------------------------
	public int getSpeedPriorityModifier(Pokemon pk) {
		if (pk.getAbilitySelected() == null)
			return 0;

		// 100_Stall ability => moves last
		if (pk.hasStallAbility())
			return -1;

		return 0;
	}
}
