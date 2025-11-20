package com.github.celestial_awakening.entity.living.solmanders;

import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class AbstractSolmander extends AbstractCAMonster {
    protected AbstractSolmander(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public void updateAnim() {

    }
}
