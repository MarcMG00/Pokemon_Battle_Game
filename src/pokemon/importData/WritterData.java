package pokemon.importData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import pokemon.model.Ability;
import pokemon.model.Attack;
import pokemon.model.Pokemon;
import pokemon.model.PokemonType;

public class WritterData {

	// ==================================== FIELDS
	// ====================================
	
	private ArrayList<Pokemon> pokemon;
	private HashMap<String, ArrayList<PokemonType>> pokemonTypePerPokemon;
	private ArrayList<Ability> abilities;
	private HashMap<String, HashMap<String, ArrayList<Ability>>> abilitiesPerPokemon;
	private ArrayList<PokemonType> types;
	private HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes;
	private ArrayList<Attack> attacks;
	private HashMap<Integer, HashMap<String, ArrayList<Integer>>> attacksPerPokemon;

	private static final String SAMPLE_CSV_ALL_POKEMON = "./data/pokemonList.csv";
	private static final String SAMPLE_CSV_ALL_POKEMON_ABS = "./data/pokemonList2.csv";
	private static final String SAMPLE_CSV_ALL_ABS = "./data/absList.csv";
	private static final String SAMPLE_CSV_ALL_POKEMON_TYPES = "./data/pokemonList3.csv";
	private static final String SAMPLE_CSV_ALL_ATTACKS = "./data/attacksList.txt";
	private static final String SAMPLE_CSV_ALL_ATTACKS_FOREACH_POKEMON = "./data/attacksForEachPokemon.txt";

	// ==================================== CONSTRCUTORS
	// ====================================
	
	public WritterData() {

	}

	// ==================================== GETTERS/SETTERS
	// ====================================
	
	public ArrayList<Pokemon> getPokemon() {
		return pokemon;
	}

	public void setPokemon(ArrayList<Pokemon> pokemon) {
		this.pokemon = pokemon;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}

	public HashMap<String, HashMap<String, ArrayList<Ability>>> getAbilitiesPerPokemon() {
		return abilitiesPerPokemon;
	}

	public void setAbilitiesPerPokemon(HashMap<String, HashMap<String, ArrayList<Ability>>> abilitiesPerPokemon) {
		this.abilitiesPerPokemon = abilitiesPerPokemon;
	}

	public HashMap<String, ArrayList<PokemonType>> getPokemonTypePerPokemon() {
		return pokemonTypePerPokemon;
	}

	public void setPokemonTypePerPokemon(HashMap<String, ArrayList<PokemonType>> pokemonTypePerPokemon) {
		this.pokemonTypePerPokemon = pokemonTypePerPokemon;
	}

	public ArrayList<PokemonType> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PokemonType> types) {
		this.types = types;
	}

	public HashMap<String, HashMap<String, ArrayList<PokemonType>>> getEffectPerTypes() {
		return effectPerTypes;
	}

	public void setEffectPerTypes(HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes) {
		this.effectPerTypes = effectPerTypes;
	}

	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

	public void setAttacks(ArrayList<Attack> attacks) {
		this.attacks = attacks;
	}

	public HashMap<Integer, HashMap<String, ArrayList<Integer>>> getAttacksPerPokemon() {
		return attacksPerPokemon;
	}

	public void setAttacksPerPokemon(HashMap<Integer, HashMap<String, ArrayList<Integer>>> attacksPerPokemon) {
		this.attacksPerPokemon = attacksPerPokemon;
	}

	// ==================================== METHODS
	// ====================================

	// -----------------------------
	// Writes in a CSV all the Pokemon from Pokemon list
	// -----------------------------
	@SuppressWarnings("resource")
	public void writePokemonCSV(ArrayList<Pokemon> pokemonList) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_POKEMON));

			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("PokemonId", "PokemonName",
					"PS", "Attack", "Def", "Speed", "AttackSp", "DefSp"));

			for (Pokemon pk : pokemonList) {

				csvPrinter.printRecord(pk.getId(), pk.getName(), pk.getPs(), pk.getAttack(), pk.getDef(), pk.getSpeed(),
						pk.getSpecialAttack(), pk.getSpecialDefense());

			}

			csvPrinter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------
	// Writes in a CSV all the abilities from abilities list
	// -----------------------------
	@SuppressWarnings("resource")
	public void writeAbilitiesCSV(ArrayList<Ability> abilitiesList) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_ABS));

			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader("AbilityId", "AbilityName", "Effect"));

			for (Ability ablty : abilitiesList) {

				csvPrinter.printRecord(ablty.getId(), ablty.getName(), ablty.getDescription());

			}

			csvPrinter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------
	// This method extends the CSV of pokemonList.csv in a new file
	// (pokemonList2.csv)
	// Appends the abilities for each Pokemon in pokemonList2.csv
	// -----------------------------
	@SuppressWarnings("resource")
	public void AppendAbilities() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_POKEMON_ABS));

			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader("PokemonId", "PokemonName", "PS", "Attack", "Def", "Speed", "AttackSp",
							"DefSp", "Ability1", "Ability2", "HiddenAbility1", "HiddenAbility2"));

			FileReader fileReader = new FileReader(SAMPLE_CSV_ALL_POKEMON);
			BufferedReader br = new BufferedReader(fileReader);

			// Skips first line
			br.readLine();
			StringBuilder sb = new StringBuilder();
			String line;

			// We read each line of pokemonList.csv and put it to pokemonList2.csv
			while ((line = br.readLine()) != null) {

				String[] pks = line.split(",");

				for (Map.Entry<String, HashMap<String, ArrayList<Ability>>> entry : this.getAbilitiesPerPokemon()
						.entrySet()) {

					if (pks[0].equals(entry.getKey())) {

						// If there is only one dico (one ability)
						if (entry.getValue().size() == 1) {

							sb.append("," + entry.getValue().get("Habilidad").get(0).getId() + "," + "," + ",");

						} else {

							// If there are 2 values in dico for normal abilities
							if (entry.getValue().get("Habilidad").size() == 2) {

								for (Ability ablty : entry.getValue().get("Habilidad")) {

									sb.append("," + ablty.getId());

								}
							}

							// If there is 1 value in dico for normal abilities
							else {

								sb.append("," + entry.getValue().get("Habilidad").get(0).getId() + ",");

							}

							// If there are 2 values in dico for hidden abilities
							if (entry.getValue().get("Habilidad oculta").size() == 2) {

								for (Ability ablty : entry.getValue().get("Habilidad oculta")) {

									sb.append("," + ablty.getId());

								}
							}

							// If there is 1 value in dico for hidden abilities
							else {

								sb.append("," + entry.getValue().get("Habilidad oculta").get(0).getId() + ",");

							}
						}

						// Prints the line in the next CSV
						csvPrinter.printRecord(line + sb);

						// Restart the string for abilities
						sb = new StringBuilder();
					}
				}
			}

			csvPrinter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------
	// This method extends the CSV of pokemonList2.csv in a new file
	// (pokemonList3.csv)
	// Appends the Pokemon types for each Pokemon in pokemonList3.csv
	// -----------------------------
	@SuppressWarnings("resource")
	public void AppendPokemonTypes() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_POKEMON_TYPES));

			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader("PokemonId", "PokemonName", "PS", "Attack", "Def", "Speed", "AttackSp",
							"DefSp", "Ability1", "Ability2", "HiddenAbility1", "HiddenAbility2", "Type1", "Type2"));

			FileReader fileReader = new FileReader(SAMPLE_CSV_ALL_POKEMON_ABS);
			BufferedReader br = new BufferedReader(fileReader);

			// Skips first line
			br.readLine();
			StringBuilder sb = new StringBuilder();
			String line;

			// We read each line of pokemonList2.csv and put it to pokemonList3.csv
			while ((line = br.readLine()) != null) {

				String[] pks = line.split(",");

				for (Map.Entry<String, ArrayList<PokemonType>> entry : this.getPokemonTypePerPokemon().entrySet()) {

					if (pks[0].equals(entry.getKey())) {

						// If there is only one dico (one ability)
						if (entry.getValue().size() == 1) {

							sb.append("," + entry.getValue().get(0).getId() + ",");

						} else {

							// If there are 2 values in dico for normal abilities
							sb.append("," + entry.getValue().get(0).getId() + ",");
							sb.append(entry.getValue().get(1).getId());

						}

						// Prints the line in the next CSV
						csvPrinter.printRecord(line + sb);

						// Restart the string for abilities
						sb = new StringBuilder();

					}
				}
			}

			csvPrinter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------
	// Writes in a CSV all the attacks from attacks list
	// -----------------------------
	@SuppressWarnings("resource")
	public void writeAttacksCSV() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_ATTACKS));

			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("AttackId", "AttackName",
					"Type", "Power", "PP", "Precision", "Effect", "Base"));

			for (Attack atk : this.getAttacks()) {

				csvPrinter.printRecord(atk.getId(), atk.getName(), atk.getType(), atk.getPower(), atk.getPp(),
						atk.getPrecision(), atk.getEffect(),
						atk.getBases().size() > 1 ? atk.getBases().get(0) + ";" + atk.getBases().get(1)
								: atk.getBases().get(0));

			}

			csvPrinter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------
	// Writes in a CSV all the attacks from attacks list
	// -----------------------------
	@SuppressWarnings("resource")
	public void writeAttacksCSV2() {
		try {
			FileWriter fileWriter = new FileWriter(SAMPLE_CSV_ALL_ATTACKS, true);
			BufferedWriter writer = new BufferedWriter(fileWriter);

			writer.append("AttackId,AttackName,Type,Power,PP,Precision,Effect,Base");
			writer.newLine();

			for (Attack atk : this.getAttacks()) {

				writer.append(String.valueOf(atk.getId()) + "," + atk.getName() + "," + atk.getType() + ","
						+ String.valueOf(atk.getPower()) + "," + String.valueOf(atk.getPp()) + ","
						+ String.valueOf(atk.getPrecision()) + "," + atk.getEffect() + ",");

				if (atk.getBases().size() > 1) {

					writer.append(String.valueOf(atk.getBases().get(0) + ";" + atk.getBases().get(1)));

				} else {

					writer.append(String.valueOf(atk.getBases().get(0)));

				}

				writer.newLine();

			}

			writer.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------
	// Writes in a CSV all the attacks for each Pokemon
	// -----------------------------
	@SuppressWarnings("resource")
	public void writeAttacksForEachPokemon() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_ATTACKS_FOREACH_POKEMON));

			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader("PokemonId", "PhysAttacks", "SpeAttacks", "OthAttacks"));

			for (Map.Entry<Integer, HashMap<String, ArrayList<Integer>>> attack : this.getAttacksPerPokemon().entrySet()) {

				String csvP = "";
				csvP += attack.getKey() + ",";

				for (Integer atId : attack.getValue().get("Ataques fisicos")) {

					csvP += atId + ";";

				}

				// Remove the last ";" => instead put a ","
				csvP = StringUtils.chop(csvP);
				csvP += ",";

				for (Integer atId : attack.getValue().get("Ataques especiales")) {

					csvP += atId + ";";

				}

				csvP = StringUtils.chop(csvP);
				csvP += ",";

				for (Integer atId : attack.getValue().get("Ataques otros")) {

					csvP += atId + ";";

				}

				csvP = StringUtils.chop(csvP);

				csvPrinter.printRecord(csvP);

			}

			csvPrinter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
