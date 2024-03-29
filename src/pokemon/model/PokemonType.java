package pokemon.model;

public class PokemonType {
	private int idPkTipo;
	private String nombreTipo;
	private float xdanoMenor;
	private float xdanoDebil;
	private float xdanoMayor;
	
	public PokemonType(int idPkTipo, String nombreTipo) {
		super();
		this.idPkTipo = idPkTipo;
		this.nombreTipo = nombreTipo;
		this.xdanoMenor = 0f;
		this.xdanoMayor = 2f;
		this.xdanoDebil = 1.5f;
	}

	public int getIdPkTipo() {
		return idPkTipo;
	}

	public void setIdPkTipo(int idPkTipo) {
		this.idPkTipo = idPkTipo;
	}

	public String getNombreTipo() {
		return nombreTipo;
	}

	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}

	public float getXdanoMenor() {
		return xdanoMenor;
	}

	public void setXdanoMenor(float xdanoMenor) {
		this.xdanoMenor = xdanoMenor;
	}

	public float getXdanoMayor() {
		return xdanoMayor;
	}

	public void setXdanoMayor(float xdanoMayor) {
		this.xdanoMayor = xdanoMayor;
	}

	public float getXdanoDebil() {
		return xdanoDebil;
	}

	public void setXdanoDebil(float xdanoDebil) {
		this.xdanoDebil = xdanoDebil;
	}
	
	
}
