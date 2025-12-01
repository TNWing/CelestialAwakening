package com.github.celestial_awakening.entity.living.planetary_guardians;

import com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian.CoreGuardianCombatAIGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class CoreGuardian extends AbstractGuardian{
    protected CoreGuardian(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(5,new CoreGuardianCombatAIGoal(this));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D, 60));
    }
}
