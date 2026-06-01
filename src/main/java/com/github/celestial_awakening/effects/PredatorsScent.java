package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class PredatorsScent extends MobEffect {
    protected PredatorsScent(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity target, int amplifier) {
        AABB box=null;
        TargetingConditions conds=null;
        List<Mob> entitiesToPanic=target.level().getNearbyEntities(Mob.class,null,target,null);
        for (Mob entity:entitiesToPanic) {
            boolean bool=entity.goalSelector.getAvailableGoals().stream().anyMatch(wrappedGoal -> wrappedGoal.getGoal() instanceof PanicGoal);
            //entity.goalSelector
            if (bool){
                entity.setLastHurtByMob(target);
            }
            //entity.selec
        }
    }
}
