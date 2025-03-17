package com.github.celestial_awakening.entity.living.transcendents;

import com.github.celestial_awakening.entity.combat.transcendents.astralite.AstraliteCombatAIGoal;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

import java.util.HashMap;

public class Astralite extends AbstractTranscendent {
    public Astralite(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward=12;
        actionIDToAnimMap.put(0,idleAnimationState);
        actionIDToAnimMap.put(3,basicAttackAnimationState);
    }
    static double baseHP=20.0D;
    static double baseDmg=3.0D;
    static double baseArmor=4D;
    static double baseTough=0D;
    public final AnimationState idleAnimationState=new AnimationState();
    public final AnimationState readyUpAnimationState=new AnimationState();
    public final AnimationState walkAnimationState=new AnimationState();
    public final AnimationState basicAttackAnimationState=new AnimationState();
    public final AnimationState photonSurgeAnimationState=new AnimationState();
    HashMap<Integer,AnimationState> actionIDToAnimMap=new HashMap();
    public static AttributeSupplier.Builder createAttributes() {//TODO
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.15D)
                .add(Attributes.MAX_HEALTH, baseHP)
                .add(Attributes.ARMOR, baseArmor)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough)
                .add(Attributes.FOLLOW_RANGE,48D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg);
    }
    public static void addScaledAttributes(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> entityType){
        addScaledAttributes(event, entityType,baseHP,baseArmor,baseTough,baseDmg);
    }
    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation=new FlyingPathNavigation(this, level);
        navigation.setCanFloat(true);
        return navigation;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4,new AstraliteCombatAIGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public void tick() {
        super.tick();
    }

    @Override
    public boolean hurt(DamageSource p_19946_, float p_19947_) {
        boolean b=super.hurt(p_19946_,p_19947_);
        return b;
    }
    @Override
    public void travel(Vec3 travelVec){
        super.travel(travelVec);
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
            ACTION_FRAME=0;
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
}
