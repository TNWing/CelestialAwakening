package com.github.celestial_awakening;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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


    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder().comment("Celestial Awakening Config\n\nTranscendents Config\n");
/*
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_HP_SCALE=BUILDER.comment("Multiplies each mob's base HP by this value. Default: 1").defineInRange("mob_hp_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_DMG_SCALE=BUILDER.comment("(CURRENTLY NOT FUNCTIONAL)Multiplies each mob's base damage by this value. Default: 1").defineInRange("mob_dmg_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_ARMOR_PT_SCALE=BUILDER.comment("Multiplies each mob's base armor points by this value. Default: 1").defineInRange("mob_armor_pt_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_ARMOR_TOUGH_SCALE=BUILDER.comment("Multiplies each mob's base armor toughness by this value. Default: 1").defineInRange("mob_armor_t_mult",1D,1D,Double.MAX_VALUE);


    private static final ForgeConfigSpec.ConfigValue<Double> ITEM_DMG_SCALE=BUILDER.comment("Multiplies each item's base damage by this value. Default: 1").defineInRange("item_dmg_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> ARMOR_PT_SCALE=BUILDER.comment("Multiplies each armor's base armor points by this value. Default: 1").defineInRange("armor_pt_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> ARMOR_T_SCALE=BUILDER.comment("Multiplies each armor's base armor toughness by this value. Default: 1").defineInRange("armor_t_mult",1D,1D,Double.MAX_VALUE);



 */

    //private static final ForgeConfigSpec.ConfigValue<Integer> UPDATE_TICK_DELAY=BUILDER.comment("Represents how many ticks the Server waits for before performing a manual update on various mod components.\nWould recommend not touching this unless game\nDefault 2400 ticks(2 realtime minutes)").defineInRange("sol_cult_init_delay",2400,600,24000);



    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TRANSCENDENTS_DIMENSIONS =BUILDER.comment("Dimensions that the transcendents are allowed to be active in.\nDefault minecraft:overworld").defineListAllowEmpty("transcendents_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")), obj->obj instanceof String);

    private static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_MULTIPLE_DIVINER =BUILDER.comment("If the Transcendents's diviner can be active in multiple dimensions, sets whether or not the diviner being active in multiple dimensions simultaneously.\nDefault false").define("transcendents_diviner_shared_dim",false);

    private static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DELAY =BUILDER.comment("Upon creating a world, block the Transcendents from doing anything until a set amount of time has passed.\nDefault 240000 ticks(10 in game days)").defineInRange("transcendents_init_delay",240000,0,Integer.MAX_VALUE);

    private static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_MIN_CD =BUILDER.comment("Smallest amount of time it takes for the Transcendents's diviner to perform another scrying. Does not restrict other factors from hastening the next scry.\nDefault 36000 ticks (1.5 in game days)").defineInRange("transcendents_div_min_cd",36000,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_MAX_CD =BUILDER.comment("Largest amount of time it takes for the Transcendents's diviner to perform another scrying. Does not restrict other factors from impeding the next scry.\nDefault 72000 ticks (3 in game days)").defineInRange("transcendents_div_max_cd",72000,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TRANSCENDENTS_ENEMIES =BUILDER.comment("(CURRENTLY NOT FUNCTIONAL)\nNon-player living entities that the transcendents will intentionally target.\nDefault: None.\nFormat:minecraft:zombie").defineListAllowEmpty("transcendents_targets",new ArrayList<>(), obj->obj instanceof String);

    private static final ForgeConfigSpec.ConfigValue<Boolean> TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS =BUILDER.comment("Determines whether or not the diviner's heatwave can modify the terrain.\nDefault true").define("transcendents_diviner_heatwave",true);

    private static final ForgeConfigSpec.ConfigValue<Integer> TRANSCENDENTS_DIVINER_SCAN_POWER_INCREASE =BUILDER.comment("The amount of power the diviner gets for each entity scanned.\nDefault 10").defineInRange("transcendents_div_scan_power",10,0,100);




    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LUNAR_MATERIAL_DIMENSIONS=BUILDER.comment("\n\nLunar Config\n\nDimensions that moonstones can spawn in.\nDefault minecraft:overworld").defineListAllowEmpty("lunar_mat_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")), obj->obj instanceof String);

    //private static final ForgeConfigSpec.ConfigValue<List<? extends String>> PK_DIMENSIONS=BUILDER.comment("Dimensions that Phantom Knights are allowed to spawn in. Default minecraft:overworld").defineListAllowEmpty("pk_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")),obj->obj instanceof String);
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_CRESCENCIA_MIN_DAY=BUILDER.comment("Earliest day Phantom Knight Crescencia can spawn.\nDefault: 6").defineInRange("pk_crescencia_min_day",6,0,Integer.MAX_VALUE);

    private static final ForgeConfigSpec.ConfigValue<Integer> PK_SPAWN_CAP=BUILDER.comment("Maximum number of Phantom Knights that can spawn naturally each night.\nDefault: 1").defineInRange("pk_spawn_cap",1,1,Integer.MAX_VALUE);

    private static final ForgeConfigSpec.ConfigValue<Integer> PK_RMG_RES_DIST=BUILDER.comment("The max distance between a phantom knight and an attacker before the attack's damage output gets reduced\nDefault: 15").defineInRange("pk_dmg_res_dist",1,1,Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue HONOR_DUEL_DIST=BUILDER.comment("\n\nArmor Config\n\n").comment("The maximum number of blocks between two entities linked entities linked by honor duel before the link breaks.\nDefault: 25").defineInRange("honor_duel_dist",25,1,(int)Integer.MAX_VALUE);



    static final ForgeConfigSpec SPEC =  BUILDER.build();


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
    public static Set<ResourceKey<EntityType<?>>> transcendentsTargets;


    public static int pkSpawnCap;
    public static int pkSpawnDayCD;
    public static int pkDmgResDist;
    public static int pkCrescenciaMinDay;
    public static Set<ResourceKey<DimensionType>> pkDimensionTypes;
    public static Set<ResourceKey<DimensionType>> lunarMatDimensionTypes;


    public static double honorDuelDist;

    static Set<ResourceKey<DimensionType>> strToDimTypeKey(List<? extends String> list){
        return list.stream()
                .map(obj-> ResourceKey.create(Registries.DIMENSION_TYPE,new ResourceLocation(obj)))
                .collect(Collectors.toSet());
    }

    static Set<ResourceKey<EntityType<?>>> strToEntities(List<? extends String> list){
        return list.stream().map(
          obj->ResourceKey.create(Registries.ENTITY_TYPE,new ResourceLocation(obj)))
                .filter(key->{EntityType<?> type= ForgeRegistries.ENTITY_TYPES.getValue(key.location());
                return type!=null && LivingEntity.class.isAssignableFrom(type.getBaseClass());
                })
                .collect(Collectors.toSet());
    }
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        transcendentsDimensionTypes = strToDimTypeKey(TRANSCENDENTS_DIMENSIONS.get());

        lunarMatDimensionTypes=strToDimTypeKey(LUNAR_MATERIAL_DIMENSIONS.get());

        divinerShared= TRANSCENDENTS_MULTIPLE_DIVINER.get();

        transcendentsInitDelay = TRANSCENDENTS_DELAY.get();

        transcendentsDivMinCD = TRANSCENDENTS_MIN_CD.get();

        transcendentsDivMaxCD = TRANSCENDENTS_MAX_CD.get();

        transcendentsTargets =strToEntities(TRANSCENDENTS_ENEMIES.get());

        divinerHeatWaveBlockMod=TRANSCENDENTS_DIVINER_HEATWAVE_AFFECTS_BLOCKS.get();


        //pkDimensionTypes=strToDimTypeKey(PK_DIMENSIONS.get());

        pkCrescenciaMinDay=PK_CRESCENCIA_MIN_DAY.get()-1;

        pkSpawnCap=PK_SPAWN_CAP.get();

        pkDmgResDist=PK_RMG_RES_DIST.get();

        honorDuelDist= HONOR_DUEL_DIST.get();


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
        System.out.println("new HP SCALE IS " + mobHPScale);
    }
}
