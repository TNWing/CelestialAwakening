package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.datagen.loot.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootInit {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MOD_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CelestialAwakening.MODID);

    //public static final RegistryObject<Codec<? extends IGlobalLootModifier>> DARK_LEATHER_CODEC=LOOT_SERIALIZER.register("dark_leather_modifier", DarkLeatherLootModifier.CODEC);

    //public static final RegistryObject<LootItemConditionType> NEW_MOON_COND=LOOT_COND.register("new_moon_cond",()->new NewMoonCondition());
    //public static final RegistryObject<LootItem>

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> FISH_SCALE=LOOT_MOD_SERIALIZER.register("scale_from_fish", LunarScaleFromFishLootModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> LUNAR_FISHING=
            LOOT_MOD_SERIALIZER.register("lunar_fishing", LunarFishingLootModifier.CODEC);

    public static final DeferredRegister<LootItemConditionType> LOOT_COND_SERIALIZER = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, CelestialAwakening.MODID);
    public static final RegistryObject<LootItemConditionType> ARMOR_COND=LOOT_COND_SERIALIZER.register("armor_cond",()-> new LootItemConditionType(new ArmorLootCondition.Serializer()));
    public static final RegistryObject<LootItemConditionType> DAYTIME_BOOST_COND=LOOT_COND_SERIALIZER.register("daytime_boost_cond",
            ()->new LootItemConditionType(new DaytimeBonusCondition.Serializer()));
    public static final RegistryObject<LootItemConditionType> DAYTIME_BASE_COND=
            LOOT_COND_SERIALIZER.register("daytime_base_cond",
            ()->new LootItemConditionType(new DaytimeBaseCondition.Serializer()));
    //need to make a daytime base cond for the base drop rate during day

    public static void registerLootConditions(){
        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(NewMoonCondition.class,new NewMoonCondition());
    }


    //gsonBuilder.registerTypeAdapter(MyLootCondition.class, new MyLootCondition.Serializer());
}
