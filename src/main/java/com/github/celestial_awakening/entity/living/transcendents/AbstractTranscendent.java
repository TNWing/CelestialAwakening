package com.github.celestial_awakening.entity.living.transcendents;

import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class AbstractTranscendent extends AbstractCALivingEntity {
    /*
how goals block other goals from being used (i think
say two goals have the same condition (if cond==true, then canuse)
presumably, each goal is check sequentially bbased on priority, so a goal will fully execute before checking another goal
 */

    protected AbstractTranscendent(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void defineSynchedData(){
        super.defineSynchedData();
    }

    protected void registerGoals(){
        super.registerGoals();

    }

    public boolean canStillSenseTarget(){
        LivingEntity target=this.getTarget();
        return target.isCurrentlyGlowing() || super.canStillSenseTarget();
    }


}
