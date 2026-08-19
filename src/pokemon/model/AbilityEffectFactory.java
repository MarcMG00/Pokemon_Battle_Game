package pokemon.model;

import pokemon.abilityInterface.AbilityEffect;
import pokemon.abilityInterface.AftermathAbility;
import pokemon.abilityInterface.AirLockAbility;
import pokemon.abilityInterface.AngerPointAbility;
import pokemon.abilityInterface.AnticipationAbility;
import pokemon.abilityInterface.CloudNineAbility;
import pokemon.abilityInterface.ColorChangeAbility;
import pokemon.abilityInterface.CursedBodyAbility;
import pokemon.abilityInterface.CuteCharmAbility;
import pokemon.abilityInterface.DownloadAbility;
import pokemon.abilityInterface.DrizzleAbility;
import pokemon.abilityInterface.DroughtAbility;
import pokemon.abilityInterface.DrySkinAbility;
import pokemon.abilityInterface.EffectSporeAbility;
import pokemon.abilityInterface.EmptyAbility;
import pokemon.abilityInterface.FlameBodyAbility;
import pokemon.abilityInterface.FlashFireAbility;
import pokemon.abilityInterface.ForecastAbility;
import pokemon.abilityInterface.HeavyMetalAbility;
import pokemon.abilityInterface.HydratationAbility;
import pokemon.abilityInterface.IceBodyAbility;
import pokemon.abilityInterface.IntimidateAbility;
import pokemon.abilityInterface.LevitateAbility;
import pokemon.abilityInterface.LightMetalAbility;
import pokemon.abilityInterface.LightningRodAbility;
import pokemon.abilityInterface.MinusAbility;
import pokemon.abilityInterface.MoodyAbility;
import pokemon.abilityInterface.MotorDriveAbility;
import pokemon.abilityInterface.NaturalCureAbility;
import pokemon.abilityInterface.NormalizeAbility;
import pokemon.abilityInterface.PlusAbility;
import pokemon.abilityInterface.PoisonPointAbility;
import pokemon.abilityInterface.PressureAbility;
import pokemon.abilityInterface.QuickFeetAbility;
import pokemon.abilityInterface.RainDishAbility;
import pokemon.abilityInterface.RoughSkinAbility;
import pokemon.abilityInterface.SandStreamAbility;
import pokemon.abilityInterface.ShedSkinAbility;
import pokemon.abilityInterface.SnowWarningAbility;
import pokemon.abilityInterface.SpeedBoostAbility;
import pokemon.abilityInterface.StaticAbility;
import pokemon.abilityInterface.SteadfastAbility;
import pokemon.abilityInterface.StenchAbility;
import pokemon.abilityInterface.StormDrainAbility;
import pokemon.abilityInterface.SynchronizeAbility;
import pokemon.abilityInterface.TraceAbility;
import pokemon.abilityInterface.VoltAbsorbAbility;
import pokemon.abilityInterface.WaterAbsorbAbility;
import pokemon.abilityInterface.WeakArmorAbility;
import pokemon.abilityInterface.WonderGuardAbility;

public class AbilityEffectFactory {
	// -----------------------------
	// Set the ability effect of the attack
	// TODO >> 006 / 008 / 012 / 43 (during attacks ?) / 53 (when applying objects)
	// / 60 (when applying objects) / 82 (when applying objects) / 81 (to complete)
	// / 83 (to complete) / 84 (when applying
	// objects) / 90 (to complete) / 98 (to complete) / 99 (to complete) / 100 (to
	// complete) / 103 (when applying objects)/ 104 (when having more abilities) /
	// 108 (when all attacks will be programmed) / 112 (when having more attacks) /
	// 119 (when applying objects) / 121 (when applying objects) / 124 (when
	// applying objects) / 125 (to complete) / 139 (when having objects) / 140 (no
	// dual combat)
	// -----------------------------
	public static AbilityEffect createEffect(Ability ability, Pokemon owner) {
		switch (ability.getId()) {
		// Hedor/Stench
		case 1:
			return new StenchAbility(owner);
		// Llovizna/Drizzle
		case 2:
			return new DrizzleAbility(owner);
		// Impulso/Speed boost
		case 3:
			return new SpeedBoostAbility(owner);
		// Electricidad estática/Static
		case 9:
			return new StaticAbility(owner);
		// Absorbe electricidad/Volt absorb
		case 10:
			return new VoltAbsorbAbility(owner);
		// Absorbe agua/Water absorb
		case 11:
			return new WaterAbsorbAbility(owner);
		// Aclimatación/Cloud nine
		case 13:
			return new CloudNineAbility(owner);
		// Cambio color/Color change
		case 16:
			return new ColorChangeAbility(owner);
		// Absorbe fuego/Flash fire
		case 18:
			return new FlashFireAbility(owner);
		// Intimidación/Intimidate
		case 22:
			return new IntimidateAbility(owner);
		// Piel tosca/Rough skin
		case 24:
			return new RoughSkinAbility(owner);
		// Superguarda/Wonder guard
		case 25:
			return new WonderGuardAbility(owner);
		// Levitación/Levitate
		case 26:
			return new LevitateAbility(owner);
		// Efecto espora/Effect spore
		case 27:
			return new EffectSporeAbility(owner);
		// Sincronía/Synchronize
		case 28:
			return new SynchronizeAbility(owner);
		// Cura natural/Natural cure
		case 30:
			return new NaturalCureAbility(owner);
		// Pararrayos/Lightning rod
		case 31:
			return new LightningRodAbility(owner);
		// Calco/Trace
		case 36:
			return new TraceAbility(owner);
		// Punto tóxico/Poison point
		case 38:
			return new PoisonPointAbility(owner);
		// Cura lluvia/Rain dish
		case 44:
			return new RainDishAbility(owner);
		// Chorro arena/Sand stream
		case 45:
			return new SandStreamAbility(owner);
		// Presión/Pressure
		case 46:
			return new PressureAbility(owner);
		// Cuerpo llama/Flame body
		case 49:
			return new FlameBodyAbility(owner);
		// Gran encanto/Cute charm
		case 56:
			return new CuteCharmAbility(owner);
		// Más/Plus
		case 57:
			return new PlusAbility(owner);
		// Menos/Minus
		case 58:
			return new MinusAbility(owner);
		// Predicción/Forecast
		case 59:
			return new ForecastAbility(owner);
		// Mudar/Shed skin
		case 61:
			return new ShedSkinAbility(owner);
		// Sequía/Drought
		case 70:
			return new DroughtAbility(owner);
		// Esclusa de aire/Air lock
		case 76:
			return new AirLockAbility(owner);
		// Electromotor/Motor drive
		case 78:
			return new MotorDriveAbility(owner);
		// Impasible/Steadfast
		case 80:
			return new SteadfastAbility(owner);
		// Irascible/Anger point
		case 83:
			return new AngerPointAbility(owner);
		// Piel seca/Dry skin
		case 87:
			return new DrySkinAbility(owner);
		// Descarga/Download
		case 88:
			return new DownloadAbility(owner);
		// Hidratación/Hydratation
		case 93:
			return new HydratationAbility(owner);
		// Pies rápidos/Quick feet
		case 95:
			return new QuickFeetAbility(owner);
		// Normalidad/Normalize
		case 96:
			return new NormalizeAbility(owner);
		// Detonación/Aftermath
		case 106:
			return new AftermathAbility(owner);
		// Anticipación/Anticipation
		case 107:
			return new AnticipationAbility(owner);
		// Colector/Storm drain
		case 114:
			return new StormDrainAbility(owner);
		// Gélido/Ice body
		case 115:
			return new IceBodyAbility(owner);
		// Nevada/Snow warning
		case 117:
			return new SnowWarningAbility(owner);
		// Cuerpo maldito/Cursed body
		case 130:
			return new CursedBodyAbility(owner);
		// Armadura frágil/Weak armor
		case 133:
			return new WeakArmorAbility(owner);
		// Metal pesado/Heavy metal
		case 134:
			return new HeavyMetalAbility(owner);
		// Metal liviano/Light metal
		case 135:
			return new LightMetalAbility(owner);
		// Veleta/Moody
		case 141:
			return new MoodyAbility(owner);
		default:
			return new EmptyAbility(owner);
		}
	}
}
