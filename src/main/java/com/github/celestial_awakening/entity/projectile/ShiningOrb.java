package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ShiningOrb extends CA_Projectile {
    private int life;

    double explosionRadius =1.5D;
    public ShiningOrb(EntityType<ShiningOrb> shiningOrbEntityType, Level level) {
        super(shiningOrbEntityType,level,80);
        damagesource=this.level().damageSources().indirectMagic(this,null);//new DamageSource(,null);
        this.setNoGravity(true);
        life=0;
    }


    public static ShiningOrb create(Level level,int lt,float spd,float hA,float vA,float dmg) {
        ShiningOrb entity = new ShiningOrb(EntityInit.SHINING_ORB.get(), level);
        entity.setNoGravity(true);
        entity.life=0;
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
            this.life++;
            if (this.life>=this.getLifeTime()){
                explode();
            }
        } else {
            this.discard();
        }
    }

    protected void explode(){
        AABB aabb=new AABB(this.getX()- explosionRadius,this.getY()- explosionRadius,this.getZ()- explosionRadius,this.getX()+explosionRadius,this.getY()+explosionRadius,this.getZ()+explosionRadius);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,aabb);
        for(int ind = 0; ind < list.size(); ind++) {
            Entity entity = list.get(ind);
            entity.hurt(damagesource,2.5f);
            //TODO: cause it to deal heavy kb
        }
        this.discard();
    }
}
