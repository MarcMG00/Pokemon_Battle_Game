package pokemon.model;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
	public void resetPokemonBeforeSwitch(Pokemon pk) {
		abilityService.applyExitAbilityOnSwitch(battleCtx, pk);

		pk.setAttackStage(0);
		pk.setSpecialAttackStage(0);
		pk.setPrecisionStage(0);
		pk.setDefenseStage(0);
		pk.setSpecialDefenseStage(0);
		pk.setSpeedStage(0);

		pk.setLastUsedAttack(new Attack());
		pk.getAbilitySelected().setAlreadyUsedOnEnter(false);
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
		if (selected.isDebilitated()) {
			System.out.println(selected.getName() + " (Id:" + selected.getId() + ") fue debilitado. Escoge otro.");
			return false;
		}
		return true;
	}

	// -----------------------------
	// Perform Pokemon switch
	// -----------------------------
	private void performSwitch(Pokemon selected) {
		Pokemon current = battleCtx.getPlayer().getPkCombatting();

		resetPokemonBeforeSwitch(current);

		// Remove drained ALL STATUS state (cause player changed)
		statusService.clearDrainEffects(current, battleCtx.getIa().getPkCombatting());

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
		if (battleCtx.getPlayer().getPkCombatting().getId() == id
				&& !battleCtx.getIa().getPkCombatting().isDebilitated()) {
			System.out.println("Ese Pokémon ya está combatiendo.");
			return true;
		}
		return false;
	}

	// -----------------------------
	// Put attacks from damage level
	// -----------------------------
	public void refreshAttackOrders() {
		battleCtx.getIa().orderAttacksFromDammageLevelPokemon(battleCtx.getEffectPerTypes());
		battleCtx.getPlayer().orderAttacksFromDammageLevelPokemon(battleCtx.getEffectPerTypes());
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
		Pokemon changeTo = battleCtx.getIa().decideBestChangePokemon(battleCtx.getPlayer().getPkCombatting(),
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
		List<Pokemon> available = getAvailablePokemonForSwitch(defender);

		if (available.isEmpty()) {
			defender.setForceSwitchPokemon(false);
			return; // Cannot change => does not anything
		}

		Pokemon newPk = chooseRandomPokemon(available);

		printForcedSwitchMessage(defender, newPk);

		performForcedSwitch(battleCtx, defender, newPk);

		defender.setForceSwitchPokemon(false);
	}

	// -----------------------------
	// Get available Pokemon for forced switch
	// -----------------------------
	private List<Pokemon> getAvailablePokemonForSwitch(Player defender) {
		return defender.getPokemon().stream().filter(p -> !p.isDebilitated() && p != defender.getPkCombatting())
				.toList();
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
	private void printForcedSwitchMessage(Player defender, Pokemon newPk) {
		System.out.println(defender.getPkCombatting().getName() + " fue expulsado por "
				+ defender.getPkFacing().getNextMovement().getName() + ".");

		boolean isPlayer = defender == battleCtx.getPlayer();

		System.out
				.println((isPlayer ? "Jugador" : "IA") + " envía a " + newPk.getName() + " (Id:" + newPk.getId() + ")");
	}

	// -----------------------------
	// Perform forced switch
	// -----------------------------
	private void performForcedSwitch(BattleContext battleCtx, Player defender, Pokemon newPk) {
		Pokemon current = defender.getPkCombatting();

		resetPokemonBeforeSwitch(current);

		newPk.setJustEnteredBattle(true);
		defender.setPkCombatting(newPk);

		abilityService.applyEntryAbilityOnSwitch(battleCtx, newPk, getOpponent(defender).getPkCombatting());

		updateFacingAfterForcedSwitch(defender, newPk);
	}

	// -----------------------------
	// Update facing after forced switch
	// -----------------------------
	private void updateFacingAfterForcedSwitch(Player defender, Pokemon newPk) {
		if (defender == battleCtx.getPlayer()) {
			battleCtx.getIa().setPkFacing(newPk);
			battleCtx.getPlayer().setPkFacing(battleCtx.getIa().getPkCombatting());
		} else {
			battleCtx.getPlayer().setPkFacing(newPk);
			battleCtx.getIa().setPkFacing(battleCtx.getPlayer().getPkCombatting());
		}
	}

	// -----------------------------
	// Get opponent player
	// -----------------------------
	private Player getOpponent(Player player) {
		return player == battleCtx.getPlayer() ? battleCtx.getIa() : battleCtx.getPlayer();
	}
}
