package pokemon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pokemon.abilityInterface.AbilityEffect;
import pokemon.enums.Weather;

public class AbilityService {
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	private static final String ANSI_RESET = "\u001B[0m";

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

				// Initialize an empty ability
				Ability emptyAbility = new Ability();
				pk.setAbilitySelected(emptyAbility);
				pk.setBaseAbility(emptyAbility);

				continue;
			}

			Ability finalAbilityDeepCopy = new Ability(finalAbility);
			// Put the effect of the ability to Pokemon => unicity for each Pokemon (each
			// Pokemon is owner of the effect)
			AbilityEffect effect = AbilityEffectFactory.createEffect(finalAbilityDeepCopy, pk);
			finalAbilityDeepCopy.setEffect(effect);

			pk.setAbilitySelected(finalAbilityDeepCopy);
			pk.setBaseAbility(finalAbilityDeepCopy);

			System.out.println(
					pk.getName() + " (Id:" + pk.getId() + ")" + " obtuvo la habilidad: " + finalAbility.getName());
		}
	}

	// -----------------------------
	// Apply abilities on battle start (handle by speed order)
	// -----------------------------
	public void resolveEntryAbilities(BattleContext battleCtx) {
		Pokemon pkPlayer = battleCtx.getPkPlayer();
		Pokemon pkIA = battleCtx.getPkIA();

		for (Pokemon pokemon : getEntryOrder(pkPlayer, pkIA)) {
			Pokemon opponent = pokemon == pkPlayer ? pkIA : pkPlayer;

			pokemon.getAbilitySelected().getEffect().onSwitchIn(battleCtx, opponent);
		}
	}

	// -----------------------------
	// Get ability Pokemon order execution
	// -----------------------------
	private List<Pokemon> getEntryOrder(Pokemon pkPlayer, Pokemon pkIA) {

		if (pkPlayer.getSpeed() > pkIA.getSpeed())
			return List.of(pkPlayer, pkIA);

		return List.of(pkIA, pkPlayer);
	}

	// -----------------------------
	// Apply abilities on battle start (that are not weather type)
	// -----------------------------
	public void applyAbilitiesStartBattle(BattleContext battleCtx) {
		Pokemon pkPlayer = battleCtx.getPkPlayer();
		Pokemon pkIA = battleCtx.getPkIA();

		boolean pkPlayerHasWeatherType = pkPlayer.getAbilitySelected().isWeatherType();
		boolean pkIAHasWeatherType = pkIA.getAbilitySelected().isWeatherType();

		if (pkPlayerHasWeatherType && pkIAHasWeatherType)
			return;

		if (!pkPlayerHasWeatherType && pkIAHasWeatherType) {
			pkPlayer.getAbilitySelected().getEffect().onSwitchIn(battleCtx, battleCtx.getPkIA());
			return;
		}

		if (!pkIAHasWeatherType && pkPlayerHasWeatherType) {
			pkIA.getAbilitySelected().getEffect().onSwitchIn(battleCtx, battleCtx.getPkPlayer());
			return;
		}

		// Both have normal abilities => slower wins
		Pokemon slower = pkPlayer.getSpeed() <= pkIA.getSpeed() ? pkPlayer : pkIA;
		Pokemon faster = pkPlayer.getSpeed() > pkIA.getSpeed() ? pkPlayer : pkIA;

		faster.getAbilitySelected().getEffect().onSwitchIn(battleCtx, slower);
		slower.getAbilitySelected().getEffect().onSwitchIn(battleCtx, faster);
	}

	// -----------------------------
	// 42_Magnet_Pull ability doesn't allow to change Pokemon that are steel type
	// -----------------------------
	public boolean isBlockedByMagnetPull(BattleContext battleCtx, boolean isPlayer) {
		Player player = isPlayer ? battleCtx.getIa() : battleCtx.getPlayer();
		Pokemon pk = isPlayer ? battleCtx.getPkPlayer() : battleCtx.getPkIA();

		if (pk.hasMagnetPullAbility() && player.getPkCombatting().getTypes().stream().anyMatch(t -> t.isSteelType())) {
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
		Pokemon pkDefender = player.getPkCombatting();
		Pokemon pkBlocking = isPlayer ? battleCtx.getPkPlayer() : battleCtx.getPkIA();

		boolean pkDefenderIsFlyigType = pkDefender.getTypes().stream().anyMatch(t -> t.isFlyingType());
		boolean pkDefenderIsLevitating = pkDefender.hasLevitateAbility() || pkDefender.isLevitating();

		if (pkBlocking.hasArenaTrapAbility() && (!pkDefenderIsFlyigType || pkDefenderIsLevitating)) {
			System.out.println(player.getPkCombatting().getName() + " (" + player.getPkCombatting().getId()
					+ ") no puede cambiarse a causa de la habilidad Trampa arena del Pokémon rival");

			return true;
		}
		return false;
	}

	// -----------------------------
	// Apply abilities when switching Pokemon if needed
	// -----------------------------
	public void applyAbilityOnSwitchInIfNeeded(BattleContext battleCtx, Pokemon pkEntering, Pokemon defender) {
		Ability abilityEntering = pkEntering.getAbilitySelected();
		Ability abilityDefendering = defender.getAbilitySelected();

		if (abilityEntering.getId() == 5000)
			return;

		abilityEntering.getEffect().onSwitchIn(battleCtx, defender);

		// For example for 59_Foceast ability
		// If 36_Trace (copies ability) => needs to be applied
		abilityDefendering.getEffect().duringBattle(battleCtx, pkEntering);
	}

	// -----------------------------
	// Remove abilities effects before changing to new pokemon (ex : remove
	// 13_Clou_Nine or reset abilities like Plus/Minus or get more PS with
	// Regenerator, etc.)
	// -----------------------------
	public void applyAbilityOnSwitchOutIfNeeded(BattleContext battleCtx, Pokemon leaver) {
		Ability ability = leaver.getBaseAbility();

		if (ability.getId() == 5000)
			return;

		// Do switch out - ability
		ability.getEffect().onSwitchOut(battleCtx);
		// Do switch out condition - ability => for now, it only can be applied if
		// hasn't faint
		if (!leaver.hasFainted())
			ability.getEffect().onSwitchOutCondition(battleCtx);
	}

	// -----------------------------
	// Apply abilities before the end of the turn (both players)
	// -----------------------------
	public void applyAbilitiesBeforeEndTurn(BattleContext battleCtx, boolean playerAttacksFirst) {
		if (playerAttacksFirst) {
			applyAbilityBeforeEndTurnIfNeeded(battleCtx, true);
			applyAbilityBeforeEndTurnIfNeeded(battleCtx, false);
		} else {
			applyAbilityBeforeEndTurnIfNeeded(battleCtx, false);
			applyAbilityBeforeEndTurnIfNeeded(battleCtx, true);
		}
	}

	// -----------------------------
	// Apply abilities before the end of the turn (only IA)
	// -----------------------------
	public void applyIAAbilitiesBeforeEndTurnIfNeeded(BattleContext battleCtx) {
		applyAbilityBeforeEndTurnIfNeeded(battleCtx, false);
	}

	// -----------------------------
	// Apply ability before end of the turn
	// -----------------------------
	private void applyAbilityBeforeEndTurnIfNeeded(BattleContext battleCtx, boolean isPlayer) {
		Pokemon pk = isPlayer ? battleCtx.getPkPlayer() : battleCtx.getPkIA();
		Ability ability = pk.getAbilitySelected();

		if (ability.getId() == 5000)
			return;

		ability.getEffect().beforeEndOfTurn(battleCtx);
	}

	// -----------------------------
	// Apply abilities at the end of the turn (both players)
	// -----------------------------
	public void applyEndTurnAbilitiesIfNeeded(BattleContext battleCtx, boolean playerAttacksFirst) {
		if (playerAttacksFirst) {
			applyEndTurnAbilityIfNeeded(battleCtx, true);
			applyEndTurnAbilityIfNeeded(battleCtx, false);
		} else {
			applyEndTurnAbilityIfNeeded(battleCtx, false);
			applyEndTurnAbilityIfNeeded(battleCtx, true);
		}
	}

	// -----------------------------
	// Apply abilities at the end of the turn (only IA)
	// -----------------------------
	public void applyIAEndTurnAbilitiesIfNeeded(BattleContext battleCtx) {
		applyEndTurnAbilityIfNeeded(battleCtx, false);
	}

	// -----------------------------
	// Apply ability on end of the turn
	// -----------------------------
	private void applyEndTurnAbilityIfNeeded(BattleContext battleCtx, boolean isPlayer) {
		Pokemon pk = isPlayer ? battleCtx.getPkPlayer() : battleCtx.getPkIA();
		Ability ability = pk.getAbilitySelected();

		if (pk.hasFainted())
			return;

		if (ability.getId() == 5000)
			return;

		ability.getEffect().endOfTurn(battleCtx);
	}

	// -----------------------------
	// Apply ability from defender (end of the attack from attacker)
	// -----------------------------
	public void applyAbilityAfterDamageIfNeeded(Pokemon attacker, Pokemon defender, Attack attack, float dmg,
			boolean isCriticalAttack, Weather weather, boolean isWeatherSuppressed) {
		handleTruantAbility(attacker);

		// Damage must be done
		if (dmg <= 0)
			return;

		Ability defenderAbility = defender.getAbilitySelected();
		defenderAbility.getEffect().afterAttack(null, attacker, defender, attack, dmg, 0d, isCriticalAttack, weather,
				isWeatherSuppressed);
	}

	// -----------------------------
	// Handle 54_Truant ability effect if needed
	// -----------------------------
	public void handleTruantAbility(Pokemon attacker) {
		// 54_Truant ability => can't do anything next round
		if (attacker.hasTruantAbility()) {
			System.out.println(attacker.getName() + " (" + attacker.getId() + ") "
					+ "no popdrá atacar o cambiarse en el siguiente turno a causa de "
					+ attacker.getAbilitySelected().getName());
			attacker.setCanDonAnythingNextRound(false);
		}
	}

	// -----------------------------
	// Get priority points (allows to know first Pokemon attacking)
	// -----------------------------
	public int getPriorityModifier(Pokemon pk) {
		int modifier = 0;

		// 100_Stall ability => moves last
		if (pk.hasStallAbility())
			modifier -= 1;

		return modifier;
	}
}
