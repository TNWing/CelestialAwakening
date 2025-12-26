package com.github.celestial_awakening.entity.combat;

import net.minecraft.world.entity.Mob;

public class CoreGuardianDeepYTargetGoal extends CANearestAttackableTargetGoal{
    public CoreGuardianDeepYTargetGoal(Mob p_26060_, Class p_26061_, boolean p_26062_) {
        super(p_26060_, p_26061_, p_26062_);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public boolean canUse() {
        return super.canUse() && target.getY()<=this.mob.level().getMinBuildHeight()+20;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }
}
