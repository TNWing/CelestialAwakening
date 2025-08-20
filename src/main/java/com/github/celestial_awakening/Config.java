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

    static final ForgeConfigSpec.ConfigValue<List<? extends String>> TRANSCENDENTS_DIMENSIONS;
    static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_MULTIPLE_DIVINER;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DELAY;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_MIN_CD;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_MAX_CD;
    static final ForgeConfigSpec.ConfigValue<List<? extends String>> TRANSCENDENTS_ENEMIES;
    static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS;
    static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DIVINER_SCAN_POWER_INCREASE;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LUNAR_MATERIAL_DIMENSIONS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> PK_DIMENSIONS;
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_CRESCENCIA_MIN_DAY;
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_SPAWN_CAP;
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_RMG_RES_DIST;
    private static final ForgeConfigSpec.ConfigValue<Boolean> PK_DAY_DESPAWN;
    private static final ForgeConfigSpec.IntValue HONOR_DUEL_DIST;
    private static final ForgeConfigSpec.DoubleValue PHOTON_CYCLE_FIRE_MULT;
    private static final ForgeConfigSpec.DoubleValue PHOTON_CYCLE_GLOW_MULT;
    private static final ForgeConfigSpec.DoubleValue PHOTON_CYCLE_FINAL_MULT;

    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_IRON_DMG_MULT;
    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_IRON_ATK_SPD_MULT;
    private static final ForgeConfigSpec.DoubleValue MIDNIGHT_IRON_MINING_SPD_MULT;

    static{
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Celestial Awakening Config");

        builder.push("Global_Enemy_Config");
            OUTOFCOMBAT_HEAL=builder.comment("Allows some enemies to heal when not in combat.\nDefault: true").define("enemy_combat_regen",true);
        builder.pop();


        builder.push("Transcendents_Config");
            TRANSCENDENTS_DIMENSIONS =builder.comment("Dimensions that the Transcendents are allowed to be active in.\nDefault minecraft:overworld").defineListAllowEmpty("transcendents_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")), obj->obj instanceof String);
            TRANSCENDENTS_MULTIPLE_DIVINER=builder.comment("If the Transcendents's diviner can be active in multiple dimensions, sets whether or not the diviner being active in multiple dimensions simultaneously.\nDefault false").define("transcendents_diviner_shared_dim",false);
            TRANSCENDENTS_DELAY=builder.comment("Upon creating a world, block the Transcendents from doing anything until a set amount of time has passed.\nDefault 240000 ticks(10 in game days)").defineInRange("transcendents_init_delay",240000,0,Integer.MAX_VALUE);
            TRANSCENDENTS_MIN_CD =builder.comment("Minimum cooldown for the diviner's scrying. Does not restrict other factors from hastening the next scry.\nDefault 36000 ticks (1.5 in game days)").defineInRange("transcendents_div_min_cd",36000,0,Integer.MAX_VALUE);
            TRANSCENDENTS_MAX_CD =builder.comment("Maximum cooldown for the diviner's scrying. Does not restrict other factors from impeding the next scry.\nDefault 72000 ticks (3 in game days)").defineInRange("transcendents_div_max_cd",72000,0,Integer.MAX_VALUE);
            TRANSCENDENTS_ENEMIES =builder.comment("List of living entities that the Transcendents will intentionally target.\nDefault: minecraft:player.\nFormat:minecraft:zombie").defineListAllowEmpty("transcendents_targets",new ArrayList<>(Arrays.asList("minecraft:player")), obj->obj instanceof String);
            TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS =builder.comment("Determines whether or not the diviner's heatwave can modify the terrain.\nDefault true").define("transcendents_diviner_heatwave",true);
            TRANSCENDENTS_DIVINER_SCAN_POWER_INCREASE =builder.comment("The amount of power the diviner gets for each entity scanned.\nDefault 10").defineInRange("transcendents_div_scan_power",10,0,100);
        builder.pop();

        builder.push("Lunar_Config");
            LUNAR_MATERIAL_DIMENSIONS=builder.comment("Dimensions that moonstones can spawn in.\nDefault minecraft:overworld").defineListAllowEmpty("lunar_mat_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")), obj->obj instanceof String);
            PK_DIMENSIONS=builder.comment("Dimensions that Phantom Knights are allowed to spawn in. Default minecraft:overworld").defineListAllowEmpty("pk_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")),obj->obj instanceof String);
            PK_CRESCENCIA_MIN_DAY=builder.comment("Earliest day Phantom Knight Crescencia can spawn.\nDefault: 6").defineInRange("pk_crescencia_min_day",6,0,Integer.MAX_VALUE);
            PK_SPAWN_CAP=builder.comment("Maximum number of Phantom Knights that can spawn naturally each night.\nDefault: 1").defineInRange("pk_spawn_cap",1,1,100);
            PK_RMG_RES_DIST=builder.comment("The max distance between a Phantom Knight and an attacker before the attack's damage output gets reduced\nDefault: 15").defineInRange("pk_dmg_res_dist",15,1,100);
            PK_DAY_DESPAWN=builder.comment("Determines if phantom knights despawn during the day (this also prevents them from spawning during the day.\nDefault:true").define("pk_day_despawn",true);
        builder.pop();
        builder.push("Armor_Config");
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
                MIDNIGHT_IRON_DMG_MULT=builder.comment("The damage multiplier midnight iron tools receive at night.\nDefault:1.15").defineInRange("midnight_iron_dmg_mult",1.15d,1,100);
                MIDNIGHT_IRON_ATK_SPD_MULT=builder.comment("The attack speed multiplier midnight iron tools receive at night. Lower values means faster attack speed.\nDefault: 0.95").defineInRange("midnight_iron_atk_spd_mult",0.95d,0.1,1);
                MIDNIGHT_IRON_MINING_SPD_MULT=builder.comment("The mining speed multiplier midnight iron tools receive at night.\nDefault: 1.2").defineInRange("midnight_iron_mining_spd_mult",1.2d,1,100);
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


    public static double mobHPScale=1;
    public static double mobDmgScale=1;
    public static double mobArmorPtScale=1;
    public static double mobArmorToughnessScale=1;

    public static double itemDmgScale=1;
    public static double armorPtScale=1;
    public static double armorToughnessScale=1;

    public static boolean divinerShared;
    public static boolean divinerHeatWaveBlockMod;
    public static int divinerScanPower;
    public static Set<ResourceKey<DimensionType>> transcendentsDimensionTypes;
    public static int transcendentsInitDelay;
    public static int transcendentsDivMinCD;
    public static int transcendentsDivMaxCD;
    public static Set<EntityType<?>> transcendentsTargets;


    public static int pkSpawnCap;
    public static int pkSpawnDayCD;
    public static int pkDmgResDist=15;
    public static int pkCrescenciaMinDay;
    public static Set<ResourceKey<DimensionType>> pkDimensionTypes;
    public static boolean pkDayDespawn;
    public static Set<ResourceKey<DimensionType>> lunarMatDimensionTypes;


    public static double honorDuelDist;

    public static double photonCycleFireMult;
    public static double photonCycleGlowMult;
    public static double photonCycleFinalMult;

    public static double midnightIronDmgMult;
    public static double midnightIronAtkSpdMult;
    public static double midnightIronMiningSpdMult;

    public static boolean useVanillaTeams;
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
        mobCombatRegen= OUTOFCOMBAT_HEAL.get();

        transcendentsDimensionTypes = strToDimTypeKey(TRANSCENDENTS_DIMENSIONS.get());

        lunarMatDimensionTypes=strToDimTypeKey(LUNAR_MATERIAL_DIMENSIONS.get());

        divinerShared= TRANSCENDENTS_MULTIPLE_DIVINER.get();

        transcendentsInitDelay = TRANSCENDENTS_DELAY.get();

        transcendentsDivMinCD = TRANSCENDENTS_MIN_CD.get();

        transcendentsDivMaxCD = TRANSCENDENTS_MAX_CD.get();

        transcendentsTargets =strToEntities(TRANSCENDENTS_ENEMIES.get());


        divinerHeatWaveBlockMod=TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS.get();

        divinerScanPower=TRANSCENDENTS_DIVINER_SCAN_POWER_INCREASE.get();

        //pkDimensionTypes=strToDimTypeKey(PK_DIMENSIONS.get());

        pkCrescenciaMinDay=PK_CRESCENCIA_MIN_DAY.get()-1;

        pkSpawnCap=PK_SPAWN_CAP.get();

        pkDmgResDist=PK_RMG_RES_DIST.get();

        pkDayDespawn=PK_DAY_DESPAWN.get();

        honorDuelDist= HONOR_DUEL_DIST.get();

        photonCycleFireMult=PHOTON_CYCLE_FIRE_MULT.get();
        photonCycleGlowMult=PHOTON_CYCLE_GLOW_MULT.get();
        photonCycleFinalMult=PHOTON_CYCLE_FINAL_MULT.get();

        midnightIronDmgMult=MIDNIGHT_IRON_DMG_MULT.get();
        midnightIronAtkSpdMult =MIDNIGHT_IRON_ATK_SPD_MULT.get();
        midnightIronMiningSpdMult=MIDNIGHT_IRON_MINING_SPD_MULT.get();
        /*
        mobHPScale=MOB_HP_SCALE.get();
        mobDmgScale=MOB_DMG_SCALE.get();
        mobArmorPtScale=MOB_ARMOR_PT_SCALE.get();
        mobArmorToughnessScale=MOB_ARMOR_TOUGH_SCALE.get();

        itemDmgScale= ITEM_DMG_SCALE.get();
        armorPtScale=ARMOR_PT_SCALE.get();
        armorToughnessScale=ARMOR_T_SCALE.get();

         */
        refreshMobAttributes();

    }

    public static void refreshMobAttributes(){
        //Asteron.updateAttributesFromConfig();
        //System.out.println("new HP SCALE IS " + mobHPScale);
    }
}
