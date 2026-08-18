package pokemon.model;

public class AttackResult {

	private float damage;
	private boolean hasDealtDamage;
	private boolean isCriticalAttack;

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

	public boolean hasDealtDamage() {
		return hasDealtDamage;
	}
}
