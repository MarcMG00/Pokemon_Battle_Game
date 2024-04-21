package pokemon.model;

public class PkVPk {
	private Pokemon pkCombatting;
	private Pokemon pkFacing;
	private float efectividad;
	private int variacion;
	
	public PkVPk(Pokemon pkCombatting, Pokemon pkFacing) {
		this.pkCombatting = pkCombatting;
		this.pkFacing = pkFacing;
		this.efectividad = 0;
		this.variacion = 0;
	}
	
	public void doDammage() {
		
	}
	
	public void doEffect() {
		
	}

}
