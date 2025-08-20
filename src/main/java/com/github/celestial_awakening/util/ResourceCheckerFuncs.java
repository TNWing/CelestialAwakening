package com.github.celestial_awakening.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.Set;

public class ResourceCheckerFuncs {
    public static boolean validDim(Level level, Set<ResourceKey<DimensionType>> dimSet) {
        return dimSet.contains(level.dimensionTypeId());
    }
    public static boolean validEntityType(LivingEntity entity, Set<EntityType<?>> eTypeSet) {
        return eTypeSet.contains(entity.getType());
        //return dimSet.contains(level.dimensionTypeId());
    }
}
