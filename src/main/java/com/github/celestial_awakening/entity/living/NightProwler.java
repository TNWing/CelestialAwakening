package com.github.celestial_awakening.entity.living;

import com.github.celestial_awakening.entity.combat.night_prowlers.NightProwlerCombatAIGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

public class NightProwler extends AbstractCALivingEntity {
    public NightProwler(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.standardAABB=this.getBoundingBox();
    }
    static double baseHP=28.0D;
    static double baseDmg=5.0D;
    static double baseArmor=1D;
    static double baseTough=1D;
    //Animation
    //SpawnPlacements
    public final AnimationState idleAnimationState=new AnimationState();
    public final AnimationState attackAnimationState=new AnimationState();
    public final AnimationState crouchAnimationState=new AnimationState();
    public final AnimationState leapAnimationState=new AnimationState();


    protected float opacity;
    public boolean hasCollision;
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.5F)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.1D)
                .add(Attributes.MAX_HEALTH, baseHP)
                .add(Attributes.ARMOR, baseArmor)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough)
                .add(Attributes.FOLLOW_RANGE,24D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg);

    }

    public static void addScaledAttributes(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> entityType){
        addScaledAttributes(event, entityType,baseHP,baseArmor,baseTough,baseDmg);
    }
    public AABB standardAABB;

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

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new NightProwlerCombatAIGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
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
        //TODO
        /*
        figure out how to get walk anim to work
         */


    }


    @Override
    public void updateAnim(){
        switch(this.entityData.get(ACTION_ID)){
            case 0:{//idle
                if (isSameAnim()){
                    if (animTime>0){
                        animTime--;
                    }

                }
                else{
                    stopAnim(currentAction);
                    currentAction=0;
                    animTime=60;

                    idleAnimationState.start(this.tickCount);
                }
                break;
            }
            case 1:{//walk
                if (isSameAnim()){
                    if (animTime>0){
                        animTime--;
                    }

                }
                else{
                    stopAnim(currentAction);
                    currentAction=1;
                    animTime=60;

                }
                break;
            }
            case 2:{//attack
                if (isSameAnim()){
                    if (animTime>0){
                        animTime--;
                    }

                }
                else{
                    stopAnim(currentAction);
                    currentAction=2;
                    animTime=20;
                    //attackAnimationState.start(this.tickCount);
                }
                break;
            }
            case 3:{//crouch
                if (isSameAnim()){
                    if (animTime>0){
                        animTime--;
                    }

                }
                else{
                    stopAnim(currentAction);
                    currentAction=3;
                    animTime=30;
                    crouchAnimationState.start(this.tickCount);
                }
                break;
            }
            case 4:{//leap
                if (isSameAnim()){
                    if (animTime>0){
                        animTime--;
                    }

                }
                else{
                    stopAnim(currentAction);
                    currentAction=4;
                    animTime=30;
                    leapAnimationState.start(this.tickCount);
                }
                break;
            }
        }
    }

    public void stopAnim(int i){
        System.out.println("STOPPING ANIM W/ ACT ID " + i);
        switch (i){
            case 0:{
                idleAnimationState.stop();
                break;
            }
            case 1:{
                break;
            }
            case 2:{
                attackAnimationState.stop();
                break;
            }
            case 3:{
                crouchAnimationState.stop();
                break;
            }
            case 4:{
                leapAnimationState.stop();
                break;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amt){
        return super.hurt(source,amt);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return super.doHurtTarget(entity);
    }
}
