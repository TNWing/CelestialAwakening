package com.github.celestial_awakening.entity.living.planetary_guardians;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoltenGuardian extends AbstractGuardian {
    protected MoltenGuardian(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D, 60));
    }
    public boolean okTarget(@Nullable LivingEntity p_32396_) {
        if (p_32396_ != null) {
            return p_32396_.isInLava() || false;//is near lava?
        } else {
            return false;
        }
    }
}
