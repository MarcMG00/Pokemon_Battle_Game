package pokemon.model;

import pokemon.enums.StatusConditions;

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

	// Knows the evasion or accuracy for the pokemon selected (1 is for accuracy, 2 is for evasion)
	public float getEvasionOrAccuracy(Pokemon pk, int t) {

		int evAcu = 0;
		float resultEvAcu = 1;

		switch (t) {
		case 1:
			evAcu = pk.getPrecisionPoints();
			break;
		case 2:
			evAcu = pk.getEvasionPoints();
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
//					System.out.println(this.getPkFacing().getNombrePokemon() + " evit� el ataque");
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
//						System.out.println(this.getPkFacing().getNombrePokemon() + " evit� el ataque");
//					}
//				}
//			} else {
//				// Reduces the PP of the movement
//				this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
//				// Pokemon facing avoided the attack (because pk facing is flying)
//				System.out.println(this.getPkFacing().getNombrePokemon() + " evit� el ataque");
//			}
//		}
//	}

	// Sets if the attack will be accurate or not
	public void getProbabilityOfAttackingAndAttack(boolean useAttackChargedNow) {

		// Apply the attack charged
		if (useAttackChargedNow && !this.getPkCombatting().getAlreadyUsedFly()) {
			
			// gets accuracy/evasion
			float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100) * (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
			
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
					this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
					
					// Pokemon facing AVOIDED the attack
					System.out.println(this.getPkFacing().getName() + " evitó el ataque (vuelo - Pokemon atacante falló 1)");
					
				}
			}
			
			// Cannot attack first for next run (has to wait after the Pokemon facing attacks)
			this.getPkCombatting().setAlreadyUsedFly(true);
			
			this.getPkCombatting().setIsChargingAttackForNextRound(false);
			
		} else if (!useAttackChargedNow && this.getPkCombatting().getAlreadyUsedFly()) {
			
			// gets accuracy/evasion
			float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100) * (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
			
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
					this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
					
					// Pokemon facing AVOIDED the attack
					System.out.println(this.getPkFacing().getName() + " evitó el ataque (vuelo - Pokemon atacante falló 2)");
					
				}
			}
			
			// Cannot attack first for next run (has to wait after the Pokemon facing attacks)
			this.getPkCombatting().setAlreadyUsedFly(false);
			
			this.getPkCombatting().setIsChargingAttackForNextRound(true);
			
		} else {
			
			// Verify if a Pokemon can attack if Pokemon facing is doing strange attacks
			if ((this.getPkFacing().getNextMouvement().getId() == 19
					&& this.getPkFacing().getIsChargingAttackForNextRound())
					&& (this.getPkCombatting().getNextMouvement().getId() == 16
							|| this.getPkCombatting().getNextMouvement().getId() == 18
							|| this.getPkCombatting().getNextMouvement().getId() == 87
							|| this.getPkCombatting().getNextMouvement().getId() == 239
							|| this.getPkCombatting().getNextMouvement().getId() == 327
							|| this.getPkCombatting().getNextMouvement().getId() == 479
							|| this.getPkCombatting().getNextMouvement().getId() == 542)) {

				// gets accuracy/evasion
				float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100) * (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
				
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
						this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
						
						// Pokemon facing AVOIDED the attack
						System.out.println(this.getPkCombatting().getName() + " usó " + this.getPkCombatting().getNextMouvement().getName() + " (verificación 1)");
						
						System.out.println(this.getPkFacing().getName() + " evitó el ataque (vuelo - con movimiento que lo counteree)");
						
					}
				}
			}
			
			// Pk facing avoid the attack cause is not using an attack that can hurt while
			// Pokemon facing is flying
			else if ((this.getPkFacing().getNextMouvement().getId() == 19
					&& this.getPkFacing().getIsChargingAttackForNextRound())
					&& !(this.getPkCombatting().getNextMouvement().getId() == 16
							|| this.getPkCombatting().getNextMouvement().getId() == 18
							|| this.getPkCombatting().getNextMouvement().getId() == 87
							|| this.getPkCombatting().getNextMouvement().getId() == 239
							|| this.getPkCombatting().getNextMouvement().getId() == 327
							|| this.getPkCombatting().getNextMouvement().getId() == 479
							|| this.getPkCombatting().getNextMouvement().getId() == 542)
					&& this.getPkCombatting().getNextMouvement().getId() != 19) {
				
				// Reduces the PP of the movement
				this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
				
				// Pokemon facing avoided the attack (because pk facing is flying)
				System.out.println(this.getPkCombatting().getName() + " usó " + this.getPkCombatting().getNextMouvement().getName() + " (verificación 2)");
				
				System.out.println(this.getPkFacing().getName() + " evitó el ataque (vuelo - sin movimiento que lo counteree)");
				
			}
			
			// Prepare attack for flying
			else if (this.getPkCombatting().getNextMouvement().getId() == 19 && !this.getPkCombatting().getIsChargingAttackForNextRound()) {
				
				getAttackEffect(true);
				
			}
			
			// Normally enters to attack when Pokemon combatting is flying
			else if (this.getPkCombatting().getNextMouvement().getId() == 19 && this.getPkCombatting().getIsChargingAttackForNextRound()) {
				
				// gets accuracy/evasion
				float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100) * (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));

				// Get the attack effect (cause 100% is the max)
				if (a >= 1) {
					
					getAttackEffect(true);
					
				}
				
				// See the accuracy of the attack
				else {
					
					int randomEfectivity = 0;
					randomEfectivity = (int) (Math.random() * 100);

					if (randomEfectivity / 100 <= a) {
						
						// Get the attack effect
						this.getAttackEffect(true);
						
					} else {
						
						// Reduces the PP of the movement
						this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
						
						// Pokemon facing AVOIDED the attack
						System.out.println(this.getPkFacing().getName() + " evit� el ataque");
						
					}
				}
			}
			
			// Do attacks if Pokemon facing is not doing strange attacks that Pokemon combating cannot attack
			else if (!(this.getPkFacing().getNextMouvement().getId() == 19 && this.getPkFacing().getIsChargingAttackForNextRound())) {
				
				// gets accuracy/evasion
				float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100) * (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));

				// Get the attack effect (cause 100% is the max)
				if (a >= 1) {
					
					getAttackEffect(true);
					
				}
				
				// See the accuracy of the attack
				else {
					
					int randomEfectivity = 0;
					
					randomEfectivity = (int) (Math.random() * 100);

					if (randomEfectivity / 100 <= a) {
						
						// Get the attack effect
						this.getAttackEffect(true);
						
					} else {
						
						// Reduces the PP of the movement
						this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
						
						// Pokemon facing AVOIDED the attack
						System.out.println(this.getPkFacing().getName() + " evitó el ataque");
						
					}
				}
			} else {
				
				// gets accuracy/evasion
				float a = (this.getPkCombatting().getNextMouvement().getPrecision() / 100) * (getEvasionOrAccuracy(pkCombatting, 1) / getEvasionOrAccuracy(pkFacing, 2));
				
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
						this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
						
						// Pokemon facing AVOIDED the attack
						System.out.println(this.getPkFacing().getName() + " evitó el ataque");
						
					}
				}
			}
		}
	}

	// Gets if an attack is critic (x2 of damage)
	public boolean getCriticity() {
		boolean isCritic = false;

		int randomCritic = (int) (Math.random() * 100);

		// 10/100 of probabilities to have a critic attack
		if (randomCritic <= 10) {
			
			isCritic = true;
			
		}
		
		return isCritic;
	}

	// Gets the attack effect and apply damage
	public float getAttackEffect(boolean alreadyCheckedAttack) {

		float dmg = 0;
		boolean isCritic;
		int nbTimesAttack;
		int nbTurnsHoldingStatus;
		int probabilityGettingStatus;
		int highProbabilityCritic;
		int setBaseDmgFromBegining;

		switch (this.getPkCombatting().getNextMouvement().getId()) {
		// Destructor (tested)
		case 1:
			System.out.println(this.getPkCombatting().getName() + " usó Destructor");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Golpe kárate (tested)
		case 2:
			System.out.println(this.getPkCombatting().getName() + " usó Golpe kárate");
			
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
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Doble bofetón (tested)
		case 3:
			System.out.println(this.getPkCombatting().getName() + " usó Doble bofetón");
			
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
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Puño cometa (tested)
		case 4:
			System.out.println(this.getPkCombatting().getName() + " usó Puño cometa");
			
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
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Megapuño (tested)
		case 5:
			System.out.println(this.getPkCombatting().getName() + " usó Megapuño");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Día de pago (tested)
		case 6:
			System.out.println(this.getPkCombatting().getName() + " usó Día de pago");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Puño fuego (tested)
		case 7:
			System.out.println(this.getPkCombatting().getName() + " usó Puño fuego");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			
			probabilityGettingStatus = (int) (Math.random() * 100);
			
			System.out.println("proba de quemar : " + probabilityGettingStatus);
			
			if (probabilityGettingStatus <= 10) {
				
				// Check if the Pokemon facing isn't without any status
				if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {
					
					nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);
					
					State burned = new State(StatusConditions.BURNED, nbTurnsHoldingStatus);
					
					this.getPkFacing().setStatusCondition(burned);
					
					System.out.println(this.getPkFacing().getName() + " fue quemado por " + nbTurnsHoldingStatus + " turnos");
					
				}
			}
			
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Puño hielo
		case 8:
			System.out.println(this.getPkCombatting().getName() + " usó Puño hielo");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}

			// Ice Pokemon cannot be frozen
			if (this.pkFacing.getTypes().stream().filter(t -> t.getId() == 9).findAny().get() == null) {
				
				probabilityGettingStatus = (int) (Math.random() * 100);
				
				System.out.println("proba de congelar : " + probabilityGettingStatus);
				
				// 10% of probabilities to be frozen
				if (probabilityGettingStatus <= 10) {
					
					if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS) {
						
						nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);
						
						State frozen = new State(StatusConditions.FROZEN, nbTurnsHoldingStatus);
						
						this.getPkFacing().setStatusCondition(frozen);
						
						System.out.println(this.getPkCombatting().getName() + " fue congelado por " + nbTurnsHoldingStatus + " turnos");
					}
				}
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Puño trueno (tested)
		case 9:
			System.out.println(this.getPkCombatting().getName() + " usó Puño trueno");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}

			// Possibility of paralyzing Pokemon facing if is not already paralyzed and has not a Status
			if (this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.NO_STATUS
					&& !(this.getPkFacing().getStatusCondition().getStatusCondition() == StatusConditions.PARALYZED)) {
				
				probabilityGettingStatus = (int) (Math.random() * 100);
				
				System.out.println("proba de paralizar : " + probabilityGettingStatus);
				
				if (probabilityGettingStatus <= 10) {
					
					nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 2)) + 2);
					
					State paralyzed = new State(StatusConditions.PARALYZED, nbTurnsHoldingStatus);
					
					this.getPkFacing().setStatusCondition(paralyzed);
					
					System.out.println(this.getPkFacing().getName() + " fue paralizado por " + nbTurnsHoldingStatus + " turnos");
					
				}
			}

			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Arañazo (tested)
		case 10:
			System.out.println(this.getPkCombatting().getName() + " usó Arañazo");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Agarre (tested)
		case 11:
			System.out.println(this.getPkCombatting().getName() + " usó Agarre");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Guillotina
		case 12:
			System.out.println(this.getPkCombatting().getName() + " usó Guillotina");
			break;

		// Viento cortante
		case 13:
			if (!this.getPkCombatting().getIsChargingAttackForNextRound()) {
				
				// This attack requires to charge first time for one round
				System.out.println(this.getPkCombatting().getName() + " se prepara para Viento cortante");
				
				this.getPkCombatting().setIsChargingAttackForNextRound(true);
				
			} else {
				
				System.out.println(this.getPkCombatting().getName() + " usó Viento cortante");
				
				dmg = doDammage();
				
				highProbabilityCritic = (int) (Math.random() * 100);

				// 10/100 of probabilities to have a critic attack
				if (highProbabilityCritic <= 40) {
					
					dmg = dmg * 2;
					System.out.println("Fue un golpe crítico");
					
				}
				
				// Pokemon is no more charging an attack
				this.getPkCombatting().setIsChargingAttackForNextRound(false);
				
				this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Corte (tested)
		case 15:
			System.out.println(this.getPkCombatting().getName() + " usó Corte");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Tornado (tested)
		case 16:
			// Gets the power from the beginning (to avoid variations after attacking)
			setBaseDmgFromBegining = this.getPkCombatting().getNextMouvement().getPower();
			
			this.getPkCombatting().getNextMouvement().setPower(this.getPkCombatting().getNextMouvement().getPower() * 2);
			
			System.out.println(this.getPkCombatting().getName() + " usó Tornado");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}

			// Puts again the same power base as the beginning
			this.getPkCombatting().getNextMouvement().setPower(setBaseDmgFromBegining);
			
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Ataque ala (tested)
		case 17:
			System.out.println(this.getPkCombatting().getName() + " usó Ataque ala");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Vuelo
		case 19:
			if (!this.getPkCombatting().getIsChargingAttackForNextRound()) {
				
				// This attack requires to charge first time for one round
				System.out.println(this.getPkCombatting().getName() + " voló muy alto");
				
				this.getPkCombatting().setIsChargingAttackForNextRound(true);

				if (this.getPkFacing().getNextMouvement().getId() == 19 && !this.getPkFacing().getIsChargingAttackForNextRound()) {
					
					this.getPkCombatting().setIsFirstInUsingTheSameAttack(true);
				}

				// Reduces de PP of the pokemon facing if he is not using vuelo
				if (!alreadyCheckedAttack && this.getPkFacing().getNextMouvement().getId() != 19) {
					
					this.getPkFacing().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
					
					// Pokemon facing AVOIDED the attack
					System.out.println(this.getPkFacing().getName() + " usó " + this.getPkFacing().getNextMouvement().getName() + " (verificación 1)");
					
					System.out.println(this.getPkCombatting().getName() + " evitó el ataque (vuelo - Pokemon atacante falló despuós del check)");
					
				}

			} else {
				
				if (!(this.getPkFacing().getNextMouvement().getId() == 19 && this.getPkFacing().getIsChargingAttackForNextRound())) {
					
					if (this.getPkFacing().getNextMouvement().getId() == 19 && this.getPkFacing().getIsChargingAttackForNextRound()) {
						
						System.out.println(this.getPkFacing().getName() + " usó " + this.getPkFacing().getNextMouvement().getName() + "FF");
						
						System.out.println(this.getPkCombatting().getName() + " evitó el ataque (normal 5)");
						
						this.getPkFacing().getNextMouvement().setPp(this.getPkFacing().getNextMouvement().getPp() - 1);

						System.out.println(this.getPkCombatting().getName() + " usó Vuelo 2.1");
						
					} else {
						
						System.out.println(this.getPkCombatting().getName() + " usó Vuelo 2.2");
						
						if(!this.getPkFacing().getIsFirstInUsingTheSameAttack()) {
							
							this.getPkCombatting().setIsFirstInUsingTheSameAttack(!this.getPkCombatting().getIsFirstInUsingTheSameAttack());
							
						}
					}
					
					dmg = doDammage();
					
					isCritic = getCriticity();
					
					if (isCritic) {
						
						dmg = dmg * 2;
						System.out.println("Fue un golpe crítico");
						
					}
					
					// Pokemon is no more charging an attack
					this.getPkCombatting().setIsChargingAttackForNextRound(false);
					
					this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
					
				} else {
					
					// AVOID attack cause Pokemon facing is flying
					if (this.getPkCombatting().getIsChargingAttackForNextRound() && !this.getPkFacing().getIsChargingAttackForNextRound()) {
						
						System.out.println(this.getPkFacing().getName() + " evitó el ataque (normal)");
						
					} else if (this.getPkFacing().getNextMouvement().getId() == 19
							&& this.getPkFacing().getIsChargingAttackForNextRound()
							&& this.getPkCombatting().getIsFirstInUsingTheSameAttack()) {
						
						System.out.println(this.getPkCombatting().getName() + " usó vuelo 4");
						
						System.out.println(this.getPkFacing().getName() + " evitó el ataque (normal 4)");
						
					} else {
						
						System.out.println(this.getPkCombatting().getName() + " usó vuelo 3");
						
						System.out.println(this.getPkFacing().getName() + " evitó el ataque (normal 3)");
						
					}

					// Pokemon is no more charging an attack
					this.getPkCombatting().setIsChargingAttackForNextRound(false);
					
					this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);
					
				}
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Atadura
		case 20:
			System.out.println(this.getPkCombatting().getName() + " usó Atadura");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			// Check if the Pokemon facing doesn't have the estado Atrapado
			if ((this.getPkFacing().getEphemeralStates().stream().filter(e -> e.getStatusCondition() == StatusConditions.TRAPPED).findAny().get()) != null) {
				
				nbTurnsHoldingStatus = (int) ((Math.random() * (5 - 4)) + 4);
				
				System.out.println(this.getPkCombatting().getName() + " quedó atrapado");
				
				State trapped = new State(StatusConditions.TRAPPED, nbTurnsHoldingStatus);
				
				this.getPkFacing().addEstadoEfimero(trapped);
				
			}

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Atizar (tested)
		case 21:
			System.out.println(this.getPkCombatting().getName() + " usó Atizar");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Látigo cepa (tested)
		case 22:
			System.out.println(this.getPkCombatting().getName() + " usó Látigo cepa");
			
			dmg = doDammage();
			
			isCritic = getCriticity();
			
			if (isCritic) {
				
				dmg = dmg * 2;
				System.out.println("Fue un golpe crítico");
				
			}
			
			this.getPkCombatting().getNextMouvement().setPp(this.getPkCombatting().getNextMouvement().getPp() - 1);

			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
			if (this.getPkFacing().getPs() <= 0) {
				
				this.getPkFacing().setStatusCondition(new State(StatusConditions.DEBILITATED));
				
			}
			break;

		// Pisotón
		case 23:
			System.out.println(this.getPkCombatting().getName() + " usó Pisotón");
			break;
		}

		return dmg;
	}

	// Apply damage
	public float doDammage() {
		
		// There is a random variation when attacking (the total damage is not the same every time)
		int randomVariation;
		
		randomVariation = (int) ((Math.random() * (100 - 85)) + 85);
		
		boolean isSpecialAttack = this.getPkCombatting().getNextMouvement().getBases().contains("especial");
		
		float dmg = 0;

		if (isSpecialAttack) {
			
			// Apply special damage
			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonus()
					* this.getPkCombatting().getNextMouvement().getEffectivenessAgainstPkFacing() * randomVariation
					* (((0.2f * 100 + 1) * this.getPkCombatting().getSpecialAttack()
							* this.getPkCombatting().getNextMouvement().getPower())
							/ (25 * this.getPkFacing().getSpecialDefense()) + 2);
			
			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + ") : " + dmg);
//			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
			
		// Apply normal damage
		} else {
			
			dmg = 0.01f * this.getPkCombatting().getNextMouvement().getBonus()
					* this.getPkCombatting().getNextMouvement().getEffectivenessAgainstPkFacing() * randomVariation
					* (((0.2f * 100 + 1) * this.getPkCombatting().getAttack()
							* this.getPkCombatting().getNextMouvement().getPower()) / (25 * this.getPkFacing().getDef())
							+ 2);
			
			System.out.println("Damage to Pokemon facing (" + this.getPkFacing().getName() + ") : " + dmg);
//			this.getPkFacing().setPs(this.getPkFacing().getPs() - dmg);
		}

		return dmg;
	}

	public void doEffectOther() {

	}

}
