package com.github.celestial_awakening.util;

import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class EntityFuncs {
    public static boolean isInCombat(LivingEntity livingEntity){

        return ObfuscationReflectionHelper.getPrivateValue(CombatTracker.class,livingEntity.getCombatTracker(),"f_19281_");
    }
}
