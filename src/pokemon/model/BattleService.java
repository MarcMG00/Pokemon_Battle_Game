package pokemon.model;

import java.util.Scanner;

import pokemon.enums.StatusConditions;

public class BattleService {
	private final Player player;
	private final Player ia;

	private final AttackService attackService;
	private final AbilityService abilityService;
	private final WeatherService weatherService;

	public BattleService(Game game, Player player, Player ia) {
		this.player = player;
		this.ia = ia;

		this.attackService = new AttackService(game);
		this.abilityService = new AbilityService(game);
		this.weatherService = new WeatherService(game);
	}

	public void startBattle() {
		int nbRound = 1;
		Scanner sc = new Scanner(System.in);

		abilityService.applyTraceOnBattleStart(player.getPkCombatting(), ia.getPkCombatting());

		abilityService.applyAbilities(player.getPkCombatting(), ia.getPkCombatting());

		weatherService.applyEntryWeatherAbilities();

		while (ia.getPokemon().size() >= 1 && player.getPokemon().size() >= 1) {
			System.out.println("----------------------------------");
			System.out.println("Let's start round nº : " + nbRound);
			System.out.println("----------------------------------");

			Pokemon pkPlayer = player.getPkCombatting();

			boolean playerIsCharging = pkPlayer.getIsChargingAttackForNextRound();

			boolean playerIsTrapped = pkPlayer.hasActiveEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK)
					&& pkPlayer.getEphemeralStatus(StatusConditions.TRAPPEDBYOWNATTACK).getNbTurns() > 0;

			int attackChoice = (playerIsCharging || playerIsTrapped) ? 1 : attackService.getPlayerChoice(sc);

			if (attackChoice == 1)
				attackService.handleAttackTurn(sc);
			else {
				if (ia.getPkCombatting().getAbilitySelected().getId() == 23) {
					System.out.println("No puedes cambiar de Pokémon a causa de Sombra trampa");

					nbRound--;

				} else {
					boolean cancelled = !attackService.handleChangeTurn(sc);

					if (cancelled) {
						System.out.println("Cambio cancelado. Regresando al menú...");
						nbRound--;
					}
				}
			}

			nbRound++;
		}
	}
}
