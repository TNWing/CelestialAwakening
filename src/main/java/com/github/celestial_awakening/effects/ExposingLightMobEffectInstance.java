package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class ExposingLightMobEffectInstance extends MobEffectInstance {
    private int stacks;
    //amp decreases the cd btwn stack increases
    private int stackCD;
    ///rework idea #3
    public ExposingLightMobEffectInstance(MobEffect p_19513_) {
        super(p_19513_);
    }
    public int getStacks(){
        return this.stacks;
    }
    public void increaseStacks(int i){

    }
/*
    @Override
    public void applyEffect(LivingEntity livingEntity) {//not even triggerin g
        if (this.startDuration!=this.getDuration() && this.hasRemainingDuration()) {
            ((ExposingLight) this.getEffect()).applyEffectTick(livingEntity, this.getAmplifier(),this.stacks);
            stage++;
        }
    }



 */

    public int getStackCD(){
        return this.stackCD;
    }
}
