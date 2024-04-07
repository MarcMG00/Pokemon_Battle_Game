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
	public ArrayList<String> getBase() {
		return bases;
	}
	public void setBase(ArrayList<String> bases) {
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
	
	public void addBase(String bs) {
		this.bases.add(bs);
	}
	
}
