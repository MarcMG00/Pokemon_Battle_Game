package pokemon.enums;

// Status conditions enumeration
public enum StatusConditions {
	// Ephemeral states / Estados efímeros
	CONFUSED, // confuso
	CURSED, // maldito
	INFATUATED, // enamorado
	TRAPPED, // atrapado
	SEEDED, // drenado
	PERISH_SONG, // canto mortal
	TRAPPEDBYOWNATTACK, // Solo puede usar este ataque durante unos turnos

	// Status conditions / Estados persitentes
	PARALYZED, // paralizado
	POISONED, // envenenado
	BADLY_POISONED, // gravemente envenando
	FROZEN, // congelado
	ASLEEP, // dormido
	BURNED, // quemado
	DISABLE, // anula el útlimo ataque usado por el rival durante unos turnos

	DRAINEDALLTURNS, // Drained but there is no turns (removed by other conditions)

	// No status
	NO_STATUS, // sin estado

	// Pokemon is debilitated
	DEBILITATED // debilitado
}
