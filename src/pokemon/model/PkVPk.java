package pokemon.model;

import java.util.Random;

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

	// Knows the evasion or accuracy for the pokemon selected (1 is for accuracy, 2 is for evasion)
	public float getEvasionOrAccuracy(Pokemon pk, int t) {
		
		int evAcu = 0;
		float resultEvAcu = 1;
		
		switch(t) {
			case 1:
				evAcu = pk.getPuntosPrecision();
				break;
			case 2:
				evAcu = pk.getPuntosEvasion();
				break;
		}
		
		switch(evAcu) {
			case -6 :
				resultEvAcu = 3/9;
				break;
			case -5 :
				resultEvAcu = 3/8;
				break;
			case -4 :
				resultEvAcu = 3/7;
				break;
			case -3 :
				resultEvAcu = 3/6;
				break;
			case -2 :
				resultEvAcu = 3/5;
				break;
			case -1 :
				resultEvAcu = 3/4;
				break;
			case 1 :
				resultEvAcu = 4/3;
				break;
			case 2 :
				resultEvAcu = 5/3;
				break;
			case 3 :
				resultEvAcu = 6/3;
				break;
			case 4 :
				resultEvAcu = 7/3;
				break;
			case 5 :
				resultEvAcu = 8/3;
				break;
			case 6 :
				resultEvAcu = 9/3;
				break;
		}
		
		return resultEvAcu;
	}

	// Sets if the attack will be accurate or not
	public void getProbabilityOfAttacking() {
		// gets accuracy/evasion
		float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100)
				* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));

		// Get the attack effect (cause 100% is the max)
		if (a >= 1) {
			getAttackEffect();
		}
		else {
			Random randomEfectivityFloat = new Random();
			float randomEfectivity = randomEfectivityFloat.nextFloat();
			
			if(randomEfectivity >= a) {
				// Get the attack effect
				getAttackEffect();
			}
			else {
				// Pokemon facing avoided the attack
			}
		}
	}
	
	// Gets if an attack is critic (x2 of damage)
	public boolean getCriticity() {
		boolean isCritic = false;
		
		int randomCritico;
		randomCritico = (int)(Math.random() * 100);
		
		// 10/100 of probabilities to have a critic attack
		if(randomCritico <= 10) {
			isCritic = true;
		}
		
		return isCritic;
	}
	
	public float getAttackEffect() {
		
		float dmg = 0;
		boolean isCritic;
		int nbTimesAttack;
		
		switch(this.getPkCombatting().getNextMouvement().getIdAta()) {
		// Destructor
		case 1:
			dmg = doDammage();
			isCritic = getCriticity();
			if(isCritic) {
				dmg = dmg * 2;
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp()-1);
			break;
			
		// Golpe kárate
		case 2:
			dmg = doDammage();
			int randomCritico;
			randomCritico = (int)(Math.random() * 100);
			
			// 10/100 of probabilities to have a critic attack
			if(randomCritico <= 40) {
				dmg = dmg * 2;
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp()-1);
			break;
		
		// Doble bofetón
		case 3:
			dmg = doDammage();
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);
			dmg = dmg * nbTimesAttack;
			isCritic = getCriticity();
			if(isCritic) {
				dmg = dmg * 2;
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp()-1);
			break;
			
		// Puño cometa
		case 4:
			dmg = doDammage();
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);
			dmg = dmg * nbTimesAttack;
			isCritic = getCriticity();
			if(isCritic) {
				dmg = dmg * 2;
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp()-1);
			break;
			
		// Megapuño
		case 5:
			dmg = doDammage();
			isCritic = getCriticity();
			if(isCritic) {
				dmg = dmg * 2;
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp()-1);
			break;
		
		// Día de pago
		case 6:
			dmg = doDammage();
			isCritic = getCriticity();
			if(isCritic) {
				dmg = dmg * 2;
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp()-1);
			break;
			
		// Puño fuego
		case 7:
			
		}
		return dmg;
	}

	// Applies the damage
	public float doDammage() {
		int randomVariacion;
		randomVariacion = (int) ((Math.random() * (100 - 85)) + 85);
		boolean isAtaEspecial = this.getPkCombatting().getNextMouvement().getBases().contains("especial");
		float dmg = 0;
		if (isAtaEspecial) {
			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonificacion()
					* this.getPkCombatting().getNextMouvement().getEfectividadContraPkAdversario() * randomVariacion
					* (((0.2f * 100 + 1) * this.getPkCombatting().getAtEsp()
							* this.getPkCombatting().getNextMouvement().getPoder())
							/ (25 * this.getPkFacing().getDefEsp()) + 2);
		} else {
			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonificacion()
					* this.getPkCombatting().getNextMouvement().getEfectividadContraPkAdversario() * randomVariacion
					* (((0.2f * 100 + 1) * this.getPkCombatting().getAta()
							* this.getPkCombatting().getNextMouvement().getPoder()) / (25 * this.getPkFacing().getDef())
							+ 2);
		}

		return dmg;
	}

	public void doEffect() {

	}

}
