package pokemon.model;

public class StatusResult {
	private boolean attackerFainted;

	public StatusResult(boolean attackerFainted) {
		this.attackerFainted = attackerFainted;
	}

	public boolean isAttackerFainted() {
		return attackerFainted;
	}
}
