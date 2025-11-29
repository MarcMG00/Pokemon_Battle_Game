package pokemon.importData;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pokemon.model.Ability;
import pokemon.model.Attack;
import pokemon.model.Pokemon;
import pokemon.model.PokemonType;

public class ScrappingWeb {
	
	private ArrayList<Ability> abilities;
	private ArrayList<PokemonType> types;
	private ArrayList<Attack> attacks;

	public ScrappingWeb() {
		this.abilities = new ArrayList<>();
		this.types = new ArrayList<>();
		this.attacks = new ArrayList<>();
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}

	public ArrayList<PokemonType> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PokemonType> types) {
		this.types = types;
	}

	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

	public void setAttacks(ArrayList<Attack> attacks) {
		this.attacks = attacks;
	}

	// ==================================== SCRAPPING WEB
	// ====================================

	// Add to Pokemon list all the Pokemon from 1 to 809
	public ArrayList<Pokemon> scrappingWebPokemon() {
		ArrayList<Pokemon> pokemon = new ArrayList<>();
		
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

				Element pokemonEle = document.select(".pkmain").get(classPkMain);

				// Gets only the first 6 statistics elements on the table with "right" class
				List<Object> pokemonStats = pokemonEle.select(".right").stream().limit(6).collect(Collectors.toList());

				// Create Pokemon
				Pokemon pk = new Pokemon(id, name, Integer.parseInt(((Element) pokemonStats.get(0)).text()),
						Integer.parseInt(((Element) pokemonStats.get(1)).text()),
						Integer.parseInt(((Element) pokemonStats.get(2)).text()),
						Integer.parseInt(((Element) pokemonStats.get(3)).text()),
						Integer.parseInt(((Element) pokemonStats.get(4)).text()),
						Integer.parseInt(((Element) pokemonStats.get(5)).text()));
				pokemon.add(pk);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return pokemon;
	}

	// Add to ability list all the abilities (309)
	public ArrayList<Ability> scrappingWebAllAbs() {
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
		
		return this.abilities;
	}

	// Reads all the possible abilities for different Pokemon and put it to dico
	public HashMap<String, HashMap<String, ArrayList<Ability>>> scrappingWebReadAbsFromPokemonAllTables() {
		HashMap<String, HashMap<String, ArrayList<Ability>>> abilitiesPerPokemon = new HashMap<>();
		
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
						if (!abilitiesPerPokemon.containsKey(tPk.select("td").get(0).text())) {

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
							if (!abilitiesPerPokemon.containsKey(tPk.select("td").get(0).text())) {

								abilitiesPerPokemon.put(tPk.select("td").get(0).text(), abs);

							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return abilitiesPerPokemon;
	}

	// Read all the possible types for different Pokemon and put it to dico
	public HashMap<String, ArrayList<PokemonType>> scrappingWebReadTypeForEachPokemon() {
		HashMap<String, ArrayList<PokemonType>> pokemonTypePerPokemon = new HashMap<>();
		
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

						if (!pokemonTypePerPokemon.containsKey(tPk.select("td").get(0).text())) {

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
							if (!pokemonTypePerPokemon.containsKey(tPk.select("td").get(0).text())) {

								pokemonTypePerPokemon.put(tPk.select("td").get(0).text(), pokemonTypes);

							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pokemonTypePerPokemon;
	}

	// Gets all the attacks
	public ArrayList<Attack> scrappingWebAttacks() {
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
		
		return this.attacks;
	}

	// Gets all the attacks for each Pokemon
	// TD + TH [1-(235,664,789,790),6-722,7-650,8-494,9-387,10-152,11-1]
	public HashMap<Integer, HashMap<String, ArrayList<Integer>>> scrappingWebAttacksForEachPokemon() {
		HashMap<Integer, HashMap<String, ArrayList<Integer>>> attacksPerPokemon = new HashMap<>();
		
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
					attacksPerPokemon.put(iPk, AllAttacks);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return attacksPerPokemon;
	}
}
