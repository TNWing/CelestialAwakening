package com.github.celestial_awakening.entity.living;

import com.github.celestial_awakening.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

import java.util.List;
import java.util.function.Predicate;

//TODO:
//move the code in pathtotarget goal into targetting abilitygoal.
public abstract class AbstractCALivingEntity extends Monster {
    protected static final EntityDataAccessor<Byte> ACTION_ID = SynchedEntityData.defineId(AbstractCALivingEntity.class, EntityDataSerializers.BYTE);
    //may not need action frame to be dataaccessor?
    protected Integer ACTION_FRAME;
    public int animTime;
    public int currentAction;

    public int nextActionTickCount;//delay before next action
    protected int actionTickCast;//ticks an action takes
    public boolean isActing;
    public boolean canMove;
    public boolean fixedRot;
    public boolean fixedHeadRot;
    public double minRange;
    public double maxRange;
    public boolean keepDist;//if true, instead of mob moving towards target, target tries to stay at least X blocks away
    public float spdMod;

    protected static Double maxHealthMult =Config.mobHPScale;
    protected static Double dmgMult =  Config.mobDmgScale;
    protected static Double armorPtMult = Config.armorPtScale;
    protected static Double armorToughMult =Config.armorToughnessScale;
    protected boolean isCombatActive;
    protected AbstractCALivingEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {

        super(p_33002_, p_33003_);
        nextActionTickCount=0;
        setActionId(0);
        canMove=true;
        isActing=false;
        keepDist=false;
        spdMod=1f;
    }

    public static void addScaledAttributes(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> type,double baseHP,double baseArmor,double baseTough,double baseDmg) {
        event.add(type,Attributes.MAX_HEALTH,baseHP*Config.mobHPScale);
        event.add(type,Attributes.ARMOR,baseArmor*Config.mobArmorPtScale);
        event.add(type,Attributes.ARMOR_TOUGHNESS,baseTough*Config.mobArmorToughnessScale);
        event.add(type,Attributes.ATTACK_DAMAGE,baseDmg*Config.mobDmgScale);
    }
    public static void createAttributes(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> type,double baseHP,double baseArmor,double baseTough,double baseDmg) {
        event.add(type,Attributes.MAX_HEALTH,baseHP*Config.mobHPScale);
        event.add(type,Attributes.ARMOR,baseArmor*Config.mobArmorPtScale);
        event.add(type,Attributes.ARMOR_TOUGHNESS,baseTough*Config.mobArmorToughnessScale);
        event.add(type,Attributes.ATTACK_DAMAGE,baseDmg*Config.mobDmgScale);
    }



    public boolean isActing() {
        if (this.level().isClientSide) {
            return isActing;
        }
        else {
            return this.isActing;
        }
    }
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(ACTION_ID,(byte)0);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void tick() {
        float yRot=this.getYRot();
        float xRot=this.getXRot();
        float yHeadRot=this.getYHeadRot();
        super.tick();
        if (fixedRot){
            this.setRot(yRot,xRot);
        }
        if (fixedHeadRot){
            this.setYHeadRot(yHeadRot);
        }
        if (this.level().isClientSide){
            updateAnim();
        }


    }

    public List<LivingEntity> findCollidedEntities(Predicate p){
        AABB aabb=this.getBoundingBox();
        List<LivingEntity> livingEntityList=this.level().getEntitiesOfClass(LivingEntity.class,aabb,p);
        return livingEntityList;
    }
    public List<LivingEntity> findCollidedEntities(Predicate p,float i){
        AABB aabb=this.getBoundingBox().inflate(i);
        List<LivingEntity> livingEntityList=this.level().getEntitiesOfClass(LivingEntity.class,aabb,p);
        return livingEntityList;
    }

    public void setCombatActive(boolean b){
        this.isCombatActive=b;
    }
    public abstract void updateAnim();

    public void setActionId(int i) {
        this.entityData.set(ACTION_ID, (byte)i);
    }
    public int getActionId() {
        return this.entityData.get(ACTION_ID);
    }

    public void setActionFrame(int i) {
        ACTION_FRAME=i;
    }
    public int getActionFrame() {
        return ACTION_FRAME;
    }

    public void incrementActionFrame() {
        ACTION_FRAME+=1;
    }
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    protected boolean isSameAnim(){
        return this.entityData.get(ACTION_ID)==currentAction;
    }

    public boolean canStillSenseTarget(){
        LivingEntity target=this.getTarget();
        return this.getSensing().hasLineOfSight(target) && (this.distanceToSqr(target)<=Math.pow(this.getAttributeValue(Attributes.FOLLOW_RANGE),2));
    }
    public void setDeltaMovementOfProjectile(Projectile projectile, Vec3 dm){
        projectile.setDeltaMovement(dm);
        System.out.println("HELLO from client?" + this.level().isClientSide);
    }
}
