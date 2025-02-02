package com.github.celestial_awakening.datagen;

import com.github.celestial_awakening.CelestialAwakening;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class LootProvider extends GlobalLootModifierProvider {
    public LootProvider(PackOutput output) {
        super(output, CelestialAwakening.MODID);
    }

    @Override
    protected void start() {
        //add("moonstone_fishing",new LunarFishingLootModifier(new LootItemCondition[]{LootItemRandomChanceCondition.rand}));
        //add("dark_leather_modifier",new DarkLeatherLootModifier(new LootItemCondition[]{new NewMoonCondition()}));
    }
}
