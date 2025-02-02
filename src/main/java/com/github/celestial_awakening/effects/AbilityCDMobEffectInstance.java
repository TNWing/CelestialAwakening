package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class AbilityCDMobEffectInstance extends MobEffectInstance {
    //no need for custom effectinstance? just make a bunch of unique instances per ability? will need a custom mob effect to ensure cd dont reset on death
    private String abilityName;
    public AbilityCDMobEffectInstance(MobEffect p_19513_) {
        super(p_19513_);
    }
}
