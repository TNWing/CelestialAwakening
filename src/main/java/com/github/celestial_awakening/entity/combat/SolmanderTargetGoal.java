package com.github.celestial_awakening.entity.combat;

import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.scores.Team;

/*
functions like TargetGoal with the following differences
-reads mob data to determine if the mob can detect targets through walls
 */
public class SolmanderTargetGoal extends CANearestAttackableTargetGoal {

    public SolmanderTargetGoal(Mob p_26060_, Class p_26061_, boolean p_26062_) {
        super(p_26060_, p_26061_, p_26062_);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.mob.level().isDay();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }
}
