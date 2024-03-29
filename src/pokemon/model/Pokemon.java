package pokemon.model;

import java.util.ArrayList;

public class Pokemon {
	private int idPokemon;
	private String nombrePokemon;
	private int ps;
	private int ata;
	private int def;
	private int vel;
	private int atEsp;
	private int defEsp;
	private ArrayList<Habilidad> normalHabs;
	private ArrayList<Habilidad> ocultedHabs;
	private ArrayList<PokemonType> types;
	
	public Pokemon(int idPokemon, String nombrePokemon, int ps, int ata, int def, int vel, int atEsp, int defEsp) {
		this.idPokemon = idPokemon;
		this.nombrePokemon = nombrePokemon;
		this.ps = ps;
		this.ata = ata;
		this.def = def;
		this.vel = vel;
		this.atEsp = atEsp;
		this.defEsp = defEsp;
		this.normalHabs = new ArrayList<>();
		this.ocultedHabs = new ArrayList<>();
		this.types = new ArrayList<>();
	}

	public int getIdPokemon() {
		return idPokemon;
	}

	public void setIdPokemon(int idPokemon) {
		this.idPokemon = idPokemon;
	}

	public String getNombrePokemon() {
		return nombrePokemon;
	}

	public void setNombrePokemon(String nombrePokemon) {
		this.nombrePokemon = nombrePokemon;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public int getAta() {
		return ata;
	}

	public void setAta(int ata) {
		this.ata = ata;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getVel() {
		return vel;
	}

	public void setVel(int vel) {
		this.vel = vel;
	}

	public int getAtEsp() {
		return atEsp;
	}

	public void setAtEsp(int atEsp) {
		this.atEsp = atEsp;
	}

	public int getDefEsp() {
		return defEsp;
	}

	public void setDefEsp(int defEsp) {
		this.defEsp = defEsp;
	}

	public ArrayList<Habilidad> getNormalHabs() {
		return normalHabs;
	}

	public void setNormalHabs(ArrayList<Habilidad> normalHabs) {
		this.normalHabs = normalHabs;
	}

	public ArrayList<Habilidad> getOcultedHabs() {
		return ocultedHabs;
	}

	public void setOcultedHabs(ArrayList<Habilidad> ocultedHabs) {
		this.ocultedHabs = ocultedHabs;
	}
	
	public ArrayList<PokemonType> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PokemonType> types) {
		this.types = types;
	}

	public void addNormalHab(Habilidad nHab) {
		this.normalHabs.add(nHab);
	}
	
	public void addOcultedHab(Habilidad oHab) {
		this.ocultedHabs.add(oHab);
	}
	
	public void addType(PokemonType pt) {
		this.types.add(pt);
	}
	
	
}
