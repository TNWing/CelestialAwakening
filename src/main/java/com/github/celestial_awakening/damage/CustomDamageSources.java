package com.github.celestial_awakening.damage;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class CustomDamageSources {
    private final Registry<DamageType> damageTypes;
    public CustomDamageSources(RegistryAccess p_270740_) {
        this.damageTypes = p_270740_.registryOrThrow(Registries.DAMAGE_TYPE);
    }

    /*
       public DamageSource mobAttack(LivingEntity p_270357_) {
      return this.source(DamageTypes.MOB_ATTACK, p_270357_);
   }
     */

    private DamageSource source(ResourceKey<DamageType> p_270957_) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(p_270957_));
    }

    private DamageSource source(ResourceKey<DamageType> p_270142_, @Nullable Entity p_270696_) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(p_270142_), p_270696_);
    }
    private DamageSourceNoIFrames noIFramesSource(ResourceKey<DamageType> p_270957_,LivingEntity entity) {
        return new DamageSourceNoIFrames(this.damageTypes.getHolderOrThrow(p_270957_),entity);
    }
    private DamageSource source(ResourceKey<DamageType> p_270076_, @Nullable Entity p_270656_, @Nullable Entity p_270242_) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(p_270076_), p_270656_, p_270242_);
    }

    public DamageSource glacier(LivingEntity entity){
        return this.source(DamageTypes.CRAMMING,entity);
    }
    public DamageSource frostburn(LivingEntity entity){
        return this.source(DamageTypes.FREEZE,entity);
    }
}
