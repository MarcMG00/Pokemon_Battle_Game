package pokemon.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.text.Normalizer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pokemon.enums.AttackCategory;
import pokemon.enums.StatusConditions;

public class Game {
	private static final String SAMPLE_CSV_ALL_POKEMON = "./data/pokemonList.csv";
	private static final String SAMPLE_CSV_ALL_POKEMON_ABS = "./data/pokemonList2.csv";
	private static final String SAMPLE_CSV_ALL_ABS = "./data/absList.csv";
	private static final String SAMPLE_CSV_ALL_TYPES = "./data/typesList.txt";
	private static final String SAMPLE_CSV_ALL_POKEMON_TYPES = "./data/pokemonList3.csv";
	private static final String SAMPLE_CSV_ALL_ATTACKS = "./data/attacksList.txt";
	private static final String SAMPLE_CSV_ALL_ATTACKS_FOREACH_POKEMON = "./data/attacksForEachPokemon.txt";

	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_RESET = "\u001B[0m";

	private ArrayList<Pokemon> pokemon;
	private HashMap<String, ArrayList<PokemonType>> pokemonTypePerPokemon;
	private ArrayList<Ability> abilities;
	private HashMap<String, HashMap<String, ArrayList<Ability>>> abilitiesPerPokemon;
	private ArrayList<PokemonType> types;
	private HashMap<String, HashMap<String, ArrayList<PokemonType>>> effectPerTypes;
	private ArrayList<Attack> attacks;
	private HashMap<Integer, HashMap<String, ArrayList<Integer>>> attacksPerPokemon;
	private HashMap<String, ArrayList<Pokemon>> pokemonPerType;
	private Map<Integer, PokemonType> typeById = new HashMap<>();
	private Map<Integer, Pokemon> pokemonById = new HashMap<>();
	private Map<Integer, Attack> attackById = new HashMap<>();

	private Player player;
	private IAPlayer IA;

	public Game() {
		this.pokemon = new ArrayList<>();
		this.pokemonTypePerPokemon = new HashMap<>();
		this.abilities = new ArrayList<>();
		this.abilitiesPerPokemon = new HashMap<>();
		this.types = new ArrayList<>();
		this.effectPerTypes = new HashMap<>();
		this.attacks = new ArrayList<>();
		this.attacksPerPokemon = new HashMap<>();
		this.player = new Player();
		this.IA = new IAPlayer();
		this.pokemonPerType = new HashMap<>();
		this.typeById = new HashMap<>();
		this.pokemonById = new HashMap<>();
		this.attackById = new HashMap<>();
	}

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

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getIA() {
		return IA;
	}

	public void setIA(IAPlayer iA) {
		IA = iA;
	}

	public HashMap<String, ArrayList<Pokemon>> getPokemonPerType() {
		return pokemonPerType;
	}

	public void setPokemonPerType(HashMap<String, ArrayList<Pokemon>> pokemonPerType) {
		this.pokemonPerType = pokemonPerType;
	}

	public Map<Integer, PokemonType> getTypeById() {
		return typeById;
	}

	public void setTypeById(Map<Integer, PokemonType> typeById) {
		this.typeById = typeById;
	}

	public Map<Integer, Pokemon> getPokemonById() {
		return pokemonById;
	}

	public void setPokemonById(Map<Integer, Pokemon> pokemonById) {
		this.pokemonById = pokemonById;
	}

	public Map<Integer, Attack> getAttackById() {
		return attackById;
	}

	public void setAttackById(Map<Integer, Attack> attackById) {
		this.attackById = attackById;
	}

	// ==================================== SCRAPPING WEB
	// ====================================

	// Add to Pokemon list all the Pokemon from 1 to 809
	public void scrappingWebPokemon() {
		try {
			// Total nb of Pokemon
			int nbPk = 809;

			// We put all the Pokemon to the list
			for (int id = 1; id <= nbPk; id++) {
				// Each Pokemon change by id in the url
				String url = "https://www.pokexperto.net/index2.php?seccion=nds/nationaldex/stats&pk=" + id;

				// Gets the url
				Document document = Jsoup.connect(url).get();

				// Gets the "pkmain" class we are interested for statistics
				int classPkMain = 0;

				// Gets the "pktitle" class for the Pokemon name
				Element pokemonName = document.selectFirst(".pktitle");
				String name = pokemonName.select(".mini").text();

				// Counts number of "pkmain" class (there are more or less mega evolutions
				// depending on the Pokemon, so there are more or less "pkmain" class)
				Elements nbClass = document.getElementsByClass("pkmain");
				int nbAmount = nbClass.size();

				switch (nbAmount) {
				case 149:
					// 1-
					classPkMain = 22;
					break;
				case 155:
					classPkMain = 28;
					break;
				case 161:
					// 1-
					classPkMain = 34;
					break;
				case 167:
					// 1-
					classPkMain = 40;
					break;
				case 185:
					// 1-
					classPkMain = 58;
					break;
				case 197:
					// 1-
					classPkMain = 70;
					break;
				}

				Element pokemon = document.select(".pkmain").get(classPkMain);

				// Gets only the first 6 statistics elements on the table with "right" class
				List<Object> pokemonStats = pokemon.select(".right").stream().limit(6).collect(Collectors.toList());

				// Create Pokemon
				Pokemon pk = new Pokemon(id, name, Integer.parseInt(((Element) pokemonStats.get(0)).text()),
						Integer.parseInt(((Element) pokemonStats.get(1)).text()),
						Integer.parseInt(((Element) pokemonStats.get(2)).text()),
						Integer.parseInt(((Element) pokemonStats.get(3)).text()),
						Integer.parseInt(((Element) pokemonStats.get(4)).text()),
						Integer.parseInt(((Element) pokemonStats.get(5)).text()));
				this.pokemon.add(pk);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Add to ability list all the abilities (309)
	public void scrappingWebAllAbs() {
		try {
			String url = "https://www.wikidex.net/wiki/Lista_de_habilidades";

			// Gets the url
			Document document = Jsoup.connect(url).get();

			// Selects the abilities table
			Elements table = document.select(".tabpokemon");

			// Selects all the "tr" in the table
			Elements elementObjTr = table.first().children().select("tr");

			for (Element tr : elementObjTr) {

				// We not consider the first "tr" cause it represents the titles (# represents
				// the column name for abilities Id)
				if (!tr.firstElementChild().text().equals("#")) {

					// We get the 2 firsts "td" (representing the name and the description effect) -
					// we remove all accents
					Ability ablty = new Ability(Integer.parseInt(tr.firstElementChild().text()),
							Normalizer.normalize(tr.select("td>a").get(0).text(), Normalizer.Form.NFKD)
									.replaceAll("[^\\p{ASCII}]", ""),
							Normalizer.normalize(tr.select("td").get(2).text(), Normalizer.Form.NFKD)
									.replaceAll("[^\\p{ASCII}]", ""));

					this.abilities.add(ablty);

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Reads all the possible abilities for different Pokemon and put it to dico
	public HashMap<String, HashMap<String, ArrayList<Ability>>> scrappingWebReadAbsFromPokemonAllTables() {
		try {
			String url = "https://www.wikidex.net/wiki/Lista_de_Pok%C3%A9mon_con_sus_habilidades";

			// Gets the url
			Document document = Jsoup.connect(url).get();

			// Select all the Pokemon tables we are interested (each table represents a
			// generation, we have a total of 809 Pokemon in 7 tables)
			for (int tab = 0; tab < 7; tab++) {

				// We get each table
				Element table = document.select(".tabpokemon").get(tab);

				// We get each <tr> from the table concerned
				Elements elementObjTr = table.select("tr");

				for (Element tPk : elementObjTr) {

					// We don't take the first <tr> line (represents the name of the columns)
					if (!tPk.firstElementChild().text().equals("Nº")) {

						// We don't want the mega evolutions (they have the same number as the last
						// evolution of the Pokemon)
						if (!this.abilitiesPerPokemon.containsKey(tPk.select("td").get(0).text())) {

							// Hash Map to put al the abilities for each Pokemon
							HashMap<String, ArrayList<Ability>> abs = new HashMap<>();

							// List of normal abilities for each Pokemon
							ArrayList<Ability> pokemonAbs = new ArrayList<>();

							// List of hidden abilities for each Pokemon
							ArrayList<Ability> pokemonHiddenAbs = new ArrayList<>();

							// First ability
							Optional<Ability> ablty;

							// Second ability
							Ability ablty2;

							// Hidden ability
							Optional<Ability> abltyHid;

							// Depending on the number of columns, there are more or less abilities (each
							// ability is in a column)
							int nbTd = tPk.select("td").size();

							switch (nbTd) {
							// There's only one ability, so we put it as normal ability
							case 6:
								// We remove all the digits from the ability and we search the ability in our
								// list of abilities (we compare by name)
								ablty = this.abilities
										.stream().filter(
												habil -> habil.getName()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(5).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();

								// Abilities are optional, so we do a condition of nullability
								if (ablty.isPresent()) {

									// We put the ability in the normal list of abilities
									ablty2 = new Ability(ablty.get().getId(), ablty.get().getName(),
											ablty.get().getDescription());

									pokemonAbs.add(ablty2);

								}
								// We put the normal list of abilities in the dico as "Habilidad" (normal
								// ability)
								abs.put("Habilidad", pokemonAbs);
								break;
							case 7:
								ablty = this.abilities
										.stream().filter(
												habil -> habil.getName()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(5).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();

								if (ablty.isPresent()) {

									ablty2 = new Ability(ablty.get().getId(), ablty.get().getName(),
											ablty.get().getDescription());

									pokemonAbs.add(ablty2);

								}
								abs.put("Habilidad", pokemonAbs);

								abltyHid = this.abilities
										.stream().filter(
												habil -> habil.getName()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(6).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();

								if (abltyHid.isPresent()) {

									ablty2 = new Ability(abltyHid.get().getId(), abltyHid.get().getName(),
											abltyHid.get().getDescription());

									pokemonHiddenAbs.add(ablty2);

								}
								abs.put("Habilidad oculta", pokemonHiddenAbs);
								break;
							case 8:
								ablty = this.abilities
										.stream().filter(
												habil -> habil.getName()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(5).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();

								if (ablty.isPresent()) {

									ablty2 = new Ability(ablty.get().getId(), ablty.get().getName(),
											ablty.get().getDescription());

									pokemonAbs.add(ablty2);

								}

								ablty = this.abilities
										.stream().filter(
												habil -> habil.getName()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(6).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();

								if (ablty.isPresent()) {

									ablty2 = new Ability(ablty.get().getId(), ablty.get().getName(),
											ablty.get().getDescription());

									pokemonAbs.add(ablty2);

								}
								abs.put("Habilidad", pokemonAbs);

								// There's only (at the moment) one Pokemon that has 2 hidden abilities.
								// We do a split (with "/", cause there are not 2 columns for each ability)
								// For the first ability, we remove the last white space (replaceAll("\\s","")
								// cause there are not spaces in the word) before the ("/")
								// For the second ability, we remove the first white space (trim()) after the
								// ("/")
								if (tPk.select("td").get(0).text().equals("744")) {

									abltyHid = this.abilities
											.stream().filter(
													habil -> habil
															.getName().equals(
																	Normalizer
																			.normalize(
																					tPk.select("td").get(7).text()
																							.split("/")[0]
																							.replaceAll("\\s", "")
																							.replaceAll("[0-9]", "")
																							.toUpperCase(),
																					Normalizer.Form.NFKD)
																			.replaceAll("[^\\p{ASCII}]", "")))
											.findFirst();

									if (abltyHid.isPresent()) {

										ablty2 = new Ability(abltyHid.get().getId(), abltyHid.get().getName(),
												abltyHid.get().getDescription());

										pokemonHiddenAbs.add(ablty2);

									}

									abltyHid = this.abilities
											.stream().filter(
													habil -> habil
															.getName().equals(
																	Normalizer
																			.normalize(
																					tPk.select("td").get(7).text()
																							.split("/")[1].trim()
																							.replaceAll("[0-9]", "")
																							.toUpperCase(),
																					Normalizer.Form.NFKD)
																			.replaceAll("[^\\p{ASCII}]", "")))
											.findFirst();

									if (abltyHid.isPresent()) {

										ablty2 = new Ability(abltyHid.get().getId(), abltyHid.get().getName(),
												abltyHid.get().getDescription());

										pokemonHiddenAbs.add(ablty2);

									}
								} else {
									abltyHid = this.abilities.stream()
											.filter(habil -> habil.getName()
													.equals(Normalizer
															.normalize(
																	tPk.select("td").get(7).text()
																			.replaceAll("[0-9]", "").toUpperCase(),
																	Normalizer.Form.NFKD)
															.replaceAll("[^\\p{ASCII}]", "")))
											.findFirst();

									if (abltyHid.isPresent()) {

										ablty2 = new Ability(abltyHid.get().getId(), abltyHid.get().getName(),
												abltyHid.get().getDescription());

										pokemonHiddenAbs.add(ablty2);

									}
								}

								abs.put("Habilidad oculta", pokemonHiddenAbs);
								break;
							}

							// We put the dico in our principal var
							if (!this.abilitiesPerPokemon.containsKey(tPk.select("td").get(0).text())) {

								this.abilitiesPerPokemon.put(tPk.select("td").get(0).text(), abs);

							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.abilitiesPerPokemon;
	}

	// Read all the possible types for different Pokemon and put it to dico
	public HashMap<String, ArrayList<PokemonType>> scrappingWebReadTypeForEachPokemon() {
		try {
			String url = "https://www.wikidex.net/wiki/Lista_de_Pok%C3%A9mon_con_sus_habilidades";

			// Gets the url
			Document document = Jsoup.connect(url).get();

			for (int tab = 0; tab < 7; tab++) {

				// We get each table
				Element table = document.select(".tabpokemon").get(tab);

				// We get each <tr> from the table concerned
				Elements elementObjTr = table.select("tr");

				for (Element tPk : elementObjTr) {

					// We don't take the first <tr> line (represents the name of the columns)
					if (!tPk.firstElementChild().text().equals("Nº")) {

						if (!this.pokemonTypePerPokemon.containsKey(tPk.select("td").get(0).text())) {

							ArrayList<PokemonType> pokemonTypes = new ArrayList<>();
							Optional<PokemonType> optPkT1;
							Optional<PokemonType> optPkT2;
							Element aAfterTD;

							// Gets first type (there will be one every time at position 3)
							optPkT1 = this.types.stream()
									.filter(tp -> tp.getName()
											.equals(Normalizer
													.normalize(
															tPk.select("td").get(3).select("a").first().attr("title")
																	.replaceAll("[0-9]", "").toUpperCase(),
															Normalizer.Form.NFKD)
													.replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s", "").toUpperCase()
													.substring(4).trim()))
									.findFirst();

							if (optPkT1.isPresent()) {

								pokemonTypes.add(optPkT1.get());

							}

							// Gets second type (it can be null)
							// We check if there is an "a" to get the type name (if true, the Pokemon only
							// has one type, so skip column at position 4)
							aAfterTD = tPk.select("td").get(4).select("a").first();

							if (aAfterTD != null) {

								optPkT2 = this.types.stream()
										.filter(tp -> tp.getName().equals(Normalizer
												.normalize(
														tPk.select("td").get(4).select("a").first().attr("title")
																.replaceAll("[0-9]", "").toUpperCase(),
														Normalizer.Form.NFKD)
												.replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s", "").toUpperCase()
												.substring(4).trim()))
										.findFirst();

								if (optPkT2.isPresent()) {

									pokemonTypes.add(optPkT2.get());

								}
							}

							// We put the dico in our principal var
							if (!this.pokemonTypePerPokemon.containsKey(tPk.select("td").get(0).text())) {

								this.pokemonTypePerPokemon.put(tPk.select("td").get(0).text(), pokemonTypes);

							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.pokemonTypePerPokemon;
	}

	// Gets all the attacks
	public void scrappingWebAttacks() {
		try {
			String url = "https://www.pokexperto.net/index2.php?seccion=nds/movimientos_pokemon";

			// Gets the url and converts to String (HTML)
			// Names are in Spanish and English, so we remove the English version with a
			// regex
			// The first replace, it's used to remove the href (<a>) and the content after
			// br
			// Other ones are used to remove spaces and the content after br
			String documentStr = Jsoup.connect(url).get().toString().replaceAll("<br><[^>]*>.*?/a>", "")
					.replaceAll("<br>\n\\s{2,}.*?/td", "</td>").replaceAll("<br>\n\\s{2,}.*?/th", "</th>");

			// Parse the HTML to Document
			Document document = Jsoup.parse(documentStr);

			// Gets the 13 tab class we are interested in
			Element table = document.getElementsByClass("bordetodos").get(13);

			// We get each <tr> from the concerned table
			Elements elementObjTr = table.select("tr");

			for (Element ta : elementObjTr) {

				// Skip first line (titles of columns) and attack number 905 (there are no
				// values)
				if (!ta.firstElementChild().text().equals("#") && !ta.select("th").text().equals("905")) {

					// Sets the new Attack
					Attack atk = new Attack(Integer.parseInt(ta.select("th").text()), ta.select("td").get(0).text(),
							ta.select("td").get(1).attr("sorttable_customkey").toUpperCase(),
							ta.select("td").get(3).text().equals("--") ? 0
									: Integer.parseInt(ta.select("td").get(3).text()),
							Integer.parseInt(ta.select("td").get(4).text()),
							ta.select("td").get(5).text().equals("--") ? 0
									: Integer.parseInt(ta.select("td").get(5).text().substring(0,
											ta.select("td").get(5).text().length() - 1)),
							ta.select("td").get(6).text().replace(",", ""));

					// There are some attacks that have 2 base types, so we put it in a list
					Elements bases = ta.select("td").get(2).select("img");

					for (Element base : bases) {

						atk.addBase(base.attr("alt"));

					}

					// Adds the attack to the general attacks list
					this.attacks.add(atk);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Gets all the attacks for each Pokemon
	// TD + TH [1-(235,664,789,790),6-722,7-650,8-494,9-387,10-152,11-1]
	public void scrappingWebAttacksForEachPokemon() {
		try {

			for (int iPk = 1; iPk < 808; iPk++) {

				// Some Pokemon have only one attack (they are treated manually)
				if (iPk != 235 && iPk != 664 && iPk != 789 && iPk != 790) {

					HashMap<String, ArrayList<Integer>> AllAttacks = new HashMap<>();

					ArrayList<Integer> physicalAttacks = new ArrayList<>();
					ArrayList<Integer> specialAttacks = new ArrayList<>();
					ArrayList<Integer> otherAttacks = new ArrayList<>();

					// URL for each Pokemon
					String url = "https://www.pokexperto.net/index2.php?seccion=nds/nationaldex/movimientos_pokemon&pk="
							+ iPk;

					// We convert the page in a string (HTML)
					// We remove the English name version
					String documentStr = Jsoup.connect(url).get().toString().replaceAll("<br><[^>]*>.*?/td>", "</td>");

					// We parse the string to a Document
					Document document = Jsoup.parse(documentStr);

					// There are 3 tables (physical, special and others)
					Element tablePhysical = document.getElementsByClass("bordetodos").get(0);
					Element tableSpecials = document.getElementsByClass("bordetodos").get(1);
					Element tableOthers = document.getElementsByClass("bordetodos").get(2);

					// We are interested only in <tr>
					Elements allTrPh = tablePhysical.select("tr");
					Elements allTrSp = tableSpecials.select("tr");
					Elements allTrO = tableOthers.select("tr");

					// Physical attacks
					for (Element trPh : allTrPh) {

						Optional<Attack> atk;
						int thSize = trPh.select("th").size();

						// Titles are only composed by th (so we skip the first line (titles))
						if (thSize != 0) {

							// We take only name class "bverde3"
							if (!trPh.select("th").get(0).attr("class").equals("bverde3")) {

								// The attack name is situated at the first <td>
								String attackName = trPh.select("td").get(0).text();

								// We search the attack on the attacks list by its name
								atk = this.attacks.stream().filter(at -> at.getName().equals(attackName)).findFirst();

								if (atk.isPresent()) {

									physicalAttacks.add(atk.get().getId());

								}
							}
						}
					}

					AllAttacks.put("Ataques fisicos", physicalAttacks);

					// Special attacks
					for (Element trSp : allTrSp) {

						Optional<Attack> atk;
						int thSize = trSp.select("th").size();

						if (thSize != 0) {

							if (!trSp.select("th").get(0).attr("class").equals("bverde3")) {

								String attackName = trSp.select("td").get(0).text();

								atk = this.attacks.stream().filter(at -> at.getName().equals(attackName)).findFirst();

								if (atk.isPresent()) {

									specialAttacks.add(atk.get().getId());

								}
							}
						}
					}

					AllAttacks.put("Ataques especiales", specialAttacks);

					// Other attacks
					for (Element trO : allTrO) {

						Optional<Attack> atk;
						int thSize = trO.select("th").size();

						if (thSize != 0) {

							if (!trO.select("th").get(0).attr("class").equals("bverde3")) {

								String attackName = trO.select("td").get(0).text();

								atk = this.attacks.stream().filter(at -> at.getName().equals(attackName)).findFirst();

								if (atk.isPresent()) {

									otherAttacks.add(atk.get().getId());

								}
							}
						}
					}

					AllAttacks.put("Ataques otros", otherAttacks);

					// We put all the attacks for the current Pokemon
					this.attacksPerPokemon.put(iPk, AllAttacks);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ==================================== WRITTERS
	// ====================================

	// Writes in a CSV all the Pokemon from Pokemon list
	@SuppressWarnings("resource")
	public static void writePokemonCSV(ArrayList<Pokemon> pokemonList) {
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

	// Writes in a CSV all the abilities from abilities list
	@SuppressWarnings("resource")
	public static void writeAbilitiesCSV(ArrayList<Ability> abilitiesList) {
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

	// This method extends the CSV of pokemonList.csv in a new file
	// (pokemonList2.csv)
	// Appends the abilities for each Pokemon in pokemonList2.csv
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

				for (Map.Entry<String, HashMap<String, ArrayList<Ability>>> entry : this.abilitiesPerPokemon
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

	// This method extends the CSV of pokemonList2.csv in a new file
	// (pokemonList3.csv)
	// Appends the Pokemon types for each Pokemon in pokemonList3.csv
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

				for (Map.Entry<String, ArrayList<PokemonType>> entry : pokemonTypePerPokemon.entrySet()) {

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

	// Writes in a CSV all the attacks from attacks list
	@SuppressWarnings("resource")
	public void writeAttacksCSV() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_ATTACKS));

			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("AttackId", "AttackName",
					"Type", "Power", "PP", "Precision", "Effect", "Base"));

			for (Attack atk : this.attacks) {

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

	// Writes in a CSV all the attacks from attacks list
	@SuppressWarnings("resource")
	public void writeAttacksCSV2() {
		try {
			FileWriter fileWriter = new FileWriter(SAMPLE_CSV_ALL_ATTACKS, true);
			BufferedWriter writer = new BufferedWriter(fileWriter);

			writer.append("AttackId,AttackName,Type,Power,PP,Precision,Effect,Base");
			writer.newLine();

			for (Attack atk : this.attacks) {

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

	// Writes in a CSV all the attacks for each Pokemon
	@SuppressWarnings("resource")
	public void writeAttacksForEachPokemon() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_ATTACKS_FOREACH_POKEMON));

			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader("PokemonId", "PhysAttacks", "SpeAttacks", "OthAttacks"));

			for (Map.Entry<Integer, HashMap<String, ArrayList<Integer>>> attack : this.attacksPerPokemon.entrySet()) {

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

	// ==================================== READERS
	// ====================================

	// Reads pokemon.csv file and adds to Pokemon list
	public void readPokemon(String fileName) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {

				String[] pks = line.split(",");

				if (this.pokemon.size() == 809) {

					break;

				} else {

					Pokemon pokemon = new Pokemon(Integer.parseInt(pks[0]), pks[1], Integer.parseInt(pks[2]),
							Integer.parseInt(pks[3]), Integer.parseInt(pks[4]), Integer.parseInt(pks[5]),
							Integer.parseInt(pks[6]), Integer.parseInt(pks[7]));

					// Gets first ability (all Pokemon have at least one ability)
					if (!pks[12].isEmpty()) {

						for (PokemonType pkty : this.types) {

							if (pkty.getId() == Integer.parseInt(pks[12])) {

								pokemon.addType(pkty);

							}
						}
					}

					// Gets the other type (if a Pokemon has 2 types)
					if (pks.length == 14) {

						// It detects a " " " at the end of the second type, so we remove it : we have
						// 17" instead of 17
//						pks[13] = pks[13].substring(0, pks[13].length() - 1);

						// Gets second type
						if (!pks[13].isEmpty()) {

							for (PokemonType pkty : this.types) {

								if (pkty.getId() == Integer.parseInt(pks[13])) {

									pokemon.addType(pkty);

								}
							}
						}
					}

					this.pokemon.add(pokemon);
					this.pokemonById.put(pokemon.getId(), pokemon);

				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file  : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				System.out.println("Finished reading readPokemon");
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	// Reads habsList.csv file and adds to abilities list
	public void readAbilities(String fileName) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {

				String[] ablty = line.split(",");

				if (this.abilities.size() == 309) {

					break;

				} else {

					this.abilities.add(new Ability(Integer.parseInt(ablty[0]), ablty[1].toUpperCase(), ablty[2]));

				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				System.out.println("Finished reading readAbilities");
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	// Reads pokemon.csv file (for abilities) and adds to Pokemon
	public void addAbsForEachPokemon(String fileName) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {

				Optional<Pokemon> pkOpt;
				String[] abltysPk = line.split(",");

				// Gets the current Pokemon of the line from the Pokemon list
				pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(abltysPk[0])).findFirst();

				if (pkOpt.isPresent()) {

					// Gets first ability (all Pokemon have at least one ability)
					if (!abltysPk[8].isEmpty()) {

						for (Ability ablty : this.abilities) {

							if (ablty.getId() == Integer.parseInt(abltysPk[8])) {

								pkOpt.get().addNormalAbility(ablty);

							}
						}
					}

					// Gets other abilities (if a Pokemon has more)
					if (abltysPk.length > 9) {

						// Gets second ability
						if (!abltysPk[9].isEmpty()) {

							for (Ability ablty : this.abilities) {

								if (ablty.getId() == Integer.parseInt(abltysPk[9])) {

									pkOpt.get().addNormalAbility(ablty);

								}
							}
						}

						// Gets hidden ability
						if (!abltysPk[10].isEmpty()) {

							for (Ability ablty : this.abilities) {

								if (ablty.getId() == Integer.parseInt(abltysPk[10])) {

									pkOpt.get().addHiddenAbility(ablty);

								}
							}
						}

						// Gets second hidden ability : only one Pokemon at the moment
						if (abltysPk.length == 12) {

							if (!abltysPk[11].isEmpty()) {

								for (Ability ablty : this.abilities) {

									if (ablty.getId() == Integer.parseInt(abltysPk[11])) {

										pkOpt.get().addHiddenAbility(ablty);

									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file  : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				System.out.println("Finished reading addAbsForEachPokemon");
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	// Reads pokemon.csv file (for types) and adds to Pokemon
	public void addTypesForEachPokemon(String fileName) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {

				Optional<Pokemon> pkOpt;
				String[] types = line.split(",");

				// It detects a " at the beginning of the Pokemon Id, so we remove it => ex : we
				// have "001 instead of 001
				types[0] = types[0].substring(1);

				// Gets the current Pokemon of the line from the Pokemon list
				pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(types[0])).findFirst();

				if (pkOpt.isPresent()) {

					// Gets first ability (all Pokemon have at least one ability)
					if (!types[12].isEmpty()) {

						for (PokemonType pkty : this.types) {

							if (pkty.getId() == Integer.parseInt(types[12])) {

								pkOpt.get().addType(pkty);

							}
						}
					}

					// Gets the other type (if a Pokemon has 2 types)
					if (types.length == 14) {

						// It detects a " at the end of the second type, so we remove it => ex : we have
						// 17" instead of 17
						types[13] = types[13].substring(0, types[13].length() - 1);

						// Gets second type
						if (!types[13].isEmpty()) {

							for (PokemonType pkty : this.types) {

								if (pkty.getId() == Integer.parseInt(types[13])) {

									pkOpt.get().addType(pkty);

								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file  : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	// Reads typesList.csv file and adds to types list
	public void readPkTypes(String fileName) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {

				String[] pkTypes = line.split(",");

				if (this.types.size() == 18) {

					break;

				} else {

					PokemonType pkType = new PokemonType(Integer.parseInt(pkTypes[0]), pkTypes[1].toUpperCase());
					this.types.add(pkType);
					typeById.put(pkType.getId(), pkType);

				}
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file  : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				System.out.println("Finished reading readPkTypes");
			} catch (IOException e) {
				System.out.println("Exception closing the file  : " + e.getMessage());
			}
		}
	}

	public void readPkTypesEffectsToOtherTypes() {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(SAMPLE_CSV_ALL_TYPES))) {

			bufferedReader.readLine(); // skip header
			String line;

			while ((line = bufferedReader.readLine()) != null) {

				String[] type = line.split(",");

				String typeName = type[1];
				HashMap<String, ArrayList<PokemonType>> types = new HashMap<>();

				// Defensive parse helper
				ArrayList<PokemonType> effects;
				PokemonType pType;

				// Helper para parsear una lista de IDs separadas por ";"
				String[][] keysAndColumns = { { "Rebienta", type[2] }, { "Le rebientan", type[3] },
						{ "Rebienta poco", type[4] }, { "Le Rebientan poco", type[5] },
						{ "No tiene efecto", type[6] } };

				for (String[] pair : keysAndColumns) {

					String key = pair[0];
					String raw = pair[1];

					if (raw.equals("0"))
						continue;

					String[] ids = raw.split(";");

					effects = new ArrayList<>();

					for (String idStr : ids) {
						int id = Integer.parseInt(idStr);
						pType = this.typeById.get(id);
						if (pType != null)
							effects.add(pType);
					}

					if (!effects.isEmpty())
						types.put(key, effects);
				}

				// Save into main dictionary
				this.effectPerTypes.put(typeName, types);
			}

			System.out.println("Finished reading readPokeTypeEffectsToOtherTypes");

		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		}
	}

	// Reads typesList.csv file and adds the effects against other types
//	public void readPkTypesEffectsToOtherTypes() {
//		FileReader fileReader = null;
//		BufferedReader bufferedReader = null;
//
//		try {
//			fileReader = new FileReader(SAMPLE_CSV_ALL_TYPES);
//			bufferedReader = new BufferedReader(fileReader);
//
//			// Skips first line
//			bufferedReader.readLine();
//			String line;
//
//			while ((line = bufferedReader.readLine()) != null) {
//
//				HashMap<String, ArrayList<PokemonType>> types = new HashMap<>();
//				ArrayList<PokemonType> typesEffect = new ArrayList<>();
//
//				String[] type = line.split(",");
//
//				// Each string[] can have several values: we split them by ";"
//				String[] pkTypeEffectBigDmg = type[2].split(";");
//				String[] pkTypeEffectVulnerable = type[3].split(";");
//				String[] pkTypeEffectLittleDmg = type[4].split(";");
//				String[] pkTypeEffectLittleVulnerable = type[5].split(";");
//				String[] pkTypeNoEffect = type[6].split(";");
//
//				Optional<PokemonType> pTypeOpt;
//				PokemonType pkTy;
//
//				// Do a lot of damage
//				if (pkTypeEffectBigDmg.length >= 1 && Integer.parseInt(pkTypeEffectBigDmg[0]) != 0) {
//
//					for (String pkT : pkTypeEffectBigDmg) {
//
//						pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
//
//						if (pTypeOpt.isPresent()) {
//
//							pkTy = pTypeOpt.get();
//
//							typesEffect.add(pkTy);
//
//						}
//					}
//
//					types.put("Rebienta", typesEffect);
//					typesEffect = new ArrayList<>();
//				}
//
//				// Hurts from other types
//				if (pkTypeEffectVulnerable.length >= 1 && Integer.parseInt(pkTypeEffectVulnerable[0]) != 0) {
//
//					for (String pkT : pkTypeEffectVulnerable) {
//
//						pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
//
//						if (pTypeOpt.isPresent()) {
//
//							pkTy = pTypeOpt.get();
//
//							typesEffect.add(pkTy);
//
//						}
//					}
//
//					types.put("Le rebientan", typesEffect);
//					typesEffect = new ArrayList<>();
//				}
//
//				// Do little damage
//				if (pkTypeEffectLittleDmg.length >= 1 && Integer.parseInt(pkTypeEffectLittleDmg[0]) != 0) {
//
//					for (String pkT : pkTypeEffectLittleDmg) {
//
//						pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
//
//						if (pTypeOpt.isPresent()) {
//
//							pkTy = pTypeOpt.get();
//
//							typesEffect.add(pkTy);
//
//						}
//					}
//
//					types.put("Rebienta poco", typesEffect);
//					typesEffect = new ArrayList<>();
//				}
//
//				// It hurts less than other types
//				if (pkTypeEffectLittleVulnerable.length >= 1
//						&& Integer.parseInt(pkTypeEffectLittleVulnerable[0]) != 0) {
//
//					for (String pkT : pkTypeEffectLittleVulnerable) {
//
//						pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
//
//						if (pTypeOpt.isPresent()) {
//
//							pkTy = pTypeOpt.get();
//
//							typesEffect.add(pkTy);
//
//						}
//					}
//
//					types.put("Le Rebientan poco", typesEffect);
//					typesEffect = new ArrayList<>();
//				}
//
//				// Doesn't has effect to him
//				if (pkTypeNoEffect.length >= 1 && Integer.parseInt(pkTypeNoEffect[0]) != 0) {
//
//					for (String pkT : pkTypeNoEffect) {
//
//						pTypeOpt = this.types.stream().filter(ty -> ty.getId() == Integer.parseInt(pkT)).findFirst();
//
//						if (pTypeOpt.isPresent()) {
//
//							pkTy = pTypeOpt.get();
//
//							typesEffect.add(pkTy);
//
//						}
//					}
//
//					types.put("No tiene efecto", typesEffect);
//				}
//
//				// We put the dico in our principal var
//				if (!this.effectPerTypes.containsKey(type[1])) {
//
//					this.effectPerTypes.put(type[1], types);
//
//				}
//
//			}
//		} catch (IOException e) {
//			System.out.println("Exception reading the file : " + e.getMessage());
//		} finally {
//			try {
//				if (fileReader != null) {
//					fileReader.close();
//				}
//				if (bufferedReader != null) {
//					bufferedReader.close();
//				}
//				System.out.println("Finished reading readPokeTypeEffectsToOtherTypes");
//			} catch (IOException e) {
//				System.out.println("Exception closing the file : " + e.getMessage());
//			}
//		}
//	}

	// Reads attacksList.csv file and adds to attacks list
	public void readAttacks(String fileName) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			// Skips first line
			bufferedReader.readLine();
			String line;

			while ((line = bufferedReader.readLine()) != null) {

				String[] attacks = line.split(",");

				Attack attack = new Attack(Integer.parseInt(attacks[0]), attacks[1], attacks[2].toUpperCase(),
						Integer.parseInt(attacks[3]), Integer.parseInt(attacks[4]), Integer.parseInt(attacks[5]),
						attacks[6]);

				// Some attacks can have 2 bases (so we split with ";")
				String[] bs = attacks[7].split(";");

				if (bs.length > 1) {

					for (String s : bs) {

						attack.addBase(s);

					}

				} else {

					attack.addBase(bs[0]);

				}

				// Set the type of the attack to his Pokemon type instead of a string
				attack.transformStrTypeToPokemonType(this.types);

				// Add the attacks that can hit while Pokemon facing is invulnerable
				putCanHitInvulnerableAttacks(attack);

				// Set the category type of the attack
				setCategoryAttackType(attack);

				// Set is attack has a special behavior
				setAttackSpecialBehavior(attack);

				// Adds the attack to the general var
				this.attacks.add(attack);
				attackById.put(attack.getId(), attack);
			}
		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				System.out.println("Finished reading readAttacks");
			} catch (IOException e) {
				System.out.println("Exception closing the file : " + e.getMessage());
			}
		}
	}

	public void readAttacksForEachPokemon() {
		try (BufferedReader bufferedReader = new BufferedReader(
				new FileReader(SAMPLE_CSV_ALL_ATTACKS_FOREACH_POKEMON))) {

			bufferedReader.readLine(); // skip header
			String line;

			while ((line = bufferedReader.readLine()) != null) {

				String[] cols = line.split(",");

				int pokemonId = Integer.parseInt(cols[0]);
				Pokemon pk = this.pokemonById.get(pokemonId);

				if (pk == null)
					continue;

				// Physical
				if (!cols[1].equals("0")) {
					for (String idStr : cols[1].split(";")) {
						Attack a = this.attackById.get(Integer.parseInt(idStr));
						if (a != null)
							pk.addPhysicalAttack(a);
					}
				}

				// Special
				if (!cols[2].equals("0")) {
					for (String idStr : cols[2].split(";")) {
						Attack a = this.attackById.get(Integer.parseInt(idStr));
						if (a != null)
							pk.addSpecialAttack(a);
					}
				}

				// Other
				if (!cols[3].equals("0")) {
					for (String idStr : cols[3].split(";")) {
						Attack a = this.attackById.get(Integer.parseInt(idStr));
						if (a != null)
							pk.addOtherAttack(a);
					}
				}
			}

			System.out.println("Finished reading readAttacksForEachPokemon");

		} catch (IOException e) {
			System.out.println("Exception reading the file : " + e.getMessage());
		}
	}

	// Reads attacksForeachPokemon.txt file and adds to each Pokemon
//	public void readAttacksForEachPokemon() {
//		FileReader fileReader = null;
//		BufferedReader bufferedReader = null;
//
//		try {
//			fileReader = new FileReader(SAMPLE_CSV_ALL_ATTACKS_FOREACH_POKEMON);
//			bufferedReader = new BufferedReader(fileReader);
//
//			// Skips first line
//			bufferedReader.readLine();
//			String line;
//
//			while ((line = bufferedReader.readLine()) != null) {
//
//				Optional<Pokemon> pkOpt;
//				Optional<Attack> pAtaOpt;
//
//				String[] pkAttacks = line.split(",");
//
//				// Each string[] can have several values: we split them by ";"
//				String[] pkPhAttacks = pkAttacks[1].split(";");
//				String[] pkSpAttacks = pkAttacks[2].split(";");
//				String[] pkOtAttacks = pkAttacks[3].split(";");
//
//				// Gets the current Pokemon of the line from the Pokemon list
//				pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(pkAttacks[0])).findFirst();
//
//				if (pkOpt.isPresent()) {
//
//					// Physical attacks
//					// Some Pokemon can have 0 attacks in a type of attack
//					if (pkPhAttacks.length >= 1 && Integer.parseInt(pkPhAttacks[0]) != 0) {
//
//						for (String phAtt : pkPhAttacks) {
//
//							pAtaOpt = this.attacks.stream().filter(a -> a.getId() == Integer.parseInt(phAtt))
//									.findFirst();
//
//							if (pAtaOpt.isPresent()) {
//
//								pkOpt.get().addPhysicalAttack(pAtaOpt.get());
//
//							}
//						}
//					}
//
//					// Special attacks
//					if (pkSpAttacks.length >= 1 && Integer.parseInt(pkSpAttacks[0]) != 0) {
//
//						for (String spAtt : pkSpAttacks) {
//
//							pAtaOpt = this.attacks.stream().filter(a -> a.getId() == Integer.parseInt(spAtt))
//									.findFirst();
//
//							if (pAtaOpt.isPresent()) {
//
//								pkOpt.get().addSpecialAttack(pAtaOpt.get());
//
//							}
//						}
//					}
//
//					// Other attacks
//					if (pkOtAttacks.length >= 1 && Integer.parseInt(pkOtAttacks[0]) != 0) {
//
//						for (String otAtt : pkOtAttacks) {
//
//							pAtaOpt = this.attacks.stream().filter(a -> a.getId() == Integer.parseInt(otAtt))
//									.findFirst();
//
//							if (pAtaOpt.isPresent()) {
//
//								pkOpt.get().addOtherAttack(pAtaOpt.get());
//
//							}
//						}
//					}
//				}
//			}
//		} catch (IOException e) {
//			System.out.println("Exception reading the file  : " + e.getMessage());
//		} finally {
//			try {
//				if (fileReader != null) {
//					fileReader.close();
//				}
//				if (bufferedReader != null) {
//					bufferedReader.close();
//				}
//				System.out.println("Finished reading readAttacksForEachPokemon");
//			} catch (IOException e) {
//				System.out.println("Exception closing the file  : " + e.getMessage());
//			}
//		}
//	}

	// ==================================== GAME METHODS
	// ====================================

	// Prints all the Pokemon
	public void printPokemon() {
		for (Pokemon pk : this.pokemon) {

			System.out.println(pk.getId() + " - " + pk.getName() + " - " + pk.getTypes().size() + " :");

			pk.getTypes().forEach(tp -> {
				System.out.println(tp.getName());
			});
		}
	}

	// Order Pokemon by type
	public void classPkPerType() {
		ArrayList<Pokemon> steelType = new ArrayList<>();
		ArrayList<Pokemon> waterType = new ArrayList<>();
		ArrayList<Pokemon> fireType = new ArrayList<>();
		ArrayList<Pokemon> bugType = new ArrayList<>();
		ArrayList<Pokemon> dragonType = new ArrayList<>();
		ArrayList<Pokemon> electricType = new ArrayList<>();
		ArrayList<Pokemon> ghostType = new ArrayList<>();
		ArrayList<Pokemon> fairyType = new ArrayList<>();
		ArrayList<Pokemon> iceType = new ArrayList<>();
		ArrayList<Pokemon> fightingType = new ArrayList<>();
		ArrayList<Pokemon> normalType = new ArrayList<>();
		ArrayList<Pokemon> grassType = new ArrayList<>();
		ArrayList<Pokemon> psychicType = new ArrayList<>();
		ArrayList<Pokemon> rockType = new ArrayList<>();
		ArrayList<Pokemon> darkType = new ArrayList<>();
		ArrayList<Pokemon> groundType = new ArrayList<>();
		ArrayList<Pokemon> poisonType = new ArrayList<>();
		ArrayList<Pokemon> flyingType = new ArrayList<>();

		Optional<PokemonType> pkTOpt;

		for (Pokemon pk : this.pokemon) {

			// If 2 types, puts the Pokemon in the two different types
			for (PokemonType pkty : pk.getTypes()) {

				pkTOpt = this.types.stream().filter(t -> t.getId() == pkty.getId()).findFirst();

				if (pkTOpt.isPresent()) {

					switch (pkTOpt.get().getId()) {
					case 1:
						steelType.add(pk);
						break;
					case 2:
						waterType.add(pk);
						break;
					case 3:
						bugType.add(pk);
						break;
					case 4:
						dragonType.add(pk);
						break;
					case 5:
						electricType.add(pk);
						break;
					case 6:
						ghostType.add(pk);
						break;
					case 7:
						fireType.add(pk);
						break;
					case 8:
						fairyType.add(pk);
						break;
					case 9:
						iceType.add(pk);
						break;
					case 10:
						fightingType.add(pk);
						break;
					case 11:
						normalType.add(pk);
						break;
					case 12:
						grassType.add(pk);
						break;
					case 13:
						psychicType.add(pk);
						break;
					case 14:
						rockType.add(pk);
						break;
					case 15:
						darkType.add(pk);
						break;
					case 16:
						groundType.add(pk);
						break;
					case 17:
						poisonType.add(pk);
						break;
					case 18:
						flyingType.add(pk);
						break;

					}
				}
			}
		}

		this.pokemonPerType.put("ACERO", steelType);
		this.pokemonPerType.put("AGUA", waterType);
		this.pokemonPerType.put("BICHO", bugType);
		this.pokemonPerType.put("DRAGON", dragonType);
		this.pokemonPerType.put("ELECTRICO", electricType);
		this.pokemonPerType.put("FANTASMA", ghostType);
		this.pokemonPerType.put("FUEGO", fireType);
		this.pokemonPerType.put("HADA", fairyType);
		this.pokemonPerType.put("HIELO", iceType);
		this.pokemonPerType.put("LUCHA", fightingType);
		this.pokemonPerType.put("NORMAL", normalType);
		this.pokemonPerType.put("PLANTA", grassType);
		this.pokemonPerType.put("PSIQUICO", psychicType);
		this.pokemonPerType.put("ROCA", rockType);
		this.pokemonPerType.put("SINIESTRO", darkType);
		this.pokemonPerType.put("TIERRA", groundType);
		this.pokemonPerType.put("VENENO", poisonType);
		this.pokemonPerType.put("VOLADOR", flyingType);

		System.out.println("Finished reading classPkPerType");
	}

	// Regex to match Pokemon player choices
	public String checkRegexToChoosePokemon() {

		// Player can choose with format : d,d,d,d,d,d, and numbers are between 1 and
		// 807
		String strRegex = "\\b([1-9]";

		// 1 to 99
		for (int i = 1; i <= 9; i++) {

			// The "|" represents OR
			strRegex += "|" + i + "[0-9]";

		}

		// 100 to 799
		for (int i = 10; i <= 79; i++) {

			strRegex += "|" + i + "[0-9]";

		}

		// Complete the rest : 800 to 807
		strRegex += "|800|801|802|803|804|805|806|807)\\b,";

		// Repeat regex 6 times cause 6 Pokemon
		strRegex = repeat(6, strRegex);

		// Remove last ","
		strRegex = StringUtils.chop(strRegex);

		return strRegex;
	}

	// Repeats sequence of the string
	public static String repeat(int count, String with) {
		return new String(new char[count]).replace("\0", with);
	}

	public static String repeat(int count) {
		return repeat(count, " ");
	}

	// Add the attacks that can hit while Pokemon facing is invulnerable
	public static void putCanHitInvulnerableAttacks(Attack attack) {
		List<Integer> canHitWhileInvulnerable = new ArrayList<>();

		switch (attack.getId()) {
		case 16:
		case 18:
		case 87:
		case 239:
		case 327:
		case 479:
		case 542:
			canHitWhileInvulnerable.add(19);
			break;
		}

		attack.setCanHitWhileInvulnerable(canHitWhileInvulnerable);
	}

	// Set the category type of the attack
	public void setCategoryAttackType(Attack attack) {
		switch (attack.getId()) {
		case 19:
			attack.setCategory(AttackCategory.CHARGED);
			break;
		default:
			attack.setCategory(AttackCategory.NORMAL);
		}
	}

	// Set is attack has a special behavior
	public void setAttackSpecialBehavior(Attack attack) {
		switch (attack.getId()) {
		case 19:
			attack.setSpecialBehavior(new FlyBehavior());
			break;
		}
	}

	// ==================================== GAME
	// ====================================

	// Intialize all vars from files
	public void InitiateVars() {
		// Instantiate all Pokemon (if CSV not already created)
		// scrappingWebPokemon();

		// Write all Pokemon to a CSV file
		// writePokemonCSV(this.pokemon);

		// Instantiate all the abilities (if CSV not already created)
//		scrappingWebAllAbs() ;

		// Write all habilities to a CSV file
//		writeAbilitiesCSV(this.abilities);

		// Initialise the diferent lists
		readPkTypes(SAMPLE_CSV_ALL_TYPES);
//		readPokemon(SAMPLE_CSV_ALL_POKEMON);
		readPokemon(SAMPLE_CSV_ALL_POKEMON_TYPES);
		readAbilities(SAMPLE_CSV_ALL_ABS);
		readAttacks(SAMPLE_CSV_ALL_ATTACKS);

		// Adds the abilities for different Pokemon (on the general list)
//		scrappingWebReadAbsFromPokemonAllTables();

		// Append abilities to pokemonList.csv (pokemonList2.csv)
//		AppendAbilities();

		// Adds abilities to Pokemon (on the Pokemon)
		addAbsForEachPokemon(SAMPLE_CSV_ALL_POKEMON_ABS);

		// Puts the lists of different damages for each type
		readPkTypesEffectsToOtherTypes();

		// Adds the type for each Pokemon
//		scrappingWebReadTypeForEachPokemon();

		// Append Pokemon type to pokemonList.csv (pokemonList3.csv)
//		AppendPokemonTypes();

		// Adds types to Pokemon (on the pokemon)
//		addTypesForEachPokemon(SAMPLE_CSV_ALL_POKEMON_TYPES);

		// All the attacks
//		scrappingWebAttacks();

		// Writes the attacks
//		writeAttacksCSV();

		// Takes accents
//		writeAttacksCSV2();

		// All attacks for each Pokemon
//		scrappingWebAttacksForEachPokemon();

		// Writes in a new CSV all the attacks for each Pokemon
//		writeAttacksForEachPokemon();

		// Adds the attacks for each Pokemon
		readAttacksForEachPokemon();

		// Order Pokemon by type
		classPkPerType();
	}

	@SuppressWarnings("resource")
	public void PokemonChoice() {
		printPokemon();
		System.out.println();
		// Instructions / Description of the game
		System.out.println(
				"Escoge 6 Pokémon para el combate (todos están al nivel 100, con sus estadísticas a nivel máximo, como si fueran Pokémon favorecidos, es decir, su mejor versión),\n"
						+ "Puedes escoger el mismo Pokémon 6 veces seguidas. La máquina no podrá.\n"
						+ "Los ataques de cada Pokémon serán iniciados aleatoriamente una vez escojas los Pokémon. Lo que quiere decir que cada Pokémon idéntico, puede tener ataques diferentes. "
						+ "Cada Pokémon tendrá 1 ataque especial, 2 ataques normales y 1 ataque de tipo otro (protección, ataques de estado, etc.).\n"
						+ "Cada Pokémon tendrá ataques que puede tener en los juegos. Es decir, que no habrá un Charizard con surf."
						+ "Solo puedes escoger Pokémon entre el 1 y el 807. Algunos números no están disponibles, así que asegúrate de mirar bien la lista presentada arriba.\n"
						+ "No importa el orden en que los escojas, solo determina el primer Pokémon que va a salir (el primero en escoger)\n"
						+ "Ten en cuenta que por cada Pokémon que escojas, la máquina va a buscar el tipo que más le afecte a tu Pokémon\n"
						+ "Ten en cuenta, que quizás a la máquina no le salgan ataques muy favorecidos, ni a ti tampoco jeje\n"
						+ "Para escoger los Pokémon, utiliza el formato : número,número,número,número,número,número\n => ej : 31,45,3,69,500,666");

		System.out.println("Escoge tus 6 Pokémon :");

		// Player choice
		Scanner sc = new Scanner(System.in);
		String allPkPlayer = sc.next();
		sc.useDelimiter(";|\r?\n|\r");

		// Regex that match the Pokemon choices
		String matchFormatChoice = checkRegexToChoosePokemon();

		// While it doesn't match
		if (!allPkPlayer.matches(matchFormatChoice)) {

			while (!allPkPlayer.matches(matchFormatChoice)) {

				System.out.println(
						"Para escoger los Pokémon, utiliza el formato :número,número,número,número,número,número y los números deben estar entre el 1 y el 807");
				allPkPlayer = sc.next();
				sc.useDelimiter(";|\r?\n|\r");

				System.out.println(allPkPlayer.split(","));
			}
		}

		// Adds the Pokemon to player Pokemon list
		String[] pkByPkPlayer = allPkPlayer.split(",");

		for (String PkID : pkByPkPlayer) {

			Optional<Pokemon> pkOpt;
			pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(PkID)).findFirst();

			if (pkOpt.isPresent()) {

				player.addPokemon(pkOpt.get());

			} else {

				System.out.println("El número marcado no está en la lista : " + PkID);
				System.out.println("Tendrás que volver a escoger tus Pokémon (reinicia el juego)");
			}
		}

		// Pokemon machine choices
		IA.IAPokemonChoice(player.getPokemon(), this.pokemonPerType, this.effectPerTypes);

		// Add attacks to each Pokemon list
		player.addAttacksForEachPokemon();
		IA.addAttacksForEachPokemon();

		// Sets first Pokemon chosen for the combat
		player.setPkCombatting(player.getPokemon().get(0));
		IA.setPkCombatting(IA.getPokemon().get(0));

		player.setPkFacing(IA.getPkCombatting());
		IA.setPkFacing(player.getPkCombatting());

		System.out.println("Player");

		// Shows Pokemon from player
		for (Pokemon p : player.getPokemon()) {

			System.out.println(p.getName() + ":");
			System.out.println();

			// Shows types from Pokemon
			for (PokemonType pt : p.getTypes()) {

				System.out.println(pt.getName());
			}

			// Shows attacks from Pokemon
			for (Attack a : p.getFourPrincipalAttacks()) {

				System.out.println(a.getName() + " - " + a.getStrTypeToPkType().getName());
			}

			System.out.println();
		}

		System.out.println();
		System.out.println("IA");

		// Shows Pokemon from IA
		for (Pokemon p : IA.getPokemon()) {

			System.out.println(p.getName() + ":");
			System.out.println();

			// Shows types from Pokemon
			for (PokemonType pt : p.getTypes()) {

				System.out.println(pt.getName());
			}

			// Shows attacks from Pokemon
			for (Attack a : p.getFourPrincipalAttacks()) {

				System.out.println(a.getName() + " - " + a.getStrTypeToPkType().getName());
			}

			System.out.println();
		}

		System.out.println("Tipo ataques primer Pk IA");

		for (Attack a : IA.getPkCombatting().getFourPrincipalAttacks()) {

			System.out.println(a.getStrTypeToPkType().getName());
		}

		// Put attacks into different lists to determine damage from attacks
		IA.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);
		player.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);

		// IA Prepares best attack against Pokemon player
		IA.prepareBestAttackIA();

		System.out.println("Next attack from machine :");
		System.out.println(IA.getPkCombatting().getNextMovement().getName() + " - "
				+ IA.getPkCombatting().getNextMovement().getStrTypeToPkType().getName());
	}

	// Main battle (start battle)
	public void startBattle() {

		int nbRound = 1;
		Scanner sc = new Scanner(System.in);

		while (IA.getPokemon().size() >= 1 && player.getPokemon().size() >= 1) {

			System.out.println("----------------------------------");
			System.out.println("Let's start round nº : " + nbRound);
			System.out.println("----------------------------------");

			boolean isStartTurn = true;

			boolean playerIsCharging = player.getPkCombatting().getIsChargingAttackForNextRound();
			int attackChoice = playerIsCharging ? 1 : getPlayerChoice(sc);

			if (attackChoice == 1) {
				handleAttackTurn(sc, isStartTurn);
			} else {
				boolean cancelled = !handleChangeTurn(sc, isStartTurn);

				if (cancelled) {
					System.out.println("Cambio cancelado. Regresando al menú principal...");
					attackChoice = -1; // show again options : attack/change
					// Stay in the same round
					nbRound--;
				}
			}

			// Get next round
			nbRound++;
		}
	}

	// Handle attacks from the turn
	private void handleAttackTurn(Scanner sc, boolean isStartTurn) {

		int attackId = 0;

		if (!player.getPkCombatting().getIsChargingAttackForNextRound()) {
			attackId = getValidAttackId(sc, player);
		}

		printPokemonStates();

		Pokemon pkPlayer = player.getPkCombatting();
		Pokemon pkIA = IA.getPkCombatting();

		boolean canAttackPlayer = checkCanAttackFromStatusCondition(pkPlayer);
		boolean canAttackIA = checkCanAttackFromStatusCondition(pkIA);

		// Apply status effects from beginning of the turn + prepare effectiveness and
		// bonus from attacks chosen
		if (canAttackPlayer) {
			applyEffectStatusCondition(pkPlayer);
			player.prepareBestAttackPlayer(attackId);
		}

		// IA can decide to change Pokemon
		if (IA.getPkCombatting().getIsChargingAttackForNextRound() == false && tryIAChange()) {
			// If change realized => don't attack
			return;
		}

		prepareIAIfPossible(canAttackIA, isStartTurn);

		// Handle normal attack sequence
		handleNormalAttackSequence(sc);

		// Reset status conditions
		if (canAttackPlayer) {
			resetEffectStatusCondition(pkPlayer);
		}
		resetIAIfPossible(canAttackIA);
	}

	// Handle attack from IA when player is changing the Pokemon
	private boolean handleChangeTurn(Scanner sc, boolean isStartTurn) {

		Pokemon pkIA = IA.getPkCombatting();

		boolean changed = changePokemon(sc);

		if (!changed)
			return false; // player cancelled the change (return to start options)

		boolean canIA = checkCanAttackFromStatusCondition(pkIA);

		prepareIAIfPossible(canIA, isStartTurn);

		handleChangeSequence(sc); // only IA attacks

		resetIAIfPossible(canIA);

		return true;
	}

	// Prepare attack from IA if can attack (after checking status conditions from
	// the beginning of the turn)
	private void prepareIAIfPossible(boolean canIA, boolean isStartTurn) {
		if (canIA) {
			applyEffectStatusCondition(IA.getPkCombatting());
			IA.prepareBestAttackIA();
		}
	}

	// Reset status effects from IA at the end of the turn
	private void resetIAIfPossible(boolean canIA) {
		if (canIA) {
			resetEffectStatusCondition(IA.getPkCombatting());
		}
	}

	// Get the player choice (attack or change Pokemon)
	private int getPlayerChoice(Scanner sc) {
		System.out.println("Quieres atacar (1) o cambiar de Pokémon (2) :");
		int choice = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");
		return choice;
	}

	// Check validity of attack id from player Pokemon
	private int getValidAttackId(Scanner sc, Player player) {
		System.out.println("Escoge un ataque :");
		player.printAttacksFromPokemonCombating();
		int ataqueId = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");

		while (!player.getPkCombatting().getFourIdAttacks().contains(ataqueId)) {
			System.out.println("Escoge un ataque que tenga el Pokemon");
			ataqueId = sc.nextInt();
			sc.useDelimiter(";|\r?\n|\r");
		}
		return ataqueId;
	}

	// Print Pokemon states (for debug)
	private void printPokemonStates() {
		System.out.println("Estado del Pokémon del jugador : "
				+ player.getPkCombatting().getStatusCondition().getStatusCondition());
		System.out.println(
				"Estado del Pokémon de la máquina : " + IA.getPkCombatting().getStatusCondition().getStatusCondition());
	}

	// Check if Pokemon combating can attack due to effect from status condition
	private boolean checkCanAttackFromStatusCondition(Pokemon attacker) {
		boolean canAttackPk = attacker.getStatusCondition()
				.doEffectStatusCondition(attacker.getStatusCondition().getStatusCondition());

		return canAttackPk;
	}

	// Apply effect of status condition at the beginning of the turn for Pokemon
	// combating
	private void applyEffectStatusCondition(Pokemon attacker) {
		attacker.checkEffectsStatusCondition(true);
	}

	// Apply effect of status condition at the end of the turn for Pokemon
	// combating
	private void resetEffectStatusCondition(Pokemon attacker) {
		attacker.checkEffectsStatusCondition(false);
	}

	// Player attack first
	private boolean playerCanAttackFirst() {
		return player.getPkCombatting().getCanAttack()
				&& player.getPkCombatting().getSpeed() > IA.getPkCombatting().getSpeed();
	}

	// Handle normal attack sequence
	private void handleNormalAttackSequence(Scanner sc) {
		boolean playerFirst = playerCanAttackFirst();

		Player first = playerFirst ? player : IA;
		Player second = playerFirst ? IA : player;

		// 1. Get order of players
		boolean turnShouldEnd = attackAndCheckIfTurnEnds(first, second, sc);

		// 2. Second player attacks if turn can continue
		if (!turnShouldEnd) {
			attackAndCheckIfTurnEnds(second, first, sc);
		}

		// 3. Reset the flinch/retreat
		IA.getPkCombatting().setHasRetreated(false);
		player.getPkCombatting().setHasRetreated(false);
	}

	// Check if Pokemon can attack + do retaliation
	private boolean attackAndCheckIfTurnEnds(Player attacker, Player defender, Scanner sc) {

		Pokemon pk = attacker.getPkCombatting();

		// If retreated, cannot attack, but turn continues
		if (pk.getHasRetreated()) {
			System.out.println(pk.getName() + " retrocedió.");
			return false; // turn continues
		}

		// If Pokemon facing is debilitated, force change and ends turn
		if (pk.getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			checkForcedPokemonChange(sc);
			return true; // turn ends
		}

		// Execute attack
		if (attacker == player)
			handlePlayerRetaliation();
		else
			handleIARetaliation();

		// If defender got Pokemon debilitated during the attack, force change and ends
		// turn
		if (defender.getPkCombatting().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			checkForcedPokemonChange(sc);
			return true; // turn ends
		}

		return false; // turn continues
	}

	// Handle change sequence
	private void handleChangeSequence(Scanner sc) {
		handleIARetaliation();
		checkForcedPokemonChange(sc);
	}

	// Player handle attack
	private void handlePlayerRetaliation() {

		if (player.getPkCombatting().getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED) {

			PkVPk battleVS = new PkVPk(player.getPkCombatting(), player.getPkFacing());
			player.setBattleVS(battleVS);

			// Get probability of attacking (we already checked for status conditions. Now
			// we do it for evasion/accuracy)
			player.getProbabiltyOfAttacking();

			if (player.getPkCombatting().getCanAttack()) {

				System.out.println(ANSI_GREEN + "Pokemon player can attack" + ANSI_RESET);
				player.applyDamage();
			} else {
				System.out.println(ANSI_RED + "Pokemon player cannot attack" + ANSI_RESET);
			}

			player.getPkCombatting().restartParametersEffect();
		} else {
			System.out.println(ANSI_RED + "Pokemon player is debilitated" + ANSI_RESET);
		}
	}

	// IA handle attack
	private void handleIARetaliation() {
		if (IA.getPkCombatting().getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED) {

			PkVPk battleVS = new PkVPk(IA.getPkCombatting(), IA.getPkFacing());
			IA.setBattleVS(battleVS);

			// Get probability of attacking (we already checked for status conditions. Now
			// we do it for evasion/accuracy)
			IA.getProbabiltyOfAttacking();

			if (IA.getPkCombatting().getCanAttack()) {

				System.out.println(ANSI_GREEN + "Pokemon IA can attack" + ANSI_RESET);
				IA.applyDamage();

			} else {
				System.out.println(ANSI_RED + "Pokemon IA cannot attack" + ANSI_RESET);
			}

			IA.getPkCombatting().restartParametersEffect();
		} else {

			System.out.println(ANSI_RED + "Pokemon IA is debilitated" + ANSI_RESET);
		}
	}

	// Check if needed to chose a new Pokemon (ex : combating Pokemon dies from
	// burning in final turn while flying, etc.)
	private void checkForcedPokemonChange(Scanner sc) {

		// Player dies
		if (player.getPkCombatting().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			System.out.println(player.getPkCombatting().getName() + " fue derrotado.");
			System.out.println("¿Qué Pokémon deberías escoger?");

			boolean changed = false;
			while (!changed) {
				changed = changePokemon(sc); // chose a new Pokemon (mandatory)
			}
		}

		// IA dies
		if (IA.getPkCombatting().getStatusCondition().getStatusCondition() == StatusConditions.DEBILITATED) {
			System.out.println(IA.getPkCombatting().getName() + " fue derrotado.");

			Pokemon newIA = IA.decideBestChangePokemon(player.getPkCombatting(), effectPerTypes);

			// If decideBestChangePokemon returns null => choose the first Pokemon available
			if (newIA == null) {
				newIA = IA.getPokemon().stream()
						.filter(pk -> pk.getStatusCondition().getStatusCondition() != StatusConditions.DEBILITATED)
						.findFirst().get();
			}

			System.out.println("IA eligió a " + newIA.getName());

			IA.setPkCombatting(newIA);
			IA.setPkFacing(player.getPkCombatting());

			player.setPkFacing(IA.getPkCombatting());
			refreshAttackOrders();
			IA.prepareBestAttackIA();
		}
	}

	// Change Pokemon
	private boolean changePokemon(Scanner sc) {

		while (true) {

			System.out.println("\n--- Cambio de Pokémon ---");
			player.printPokemonInfo();
			System.out.println("Escribe el ID del Pokémon a usar o '0' para cancelar : ");

			int id = sc.nextInt();
			sc.useDelimiter(";|\r?\n|\r");

			if (id == 0) {
				return false; // cancel change
			}

			// Not allowed to chose the Pokemon combating
			if (player.getPkCombatting().getId() == id) {
				System.out.println("Ese Pokémon ya está combatiendo. Escoge otro.");
				continue;
			}

			Optional<Pokemon> opt = player.getPokemon().stream().filter(p -> p.getId() == id).findFirst();

			if (opt.isEmpty()) {
				System.out.println("No escogiste un Pokémon válido. Escoge un Pokémon de los que posees :");
				continue;
			}

			Pokemon selected = opt.get();

			System.out.println("Jugador eligió a " + selected.getName());

			// Update Pokemon combating
			player.setPkCombatting(selected);

			// Update facing Pokemon
			player.setPkFacing(IA.getPkCombatting());
			IA.setPkFacing(player.getPkCombatting());

			refreshAttackOrders();

			return true; // change successfuly
		}
	}

	// Try IA to change Pokemon. Return true if IA changed Pokemon. If return false,
	// will attack normally
	private boolean tryIAChange() {
		// 15% of probability to change Pokemon
		int randomNumber = (int) (Math.random() * 100) + 1;
		if (randomNumber > 15) {
			System.out.println("IA no cambiará (probabilidad muy baja)");
			return false; // don't change
		}

		// Check from others Pokemon from the team to see a potential better option
		Pokemon changeTo = IA.decideBestChangePokemon(player.getPkCombatting(), this.effectPerTypes);

		if (changeTo == null) {
			System.out.println("IA no tiene un mejor Pokémon al que cambiar");
			return false; // doesn't exists a better option
		}

		// Do Pokemon change => update Pokemon comabting from IA, etc.
		System.out.println("IA cambió a " + changeTo.getName());
		IA.setPkCombatting(changeTo);

		// Update Pokemon facing for each player
		IA.setPkFacing(player.getPkCombatting());
		player.setPkFacing(IA.getPkCombatting());

		refreshAttackOrders();

		return true;
	}

	private void refreshAttackOrders() {
		IA.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);
		player.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);
	}

	// Tests for attacks (466 Electivire, 398 Staraptor, 6 Charizard, 127 Pinsir,
	// 123 Scyther, 16 Pidgey, 95 Onix, 523 Zebstrika)
	public void doTest() {
		// Sets the same Pk
		String allPkPlayer = "6,6,6";
		String allPkIA = "523,523,523";

		String[] pkByPkPlayer = allPkPlayer.split(",");
		Map<Integer, Integer> pkCount = new HashMap<>();

		// Add Pokemon to player
		for (String PkID : pkByPkPlayer) {

			int baseId = Integer.parseInt(PkID);

			Optional<Pokemon> pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == baseId).findFirst();

			if (pkOpt.isPresent()) {

				// Creates a new instance of Pokemon in memory (otherwise there are problems of
				// duplications)
				Pokemon newPk = new Pokemon(pkOpt.get());

				// Increase count for this base ID
				int count = pkCount.getOrDefault(baseId, 0);

				// If it's not the first one, modify ID
				if (count > 0) {
					int newId = baseId * 1000 + count;
					newPk.setId(newId);
				}

				// Update repetitions counter
				pkCount.put(baseId, count + 1);

				// Add to player team
				player.addPokemon(newPk);
			}
		}

		// Sets first Pokemon to combat for player
		player.setPkCombatting(player.getPokemon().get(0));

		// Sets the attacks to pokemon's player to test
		for (Pokemon pk : player.getPokemon()) {

			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 7).findFirst().get());
			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 9).findFirst().get());
			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 19).findFirst().get());

			// Adds the Ids of attacks chosed in a list
//			for (Attack ataChosed : player.getPkCombatting().getFourPrincipalAttacks()) {
//
//				player.getPkCombatting().addIdAttack(ataChosed.getId());
//
//			}
			for (Attack ataChosed : pk.getFourPrincipalAttacks()) {
				pk.addIdAttack(ataChosed.getId());
			}

		}

		String[] pkByPkIA = allPkIA.split(",");

		// Add Pokemon to IA
		for (String PkID : pkByPkIA) {

			Optional<Pokemon> pkOpt;

			pkOpt = this.pokemon.stream().filter(pk -> pk.getId() == Integer.parseInt(PkID)).findFirst();

			if (pkOpt.isPresent()) {

				// Creates a new instance of Pokemon in memory (otherwise there are problems of
				// duplications)
				IA.addPokemon(new Pokemon(pkOpt.get()));

			}
		}

		// Sets first Pokemon to combat for IA
		IA.setPkCombatting(IA.getPokemon().get(0));

		for (Pokemon pk : IA.getPokemon()) {

//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 7).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 9).findFirst().get());
//			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 19).findFirst().get());
//			pk.addAttacks(pk.getSpecialAttacks().stream().filter(af -> af.getId() == 16).findFirst().get());
			pk.addAttacks(pk.getPhysicalAttacks().stream().filter(af -> af.getId() == 23).findFirst().get());

			// Adds the Ids of attacks chosen in a list
			for (Attack ataChosed : IA.getPkCombatting().getFourPrincipalAttacks()) {

				IA.getPkCombatting().addIdAttack(ataChosed.getId());
			}
		}

		// Sets Pokemon facing to each other
		player.setPkFacing(IA.getPokemon().get(0));
		IA.setPkFacing(player.getPokemon().get(0));

		IA.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);
		player.orderAttacksFromDammageLevelPokemon(this.effectPerTypes);

	}
}