package com.github.celestial_awakening.effects;

import com.github.celestial_awakening.init.MobEffectInit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class CelestialBeaconMobEffectInstance extends MobEffectInstance {
    private int stage;
    private int startDuration;
    public CelestialBeaconMobEffectInstance(MobEffect p_19513_) {
        super(p_19513_);
        this.startDuration=this.getDuration();
        this.stage=1;
    }
    public CelestialBeaconMobEffectInstance(int duration) {
        super(MobEffectInit.CELESTIAL_BEACON.get(),duration);
        this.startDuration=duration;
        this.stage=1;
    }
    public CelestialBeaconMobEffectInstance(int duration,int amp) {
        super(MobEffectInit.CELESTIAL_BEACON.get(),duration,amp);
        this.startDuration=duration;
        this.stage=1;
    }
    public CelestialBeaconMobEffectInstance(int duration,int amp,int stage) {
        super(MobEffectInit.CELESTIAL_BEACON.get(),duration,amp);
        this.startDuration=duration;
        this.stage=stage;
    }
    @Override
    public void applyEffect(LivingEntity livingEntity) {
        if (this.startDuration!=this.getDuration() && this.hasRemainingDuration()) {
            ((CelestialBeacon) this.getEffect()).applyEffectTick(livingEntity, this.getAmplifier(),this.stage);
            stage++;
        }
    }

    private boolean hasRemainingDuration() {
        return this.isInfiniteDuration() || this.getDuration() > 0;
    }

}
