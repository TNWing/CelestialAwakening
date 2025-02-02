package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AbilityCDMobEffect extends MobEffect {
    //TODO: decide on mobeffect or cap data
    /*
    MobEffect
    Pros: generally O(1) lookup,
    Cons: can potentially have worse lookup time if lots of effects

    Cap Data
    Pros: guarenteed O(1) lookup
    Cons: have to define unique variables for each abilitycd, which can add to storage problems, also looks messy;
    manual need to decrement CD;
    syncing cap data can be tedious/less performant (although may not need to sync data that often so this point may not be a problem)


     */
    public boolean keepOnDeath=true;
    protected AbilityCDMobEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }
}
