package pokemon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Player {
	private ArrayList<Pokemon> pokemons;
	private Pokemon pkCombatting;
	private Pokemon pkFacing;
	private Ataque nextAttack;

	public Player() {
		super();
		this.pokemons = new ArrayList<>();
		this.pkCombatting = new Pokemon();
		this.nextAttack = new Ataque();
		this.pkFacing = new Pokemon();
	}

	public ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setPokemons(ArrayList<Pokemon> pokemons) {
		this.pokemons = pokemons;
	}

	public Pokemon getPkCombatting() {
		return pkCombatting;
	}

	public void setPkCombatting(Pokemon pkCombatting) {
		this.pkCombatting = pkCombatting;
	}

	public Ataque getNextAttack() {
		return nextAttack;
	}

	public void setNextAttack(Ataque nextAttack) {
		this.nextAttack = nextAttack;
	}

	// Adds Pokemon to Pokemon player
	public void addPokemon(Pokemon pk) {
		this.pokemons.add(pk);
	}

	public Pokemon getPkFacing() {
		return pkFacing;
	}

	public void setPkFacing(Pokemon pkFacing) {
		this.pkFacing = pkFacing;
	}

	// Adds 4 attacs to a Pokemon (1 other, 2 fisics, 1 special)
	public void addAtacsForEackPokemon() {
		for (Pokemon pk : this.pokemons) {
			System.out.println(pk.getNombrePokemon());
			Random rand = new Random();
			pk.addAtaques(pk.getAtaEstado().get(rand.nextInt(pk.getAtaEstado().size())));

			for (int times = 0; times < 2; times++) {
				rand = new Random();
				pk.addAtaques(pk.getAtaFisicos().get(rand.nextInt(pk.getAtaFisicos().size())));
			}

			rand = new Random();
			pk.addAtaques(pk.getAtaEspeciales().get(rand.nextInt(pk.getAtaEspeciales().size())));
			System.out.println("fin PK");
		}
	}

	// Puts in a Map the number of times that appears the element
	@SuppressWarnings("unused")
	private Map<String, Long> countDuplicates(List<String> list) {
		Map<String, Long> couterMap;
		return couterMap = list.stream().collect(Collectors.groupingBy(e -> e.toString(), Collectors.counting()));
	}

	// Order all the attacks by damage level against the Pokemon facing
	public void orderAttacksFromDammageLevelPokemonIA(Pokemon pkPlayer, HashMap<String, ArrayList<Pokemon>> pokemonsPorTipo,
			HashMap<String, HashMap<String, ArrayList<PokemonType>>> efectoPorTipos) {
		// Gets the Pokemon types that player is currently facing
		ArrayList<PokemonType> pkFacing = pkPlayer.getTypes();

		// Copy all the effects for each type of the current Pokemon player
		HashMap<String, HashMap<String, ArrayList<PokemonType>>> efectoPorTiposCopy = new HashMap<>();

		// Vars that put the attacks by their level of damage
		ArrayList<Ataque> iaAtaquesRebientan = new ArrayList<>();
		ArrayList<Ataque> iaAtaquesNormales = new ArrayList<>();
		ArrayList<Ataque> iaAtaquesDebiles = new ArrayList<>();
		ArrayList<Ataque> iaAtaquesNoAfectan = new ArrayList<>();
		// Puts the diferent types of the attacks in a list without duplicates
		ArrayList<PokemonType> tipoAtaquesNoRepes = new ArrayList<>();

		// Gets information about the type of Pokemon (diferent damages...)
		Map<String, HashMap<String, ArrayList<PokemonType>>> efectoPorTiposFiltered;
		// Types that doesn't hurt the pokemon facing
		ArrayList<PokemonType> listaNoAfecta = new ArrayList<>();
		
		// Vars to get the deiferent information for each type of the pokemon facing
		List<String> tiposRepetidosArrebientan = new ArrayList<>();
		List<String> tiposRepetidosNormales = new ArrayList<>();
		List<String> tiposRepetidosDebiles = new ArrayList<>();

		// Puts the types of IA Pokemon attacking in the list (without duplicates)
		for (Ataque a : this.getPkCombatting().getCuatroAtaques()) {
			if (!tipoAtaquesNoRepes.contains(a.getStrTipoToPkType())) {
				tipoAtaquesNoRepes.add(a.getStrTipoToPkType());
			}
		}

		for (int iType = 0; iType < pkFacing.size(); iType++) {
			int iTypeFinal = iType;
			// Filters in a new Map all the damages of the Pokemon facing from the picked type
			efectoPorTiposFiltered = efectoPorTipos.entrySet().stream()
					.filter(ef -> ef.getKey().toUpperCase().equals(pkPlayer.getTypes().get(iTypeFinal).getNombreTipo()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			// Put the results in a new Map
			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> ef : efectoPorTiposFiltered.entrySet()) {
				efectoPorTiposCopy.put(ef.getKey(), ef.getValue());
			}
		}

		// If Pokemon facing has 2 types
		if (efectoPorTiposCopy.size() == 2) {
			// Each value in "efectoPorTiposCopy" corresponds a type of pokemon facing
			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> ef : efectoPorTiposCopy.entrySet()) {
				// Put only types that don't hurt
				if (ef.getValue().containsKey("No tiene efecto") && ef.getValue().get("No tiene efecto").size() != 0) {
					for (PokemonType tNoAfecta : ef.getValue().get("No tiene efecto")) {
						if (!listaNoAfecta.contains(tNoAfecta)) {
							listaNoAfecta.add(tNoAfecta);
						}
					}
				}
				
				// Put only types that hurts a lot
				if (ef.getValue().containsKey("Le rebientan") && ef.getValue().get("Le rebientan").size() != 0) {
					for (PokemonType tArrebienta : ef.getValue().get("Le rebientan")) {
						tiposRepetidosArrebientan.add(tArrebienta.getNombreTipo().toUpperCase());
					}
				}

				// Put only types that hurt a little
				if (ef.getValue().containsKey("Le Rebientan poco")
						&& ef.getValue().get("Le Rebientan poco").size() != 0) {
					for (PokemonType tArrebienta : ef.getValue().get("Le Rebientan poco")) {
						tiposRepetidosDebiles.add(tArrebienta.getNombreTipo().toUpperCase());
					}
				}

				// Put normal attacks (don't hurt a lot, but are not the weakest)
				for (PokemonType pAta : tipoAtaquesNoRepes) {
					if (((ef.getValue().containsKey("Le rebientan")
							&& !ef.getValue().get("Le rebientan").contains(pAta))
							|| (ef.getValue().containsKey("Le Rebientan poco")
									&& !ef.getValue().get("Le Rebientan poco").contains(pAta)))
							&& (!listaNoAfecta.contains(pAta))) {
						tiposRepetidosNormales.add(pAta.getNombreTipo().toUpperCase());
					}
				}
			}

			// Counts how many times appear one type in a list
			System.out.println();
			Map<String, Long> tiposRepetidosRebientanFinal = countDuplicates(tiposRepetidosArrebientan);
			System.out.println("tipos rebientan si o si : " + tiposRepetidosRebientanFinal);

			Map<String, Long> tiposRepetidosDebilesFinal = countDuplicates(tiposRepetidosDebiles);
			System.out.println("tipos debiles : " + tiposRepetidosDebilesFinal);

			Map<String, Long> tiposRepetidosNormalesFinal = countDuplicates(tiposRepetidosNormales);
			System.out.println("tipos normales a los dos : " + tiposRepetidosNormalesFinal);

			System.out.println("no afecta a charizard :");
			for (PokemonType pkt : listaNoAfecta) {
				System.out.println(pkt.getNombreTipo());
			}
			System.out.println();
			
			for (Ataque finalAttack : this.getPkCombatting().getCuatroAtaques()) {
				// don't put the attack twice a time in a diferent list
				boolean isPicked = false;
				// Attack doesn't hurt if it is in the list "no afecta"
				if (listaNoAfecta.contains(finalAttack.getStrTipoToPkType())
						&& !iaAtaquesNoAfectan.contains(finalAttack)) {
					iaAtaquesNoAfectan.add(finalAttack);
				} else {
					// If type attack appears twice in list "Rebientan", it affects both types from Pokemon facing
					for (Map.Entry<String, Long> key : tiposRepetidosRebientanFinal.entrySet()) {
						if (key.getKey().equals(finalAttack.getStrTipoToPkType().getNombreTipo().toUpperCase())
								&& key.getValue() == 2 && !iaAtaquesRebientan.contains(finalAttack)) {
							iaAtaquesRebientan.add(finalAttack);
							isPicked = true;
						}
					}
					// If type attack appears twice in list "Debiles", it affects both types from Pokemon facing
					for (Map.Entry<String, Long> key : tiposRepetidosDebilesFinal.entrySet()) {
						if (key.getKey().equals(finalAttack.getStrTipoToPkType().getNombreTipo().toUpperCase())
								&& key.getValue() == 2 && !iaAtaquesDebiles.contains(finalAttack)) {
							iaAtaquesDebiles.add(finalAttack);
							isPicked = true;
						}
					}
					// If type is in list "Debiles", it doesn't matter if it hurts a lot to one type from Pokemon facing
					// still doesn't hurts the other type, so it is a weak attack
					if (!isPicked) {
						if (tiposRepetidosDebilesFinal
								.containsKey(finalAttack.getStrTipoToPkType().getNombreTipo().toUpperCase())
								&& !iaAtaquesDebiles.contains(finalAttack)) {
							iaAtaquesDebiles.add(finalAttack);
						}
						// If type appears in "Rebienta" and in normal, it ascends to "hurts a lot"
						else if (tiposRepetidosRebientanFinal
								.containsKey(finalAttack.getStrTipoToPkType().getNombreTipo().toUpperCase())
								&& tiposRepetidosNormales
										.contains(finalAttack.getStrTipoToPkType().getNombreTipo().toUpperCase())
								&& !iaAtaquesRebientan.contains(finalAttack)) {
							iaAtaquesRebientan.add(finalAttack);
						}
						// Else it is normal
						else {
							if (!iaAtaquesNormales.contains(finalAttack)) {
								iaAtaquesNormales.add(finalAttack);
							}
						}
					}
				}
			}
		}
		// If Pokemon facing has 1 type
		else {
			for (Map.Entry<String, HashMap<String, ArrayList<PokemonType>>> ef : efectoPorTiposCopy.entrySet()) {
				for (Ataque finalAttack : this.getPkCombatting().getCuatroAtaques()) {
					if (ef.getValue().get("Le rebientan").contains(finalAttack.getStrTipoToPkType())
							&& !iaAtaquesRebientan.contains(finalAttack)
							&& !listaNoAfecta.contains(finalAttack.getStrTipoToPkType())) {
						iaAtaquesRebientan.add(finalAttack);
					} else if (ef.getValue().get("Le Rebientan poco").contains(finalAttack.getStrTipoToPkType())
							&& !iaAtaquesDebiles.contains(finalAttack)
							&& !listaNoAfecta.contains(finalAttack.getStrTipoToPkType())) {
						iaAtaquesDebiles.add(finalAttack);
					} else if (listaNoAfecta.contains(finalAttack.getStrTipoToPkType())) {
						iaAtaquesNoAfectan.add(finalAttack);
					} else {
						if (!iaAtaquesNormales.contains(finalAttack)
								&& !listaNoAfecta.contains(finalAttack.getStrTipoToPkType())) {
							iaAtaquesNormales.add(finalAttack);
						}
					}
				}
			}
		}

		// Sets all the attacks from the Pokemon by their level of damage
		this.getPkCombatting().setAtaquesRebientan(iaAtaquesRebientan);
		this.getPkCombatting().setAtaquesNormales(iaAtaquesNormales);
		this.getPkCombatting().setAtaquesDebiles(iaAtaquesDebiles);
		this.getPkCombatting().setAtaquesNoAfectan(iaAtaquesNoAfectan);

		System.out.println("Ataques no afectan");
		for (Ataque a : this.getPkCombatting().getAtaquesNoAfectan()) {
			System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
		}
		System.out.println("Ataques rebientan");
		for (Ataque a : this.getPkCombatting().getAtaquesRebientan()) {
			System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
		}
		System.out.println("Ataques normales");
		for (Ataque a : this.getPkCombatting().getAtaquesNormales()) {
			System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
		}
		System.out.println("Ataques debiles");
		for (Ataque a : this.getPkCombatting().getAtaquesDebiles()) {
			System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
		}
		System.out.println("next method");
		System.out.println();
	}

	// Choice the attack
	public void prepareBestAttack() {
		// Gets a random number to chose the base of the attack
		int randomNumber;
		randomNumber = (int) (Math.random() * (100) + 1);
		System.out.println(randomNumber);

		// Allows to chose an attack if doesn't meet the conditions before
		boolean isAttackChoosed = false;

		// Fisic or special attack
		if (randomNumber > 10) {
			// Attacks that hurt a lot
			if (!this.getPkCombatting().getAtaquesRebientan().isEmpty()) {
				for (PokemonType pkT : this.getPkCombatting().getTypes()) {
					Optional<Ataque> nextAtaque = this.getPkCombatting().getAtaquesRebientan().stream()
							.filter(a -> a.getStrTipoToPkType() == pkT).findFirst();
					if (nextAtaque.isPresent()) {
						if(!nextAtaque.get().getBases().contains("otros")) {
							this.getPkCombatting().setNextMouvement(nextAtaque.get());
							System.out
									.println(nextAtaque.get().getNombreAta() + "-rebienta " + nextAtaque.get().getBases());
							isAttackChoosed = true;
							break;
						}
					}
				}
			}
			if (!isAttackChoosed) {
				// Normal attacks
				if (!this.getPkCombatting().getAtaquesNormales().isEmpty()) {
					for (PokemonType pkT : this.getPkCombatting().getTypes()) {
						Optional<Ataque> nextAtaque = this.getPkCombatting().getAtaquesNormales().stream()
								.filter(a -> a.getStrTipoToPkType() == pkT && !a.getBases().contains("otros"))
								.findFirst();
						if (nextAtaque.isPresent()) {
							this.getPkCombatting().setNextMouvement(nextAtaque.get());
							System.out.println(
									nextAtaque.get().getNombreAta() + "-normal " + nextAtaque.get().getBases());
							isAttackChoosed = true;
							break;
						}
					}
				}
			}
			// Else choose first attack that doesn't match with "otros" and is not the
			// weakest attack
			if (!isAttackChoosed) {
				if (!this.getPkCombatting().getAtaquesNormales().isEmpty()) {
					Optional<Ataque> nextAtaque = this.getPkCombatting().getCuatroAtaques().stream()
							.filter(a -> !a.getBases().contains("otros")
									&& !this.getPkCombatting().getAtaquesDebiles().contains(a))
							.findFirst();
					if (nextAtaque.isPresent()) {
						this.getPkCombatting().setNextMouvement(nextAtaque.get());
						System.out.println(
								nextAtaque.get().getNombreAta() + "-no el peor " + nextAtaque.get().getBases());
						isAttackChoosed = true;
					}
				}
			}
			// Else choose a random attack that doesn't match with "otros"
			if (!isAttackChoosed) {
				if (!this.getPkCombatting().getAtaquesNormales().isEmpty()) {
					Optional<Ataque> nextAtaque = this.getPkCombatting().getCuatroAtaques().stream()
							.filter(a -> !a.getBases().contains("otros")).findFirst();
					if (nextAtaque.isPresent()) {
						this.getPkCombatting().setNextMouvement(nextAtaque.get());
						System.out.println(
								nextAtaque.get().getNombreAta() + "-random sin otros " + nextAtaque.get().getBases());
						isAttackChoosed = true;
					}
				}
			}
		} else {
			// Choose the only attack from "Otros"
			this.getPkCombatting().setNextMouvement(this.getPkCombatting().getCuatroAtaques().stream()
					.filter(a -> a.getBases().contains("otros")).findFirst().get());
			System.out.println(this.getPkCombatting().getNextMouvement().getNombreAta());
		}
	}
}
