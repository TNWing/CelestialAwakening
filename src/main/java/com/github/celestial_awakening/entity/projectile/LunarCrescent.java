package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.damage.DamageSourceNoIFrames;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class LunarCrescent extends CA_Projectile {
    private int life;
    private static final EntityDataAccessor<Float> ZROT = SynchedEntityData.defineId(LunarCrescent.class, EntityDataSerializers.FLOAT);
    /*
    NOTE:
    Magic DType bypasses shield
    Need to make a custom dtype later
     */
    DamageSourceNoIFrames damagesource=new DamageSourceIgnoreIFrames(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),this,null);
    private ArrayList<Integer> entityIDs=new ArrayList<>();

    private HashMap<Integer,Integer> entityHitMap=new HashMap<>();
    public LunarCrescent(EntityType<LunarCrescent> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_,70);
        life=0;
    }

    public LunarCrescent(Level level, float damage, Vec3 dm, int lifeVal) {
        super(EntityInit.LUNAR_CRESCENT.get(), level,lifeVal);
        life=0;
        this.setDeltaMovement(dm);

        this.setDmg(damage);
        setZRot(0);
    }
    public LunarCrescent(Level level, float damage, int lifeVal,float spd,float hAng,float vAng,float zR) {
        super(EntityInit.LUNAR_CRESCENT.get(), level,spd,hAng,vAng,lifeVal);
        life=0;
        this.setDmg(damage);
        this.setDims(1f,0.2f,0.4f);
        setZRot(zR);

        this.setDeltaMovement(calculateMoveVec());
    }
    public LunarCrescent(Level level, float damage, int lifeVal,float spd,float hAng,float vAng,float zR,float width,float height,float depth,float rs) {
        super(EntityInit.LUNAR_CRESCENT.get(), level,spd,hAng,vAng,lifeVal);
        life=0;
        this.setDmg(damage);
        this.setRScales(rs);
        this.setDims(width,height,depth);
        setZRot(zR);
        this.setDeltaMovement(calculateMoveVec());
    }
    public LunarCrescent(Level level, float damage,float zR) {
        super(EntityInit.LUNAR_CRESCENT.get(), level,70);
        life=0;

        this.setDmg(damage);
        setZRot(zR);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ZROT,0f);

    }

    @Override
    public void setOwner(Entity e){
        super.setOwner(e);
        System.out.println("HEN" + damagesource.is(DamageTypeTags.BYPASSES_SHIELD));
        damagesource=new DamageSourceIgnoreIFrames(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), this,e);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accesor){
        super.onSyncedDataUpdated(accesor);
    }

    public float getZRot(){
        return this.entityData.get(ZROT);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_19975_) {
        EntityDimensions dims=EntityDimensions.scalable(this.getWidth(), this.getHeight());
        if (this.entityData.get(ZROT)==90){
            //is it this?
            dims=EntityDimensions.scalable(this.getHeight(), this.getWidth());
        }
        return dims;
    }


//even with f=0, the thing is still properly rotated
    //but when zrot is blocked out, its not affected?
    /*
    So when ZROT is properly set, it rotates
    but why, ZROT is only really called in renderer? unless that impacts aabb
    no, renderer only impacts sprite, not the aabb
    so why?
     */
    public void setZRot(float fa){
        float f=0;
        float width=this.getWidth();
        float height=this.getHeight();
        float depth=this.getDepth();

        this.entityData.set(ZROT,fa);

        double sin = Math.sin(Math.toRadians(f));
        double cos = Math.cos(Math.toRadians(f));
        double newWidth = Math.abs(width * cos) + Math.abs(height * sin);
        double newHeight = Math.abs(width * sin) + Math.abs(height * cos);
        this.setBoundingBox(new AABB(
                this.getX() - newWidth / 2,
                this.getY()-newHeight/2,
                this.getZ() - depth / 2,
                this.getX() + newWidth / 2,
                this.getY() + newHeight/2,
                this.getZ() + depth / 2
        ));
        this.refreshDimensions();
    }


    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(ZROT,tag.getFloat("ZRot"));
        int[] arr=tag.getIntArray("Entities Hit");
        this.entityIDs.clear();
        for (int i:arr){
            this.entityIDs.add(i);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("ZRot",this.entityData.get(ZROT));
        tag.putIntArray("Entities Hit",this.entityIDs);
    }
    public void tick() {
        Entity owner = this.getOwner();
        if (this.level().isClientSide || (owner == null || !owner.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            if (this.isReal()){
                Predicate alreadyHitPred= o -> o instanceof Entity && !entityIDs.contains(((Entity)o).getId());
                Predicate p;
                if (owner!=null){
                    p=alreadyHitPred.and(CA_Predicates.opposingTeams_IgnoreSameClass_Predicate((LivingEntity) this.getOwner()));
                }
                else{
                    p=alreadyHitPred;
                }
                List<LivingEntity> entities=this.entitiesToHurt(p);
                for (LivingEntity e:entities) {
                    this.entityIDs.add(e.getId());
                    if (e.hurt(damagesource,this.getDmg())){
                        if (owner instanceof LivingEntity){
                            ((LivingEntity) owner).setLastHurtMob(e);
                        }
                    }
                }
                this.checkInsideBlocks();
            }


            this.life++;
            if (this.life>=this.getLifeTime()){
                this.discard();
            }
        } else {
            this.discard();
        }

    }
}
