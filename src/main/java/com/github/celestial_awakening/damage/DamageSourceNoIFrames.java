package com.github.celestial_awakening.damage;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DamageSourceNoIFrames extends DamageSource {
    public int invulTicks;
//does not apply iframes but still respects them
    public DamageSourceNoIFrames(Holder.Reference<DamageType> holderOrThrow, Entity entity) {
        super(holderOrThrow,entity);
    }
    public DamageSourceNoIFrames(Holder.Reference<DamageType> holderOrThrow, Entity causingEntity,Entity directEntity) {
        super(holderOrThrow,causingEntity,directEntity);
    }
    public DamageSourceNoIFrames(Holder.Reference<DamageType> holderOrThrow, Entity causingEntity, Entity directEntity, Vec3 vec) {
        super(holderOrThrow,causingEntity,directEntity,vec);
    }
    @Override
    public boolean is(TagKey<DamageType> tagKey) {

        return this.typeHolder().is(tagKey);
    }

}
