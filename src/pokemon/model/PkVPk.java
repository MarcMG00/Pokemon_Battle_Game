package pokemon.model;

import pokemon.enums.EstadosEnum;

public class PkVPk {
	private Pokemon pkCombatting;
	private Pokemon pkFacing;
	private float efectividad;
	private int variacion;
	private float finalDamage;
	private float bonificacion;

	public PkVPk(Pokemon pkAttacking, Pokemon pkFacing) {
		this.pkCombatting = pkAttacking;
		this.pkFacing = pkFacing;
		this.efectividad = 0;
		this.variacion = 0;
		this.finalDamage = 0;
		this.bonificacion = 0;
	}

	public Pokemon getPkCombatting() {
		return pkCombatting;
	}

	public void setPkCombatting(Pokemon pkCombatting) {
		this.pkCombatting = pkCombatting;
	}

	public Pokemon getPkFacing() {
		return pkFacing;
	}

	public void setPkFacing(Pokemon pkFacing) {
		this.pkFacing = pkFacing;
	}

	public float getEfectividad() {
		return efectividad;
	}

	public void setEfectividad(float efectividad) {
		this.efectividad = efectividad;
	}

	public int getVariacion() {
		return variacion;
	}

	public void setVariacion(int variacion) {
		this.variacion = variacion;
	}

	public float getFinalDamage() {
		return finalDamage;
	}

	public void setFinalDamage(float finalDamage) {
		this.finalDamage = finalDamage;
	}

	public float getBonificacion() {
		return bonificacion;
	}

	public void setBonificacion(float bonificacion) {
		this.bonificacion = bonificacion;
	}

	// Knows the evasion or accuracy for the pokemon selected (1 is for accuracy, 2
	// is for evasion)
	public float getEvasionOrAccuracy(Pokemon pk, int t) {

		int evAcu = 0;
		float resultEvAcu = 1;

		switch (t) {
		case 1:
			evAcu = pk.getPuntosPrecision();
			break;
		case 2:
			evAcu = pk.getPuntosEvasion();
			break;
		}

		switch (evAcu) {
		case -6:
			resultEvAcu = 3 / 9;
			break;
		case -5:
			resultEvAcu = 3 / 8;
			break;
		case -4:
			resultEvAcu = 3 / 7;
			break;
		case -3:
			resultEvAcu = 3 / 6;
			break;
		case -2:
			resultEvAcu = 3 / 5;
			break;
		case -1:
			resultEvAcu = 3 / 4;
			break;
		case 1:
			resultEvAcu = 4 / 3;
			break;
		case 2:
			resultEvAcu = 5 / 3;
			break;
		case 3:
			resultEvAcu = 6 / 3;
			break;
		case 4:
			resultEvAcu = 7 / 3;
			break;
		case 5:
			resultEvAcu = 8 / 3;
			break;
		case 6:
			resultEvAcu = 9 / 3;
			break;
		}

		return resultEvAcu;
	}

//	// Sets if the attack will be accurate or not
//	public void getProbabilityOfAttackingAndAttack() {
//
//		// Do attacks if Pokemon facing is not doing strange attacks that Pokemon
//		// combatting cannot attack
//		if (!(this.getPkFacing().getNextMouvement().getIdAta() == 19
//				&& this.getPkFacing().isChargingAttackForNextRound())) {
//			// gets accuracy/evasion
//			float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100)
//					* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
//
//			// Get the attack effect (cause 100% is the max)
//			if (a >= 1) {
//				getAttackEffect();
//				// See the accuracy of the attack
//			} else {
//				int randomEfectivity = 0;
//				randomEfectivity = (int) (Math.random() * 100);
//
//				if (randomEfectivity <= a) {
//					// Get the attack effect
//					this.getAttackEffect();
//				} else {
//					// Reduces the PP of the movement
//					this.getPkCombatting().getNextMouvement()
//							.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
//					// Pokemon facing AVOIDED the attack
//					System.out.println(this.getPkFacing().getNombrePokemon() + " evitó el ataque");
//				}
//			}
//		} else {
//			// Verify if a Pokemon can attack if Pokemon facing is doing strange attacks
//			if ((this.getPkFacing().getNextMouvement().getIdAta() == 19
//					&& this.getPkFacing().isChargingAttackForNextRound())
//					&& (this.getPkCombatting().getNextMouvement().getIdAta() == 16
//							|| this.getPkCombatting().getNextMouvement().getIdAta() == 18
//							|| this.getPkCombatting().getNextMouvement().getIdAta() == 87
//							|| this.getPkCombatting().getNextMouvement().getIdAta() == 239
//							|| this.getPkCombatting().getNextMouvement().getIdAta() == 327
//							|| this.getPkCombatting().getNextMouvement().getIdAta() == 479
//							|| this.getPkCombatting().getNextMouvement().getIdAta() == 542)) {
//
//				// gets accuracy/evasion
//				float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100)
//						* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
//				// Get the attack effect (cause 100% is the max)
//				if (a >= 1) {
//					getAttackEffect();
//				} else {
//					int randomEfectivity = 0;
//					randomEfectivity = (int) (Math.random() * 100);
//
//					if (randomEfectivity <= a) {
//						// Get the attack effect
//						getAttackEffect();
//					} else {
//						// Reduces the PP of the movement
//						this.getPkCombatting().getNextMouvement()
//								.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
//						// Pokemon facing AVOIDED the attack
//						System.out.println(this.getPkFacing().getNombrePokemon() + " evitó el ataque");
//					}
//				}
//			} else {
//				// Reduces the PP of the movement
//				this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
//				// Pokemon facing avoided the attack (because pk facing is flying)
//				System.out.println(this.getPkFacing().getNombrePokemon() + " evitó el ataque");
//			}
//		}
//	}

	// Sets if the attack will be accurate or not
	public void getProbabilityOfAttackingAndAttack(boolean useAttackChargedNow) {

		// Apply the attack charged
		if (useAttackChargedNow && !this.getPkCombatting().isAlreadyUsedVuelo()) {
			// gets accuracy/evasion
			float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100)
					* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
			// Get the attack effect (cause 100% is the max)
			if (a >= 1) {
				getAttackEffect(true);
			} else {
				int randomEfectivity = 0;
				randomEfectivity = (int) (Math.random() * 100);

				if (randomEfectivity / 100 <= a) {
					// Get the attack effect
					getAttackEffect(true);
				} else {
					// Reduces the PP of the movement
					this.getPkCombatting().getNextMouvement()
							.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
					// Pokemon facing AVOIDED the attack
					System.out.println(this.getPkFacing().getNombrePokemon()
							+ " evitó el ataque (vuelo - Pokemon atacante falló 1)");
				}
			}
			// Cannot attack first for next run (has to wait after the Pokemon facing
			// attacks)
			this.getPkCombatting().setAlreadyUsedVuelo(true);
			this.getPkCombatting().setChargingAttackForNextRound(false);
		} else if (!useAttackChargedNow && this.getPkCombatting().isAlreadyUsedVuelo()) {
			// gets accuracy/evasion
			float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100)
					* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
			// Get the attack effect (cause 100% is the max)
			if (a >= 1) {
				getAttackEffect(false);
			} else {
				int randomEfectivity = 0;
				randomEfectivity = (int) (Math.random() * 100);

				if (randomEfectivity / 100 <= a) {
					// Get the attack effect
					getAttackEffect(false);
				} else {
					// Reduces the PP of the movement
					this.getPkCombatting().getNextMouvement()
							.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
					// Pokemon facing AVOIDED the attack
					System.out.println(this.getPkFacing().getNombrePokemon()
							+ " evitó el ataque (vuelo - Pokemon atacante falló 2)");
				}
			}
			// Cannot attack first for next run (has to wait after the Pokemon facing
			// attacks)
			this.getPkCombatting().setAlreadyUsedVuelo(false);
			this.getPkCombatting().setChargingAttackForNextRound(true);
		} else {
			// Verify if a Pokemon can attack if Pokemon facing is doing strange attacks
			if ((this.getPkFacing().getNextMouvement().getIdAta() == 19
					&& this.getPkFacing().isChargingAttackForNextRound())
					&& (this.getPkCombatting().getNextMouvement().getIdAta() == 16
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 18
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 87
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 239
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 327
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 479
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 542)) {

				// gets accuracy/evasion
				float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100)
						* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
				// Get the attack effect (cause 100% is the max)
				if (a >= 1) {
					getAttackEffect(true);
				} else {
					int randomEfectivity = 0;
					randomEfectivity = (int) (Math.random() * 100);

					if (randomEfectivity / 100 <= a) {
						// Get the attack effect
						getAttackEffect(true);
					} else {
						// Reduces the PP of the movement
						this.getPkCombatting().getNextMouvement()
								.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
						// Pokemon facing AVOIDED the attack
						System.out.println(this.getPkCombatting().getNombrePokemon() + " usó "
								+ this.getPkCombatting().getNextMouvement().getNombreAta() + " (verificació)");
						System.out.println(this.getPkFacing().getNombrePokemon()
								+ " evitó el ataque (vuelo - con movimiento que lo counteree)");
					}
				}
			}
			// Pk facing avoid the attack cause is not using an attack that can hurt while
			// Pokemon facinf is flying
			else if ((this.getPkFacing().getNextMouvement().getIdAta() == 19
					&& this.getPkFacing().isChargingAttackForNextRound())
					&& !(this.getPkCombatting().getNextMouvement().getIdAta() == 16
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 18
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 87
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 239
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 327
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 479
							|| this.getPkCombatting().getNextMouvement().getIdAta() == 542)) {
				// Reduces the PP of the movement
				this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
				// Pokemon facing avoided the attack (because pk facing is flying)
				System.out.println(this.getPkCombatting().getNombrePokemon() + " usó "
						+ this.getPkCombatting().getNextMouvement().getNombreAta());
				System.out.println(this.getPkFacing().getNombrePokemon()
						+ " evitó el ataque (vuelo - sin movimiento que lo counteree)");
			}
			// Prepare attack for flying
			else if (this.getPkCombatting().getNextMouvement().getIdAta() == 19
					&& !this.getPkCombatting().isChargingAttackForNextRound()) {
				getAttackEffect(true);
			}
			// Normally enters to attack when Pokemon combatting is flying
			else if (this.getPkCombatting().getNextMouvement().getIdAta() == 19
					&& this.getPkCombatting().isChargingAttackForNextRound()) {
				// gets accuracy/evasion
				float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100)
						* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));

				// Get the attack effect (cause 100% is the max)
				if (a >= 1) {
					getAttackEffect(true);
					// See the accuracy of the attack
				} else {
					int randomEfectivity = 0;
					randomEfectivity = (int) (Math.random() * 100);

					if (randomEfectivity / 100 <= a) {
						// Get the attack effect
						this.getAttackEffect(true);
					} else {
						// Reduces the PP of the movement
						this.getPkCombatting().getNextMouvement()
								.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
						// Pokemon facing AVOIDED the attack
						System.out.println(this.getPkFacing().getNombrePokemon() + " evitó el ataque");
					}
				}
			}
			// Do attacks if Pokemon facing is not doing strange attacks that Pokemon
			// combatting cannot attack
			else if (!(this.getPkFacing().getNextMouvement().getIdAta() == 19
					&& this.getPkFacing().isChargingAttackForNextRound())) {
				// gets accuracy/evasion
				float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100)
						* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));

				// Get the attack effect (cause 100% is the max)
				if (a >= 1) {
					getAttackEffect(true);
					// See the accuracy of the attack
				} else {
					int randomEfectivity = 0;
					randomEfectivity = (int) (Math.random() * 100);

					if (randomEfectivity / 100 <= a) {
						// Get the attack effect
						this.getAttackEffect(true);
					} else {
						// Reduces the PP of the movement
						this.getPkCombatting().getNextMouvement()
								.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
						// Pokemon facing AVOIDED the attack
						System.out.println(this.getPkFacing().getNombrePokemon() + " evitó el ataque");
					}
				}
			} else {
				// gets accuracy/evasion
				float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100)
						* (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
				// Get the attack effect (cause 100% is the max)
				if (a >= 1) {
					getAttackEffect(true);
				} else {
					int randomEfectivity = 0;
					randomEfectivity = (int) (Math.random() * 100);

					if (randomEfectivity / 100 <= a) {
						// Get the attack effect
						getAttackEffect(true);
					} else {
						// Reduces the PP of the movement
						this.getPkCombatting().getNextMouvement()
								.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
						// Pokemon facing AVOIDED the attack
						System.out.println(this.getPkFacing().getNombrePokemon() + " evitó el ataque");
					}
				}
			}
		}
	}

	// Gets if an attack is critic (x2 of damage)
	public boolean getCriticity() {
		boolean isCritic = false;

		int randomCritico;
		randomCritico = (int) (Math.random() * 100);

		// 10/100 of probabilities to have a critic attack
		if (randomCritico <= 10) {
			isCritic = true;
		}
		return isCritic;
	}

	// Gets the attack effect and apply damage
	public float getAttackEffect(boolean alreadyCheckedAttack) {

		float dmg = 0;
		boolean isCritic;
		int nbTimesAttack;
		int nbTurnosEstado;
		int porcientoCaerEnEstado;
		int highProbabilityCritic;
		int setBaseDmgFromBegining;

		switch (this.getPkCombatting().getNextMouvement().getIdAta()) {
		// Destructor (tested)
		case 1:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Arañazo");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Golpe kárate (tested)
		case 2:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Golpe kárate");
			dmg = doDammage();
			highProbabilityCritic = (int) (Math.random() * 100);

			// 10/100 of probabilities to have a critic attack
			if (highProbabilityCritic <= 40) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Doble bofetón (tested)
		case 3:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Doble bofetón");
			dmg = doDammage();
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);
			System.out.println(" nº de veces : " + nbTimesAttack);
			dmg = dmg * nbTimesAttack;
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Puño cometa (tested)
		case 4:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Puño coñeta");
			dmg = doDammage();
			nbTimesAttack = (int) ((Math.random() * (5 - 1)) + 1);
			System.out.println(" nº de veces : " + nbTimesAttack);
			dmg = dmg * nbTimesAttack;
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Megapuño (tested)
		case 5:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Megapuño");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Día de pago (tested)
		case 6:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Día de pago");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Puño fuego (tested)
		case 7:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Puño fuego");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			porcientoCaerEnEstado = (int) (Math.random() * 100);
			System.out.println("proba de quemar : " + porcientoCaerEnEstado);
			if (porcientoCaerEnEstado <= 10) {
				// Check if the Pokemon facing isn't without any estado
				if (this.getPkFacing().getEstadoPersistente().getEstadoEnum() == EstadosEnum.SIN_ESTADO) {
					nbTurnosEstado = (int) ((Math.random() * (5 - 2)) + 2);
					Estado quemado = new Estado(EstadosEnum.QUEMADO, nbTurnosEstado);
					this.getPkFacing().setEstadoPersistente(quemado);
					System.out.println(
							this.getPkFacing().getNombrePokemon() + " fue quemado por " + nbTurnosEstado + " turnos");
				}
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Puño hielo
		case 8:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Puño hielo");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			// Ice pokemon cannot be frozen
			if (this.pkFacing.getTypes().stream().filter(t -> t.getIdPkTipo() == 9).findAny().get() == null) {
				porcientoCaerEnEstado = (int) (Math.random() * 100);
				System.out.println("proba de congelar : " + porcientoCaerEnEstado);
				// 10% of probabilities to be frozen
				if (porcientoCaerEnEstado <= 10) {
					if (this.getPkFacing().getEstadoPersistente().getEstadoEnum() == EstadosEnum.SIN_ESTADO) {
						nbTurnosEstado = (int) ((Math.random() * (5 - 2)) + 2);
						Estado congelado = new Estado(EstadosEnum.CONGELADO, nbTurnosEstado);
						this.getPkFacing().setEstadoPersistente(congelado);
						System.out.println(this.getPkCombatting().getNombrePokemon() + " fue congelado por "
								+ nbTurnosEstado + " turnos");
					}
				}
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Puño trueno (tested)
		case 9:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Puño trueno");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			// Possibility of paralyzing Pokemon facing if is not already paralyzed and has
			// not a Estado
			if (this.getPkFacing().getEstadoPersistente().getEstadoEnum() == EstadosEnum.SIN_ESTADO
					&& !(this.getPkFacing().getEstadoPersistente().getEstadoEnum() == EstadosEnum.PARALIZADO)) {
				porcientoCaerEnEstado = (int) (Math.random() * 100);
				System.out.println("proba de paralizar : " + porcientoCaerEnEstado);
				if (porcientoCaerEnEstado <= 10) {
					nbTurnosEstado = (int) ((Math.random() * (5 - 2)) + 2);
					Estado paralizado = new Estado(EstadosEnum.PARALIZADO, nbTurnosEstado);
					this.getPkFacing().setEstadoPersistente(paralizado);
					System.out.println(this.getPkFacing().getNombrePokemon() + " fue paralizado por " + nbTurnosEstado
							+ " turnos");
				}
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Arañazo (tested)
		case 10:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Arañazo");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Agarre (tested)
		case 11:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Agarre");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Guillotina
		case 12:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Guillotina");
			break;

		// Viento cortante
		case 13:
			if (!this.getPkCombatting().isChargingAttackForNextRound()) {
				// This attack requires to charge first time for one round
				System.out.println(this.getPkCombatting().getNombrePokemon() + " se prepara para Viento cortante");
				this.getPkCombatting().setChargingAttackForNextRound(true);
			} else {
				System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Viento cortante");
				dmg = doDammage();
				highProbabilityCritic = (int) (Math.random() * 100);

				// 10/100 of probabilities to have a critic attack
				if (highProbabilityCritic <= 40) {
					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
				}
				// Pokemon is no more charging an attack
				this.getPkCombatting().setChargingAttackForNextRound(false);
				this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Corte (tested)
		case 15:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Corte");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Tornado (tested)
		case 16:
			// Gets the power from the beginning (to avoid variations after attacking)
			setBaseDmgFromBegining = this.getPkCombatting().getNextMouvement().getPoder();
			this.getPkCombatting().getNextMouvement()
					.setPoder(this.getPkCombatting().getNextMouvement().getPoder() * 2);
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Tornado");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}

			// Puts again the same power base as the beginning
			this.getPkCombatting().getNextMouvement().setPoder(setBaseDmgFromBegining);
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Ataque ala (tested)
		case 17:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Ataque ala");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Vuelo (continue to verify)
		case 19:
			if (!this.getPkCombatting().isChargingAttackForNextRound()) {
				// This attack requires to charge first time for one round
				// POSAR frase
				System.out.println(this.getPkCombatting().getNombrePokemon() + " voló muy alto");
				this.getPkCombatting().setChargingAttackForNextRound(true);

				if (!alreadyCheckedAttack) {
					this.getPkFacing().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
					// Pokemon facing AVOIDED the attack
					System.out.println(this.getPkFacing().getNombrePokemon() + " usó "
							+ this.getPkFacing().getNextMouvement().getNombreAta());
					System.out.println(this.getPkCombatting().getNombrePokemon()
							+ " evitó el ataque (vuelo - Pokemon atacante falló después del check)");
				}

			} else {
				if (!(this.getPkFacing().getNextMouvement().getIdAta() == 19
						&& this.getPkFacing().isChargingAttackForNextRound())) {
					System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Vuelo");
					dmg = doDammage();
					isCritic = getCriticity();
					if (isCritic) {
						dmg = dmg * 2;
						System.out.println("Fue un golpe crítico");
					}
					// Pokemon is no more charging an attack
					this.getPkCombatting().setChargingAttackForNextRound(false);
					this.getPkCombatting().getNextMouvement()
							.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
				} else {
					// AVOID attack cause Pokemon facing is flying
					System.out.println(this.getPkFacing().getNombrePokemon() + " evitó el ataque (normal)");
					// Pokemon is no more charging an attack
					this.getPkCombatting().setChargingAttackForNextRound(false);
					this.getPkCombatting().getNextMouvement()
							.setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
				}
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Atadura
		case 20:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Atadura");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			// Check if the Pokemon facing doesn't have the estado Atrapado
			if ((this.getPkFacing().getEstadosEfimeros().stream().filter(e -> e.getEstadoEnum() == EstadosEnum.ATRAPADO)
					.findAny().get()) != null) {
				nbTurnosEstado = (int) ((Math.random() * (5 - 4)) + 4);
				System.out.println(this.getPkCombatting().getNombrePokemon() + " quedó atrapado");
				Estado atrapdo = new Estado(EstadosEnum.ATRAPADO, nbTurnosEstado);
				this.getPkFacing().addEstadoEfimero(atrapdo);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Atizar (tested)
		case 21:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Atizar");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Látigo cepa (tested)
		case 22:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Látigo cepa");
			dmg = doDammage();
			isCritic = getCriticity();
			if (isCritic) {
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			if (this.getPkFacing().getPs() <= 0) {
				this.getPkFacing().setEstadoPersistente(new Estado(EstadosEnum.DEBILITADO));
			}
			break;

		// Pisotón
		case 23:
			System.out.println(this.getPkCombatting().getNombrePokemon() + " usó Pisotón");
			break;
		}

		return dmg;
	}

	// Damage
	public float doDammage() {
		int randomVariacion;
		randomVariacion = (int) ((Math.random() * (100 - 85)) + 85);
		boolean isAtaEspecial = this.getPkCombatting().getNextMouvement().getBases().contains("especial");
		float dmg = 0;

		if (isAtaEspecial) {
			// Apply special damage
			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonificacion()
					* this.getPkCombatting().getNextMouvement().getEfectividadContraPkAdversario() * randomVariacion
					* (((0.2f * 100 + 1) * this.getPkCombatting().getAtEsp()
							* this.getPkCombatting().getNextMouvement().getPoder())
							/ (25 * this.getPkFacing().getDefEsp()) + 2);
			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getNombrePokemon() + ") : " + dmg);
//			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			// Apply normal damage
		} else {
			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonificacion()
					* this.getPkCombatting().getNextMouvement().getEfectividadContraPkAdversario() * randomVariacion
					* (((0.2f * 100 + 1) * this.getPkCombatting().getAta()
							* this.getPkCombatting().getNextMouvement().getPoder()) / (25 * this.getPkFacing().getDef())
							+ 2);
			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getNombrePokemon() + ") : " + dmg);
//			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
		}

		return dmg;
	}

	public void doEffectOther() {

	}

}
