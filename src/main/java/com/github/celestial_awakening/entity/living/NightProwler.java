package com.github.celestial_awakening.entity.living;

import com.github.celestial_awakening.entity.combat.night_prowlers.NightProwlerCombatAIGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

import java.util.HashMap;

public class NightProwler extends AbstractCALivingEntity {

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
    public final AnimationState leapRecoveryAnimationState=new AnimationState();

    HashMap<Integer,AnimationState> actionIDToAnimMap=new HashMap();
    protected float opacity;
    public boolean hasCollision;

    public AABB standardAABB;


    public NightProwler(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.standardAABB=this.getBoundingBox();
        actionIDToAnimMap.put(0,idleAnimationState);
        actionIDToAnimMap.put(1,attackAnimationState);
        actionIDToAnimMap.put(2,crouchAnimationState);
        actionIDToAnimMap.put(3,leapAnimationState);
        actionIDToAnimMap.put(4,leapRecoveryAnimationState);
        this.xpReward=12;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.4F)
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
    public void updateAnim() {
        int id=this.entityData.get(ACTION_ID);
        AnimationState currentState=actionIDToAnimMap.get(id);
        System.out.println("HELLO UPDATING anim, id is " + id);
        if (isSameAnim()){
            incrementActionFrame();
            if (id==1 && getActionFrame()==18 && this.getTarget()!=null){
                this.lookControl.setLookAt(this.getTarget());
            }
        }
        else if (id!=-1){
            actionIDToAnimMap.get(currentAction).stop();
            currentAction=id;
            currentState.start(this.tickCount);
            switch(this.entityData.get(ACTION_ID)){
                case 1:{
                }
                default:{
                    break;
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amt){
        return super.hurt(source,amt);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (entity instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)entity).getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            entity.setSecondsOnFire(i * 4);
        }
        boolean flag = entity.hurt(this.damageSources().mobAttack(this), f);
        if (flag) {
            if (f1 > 0.0F && entity instanceof LivingEntity) {
                ((LivingEntity)entity).knockback((double)(f1 * 0.5F), (double) Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            if (entity instanceof Player) {
                Player player = (Player)entity;
                //this.maybeDisableShield(player, this.getMainHandItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
            }

            this.doEnchantDamageEffects(this, entity);
            this.setLastHurtMob(entity);
        }

        return flag;
    }
}
