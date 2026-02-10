package com.github.celestial_awakening.entity.living.transcendents;

import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class AbstractTranscendent extends AbstractCAMonster {
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
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F;
    }
    protected SoundEvent getHurtSound(DamageSource p_30424_) {
        return SoundEvents.HOSTILE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

}
