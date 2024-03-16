package pokemon.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Game {
	private static final String SAMPLE_CSV_FILE = "./pokemonList.csv";
	private ArrayList<Pokemon> pokemons;
	
	public Game() {
		this.pokemons = new ArrayList<Pokemon>();
	}
	
	public ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setPokemons(ArrayList<Pokemon> pokemons) {
		this.pokemons = pokemons;
	}
	
//	public static void main(String[] args) {
//		
//		try {	
//			// Total nb of Pokemon
//			int nbPk = 809;
//			// List of Pokemon
//			ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
//			
//			// We put all the Pokemon to the list
//			for(int id = 1; id <= nbPk; id++) {
//				// Each Pokemon change by id in the url
//				String url = "https://www.pokexperto.net/index2.php?seccion=nds/nationaldex/stats&pk=" + id;
//				// Gets the class "pkmain" we are interested (it is founded at position 28) for estadistics
////				int classPkMain = 28;
//				
//				Document document = Jsoup.connect(url).get();
////				Element pokemon = document.select(".pkmain").get(classPkMain);
//				
//				// Gets the class "pktitle" for the name
//				Element pokemonName = document.selectFirst(".pktitle");
//				String name = pokemonName.select(".mini").text();
//				
//				// Prints the content
//				System.out.println("================Pkmain name================");
//				System.out.println(name);
//				
//				// Counts number of class "pkmain"
//				Elements ntmDivs = document.getElementsByClass("pkmain");
//				int ntmAmount = ntmDivs.size();
//				System.out.println(ntmAmount);
//				
//				// Gets only the first 6 estadistics elements on the table with class "right"
////				List<Object> pokemonStats = pokemon.select(".right").stream().limit(6).collect(Collectors.toList());
////				if(pokemonStats.size() < 6) {
////					do {
////						classPkMain += 1;
////						pokemon = document.select(".pkmain").get(classPkMain);
////						pokemonStats = pokemon.select(".right").stream().limit(6).collect(Collectors.toList());
////					}
////					while(pokemonStats.size() < 6);
////				}
//			}
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public ArrayList<Pokemon> scrappingWebPokemon() {
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
				
				// Prints the content (to know the current pokemon treating)
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
				
//				for(Pokemon p : pokemons) {
//					System.out.println(p.getIdPokemon() + " - " + 
//									   p.getNombrePokemon() + " - " + 
//									   p.getPs() + " - " + 
//									   p.getAta() + " - " + 
//									   p.getDef() + " - " + 
//									   p.getVel() + " - " + 
//									   p.getAtEsp() + " - " + 
//									   p.getDefEsp());
//				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
			return this.pokemons;
	}
	
	public static void writePokemonCSV(ArrayList<Pokemon> pokemons) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));

	        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
	                .withHeader("PokemonId", "PokemonName", "PS", "Ata", "Def", "Vel", "AtEsp", "DefEsp"));
	        
	        for(Pokemon p : pokemons) {
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
	
	public void lancerCombat() {
		// Instantiate all Pokemon (if CSV not already created)
		this.pokemons = scrappingWebPokemon();
		
		// Write all Pokemon to a CSV file
		writePokemonCSV(this.pokemons);
	}
}
