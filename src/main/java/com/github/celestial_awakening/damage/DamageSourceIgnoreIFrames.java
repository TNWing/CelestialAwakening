package com.github.celestial_awakening.damage;

import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DamageSourceIgnoreIFrames extends DamageSourceNoIFrames {
//damage source that doesn't trigger iframe or respects them
    public DamageSourceIgnoreIFrames(Holder.Reference<DamageType> holderOrThrow) {
        super(holderOrThrow);
    }
    public DamageSourceIgnoreIFrames(Holder.Reference<DamageType> holderOrThrow, Entity entity) {
        super(holderOrThrow,entity);
    }
    public DamageSourceIgnoreIFrames(Holder.Reference<DamageType> holderOrThrow, Entity causing, Entity entity) {
        super(holderOrThrow,causing,entity);
        //so after set owner, THE BYPASS SHIELD BECCOMES TRUE
        //System.out.println("ENT WITH ID " + causing.getId() + " can BYPASS SHIELD " + this.is(DamageTypeTags.BYPASSES_SHIELD) + " ON SIDE client? " + causing.level().isClientSide);

    }
    public DamageSourceIgnoreIFrames(Holder.Reference<DamageType> holderOrThrow, Entity causingEntity, Entity directEntity, Vec3 vec) {
        super(holderOrThrow,causingEntity,directEntity,vec);
    }
    @Override
    public boolean is(TagKey<DamageType> tagKey) {
        if (tagKey== DamageTypeTags.BYPASSES_COOLDOWN){
            return true;
        }
        return this.typeHolder().is(tagKey);
    }
}
