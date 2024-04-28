package pokemon.model;

public class PkVPk {
	private Pokemon pkCombatting;
	private Pokemon pkFacing;
	private float efectividad;
	private int variacion;
	private float finalDamage;
	private float bonificacion;
	
	public PkVPk(Pokemon pkAttacking, Pokemon pkFacing) {
		this.pkCombatting = pkAttacking;
		this.pkFacing = pkFacing;
		this.efectividad = 0;
		this.variacion = 0;
		this.finalDamage = 0;
		this.bonificacion = 0;
	}
	
	public Pokemon getPkCombatting() {
		return pkCombatting;
	}
	public void setPkCombatting(Pokemon pkCombatting) {
		this.pkCombatting = pkCombatting;
	}
	public Pokemon getPkFacing() {
		return pkFacing;
	}
	public void setPkFacing(Pokemon pkFacing) {
		this.pkFacing = pkFacing;
	}
	public float getEfectividad() {
		return efectividad;
	}
	public void setEfectividad(float efectividad) {
		this.efectividad = efectividad;
	}
	public int getVariacion() {
		return variacion;
	}
	public void setVariacion(int variacion) {
		this.variacion = variacion;
	}
	public float getFinalDamage() {
		return finalDamage;
	}
	public void setFinalDamage(float finalDamage) {
		this.finalDamage = finalDamage;
	}	
	public float getBonificacion() {
		return bonificacion;
	}
	public void setBonificacion(float bonificacion) {
		this.bonificacion = bonificacion;
	}

	// Applies the damage (search for the attac cause a lot of attacks have diferent effects)
	// Posar precisio
	public float doDammage() {
		int randomVariacion;
		randomVariacion = (int)((Math.random() * (100-85)) + 85);
		boolean isAtaEspecial = this.getPkCombatting().getNextMouvement().getBases().contains("especial");
		float dmg = 0;
		if(isAtaEspecial) {
			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonificacion() * this.getPkCombatting().getNextMouvement().getEfectividadContraPkAdversario() * randomVariacion*
					(((0.2f * 100 + 1) * this.getPkCombatting().getAtEsp() * this.getPkCombatting().getNextMouvement().getPoder()) / (25 * this.getPkFacing().getDefEsp()) + 2);
		}
		else {
			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonificacion() * this.getPkCombatting().getNextMouvement().getEfectividadContraPkAdversario() * randomVariacion*
					(((0.2f * 100 + 1) * this.getPkCombatting().getAta() * this.getPkCombatting().getNextMouvement().getPoder()) / (25 * this.getPkFacing().getDef()) + 2);
		}
		
		return dmg;
	}
	
	public void doEffect() {
		
	}

}
