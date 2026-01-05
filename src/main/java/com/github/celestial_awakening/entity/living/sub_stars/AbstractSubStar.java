package com.github.celestial_awakening.entity.living.sub_stars;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;

import java.util.List;

public class AbstractSubStar extends AbstractCAMonster {

    int ticksOnFire;
    float size;
    int tickReq;
    protected AbstractSubStar(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public void updateAnim() {

    }
    public void tick(){

        if (this.tickCount%40==0){
            AABB aabb=new AABB(this.blockPosition()).inflate(size);
            List<Entity> entityList=this.level().getEntitiesOfClass(Entity.class,aabb,(e)->!e.fireImmune());
            for (Entity entity:entityList) {
                entity.setRemainingFireTicks(Math.max(ticksOnFire,entity.getRemainingFireTicks()));
            }
        }
        super.tick();
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public boolean canDrownInFluidType(FluidType type){
        return false;
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amt) {
        if (!(source.is(DamageTypeTags.IS_FREEZING)
            || source.getEntity() instanceof Snowball
                || source.is(DamageTypeTags.IS_DROWNING)
        )){

        }
        else{
            super.actuallyHurt(source,amt);
        }
    }
}
