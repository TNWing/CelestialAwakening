package com.github.celestial_awakening.entity.living.phantom_knights;

import com.github.celestial_awakening.entity.combat.phantom_knights.PK_CrescenciaCombatAIGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

public class PhantomKnight_Crescencia extends AbstractPhantomKnight{
    static double baseHP=225.0D;
    static double baseDmg=4.5D;
    static double baseArmor=9D;
    static double baseTough=6D;


    public final AnimationState idleAnimationState=new AnimationState();
    public final AnimationState whirlwindStartAnimationState=new AnimationState();
    public final AnimationState whirlwindAnimationState=new AnimationState();
    public final AnimationState nightSlashStartAnimationState=new AnimationState();
    public final AnimationState nightSlashStrikeAnimationState=new AnimationState();
    public PhantomKnight_Crescencia(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
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


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.4D)
                .add(Attributes.MAX_HEALTH, baseHP)
                .add(Attributes.ARMOR, baseArmor)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough)
                .add(Attributes.FOLLOW_RANGE,32D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg);
    }


    public static void addScaledAttributes(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> entityType){
        addScaledAttributes(event, entityType,baseHP,baseArmor,baseTough,baseDmg);
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4,new PK_CrescenciaCombatAIGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        //this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1D));
        //this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        //this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public void tick() {
        super.tick();
    }

    @Override
    public void updateAnim() {
        switch (this.entityData.get(ACTION_ID)) {
            case 0: {//idle
                if (isSameAnim()) {
                    if (animTime > 0) {
                        animTime--;
                    }

                } else {
                    stopAnim(currentAction);
                    currentAction = 0;
                    animTime = 60;

                    idleAnimationState.start(this.tickCount);
                }
                break;
            }
            case 1: {//walk
                if (isSameAnim()) {
                    if (animTime > 0) {
                        animTime--;
                    }

                } else {
                    stopAnim(currentAction);
                    currentAction = 1;
                    animTime = 60;
                }
                break;
            }
            case 2: {//whirlwindStart
                if (isSameAnim()) {
                    if (animTime > 0) {
                        animTime--;
                    }

                } else {
                    stopAnim(currentAction);
                    currentAction = 2;
                    animTime = 20;
                    whirlwindStartAnimationState.start(this.tickCount);
                }
                break;
            }
            case 3: {//whirlwind
                if (isSameAnim()) {
                    if (animTime > 0) {
                        animTime--;
                    }

                } else {
                    stopAnim(currentAction);
                    currentAction = 3;
                    animTime = 30;
                    whirlwindAnimationState.start(this.tickCount);
                }
                break;
            }
            case 4: {//nightSlashStart
                if (isSameAnim()) {
                    if (animTime > 0) {
                        animTime--;
                    }

                } else {
                    stopAnim(currentAction);
                    currentAction = 4;
                    animTime = 30;
                    nightSlashStartAnimationState.start(this.tickCount);
                }
                break;
            }
            case 5:{//nightSlashStrike
                if (isSameAnim()) {
                    if (animTime > 0) {
                        animTime--;
                    }

                } else {
                    stopAnim(currentAction);
                    currentAction = 4;
                    animTime = 30;
                    nightSlashStrikeAnimationState.start(this.tickCount);
                }
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
                whirlwindStartAnimationState.stop();
                break;
            }
            case 3:{
                whirlwindAnimationState.stop();
                break;
            }
            case 4:{
                nightSlashStartAnimationState.stop();
                break;
            }
            case 5:{
                nightSlashStrikeAnimationState.stop();
                break;
            }
        }
    }
}
