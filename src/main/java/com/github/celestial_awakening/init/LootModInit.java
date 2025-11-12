package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.datagen.loot.LunarFishingLootModifier;
import com.github.celestial_awakening.datagen.loot.LunarScaleFromFishLootModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootModInit {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MOD_SERIALIZER=
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CelestialAwakening.MODID);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> MOONSTONE_FISHING=
            LOOT_MOD_SERIALIZER.register("moonstone_fishing", LunarFishingLootModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> FISH_SCALE=LOOT_MOD_SERIALIZER.register("scale_from_fish", LunarScaleFromFishLootModifier.CODEC);

}
