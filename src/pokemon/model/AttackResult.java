package pokemon.model;

public class AttackResult {

	private float damage;
	private boolean hasDealtDamage;

	public void addDamage(float dmg) {
		this.damage += dmg;
		if (dmg > 0)
			this.hasDealtDamage = true;
	}

	public float getDamage() {
		return damage;
	}

	public boolean hasDealtDamage() {
		return hasDealtDamage;
	}
}
