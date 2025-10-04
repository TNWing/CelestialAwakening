package com.github.celestial_awakening.entity.living;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.entity.AlertInterface;
import com.github.celestial_awakening.entity.CA_Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class AbstractCAMonster extends Monster implements CA_Entity {
    String kbImmuneUUID="4d80d010-d025-40de-80ad-6b262763cc30";
    public AttributeModifier kbImmune=new AttributeModifier(UUID.fromString(kbImmuneUUID),"KB Immune",1d,AttributeModifier.Operation.ADDITION);

    protected static final EntityDataAccessor<Byte> ACTION_ID = SynchedEntityData.defineId(AbstractCAMonster.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Float> OPACITY = SynchedEntityData.defineId(AbstractCAMonster.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> HAS_COLLISION = SynchedEntityData.defineId(AbstractCAMonster.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> HAS_XRAY = SynchedEntityData.defineId(AbstractCAMonster.class, EntityDataSerializers.BOOLEAN);
    //protected static final EntityDataAccessor<Boolean> TARGET_PERSISTANCE = SynchedEntityData.defineId(AbstractCAMonster.class, EntityDataSerializers.BOOLEAN);

    protected Integer ACTION_FRAME=0;
    public int animTime;
    public int currentAction;

    public int nextActionTickCount;//delay before next action
    public boolean isActing;
    public boolean canMove;
    public boolean fixedRot;
    public boolean fixedHeadRot;
    public double minRange;
    public double maxRange;
    public boolean keepDist;//if true, instead of mob moving towards target, target tries to stay at least X blocks away, not used currently tho
    public float spdMod;

    protected static Double maxHealthMult =Config.mobHPScale;
    protected static Double dmgMult =  Config.mobDmgScale;
    protected static Double armorPtMult = Config.armorPtScale;
    protected static Double armorToughMult =Config.armorToughnessScale;
    protected boolean isCombatActive;
    protected int bossBarWindup=0;
    protected int genericAbilityCD;//after using an ability, how long until can use another non-basic ability

    boolean hasSpawned=false;

    AlertInterface alertInterface;

    public int getBossBarWindup(){
        return this.bossBarWindup;
    }
    protected AbstractCAMonster(EntityType<? extends Monster> p_33002_, Level p_33003_) {

        super(p_33002_, p_33003_);
        nextActionTickCount=0;
        setActionId(0);
        canMove=true;
        isActing=false;
        keepDist=false;

        spdMod=1f;
        hasSpawned=true;
        
    }

    @Override
    public boolean startRiding(@NotNull Entity entityIn) {
        if (entityIn instanceof Boat || entityIn instanceof Minecart) {
            return false;
        }

        return super.startRiding(entityIn);
    }

    public static void addScaledAttributes(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> type, double baseHP, double baseArmor, double baseTough, double baseDmg) {
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
        this.entityData.define(OPACITY,1f);
        this.entityData.define(HAS_COLLISION,true);
        this.entityData.define(HAS_XRAY,false);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("HasSpawned")){
            hasSpawned=tag.getBoolean("HasSpawned");
        }
        setXRay(tag.getBoolean("XRay"));
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasSpawned",hasSpawned);
        tag.putBoolean("XRay",hasXRay());
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
        return findCollidedEntities(p,0);
    }
    public List<LivingEntity> findCollidedEntities(Predicate p,float i){
        AABB aabb=this.getBoundingBox().inflate(i);
        List<LivingEntity> livingEntityList=this.level().getEntitiesOfClass(LivingEntity.class,aabb,p);
        return livingEntityList;
    }

    public void setCombatActive(boolean b){
        this.isCombatActive=b;
    }
    public boolean isCombatActive(){
        return this.isCombatActive;
    }
    public abstract void updateAnim();

    public void setActionId(int i) {
        this.entityData.set(ACTION_ID, (byte)i);
    }
    public int getActionId() {
        return this.entityData.get(ACTION_ID);
    }


    public void setOpacity(float f) {
        this.entityData.set(OPACITY, f);
    }
    public float getOpacity() {
        return this.entityData.get(OPACITY);
    }

    public void setCollision(boolean b) {
        this.entityData.set(HAS_COLLISION, b);
    }
    public boolean hasCollision() {
        return this.entityData.get(HAS_COLLISION);
    }
    public void setXRay(boolean b){
        this.entityData.set(HAS_XRAY,b);
    }

    public boolean hasXRay(){
        return this.entityData.get(HAS_XRAY);
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


    @Override
    public AlertInterface getAlertInterface() {
        return this.alertInterface;
    }

    @Override
    public void setAlertInterface(AlertInterface alertInterface) {
        this.alertInterface=alertInterface;
    }
}
