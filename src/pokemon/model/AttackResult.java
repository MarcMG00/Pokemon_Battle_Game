package pokemon.model;

public class AttackResult {

	private float damage;
	private boolean hasDealtDamage;
	private boolean isCriticalAttack;
	private boolean continueAttack;

	public void addDamage(float dmg) {
		this.damage += dmg;
		if (dmg > 0)
			this.hasDealtDamage = true;
	}

	public void setCritical(boolean critical) {
		this.isCriticalAttack = critical;
	}

	public boolean isCriticalAttack() {
		return isCriticalAttack;
	}

	public float getDamage() {
		return damage;
	}
	
	public void setDamage(float damage) {
		this.damage = damage;
	}

	public boolean hasDealtDamage() {
		return hasDealtDamage;
	}

	public boolean continueAttack() {
		return continueAttack;
	}

	public void setContinueAttack(boolean continueAttack) {
		this.continueAttack = continueAttack;
	}
}
