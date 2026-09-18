package pokemon.model;

public class AttackResolutionService {

	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	private static final String ANSI_RESET = "\u001B[0m";

	private final DamageService damageService;

	public AttackResolutionService() {
		// TODO Auto-generated constructor stub
		this.damageService = new DamageService();
	}

	// -----------------------------
	// Resolve hit from attack
	// -----------------------------
	public AttackResult resolveHit(AttackContext ctx) {
		AttackResult result = damageService.doDamage(ctx);
		result = resolveDamage(ctx, result);

		return result;
	}

	// -----------------------------
	// Subtract defender PS + defender ability after hit
	// -----------------------------
	public AttackResult resolveDamage(AttackContext ctx, AttackResult result) {
		// Informative : calculate damage based on remaining PS from defender (used for
		// abilities that react over damage received, drain effect, etc.)
		float actualDamage = Math.min(result.getDamage(), ctx.getDefender().getPs());

		ctx.getDefender().setPs(ctx.getDefender().getPs() - actualDamage);
		result.setDamage(actualDamage);

		boolean continueAttack = ctx.getDefender().getAbilitySelected().getEffect().onHit(ctx, result, 0d);
		result.setContinueAttack(continueAttack);

		handleRageAttackDefender(ctx, result);

		return result;
	}

	// -----------------------------
	// Handle attack effects after using it
	// -----------------------------
	public void handleAfterMoveUsed(AttackContext ctx) {
		handleRageAttackAttacker(ctx);
	}

	// -----------------------------
	// Handle rage attack from attacker => start Rage effect (99_Rage)
	// -----------------------------
	private void handleRageAttackAttacker(AttackContext ctx) {
		if (!ctx.getAttack().isRage())
			return;

		System.out.println(
				ANSI_PURPLE + ctx.getAttacker().getName() + " podrá recibir el boost del ataque Furia" + ANSI_RESET);

		ctx.getAttacker().setUsingRageAttack(true);
	}

	// -----------------------------
	// Handle rage attack from defender => apply Rage effect (99_Rage)
	// -----------------------------
	private void handleRageAttackDefender(AttackContext ctx, AttackResult attackRes) {
		// Only active rage boost if attack has dealt damage
		if (!attackRes.hasDealtDamage())
			return;

		// Defender has used Rage attack and is waiting to receive damage
		if (!ctx.getDefender().isUsingRageAttack())
			return;

		boolean isReduceStatStage = false;
		// 126_Contrary ability reverse the increase or reduce stat stage
		if (ctx.getAttacker().hasContraryAbility())
			isReduceStatStage = true;

		ctx.getDefender().handleRageAttack(isReduceStatStage);
	}

}
