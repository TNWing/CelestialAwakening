package com.github.celestial_awakening.datagen.loot;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class NewMoonCondition implements LootItemCondition {
    @Override
    public LootItemConditionType getType() {
        return null;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ServerLevel level=lootContext.getLevel();
        if (level.getMoonPhase()==5 && level.dimensionTypeId()== BuiltinDimensionTypes.OVERWORLD && level.isNight()){
            return true;
        }
        return false;
    }
}
