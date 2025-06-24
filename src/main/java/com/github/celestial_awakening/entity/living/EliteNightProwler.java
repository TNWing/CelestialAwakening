package com.github.celestial_awakening.entity.living;

import com.github.celestial_awakening.entity.combat.night_prowlers.NightProwlerCombatAIGoal;
import com.github.celestial_awakening.entity.living.night_prowlers.AbstractNightProwler;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
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

public class EliteNightProwler extends AbstractNightProwler{
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

    public EliteNightProwler(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.35F)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.1D)
                .add(Attributes.MAX_HEALTH, baseHP)
                .add(Attributes.ARMOR, baseArmor)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough)
                .add(Attributes.FOLLOW_RANGE,24D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg);

    }
    @Override
    public void updateAnim() {
        int id=this.entityData.get(ACTION_ID);
        AnimationState currentState=actionIDToAnimMap.get(id);
        if (currentState==null){
            return;
        }
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
    protected void updateWalkAnimation(float pPartialTick) {
        //super.updateWalkAnimation(pPartialTick);

        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
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
}
