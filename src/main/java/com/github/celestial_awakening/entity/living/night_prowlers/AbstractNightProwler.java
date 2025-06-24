package com.github.celestial_awakening.entity.living.night_prowlers;

import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public abstract class AbstractNightProwler extends AbstractCAMonster {
    protected HashMap<Integer,AnimationState> actionIDToAnimMap=new HashMap();
    public AABB standardAABB;

    public AbstractNightProwler(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    public void travel(Vec3 vec3){
        super.travel(vec3);
    }
    public void tick() {
        super.tick();
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        //super.updateWalkAnimation(pPartialTick);

        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }
    @Override
    public void setDeltaMovement(Vec3 dm){
        super.setDeltaMovement(dm);
    }


    @Override
    public boolean hurt(DamageSource source, float amt){
        return super.hurt(source,amt);
    }
}
