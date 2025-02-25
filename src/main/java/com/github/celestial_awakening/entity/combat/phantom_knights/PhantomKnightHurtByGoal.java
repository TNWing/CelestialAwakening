package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.living.phantom_knights.AbstractPhantomKnight;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class PhantomKnightHurtByGoal extends HurtByTargetGoal {
    public PhantomKnightHurtByGoal(AbstractPhantomKnight p_26039_, Class<?>... p_26040_) {
        super(p_26039_, p_26040_);
    }

    @Override
    public void start() {
        super.start();
        //(AbstractPhantomKnight)mob
    }
}
