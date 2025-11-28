package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

import static com.github.celestial_awakening.nbt_strings.ProjDataNBTNames.pd_HolderName;

public class ShiningOrb extends CA_Projectile {

    double explosionRadius =2.25D;
    private static final EntityDataAccessor<Boolean> SHOULD_EXPLODE= SynchedEntityData.defineId(ShiningOrb.class, EntityDataSerializers.BOOLEAN);
    public ShiningOrb(EntityType<ShiningOrb> shiningOrbEntityType, Level level) {
        super(shiningOrbEntityType,level,80);
        damagesource=this.level().damageSources().indirectMagic(this,null);//new DamageSource(,null);
        this.setNoGravity(true);
        life=0;
    }


    public static ShiningOrb create(Level level,int lt,float spd,float hA,float vA,float dmg) {
        ShiningOrb entity = new ShiningOrb(EntityInit.SHINING_ORB.get(), level);
        entity.setNoGravity(true);
        entity.setLifetime(lt);
        entity.setMoveValues(spd,hA,vA);
        entity.setDmg(dmg);
        return entity;
    }
    @Override
    public void setOwner(Entity e){
        super.setOwner(e);
        if (e instanceof LivingEntity){
            pred=CA_Predicates.opposingTeams_IgnoreSameClass_Predicate((LivingEntity) e);
        }

        damagesource=this.level().damageSources().indirectMagic(this,e);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOULD_EXPLODE,true);
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        CompoundTag data=tag.getCompound(pd_HolderName);
        this.entityData.set(SHOULD_EXPLODE,data.getBoolean("Explode"));
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {//TODO: check to make sure this is fine
        super.addAdditionalSaveData(tag);
        CompoundTag data=tag.getCompound(pd_HolderName);
        data.putBoolean("Explode",this.entityData.get(SHOULD_EXPLODE));
    }

    public void setShouldExplode(boolean b){
        this.entityData.set(SHOULD_EXPLODE,b);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accesor){
        super.onSyncedDataUpdated(accesor);
    }

    public void tick(){
        Entity entity = this.getOwner();

        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            this.calcAndMove();
            List<LivingEntity> entities;
            if (this.getOwner() instanceof LivingEntity){
                entities=this.entitiesToHurt(pred);
            }
            else{
                entities=this.entitiesToHurt();
            }
            if (!entities.isEmpty()){
                explode();
            }
            this.checkInsideBlocks();
            super.tick();
            this.setCurrentLifetime(this.getCurrentLifeTime()+1);
            if (this.getCurrentLifeTime()>=this.getLifeTime()){
                this.discard();
            }
        } else {
            this.discard();
        }
    }

    protected void onInsideBlock(BlockState blockState) {
        if (!blockState.isAir()){
            explode();
        }
    }

    protected void explode(){
        Predicate pred=null;
        if (this.getOwner() instanceof AbstractTranscendent){
            pred= CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate((LivingEntity)this.getOwner(),List.of(AbstractTranscendent.class));
        }
        AABB aabb=new AABB(this.getX()- explosionRadius,this.getY()- explosionRadius,this.getZ()- explosionRadius,this.getX()+explosionRadius,this.getY()+explosionRadius,this.getZ()+explosionRadius);
        float kb=1.75f;
        if (!this.entityData.get(SHOULD_EXPLODE)){
            aabb=this.getBoundingBox();
            kb=0.1f;
        }
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,aabb,pred);
        for(int ind = 0; ind < list.size(); ind++) {
            LivingEntity entity = list.get(ind);
            if (entity.hurt(damagesource,this.getDmg())){
                double rad= Math.toRadians(MathFuncs.getAngFrom2DVec(MathFuncs.getDirVec(this.position(),entity.position())));
                double xDir=Math.sin(rad);
                double zDir=Math.cos(rad);
                entity.knockback(kb,-xDir,-zDir);
                entity.setSecondsOnFire(3);
            }
        }
        this.discard();
    }
}
