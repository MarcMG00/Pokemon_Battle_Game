package pokemon.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.text.Normalizer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Game {
	private static final String SAMPLE_CSV_ALL_POKEMON = "./data/pokemonList.csv";
	private static final String SAMPLE_CSV_ALL_POKEMON_NEW = "./data/pokemonList2.csv";
	private static final String SAMPLE_CSV_ALL_HABS = "./data/habsList.csv";
	private ArrayList<Pokemon> pokemons;
	private ArrayList<Habilidad> habilidades;
	private HashMap<String, HashMap<String, ArrayList<Habilidad>>> habilidadPorPokemon;
	
	public Game() {
		this.pokemons = new ArrayList<Pokemon>();
		this.habilidades = new ArrayList<Habilidad>();
		this.habilidadPorPokemon = new HashMap<String, HashMap<String, ArrayList<Habilidad>>>();
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

	// Add to pokemon list all the pokemon from 1 to 809
	public void scrappingWebPokemon() {
		try {	
			// Total nb of Pokemon
			int nbPk = 809;
			
			// We put all the Pokemon to the list
			for(int id = 1; id <= nbPk; id++) {
				// Each Pokemon change by id in the url
				String url = "https://www.pokexperto.net/index2.php?seccion=nds/nationaldex/stats&pk=" + id;
				// Gets the url
				Document document = Jsoup.connect(url).get();
				// Gets the "pkmain" class we are interested for estadistics
				int classPkMain = 0;
				
				// Gets the "pktitle" class for the Pokemon name
				Element pokemonName = document.selectFirst(".pktitle");
				String name = pokemonName.select(".mini").text();
				
				// Counts number of "pkmain" class (there are more or less mega evolutions depending on the Pokemon, so there
				// are more or less "pkmain" class
				Elements nbClass = document.getElementsByClass("pkmain");
				int nbAmount = nbClass.size();
				
				switch(nbAmount) {
					case 149:
						//1-
						classPkMain = 22;
						break;
					case 155:
						classPkMain = 28;
						break;
					case 161:
						//1-
						classPkMain = 34;
						break;
					case 167:
						//1-
						classPkMain = 40;
						break;
					case 185:
						//1-
						classPkMain = 58;
						break;
					case 197:
						//1-
						classPkMain = 70;
						break;
				}
				
				Element pokemon = document.select(".pkmain").get(classPkMain);
				
				// Prints the content (the current pokemon treating)
				System.out.println("================Pkmain name================");
				System.out.println(name + "-" + id);
				
				// Gets only the first 6 estadistics elements on the table with "right" class
				List<Object> pokemonStats = pokemon.select(".right").stream().limit(6).collect(Collectors.toList());

				// Create Pokemon
				Pokemon pk = new Pokemon(id,
										 name,
										 Integer.parseInt(((Element) pokemonStats.get(0)).text()),
										 Integer.parseInt(((Element) pokemonStats.get(1)).text()),
										 Integer.parseInt(((Element) pokemonStats.get(2)).text()),
										 Integer.parseInt(((Element) pokemonStats.get(3)).text()),
										 Integer.parseInt(((Element) pokemonStats.get(4)).text()),
										 Integer.parseInt(((Element) pokemonStats.get(5)).text())
										 );
				this.pokemons.add(pk);
			}
		}
		catch(IOException e) {
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
			
			for(Element tr : elementObjTr) {
				// We not consider the first "tr" cause it represents the titles (# represents the column name for habilities Id)
				if(!tr.firstElementChild().text().equals("#")) {
					// We get the 2 first "td" (representing the name and the description effect) - we remove all diacritics
					Habilidad hab = new Habilidad(Integer.parseInt(tr.firstElementChild().text()),
												  Normalizer.normalize(tr.select("td>a").get(0).text(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""),
												  Normalizer.normalize(tr.select("td").get(2).text(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""));
					this.habilidades.add(hab);
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Read all the possible habilities for diferent pokemon and put it to dico
	public HashMap<String, HashMap<String, ArrayList<Habilidad>>> scrappingWebReadHabsFromPokemonAllTables() {
		try {
			String url = "https://www.wikidex.net/wiki/Lista_de_Pok%C3%A9mon_con_sus_habilidades";
			// Gets the url
			Document document = Jsoup.connect(url).get();
			// Select all the pokemon table we are interested (each table represents a generation, we have a total of 809 pokemon in 7 tables)
			Elements allTables = document.select(".tabpokemon");
			
			for(Element t : allTables) {
				for(int tab = 0; tab < 7; tab++) {
					
					// We get each table
					Element table = document.select(".tabpokemon").get(tab);
					// We get each <tr> from the table concerned
					Elements elementObjTr = table.select("tr");
					
					for(Element tPk : elementObjTr) {
						// We don't take the first <tr> line (represents the name of the columns)
						if(!tPk.firstElementChild().text().equals("Nº")) {
							// We don't want the mega evolutions (they have the same number as the last evolution of the Pokemon)
							if(!this.habilidadPorPokemon.containsKey(tPk.select("td").get(0).text())) {
								// Hash Map to put al the habilities for each Pokemon
								HashMap <String, ArrayList<Habilidad>> habs = new HashMap <String, ArrayList <Habilidad>>();
								// List of normal habilities for each pokemon
								ArrayList<Habilidad> pokemonHabs = new ArrayList<Habilidad>();
								// List of oculted habilities for each pokemon
								ArrayList<Habilidad> pokemonHabsOcultas = new ArrayList<Habilidad>();
								// First hability
								Optional<Habilidad> h;
								// Second hability
								Habilidad h2;
								// Oculted hability
								Optional<Habilidad> hO;
								
								System.out.println("================Pkmain id================");
								System.out.println(tPk.select("td").get(0).text());
								System.out.println(tPk.select("td").size() + " columns");
								
								// Depending on the number of columns, there are more or less habilities (each hability is in a column)
								int nbTd = tPk.select("td").size();
								
								switch(nbTd) {
									// There's only one hability, so we put it as normal hability
									case 6:
										// We remove all the digits from the hability and we search the hability in our list of habilities (we compare by name)
										h = this.habilidades.stream().filter(habil -> habil.getNombreHab().equals(Normalizer.normalize(tPk.select("td").get(5).text().replaceAll("[0-9]","").toUpperCase(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""))).findFirst();
										// Habilities are optional, so we do a condition of nullability
										if(h.isPresent()) {
											// We put the hability in the normal list of habilities
											h2 = new Habilidad(h.get().getIdHab(), h.get().getNombreHab(), h.get().getDescripcionHab());
											pokemonHabs.add(h2);
										}
										// We put the normal list of habilities in the dico as "Habilidad" (normal hability)
										habs.put("Habilidad", pokemonHabs);
										break;
									case 7:
										h = this.habilidades.stream().filter(habil -> habil.getNombreHab().equals(Normalizer.normalize(tPk.select("td").get(5).text().replaceAll("[0-9]","").toUpperCase(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""))).findFirst();
										if(h.isPresent()) { 
											h2 = new Habilidad(h.get().getIdHab(), h.get().getNombreHab(), h.get().getDescripcionHab());
											pokemonHabs.add(h2);
										}
										habs.put("Habilidad", pokemonHabs);
										
										hO = this.habilidades.stream().filter(habil -> habil.getNombreHab().equals(Normalizer.normalize(tPk.select("td").get(6).text().replaceAll("[0-9]","").toUpperCase(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""))).findFirst();
										if(hO.isPresent()) { 
											h2 = new Habilidad(hO.get().getIdHab(), hO.get().getNombreHab(), hO.get().getDescripcionHab());
											pokemonHabsOcultas.add(h2);
										}
										habs.put("Habilidad oculta", pokemonHabsOcultas);
										break;
									case 8:
										h = this.habilidades.stream().filter(habil -> habil.getNombreHab().equals(Normalizer.normalize(tPk.select("td").get(5).text().replaceAll("[0-9]","").toUpperCase(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""))).findFirst();
										if(h.isPresent()) { 
											h2 = new Habilidad(h.get().getIdHab(), h.get().getNombreHab(), h.get().getDescripcionHab());
											pokemonHabs.add(h2);
										}
										
										h = this.habilidades.stream().filter(habil -> habil.getNombreHab().equals(Normalizer.normalize(tPk.select("td").get(6).text().replaceAll("[0-9]","").toUpperCase(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""))).findFirst();
										if(h.isPresent()) { 
											h2 = new Habilidad(h.get().getIdHab(), h.get().getNombreHab(), h.get().getDescripcionHab());
											pokemonHabs.add(h2);
										}
										habs.put("Habilidad", pokemonHabs);
										
										// There's only (at the moment) one Pokemon that has 2 oculted habilities. We do a split (with "/", cause there are not 2 columns for each hability)
										// For the first hability, we remove the last white space (replaceAll("\\s","") cause there are no spaces in the word) before the ("/")
										// For the second hability, we remove the first white space (trim()) after the ("/")
										if(tPk.select("td").get(0).text().equals("744")) {
											hO = this.habilidades.stream().filter(habil -> habil.getNombreHab().equals(Normalizer.normalize(tPk.select("td").get(7).text().split("/")[0].replaceAll("\\s","").replaceAll("[0-9]","").toUpperCase(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""))).findFirst();
											if(hO.isPresent()) { 
												h2 = new Habilidad(hO.get().getIdHab(), hO.get().getNombreHab(), hO.get().getDescripcionHab());
												pokemonHabsOcultas.add(h2);
											}
											hO = this.habilidades.stream().filter(habil -> habil.getNombreHab().equals(Normalizer.normalize(tPk.select("td").get(7).text().split("/")[1].trim().replaceAll("[0-9]","").toUpperCase(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""))).findFirst();
											if(hO.isPresent()) { 
												h2 = new Habilidad(hO.get().getIdHab(), hO.get().getNombreHab(), hO.get().getDescripcionHab());
												pokemonHabsOcultas.add(h2);
											}
										}
										else {
											hO = this.habilidades.stream().filter(habil -> habil.getNombreHab().equals(Normalizer.normalize(tPk.select("td").get(7).text().replaceAll("[0-9]","").toUpperCase(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""))).findFirst();
											if(hO.isPresent()) { 
												h2 = new Habilidad(hO.get().getIdHab(), hO.get().getNombreHab(), hO.get().getDescripcionHab());
												pokemonHabsOcultas.add(h2);
											}
										}
										habs.put("Habilidad oculta", pokemonHabsOcultas);
										break;
								}
									
								// We put the dico in our principal var
								if(!this.habilidadPorPokemon.containsKey(tPk.select("td").get(0).text())) {
									this.habilidadPorPokemon.put(tPk.select("td").get(0).text(), habs);
								}
							}
						}
					}
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return this.habilidadPorPokemon;
	}
	
	// Write in a CSV all the pokemon from pokemon list
	public static void writePokemonCSV(ArrayList<Pokemon> pokemonsList) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_POKEMON));

	        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
	                .withHeader("PokemonId", "PokemonName", "PS", "Ata", "Def", "Vel", "AtEsp", "DefEsp"));
	        
	        for(Pokemon p : pokemonsList) {
	        	csvPrinter.printRecord(p.getIdPokemon(),
	        						   p.getNombrePokemon(),
	        						   p.getPs(),
	        						   p.getAta(),
	        						   p.getDef(),
	        						   p.getVel(),
	        						   p.getAtEsp(),
	        						   p.getDefEsp());
	        }
	        csvPrinter.flush();  
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Write in a CSV all the habilities from hability list
	public static void writeHabilitiesCSV(ArrayList<Habilidad> habilidadesList) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_HABS));

	        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
	                .withHeader("HabilityId", "HabilityName", "Effect"));
	        
	        for(Habilidad h : habilidadesList) {
	        	csvPrinter.printRecord(h.getIdHab(),
	        						   h.getNombreHab(),
	        						   h.getDescripcionHab());
	        }
	        csvPrinter.flush();  
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Reads pokemon.csv file and adds to Pokemon list
	public void readPokemon(String fileName) {
		FileReader fileReader = null;
	    BufferedReader bufferedReader = null;
	    
	    try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			bufferedReader.readLine();
			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				String[] pks = ligne.split(",");
				if(this.pokemons.size() == 809) {
					break;
				}
				else {
					this.pokemons.add(new Pokemon(Integer.parseInt(pks[0]), pks[1], Integer.parseInt(pks[2]), Integer.parseInt(pks[3]), Integer.parseInt(pks[4]), Integer.parseInt(pks[5]), Integer.parseInt(pks[6]), Integer.parseInt(pks[7])));
				}
			}
		}
	    catch (IOException e) {
			System.out.println("Exception ï¿½ la lecture du fichier : " + e.getMessage());
		}
	    finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue ï¿½ la fermeture du fichier : " + e.getMessage());
			}
		}
	}
	
	// Reads habsList.csv file and adds to Pokemon list
	public void readHabilities(String fileName) {
		FileReader fileReader = null;
	    BufferedReader bufferedReader = null;
	    
	    try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			bufferedReader.readLine();
			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				String[] pks = ligne.split(",");
				if(this.habilidades.size() == 309) {
					break;
				}
				else {
					this.habilidades.add(new Habilidad(Integer.parseInt(pks[0]), pks[1].toUpperCase(), pks[2]));
				}
			}
		}
	    catch (IOException e) {
			System.out.println("Exception ï¿½ la lecture du fichier : " + e.getMessage());
		}
	    finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				System.out.println("Exception survenue ï¿½ la fermeture du fichier : " + e.getMessage());
			}
		}
	}
	
	// This method extends the CSV of pokemonList.csv in a new file (pokemonList2.csv)
	// Append the habilities for each Pokemon in pokemonList2.csv
	public void AppendHabilities() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_ALL_POKEMON_NEW));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
	                .withHeader("PokemonId", "PokemonName", "PS", "Ata", "Def", "Vel", "AtEsp", "DefEsp","Habilidad1", "Habilidad2", "Habilidad oculta1", "Habilidad oculta2"));
			
			FileReader fileReader = new FileReader(SAMPLE_CSV_ALL_POKEMON);
			BufferedReader br = new BufferedReader(fileReader);
			br.readLine();
			StringBuilder sb = new StringBuilder();
			String ligne;
			
			// We read each line of pokemonList.csv and put it to pokemonList2.csv
			while ((ligne = br.readLine()) != null) {
				System.out.println("rentre 1");
				String[] pks = ligne.split(",");
				for(Map.Entry<String, HashMap<String, ArrayList<Habilidad>>> entry : this.habilidadPorPokemon.entrySet()) {
					if(pks[0].equals(entry.getKey())) {
						// If there is only one dico (one hability)
						if(entry.getValue().size() == 1) {
							sb.append("," + entry.getValue().get("Habilidad").get(0).getIdHab() + "," + "," + ",");
						}
						else {
							// If there are 2 values in dico for normal habilities
							if(entry.getValue().get("Habilidad").size() == 2) {
								for(Habilidad h : entry.getValue().get("Habilidad")) {
									sb.append("," + h.getIdHab());
								}
							}
							// If there is 1 value1 in dico for normal habilities
							else {
								sb.append("," + entry.getValue().get("Habilidad").get(0).getIdHab() + ",");
							}
							
							// If there are 2 values in dico for oculted habilities
							if(entry.getValue().get("Habilidad oculta").size() == 2) {
								for(Habilidad h : entry.getValue().get("Habilidad oculta")) {
									sb.append("," + h.getIdHab());
								}
							}
							// If there is 1 value in dico for oculted habilities
							else {
								sb.append("," + entry.getValue().get("Habilidad oculta").get(0).getIdHab() + ",");
							}
						}
						csvPrinter.printRecord(ligne + sb);
						System.out.println(ligne + sb);
						sb = new StringBuilder();
					}
				}
			}
			csvPrinter.flush();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void lancerCombat() {
		// Instantiate all Pokemon (if CSV not already created)
		//scrappingWebPokemon();
		
		// Write all Pokemon to a CSV file
		//writePokemonCSV(this.pokemons);
		
		// Instantiate all the habilities (if CSV not already created)
//		scrappingWebAllHabs() ;
		
		// Write all habilities to a CSV file
//		writeHabilitiesCSV(this.habilidades);
		
		// Initialise the diferent lists
		readPokemon("data/pokemonList.csv");
		readHabilities("data/habsList.csv");
		
		// Adds the habilities for diferent Pokemon
		scrappingWebReadHabsFromPokemonAllTables();
		
		// Append habilities to pokemonList.csv
		AppendHabilities();
	}
}
