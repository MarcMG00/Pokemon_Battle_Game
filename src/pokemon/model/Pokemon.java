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
	private ArrayList<Ataque> ataFisicos;
	private ArrayList<Ataque> ataEspeciales;
	private ArrayList<Ataque> ataEstado;
	private ArrayList<Ataque> cuatroAtaques;
	
	
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
		this.ataFisicos = new ArrayList<>();
		this.ataEspeciales = new ArrayList<>();
		this.ataEstado = new ArrayList<>();
		this.cuatroAtaques = new ArrayList<>();
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
	
	public ArrayList<Ataque> getAtaFisicos() {
		return ataFisicos;
	}

	public void setAtaFisicos(ArrayList<Ataque> ataFisicos) {
		this.ataFisicos = ataFisicos;
	}

	public ArrayList<Ataque> getAtaEspeciales() {
		return ataEspeciales;
	}

	public void setAtaEspeciales(ArrayList<Ataque> ataEspeciales) {
		this.ataEspeciales = ataEspeciales;
	}

	public ArrayList<Ataque> getAtaEstado() {
		return ataEstado;
	}

	public void setAtaEstado(ArrayList<Ataque> ataEstado) {
		this.ataEstado = ataEstado;
	}

	public ArrayList<Ataque> getCuatroAtaques() {
		return cuatroAtaques;
	}

	public void setCuatroAtaques(ArrayList<Ataque> cuatroAtaques) {
		this.cuatroAtaques = cuatroAtaques;
	}

	// Adds habs to a Pokemon
	public void addNormalHab(Habilidad nHab) {
		this.normalHabs.add(nHab);
	}
	
	// Adds oculted habs to a Pokemon
	public void addOcultedHab(Habilidad oHab) {
		this.ocultedHabs.add(oHab);
	}
	
	// Adds types to a Pokemon
	public void addType(PokemonType pt) {
		this.types.add(pt);
	}
	
	// Adds fisic attacs to a Pokemon
	public void addAtaFisicos(Ataque ataF) {
		this.ataFisicos.add(ataF);
	}
	
	// Adds special attacs to a Pokemon
	public void addAtaEspeciales(Ataque ataE) {
		this.ataEspeciales.add(ataE);
	}
	
	// Adds other attacs to a Pokemon
	public void addAtaEstado(Ataque ataO) {
		this.ataEstado.add(ataO);
	}

	// Adds the four final attacs to a Pokemon
	public void addAtaques(Ataque ata) {
		this.cuatroAtaques.add(ata);
	}
	
}
