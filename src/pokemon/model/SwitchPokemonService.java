package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import pokemon.enums.StatusConditions;

public class SwitchPokemonService {
	private final BattleContext battleCtx;
	private final StatusService statusService;
	private final AbilityService abilityService;

	public SwitchPokemonService(BattleContext battleCtx) {
		this.battleCtx = battleCtx;
		this.statusService = new StatusService();
		this.abilityService = new AbilityService();
	}

	// -----------------------------
	// Reset stats from Pokemon leaving
	// -----------------------------
	public void resetPokemonBeforeSwitch(Pokemon leaver) {
		abilityService.applyExitAbilityOnSwitch(battleCtx, leaver);

		leaver.setAttackStage(0);
		leaver.setSpecialAttackStage(0);
		leaver.setPrecisionStage(0);
		leaver.setDefenseStage(0);
		leaver.setSpecialDefenseStage(0);
		leaver.setSpeedStage(0);

		leaver.setLastUsedAttack(new Attack());
		leaver.getAbilitySelected().setAlreadyUsedOnEnter(false);
	}

	// -----------------------------
	// Update Pokemon facing when a new one is entering on combat
	// -----------------------------
	public void updatePkFacingAfterSwitch() {
		battleCtx.getPlayer().setPkFacing(battleCtx.getIa().getPkCombatting());
		battleCtx.getIa().setPkFacing(battleCtx.getPlayer().getPkCombatting());
	}

	// -----------------------------
	// Change Pokemon
	// -----------------------------
	public boolean changePokemon(Scanner sc) {
		while (true) {
			printChangeMenu();

			int id = readPokemonId(sc);

			if (isCancelSelection(id))
				return false;

			Optional<Pokemon> selectedOpt = findPokemonById(id);

			if (!isValidSelection(selectedOpt, id))
				continue;

			Pokemon selected = selectedOpt.get();

			if (!canPokemonBeSelected(selected))
				continue;

			performSwitch(selected);

			return true; // change successfully
		}
	}

	// -----------------------------
	// Print change menu
	// -----------------------------
	private void printChangeMenu() {
		System.out.println("\n--- Cambio de Pokémon ---");
		battleCtx.getPlayer().printPokemonInfo();
		System.out.println("Escribe el ID del Pokémon a usar o '0' para cancelar : ");
	}

	// -----------------------------
	// Read Pokemon ID
	// -----------------------------
	private int readPokemonId(Scanner sc) {
		int id = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");
		return id;
	}

	// -----------------------------
	// Check cancel selection
	// -----------------------------
	private boolean isCancelSelection(int id) {
		return id == 0;
	}

	// -----------------------------
	// Find Pokemon by ID
	// -----------------------------
	private Optional<Pokemon> findPokemonById(int id) {
		return battleCtx.getPlayer().getPokemon().stream().filter(p -> p.getId() == id).findFirst();
	}

	// -----------------------------
	// Validate selection existence and basic rules
	// -----------------------------
	private boolean isValidSelection(Optional<Pokemon> opt, int id) {
		if (isInvalidPokemonChoice(id))
			return false;

		if (opt.isEmpty()) {
			System.out.println("No escogiste un Pokémon válido. Escoge un Pokémon de los que posees :");
			return false;
		}
		return true;
	}

	// -----------------------------
	// Check if Pokemon can be selected
	// -----------------------------
	private boolean canPokemonBeSelected(Pokemon selected) {
		if (selected.isFainted()) {
			System.out.println(selected.getName() + " (Id:" + selected.getId() + ") fue debilitado. Escoge otro.");
			return false;
		}
		return true;
	}

	// -----------------------------
	// Perform Pokemon switch
	// -----------------------------
	private void performSwitch(Pokemon selected) {
		Pokemon leaver = battleCtx.getPlayer().getPkCombatting();

		resetPokemonBeforeSwitch(leaver);

		// Remove drained ALL STATUS state (cause player changed)
		statusService.clearDrainEffects(leaver, battleCtx.getIa().getPkCombatting());

		System.out.println("Jugador eligió a " + selected.getName());

		// Update Pokemon combating
		selected.setJustEnteredBattle(true);
		battleCtx.getPlayer().setPkCombatting(selected);

		updatePkFacingAfterSwitch();

		// Update weather ability if any
		abilityService.applyEntryAbilityOnSwitch(battleCtx, selected, battleCtx.getIa().getPkCombatting());

		refreshAttackOrders();
	}

	// -----------------------------
	// Chech Pokemon selected is not the one already on the field
	// -----------------------------
	private boolean isInvalidPokemonChoice(int id) {
		if (battleCtx.getPlayer().getPkCombatting().getId() == id && !battleCtx.getIa().getPkCombatting().isFainted()) {
			System.out.println("Ese Pokémon ya está combatiendo.");
			return true;
		}
		return false;
	}

	// -----------------------------
	// Put attacks from damage level
	// -----------------------------
	public void refreshAttackOrders() {
		AttackAnalyzer.orderAttacksByDamage(battleCtx.getIa().getPkCombatting(), battleCtx.getIa().getPkFacing(),
				battleCtx.getEffectPerTypes());
		AttackAnalyzer.orderAttacksByDamage(battleCtx.getPlayer().getPkCombatting(),
				battleCtx.getPlayer().getPkFacing(), battleCtx.getEffectPerTypes());
	}

	// -----------------------------
	// Try IA to change Pokemon. Return true if IA changed Pokemon. If return false,
	// will attack normally
	// -----------------------------
	public boolean tryIAChange() {
		if (abilityService.isBlockedByMagnetPull(battleCtx, true))
			return false;

		if (abilityService.isBlockedByArenaTrap(battleCtx, true))
			return false;

		// 15% of probability to change Pokemon
		int randomNumber = (int) (Math.random() * 100) + 1;

		if (randomNumber > 15) {
			System.out.println("IA no cambiará (probabilidad muy baja)");
			return false; // don't change
		}

		// Check from others Pokemon from the team to see a potential better option
		Pokemon changeTo = this.decideBestChangePokemon(battleCtx.getIa(), battleCtx.getPlayer().getPkCombatting(),
				battleCtx.getEffectPerTypes());

		if (changeTo == null) {
			System.out.println("IA no tiene un mejor Pokémon al que cambiar");
			return false; // doesn't exists a better option
		}

		resetPokemonBeforeSwitch(battleCtx.getIa().getPkCombatting());

		System.out.println("IA cambió a " + changeTo.getName());

		battleCtx.getIa().setPkCombatting(changeTo);
		battleCtx.getIa().getPkCombatting().setJustEnteredBattle(true);

		updatePkFacingAfterSwitch();

		refreshAttackOrders();

		return true;
	}

	// -----------------------------
	// Handle if a player has to change to another random Pokemon because of
	// "Whirlwind" or "Roar", etc.
	// -----------------------------
	public void handleForcedSwitch(Player defender) {
		Pokemon pkCombating = defender.getPkCombatting();
		Pokemon pkFacing = defender.getPkFacing();

		statusService.clearDrainEffects(pkCombating, pkFacing);

		// Get available Pokemon
		List<Pokemon> pkAvailable = getAvailablePokemonForSwitch(defender);

		if (pkAvailable.isEmpty()) {
			defender.setForceSwitchPokemon(false);
			return; // Cannot change => does not anything
		}

		Pokemon newPkEntering = chooseRandomPokemon(pkAvailable);

		printForcedSwitchMessage(defender, newPkEntering);

		performForcedSwitch(battleCtx, defender, newPkEntering);

		defender.setForceSwitchPokemon(false);
	}

	// -----------------------------
	// Get available Pokemon for forced switch
	// -----------------------------
	private List<Pokemon> getAvailablePokemonForSwitch(Player defender) {
		return defender.getPokemon().stream().filter(p -> !p.isFainted() && p != defender.getPkCombatting()).toList();
	}

	// -----------------------------
	// Choose random Pokemon
	// -----------------------------
	private Pokemon chooseRandomPokemon(List<Pokemon> pokemons) {
		return pokemons.get((int) (Math.random() * pokemons.size()));
	}

	// -----------------------------
	// Print forced switch messages
	// -----------------------------
	private void printForcedSwitchMessage(Player defender, Pokemon newPkEntering) {
		System.out.println(defender.getPkCombatting().getName() + " fue expulsado por "
				+ defender.getPkFacing().getNextMovement().getName() + ".");

		boolean isPlayer = defender == battleCtx.getPlayer();

		System.out.println((isPlayer ? "Jugador" : "IA") + " envía a " + newPkEntering.getName() + " (Id:"
				+ newPkEntering.getId() + ")");
	}

	// -----------------------------
	// Perform forced switch
	// -----------------------------
	private void performForcedSwitch(BattleContext battleCtx, Player defender, Pokemon newPkEntering) {
		Pokemon leaver = defender.getPkCombatting();

		resetPokemonBeforeSwitch(leaver);

		newPkEntering.setJustEnteredBattle(true);
		defender.setPkCombatting(newPkEntering);

		abilityService.applyEntryAbilityOnSwitch(battleCtx, newPkEntering, getOpponent(defender).getPkCombatting());

		updateFacingAfterForcedSwitch(defender, newPkEntering);
	}

	// -----------------------------
	// Update facing after forced switch
	// -----------------------------
	private void updateFacingAfterForcedSwitch(Player defender, Pokemon newPkEntering) {
		if (defender == battleCtx.getPlayer()) {
			battleCtx.getIa().setPkFacing(newPkEntering);
			battleCtx.getPlayer().setPkFacing(battleCtx.getIa().getPkCombatting());
		} else {
			battleCtx.getPlayer().setPkFacing(newPkEntering);
			battleCtx.getIa().setPkFacing(battleCtx.getPlayer().getPkCombatting());
		}
	}

	// -----------------------------
	// Get opponent player
	// -----------------------------
	private Player getOpponent(Player player) {
		return player == battleCtx.getPlayer() ? battleCtx.getIa() : battleCtx.getPlayer();
	}

	// -----------------------------
	// Decides best Pokemon change against Pokemon facing
	// -----------------------------
	public Pokemon decideBestChangePokemon(Player player, Pokemon pkPlayerFacing,
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {

		// 1️ - Random check (15% of probability to change)
		int random = (int) (Math.random() * 100) + 1;
		if (random > 15) {
			return null; // don't change
		}

		Pokemon currentPkCombatingBeforeChange = player.getPkCombatting();

		// 2️ - Analyze actual attacks from Pokemon combating
		AttackAnalyzer.orderAttacksByDamage(player.getPkCombatting(), pkPlayerFacing, effectPerTypes);
		int currentBestDamage = getDamageScore(currentPkCombatingBeforeChange);

		// 3️ - Search other Pokemon form the team (not including Pokemon combating)
		Pokemon bestCandidate = null;
		int bestScore = currentBestDamage;

		// Get only Pokemon not debilitated
		List<Pokemon> pokemonAvailable = player.getPokemon().stream()
				.filter(pk -> pk.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED).toList();

		for (Pokemon candidate : pokemonAvailable) {

			if (candidate == currentPkCombatingBeforeChange)
				continue;

			// Ordenate attacks from the candidate Pokemon vs Pokemon facing
			player.setPkCombatting(candidate);
			player.setPkFacing(pkPlayerFacing);
			AttackAnalyzer.orderAttacksByDamage(player.getPkCombatting(), pkPlayerFacing, effectPerTypes);

			int score = getDamageScore(candidate);

			// Rule 1: Pokemon with STAB super effective
			if (hasSTABSuperEffective(candidate)) {
				return candidate;
			}

			// Rule 2: Pokemon with a very high damage but no STAB
			if (score > bestScore) {
				bestScore = score;
				bestCandidate = candidate;
			}
		}

		// Restore real battler before returning!
		player.setPkCombatting(currentPkCombatingBeforeChange);

		// 4️ - Apply rule 2 if founded something better than actual Pokemon
		if (bestCandidate != null && bestScore > currentBestDamage) {
			return bestCandidate;
		}

		// 5️ - If nothing founded, don't change
		return null;
	}

	// -----------------------------
	// Get damage score from Pokemon candidate
	// -----------------------------
	private int getDamageScore(Pokemon pk) {
		if (!pk.getLotDamageAttacks().isEmpty())
			return 3; // very high damage
		if (!pk.getNormalAttacks().isEmpty())
			return 2; // normal damage
		if (!pk.getLowAttacks().isEmpty())
			return 1; // low damage

		return 0; // no effect
	}

	// -----------------------------
	// Check if an attack has STAB super effective
	// -----------------------------
	private boolean hasSTABSuperEffective(Pokemon pk) {

		for (Attack atk : pk.getLotDamageAttacks()) {

			// "same type" (STAB)
			if (pk.getTypes().contains(atk.getStrTypeToPkType())) {
				return true;
			}
		}

		return false;
	}
}
