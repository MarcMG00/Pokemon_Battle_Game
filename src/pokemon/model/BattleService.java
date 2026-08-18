package pokemon.model;

import java.util.Scanner;

import pokemon.enums.StatusConditions;

public class BattleService {
	private final AttackService attackService;
	private final AbilityService abilityService;
	private final WeatherService weatherService;
	private final BattleContext battleCtx;

	public BattleService(BattleContext battleCtx) {
		this.battleCtx = battleCtx;

		this.attackService = new AttackService(battleCtx);
		this.abilityService = new AbilityService();
		this.weatherService = new WeatherService(battleCtx);

	}

	public void startBattle() {
		int nbRound = 1;
		Scanner sc = new Scanner(System.in);

		abilityService.resolveEntryAbilities(battleCtx);
		weatherService.applyWeatherSuppressionIfNeeded(battleCtx);

		while (battleCtx.getIa().getPokemon().size() >= 1 && battleCtx.getPlayer().getPokemon().size() >= 1) {
			System.out.println("----------------------------------");
			System.out.println("Let's start round nº : " + nbRound);
			System.out.println("----------------------------------");

			Pokemon pkPlayer = battleCtx.getPkPlayer();
			Pokemon pkIA = battleCtx.getPkIA();

			pkPlayer.restartParametersEffectInitialTurn();
			pkIA.restartParametersEffectInitialTurn();

			boolean playerIsCharging = pkPlayer.isChargingAttackForNextRound();

			boolean playerIsTrapped = pkPlayer.hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)
					&& pkPlayer.getEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK).getNbTurns() > 0;

			int attackChoice = (playerIsCharging || playerIsTrapped) ? 1 : attackService.getPlayerChoice(sc);

			if (attackChoice == 1)
				attackService.handleAttackTurn(sc);
			else {
				boolean cancelled = attackService.handlePlayerSwitchIAAttacks(sc);

				if (cancelled) {
					System.out.println("Cambio cancelado. Regresando al menú...");
					nbRound--;
				}
			}

			nbRound++;
		}

		System.out.println("----------------------------------");
		System.out.println("Fin del combate...");
		System.out.println("----------------------------------");
	}
}
