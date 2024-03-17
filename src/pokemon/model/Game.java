package pokemon.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
	private static final String SAMPLE_CSV_ALL_HABS = "./data/habsList.csv";
	private ArrayList<Pokemon> pokemons;
	private ArrayList<Habilidad> habilidades;
	
	public Game() {
		this.pokemons = new ArrayList<Pokemon>();
		this.habilidades = new ArrayList<Habilidad>();
	}
	
	public ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setPokemons(ArrayList<Pokemon> pokemons) {
		this.pokemons = pokemons;
	}

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
	
	public ArrayList<Habilidad> scrappingWebAllHabs() {
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
												  Normalizer.normalize(tr.select("td>a").get(1).text(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""),
												  Normalizer.normalize(tr.select("td").get(2).text(), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", ""));
					this.habilidades.add(hab);
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
			return null;
	}
	
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
	
	public void lancerCombat() {
		// Instantiate all Pokemon (if CSV not already created)
		//scrappingWebPokemon();
		
		// Write all Pokemon to a CSV file
		//writePokemonCSV(this.pokemons);
		
		// Instantiate all the habilities (if CSV not already created)
		//scrappingWebAllHabs() ;
		
		// Write all habilities to a CSV file
		//writeHabilitiesCSV(this.habilidades);
	}
}
