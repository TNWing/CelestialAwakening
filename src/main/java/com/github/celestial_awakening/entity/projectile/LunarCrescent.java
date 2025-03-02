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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class LunarCrescent extends CA_Projectile {
    private int life;
    public ItemStack itemStackSource;
    /*
    NOTE:
    Magic DType bypasses shield
    Need to make a custom dtype later
     */
    private ArrayList<Integer> entityIDs=new ArrayList<>();

    private HashMap<Integer,Integer> entityHitMap=new HashMap<>();
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
        damagesource=new DamageSourceIgnoreIFrames(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), this,e);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accesor){
        super.onSyncedDataUpdated(accesor);
    }




//even with f=0, the thing is still properly rotated
    //but when zrot is blocked out, its not affected?
    /*
    So when ZROT is properly set, it rotates
    but why, ZROT is only really called in renderer? unless that impacts aabb
    no, renderer only impacts sprite, not the aabb
    so why?
     */



    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        int[] arr=tag.getIntArray("Entities Hit");
        this.entityIDs.clear();
        for (int i:arr){
            this.entityIDs.add(i);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putIntArray("Entities Hit",this.entityIDs);
    }
    public void tick() {
        if (level().isClientSide){
            //System.out.println("before lc runs " + this.position()+ "   WITH ZR " + this.getZRot());
        }
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
                            if (!e.isAlive() && itemStackSource.getItem() instanceof MoonlightReaper){
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


            this.life++;
            if (level().isClientSide){
                //System.out.println("after lc done " + this.position()+ "   WITH ZR " + this.getZRot());
            }
            if (this.life>=this.getLifeTime()){
                this.discard();
            }
        } else {
            this.discard();
        }

    }
}
