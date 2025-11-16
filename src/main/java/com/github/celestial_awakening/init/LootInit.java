package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LootInit {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_SERIALIZER=
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CelestialAwakening.MODID);
    private static final DeferredRegister<LootItemConditionType> LOOT_COND = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, CelestialAwakening.MODID);
    //public static final RegistryObject<Codec<? extends IGlobalLootModifier>> DARK_LEATHER_CODEC=LOOT_SERIALIZER.register("dark_leather_modifier", DarkLeatherLootModifier.CODEC);

    //public static final RegistryObject<LootItemConditionType> NEW_MOON_COND=LOOT_COND.register("new_moon_cond",()->new NewMoonCondition());
    //public static final RegistryObject<LootItem>
    public static void registerLootConditions(){
        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(NewMoonCondition.class,new NewMoonCondition());
    }


    //gsonBuilder.registerTypeAdapter(MyLootCondition.class, new MyLootCondition.Serializer());
}
