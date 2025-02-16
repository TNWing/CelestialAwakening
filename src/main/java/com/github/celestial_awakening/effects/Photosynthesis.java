package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Photosynthesis extends MobEffect {
    public Photosynthesis(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }
    /*
    3 min:
     */
    @Override
    public boolean isDurationEffectTick(int ticks, int amplifier) {
        int k=400>>amplifier;
        if (k>0){
            return ticks%k==1;
        }
        else{
            return true;
        }
    }

    @Override
    public void applyEffectTick(LivingEntity target, int amplifier) {
        if (target instanceof Player){
            Player player= (Player) target;
            player.getFoodData().eat(1,0);
        }
    }
}
