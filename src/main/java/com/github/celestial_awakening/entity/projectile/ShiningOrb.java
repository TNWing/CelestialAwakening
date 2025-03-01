package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

public class ShiningOrb extends CA_Projectile {
    private int life;

    double explosionRadius =2.25D;
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
        damagesource=this.level().damageSources().indirectMagic(this,e);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accesor){
        super.onSyncedDataUpdated(accesor);
    }

    public void tick(){
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            List<LivingEntity> entities;
            if (this.getOwner() instanceof LivingEntity){
                entities=this.entitiesToHurt(CA_Predicates.opposingTeams_IgnoreSameClass_Predicate((LivingEntity) this.getOwner()));
            }
            else{
                entities=this.entitiesToHurt();
            }
            if (!entities.isEmpty()){
                explode();
            }
            this.checkInsideBlocks();
            this.life++;
            if (this.life>=this.getLifeTime()){
                explode();
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
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,aabb,pred);
        for(int ind = 0; ind < list.size(); ind++) {
            LivingEntity entity = list.get(ind);
            if (entity.hurt(damagesource,2.5f)){
                double rad= Math.toRadians(MathFuncs.getAngFrom2DVec(MathFuncs.getDirVec(this.position(),entity.position())));
                double xDir=Math.sin(rad);
                double zDir=Math.cos(rad);
                entity.knockback(1.75f,-xDir,-zDir);
            }
        }
        this.discard();
    }
}
