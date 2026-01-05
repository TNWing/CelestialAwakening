package com.github.celestial_awakening;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = CelestialAwakening.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER;

    static final ForgeConfigSpec.BooleanValue OUTOFCOMBAT_HEAL;

    static final ForgeConfigSpec.BooleanValue ENABLE_WIP_CONTENT;


    private static final ForgeConfigSpec.ConfigValue<Double> SUNSTONE_LEAVES_RATE;
    private static final ForgeConfigSpec.ConfigValue<Double> SUNSTONE_GRASS_RATE;
    static final ForgeConfigSpec.ConfigValue<List<? extends String>> TRANSCENDENTS_DIMENSIONS;
    static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_MULTIPLE_DIVINER;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DELAY;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_MIN_CD;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_MAX_CD;
    static final ForgeConfigSpec.ConfigValue<List<? extends String>> TRANSCENDENTS_ENEMIES;
    static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_DIVINER_HEATWAVE_ENABLED;
    static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS;
    static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_DIVINER_AOD_ENABLED;
    static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_DIVINER_AOD_COSMETIC_ONLY;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DIVINER_SCAN_POWER_INCREASE;

    static final ForgeConfigSpec.IntValue PROWLER_RAID_INTERVAL;
    static final ForgeConfigSpec.ConfigValue<String> PROWLER_DESTRUCTION;
    static final ForgeConfigSpec.IntValue SOLMANDER_DELAY;

    static final ForgeConfigSpec.IntValue CORE_GUARDIAN_COUNTER;

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LUNAR_MATERIAL_DIMENSIONS;
    private static final ForgeConfigSpec.ConfigValue<Integer> MOONSTONE_LIMIT;
    private static final ForgeConfigSpec.ConfigValue<Integer> MOONSTONE_INTERVAL;
    private static final ForgeConfigSpec.ConfigValue<Integer> MOONSTONE_CNT;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> PK_DIMENSIONS;
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_CRESCENCIA_MIN_DAY;
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_SPAWN_CAP;
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_RMG_RES_DIST;
    private static final ForgeConfigSpec.ConfigValue<Boolean> PK_DAY_DESPAWN;

    private static final ForgeConfigSpec.ConfigValue<Integer> INS_MOH;
    private static final ForgeConfigSpec.ConfigValue<Boolean> INSANITY_SOUNDS;
    private static final ForgeConfigSpec.ConfigValue<String> INSANITY_VISUALS;
    private static final ForgeConfigSpec.ConfigValue<Integer> MOON_INSANITY;
    private static final ForgeConfigSpec.ConfigValue<Integer> INSANITY_PASSIVE_REC;
    private static final ForgeConfigSpec.IntValue EXCITED_PARTICLES_ANIMAL_INTERVAL;
    private static final ForgeConfigSpec.IntValue EXCITED_PARTICLES_CROP_INTERVAL;



    private static final ForgeConfigSpec.IntValue HONOR_DUEL_DIST;
    private static final ForgeConfigSpec.DoubleValue PHOTON_CYCLE_FIRE_MULT;
    private static final ForgeConfigSpec.DoubleValue PHOTON_CYCLE_GLOW_MULT;
    private static final ForgeConfigSpec.DoubleValue PHOTON_CYCLE_FINAL_MULT;

    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_IRON_DMG_MULT;
    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_IRON_ATK_SPD_MULT;
    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_IRON_MINING_SPD_MULT;

    private static final ForgeConfigSpec.DoubleValue MOON_SCYTHE_BASE_DMG;
    private static final ForgeConfigSpec.DoubleValue MOON_SCYTHE_BASE_SPD;
    private static final ForgeConfigSpec.DoubleValue MOON_SCYTHE_STRIKE_DMG;
    private static final ForgeConfigSpec.DoubleValue MOON_SCYTHE_WAVE_DMG;

    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_REAPER_BASE_DMG;
    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_REAPER_BASE_SPD;
    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_REAPER_STRIKE_DMG;
    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_REAPER_WAVE_DMG;

    private static final ForgeConfigSpec.DoubleValue LUNAR_ARROW_BASE_DMG;
    private static final ForgeConfigSpec.DoubleValue SOLAR_ARROW_BASE_DMG;




/*
maybe use json files instead since it'll look neater?
 */
    private static final ForgeConfigSpec.ConfigValue<List<Integer>> WHELP_WAVE1_VALS=null;
    private static final ForgeConfigSpec.ConfigValue<Integer[]> WHELP_WAVE2_VALS=null;
    /*
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WHELP_WAVE2_VALS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WHELP_WAVE3_VALS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WHELP_WAVE4_VALS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WHELP_WAVE5_VALS;

     */

    static{
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Celestial Awakening Config");
        ENABLE_WIP_CONTENT= builder.comment("If true, enables functional content that is incomplete (such as items with missing textures, or enemies with only partially finished AI.\nDefault: false").define("enable_wip",false);

        builder.push("Material_Config");
            SUNSTONE_LEAVES_RATE=builder.comment("Base % drop rate of sunstones from tree leaf blocks.\nDefault: 6.5").defineInRange("sunstone_leaves",6.5d,0,100d);
            SUNSTONE_GRASS_RATE=builder.comment("Base % drop rate of sunstones from bushes and tall grass blocks.\nDefault: 4.5").defineInRange("sunstone_grass",4.5d,0,100d);
            LUNAR_MATERIAL_DIMENSIONS=builder.comment("Dimensions that moonstones can spawn in.\nDefault: [minecraft:overworld]").defineListAllowEmpty("lunar_mat_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")), obj->obj instanceof String);
            MOONSTONE_LIMIT=builder.comment("Max number of moonstones that can exist in a dimension simultaneously. Set to -1 to have no limit.\nDefault: 24").defineInRange("moonstone_lim",24,-1,Integer.MAX_VALUE);
            MOONSTONE_INTERVAL=builder.comment("Interval in ticks for each instance of moonstone spawning.\nDefault:1500").defineInRange("moonstone_interval",1500,100,Integer.MAX_VALUE);
            MOONSTONE_CNT=builder.comment("Amount of moonstones spawned per interval for each player.\nDefault: 2").defineInRange("moonstone_cnt",2,1,Integer.MAX_VALUE);
        builder.pop();

        builder.push("Sanity_Config");
            INS_MOH=builder.comment("Change in sanity when hit by a mob that has the mark of haunting effect.\nDefault:-500").defineInRange("ins_moh",-500,Short.MIN_VALUE,Short.MAX_VALUE);
            INSANITY_SOUNDS=builder.comment("If true, causes certain sounds to have a chance to play when insane.\nDefault: true").define("ins_sound",true);
            INSANITY_VISUALS=builder.comment("Impacts the rendering of certain mobs for insane players." +
                    "\nNONE:Visual modifications are disabled" +
                    "\nSIMPLE:Model of entities are replaced, but do not have any animations" +
                    "\nCOMPLEX(Not implemented yet):Model and animation of entities are replaced. Likely adds some performance overhead"+
                    "\nDefault:SIMPLE.").define("ins_visual","SIMPLE");
            MOON_INSANITY=builder.comment("The rate of change for sanity per tick if the player is staring at the moon\nDefault:-20").defineInRange("moon_ins",20,1,Short.MAX_VALUE);
            INSANITY_PASSIVE_REC=builder.comment("Passive recovery of sanity (occurs every 5 seconds.\nDefault:40").defineInRange("ins_rec",40,1,Short.MAX_VALUE);
        builder.pop();

        builder.push("Global_Enemy_Config");
            OUTOFCOMBAT_HEAL=builder.comment("Allows some enemies to heal when not in combat.\nDefault: true").define("enemy_combat_regen",true);
        builder.pop();

        builder.push("Transcendents_Config");

            TRANSCENDENTS_DIMENSIONS =builder.comment("Dimensions that the Transcendents are allowed to be active in.\nDefault: [minecraft:overworld]").defineListAllowEmpty("transcendents_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")), obj->obj instanceof String);
            TRANSCENDENTS_MULTIPLE_DIVINER=builder.comment("If the Transcendents's diviner can be active in multiple dimensions, sets whether or not the diviner being active in multiple dimensions simultaneously.\nDefault: false").define("transcendents_diviner_shared_dim",false);
            TRANSCENDENTS_DELAY=builder.comment("Upon creating a world, block the Transcendents from doing anything until a set amount of time has passed.\nDefault: 240000 ticks(10 in game days)").defineInRange("transcendents_init_delay",240000,0,Integer.MAX_VALUE);
            TRANSCENDENTS_MIN_CD =builder.comment("Minimum cooldown for the diviner's scrying. Does not restrict other factors from hastening the next scry.\nDefault: 36000 ticks (1.5 in game days)").defineInRange("transcendents_div_min_cd",36000,0,Integer.MAX_VALUE);
            TRANSCENDENTS_MAX_CD =builder.comment("Maximum cooldown for the diviner's scrying. Does not restrict other factors from impeding the next scry.\nDefault: 72000 ticks (3 in game days)").defineInRange("transcendents_div_max_cd",72000,0,Integer.MAX_VALUE);
            TRANSCENDENTS_ENEMIES =builder.comment("List of living entities that the Transcendents will intentionally target.\nDefault: [minecraft:player].\nFormat:minecraft:zombie").defineListAllowEmpty("transcendents_targets",new ArrayList<>(Arrays.asList("minecraft:player")), obj->obj instanceof String);
            TRANSCENDENTS_DIVINER_HEATWAVE_ENABLED=builder.comment("Determines whether or not the diviner can use the heatwave ability.\nThis ability strikes exposed targets with fire, igniting them and damaging nearby terrain.\nDefault: true").define("transcendents_diviner_heatwave_active",true);
            TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS =builder.comment("Allows the heatwave ability to modify terrain.\nDefault: true").define("transcendents_diviner_heatwave",true);
            TRANSCENDENTS_DIVINER_AOD_ENABLED =builder.comment("Determines whether or not the diviner can use the Age of Darkness ability.\nAge of Darkness causes the world's sky to darken.\nDefault - true").define("transcendents_diviner_aod_active",true);
            TRANSCENDENTS_DIVINER_AOD_COSMETIC_ONLY=builder.comment("If Age of Darkness is enabled, sets whether or not the effects are cosmetic only.\nIf false, the changes in light levels can allow hostile mobs to spawn whenever and prevent undead mobs from burning during the day.\nDefault: false").define("transcendents_diviner_aod_cosmetic",false);
            TRANSCENDENTS_DIVINER_SCAN_POWER_INCREASE =builder.comment("The amount of power the diviner gets for each entity scanned.\nDefault: 10").defineInRange("transcendents_div_scan_power",10,0,100);
        builder.pop();

        builder.push("Phantom_Knight_Config");

            PK_DIMENSIONS=builder.comment("Dimensions that Phantom Knights are allowed to spawn in. Default: [minecraft:overworld]").defineListAllowEmpty("pk_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")),obj->obj instanceof String);
            PK_CRESCENCIA_MIN_DAY=builder.comment("Earliest day Phantom Knight Crescencia can spawn.\nDefault: 6").defineInRange("pk_crescencia_min_day",6,0,Integer.MAX_VALUE);
            PK_SPAWN_CAP=builder.comment("Maximum number of Phantom Knights that can spawn naturally each night.\nDefault: 1").defineInRange("pk_spawn_cap",1,1,100);
            PK_RMG_RES_DIST=builder.comment("The max distance between a Phantom Knight and an attacker before the attack's damage output gets reduced\nDefault: 15").defineInRange("pk_dmg_res_dist",15,1,100);
            PK_DAY_DESPAWN=builder.comment("Determines if phantom knights despawn during the day (this also prevents them from spawning during the day.\nDefault: true").define("pk_day_despawn",true);

        builder.pop();


        builder.push("Prowler_Config");
            PROWLER_RAID_INTERVAL=builder.comment("The number of nights that need to pass before the player experiences a prowler raid.\nDefault: 10").defineInRange("prowler_raid_interval",10,1,Integer.MAX_VALUE);
            PROWLER_DESTRUCTION=builder.comment("Determines what prowlers can destroy blocks.\nOptions: ALL, RAID, NONE.\nDefault: RAID").define("prowler_destruction","RAID");
        builder.pop();


        builder.push("Solmander_Config");
            SOLMANDER_DELAY=builder.comment("The amount of time (in ticks) before solmanders can spawn naturally.\nDefault: 72000").defineInRange("solmander_delay",72000,1,Integer.MAX_VALUE);
        builder.pop();


        builder.push("Planetary_Guardian_Config");
            CORE_GUARDIAN_COUNTER=builder.comment("The game checks every 400 ticks (20 seconds) to see who is below y-level 0, then adds this value to a counter for each player. When this counter hits 1000, core guardians will be able to spawn.\nDefault: 15").defineInRange("core_guardian_counter",15,1,50);
        builder.pop();


        builder.push("Armor_Config");
            builder.push("Radiant_Armor");
                EXCITED_PARTICLES_ANIMAL_INTERVAL =builder.comment("The delay in ticks between each activation of excited particles' animal effect.\nDefault: 30").defineInRange("excited_particles_animal_interval",30,1,Integer.MAX_VALUE);
                EXCITED_PARTICLES_CROP_INTERVAL =builder.comment("The delay in ticks between each activation of excited particles' crop effect.\nDefault: 400").defineInRange("excited_particles_crop_interval",400,1,Integer.MAX_VALUE);

        builder.pop();
            builder.push("Knightmare_Suit");
                HONOR_DUEL_DIST=builder.comment("The maximum number of blocks between two entities linked entities linked by honor duel before the link breaks.\nDefault: 25").defineInRange("honor_duel_dist",25,1,100);
            builder.pop();
            builder.push("Everlight_Armor");
                PHOTON_CYCLE_FIRE_MULT=builder.comment("Photon Cycle\nThe formula for calculating the heal amount is m * ( (Burning Time Reduced)*a + (Glowing Time Reduced)*b ), with time being measured in ticks.")
                        .comment("Value of a.\nDefault: 0.6").defineInRange("photon_cycle_fire",0.6d,0,100);
                PHOTON_CYCLE_GLOW_MULT=builder.
                comment("Value of b.\nDefault: 0.9").defineInRange("photon_cycle_glow",0.9d,0,100);
                PHOTON_CYCLE_FINAL_MULT=builder.
                comment("Value of m.\nDefault: 0.05").defineInRange("photon_cycle_final",0.05d,0,100);
            builder.pop();
        builder.pop();

        builder.push("Weapons_&_Tools_Config");
            builder.push("Midnight_Iron_Tools");
                MIDNIGHT_IRON_DMG_MULT=builder.comment("The damage multiplier midnight iron tools receive at night.\nDefault: 1.2").defineInRange("midnight_iron_dmg_mult",1.2d,1,100);
                MIDNIGHT_IRON_ATK_SPD_MULT=builder.comment("The attack speed multiplier midnight iron tools receive at night. Lower values means faster attack speed.\nDefault: 0.9").defineInRange("midnight_iron_atk_spd_mult",0.9d,0.1,1);
                MIDNIGHT_IRON_MINING_SPD_MULT=builder.comment("The mining speed multiplier midnight iron tools receive at night.\nDefault: 1.3").defineInRange("midnight_iron_mining_spd_mult",1.3d,1,100);
            builder.pop();
            builder.push("Moon_Scythe&Midnight_Reaper");
                MOON_SCYTHE_BASE_DMG=builder.comment("Base damage of the moon scythe weapon.\nDefault: 6.5").defineInRange("moon_scythe_base_damage",6.5d,0,Double.MAX_VALUE);
                MOON_SCYTHE_BASE_SPD=builder.comment("Base spd of the moon scythe weapon.\nDefault: -2.8").defineInRange("moon_scythe_base_spd",-2.8d,-Double.MAX_VALUE,Double.MAX_VALUE);
                MOON_SCYTHE_WAVE_DMG=builder.comment("Base damage of the moon scythe's crescent wave.\nDefault: 2.5").defineInRange("moon_scythe_wave_damage",2.5d,0,Double.MAX_VALUE);
                MOON_SCYTHE_STRIKE_DMG=builder.comment("Base damage of the moon scythe's crescent strike.\nDefault: 4.5").defineInRange("moon_scythe_strike_damage",4.5d,0,Double.MAX_VALUE);

                MIDNIGHT_REAPER_BASE_DMG=builder.comment("Base damage of the midnight reaper weapon.\nDefault: 7.8").defineInRange("midnight_reaper_base_damage",7.8d,0,Double.MAX_VALUE);
                MIDNIGHT_REAPER_BASE_SPD=builder.comment("Base attack speed of the midnight reaper weapon.\nDefault: -2.7").defineInRange("midnight_reaper_base_spd",-2.7d,-Double.MAX_VALUE,Double.MAX_VALUE);
                MIDNIGHT_REAPER_WAVE_DMG=builder.comment("Base damage of the midnight reaper's crescent wave.\nDefault: 3.5").defineInRange("moon_scythe_wave_damage",3.5d,0,Double.MAX_VALUE);
                MIDNIGHT_REAPER_STRIKE_DMG=builder.comment("Base damage of the midnight reaper's crescent strike.\nDefault: 5.5").defineInRange("moon_scythe_strike_damage",5.5d,0,Double.MAX_VALUE);


            builder.pop();
            builder.push("Arrows");
                builder.comment("For reference, vanilla arrows have a base damage of 2.");
                LUNAR_ARROW_BASE_DMG=builder.comment("Base damage of lunar arrows.\nDefault: 1.8").defineInRange("lunar_arrow_base_dmg",1.8d,0,Double.MAX_VALUE);
                SOLAR_ARROW_BASE_DMG=builder.comment("Base damage of solar arrows.\nDefault: 1.5").defineInRange("solar_arrow_base_dmg",1.5d,0,Double.MAX_VALUE);
            builder.pop();
        builder.pop();
        BUILDER=builder;
    }
/*
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_HP_SCALE=builder.comment("Multiplies each mob's base HP by this value. Default: 1").defineInRange("mob_hp_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_DMG_SCALE=builder.comment("(CURRENTLY NOT FUNCTIONAL)Multiplies each mob's base damage by this value. Default: 1").defineInRange("mob_dmg_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_ARMOR_PT_SCALE=builder.comment("Multiplies each mob's base armor points by this value. Default: 1").defineInRange("mob_armor_pt_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_ARMOR_TOUGH_SCALE=builder.comment("Multiplies each mob's base armor toughness by this value. Default: 1").defineInRange("mob_armor_t_mult",1D,1D,Double.MAX_VALUE);


    private static final ForgeConfigSpec.ConfigValue<Double> ITEM_DMG_SCALE=builder.comment("Multiplies each item's base damage by this value. Default: 1").defineInRange("item_dmg_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> ARMOR_PT_SCALE=builder.comment("Multiplies each armor's base armor points by this value. Default: 1").defineInRange("armor_pt_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> ARMOR_T_SCALE=builder.comment("Multiplies each armor's base armor toughness by this value. Default: 1").defineInRange("armor_t_mult",1D,1D,Double.MAX_VALUE);
 */

    //private static final ForgeConfigSpec.ConfigValue<Integer> UPDATE_TICK_DELAY=builder.comment("Represents how many ticks the Server waits for before performing a manual update on various mod components.\nWould recommend not touching this unless game\nDefault 2400 ticks(2 realtime minutes)").defineInRange("sol_cult_init_delay",2400,600,24000);

    static final ForgeConfigSpec SPEC =  BUILDER.build();

    public static boolean mobCombatRegen=true;
    public static boolean wipEnabled=false;


    public static double mobHPScale=1;
    public static double mobDmgScale=1;
    public static double mobArmorPtScale=1;
    public static double mobArmorToughnessScale=1;

    public static double itemDmgScale=1;
    public static double armorPtScale=1;
    public static double armorToughnessScale=1;

    public static boolean divinerShared;
    public static boolean divinerHeatwaveEnabled;
    public static boolean divinerAoDEnabled;
    public static boolean divinerHeatWaveBlockMod;
    public static boolean divinerAoDCosmetic;

    public static int divinerScanPower;
    public static Set<ResourceKey<DimensionType>> transcendentsDimensionTypes;
    public static int transcendentsInitDelay;
    public static int transcendentsDivMinCD;
    public static int transcendentsDivMaxCD;
    public static Set<EntityType<?>> transcendentsTargets;

    public static int prowlerRaidInterval;

    public static int solmanderDelay;

    public static int pkSpawnCap;
    public static int pkSpawnDayCD;
    public static int pkDmgResDist=15;
    public static float pkDmgResDistVal=0.2f;
    public static int pkCrescenciaMinDay;
    public static Set<ResourceKey<DimensionType>> pkDimensionTypes;
    public static boolean pkDayDespawn;

    public static int coreGuardianCounter=2;


    public static double sunstoneLeavesRate;
    public static double sunstoneGrassRate;

    public static Set<ResourceKey<DimensionType>> lunarMatDimensionTypes;
    public static int moonstoneDimLim=15;
    public static int moonstoneInterval;
    public static int moonstoneCnt=2;



    public static boolean insSound=true;
    public static int moonInsVal;
    public enum INS_VISUALS{
        NONE,
        SIMPLE,
        COMPLEX
    }
    public static INS_VISUALS insVisuals=INS_VISUALS.SIMPLE;
    public static int insRec;
    public static int insMoH;

    public static int rejWaveInterval =300;
    public static double rejWaveAmt =0.5d;
    public static int excitedParticlesAnimalTickInterval =30;
    public static int excitedParticlesCropTickInterval=400;

    public static double honorDuelDist;

    public static double photonCycleFireMult;
    public static double photonCycleGlowMult;
    public static double photonCycleFinalMult;

    public static double midnightIronDmgMult;
    public static double midnightIronAtkSpdMult;
    public static double midnightIronMiningSpdMult;

    public static double moonScytheBaseDmg=6.5f;
    public static double moonScytheBaseSpd=-2.8f;
    public static double moonScytheWaveDmg;
    public static double moonScytheStrikeDmg;

    public static double midnightReaperBaseDmg=7.8f;
    public static double midnightReaperBaseSpd=-2.7f;;
    public static double midnightReaperWaveDmg;
    public static double midnightReaperStrikeDmg;

    public static double arrowSolarDmg;
    public static double arrowLunarDmg;

    public static boolean useVanillaTeams;

    public enum ProwlerDestruction{
        ALL,
        RAID,
        NONE
    }
    public static ProwlerDestruction prowlerDestruction=ProwlerDestruction.RAID;

    public static List<Integer> whelpWave1Vals;


    static Set<ResourceKey<DimensionType>> strToDimTypeKey(List<? extends String> list){
        return list.stream()
                .map(obj-> ResourceKey.create(Registries.DIMENSION_TYPE,new ResourceLocation(obj)))
                .collect(Collectors.toSet());
    }


    static Set<EntityType<?>> strToEntities(List<? extends String> list){
        return list.stream().map(
          obj-> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(obj))).collect(Collectors.toSet());
    }
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        //event.getConfig().get
        wipEnabled=ENABLE_WIP_CONTENT.get();
        mobCombatRegen= OUTOFCOMBAT_HEAL.get();

        transcendentsDimensionTypes = strToDimTypeKey(TRANSCENDENTS_DIMENSIONS.get());

        lunarMatDimensionTypes=strToDimTypeKey(LUNAR_MATERIAL_DIMENSIONS.get());

        moonstoneDimLim=MOONSTONE_LIMIT.get();

        moonstoneInterval=MOONSTONE_INTERVAL.get();


        moonstoneCnt=MOONSTONE_CNT.get();

        sunstoneGrassRate=SUNSTONE_GRASS_RATE.get();
        sunstoneLeavesRate=SUNSTONE_LEAVES_RATE.get();

        divinerShared= TRANSCENDENTS_MULTIPLE_DIVINER.get();

        transcendentsInitDelay = TRANSCENDENTS_DELAY.get();

        transcendentsDivMinCD = TRANSCENDENTS_MIN_CD.get();

        transcendentsDivMaxCD = TRANSCENDENTS_MAX_CD.get();

        transcendentsTargets =strToEntities(TRANSCENDENTS_ENEMIES.get());

        divinerHeatwaveEnabled=TRANSCENDENTS_DIVINER_HEATWAVE_ENABLED.get();

        divinerAoDEnabled= TRANSCENDENTS_DIVINER_AOD_ENABLED.get();

        divinerHeatWaveBlockMod=TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS.get();

        divinerAoDCosmetic=TRANSCENDENTS_DIVINER_AOD_COSMETIC_ONLY.get();

        divinerScanPower=TRANSCENDENTS_DIVINER_SCAN_POWER_INCREASE.get();

        solmanderDelay=SOLMANDER_DELAY.get();

        prowlerRaidInterval=PROWLER_RAID_INTERVAL.get();
        prowlerDestruction=ProwlerDestruction.valueOf(PROWLER_DESTRUCTION.get());

        coreGuardianCounter=CORE_GUARDIAN_COUNTER.get();


        pkDimensionTypes=strToDimTypeKey(PK_DIMENSIONS.get());

        pkCrescenciaMinDay=PK_CRESCENCIA_MIN_DAY.get()-1;

        pkSpawnCap=PK_SPAWN_CAP.get();

        pkDmgResDist=PK_RMG_RES_DIST.get();

        pkDayDespawn=PK_DAY_DESPAWN.get();

        insSound=INSANITY_SOUNDS.get();
        try{
            insVisuals=INS_VISUALS.valueOf(INSANITY_VISUALS.get().toUpperCase());
        }
        catch(Exception e){
            insVisuals=INS_VISUALS.SIMPLE;
        }

        moonInsVal=MOON_INSANITY.get();

        insRec=INSANITY_PASSIVE_REC.get();

        insMoH=INS_MOH.get();

        excitedParticlesAnimalTickInterval = EXCITED_PARTICLES_ANIMAL_INTERVAL.get();
        excitedParticlesCropTickInterval= EXCITED_PARTICLES_CROP_INTERVAL.get();


        honorDuelDist= HONOR_DUEL_DIST.get();

        photonCycleFireMult=PHOTON_CYCLE_FIRE_MULT.get();
        photonCycleGlowMult=PHOTON_CYCLE_GLOW_MULT.get();
        photonCycleFinalMult=PHOTON_CYCLE_FINAL_MULT.get();

        midnightIronDmgMult=MIDNIGHT_IRON_DMG_MULT.get();
        midnightIronAtkSpdMult =MIDNIGHT_IRON_ATK_SPD_MULT.get();
        midnightIronMiningSpdMult=MIDNIGHT_IRON_MINING_SPD_MULT.get();

        moonScytheBaseDmg=MOON_SCYTHE_BASE_DMG.get();
        moonScytheBaseSpd=MOON_SCYTHE_BASE_SPD.get();
        moonScytheStrikeDmg=MOON_SCYTHE_STRIKE_DMG.get();
        moonScytheWaveDmg=MOON_SCYTHE_WAVE_DMG.get();

        midnightReaperBaseDmg=MIDNIGHT_REAPER_BASE_DMG.get();
        midnightReaperBaseSpd=MIDNIGHT_REAPER_BASE_SPD.get();
        midnightReaperStrikeDmg=MIDNIGHT_REAPER_STRIKE_DMG.get();
        midnightReaperWaveDmg=MIDNIGHT_REAPER_WAVE_DMG.get();

        arrowLunarDmg=LUNAR_ARROW_BASE_DMG.get();
        arrowSolarDmg=SOLAR_ARROW_BASE_DMG.get();
        /*
        mobHPScale=MOB_HP_SCALE.get();
        mobDmgScale=MOB_DMG_SCALE.get();
        mobArmorPtScale=MOB_ARMOR_PT_SCALE.get();
        mobArmorToughnessScale=MOB_ARMOR_TOUGH_SCALE.get();

        itemDmgScale= ITEM_DMG_SCALE.get();
        armorPtScale=ARMOR_PT_SCALE.get();
        armorToughnessScale=ARMOR_T_SCALE.get();

         */
        /*
        only update the
         */
        /*
        if (WHELP_WAVE1_VALS.get().size()==5){
            whelpWave1Vals= WHELP_WAVE1_VALS.get();
        }

         */
        refreshMobAttributes();
    }

    public static void refreshMobAttributes(){
        //Asteron.updateAttributesFromConfig();
        //System.out.println("new HP SCALE IS " + mobHPScale);
    }


}
