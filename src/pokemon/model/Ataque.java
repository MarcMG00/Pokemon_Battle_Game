package pokemon.model;

import java.util.ArrayList;

public class Ataque {
	private int idAta;
	private String nombreAta;
	private String tipo;
	private ArrayList<String> bases;
	private int poder;
	private int pp;
	private int precision;
	private String efecto;
	private PokemonType strTipoToPkType;
	private float efectividadContraPkAdversario;
	private float bonificacion;
	
	public Ataque() {
		super();
		this.idAta = 0;
		this.nombreAta = "";
		this.tipo = "";
		this.bases = new ArrayList<>();
		this.poder = 0;
		this.pp = 0;
		this.precision = 0;
		this.efecto = "";
		this.strTipoToPkType = new PokemonType();
		this.efectividadContraPkAdversario = 0;
		this.bonificacion = 0;
	}
	
	public Ataque(int idAta, String nombreAta, String tipo, int poder, int pp, int precision, String efecto) {
		super();
		this.idAta = idAta;
		this.nombreAta = nombreAta;
		this.tipo = tipo;
		this.bases = new ArrayList<>();
		this.poder = poder;
		this.pp = pp;
		this.precision = precision;
		this.efecto = efecto;
		this.strTipoToPkType = new PokemonType();
		this.efectividadContraPkAdversario = 0;
		this.bonificacion = 0;
	}
	
	public int getIdAta() {
		return idAta;
	}
	public void setIdAta(int idAta) {
		this.idAta = idAta;
	}
	public String getNombreAta() {
		return nombreAta;
	}
	public void setNombreAta(String nomberAta) {
		this.nombreAta = nomberAta;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public ArrayList<String> getBases() {
		return bases;
	}
	public void setBases(ArrayList<String> bases) {
		this.bases = bases;
	}
	public int getPoder() {
		return poder;
	}
	public void setPoder(int poder) {
		this.poder = poder;
	}
	public int getPp() {
		return pp;
	}
	public void setPp(int pp) {
		this.pp = pp;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public String getEfecto() {
		return efecto;
	}
	public void setEfecto(String efecto) {
		this.efecto = efecto;
	}
	public PokemonType getStrTipoToPkType() {
		return strTipoToPkType;
	}
	public void setStrTipoToPkType(PokemonType strTipoToPkType) {
		this.strTipoToPkType = strTipoToPkType;
	}
	public float getEfectividadContraPkAdversario() {
		return efectividadContraPkAdversario;
	}
	public void setEfectividadContraPkAdversario(float efectividadContraPkAdversario) {
		this.efectividadContraPkAdversario = efectividadContraPkAdversario;
	}
	public float getBonificacion() {
		return bonificacion;
	}
	public void setBonificacion(float bonificacion) {
		this.bonificacion = bonificacion;
	}

	// Add type of base to an Attack
	public void addBase(String bs) {
		this.bases.add(bs);
	}
	
	// Set the type of the attack to his Pokemon type instead of a string
	public void transformStrTipoToPokemonType(ArrayList<PokemonType> types) {
		this.setStrTipoToPkType(types.stream().filter(pk -> pk.getNombreTipo().equals(this.getTipo())).findFirst().get());
	}	
}
