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

import pokemon.enums.EstadosEnum;

public class Game {
	private static final String SAMPLE_CSV_ALL_POKEMON = "./data/pokemonList.csv";
	private static final String SAMPLE_CSV_ALL_POKEMON_HABS = "./data/pokemonList2.csv";
	private static final String SAMPLE_CSV_ALL_HABS = "./data/habsList.csv";
	private static final String SAMPLE_CSV_ALL_TYPES = "./data/typesList.txt";
	private static final String SAMPLE_CSV_ALL_POKEMON_TYPES = "./data/pokemonList3.csv";
	private static final String SAMPLE_CSV_ALL_ATTACS = "./data/attacsList.txt";
	private static final String SAMPLE_CSV_ALL_ATTACS_FOREACH_POKEMON = "./data/attacsForeachPokemon.txt";

	private ArrayList<Pokemon> pokemons;
	private HashMap<String, ArrayList<PokemonType>> tipoPkPorPokemon;
	private ArrayList<Habilidad> habilidades;
	private HashMap<String, HashMap<String, ArrayList<Habilidad>>> habilidadPorPokemon;
	private ArrayList<PokemonType> types;
	private HashMap<String, HashMap<String, ArrayList<PokemonType>>> efectoPorTipos;
	private ArrayList<Ataque> ataques;
	private HashMap<Integer, HashMap<String, ArrayList<Integer>>> ataquesPorPokemon;
	private HashMap<String, ArrayList<Pokemon>> pokemonsPorTipo;

	private Player player;
	private IAPlayer IA;

	public Game() {
		this.pokemons = new ArrayList<>();
		this.tipoPkPorPokemon = new HashMap<>();
		this.habilidades = new ArrayList<>();
		this.habilidadPorPokemon = new HashMap<>();
		this.types = new ArrayList<>();
		this.efectoPorTipos = new HashMap<>();
		this.ataques = new ArrayList<>();
		this.ataquesPorPokemon = new HashMap<>();
		this.player = new Player();
		this.IA = new IAPlayer();
		this.pokemonsPorTipo = new HashMap<>();
	}

	public ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setPokemons(ArrayList<Pokemon> pokemons) {
		this.pokemons = pokemons;
	}

	public ArrayList<Habilidad> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(ArrayList<Habilidad> habilidades) {
		this.habilidades = habilidades;
	}

	public HashMap<String, HashMap<String, ArrayList<Habilidad>>> getHabilidadPorPokemon() {
		return habilidadPorPokemon;
	}

	public void setHabilidadPorPokemon(HashMap<String, HashMap<String, ArrayList<Habilidad>>> habilidadPorPokemon) {
		this.habilidadPorPokemon = habilidadPorPokemon;
	}

	public HashMap<String, ArrayList<PokemonType>> getTipoPkPorPokemon() {
		return tipoPkPorPokemon;
	}

	public void setTipoPkPorPokemon(HashMap<String, ArrayList<PokemonType>> typePorPokemon) {
		this.tipoPkPorPokemon = typePorPokemon;
	}

	public ArrayList<PokemonType> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PokemonType> types) {
		this.types = types;
	}

	public HashMap<String, HashMap<String, ArrayList<PokemonType>>> getEfectoPorTipos() {
		return efectoPorTipos;
	}

	public void setEfectoPorTipos(HashMap<String, HashMap<String, ArrayList<PokemonType>>> efectoPorTipos) {
		this.efectoPorTipos = efectoPorTipos;
	}

	public ArrayList<Ataque> getAtaques() {
		return ataques;
	}

	public void setAtaques(ArrayList<Ataque> ataques) {
		this.ataques = ataques;
	}

	public HashMap<Integer, HashMap<String, ArrayList<Integer>>> getAtaquesPorPokemon() {
		return ataquesPorPokemon;
	}

	public void setAtaquesPorPokemon(HashMap<Integer, HashMap<String, ArrayList<Integer>>> ataquesPorPokemon) {
		this.ataquesPorPokemon = ataquesPorPokemon;
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

	public HashMap<String, ArrayList<Pokemon>> getPokemonsPorTipo() {
		return pokemonsPorTipo;
	}

	public void setPokemonsPorTipo(HashMap<String, ArrayList<Pokemon>> pokemonsPorTipo) {
		this.pokemonsPorTipo = pokemonsPorTipo;
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
				// Gets the "pkmain" class we are interested for estadistics
				int classPkMain = 0;

				// Gets the "pktitle" class for the Pokemon name
				Element pokemonName = document.selectFirst(".pktitle");
				String name = pokemonName.select(".mini").text();

				// Counts number of "pkmain" class (there are more or less mega evolutions
				// depending on the Pokemon, so there
				// are more or less "pkmain" class
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

				// Prints the content (the current pokemon treating)
//				System.out.println("================Pkmain name================");
//				System.out.println(name + "-" + id);

				// Gets only the first 6 estadistics elements on the table with "right" class
				List<Object> pokemonStats = pokemon.select(".right").stream().limit(6).collect(Collectors.toList());

				// Create Pokemon
				Pokemon pk = new Pokemon(id, name, Integer.parseInt(((Element) pokemonStats.get(0)).text()),
						Integer.parseInt(((Element) pokemonStats.get(1)).text()),
						Integer.parseInt(((Element) pokemonStats.get(2)).text()),
						Integer.parseInt(((Element) pokemonStats.get(3)).text()),
						Integer.parseInt(((Element) pokemonStats.get(4)).text()),
						Integer.parseInt(((Element) pokemonStats.get(5)).text()));
				this.pokemons.add(pk);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Add to hability list all the habilities (309)
	public void scrappingWebAllHabs() {
		try {
			String url = "https://www.wikidex.net/wiki/Lista_de_habilidades";
			// Gets the url
			Document document = Jsoup.connect(url).get();
			// Selects the habilities table
			Elements table = document.select(".tabpokemon");
			// Selects all the "tr" in the table
			Elements elementObjTr = table.first().children().select("tr");

			for (Element tr : elementObjTr) {
				// We not consider the first "tr" cause it represents the titles (# represents
				// the column name for habilities Id)
				if (!tr.firstElementChild().text().equals("#")) {
					// We get the 2 first "td" (representing the name and the description effect) -
					// we remove all diacritics
					Habilidad hab = new Habilidad(Integer.parseInt(tr.firstElementChild().text()),
							Normalizer.normalize(tr.select("td>a").get(0).text(), Normalizer.Form.NFKD)
									.replaceAll("[^\\p{ASCII}]", ""),
							Normalizer.normalize(tr.select("td").get(2).text(), Normalizer.Form.NFKD)
									.replaceAll("[^\\p{ASCII}]", ""));
					this.habilidades.add(hab);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Reads all the possible habilities for diferent Pokemon and put it to dico
	public HashMap<String, HashMap<String, ArrayList<Habilidad>>> scrappingWebReadHabsFromPokemonAllTables() {
		try {
			String url = "https://www.wikidex.net/wiki/Lista_de_Pok%C3%A9mon_con_sus_habilidades";
			// Gets the url
			Document document = Jsoup.connect(url).get();
			// Select all the pokemon tables we are interested (each table represents a
			// generation, we have a total of 809 pokemon in 7 tables)
			for (int tab = 0; tab < 7; tab++) {

				// We get each table
				Element table = document.select(".tabpokemon").get(tab);
				// We get each <tr> from the table concerned
				Elements elementObjTr = table.select("tr");

				for (Element tPk : elementObjTr) {
					// We don't take the first <tr> line (represents the name of the columns)
					if (!tPk.firstElementChild().text().equals("N�")) {
						// We don't want the mega evolutions (they have the same number as the last
						// evolution of the Pokemon)
						if (!this.habilidadPorPokemon.containsKey(tPk.select("td").get(0).text())) {
							// Hash Map to put al the habilities for each Pokemon
							HashMap<String, ArrayList<Habilidad>> habs = new HashMap<>();
							// List of normal habilities for each pokemon
							ArrayList<Habilidad> pokemonHabs = new ArrayList<>();
							// List of oculted habilities for each pokemon
							ArrayList<Habilidad> pokemonHabsOcultas = new ArrayList<>();
							// First hability
							Optional<Habilidad> h;
							// Second hability
							Habilidad h2;
							// Oculted hability
							Optional<Habilidad> hO;

//								System.out.println("================Pkmain id================");
//								System.out.println(tPk.select("td").get(0).text());
//								System.out.println(tPk.select("td").size() + " columns");

							// Depending on the number of columns, there are more or less habilities (each
							// hability is in a column)
							int nbTd = tPk.select("td").size();

							switch (nbTd) {
							// There's only one hability, so we put it as normal hability
							case 6:
								// We remove all the digits from the hability and we search the hability in our
								// list of habilities (we compare by name)
								h = this.habilidades
										.stream().filter(
												habil -> habil.getNombreHab()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(5).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();
								// Habilities are optional, so we do a condition of nullability
								if (h.isPresent()) {
									// We put the hability in the normal list of habilities
									h2 = new Habilidad(h.get().getIdHab(), h.get().getNombreHab(),
											h.get().getDescripcionHab());
									pokemonHabs.add(h2);
								}
								// We put the normal list of habilities in the dico as "Habilidad" (normal
								// hability)
								habs.put("Habilidad", pokemonHabs);
								break;
							case 7:
								h = this.habilidades
										.stream().filter(
												habil -> habil.getNombreHab()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(5).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();
								if (h.isPresent()) {
									h2 = new Habilidad(h.get().getIdHab(), h.get().getNombreHab(),
											h.get().getDescripcionHab());
									pokemonHabs.add(h2);
								}
								habs.put("Habilidad", pokemonHabs);

								hO = this.habilidades
										.stream().filter(
												habil -> habil.getNombreHab()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(6).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();
								if (hO.isPresent()) {
									h2 = new Habilidad(hO.get().getIdHab(), hO.get().getNombreHab(),
											hO.get().getDescripcionHab());
									pokemonHabsOcultas.add(h2);
								}
								habs.put("Habilidad oculta", pokemonHabsOcultas);
								break;
							case 8:
								h = this.habilidades
										.stream().filter(
												habil -> habil.getNombreHab()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(5).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();
								if (h.isPresent()) {
									h2 = new Habilidad(h.get().getIdHab(), h.get().getNombreHab(),
											h.get().getDescripcionHab());
									pokemonHabs.add(h2);
								}

								h = this.habilidades
										.stream().filter(
												habil -> habil.getNombreHab()
														.equals(Normalizer
																.normalize(
																		tPk.select("td").get(6).text()
																				.replaceAll("[0-9]", "").toUpperCase(),
																		Normalizer.Form.NFKD)
																.replaceAll("[^\\p{ASCII}]", "")))
										.findFirst();
								if (h.isPresent()) {
									h2 = new Habilidad(h.get().getIdHab(), h.get().getNombreHab(),
											h.get().getDescripcionHab());
									pokemonHabs.add(h2);
								}
								habs.put("Habilidad", pokemonHabs);

								// There's only (at the moment) one Pokemon that has 2 oculted habilities. We do
								// a split (with "/", cause there are not 2 columns for each hability)
								// For the first hability, we remove the last white space (replaceAll("\\s","")
								// cause there are not spaces in the word) before the ("/")
								// For the second hability, we remove the first white space (trim()) after the
								// ("/")
								if (tPk.select("td").get(0).text().equals("744")) {
									hO = this.habilidades
											.stream().filter(
													habil -> habil
															.getNombreHab().equals(
																	Normalizer
																			.normalize(
																					tPk.select("td").get(7).text()
																							.split("/")[0]
																									.replaceAll("\\s",
																											"")
																									.replaceAll("[0-9]",
																											"")
																									.toUpperCase(),
																					Normalizer.Form.NFKD)
																			.replaceAll("[^\\p{ASCII}]", "")))
											.findFirst();
									if (hO.isPresent()) {
										h2 = new Habilidad(hO.get().getIdHab(), hO.get().getNombreHab(),
												hO.get().getDescripcionHab());
										pokemonHabsOcultas.add(h2);
									}
									hO = this.habilidades
											.stream().filter(
													habil -> habil
															.getNombreHab().equals(
																	Normalizer
																			.normalize(
																					tPk.select("td").get(7).text()
																							.split("/")[1]
																									.trim()
																									.replaceAll("[0-9]",
																											"")
																									.toUpperCase(),
																					Normalizer.Form.NFKD)
																			.replaceAll("[^\\p{ASCII}]", "")))
											.findFirst();
									if (hO.isPresent()) {
										h2 = new Habilidad(hO.get().getIdHab(), hO.get().getNombreHab(),
												hO.get().getDescripcionHab());
										pokemonHabsOcultas.add(h2);
									}
								} else {
									hO = this.habilidades.stream()
											.filter(habil -> habil.getNombreHab()
													.equals(Normalizer
															.normalize(
																	tPk.select("td").get(7).text()
																			.replaceAll("[0-9]", "").toUpperCase(),
																	Normalizer.Form.NFKD)
															.replaceAll("[^\\p{ASCII}]", "")))
											.findFirst();
									if (hO.isPresent()) {
										h2 = new Habilidad(hO.get().getIdHab(), hO.get().getNombreHab(),
												hO.get().getDescripcionHab());
										pokemonHabsOcultas.add(h2);
									}
								}
								habs.put("Habilidad oculta", pokemonHabsOcultas);
								break;
							}

							// We put the dico in our principal var
							if (!this.habilidadPorPokemon.containsKey(tPk.select("td").get(0).text())) {
								this.habilidadPorPokemon.put(tPk.select("td").get(0).text(), habs);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.habilidadPorPokemon;
	}

	// Read all the possible types for diferent Pokemon and put it to dico
	public HashMap<String, ArrayList<PokemonType>> scrappingWebReadTypeForeachPokemon() {
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
					if (!tPk.firstElementChild().text().equals("N�")) {
						if (!this.tipoPkPorPokemon.containsKey(tPk.select("td").get(0).text())) {
							ArrayList<PokemonType> pokemonTypes = new ArrayList<>();
							Optional<PokemonType> optPkT1;
							Optional<PokemonType> optPkT2;
							Element aAfterTD;

//									System.out.println("================Pkmain id================");
//									System.out.println(tPk.select("td").get(0).text());

							// Gets first type (there will be one everytime at position 3)
							optPkT1 = this.types.stream()
									.filter(tp -> tp.getNombreTipo()
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
										.filter(tp -> tp.getNombreTipo().equals(Normalizer
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
							if (!this.tipoPkPorPokemon.containsKey(tPk.select("td").get(0).text())) {
								this.tipoPkPorPokemon.put(tPk.select("td").get(0).text(), pokemonTypes);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.tipoPkPorPokemon;
	}

	// Gets all the attacs
	public void scrappingWebAttacs() {
		try {
			String url = "https://www.pokexperto.net/index2.php?seccion=nds/movimientos_pokemon";
			// Gets the url and converts to String (HTML)
			// Names are in spanish and english, so we remove the english version with a
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
				// Skip first line (titles of columns) andattac number 905 (there are no values)
				if (!ta.firstElementChild().text().equals("#") && !ta.select("th").text().equals("905")) {

					// Sets the new Attack
					Ataque atq = new Ataque(Integer.parseInt(ta.select("th").text()), ta.select("td").get(0).text(),
							ta.select("td").get(1).attr("sorttable_customkey").toUpperCase(),
							ta.select("td").get(3).text().equals("--") ? 0
									: Integer.parseInt(ta.select("td").get(3).text()),
							Integer.parseInt(ta.select("td").get(4).text()),
							ta.select("td").get(5).text().equals("--") ? 0
									: Integer.parseInt(ta.select("td").get(5).text().substring(0,
											ta.select("td").get(5).text().length() - 1)),
							ta.select("td").get(6).text().replace(",", ""));

					// There are some attacs that have 2 base types, so we put it in a list
					Elements bases = ta.select("td").get(2).select("img");
					for (Element b : bases) {
						atq.addBase(b.attr("alt"));
					}

					// Adds the attac to the general attacs list
					this.ataques.add(atq);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Gets all the attacs for each Pokemon
	// TD + TH [1-(235,664,789,790),6-722,7-650,8-494,9-387,10-152,
	// 11-1]
	public void scrappingWebAttacsForEachPokemon() {
		try {

			for (int iP = 1; iP < 808; iP++) {

				// Some Pokemon have only one attac (they are treated manually)
				if (iP != 235 && iP != 664 && iP != 789 && iP != 790) {

					HashMap<String, ArrayList<Integer>> todosLosAtas = new HashMap<>();

					ArrayList<Integer> ataFisicos = new ArrayList<>();
					ArrayList<Integer> ataEspeciales = new ArrayList<>();
					ArrayList<Integer> ataOtros = new ArrayList<>();

					// URL foreach Pokemon
					String url = "https://www.pokexperto.net/index2.php?seccion=nds/nationaldex/movimientos_pokemon&pk="
							+ iP;

					// We convert the page in a string (HTML)
					// We remove the english name version
					String documentStr = Jsoup.connect(url).get().toString().replaceAll("<br><[^>]*>.*?/td>", "</td>");

					// We parse the string to a Document
					Document document = Jsoup.parse(documentStr);

					// There are 3 tables (fisic, special and others)
					Element tableFisicos = document.getElementsByClass("bordetodos").get(0);
					Element tableEspeciales = document.getElementsByClass("bordetodos").get(1);
					Element tableOtros = document.getElementsByClass("bordetodos").get(2);

					// We are interested only in <tr>
					Elements allTrF = tableFisicos.select("tr");
					Elements allTrE = tableEspeciales.select("tr");
					Elements allTrO = tableOtros.select("tr");

					// Fisic attacs
					for (Element trF : allTrF) {
						Optional<Ataque> atq;
						int thSize = trF.select("th").size();

						// Titles are only composed by th (so we skip the first line (titles))
						if (thSize != 0) {
							// We take only name class "bverde3"
							if (!trF.select("th").get(0).attr("class").equals("bverde3")) {
								// The attac name is situated at the first <td>
								String nombreAta = trF.select("td").get(0).text();
								// We search the attac on the attacs list by its name
								atq = this.ataques.stream().filter(at -> at.getNombreAta().equals(nombreAta))
										.findFirst();

								if (atq.isPresent()) {
									ataFisicos.add(atq.get().getIdAta());
								}
							}
						}
					}

					todosLosAtas.put("Ataques fisicos", ataFisicos);

					// Special attacs
					for (Element trE : allTrE) {
						Optional<Ataque> atq;
						String s = trE.attr("class");
						System.out.println(s);
						int thSize = trE.select("th").size();

						if (thSize != 0) {
							if (!trE.select("th").get(0).attr("class").equals("bverde3")) {
								String nombreAta = trE.select("td").get(0).text();
								atq = this.ataques.stream().filter(at -> at.getNombreAta().equals(nombreAta))
										.findFirst();

								if (atq.isPresent()) {
									ataEspeciales.add(atq.get().getIdAta());
								}
							}
						}
					}

					todosLosAtas.put("Ataques especiales", ataEspeciales);

					// Other attacs
					for (Element trO : allTrO) {
						Optional<Ataque> atq;
						String s = trO.attr("class");
						System.out.println(s);
						int thSize = trO.select("th").size();

						if (thSize != 0) {
							if (!trO.select("th").get(0).attr("class").equals("bverde3")) {
								String nombreAta = trO.select("td").get(0).text();
								atq = this.ataques.stream().filter(at -> at.getNombreAta().equals(nombreAta))
										.findFirst();

								if (atq.isPresent()) {
									ataOtros.add(atq.get().getIdAta());
								}
							}
						}
					}

					todosLosAtas.put("Ataques otros", ataOtros);
					// We put all the attacs for the current Pokemon
					this.ataquesPorPokemon.put(iP, todosLosAtas);
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
	public static void writePokemonCSV(ArrayList<Pokemon> pokemonsList) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_POKEMON));

			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("PokemonId", "PokemonName",
					"PS", "Ata", "Def", "Vel", "AtEsp", "DefEsp"));

			for (Pokemon p : pokemonsList) {
				csvPrinter.printRecord(p.getIdPokemon(), p.getNombrePokemon(), p.getPs(), p.getAta(), p.getDef(),
						p.getVel(), p.getAtEsp(), p.getDefEsp());
			}
			csvPrinter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Writes in a CSV all the habilities from hability list
	@SuppressWarnings("resource")
	public static void writeHabilitiesCSV(ArrayList<Habilidad> habilidadesList) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_HABS));

			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader("HabilityId", "HabilityName", "Effect"));

			for (Habilidad h : habilidadesList) {
				csvPrinter.printRecord(h.getIdHab(), h.getNombreHab(), h.getDescripcionHab());
			}
			csvPrinter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method extends the CSV of pokemonList.csv in a new file
	// (pokemonList2.csv)
	// Appends the habilities for each Pokemon in pokemonList2.csv
	@SuppressWarnings("resource")
	public void AppendHabilities() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_POKEMON_HABS));
			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader("PokemonId", "PokemonName", "PS", "Ata", "Def", "Vel", "AtEsp",
							"DefEsp", "Habilidad1", "Habilidad2", "Habilidad oculta1", "Habilidad oculta2"));

			FileReader fileReader = new FileReader(SAMPLE_CSV_ALL_POKEMON);
			BufferedReader br = new BufferedReader(fileReader);
			// Skips first line
			br.readLine();
			StringBuilder sb = new StringBuilder();
			String ligne;

			// We read each line of pokemonList.csv and put it to pokemonList2.csv
			while ((ligne = br.readLine()) != null) {
				System.out.println("rentre 1");
				String[] pks = ligne.split(",");
				for (Map.Entry<String, HashMap<String, ArrayList<Habilidad>>> entry : this.habilidadPorPokemon
						.entrySet()) {
					if (pks[0].equals(entry.getKey())) {
						// If there is only one dico (one hability)
						if (entry.getValue().size() == 1) {
							sb.append("," + entry.getValue().get("Habilidad").get(0).getIdHab() + "," + "," + ",");
						} else {
							// If there are 2 values in dico for normal habilities
							if (entry.getValue().get("Habilidad").size() == 2) {
								for (Habilidad h : entry.getValue().get("Habilidad")) {
									sb.append("," + h.getIdHab());
								}
							}
							// If there is 1 value in dico for normal habilities
							else {
								sb.append("," + entry.getValue().get("Habilidad").get(0).getIdHab() + ",");
							}

							// If there are 2 values in dico for oculted habilities
							if (entry.getValue().get("Habilidad oculta").size() == 2) {
								for (Habilidad h : entry.getValue().get("Habilidad oculta")) {
									sb.append("," + h.getIdHab());
								}
							}
							// If there is 1 value in dico for oculted habilities
							else {
								sb.append("," + entry.getValue().get("Habilidad oculta").get(0).getIdHab() + ",");
							}
						}
						// Prints the line in the nex CSV
						csvPrinter.printRecord(ligne + sb);
						// Restart the string for habilities
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
					CSVFormat.DEFAULT.withHeader("PokemonId", "PokemonName", "PS", "Ata", "Def", "Vel", "AtEsp",
							"DefEsp", "Habilidad1", "Habilidad2", "Habilidad oculta1", "Habilidad oculta2", "Tipo1",
							"Tipo2"));

			FileReader fileReader = new FileReader(SAMPLE_CSV_ALL_POKEMON_HABS);
			BufferedReader br = new BufferedReader(fileReader);
			// Skips first line
			br.readLine();
			StringBuilder sb = new StringBuilder();
			String ligne;

			// We read each line of pokemonList.csv and put it to pokemonList2.csv
			while ((ligne = br.readLine()) != null) {
				String[] pks = ligne.split(",");
				for (Map.Entry<String, ArrayList<PokemonType>> entry : tipoPkPorPokemon.entrySet()) {
					if (pks[0].equals(entry.getKey())) {
						// If there is only one dico (one hability)
						if (entry.getValue().size() == 1) {
							sb.append("," + entry.getValue().get(0).getIdPkTipo() + ",");
						} else {
							// If there are 2 values in dico for normal habilities
							sb.append("," + entry.getValue().get(0).getIdPkTipo() + ",");
							sb.append(entry.getValue().get(1).getIdPkTipo());

						}
						// Prints the line in the nex CSV
						csvPrinter.printRecord(ligne + sb);
						// Restart the string for habilities
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
	public void writeAttacsCSV() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_ATTACS));

			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("AttacId", "AttacName", "Tipo",
					"Poder", "PP", "Precision", "Efecto", "Base"));

			for (Ataque a : this.ataques) {
				csvPrinter.printRecord(a.getIdAta(), a.getNombreAta(), a.getTipo(), a.getPoder(), a.getPp(),
						a.getPrecision(), a.getEfecto(),
						a.getBases().size() > 1 ? a.getBases().get(0) + ";" + a.getBases().get(1)
								: a.getBases().get(0));
			}
			csvPrinter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Writes in a CSV all the attacks from attacks list
	@SuppressWarnings("resource")
	public void writeAttacsCSV2() {
		try {
			FileWriter fileWriter = new FileWriter(SAMPLE_CSV_ALL_ATTACS, true);
			BufferedWriter writer = new BufferedWriter(fileWriter);

			writer.append("AttacId,AttacName,Tipo,Poder,PP,Precision,Efecto,Base");
			writer.newLine();

			for (Ataque a : this.ataques) {
				writer.append(String.valueOf(a.getIdAta()) + "," + a.getNombreAta() + "," + a.getTipo() + ","
						+ String.valueOf(a.getPoder()) + "," + String.valueOf(a.getPp()) + ","
						+ String.valueOf(a.getPrecision()) + "," + a.getEfecto() + ",");

				if (a.getBases().size() > 1) {
					writer.append(String.valueOf(a.getBases().get(0) + ";" + a.getBases().get(1)));
				} else {
					writer.append(String.valueOf(a.getBases().get(0)));
				}
				writer.newLine();
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Writes in a CSV all the attacks foreach Pokemon
	@SuppressWarnings("resource")
	public void writeAttacsForeachPokemon() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_ATTACS_FOREACH_POKEMON));

			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader("PokemonId", "AtaFisicos", "AtaEspeciales", "AtaOtros"));

			for (Map.Entry<Integer, HashMap<String, ArrayList<Integer>>> a : this.ataquesPorPokemon.entrySet()) {

				String csvP = "";
				csvP += a.getKey() + ",";
				for (Integer atId : a.getValue().get("Ataques fisicos")) {
					csvP += atId + ";";
				}
				// Remove the last ";" and put instead a ","
				csvP = StringUtils.chop(csvP);
				csvP += ",";
				for (Integer atId : a.getValue().get("Ataques especiales")) {
					csvP += atId + ";";
				}
				csvP = StringUtils.chop(csvP);
				csvP += ",";
				for (Integer atId : a.getValue().get("Ataques otros")) {
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
			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				String[] pks = ligne.split(",");
				if (this.pokemons.size() == 809) {
					break;
				} else {
					Pokemon pika = new Pokemon(Integer.parseInt(pks[0]), pks[1], Integer.parseInt(pks[2]),
							Integer.parseInt(pks[3]), Integer.parseInt(pks[4]), Integer.parseInt(pks[5]),
							Integer.parseInt(pks[6]), Integer.parseInt(pks[7]));

					// Gets first hability (all Pokemon have at least one hability)
					if (!pks[12].isEmpty()) {
						for (PokemonType pt : this.types) {
							if (pt.getIdPkTipo() == Integer.parseInt(pks[12])) {
								pika.addType(pt);
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
							for (PokemonType pt : this.types) {
								if (pt.getIdPkTipo() == Integer.parseInt(pks[13])) {
									pika.addType(pt);
								}
							}
						}
					}

					this.pokemons.add(pika);
				}
			}
		} catch (IOException e) {
			System.out.println("Exception � la lecture du fichier : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue � la fermeture du fichier : " + e.getMessage());
			}
		}
	}

	// Reads habsList.csv file and adds to habilities list
	public void readHabilities(String fileName) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			// Skips first line
			bufferedReader.readLine();
			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				String[] pks = ligne.split(",");
				if (this.habilidades.size() == 309) {
					break;
				} else {
					this.habilidades.add(new Habilidad(Integer.parseInt(pks[0]), pks[1].toUpperCase(), pks[2]));
				}
			}
		} catch (IOException e) {
			System.out.println("Exception � la lecture du fichier : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue � la fermeture du fichier : " + e.getMessage());
			}
		}
	}

	// Reads pokemon.csv file (for habilities) and adds to Pokemon
	public void addHabsForEachPokemon(String fileName) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			// Skips first line
			bufferedReader.readLine();
			String ligne;

			while ((ligne = bufferedReader.readLine()) != null) {
				Optional<Pokemon> pkOpt;
				String[] habs = ligne.split(",");

				// Gets the curent pokemon of the line form the pokemon list
				pkOpt = this.pokemons.stream().filter(pk -> pk.getIdPokemon() == Integer.parseInt(habs[0])).findFirst();
				if (pkOpt.isPresent()) {
					// Gets first hability (all Pokemon have already one hability)
					if (!habs[8].isEmpty()) {
						for (Habilidad h : this.habilidades) {
							if (h.getIdHab() == Integer.parseInt(habs[8])) {
								pkOpt.get().addNormalHab(h);
							}
						}
					}
					// Gets other hablities (if a Pokemon has more)
					if (habs.length > 9) {
						// Gets second hability
						if (!habs[9].isEmpty()) {
							for (Habilidad h : this.habilidades) {
								if (h.getIdHab() == Integer.parseInt(habs[9])) {
									pkOpt.get().addNormalHab(h);
								}
							}
						}
						// Gets oculted hability
						if (!habs[10].isEmpty()) {
							for (Habilidad h : this.habilidades) {
								if (h.getIdHab() == Integer.parseInt(habs[10])) {
									pkOpt.get().addOcultedHab(h);
								}
							}
						}
						// Gets second oculted hability : only one Pokemon at the moment
						if (habs.length == 12) {
							if (!habs[11].isEmpty()) {
								for (Habilidad h : this.habilidades) {
									if (h.getIdHab() == Integer.parseInt(habs[11])) {
										pkOpt.get().addOcultedHab(h);
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception � la lecture du fichier : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue � la fermeture du fichier : " + e.getMessage());
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
			String ligne;

			while ((ligne = bufferedReader.readLine()) != null) {
				Optional<Pokemon> pkOpt;
				String[] tps = ligne.split(",");

				// It detects a " " " at the beginning of the Pokemon Id, so we remove it : we
				// have "001 instead of 001
				tps[0] = tps[0].substring(1);

				// Gets the curent pokemon of the line from the Pokemon list
				pkOpt = this.pokemons.stream().filter(pk -> pk.getIdPokemon() == Integer.parseInt(tps[0])).findFirst();
				if (pkOpt.isPresent()) {
					// Gets first hability (all Pokemon have at least one hability)
					if (!tps[12].isEmpty()) {
						for (PokemonType pt : this.types) {
							if (pt.getIdPkTipo() == Integer.parseInt(tps[12])) {
								pkOpt.get().addType(pt);
							}
						}
					}
					// Gets the other type (if a Pokemon has 2 types)
					if (tps.length == 14) {
						// It detects a " " " at the end of the second type, so we remove it : we have
						// 17" instead of 17
						tps[13] = tps[13].substring(0, tps[13].length() - 1);
						// Gets second type
						if (!tps[13].isEmpty()) {
							for (PokemonType pt : this.types) {
								if (pt.getIdPkTipo() == Integer.parseInt(tps[13])) {
									pkOpt.get().addType(pt);
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception � la lecture du fichier : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue � la fermeture du fichier : " + e.getMessage());
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
			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				String[] pks = ligne.split(",");
				if (this.types.size() == 18) {
					break;
				} else {
					this.types.add(new PokemonType(Integer.parseInt(pks[0]), pks[1].toUpperCase()));
				}
			}
		} catch (IOException e) {
			System.out.println("Exception � la lecture du fichier : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue � la fermeture du fichier : " + e.getMessage());
			}
		}
	}

	// Reads typesList.csv file and adds the effects against other types
	public void readPkTypesEffectsToOtherTypes() {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(SAMPLE_CSV_ALL_TYPES);
			bufferedReader = new BufferedReader(fileReader);
			// Skips first line
			bufferedReader.readLine();
			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				HashMap<String, ArrayList<PokemonType>> tps = new HashMap<>();
				ArrayList<PokemonType> tpsEfc = new ArrayList<>();

				String[] ty = ligne.split(",");
				// Each string[] can have several values: we split them by ";"
				String[] pksEfcDmg = ty[2].split(";");
				String[] pksEfcVulbl = ty[3].split(";");
				String[] pksEfcLttDmg = ty[4].split(";");
				String[] pksEfcLttVulbl = ty[5].split(";");
				String[] pksNoEfc = ty[6].split(";");

				Optional<PokemonType> pTypeOpt;
				PokemonType pT;

				// Do a lot of dammage
				if (pksEfcDmg.length >= 1 && Integer.parseInt(pksEfcDmg[0]) != 0) {
					for (String pkE : pksEfcDmg) {
						pTypeOpt = this.types.stream().filter(type -> type.getIdPkTipo() == Integer.parseInt(pkE))
								.findFirst();
						if (pTypeOpt.isPresent()) {
							pT = pTypeOpt.get();
							tpsEfc.add(pT);
						}
					}
					tps.put("Rebienta", tpsEfc);
					tpsEfc = new ArrayList<>();
				}

				// Hurts from other types
				if (pksEfcVulbl.length >= 1 && Integer.parseInt(pksEfcVulbl[0]) != 0) {
					for (String pkE : pksEfcVulbl) {
						pTypeOpt = this.types.stream().filter(type -> type.getIdPkTipo() == Integer.parseInt(pkE))
								.findFirst();
						if (pTypeOpt.isPresent()) {
							pT = pTypeOpt.get();
							tpsEfc.add(pT);
						}
					}
					tps.put("Le rebientan", tpsEfc);
					tpsEfc = new ArrayList<>();
				}

				// Do little dammage
				if (pksEfcLttDmg.length >= 1 && Integer.parseInt(pksEfcLttDmg[0]) != 0) {
					for (String pkE : pksEfcLttDmg) {
						pTypeOpt = this.types.stream().filter(type -> type.getIdPkTipo() == Integer.parseInt(pkE))
								.findFirst();
						if (pTypeOpt.isPresent()) {
							pT = pTypeOpt.get();
							tpsEfc.add(pT);
						}
					}
					tps.put("Rebienta poco", tpsEfc);
					tpsEfc = new ArrayList<>();
				}

				// It hurts less than other types
				if (pksEfcLttVulbl.length >= 1 && Integer.parseInt(pksEfcLttVulbl[0]) != 0) {
					for (String pkE : pksEfcLttVulbl) {
						pTypeOpt = this.types.stream().filter(type -> type.getIdPkTipo() == Integer.parseInt(pkE))
								.findFirst();
						if (pTypeOpt.isPresent()) {
							pT = pTypeOpt.get();
							tpsEfc.add(pT);
						}
					}
					tps.put("Le Rebientan poco", tpsEfc);
					tpsEfc = new ArrayList<>();
				}

				// Doesn't has effect to him
				if (pksNoEfc.length >= 1 && Integer.parseInt(pksNoEfc[0]) != 0) {
					for (String pkE : pksNoEfc) {
						pTypeOpt = this.types.stream().filter(type -> type.getIdPkTipo() == Integer.parseInt(pkE))
								.findFirst();
						if (pTypeOpt.isPresent()) {
							pT = pTypeOpt.get();
							tpsEfc.add(pT);
						}
					}
					tps.put("No tiene efecto", tpsEfc);
				}

				// We put the dico in our principal var
				if (!this.efectoPorTipos.containsKey(ty[1])) {
					this.efectoPorTipos.put(ty[1], tps);
				}

			}
		} catch (IOException e) {
			System.out.println("Exception � la lecture du fichier : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue � la fermeture du fichier : " + e.getMessage());
			}
		}
	}

	// Reads attacsList.csv file and adds to attacs list
	public void readAttacs(String fileName) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			// Skips first line
			bufferedReader.readLine();
			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				String[] pks = ligne.split(",");

				Ataque a = new Ataque(Integer.parseInt(pks[0]), pks[1], pks[2].toUpperCase(), Integer.parseInt(pks[3]),
						Integer.parseInt(pks[4]), Integer.parseInt(pks[5]), pks[6]);

				// Some attacs can have 2 bases (so we split with ";")
				String[] bs = pks[7].split(";");

				if (bs.length > 1) {
					for (String s : bs) {
						a.addBase(s);
					}
				} else {
					a.addBase(bs[0]);
				}
				// Set the type of the attack to his Pokemon type instead of a string
				a.transformStrTipoToPokemonType(this.types);
				// Adds the attac to the general var
				this.ataques.add(a);
			}
		} catch (IOException e) {
			System.out.println("Exception � la lecture du fichier : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue � la fermeture du fichier : " + e.getMessage());
			}
		}
	}

	// Reads attacsForeachPokemon.txt file and adds to each Pokemon
	public void readAttacsForeachPokemon() {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(SAMPLE_CSV_ALL_ATTACS_FOREACH_POKEMON);
			bufferedReader = new BufferedReader(fileReader);
			// Skips first line
			bufferedReader.readLine();
			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				Optional<Pokemon> pkOpt;
				Optional<Ataque> pAtaOpt;

				String[] ata = ligne.split(",");
				// Each string[] can have several values: we split them by ";"
				String[] pkAtaFis = ata[1].split(";");
				String[] pkAtaEsp = ata[2].split(";");
				String[] pkAtaOto = ata[3].split(";");

				// Gets the curent pokemon of the line from the Pokemon list
				pkOpt = this.pokemons.stream().filter(pk -> pk.getIdPokemon() == Integer.parseInt(ata[0])).findFirst();

				if (pkOpt.isPresent()) {
					// Fisic attacs
					// Somme Pokemon can have 0 attacs in a type of attac
					if (pkAtaFis.length >= 1 && Integer.parseInt(pkAtaFis[0]) != 0) {
						for (String pkA : pkAtaFis) {
							pAtaOpt = this.ataques.stream().filter(a -> a.getIdAta() == Integer.parseInt(pkA))
									.findFirst();
							if (pAtaOpt.isPresent()) {
								pkOpt.get().addAtaFisicos(pAtaOpt.get());
							}
						}
					}

					// Special attacs
					if (pkAtaEsp.length >= 1 && Integer.parseInt(pkAtaEsp[0]) != 0) {
						for (String pkA : pkAtaEsp) {
							pAtaOpt = this.ataques.stream().filter(a -> a.getIdAta() == Integer.parseInt(pkA))
									.findFirst();
							if (pAtaOpt.isPresent()) {
								pkOpt.get().addAtaEspeciales(pAtaOpt.get());
							}
						}
					}

					// Other attacs
					if (pkAtaOto.length >= 1 && Integer.parseInt(pkAtaOto[0]) != 0) {
						for (String pkA : pkAtaOto) {
							pAtaOpt = this.ataques.stream().filter(a -> a.getIdAta() == Integer.parseInt(pkA))
									.findFirst();
							if (pAtaOpt.isPresent()) {
								pkOpt.get().addAtaEstado(pAtaOpt.get());
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception � la lecture du fichier : " + e.getMessage());
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue � la fermeture du fichier : " + e.getMessage());
			}
		}
	}

	// ==================================== GAME METHODS
	// ====================================

	// Prints all the Pokemon
	public void printPokemon() {
		for (Pokemon pk : this.pokemons) {
			System.out.println(pk.getIdPokemon() + " - " + pk.getNombrePokemon() + " - " + pk.getTypes().size() + " :");
			pk.getTypes().forEach(tp -> {
				System.out.println(tp.getNombreTipo());
			});
		}
	}

	// Order Pokemon by type
	public void classPkPerType() {
		ArrayList<Pokemon> aceroType = new ArrayList<>();
		ArrayList<Pokemon> aguaType = new ArrayList<>();
		ArrayList<Pokemon> fuegoType = new ArrayList<>();
		ArrayList<Pokemon> bichoType = new ArrayList<>();
		ArrayList<Pokemon> dragonType = new ArrayList<>();
		ArrayList<Pokemon> electricoType = new ArrayList<>();
		ArrayList<Pokemon> fantasmaType = new ArrayList<>();
		ArrayList<Pokemon> hadaType = new ArrayList<>();
		ArrayList<Pokemon> hieloType = new ArrayList<>();
		ArrayList<Pokemon> luchaType = new ArrayList<>();
		ArrayList<Pokemon> normalType = new ArrayList<>();
		ArrayList<Pokemon> plantaType = new ArrayList<>();
		ArrayList<Pokemon> psiquicoType = new ArrayList<>();
		ArrayList<Pokemon> rocaType = new ArrayList<>();
		ArrayList<Pokemon> siniestroType = new ArrayList<>();
		ArrayList<Pokemon> tierraType = new ArrayList<>();
		ArrayList<Pokemon> venenoType = new ArrayList<>();
		ArrayList<Pokemon> voladorType = new ArrayList<>();

		Optional<PokemonType> pkTOpt;

		for (Pokemon pk : this.pokemons) {
			// If 2 types, puts the Pokemon in the two diferent types
			for (PokemonType pt : pk.getTypes()) {
				pkTOpt = this.types.stream().filter(t -> t.getIdPkTipo() == pt.getIdPkTipo()).findFirst();
				if (pkTOpt.isPresent()) {
					switch (pkTOpt.get().getIdPkTipo()) {
					case 1:
						aceroType.add(pk);
						break;
					case 2:
						aguaType.add(pk);
						break;
					case 3:
						bichoType.add(pk);
						break;
					case 4:
						dragonType.add(pk);
						break;
					case 5:
						electricoType.add(pk);
						break;
					case 6:
						fantasmaType.add(pk);
						break;
					case 7:
						fuegoType.add(pk);
						break;
					case 8:
						hadaType.add(pk);
						break;
					case 9:
						hieloType.add(pk);
						break;
					case 10:
						luchaType.add(pk);
						break;
					case 11:
						normalType.add(pk);
						break;
					case 12:
						plantaType.add(pk);
						break;
					case 13:
						psiquicoType.add(pk);
						break;
					case 14:
						rocaType.add(pk);
						break;
					case 15:
						siniestroType.add(pk);
						break;
					case 16:
						tierraType.add(pk);
						break;
					case 17:
						venenoType.add(pk);
						break;
					case 18:
						voladorType.add(pk);
						break;
					}
				}
			}
		}

		this.pokemonsPorTipo.put("ACERO", aceroType);
		this.pokemonsPorTipo.put("AGUA", aguaType);
		this.pokemonsPorTipo.put("BICHO", bichoType);
		this.pokemonsPorTipo.put("DRAGON", dragonType);
		this.pokemonsPorTipo.put("ELECTRICO", electricoType);
		this.pokemonsPorTipo.put("FANTASMA", fantasmaType);
		this.pokemonsPorTipo.put("FUEGO", fuegoType);
		this.pokemonsPorTipo.put("HADA", hadaType);
		this.pokemonsPorTipo.put("HIELO", hieloType);
		this.pokemonsPorTipo.put("LUCHA", luchaType);
		this.pokemonsPorTipo.put("NORMAL", normalType);
		this.pokemonsPorTipo.put("PLANTA", plantaType);
		this.pokemonsPorTipo.put("PSIQUICO", psiquicoType);
		this.pokemonsPorTipo.put("ROCA", rocaType);
		this.pokemonsPorTipo.put("SINIESTRO", siniestroType);
		this.pokemonsPorTipo.put("TIERRA", tierraType);
		this.pokemonsPorTipo.put("VENENO", venenoType);
		this.pokemonsPorTipo.put("VOLADOR", voladorType);
	}

	// Regex to match Pokemon player choices
	public String checkRegexToChoicePokemon() {
		// Player can choice with format : d,d,d,d,d,d, but numbers are between 1 and
		// 807
		String strRegex = "\\b([1-9]";

		for (int i = 1; i <= 9; i++) {
			// The "|" represents OR
			strRegex += "|" + i + "[0-9]";
		}

		for (int i = 10; i <= 79; i++) {
			strRegex += "|" + i + "[0-9]";
		}
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

	// ==================================== GAME
	// ====================================

	// Intialize all vars from files
	public void InitiateVars() {
		// Instantiate all Pokemon (if CSV not already created)
		// scrappingWebPokemon();

		// Write all Pokemon to a CSV file
		// writePokemonCSV(this.pokemons);

		// Instantiate all the habilities (if CSV not already created)
//		scrappingWebAllHabs() ;

		// Write all habilities to a CSV file
//		writeHabilitiesCSV(this.habilidades);

		// Initialise the diferent lists
		readPkTypes(SAMPLE_CSV_ALL_TYPES);
//		readPokemon(SAMPLE_CSV_ALL_POKEMON);
		readPokemon(SAMPLE_CSV_ALL_POKEMON_TYPES);
		readHabilities(SAMPLE_CSV_ALL_HABS);
		readAttacs(SAMPLE_CSV_ALL_ATTACS);

		// Adds the habilities for diferent Pokemon (on the general list)
//		scrappingWebReadHabsFromPokemonAllTables();

		// Append habilities to pokemonList.csv (pokemonList2.csv)
//		AppendHabilities();

		// Adds habilities to pokemon (on the pokemon)
		addHabsForEachPokemon(SAMPLE_CSV_ALL_POKEMON_HABS);

		// Puts the lists of diferent damages for each type
		readPkTypesEffectsToOtherTypes();

		// Adds the type for each Pokemon
//		scrappingWebReadTypeForeachPokemon();

		// Append Pokemon type to pokemonList.csv (pokemonList3.csv)
//		AppendPokemonTypes();

		// Adds types to Pokemon (on the pokemon)
		//
		//
//		addTypesForEachPokemon(SAMPLE_CSV_ALL_POKEMON_TYPES);

		// All the attacs
//		scrappingWebAttacs();

		// Writes the attacs
//		writeAttacsCSV();
		// Takes accents
//		writeAttacsCSV2();

		// All attacs foreach Pokemon
//		scrappingWebAttacsForEachPokemon();

		// Writes in a new CSV all the attacs foreach Pokemon
//		writeAttacsForeachPokemon();

		// Adds the attacs foreach Pokemon
		readAttacsForeachPokemon();

		// Order Pokemon by type
		classPkPerType();
	}

	@SuppressWarnings("resource")
	public void PokemonChoice() {
		printPokemon();
		System.out.println();
		// Instructions/Description of the game
		System.out.println(
				"Escoge 6 Pokemon para el combate (todos est�n al nivel 100, con sus estad�sticas a nivel m�ximo),\n"
						+ "Puedes escoger el mismo Pokemon 6 veces seguidas. La m�quina no podr�.\n"
						+ "Los ataques de cada Pokemon ser�n iniciados aleatoriamente una vez escojas los Pokemon. Lo que quiere decir que cada Pokemon id�ntico, puede tener ataques diferentes.\n"
						+ "Solo puedes escoger Pokemon entre el 1 y el 807. Algunos n�meros no estan disponibles as� que aseg�rate de mirar bien la lista presentada.\n"
						+ "No importa el orden en que los escojas, solo determina el primer Pokemon que va a salir (el primero en escoger)\n"
						+ "Ten en cuenta que por cada Pokemon que t� escojas, la m�quina va a buscar el tipo que m�s le afecte a tu Pokemon\n"
						+ "Teniendo en cuenta, que quiz�s a la m�quina no le salgan ataques muy favorecidos, ni a ti tampoco jeje\n"
						+ "Para escoger los Pokemon, utiliza el formato : n�mero,n�mero,n�mero,n�mero,n�mero,n�mero\n");

		System.out.println("Escoge tus 6 Pokemon :");

		// Player choice
		Scanner sc = new Scanner(System.in);
		String allPkPlayer = sc.next();
		sc.useDelimiter(";|\r?\n|\r");

		// Regex that match the Pokemon choices
		String matchFormatChoice = checkRegexToChoicePokemon();
		if (!allPkPlayer.matches(matchFormatChoice)) {
			while (!allPkPlayer.matches(matchFormatChoice)) {
				System.out.println(
						"Para escoger los Pokemon, utiliza el formato : n�mero,n�mero,n�mero,n�mero,n�mero,n�mero y los n�meros deben estar entre el 1 y el 807");
				allPkPlayer = sc.next();
				sc.useDelimiter(";|\r?\n|\r");
				System.out.println(allPkPlayer.split(","));
			}
		}

		// Adds the Pokemon to player Pokemon list
		String[] pkByPkPlayer = allPkPlayer.split(",");
		for (String PkID : pkByPkPlayer) {
			Optional<Pokemon> pkOpt;
			pkOpt = this.pokemons.stream().filter(pk -> pk.getIdPokemon() == Integer.parseInt(PkID)).findFirst();
			if (pkOpt.isPresent()) {
				player.addPokemon(pkOpt.get());
			} else {
				System.out.println("El n�mero marcado no est� en la lista : " + PkID);
				System.out.println("Tendr�s que volver a escoger tus Pokemon (reinicia el juego)");
			}
		}

		// Machine choice Pokemon
		IA.IAChoicePokemon(player.getPokemons(), this.pokemonsPorTipo, this.efectoPorTipos);

		// Add attacks to eah Pokemon list
		player.addAtacsForEackPokemon();
		IA.addAtacsForEackPokemon();

		// Sets first Pokemon choose for the combat
		// Needs to be done during the methods ?
		player.setPkCombatting(player.getPokemons().get(0));
		IA.setPkCombatting(IA.getPokemons().get(0));

		player.setPkFacing(IA.getPkCombatting());
		IA.setPkFacing(player.getPkCombatting());

		System.out.println("Player");
		for (Pokemon p : player.getPokemons()) {
			System.out.println(p.getNombrePokemon() + ":");
			System.out.println();
			for (PokemonType pt : p.getTypes()) {
				System.out.println(pt.getNombreTipo());
			}
			for (Ataque a : p.getCuatroAtaques()) {
				System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
			}
			System.out.println();
		}
		System.out.println();

		System.out.println("IA");
		for (Pokemon p : IA.getPokemons()) {
			System.out.println(p.getNombrePokemon() + ":");
			System.out.println();
			for (PokemonType pt : p.getTypes()) {
				System.out.println(pt.getNombreTipo());
			}
			for (Ataque a : p.getCuatroAtaques()) {
				System.out.println(a.getNombreAta() + " - " + a.getStrTipoToPkType().getNombreTipo());
			}
			System.out.println();
		}

		System.out.println("Tipo ataques primer Pk IA");
		for (Ataque a : IA.getPkCombatting().getCuatroAtaques()) {
			System.out.println(a.getStrTipoToPkType().getNombreTipo());
		}

		IA.orderAttacksFromDammageLevelPokemon(this.pokemonsPorTipo, this.efectoPorTipos);
		player.orderAttacksFromDammageLevelPokemon(this.pokemonsPorTipo, this.efectoPorTipos);

		IA.prepareBestAttack(false, null);

		System.out.println("Next attack from machine :");
		System.out.println(IA.getPkCombatting().getNextMouvement().getNombreAta() + " - "
				+ IA.getPkCombatting().getNextMouvement().getStrTipoToPkType().getNombreTipo());
	}

	// Main battle (start battle)
	public void startBattle() {
		Scanner sc = new Scanner(System.in);
		while (IA.getPokemons().size() > 1 && player.getPokemons().size() > 1) {
			boolean playerCheckedBurned = false;
			boolean IACheckedBurned = false;

			int escogerOpt = player.getPkCombatting().isChargingAttackForNextRound ? 1 : getPlayerChoice(sc);

			if (escogerOpt == 1) {
				int ataqueId = 0;
				if (!player.getPkCombatting().isChargingAttackForNextRound) {
					ataqueId = getValidAttackId(sc, player);
				}

				printPokemonStates();

				playerCheckedBurned = checkAndApplyBurnedEffect(player, playerCheckedBurned);
				IACheckedBurned = checkAndApplyBurnedEffect(IA, IACheckedBurned);

				if ((player.getPkCombatting().isChargingAttackForNextRound()
						&& !player.getPkCombatting().isAlreadyUsedVuelo())
						|| (IA.getPkCombatting().isChargingAttackForNextRound()
								&& !IA.getPkCombatting().isAlreadyUsedVuelo())) {

					if (player.getPkCombatting().isChargingAttackForNextRound()
							&& !player.getPkCombatting().isAlreadyUsedVuelo()
							&& !(IA.getPkCombatting().isChargingAttackForNextRound()
									&& !IA.getPkCombatting().isAlreadyUsedVuelo())) {

						executeChargedAttack(player, true);
						System.out.println("first--------executeChargedAttack");

					} else if (IA.getPkCombatting().isChargingAttackForNextRound()
							&& !IA.getPkCombatting().isAlreadyUsedVuelo()
							&& !(player.getPkCombatting().isChargingAttackForNextRound()
									&& !player.getPkCombatting().isAlreadyUsedVuelo())) {

						executeChargedAttack(IA, true);
						executeNormalAttack(ataqueId, true, false);
						System.out.println("second--------executeChargedAttack");

					} else if ((player.getPkCombatting().isChargingAttackForNextRound()
							&& !player.getPkCombatting().isAlreadyUsedVuelo())
							&& (IA.getPkCombatting().isChargingAttackForNextRound()
									&& !IA.getPkCombatting().isAlreadyUsedVuelo())

							|| (player.getPkCombatting().isChargingAttackForNextRound()
									&& player.getPkCombatting().isAlreadyUsedVuelo())
									&& (IA.getPkCombatting().isChargingAttackForNextRound()
											&& IA.getPkCombatting().isAlreadyUsedVuelo())) {

						if (player.getPkCombatting().getNextMouvement().getIdAta() == 19
								&& IA.getPkCombatting().getNextMouvement().getIdAta() == 19) {
							System.out.println("same attack  = true");
						}
//						if() {
//							
//						}
//						else {
							executeNormalAttack(ataqueId, false, true);
//						}
						System.out.println("third--------executeChargedAttack");
					}
				} else if ((player.getPkCombatting().getNextMouvement().getIdAta() == 19
						&& player.getPkCombatting().isAlreadyUsedVuelo())
						|| (IA.getPkCombatting().getNextMouvement().getIdAta() == 19
								&& IA.getPkCombatting().isAlreadyUsedVuelo())) {

					executeFlightAttack();
					System.out.println("fourth--------executeFLightAttack");

				} else {
					if ((player.getPkCombatting().getNextMouvement().getIdAta() == 19
							|| IA.getPkCombatting().getNextMouvement().getIdAta() == 19) && (player.getPkCombatting().isFirstInUsingTheSameAttack() || IA.getPkCombatting().isFirstInUsingTheSameAttack())) {
						executeNormalAttack(ataqueId, false, true);
						System.out.println("fifth--------executeNormalAttack");
					}
					executeNormalAttack(ataqueId, false, false);
					System.out.println("six--------executeNormalAttack");

				}
			} else {
				changePokemon(sc);
			}
		}
	}

	private int getPlayerChoice(Scanner sc) {
		System.out.println("Quieres atacar (1) o cambiar de Pokemon (2) :");
		int choice = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");
		return choice;
	}

	private int getValidAttackId(Scanner sc, Player player) {
		System.out.println("Escoge un ataque :");
		player.printAttacksFromPokemonCombating();
		int ataqueId = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");

		while (!player.getPkCombatting().getCuatroAtaquesIds().contains(ataqueId)) {
			System.out.println("Escoge un ataque que tenga el Pokemon");
			ataqueId = sc.nextInt();
			sc.useDelimiter(";|\r?\n|\r");
		}
		return ataqueId;
	}

	private void printPokemonStates() {
		System.out.println(
				"Estado del Pokemon del jugador : " + player.getPkCombatting().getEstadoPersistente().getEstadoEnum());
		System.out.println(
				"Estado del Pokemon de la m�quina : " + IA.getPkCombatting().getEstadoPersistente().getEstadoEnum());
	}

	private boolean checkAndApplyBurnedEffect(Player combatant, boolean alreadyChecked) {
		if (combatant.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
			combatant.getPkCombatting().checkEffectsEstadoPersistente(true);
			alreadyChecked = true;
		}
		return alreadyChecked;
	}

	private void executeChargedAttack(Player player, boolean isPlayer) {
		player.getPkCombatting().checkEffectsEstadoPersistente(false);
		if (player.getPkCombatting().getEstadoPersistente().getEstadoEnum() != EstadosEnum.DEBILITADO) {
			if (player.getPkCombatting().isPuedeAtacar()) {
				player.applyDamage(isPlayer);
			}
		}
	}

	private void executeFlightAttack() {
		IA.getPkCombatting().checkEffectsEstadoPersistente(false);
		if (IA.getPkCombatting().isPuedeAtacar()) {
			IA.applyDamage(false);
			IA.getPkCombatting().restartParametersEffect();
//			IA.getPkCombatting().setFirstInUsingTheSameAttack(true);
			System.out.println("executeFlightAttack IA");
		}
		if (player.getPkCombatting().getEstadoPersistente().getEstadoEnum() != EstadosEnum.DEBILITADO) {
			if (player.getPkCombatting().isChargingAttackForNextRound()) {
				player.getPkCombatting().checkEffectsEstadoPersistente(false);
			}
			if (player.getPkCombatting().isPuedeAtacar()) {
				player.applyDamage(false);
//				player.getPkCombatting().setFirstInUsingTheSameAttack(true);
			}
			System.out.println("executeFlightAttack Player");
		}
	}

	private void executeNormalAttack(int ataqueId, boolean onlyPlayerAttacks, boolean sameAttack) {
		if (sameAttack) {
			player.prepareBestAttack(true, ataqueId);
			handleStateEffects();
			handleAttackSequence(false, true);
		} else {
			if (onlyPlayerAttacks) {
				player.prepareBestAttack(true, ataqueId);
				handleStateEffects();
				handleAttackSequence(true, false);
			} else {
				player.prepareBestAttack(true, ataqueId);
				handleStateEffects();
				handleAttackSequence(false, false);
			}
		}
	}

	private void handleStateEffects() {
		checkAndApplyEffect(player, EstadosEnum.PARALIZADO, false);
		checkAndApplyEffect(IA, EstadosEnum.PARALIZADO, false);
	}

	private void handleAttackSequence(boolean onlyPlayerAttacks, boolean sameAttack) {
		if (onlyPlayerAttacks) {
			handlePlayerRetaliation();
		} else {
			if (sameAttack) {
				handleSimultaneousAttack();
				if (playerCanAttackFirst()) {
					player.getPkCombatting().setFirstInUsingTheSameAttack(true);
				} else {
					IA.getPkCombatting().setFirstInUsingTheSameAttack(true);
				}
			} else {
				if (playerCanAttackFirst()) {
					player.applyDamage(false);
					handleIARetaliation();
//					if (player.getPkCombatting().getNextMouvement().getIdAta() == 19
//							&& IA.getPkCombatting().getNextMouvement().getIdAta() == 19) {
//						player.getPkCombatting().setFirstInUsingTheSameAttack(true);
//					}
				} else if (IACanAttackFirst()) {
					IA.applyDamage(false);
					handlePlayerRetaliation();
//					if (player.getPkCombatting().getNextMouvement().getIdAta() == 19
//							&& IA.getPkCombatting().getNextMouvement().getIdAta() == 19) {
//						IA.getPkCombatting().setFirstInUsingTheSameAttack(true);
//					}
				} else {
					handleSimultaneousAttack();
				}
			}
		}

		resetBurnedEffect();
	}

	private void checkAndApplyEffect(Player combatant, EstadosEnum state, boolean alreadyChecked) {
		if (combatant.getPkCombatting().getEstadoPersistente().getEstadoEnum() == state) {
			if (!alreadyChecked) {
				combatant.getPkCombatting().checkEffectsEstadoPersistente(false);
			}
			alreadyChecked = true;
		}
	}

	private boolean playerCanAttackFirst() {
		return player.getPkCombatting().isPuedeAtacar()
				&& player.getPkCombatting().getVel() > IA.getPkCombatting().getVel();
	}

	private boolean IACanAttackFirst() {
		return IA.getPkCombatting().isPuedeAtacar()
				&& IA.getPkCombatting().getVel() > player.getPkCombatting().getVel();
	}

	private void handleIARetaliation() {
		if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() != EstadosEnum.DEBILITADO) {
			checkAndApplyEffect(IA, EstadosEnum.PARALIZADO, false);
			checkAndApplyEffect(IA, EstadosEnum.QUEMADO, true);
			IA.applyDamage(false);
			IA.getPkCombatting().restartParametersEffect();
		}
	}

	private void handlePlayerRetaliation() {
		if (player.getPkCombatting().getEstadoPersistente().getEstadoEnum() != EstadosEnum.DEBILITADO) {
			checkAndApplyEffect(player, EstadosEnum.PARALIZADO, false);
			checkAndApplyEffect(player, EstadosEnum.QUEMADO, true);
			player.applyDamage(false);
			player.getPkCombatting().restartParametersEffect();
		}
	}

	// Check here conditions of flying...
	private void handleSimultaneousAttack() {
		if (IA.getPkCombatting().isFirstInUsingTheSameAttack()
				&& player.getPkCombatting().getNextMouvement().getIdAta() == 19
				&& IA.getPkCombatting().getNextMouvement().getIdAta() == 19) {
			IA.applyDamage(false);
			handlePlayerRetaliation();
			IA.getPkCombatting().setFirstInUsingTheSameAttack(false);
		} else if (player.getPkCombatting().isFirstInUsingTheSameAttack()
				&& player.getPkCombatting().getNextMouvement().getIdAta() == 19
				&& IA.getPkCombatting().getNextMouvement().getIdAta() == 19) {
			player.applyDamage(false);
			handleIARetaliation();
			player.getPkCombatting().setFirstInUsingTheSameAttack(false);
		} else {
			if (player.getPkCombatting().getVel() > IA.getPkCombatting().getVel()) {
				player.applyDamage(false);
				handleIARetaliation();
				if (player.getPkCombatting().getNextMouvement().getIdAta() == 19
						&& IA.getPkCombatting().getNextMouvement().getIdAta() == 19) {
					IA.getPkCombatting().setFirstInUsingTheSameAttack(true);
				} else {
					player.getPkCombatting().setFirstInUsingTheSameAttack(false);
					IA.getPkCombatting().setFirstInUsingTheSameAttack(false);
				}
			} else if (player.getPkCombatting().getVel() < IA.getPkCombatting().getVel()) {
				IA.applyDamage(false);
				handlePlayerRetaliation();
				if (player.getPkCombatting().getNextMouvement().getIdAta() == 19
						&& IA.getPkCombatting().getNextMouvement().getIdAta() == 19) {
					player.getPkCombatting().setFirstInUsingTheSameAttack(true);
				} else {
					player.getPkCombatting().setFirstInUsingTheSameAttack(false);
					IA.getPkCombatting().setFirstInUsingTheSameAttack(false);
				}
			} else {
				player.applyDamage(false);
				handleIARetaliation();
				if (player.getPkCombatting().getNextMouvement().getIdAta() == 19
						&& IA.getPkCombatting().getNextMouvement().getIdAta() == 19) {
//					IA.getPkCombatting().setFirstInUsingTheSameAttack(true);
				} else {
					player.getPkCombatting().setFirstInUsingTheSameAttack(false);
					IA.getPkCombatting().setFirstInUsingTheSameAttack(false);
				}
			}
		}

	}

	private void resetBurnedEffect() {
		if (player.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
			player.getPkCombatting().checkEffectsEstadoPersistente(false);
		}
		if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
			IA.getPkCombatting().checkEffectsEstadoPersistente(false);
		}
	}

	private void changePokemon(Scanner sc) {
		player.printPokemonInfo();
		System.out.println("Escoge uno de tus Pokemon : ");
		int pokemonId = sc.nextInt();
		sc.useDelimiter(";|\r?\n|\r");

		Optional<Pokemon> pkOpt = player.getPokemons().stream().filter(pk -> pk.getIdPokemon() == pokemonId)
				.findFirst();
		if (pkOpt.isPresent()) {
			player.addPokemon(pkOpt.get());
			player.setPkCombatting(pkOpt.get());
		} else {
			System.out.println(
					"No has escogido un Pokemon que te corresponde, seguir�s un turno m�s con el mismo Pokemon");
		}
	}

//	// Main battle (start battle)
//	@SuppressWarnings("resource")
//	public void startBattle() {
//
//		Scanner sc = new Scanner(System.in);
//		// Battle ends when a player doesn't have Pokemon
//		while (IA.getPokemons().size() > 1 && player.getPokemons().size() > 1) {
//
//			boolean playerAlreadyCheckedParalized = false;
//			boolean IAAlreadyCheckedParalized = false;
//			boolean playerAlreadyCheckedBurned = false;
//			boolean IAAlreadyCheckedBurned = false;
//			int escogerOpt = 0;
//
//			// Only can chose an attack if it's not charging another one (ex : vuelo)
//			if (player.getPkCombatting().isChargingAttackForNextRound) {
//				escogerOpt = 1;
//			} else {
//				// Player choice (change Pk or attack)
//				System.out.println("Quieres atacar (1) o cambiar de Pokemon (2) :");
//				escogerOpt = sc.nextInt();
//				sc.useDelimiter(";|\r?\n|\r");
//			}
//
//			// Attack
//			if (escogerOpt == 1) {
//				int ataqueId = 0;
//				if (!player.getPkCombatting().isChargingAttackForNextRound) {
//					System.out.println("Escoge un ataque :");
//					player.printAttacksFromPokemonCombating();
//					ataqueId = sc.nextInt();
//					sc.useDelimiter(";|\r?\n|\r");
//
//					// Chose an attack from the Pokemon
//					if (!player.getPkCombatting().getCuatroAtaquesIds().contains(ataqueId)) {
//						while (!player.getPkCombatting().getCuatroAtaquesIds().contains(ataqueId)) {
//							System.out.println("Escoge un ataque que tenga el Pokemon");
//							ataqueId = sc.nextInt();
//							sc.useDelimiter(";|\r?\n|\r");
//						}
//					}
//				}
//
//				System.out.println("Estado del Pokemon del jugador : "
//						+ player.getPkCombatting().getEstadoPersistente().getEstadoEnum());
//				System.out.println("Estado del Pokemon de la m�quina : "
//						+ IA.getPkCombatting().getEstadoPersistente().getEstadoEnum());
//
//				// If Pokemon are burned, there are effects before attacking
//				if (player.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//					player.getPkCombatting().checkEffectsEstadoPersistente(true);
//					playerAlreadyCheckedBurned = true;
//				}
//				if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//					IA.getPkCombatting().checkEffectsEstadoPersistente(true);
//					IAAlreadyCheckedBurned = true;
//				}
//
//				//////
//				//////
//				//////
//				//////
//				////// SEE HERE THE PROBLEMS (IF ANY) XD (TO DO >>> apply estados)
//				//////
//				//////
//				//////
//				//////
//				// If pokemon combatting is charging attack, then uses the attack charged
//				// (attacks firstly and Pokemon facing cannot attack (see conditions on
//				// methods PkVPk))
//				if (player.getPkCombatting().isChargingAttackForNextRound
//						&& !player.getPkCombatting().isAlreadyUsedVuelo()) {
//					player.getPkCombatting().checkEffectsEstadoPersistente(false);
//					if (player.getPkCombatting().getEstadoPersistente().getEstadoEnum() != EstadosEnum.DEBILITADO) {
//						if (player.getPkCombatting().isPuedeAtacar()) {
//							player.applyDamage(true);
//						}
//					}
//				}
//				// See if Pokemon combating is doing again vuelo (if true, cannot use the attack
//				// first)
//				else if (player.getPkCombatting().getNextMouvement().getIdAta() == 19
//						&& player.getPkCombatting().isAlreadyUsedVuelo()) {
//					IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//					if (IA.getPkCombatting().isPuedeAtacar()) {
//						IA.applyDamage(false);
//						// If IA it is paralyzed, restart his speed (cause it is not something
//						// accumulated)
//						if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.PARALIZADO) {
//							IA.getPkCombatting().restartParametersEffect();
//						}
//					}
//					if (player.getPkCombatting().getEstadoPersistente().getEstadoEnum() != EstadosEnum.DEBILITADO) {
//						System.out.println("entro aquiiiiiiiiiiiiii");
//						if (player.getPkCombatting().isChargingAttackForNextRound) {
//							System.out.println("no aplica paralizar (estoy preparando el ataque)");
//							player.getPkCombatting().checkEffectsEstadoPersistente(false);
//						}
//						if (player.getPkCombatting().isPuedeAtacar()) {
//							player.applyDamage(false);
//						}
//					}
//				}
//				// Is a normal attack
//				else {
//					// Attack needs to be final or static, that's why we take the initial var into a
//					// new one
//					int ataqueIdR = ataqueId;
//					// See the effectivity of the attack against rival
//					player.prepareBestAttack(true, ataqueIdR);
//
//					// Check for Estado (paralyzed or frozen)
//					if ((player.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.CONGELADO
//							|| player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() == EstadosEnum.PARALIZADO)
//							|| (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.CONGELADO
//									|| IA.getPkCombatting().getEstadoPersistente()
//											.getEstadoEnum() == EstadosEnum.PARALIZADO)) {
//
//						// See the Estado of Pokemon player
//						if (player.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.PARALIZADO) {
//							player.getPkCombatting().checkEffectsEstadoPersistente(false);
//							// No more checking for the entire round
//							playerAlreadyCheckedParalized = true;
//						}
//						// See the Estado of Pokemon IA
//						if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.PARALIZADO) {
//							IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//							IAAlreadyCheckedParalized = true;
//						}
//
//						// Attack if Pokemon player can move and has more speed than Pokemon IA
//						if (player.getPkCombatting().isPuedeAtacar()
//								&& (player.getPkCombatting().getVel() > IA.getPkCombatting().getVel())) {
//							player.applyDamage(false);
//							// If player it is paralyzed, restart his speed (cause it is not something
//							// accumulated)
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() == EstadosEnum.PARALIZADO) {
//								player.getPkCombatting().restartParametersEffect();
//							}
//							// Attack if Pokemon IA is not debilitated
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() != EstadosEnum.DEBILITADO) {
//
//								// See if Pokemon IA has an Estado during the attack
//								if (IA.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.PARALIZADO && !IAAlreadyCheckedParalized) {
//									IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//									System.out.println("IA paralizada al momento");
//								} else if (IA.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.QUEMADO && !IAAlreadyCheckedBurned) {
//									IA.getPkCombatting().checkEffectsEstadoPersistente(true);
//								}
//
//								// Attack if Pokemon can move
//								if (IA.getPkCombatting().isPuedeAtacar()) {
//									IA.applyDamage(false);
//									// If IA it is paralyzed, restart his speed (cause it is not something
//									// accumulated)
//									if (IA.getPkCombatting().getEstadoPersistente()
//											.getEstadoEnum() == EstadosEnum.PARALIZADO) {
//										IA.getPkCombatting().restartParametersEffect();
//									}
//								}
//							}
//							// Check for other Estados
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() == EstadosEnum.QUEMADO) {
//								player.getPkCombatting().checkEffectsEstadoPersistente(false);
//								player.getPkCombatting().restartParametersEffect();
//							}
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//								IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//
//							// Attack if Pokemon IA can move and has more speed than player IA
//						} else if (IA.getPkCombatting().isPuedeAtacar()
//								&& (IA.getPkCombatting().getVel() > player.getPkCombatting().getVel())) {
//							IA.applyDamage(false);
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.PARALIZADO) {
//								IA.getPkCombatting().restartParametersEffect();
//							}
//							// Attack if Pokemon player is not debilitated
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() != EstadosEnum.DEBILITADO) {
//								// See if Pokemon player has an estado during the attack
//								if (player.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.PARALIZADO && !playerAlreadyCheckedParalized) {
//									player.getPkCombatting().checkEffectsEstadoPersistente(false);
//									System.out.println("jugador paralizado al momento");
//								} else if (player.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.QUEMADO && !playerAlreadyCheckedBurned) {
//									player.getPkCombatting().checkEffectsEstadoPersistente(true);
//								}
//
//								if (player.getPkCombatting().isPuedeAtacar()) {
//									player.applyDamage(false);
//									if (player.getPkCombatting().getEstadoPersistente()
//											.getEstadoEnum() == EstadosEnum.PARALIZADO) {
//										player.getPkCombatting().restartParametersEffect();
//									}
//								}
//							}
//							// Check for other Estados
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() == EstadosEnum.QUEMADO) {
//								player.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//								IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//
//							// Attack if Pokemon player can move and Pokemon IA cannot
//						} else if (player.getPkCombatting().isPuedeAtacar() && !IA.getPkCombatting().isPuedeAtacar()) {
//							player.applyDamage(false);
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() == EstadosEnum.PARALIZADO) {
//								player.getPkCombatting().restartParametersEffect();
//							}
//							System.out.println(
//									"El " + IA.getPkCombatting().getNombrePokemon() + " enemigo, no puede moverse");
//
//							// Check for other Estados
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() == EstadosEnum.QUEMADO) {
//								player.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//								IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//
//							// Attack if IA player can move and Pokemon player cannot
//						} else if (!player.getPkCombatting().isPuedeAtacar() && IA.getPkCombatting().isPuedeAtacar()) {
//							IA.applyDamage(false);
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.PARALIZADO) {
//								IA.getPkCombatting().restartParametersEffect();
//							}
//							System.out.println(player.getPkCombatting().getNombrePokemon() + " no puede moverse");
//
//							// Check for other Estados
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() == EstadosEnum.QUEMADO) {
//								player.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//								IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//
//							// Both Pokemon cannot attack
//						} else if (!player.getPkCombatting().isPuedeAtacar() && !IA.getPkCombatting().isPuedeAtacar()) {
//							if (player.getPkCombatting().getVel() > IA.getPkCombatting().getVel()) {
//								System.out.println(player.getPkCombatting().getNombrePokemon() + " no puede moverse");
//								System.out.println(
//										"El " + IA.getPkCombatting().getNombrePokemon() + " enemigo, no puede moverse");
//							} else {
//								System.out.println(
//										"El " + IA.getPkCombatting().getNombrePokemon() + " enemigo, no puede moverse");
//								System.out.println(player.getPkCombatting().getNombrePokemon() + " no puede moverse");
//							}
//							// Check for other Estados
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() == EstadosEnum.QUEMADO) {
//								player.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//								IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//
//						}
//						// Both Pokemon attack just in case (without any conditions)
//						else {
//							if (player.getPkCombatting().getVel() > IA.getPkCombatting().getVel()) {
//								player.applyDamage(false);
//								if (IA.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() != EstadosEnum.DEBILITADO) {
//
//									if (IA.getPkCombatting().getEstadoPersistente()
//											.getEstadoEnum() == EstadosEnum.PARALIZADO && !IAAlreadyCheckedParalized) {
//										IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//										System.out.println("IA paralizada al momento");
//									} else if (IA.getPkCombatting().getEstadoPersistente()
//											.getEstadoEnum() == EstadosEnum.QUEMADO && !IAAlreadyCheckedBurned) {
//										IA.getPkCombatting().checkEffectsEstadoPersistente(true);
//									}
//
//									IA.applyDamage(false);
//									if (IA.getPkCombatting().getEstadoPersistente()
//											.getEstadoEnum() == EstadosEnum.PARALIZADO) {
//										IA.getPkCombatting().restartParametersEffect();
//									}
//								}
//							} else {
//								IA.applyDamage(false);
//								if (player.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() != EstadosEnum.DEBILITADO) {
//
//									if (player.getPkCombatting().getEstadoPersistente()
//											.getEstadoEnum() == EstadosEnum.PARALIZADO
//											&& !playerAlreadyCheckedParalized) {
//										player.getPkCombatting().checkEffectsEstadoPersistente(false);
//										System.out.println("jugador paralizado al momento");
//									} else if (player.getPkCombatting().getEstadoPersistente()
//											.getEstadoEnum() == EstadosEnum.QUEMADO && !playerAlreadyCheckedBurned) {
//										player.getPkCombatting().checkEffectsEstadoPersistente(true);
//									}
//
//									player.applyDamage(false);
//									if (player.getPkCombatting().getEstadoPersistente()
//											.getEstadoEnum() == EstadosEnum.PARALIZADO) {
//										player.getPkCombatting().restartParametersEffect();
//									}
//								}
//							}
//							// Check for other Estados
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() == EstadosEnum.QUEMADO) {
//								player.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//								IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//							}
//						}
//
//						// Without estado
//					} else {
//						if (player.getPkCombatting().getVel() > IA.getPkCombatting().getVel()) {
//							player.applyDamage(false);
//							if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() != EstadosEnum.DEBILITADO) {
//
//								if (IA.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.PARALIZADO && !IAAlreadyCheckedParalized) {
//									IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//									System.out.println("IA paralizada al momento");
//								} else if (IA.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.QUEMADO && !IAAlreadyCheckedBurned) {
//									IA.getPkCombatting().checkEffectsEstadoPersistente(true);
//								}
//
//								IA.applyDamage(false);
//								if (IA.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.PARALIZADO) {
//									IA.getPkCombatting().restartParametersEffect();
//								}
//							}
//						} else {
//							IA.applyDamage(false);
//							if (player.getPkCombatting().getEstadoPersistente()
//									.getEstadoEnum() != EstadosEnum.DEBILITADO) {
//
//								if (player.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.PARALIZADO && !playerAlreadyCheckedParalized) {
//									player.getPkCombatting().checkEffectsEstadoPersistente(false);
//									System.out.println("jugador paralizado al momento");
//								} else if (player.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.QUEMADO && !playerAlreadyCheckedBurned) {
//									player.getPkCombatting().checkEffectsEstadoPersistente(true);
//								}
//
//								player.applyDamage(false);
//								if (player.getPkCombatting().getEstadoPersistente()
//										.getEstadoEnum() == EstadosEnum.PARALIZADO) {
//									player.getPkCombatting().restartParametersEffect();
//								}
//							}
//						}
//						// Check for other Estados
//						if (player.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//							player.getPkCombatting().checkEffectsEstadoPersistente(false);
//						}
//						if (IA.getPkCombatting().getEstadoPersistente().getEstadoEnum() == EstadosEnum.QUEMADO) {
//							IA.getPkCombatting().checkEffectsEstadoPersistente(false);
//						}
//					}
//				}
//			}
//			// Change Pokemon
//			else {
//				player.printPokemonInfo();
//				System.out.println("Escoge uno de tus Pokemon : ");
//				int pokemonId = sc.nextInt();
//				sc.useDelimiter(";|\r?\n|\r");
//
//				Optional<Pokemon> pkOpt;
//				pkOpt = player.getPokemons().stream().filter(pk -> pk.getIdPokemon() == pokemonId).findFirst();
//				if (pkOpt.isPresent()) {
//					player.addPokemon(pkOpt.get());
//					player.setPkCombatting(pkOpt.get());
//				} else {
//					System.out.println(
//							"No has escogido un Pokemon que te corresponde, seguir�s un turno m�s con el mismo Pokemon");
//				}
//
//				// TO DO >>> needs to restart the values for damage with the new Pokemon
////				IA.applieDamage(false);
//			}
//		}
//	}

	// Tests for attacks (466 Electivire, 398 Staraptor)

	public void doTest() {
		// Sets the same Pk
		String allPkPlayer = "6,6,6";
		String allPkIA = "398,398,398";

		String[] pkByPkPlayer = allPkPlayer.split(",");
		for (String PkID : pkByPkPlayer) {
			Optional<Pokemon> pkOpt;
			pkOpt = this.pokemons.stream().filter(pk -> pk.getIdPokemon() == Integer.parseInt(PkID)).findFirst();
			if (pkOpt.isPresent()) {
				player.addPokemon(pkOpt.get());
//				IA.addPokemon(pkOpt.get());
			}
		}

		String[] pkByPkIA = allPkIA.split(",");
		for (String PkID : pkByPkIA) {
			Optional<Pokemon> pkOpt;
			pkOpt = this.pokemons.stream().filter(pk -> pk.getIdPokemon() == Integer.parseInt(PkID)).findFirst();
			if (pkOpt.isPresent()) {
				IA.addPokemon(pkOpt.get());
			}
		}

		player.setPkCombatting(player.getPokemons().get(0));

		// Sets the attacks to player to test
		for (Pokemon pk : player.getPokemons()) {
			pk.addAtaques(pk.getAtaFisicos().stream().filter(af -> af.getIdAta() == 7).findFirst().get());
			pk.addAtaques(pk.getAtaFisicos().stream().filter(af -> af.getIdAta() == 9).findFirst().get());
			pk.addAtaques(pk.getAtaFisicos().stream().filter(af -> af.getIdAta() == 19).findFirst().get());

			// Adds the Ids of attacks chosed in a list
			for (Ataque ataChosed : player.getPkCombatting().getCuatroAtaques()) {
				player.getPkCombatting().addAtaquesIds(ataChosed.getIdAta());
			}
		}

		for (Pokemon pk : IA.getPokemons()) {
//			pk.addAtaques(pk.getAtaFisicos().stream().filter(af -> af.getIdAta() == 7).findFirst().get());
//			pk.addAtaques(pk.getAtaFisicos().stream().filter(af -> af.getIdAta() == 9).findFirst().get());
			pk.addAtaques(pk.getAtaFisicos().stream().filter(af -> af.getIdAta() == 19).findFirst().get());

			// Adds the Ids of attacks chosed in a list
			for (Ataque ataChosed : pk.getCuatroAtaques()) {
				pk.addAtaquesIds(ataChosed.getIdAta());
			}
		}
		IA.setPkCombatting(IA.getPokemons().get(0));

		player.setPkFacing(IA.getPokemons().get(0));
		IA.setPkFacing(player.getPokemons().get(0));

		IA.orderAttacksFromDammageLevelPokemon(this.pokemonsPorTipo, this.efectoPorTipos);
		player.orderAttacksFromDammageLevelPokemon(this.pokemonsPorTipo, this.efectoPorTipos);

		IA.prepareBestAttack(false, null);
	}

}