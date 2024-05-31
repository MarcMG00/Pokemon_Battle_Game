package pokemon.model;

import java.util.ArrayList;

import pokemon.enums.EstadosEnum;

public class Pokemon {
	private int idPokemon;
	private String nombrePokemon;
	private float initialPs;
	private float initialAta;
	private float initialDef;
	private float initialVel;
	private float initialAtEsp;
	private float initialDefEsp;
	private float ps;
	private float ata;
	private float def;
	private float vel;
	private float atEsp;
	private float defEsp;
	private ArrayList<Habilidad> normalHabs;
	private ArrayList<Habilidad> ocultedHabs;
	private ArrayList<PokemonType> types;
	private ArrayList<Ataque> ataFisicos;
	private ArrayList<Ataque> ataEspeciales;
	private ArrayList<Ataque> ataEstado;
	private ArrayList<Ataque> cuatroAtaques;
	private Ataque nextMouvement;
	private ArrayList<Ataque> ataquesRebientan;
	private ArrayList<Ataque> ataquesNormales;
	private ArrayList<Ataque> ataquesDebiles;
	private ArrayList<Ataque> ataquesNoAfectan;
	private ArrayList<Integer> cuatroAtaquesIds;
	private int puntosPrecision;
	private int puntosEvasion;
	private Estado estadoPersistente;
	private ArrayList<Estado> estadosEfimeros;
	public boolean isChargingAttackForNextRound;
	public boolean isPuedeAtacar;
	public boolean alreadyUsedVuelo;

	public Pokemon() {
		this.idPokemon = 0;
		this.nombrePokemon = "";
		this.ps = 0;
		this.ata = 0;
		this.def = 0;
		this.vel = 0;
		this.atEsp = 0;
		this.defEsp = 0;
		this.initialPs = 0;
		this.initialAta = 0;
		this.initialDef = 0;
		this.initialVel = 0;
		this.initialAtEsp = 0;
		this.initialDefEsp = 0;
		this.normalHabs = new ArrayList<>();
		this.ocultedHabs = new ArrayList<>();
		this.types = new ArrayList<>();
		this.ataFisicos = new ArrayList<>();
		this.ataEspeciales = new ArrayList<>();
		this.ataEstado = new ArrayList<>();
		this.cuatroAtaques = new ArrayList<>();
		this.nextMouvement = new Ataque();
		this.ataquesRebientan = new ArrayList<>();
		this.ataquesNormales = new ArrayList<>();
		this.ataquesDebiles = new ArrayList<>();
		this.ataquesNoAfectan = new ArrayList<>();
		this.cuatroAtaquesIds = new ArrayList<>();
		this.puntosPrecision = 0;
		this.puntosEvasion = 0;
		this.estadoPersistente = new Estado();
		this.estadosEfimeros = new ArrayList<>();
		this.isChargingAttackForNextRound = false;
		this.isPuedeAtacar = true;
		this.alreadyUsedVuelo = false;
	}

	public Pokemon(int idPokemon, String nombrePokemon, float ps, float ata, float def, float vel, float atEsp,
			float defEsp) {
		this.idPokemon = idPokemon;
		this.nombrePokemon = nombrePokemon;
		this.ps = ps;
		this.ata = ata;
		this.def = def;
		this.vel = vel;
		this.atEsp = atEsp;
		this.defEsp = defEsp;
		this.initialPs = ps;
		this.initialAta = ata;
		this.initialDef = def;
		this.initialVel = vel;
		this.initialAtEsp = atEsp;
		this.initialDefEsp = defEsp;
		this.normalHabs = new ArrayList<>();
		this.ocultedHabs = new ArrayList<>();
		this.types = new ArrayList<>();
		this.ataFisicos = new ArrayList<>();
		this.ataEspeciales = new ArrayList<>();
		this.ataEstado = new ArrayList<>();
		this.cuatroAtaques = new ArrayList<>();
		this.nextMouvement = new Ataque();
		this.ataquesRebientan = new ArrayList<>();
		this.ataquesNormales = new ArrayList<>();
		this.ataquesDebiles = new ArrayList<>();
		this.ataquesNoAfectan = new ArrayList<>();
		this.cuatroAtaquesIds = new ArrayList<>();
		this.puntosPrecision = 0;
		this.puntosEvasion = 0;
		this.estadoPersistente = new Estado();
		this.estadosEfimeros = new ArrayList<>();
		this.isChargingAttackForNextRound = false;
		this.isPuedeAtacar = true;
		this.alreadyUsedVuelo = false;
	}

	public int getIdPokemon() {
		return idPokemon;
	}

	public void setIdPokemon(int idPokemon) {
		this.idPokemon = idPokemon;
	}

	public String getNombrePokemon() {
		return nombrePokemon;
	}

	public void setNombrePokemon(String nombrePokemon) {
		this.nombrePokemon = nombrePokemon;
	}

	public float getPs() {
		return ps;
	}

	public void setPs(float f) {
		this.ps = f;
	}

	public float getAta() {
		return ata;
	}

	public void setAta(float ata) {
		this.ata = ata;
	}

	public float getDef() {
		return def;
	}

	public void setDef(float def) {
		this.def = def;
	}

	public float getVel() {
		return vel;
	}

	public void setVel(float vel) {
		this.vel = vel;
	}

	public float getAtEsp() {
		return atEsp;
	}

	public void setAtEsp(float atEsp) {
		this.atEsp = atEsp;
	}

	public float getDefEsp() {
		return defEsp;
	}

	public void setDefEsp(float defEsp) {
		this.defEsp = defEsp;
	}

	public float getInitialPs() {
		return initialPs;
	}

	public void setInitialPs(float initialPs) {
		this.initialPs = initialPs;
	}

	public float getInitialAta() {
		return initialAta;
	}

	public void setInitialAta(float initialAta) {
		this.initialAta = initialAta;
	}

	public float getInitialDef() {
		return initialDef;
	}

	public void setInitialDef(float initialDef) {
		this.initialDef = initialDef;
	}

	public float getInitialVel() {
		return initialVel;
	}

	public void setInitialVel(float initialVel) {
		this.initialVel = initialVel;
	}

	public float getInitialAtEsp() {
		return initialAtEsp;
	}

	public void setInitialAtEsp(float initialAtEsp) {
		this.initialAtEsp = initialAtEsp;
	}

	public float getInitialDefEsp() {
		return initialDefEsp;
	}

	public void setInitialDefEsp(float initialDefEsp) {
		this.initialDefEsp = initialDefEsp;
	}

	public ArrayList<Habilidad> getNormalHabs() {
		return normalHabs;
	}

	public void setNormalHabs(ArrayList<Habilidad> normalHabs) {
		this.normalHabs = normalHabs;
	}

	public ArrayList<Habilidad> getOcultedHabs() {
		return ocultedHabs;
	}

	public void setOcultedHabs(ArrayList<Habilidad> ocultedHabs) {
		this.ocultedHabs = ocultedHabs;
	}

	public ArrayList<PokemonType> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PokemonType> types) {
		this.types = types;
	}

	public ArrayList<Ataque> getAtaFisicos() {
		return ataFisicos;
	}

	public void setAtaFisicos(ArrayList<Ataque> ataFisicos) {
		this.ataFisicos = ataFisicos;
	}

	public ArrayList<Ataque> getAtaEspeciales() {
		return ataEspeciales;
	}

	public void setAtaEspeciales(ArrayList<Ataque> ataEspeciales) {
		this.ataEspeciales = ataEspeciales;
	}

	public ArrayList<Ataque> getAtaEstado() {
		return ataEstado;
	}

	public void setAtaEstado(ArrayList<Ataque> ataEstado) {
		this.ataEstado = ataEstado;
	}

	public ArrayList<Ataque> getCuatroAtaques() {
		return cuatroAtaques;
	}

	public void setCuatroAtaques(ArrayList<Ataque> cuatroAtaques) {
		this.cuatroAtaques = cuatroAtaques;
	}

	public Ataque getNextMouvement() {
		return nextMouvement;
	}

	public void setNextMouvement(Ataque nextMouvement) {
		this.nextMouvement = nextMouvement;
	}

	public ArrayList<Ataque> getAtaquesRebientan() {
		return ataquesRebientan;
	}

	public void setAtaquesRebientan(ArrayList<Ataque> ataquesRebientan) {
		this.ataquesRebientan = ataquesRebientan;
	}

	public ArrayList<Ataque> getAtaquesNormales() {
		return ataquesNormales;
	}

	public void setAtaquesNormales(ArrayList<Ataque> ataquesNormales) {
		this.ataquesNormales = ataquesNormales;
	}

	public ArrayList<Ataque> getAtaquesDebiles() {
		return ataquesDebiles;
	}

	public void setAtaquesDebiles(ArrayList<Ataque> ataquesDebiles) {
		this.ataquesDebiles = ataquesDebiles;
	}

	public ArrayList<Ataque> getAtaquesNoAfectan() {
		return ataquesNoAfectan;
	}

	public void setAtaquesNoAfectan(ArrayList<Ataque> ataquesNoAfectan) {
		this.ataquesNoAfectan = ataquesNoAfectan;
	}

	public ArrayList<Integer> getCuatroAtaquesIds() {
		return cuatroAtaquesIds;
	}

	public void setCuatroAtaquesIds(ArrayList<Integer> cuatroAtaquesIds) {
		this.cuatroAtaquesIds = cuatroAtaquesIds;
	}

	public int getPuntosPrecision() {
		return puntosPrecision;
	}

	public void setPuntosPrecision(int puntosPrecision) {
		this.puntosPrecision = puntosPrecision;
	}

	public int getPuntosEvasion() {
		return puntosEvasion;
	}

	public void setPuntosEvasion(int puntosEvasion) {
		this.puntosEvasion = puntosEvasion;
	}

	public Estado getEstadoPersistente() {
		return estadoPersistente;
	}

	public void setEstadoPersistente(Estado estadoPersistente) {
		this.estadoPersistente = estadoPersistente;
	}

	public ArrayList<Estado> getEstadosEfimeros() {
		return estadosEfimeros;
	}

	public void setEstadosEfimeros(ArrayList<Estado> estadosEfimeros) {
		this.estadosEfimeros = estadosEfimeros;
	}

	public boolean isChargingAttackForNextRound() {
		return isChargingAttackForNextRound;
	}

	public void setChargingAttackForNextRound(boolean isChargingAttackForNextRound) {
		this.isChargingAttackForNextRound = isChargingAttackForNextRound;
	}

	public boolean isPuedeAtacar() {
		return isPuedeAtacar;
	}

	public void setPuedeAtacar(boolean isPuedeAtacar) {
		this.isPuedeAtacar = isPuedeAtacar;
	}
	
	public boolean isAlreadyUsedVuelo() {
		return alreadyUsedVuelo;
	}

	public void setAlreadyUsedVuelo(boolean alreadyUsedVuelo) {
		this.alreadyUsedVuelo = alreadyUsedVuelo;
	}

	// Adds habs to Pokemon
	public void addNormalHab(Habilidad nHab) {
		this.normalHabs.add(nHab);
	}

	// Adds oculted habs to Pokemon
	public void addOcultedHab(Habilidad oHab) {
		this.ocultedHabs.add(oHab);
	}

	// Adds types to Pokemon
	public void addType(PokemonType pt) {
		this.types.add(pt);
	}

	// Adds fisic attacs to Pokemon
	public void addAtaFisicos(Ataque ataF) {
		this.ataFisicos.add(ataF);
	}

	// Adds special attacs to Pokemon
	public void addAtaEspeciales(Ataque ataE) {
		this.ataEspeciales.add(ataE);
	}

	// Adds other attacs to Pokemon
	public void addAtaEstado(Ataque ataO) {
		this.ataEstado.add(ataO);
	}

	// Adds the four final attacs to Pokemon
	public void addAtaques(Ataque ata) {
		this.cuatroAtaques.add(ata);
	}

	// Adds the four final attacs Ids to Pokemon
	public void addAtaquesIds(Integer ataId) {
		this.cuatroAtaquesIds.add(ataId);
	}

	// Adds a stado efímero to Pokemon
	public void addEstadoEfimero(Estado estadoEf) {
		this.estadosEfimeros.add(estadoEf);
	}

	// Restart stats after some attacks... (cause not accumulated)
	public void restartParametersEffect() {

		switch (this.getEstadoPersistente().getEstadoEnum()) {
		case ATRAPADO:
			break;
		case CANTO_MORTAL:
			break;
		case CONFUSO:
			break;
		case CONGELADO:
			break;
		case DEBILITADO:
			break;
		case DORMIDO:
			break;
		case DRENADO:
			break;
		case ENAMORADO:
			break;
		case ENVENENADO:
			break;
		case GRAVEMENTE_ENVENENADO:
			break;
		case MALDITO:
			break;
		case PARALIZADO:
			this.setVel(this.getInitialVel());
			break;
		case QUEMADO:
			this.setAta(this.getInitialAta());
			break;
		case SIN_ESTADO:
			break;
		default:
			break;

		}
	}

	// Modifie stats for Estados
	public void checkEffectsEstadoPersistente(boolean isAttackingBurned) {
		// Do effect of the Estado
		this.getEstadoPersistente().doEffectEstadoPersistente(this.getEstadoPersistente().getEstadoEnum());
		float reducePs = 0;

		if (!(this.getEstadoPersistente().getEstadoEnum() == EstadosEnum.SIN_ESTADO)) {
			switch (this.getEstadoPersistente().getEstadoEnum()) {
			// Can move or not
			case CONGELADO:
				// Can attack the moment he is not frozen anymore
				if (this.getEstadoPersistente().isPuedeMoverseEstadoEfimero()) {
					System.out.println(this.getNombrePokemon() + " se descongeló y puede atacar");
					this.setPuedeAtacar(true);
				} else {
					this.setPuedeAtacar(false);
					System.out.println(this.getNombrePokemon() + " no se descongeló");
				}
				break;
			case DEBILITADO:
				break;
			case ENVENENADO:
				break;
			case GRAVEMENTE_ENVENENADO:
				break;
			// Modifies speed of Pokemon if can attack (reduces by 50%)
			case PARALIZADO:
				if (this.getEstadoPersistente().isPuedeMoverseEstadoPersistente()) {
					this.setVel((this.getVel() * 50) / 100);
					this.setPuedeAtacar(true);
					System.out.println(this.getNombrePokemon() + " pudo atacar paralizado");
				} else {
					this.setPuedeAtacar(false);
					System.out.println(this.getNombrePokemon() + " no pudo atacar paralizado");
				}
				break;
			// Reduces current PS by 6.25% and damage by 50%
			case QUEMADO:
				if (isAttackingBurned) {
					this.setAta(this.getAta() / 2);
					this.setPuedeAtacar(true);
				} else {
					reducePs = this.getPs() * 0.0625f;
					this.setPs(this.getPs() - reducePs);

					// Reset parameters (when a Pokemon is burned, at the end of the round, he will
					// lose PS, so we will pass directly through the method)
					if(this.getAta() != this.getInitialAta()) {
						this.restartParametersEffect();
					}
					
					this.setPuedeAtacar(true);
					System.out.println(this.getNombrePokemon() + " se resiente de la quemadura XD - PS actuales : "
							+ this.getPs());
				}
				break;
			default:
				break;
			}
		}
	}
}
