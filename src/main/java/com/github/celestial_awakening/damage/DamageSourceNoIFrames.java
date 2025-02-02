package com.github.celestial_awakening.damage;

import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DamageSourceNoIFrames extends DamageSource {
    public int invulTicks;
//does not apply iframes but still respects them
    public DamageSourceNoIFrames(Holder.Reference<DamageType> holderOrThrow, Entity entity) {
        super(holderOrThrow,entity);
    }
    @Override
    public boolean is(TagKey<DamageType> tagKey) {

        return this.typeHolder().is(tagKey);
    }

}
