package com.github.celestial_awakening.entity.living.night_prowlers;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.events.raids.ProwlerRaid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.HashMap;

public abstract class AbstractNightProwler extends AbstractCAMonster {
    private static final EntityDataAccessor<Integer> INFUSE = SynchedEntityData.defineId(AbstractNightProwler.class, EntityDataSerializers.INT);
    protected HashMap<Integer,AnimationState> actionIDToAnimMap=new HashMap();
    public AABB standardAABB;
    @Nullable
    protected ProwlerRaid raid;
    protected int raidValue;
    public AbstractNightProwler(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(INFUSE,0);
    }
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(INFUSE,tag.getInt("Infuse"));
        this.setRaidValue(tag.getInt("RVal"));
        if (tag.contains("rID")){
            if (this.level() instanceof ServerLevel) {
                LazyOptional<LevelCapability> optional=this.level().getCapability(LevelCapabilityProvider.LevelCap);
                optional.ifPresent(cap->
                    {
                        this.raid=cap.raids.getProwlerRaidFromID(tag.getInt("rID"));
                    }
                );
            }
            if(this.raid!=null){
                /*
                add to raid
                 */
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Infuse",this.entityData.get(INFUSE));
        tag.putInt("RVal", raidValue);

        if (this.raid != null) {
            tag.putInt("rID", this.raid.getRaidID());
        }

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

    public int getInfuse(){
        return this.entityData.get(INFUSE);
    }

    public int getRaidValue(){
        return this.raidValue;
    }

    public void setRaidValue(int w){
        this.raidValue =w;
    }
}
