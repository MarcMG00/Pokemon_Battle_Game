package pokemon.attackInterface;

import pokemon.model.AttackContext;
import pokemon.model.AttackResult;

public interface AttackEffect {
	AttackResult execute(AttackContext ctx);
}
