package com.github.celestial_awakening;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
//TODO: probably update to a server config instead of common
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER;
    public static boolean ready=false;
    static final ForgeConfigSpec SPEC;
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
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DIVINER_SCAN_POWER_BASE;
    static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_DIVINER_SH_ENABLED;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DIVINER_SH_ROT_BASE_CHANCE;
    static final ForgeConfigSpec.ConfigValue<List<? extends Double>> TRANSCENDENTS_DIVINER_SH_ROT_DIFF_MOD;//arr
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DIVINER_SH_INV_ROT_INTERVAL;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DIVINER_SH_FOOD_ROT_MIN_AMT;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DIVINER_SH_FOOD_ROT_MAX_AMT;

    static final ForgeConfigSpec.IntValue PROWLER_RAID_INTERVAL;
    static final ForgeConfigSpec.ConfigValue<String> PROWLER_DESTRUCTION;
    static final ForgeConfigSpec.IntValue SOLMANDER_DELAY;
    static final ForgeConfigSpec.IntValue SOLMANDER_INTERVAL;
    static final ForgeConfigSpec.IntValue SOLMANDER_CHANCE;

    static final ForgeConfigSpec.IntValue CORE_GUARDIAN_COUNTER;

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LUNAR_MATERIAL_DIMENSIONS;
    private static final ForgeConfigSpec.ConfigValue<Integer> MOONSTONE_LIMIT;
    private static final ForgeConfigSpec.ConfigValue<Integer> MOONSTONE_INTERVAL;
    private static final ForgeConfigSpec.ConfigValue<Integer> MOONSTONE_CNT;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> PK_DIMENSIONS;
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_CRESCENCIA_MIN_DAY;
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_SPAWN_CAP;
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_DMG_RES_DIST;
    private static final ForgeConfigSpec.ConfigValue<Boolean> PK_DAY_DESPAWN;

    private static final ForgeConfigSpec.ConfigValue<Integer> INS_MOH;
    private static final ForgeConfigSpec.ConfigValue<Boolean> INSANITY_SOUNDS;
    private static final ForgeConfigSpec.ConfigValue<String> INSANITY_VISUALS;
    private static final ForgeConfigSpec.ConfigValue<Integer> MOON_INSANITY;
    private static final ForgeConfigSpec.ConfigValue<Integer> INSANITY_PASSIVE_REC;
    private static final ForgeConfigSpec.IntValue EXCITED_PARTICLES_ANIMAL_INTERVAL;
    private static final ForgeConfigSpec.IntValue EXCITED_PARTICLES_CROP_INTERVAL;
    //private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCITED_PARTICLES_EXPLICITEDLY_INCLUDED_CROPS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCITED_PARTICLES_BLOCK_WHITELIST;
    private static final ForgeConfigSpec.BooleanValue EXCITED_PARTICLES_WORKS_ON_SPREADING_BLOCKS;
    private static final ForgeConfigSpec.BooleanValue EXCITED_PARTICLES_WORKS_ON_NYLIUM_BLOCKS;
    //private static final ForgeConfigSpec.BooleanValue EXCITED_PARTICLES_DEFAULT_VALS_ENABLED;

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
            TRANSCENDENTS_DIVINER_SCAN_POWER_INCREASE =builder.comment("The amount of power the diviner gets for each entity scanned.\nDefault: 10").defineInRange("transcendents_div_scan_power",10,0,100);
            TRANSCENDENTS_DIVINER_SCAN_POWER_BASE =builder.comment("The amount of power the diviner gets upon finishing a scrying.\nDefault: 0").defineInRange("transcendents_div_scan_power_base",0,0,100);


            TRANSCENDENTS_DIVINER_HEATWAVE_ENABLED=builder.comment("Determines whether or not the diviner can use the heatwave ability.\nThis ability strikes exposed targets with fire, igniting them and damaging nearby terrain.\nDefault: true").define("transcendents_diviner_heatwave_active",true);
            TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS =builder.comment("Allows the heatwave ability to modify terrain.\nDefault: true").define("transcendents_diviner_heatwave",true);
            TRANSCENDENTS_DIVINER_AOD_ENABLED =builder.comment("Determines whether or not the diviner can use the Age of Darkness ability.\nDefault - true").define("transcendents_diviner_aod_active",true);
            TRANSCENDENTS_DIVINER_AOD_COSMETIC_ONLY=builder.comment("If Age of Darkness is enabled, sets whether or not the skylight effects are cosmetic only.\nIf false, the changes in light levels can allow hostile mobs to spawn whenever as well as prevent undead mobs from burning during the day.\nDefault: false").define("transcendents_diviner_aod_cosmetic",false);


            TRANSCENDENTS_DIVINER_SH_ENABLED=builder.comment("Determines whether or not the diviner can use the Suffocating Heat ability.\nDefault - true").define("transcendents_diviner_sh_active",true);
            TRANSCENDENTS_DIVINER_SH_ROT_BASE_CHANCE =builder.comment("Base chance (out of 100) for a item slot with food to rot.\nDefault:10").defineInRange("transcendents_div_sh_rot_base",10,0,100);
            TRANSCENDENTS_DIVINER_SH_ROT_DIFF_MOD =builder.comment("Multipliers based on game difficulty for chance for food to rot.\nDefault:[0.8,1,1.2]").defineList("transcendents_div_sh_rot_diff_mod",List.of(0.8d,1d,1.2d), obj->obj instanceof Double);
            TRANSCENDENTS_DIVINER_SH_INV_ROT_INTERVAL=builder.comment("The delay between rot checks(in ticks).\nDefault:600").defineInRange("transcendents_div_sh_rot_interval",600,1,Integer.MAX_VALUE);
            TRANSCENDENTS_DIVINER_SH_FOOD_ROT_MIN_AMT=builder.comment("Minimum amount of food that is taken away on a successful rot roll.\nDefault:1").defineInRange("transcendents_div_sh_rot_min",1,0,Integer.MAX_VALUE);
            TRANSCENDENTS_DIVINER_SH_FOOD_ROT_MAX_AMT=builder.comment("Maximum amount of food that is taken away on a successful rot roll.\nDefault:2").defineInRange("transcendents_div_sh_rot_max",2,0,Integer.MAX_VALUE);

        builder.pop();

        builder.push("Phantom_Knight_Config");

            PK_DIMENSIONS=builder.comment("Dimensions that Phantom Knights are allowed to spawn in. Default: [minecraft:overworld]").defineListAllowEmpty("pk_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")),obj->obj instanceof String);
            PK_CRESCENCIA_MIN_DAY=builder.comment("Earliest day Phantom Knight Crescencia can spawn.\nDefault: 6").defineInRange("pk_crescencia_min_day",6,0,Integer.MAX_VALUE);
            PK_SPAWN_CAP=builder.comment("Maximum number of Phantom Knights that can spawn naturally each night.\nDefault: 1").defineInRange("pk_spawn_cap",1,1,100);
            PK_DMG_RES_DIST =builder.comment("The max distance between a Phantom Knight and an attacker before the attack's damage output gets reduced\nDefault: 15").defineInRange("pk_dmg_res_dist",15,1,100);
            PK_DAY_DESPAWN=builder.comment("Determines if phantom knights despawn during the day (this also prevents them from spawning during the day.\nDefault: true").define("pk_day_despawn",true);

        builder.pop();


        builder.push("Prowler_Config");
            PROWLER_RAID_INTERVAL=builder.comment("The number of nights that need to pass before the player experiences a prowler raid.\nDefault: 10").defineInRange("prowler_raid_interval",10,1,Integer.MAX_VALUE);
            PROWLER_DESTRUCTION=builder.comment("Determines what prowlers can destroy blocks.\nOptions: ALL, RAID, NONE.\nDefault: RAID").define("prowler_destruction","RAID");
        builder.pop();


        builder.push("Solmander_Config");
            SOLMANDER_DELAY=builder.comment("The amount of time (in ticks) before solmanders can spawn naturally.\nDefault: 72000").defineInRange("solmander_delay",72000,1,Integer.MAX_VALUE);
            SOLMANDER_INTERVAL=builder.comment("The amount of time (in ticks) between solmander spawn attempts.\nDefault: 1200").defineInRange("solmander_interval",1200,1,Integer.MAX_VALUE);
            SOLMANDER_CHANCE=builder.comment("Solmander spawning works by attempting to spawn near each player. This value represents the chance (out of a 100) for an attempt to succeed prior to any other checks.\nDefault: 15").defineInRange("solmander_chance",15,0,100);

        builder.pop();


        builder.push("Planetary_Guardian_Config");
            CORE_GUARDIAN_COUNTER=builder.comment("The game checks every 400 ticks (20 seconds) to see who is below y-level 0, then adds this value to a counter for each player. When this counter hits 1000, core guardians will be able to spawn.\nDefault: 15").defineInRange("core_guardian_counter",15,1,50);
        builder.pop();


        builder.push("Armor_Config");
            builder.push("Radiant_Armor");
                EXCITED_PARTICLES_ANIMAL_INTERVAL =builder.comment("The delay in ticks between each activation of excited particles' animal effect.\nDefault: 30").defineInRange("excited_particles_animal_interval",30,1,Integer.MAX_VALUE);
                EXCITED_PARTICLES_CROP_INTERVAL =builder.comment("The delay in ticks between each activation of excited particles' crop effect.\nDefault: 400").defineInRange("excited_particles_crop_interval",400,1,Integer.MAX_VALUE);
                //EXCITED_PARTICLES_DEFAULT_VALS_ENABLED=builder.comment("Excited particles triggers on blocks classified as 'Bush Blocks', 'Crop Blocks', 'Sapling Blocks', and")
                //EXCITED_PARTICLES_EXPLICITEDLY_INCLUDED_CROPS=builder.comment("Some plants that may be considered crops are not classified under a unifying class in vanilla's code. This list allows excited particles to affect specific blocks that don't match any of the requirements.\nDefault: [minecraft:cocoa,minecraft:sugar_cane]").defineListAllowEmpty("excited_particles_explicit_crops",new ArrayList<>(Arrays.asList("minecraft:cocoa","minecraft:sugar_cane")), obj->obj instanceof String);
                //EXCITED_PARTICLES_BLOCK_BLACKLIST=builder.comment("Due to the way vanilla code is structured, current implementation of excited particles would include 'spreading' blocks such as grass and nylium.\nThis list blacklists those blocks (as well as any user-defined blocks) from being affected by the armor effect.\nDefault:[minecraft:grass_block,minecraft:mycelium]")
                EXCITED_PARTICLES_WORKS_ON_SPREADING_BLOCKS=builder.comment("Determines if spreading blocks (grass and mycelium) are be affected by the armor set bonus.\nDefault:false").define("excited_particles_spreading",false);
                EXCITED_PARTICLES_WORKS_ON_NYLIUM_BLOCKS=builder.comment("Determines if nylium can be affected by the armor set bonus.\nDefault:false").define("excited_particles_nylium",false);
                EXCITED_PARTICLES_BLOCK_WHITELIST=builder.comment("Whitelist for blocks that don't fall under the standard classifications used in vanilla.\nDefault:[minecraft:chorus_flower]").defineListAllowEmpty("excited_particles_whitelist",new ArrayList<>(Arrays.asList("minecraft:chorus_flower")), obj->obj instanceof String);
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
                MIDNIGHT_REAPER_WAVE_DMG=builder.comment("Base damage of the midnight reaper's crescent wave.\nDefault: 3.5").defineInRange("midnight_reaper_wave_damage",3.5d,0,Double.MAX_VALUE);
                MIDNIGHT_REAPER_STRIKE_DMG=builder.comment("Base damage of the midnight reaper's crescent strike.\nDefault: 5.5").defineInRange("midnight_reaper_strike_damage",5.5d,0,Double.MAX_VALUE);


            builder.pop();
            builder.push("Arrows");
                builder.comment("For reference, vanilla arrows have a base damage of 2.");
                LUNAR_ARROW_BASE_DMG=builder.comment("Base damage of lunar arrows.\nDefault: 1.8").defineInRange("lunar_arrow_base_dmg",1.8d,0,Double.MAX_VALUE);
                SOLAR_ARROW_BASE_DMG=builder.comment("Base damage of solar arrows.\nDefault: 1.5").defineInRange("solar_arrow_base_dmg",1.5d,0,Double.MAX_VALUE);
            builder.pop();
        builder.pop();
        BUILDER=builder;
        SPEC=BUILDER.build();
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

//TODO: change all of these from being static vars to being static methods that return the getter

    public static boolean mobCombatRegen(){
        return OUTOFCOMBAT_HEAL.get();
    }
    public static boolean wipEnabled(){
        return ENABLE_WIP_CONTENT.get();
    }

    public static double mobHPScale=1;
    public static double mobDmgScale=1;
    public static double mobArmorPtScale=1;
    public static double mobArmorToughnessScale=1;

    public static double itemDmgScale=1;
    public static double armorPtScale=1;
    public static double armorToughnessScale=1;

    public static boolean divinerShared(){
        return TRANSCENDENTS_MULTIPLE_DIVINER.get();
    }

    public static boolean divinerHeatwaveEnabled(){
        return TRANSCENDENTS_DIVINER_HEATWAVE_ENABLED.get();
    }
    public static boolean divinerAoDEnabled(){
        return  TRANSCENDENTS_DIVINER_AOD_ENABLED.get();
    }
    public static boolean divinerHeatWaveBlockMod(){
        return TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS.get();
    }

    public static boolean divinerAoDCosmetic(){
        return TRANSCENDENTS_DIVINER_AOD_COSMETIC_ONLY.get();

    }
    public static boolean divinerSHEnabled(){
        return TRANSCENDENTS_DIVINER_SH_ENABLED.get();
    }
    public static int divinerSHRotInterval(){
        return TRANSCENDENTS_DIVINER_SH_INV_ROT_INTERVAL.get();
    }
    public static int divinerSHRotBaseChance(){
        return TRANSCENDENTS_DIVINER_SH_ROT_BASE_CHANCE.get();
    }
    public static List<Double> divinerSHRotDiffMod(){
        return (List<Double>) TRANSCENDENTS_DIVINER_SH_ROT_DIFF_MOD.get();
    }
    public static int divinerSHItemRotMinAmt(){
        return TRANSCENDENTS_DIVINER_SH_FOOD_ROT_MIN_AMT.get();
    }
    public static int divinerSHItemRotMaxAmt(){
        return TRANSCENDENTS_DIVINER_SH_FOOD_ROT_MAX_AMT.get();
    }

    public static int divinerScanPower(){
        return TRANSCENDENTS_DIVINER_SCAN_POWER_INCREASE.get();
    }
    public static int divinerScanPowerBase(){
        return TRANSCENDENTS_DIVINER_SCAN_POWER_BASE.get();
    }
    public static Set<ResourceKey<DimensionType>> transcendentsDimensionTypes;
    public static Set<ResourceKey<DimensionType>> transcendentsDimensionTypes(){
        if (!ready){
            return strToDimTypeKey(TRANSCENDENTS_DIMENSIONS.get());
        }
        return transcendentsDimensionTypes;
    }
    public static int transcendentsInitDelay(){
        return TRANSCENDENTS_DELAY.get();
    }
    public static int transcendentsDivMinCD(){
        return TRANSCENDENTS_MIN_CD.get();
    }
    public static int transcendentsDivMaxCD(){
        return TRANSCENDENTS_MAX_CD.get();
    }
    public static Set<EntityType<?>> transcendentsTargets;
    public static Set<EntityType<?>> transcendentsTargets(){
        if (!ready){
            return strToEntities(TRANSCENDENTS_ENEMIES.get());
        }
        return transcendentsTargets;
    }

    public static int prowlerRaidInterval(){
        return PROWLER_RAID_INTERVAL.get();
    }

    public static int solmanderDelay(){
        return SOLMANDER_DELAY.get();
    }
    public static int solmanderInterval(){
        return SOLMANDER_INTERVAL.get();
    }
    public static int solmanderChance(){
        return SOLMANDER_CHANCE.get();
    }

    public static int undergroundGuardianChance=15;
    public static int moltenGuardianWeight;
    public static int coreGuardianWeight=5;


    public static int pkSpawnCap(){
        return PK_SPAWN_CAP.get();
    }
    public static int pkSpawnDayCD;
    public static int pkDmgResDist(){
        return PK_DMG_RES_DIST.get();
    }
    public static float pkDmgResDistVal=0.2f;

    public static int pkCrescenciaMinDay(){
        return PK_CRESCENCIA_MIN_DAY.get()-1;
    }
    public static Set<ResourceKey<DimensionType>> pkDimensionTypes;
    public static Set<ResourceKey<DimensionType>> pkDimensionTypes(){
        if (!ready){
            return strToDimTypeKey(PK_DIMENSIONS.get());
        }
        return pkDimensionTypes;
    }
    public static boolean pkDayDespawn(){
        return PK_DAY_DESPAWN.get();
    }

    public static int coreGuardianCounter=2;


    public static double sunstoneLeavesRate(){
        return SUNSTONE_LEAVES_RATE.get();
    }
    public static double sunstoneGrassRate(){
        return SUNSTONE_GRASS_RATE.get();
    }

    public static Set<ResourceKey<DimensionType>> lunarMatDimensionTypes;
    public static Set<ResourceKey<DimensionType>> lunarMatDimensionTypes(){
        if (!ready){
            return strToDimTypeKey(LUNAR_MATERIAL_DIMENSIONS.get());
        }
        return lunarMatDimensionTypes;
    }
    public static int moonstoneDimLim(){
        return MOONSTONE_LIMIT.get();
    }
    public static int moonstoneInterval(){
        return MOONSTONE_INTERVAL.get();
    }
    public static int moonstoneCnt(){
        return MOONSTONE_CNT.get();
    }


    public static boolean insSound(){
        return INSANITY_SOUNDS.get();
    }
    public static int moonInsVal(){
        return MOON_INSANITY.get();
    }
    public enum INS_VISUALS{
        NONE,
        SIMPLE,
        COMPLEX
    }
    public static INS_VISUALS insVisuals=INS_VISUALS.SIMPLE;
    public static INS_VISUALS insVisuals(){
        if (!ready){
            try{
                return insVisuals=INS_VISUALS.valueOf(INSANITY_VISUALS.get().toUpperCase());
            }
            catch(Exception e){
                return insVisuals=INS_VISUALS.SIMPLE;
            }

        }
        return insVisuals;
    }
    public static int insRec(){
        return INSANITY_PASSIVE_REC.get();
    }

    public static int insMoH(){
        return INS_MOH.get();
    }
    public static int rejWaveInterval =300;
    public static double rejWaveAmt =0.5d;

    public static int excitedParticlesAnimalTickInterval(){
        return EXCITED_PARTICLES_ANIMAL_INTERVAL.get();
    }
    public static int excitedParticlesCropTickInterval(){
        return EXCITED_PARTICLES_CROP_INTERVAL.get();
    }
    public static Set<Block> epIncludedCrops;
    public static Set<Block> epIncludedCrops(){
        if (!ready){
            return strToBlocks(EXCITED_PARTICLES_BLOCK_WHITELIST.get());
        }
        return epIncludedCrops;
    }
    public static boolean epCropSpreading(){
        return EXCITED_PARTICLES_WORKS_ON_SPREADING_BLOCKS.get();
    }
    public static boolean epCropNylium(){
        return EXCITED_PARTICLES_WORKS_ON_NYLIUM_BLOCKS.get();
    }


    public static double honorDuelDist(){
        return HONOR_DUEL_DIST.get();
    }

    public static double photonCycleFireMult(){
        return PHOTON_CYCLE_FIRE_MULT.get();
    }
    public static double photonCycleGlowMult(){
        return PHOTON_CYCLE_GLOW_MULT.get();
    }
    public static double photonCycleFinalMult(){
        return PHOTON_CYCLE_FINAL_MULT.get();
    }


    public static double midnightIronDmgMult(){
        return MIDNIGHT_IRON_DMG_MULT.get();
    }

    public static double midnightIronAtkSpdMult(){
        return MIDNIGHT_IRON_ATK_SPD_MULT.get();
    }

    public static double midnightIronMiningSpdMult(){
        return MIDNIGHT_IRON_MINING_SPD_MULT.get();
    }
/*
                MOON_SCYTHE_BASE_DMG=builder.comment("Base damage of the moon scythe weapon.\nDefault: 6.5").defineInRange("moon_scythe_base_damage",6.5d,0,Double.MAX_VALUE);
                MOON_SCYTHE_BASE_SPD=builder.comment("Base spd of the moon scythe weapon.\nDefault: -2.8").defineInRange("moon_scythe_base_spd",-2.8d,-Double.MAX_VALUE,Double.MAX_VALUE);
                MOON_SCYTHE_WAVE_DMG=builder.comment("Base damage of the moon scythe's crescent wave.\nDefault: 2.5").defineInRange("moon_scythe_wave_damage",2.5d,0,Double.MAX_VALUE);
                MOON_SCYTHE_STRIKE_DMG=builder.comment("Base damage of the moon scythe's crescent strike.\nDefault: 4.5").defineInRange("moon_scythe_strike_damage",4.5d,0,Double.MAX_VALUE);

                MIDNIGHT_REAPER_BASE_DMG=builder.comment("Base damage of the midnight reaper weapon.\nDefault: 7.8").defineInRange("midnight_reaper_base_damage",7.8d,0,Double.MAX_VALUE);
                MIDNIGHT_REAPER_BASE_SPD=builder.comment("Base attack speed of the midnight reaper weapon.\nDefault: -2.7").defineInRange("midnight_reaper_base_spd",-2.7d,-Double.MAX_VALUE,Double.MAX_VALUE);
                MIDNIGHT_REAPER_WAVE_DMG=builder.comment("Base damage of the midnight reaper's crescent wave.\nDefault: 3.5").defineInRange("moon_scythe_wave_damage",3.5d,0,Double.MAX_VALUE);
                MIDNIGHT_REAPER_STRIKE_DMG=builder.comment("Base damage of the midnight reaper's crescent strike.\nDefault: 5.5").defineInRange("moon_scythe_strike_damage",5.5d,0,Double.MAX_VALUE);


 */
    public static double moonScytheBaseDmg(){
        if (!ready){
            return 6.5d;
        }
        return MOON_SCYTHE_BASE_DMG.get();
    }
    public static double moonScytheBaseSpd(){
        if (!ready){
            return -2.8d;
        }
        return MOON_SCYTHE_BASE_SPD.get();
    }
    public static double moonScytheWaveDmg(){
        if (!ready){
            return 2.5d;
        }
        return MOON_SCYTHE_WAVE_DMG.get();
    }
    public static double moonScytheStrikeDmg(){
        if (!ready){
            return 4.5d;
        }
        return MOON_SCYTHE_STRIKE_DMG.get();
    }
    public static double midnightReaperBaseDmg(){
        if (!ready){
            return 7.8d;
        }
        return MIDNIGHT_REAPER_BASE_DMG.get();
    }
    public static double midnightReaperBaseSpd(){
        if (!ready){
            return -2.7d;
        }
        return MIDNIGHT_REAPER_BASE_SPD.get();
    }
    public static double midnightReaperWaveDmg(){
        if (!ready){
            return 3.5d;
        }
        return MIDNIGHT_REAPER_WAVE_DMG.get();
    }
    public static double midnightReaperStrikeDmg(){
        if (!ready){
            return 5.5d;
        }
        return MIDNIGHT_REAPER_STRIKE_DMG.get();
    }
    public static double arrowSolarDmg(){
        return SOLAR_ARROW_BASE_DMG.get();
    }
    public static double arrowLunarDmg(){
        return LUNAR_ARROW_BASE_DMG.get();
    }
    public static boolean useVanillaTeams;

    public enum ProwlerDestruction{
        ALL,
        RAID,
        NONE
    }
    public static ProwlerDestruction prowlerDestruction=ProwlerDestruction.RAID;
    public static ProwlerDestruction prowlerDestruction(){
        if (!ready){
            return ProwlerDestruction.valueOf(PROWLER_DESTRUCTION.get());
        }
        return prowlerDestruction;
    }
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
    static Set<Block> strToBlocks(List<? extends String> list){
        return list.stream().map(
                obj-> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj))).collect(Collectors.toSet());
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event)
    {

        transcendentsDimensionTypes = strToDimTypeKey(TRANSCENDENTS_DIMENSIONS.get());


        lunarMatDimensionTypes=strToDimTypeKey(LUNAR_MATERIAL_DIMENSIONS.get());



        transcendentsTargets =strToEntities(TRANSCENDENTS_ENEMIES.get());


        prowlerDestruction=ProwlerDestruction.valueOf(PROWLER_DESTRUCTION.get());




        pkDimensionTypes=strToDimTypeKey(PK_DIMENSIONS.get());

        try{
            insVisuals=INS_VISUALS.valueOf(INSANITY_VISUALS.get().toUpperCase());
        }
        catch(Exception e){
            insVisuals=INS_VISUALS.SIMPLE;
        }



        epIncludedCrops=strToBlocks(EXCITED_PARTICLES_BLOCK_WHITELIST.get());
    }
    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event)
    {
        System.out.println("FINAL CONFIG LOAD");

        transcendentsDimensionTypes = strToDimTypeKey(TRANSCENDENTS_DIMENSIONS.get());


        lunarMatDimensionTypes=strToDimTypeKey(LUNAR_MATERIAL_DIMENSIONS.get());



        transcendentsTargets =strToEntities(TRANSCENDENTS_ENEMIES.get());


        prowlerDestruction=ProwlerDestruction.valueOf(PROWLER_DESTRUCTION.get());




        pkDimensionTypes=strToDimTypeKey(PK_DIMENSIONS.get());

        try{
            insVisuals=INS_VISUALS.valueOf(INSANITY_VISUALS.get().toUpperCase());
        }
        catch(Exception e){
            insVisuals=INS_VISUALS.SIMPLE;
        }



        epIncludedCrops=strToBlocks(EXCITED_PARTICLES_BLOCK_WHITELIST.get());
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
        ready=true;
    }

    public static void bakeConfig(){

            System.out.println("FINAL CONFIG LOAD");

            transcendentsDimensionTypes = strToDimTypeKey(TRANSCENDENTS_DIMENSIONS.get());


            lunarMatDimensionTypes=strToDimTypeKey(LUNAR_MATERIAL_DIMENSIONS.get());



            transcendentsTargets =strToEntities(TRANSCENDENTS_ENEMIES.get());


            prowlerDestruction=ProwlerDestruction.valueOf(PROWLER_DESTRUCTION.get());




            pkDimensionTypes=strToDimTypeKey(PK_DIMENSIONS.get());

            try{
                insVisuals=INS_VISUALS.valueOf(INSANITY_VISUALS.get().toUpperCase());
            }
            catch(Exception e){
                insVisuals=INS_VISUALS.SIMPLE;
            }



            epIncludedCrops=strToBlocks(EXCITED_PARTICLES_BLOCK_WHITELIST.get());
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
            ready=true;

    }
    public static void refreshMobAttributes(){
        //Asteron.updateAttributesFromConfig();
        //System.out.println("new HP SCALE IS " + mobHPScale);
    }


}
