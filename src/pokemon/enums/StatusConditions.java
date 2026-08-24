package pokemon.enums;

// Status conditions enumeration
public enum StatusConditions {
	// Ephemeral states / Estados efímeros
	CONFUSED("confuso"), // confuso
	CURSED("maldito"), // maldito
	INFATUATED("enamorado"), // enamorado
	TRAPPED("atrapado"), // atrapado
	SEEDED("drenado"), // drenado
	PERISH_SONG("canto mortal"), // canto mortal
	TRAPPEDBYOWNATTACK("atrapado por su propio ataque"), // Solo puede usar este ataque durante unos turnos
	DRAINEDALLTURNS("drenado todos los turnos"), // Drained but there is no turns (removed by other conditions)

	// Status conditions / Estados persitentes
	PARALYZED("paralizado"), // paralizado
	POISONED("envenenado"), // envenenado
	FROZEN("congelado"), // congelado
	ASLEEP("dormido"), // dormido
	BURNED("quemado"), // quemado
	DISABLE("con su último ataque anulado"), // anula el útlimo ataque usado por el rival durante unos turnos

	// No status
	NO_STATUS("sin estado"), // sin estado

	// Pokemon is debilitated
	DEBILITATED("debilitado"); // debilitado
	
    private final String message;

    StatusConditions(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
