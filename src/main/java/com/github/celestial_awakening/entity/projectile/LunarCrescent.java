package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.items.MoonlightReaper;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Predicate;

public class LunarCrescent extends CA_Projectile {
    public ItemStack itemStackSource;
    /*
    NOTE:
    Magic DType bypasses shield
    Need to make a custom dtype later
     */

    public LunarCrescent(EntityType<LunarCrescent> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_,70);
        life=0;
        damagesource=new DamageSourceIgnoreIFrames(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),this,null);
    }
    public static LunarCrescent create(Level level, float damage, int lifeVal,float spd,float hAng,float vAng,float zR) {
        LunarCrescent crescent=new LunarCrescent(EntityInit.LUNAR_CRESCENT.get(),level);
        crescent.setMoveValues(spd,hAng,vAng);
        crescent.setZRot(zR);
        crescent.setLifetime(lifeVal);
        crescent.setDmg(damage);
        crescent.setDims(1f,0.2f);
        crescent.setDeltaMovement(crescent.calculateMoveVec());
        return crescent;
    }
    public static LunarCrescent create(Level level, float damage, int lifeVal,float spd,float hAng,float vAng,float zR,float width,float height,float rs) {
        LunarCrescent crescent=new LunarCrescent(EntityInit.LUNAR_CRESCENT.get(),level);
        crescent.setMoveValues(spd,hAng,vAng);
        crescent.setZRot(zR);
        crescent.setLifetime(lifeVal);
        crescent.setDmg(damage);
        crescent.setRScales(rs);
        crescent.setDims(width,height);
        crescent.setDeltaMovement(crescent.calculateMoveVec());
        return crescent;
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void setOwner(Entity e){
        super.setOwner(e);
        if (e instanceof LivingEntity){
            pred= CA_Predicates.opposingTeams_IgnoreSameClass_Predicate((LivingEntity) e);
        }

        damagesource=new DamageSourceIgnoreIFrames(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), this,e);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accesor){
        super.onSyncedDataUpdated(accesor);
    }

    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }
    public void tick() {
        Entity owner = this.getOwner();
        if (this.level().isClientSide || (owner == null || !owner.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            this.calcAndMove();

            if (this.isReal()){
                Predicate<Entity> alreadyHitPred= o -> o != null && !entityIDs.contains(o.getStringUUID());
                Predicate p=alreadyHitPred.and(pred);
                List<LivingEntity> entities=this.entitiesToHurt(p);
                for (LivingEntity e:entities) {
                    this.entityIDs.add(e.getStringUUID());
                    if (e.hurt(damagesource,this.getDmg())){
                        if (owner instanceof LivingEntity){
                            ((LivingEntity) owner).setLastHurtMob(e);
                            if (!e.isAlive() && itemStackSource!=null && itemStackSource.getItem() instanceof MoonlightReaper){
                                MoonlightReaper.healOnKill(itemStackSource, e, (LivingEntity) owner);
                            }
                        }
                    }
                    else{
                        if (this.canDisableShields()){
                            this.disableTargetShields(e);
                        }
                    }
                }
                this.checkInsideBlocks();
            }
            super.tick();
            this.setCurrentLifetime(this.getCurrentLifeTime()+1);
            if (this.getCurrentLifeTime()>=this.getLifeTime()){
                this.discard();
            }
        } else {
            this.discard();
        }

    }
}
