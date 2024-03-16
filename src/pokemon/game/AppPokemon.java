package pokemon.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AppPokemon {

	public static void main(String[] args) {
		String url = "https://www.pokexperto.net/index2.php?seccion=nds/nationaldex/stats&pk=1";
		
		try {
			Document document = Jsoup.connect(url).get();
			// Gets the class "pkmain" we are interested (it is founded at position 28)
			Element pokemon = document.select(".pkmain").get(28);
			// Prints the content
			System.out.println("================Pkmain================");
			System.out.println(pokemon);
			
			// Gets only the first 6 elements on the table with class "right"
			List<Object> pokemonPS = pokemon.select(".right").stream().limit(6).collect(Collectors.toList());
			
			System.out.println("================Pkmain <tr> in a row================");
			String ps = ((Element) pokemonPS.get(0)).text();
			String ata = ((Element) pokemonPS.get(1)).text();
			String def = ((Element) pokemonPS.get(2)).text();
			String vel = ((Element) pokemonPS.get(3)).text();
			String atEsp = ((Element) pokemonPS.get(4)).text();
			String defEsp = ((Element) pokemonPS.get(5)).text();
			System.out.println(ps);
			System.out.println(ata);
			System.out.println(def);
			System.out.println(vel);
			System.out.println(atEsp);
			System.out.println(defEsp);
			
			System.out.println("================Pkmain <tr> one by one================");
	        for (Object pk : pokemonPS) {
	            Element pkEle = (Element) pk;
	            System.out.println(pkEle.text());
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
