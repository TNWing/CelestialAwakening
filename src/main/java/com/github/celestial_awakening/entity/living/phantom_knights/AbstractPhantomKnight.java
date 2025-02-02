package com.github.celestial_awakening.entity.living.phantom_knights;

import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
public abstract class AbstractPhantomKnight extends AbstractCALivingEntity {
    protected AbstractPhantomKnight(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public boolean canStillSenseTarget() {
        return super.canStillSenseTarget();
    }
    /*TODO
        the 3 miniboss phantom knights are these
        -Crescencia (crescent phase)
        -Gideon (gibbous phase)
        -Halsey (half)


        the big boss is
        Fulton(full)
     */
}
