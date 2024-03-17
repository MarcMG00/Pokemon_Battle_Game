package pokemon.model;

public class Habilidad {
	private int idHab;
	private String nombreHab;
	private String descripcionHab;
	
	public Habilidad(int idHab, String nombreHab, String descripcionHab) {
		super();
		this.idHab = idHab;
		this.nombreHab = nombreHab;
		this.descripcionHab = descripcionHab;
	}

	public int getIdHab() {
		return idHab;
	}

	public void setIdHab(int idHab) {
		this.idHab = idHab;
	}

	public String getNombreHab() {
		return nombreHab;
	}

	public void setNombreHab(String nombreHab) {
		this.nombreHab = nombreHab;
	}

	public String getDescripcionHab() {
		return descripcionHab;
	}

	public void setDescripcionHab(String descripcionHab) {
		this.descripcionHab = descripcionHab;
	}
	
	
}
