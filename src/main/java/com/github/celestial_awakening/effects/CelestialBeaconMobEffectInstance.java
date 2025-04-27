package com.github.celestial_awakening.effects;

import com.github.celestial_awakening.init.MobEffectInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Optional;

public class CelestialBeaconMobEffectInstance extends MobEffectInstance {
    private int stage;
    private int startDuration;
    //TODO: move this to a capability since mc doesnt allow saving of custom data in effects
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
    public CelestialBeaconMobEffectInstance(MobEffect effect, int dura, int amp, boolean ambient, boolean visible, boolean showIcon, @Nullable MobEffectInstance hiddenEffect, Optional<FactorData> factorData) {
        super(effect,dura,amp,ambient,visible,showIcon,hiddenEffect,factorData);


    }
    @Override
    public void applyEffect(LivingEntity livingEntity) {
        if (this.startDuration!=this.getDuration() && this.hasRemainingDuration()) {
            ((CelestialBeacon) this.getEffect()).applyEffectTick(livingEntity, this.getAmplifier(),this.stage);
            stage++;
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putInt("Stage",stage);
        return tag;
    }
    //cant override, metyhod is static
    //@Override
    public static MobEffectInstance load(CompoundTag p_19561_) {
        int i = p_19561_.getByte("Id") & 0xFF;
        MobEffect mobeffect = MobEffect.byId(i);
        mobeffect = net.minecraftforge.common.ForgeHooks.loadMobEffect(p_19561_, "forge:id", mobeffect);
        return mobeffect == null ? null : null;
    }
    private boolean hasRemainingDuration() {
        return this.isInfiniteDuration() || this.getDuration() > 0;
    }

}
