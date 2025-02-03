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


    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder().comment("Celestial Awakening Config\n\nSol Cult Config\n");
    /*
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_HP_SCALE=BUILDER.comment("Multiplies each mob's base HP by this value. Default: 1").defineInRange("mob_hp_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_DMG_SCALE=BUILDER.comment("Multiplies each mob's base damage by this value. Default: 1").defineInRange("mob_dmg_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_ARMOR_PT_SCALE=BUILDER.comment("Multiplies each mob's base armor points by this value. Default: 1").defineInRange("mob_armor_pt_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> MOB_ARMOR_TOUGH_SCALE=BUILDER.comment("Multiplies each mob's base armor toughness by this value. Default: 1").defineInRange("mob_armor_t_mult",1D,1D,Double.MAX_VALUE);


    private static final ForgeConfigSpec.ConfigValue<Double> ITEM_DMG_SCALE=BUILDER.comment("Multiplies each item's base damage by this value. Default: 1").defineInRange("item_dmg_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> ARMOR_PT_SCALE=BUILDER.comment("Multiplies each armor's base armor points by this value. Default: 1").defineInRange("armor_pt_mult",1D,1D,Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> ARMOR_T_SCALE=BUILDER.comment("Multiplies each armor's base armor toughness by this value. Default: 1").defineInRange("armor_t_mult",1D,1D,Double.MAX_VALUE);


     */
    //private static final ForgeConfigSpec.ConfigValue<Integer> UPDATE_TICK_DELAY=BUILDER.comment("Represents how many ticks the Server waits for before performing a manual update on various mod components.\nWould recommend not touching this unless game\nDefault 2400 ticks(2 realtime minutes)").defineInRange("sol_cult_init_delay",2400,600,24000);



    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SOL_CULT_DIMENSIONS=BUILDER.comment("Dimensions that the Sol Cult's diviner and scout are allowed to be active in.\nDefault minecraft:overworld").defineListAllowEmpty("sol_cult_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")),obj->obj instanceof String);

    private static final ForgeConfigSpec.ConfigValue<Boolean> SOL_CULT_MULTIPLE_DIVINER=BUILDER.comment("If the Sol Cult's diviner can be active in multiple dimensions, sets whether or not the diviner being active in multiple dimensions simultaneously.\nDefault false").define("sol_cult_diviner_shared_dim",false);

    private static final ForgeConfigSpec.ConfigValue<Integer> SOL_CULT_DELAY=BUILDER.comment("Upon creating a world, block the Sol Cult from doing anything until a set amount of time has passed.\nDefault 240000 ticks(10 in game days)").defineInRange("sol_cult_init_delay",240000,0,Integer.MAX_VALUE);

    private static final ForgeConfigSpec.ConfigValue<Integer> SOL_CULT_MIN_CD=BUILDER.comment("Smallest amount of time it takes for the Sol Cult's diviner to perform another scrying. Does not restrict other factors from hastening the next scry.\nDefault 36000 ticks (1.5 in game days)").defineInRange("sol_cult_div_min_cd",36000,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Integer> SOL_CULT_MAX_CD=BUILDER.comment("Largest amount of time it takes for the Sol Cult's diviner to perform another scrying. Does not restrict other factors from impeding the next scry.\nDefault 72000 ticks (3 in game days)").defineInRange("sol_cult_div_max_cd",72000,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>>  SOL_CULT_ENEMIES=BUILDER.comment("(CURRENTLY NOT FUNCTIONAL)\nNon-player living entities that the sol cult will intentionally target.\nDefault: None.\nFormat:minecraft:zombie").defineListAllowEmpty("sol_cult_targets",new ArrayList<>(), obj->obj instanceof String);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LUNAR_MATERIAL_DIMENSIONS=BUILDER.comment("\n\nLunar Config\n\nDimensions that moonstones can spawn in.\nDefault minecraft:overworld").defineListAllowEmpty("lunar_mat_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")), obj->obj instanceof String);

    //private static final ForgeConfigSpec.ConfigValue<List<? extends String>> PK_DIMENSIONS=BUILDER.comment("Dimensions that Phantom Knights are allowed to spawn in. Default minecraft:overworld").defineListAllowEmpty("pk_dims",new ArrayList<>(Arrays.asList("minecraft:overworld")),obj->obj instanceof String);
    private static final ForgeConfigSpec.ConfigValue<Integer> PK_CRESCENCIA_MIN_DAY=BUILDER.comment("Earliest day Phantom Knight Crescencia can spawn.\nDefault: 6").defineInRange("pk_crescencia_min_day",6,0,Integer.MAX_VALUE);

    private static final ForgeConfigSpec.ConfigValue<Integer> PK_SPAWN_CAP=BUILDER.comment("Maximum number of Phantom Knights that can spawn naturally each night.\nDefault: 1").defineInRange("pk_spawn_cap",1,1,Integer.MAX_VALUE);


    //private static ForgeConfigSpec.Builder b=BUILDER.comment("The lines below are used to modify the text and colors of item descriptions.");

    static final ForgeConfigSpec SPEC =  BUILDER.build();


    public static double mobHPScale=1;
    public static double mobDmgScale=1;
    public static double mobArmorPtScale=1;
    public static double mobArmorToughnessScale=1;

    public static double itemDmgScale=1;
    public static double armorPtScale=1;
    public static double armorToughnessScale=1;

    public static boolean divinerShared;
    public static Set<ResourceKey<DimensionType>> solCultDimensionTypes;
    public static int solCultInitDelay;
    public static int solCultDivMinCD;
    public static int solCultDivMaxCD;
    public static Set<ResourceKey<EntityType<?>>> solCultTargets;

    public static int pkSpawnCap;
    public static int pkSpawnDayCD;
    public static int pkCrescenciaMinDay;
    public static Set<ResourceKey<DimensionType>> pkDimensionTypes;
    public static Set<ResourceKey<DimensionType>> lunarMatDimensionTypes;

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
        solCultDimensionTypes= strToDimTypeKey(SOL_CULT_DIMENSIONS.get());

        lunarMatDimensionTypes=strToDimTypeKey(LUNAR_MATERIAL_DIMENSIONS.get());

        divinerShared=SOL_CULT_MULTIPLE_DIVINER.get();

        solCultInitDelay=SOL_CULT_DELAY.get();

        solCultDivMinCD=SOL_CULT_MIN_CD.get();

        solCultDivMaxCD=SOL_CULT_MAX_CD.get();

        solCultTargets=strToEntities(SOL_CULT_ENEMIES.get());

        //pkDimensionTypes=strToDimTypeKey(PK_DIMENSIONS.get());

        pkCrescenciaMinDay=PK_CRESCENCIA_MIN_DAY.get()-1;

        pkSpawnCap=PK_SPAWN_CAP.get();


        /*
        mobHPScale=MOB_HP_SCALE.get();
        mobDmgScale=MOB_DMG_SCALE.get();
        mobArmorPtScale=MOB_ARMOR_PT_SCALE.get();
        mobArmorToughnessScale=MOB_ARMOR_TOUGH_SCALE.get();

        itemDmgScale= ITEM_DMG_SCALE.get();
        armorPtScale=ARMOR_PT_SCALE.get();
        armorToughnessScale=ARMOR_T_SCALE.get();

         */
    }
}
