package com.github.celestial_awakening.damage;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class DamageSourceNoIFrames extends DamageSource {
    public int invulTicks;
//does not apply iframes but still respects them
    public DamageSourceNoIFrames(Holder.Reference<DamageType> holderOrThrow, Entity entity) {
        super(holderOrThrow,entity);
    }
    public DamageSourceNoIFrames(Holder.Reference<DamageType> holderOrThrow, Entity causingEntity,Entity directEntity) {
        super(holderOrThrow,causingEntity,directEntity);
    }
    @Override
    public boolean is(TagKey<DamageType> tagKey) {

        return this.typeHolder().is(tagKey);
    }

}
