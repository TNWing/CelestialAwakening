package com.github.celestial_awakening.entity.living.transcendents;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.entity.combat.transcendents.asteron.AsteronCombatAIGoal;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

import java.util.HashMap;

public class Asteron extends AbstractTranscendent {

    static double baseHP=20.0D;
    static double baseDmg=4.5D;
    static double baseArmor=6D;
    static double baseTough=1D;
    public final AnimationState idleAnimationState=new AnimationState();
    public final AnimationState readyUpAnimationState=new AnimationState();
    public final AnimationState walkAnimationState=new AnimationState();
    public final AnimationState basicAttackAnimationState=new AnimationState();
    public final AnimationState piercingRaysAnimationState=new AnimationState();
    public final AnimationState piercingRaysRecoveryAnimationState=new AnimationState();
    HashMap<Integer,AnimationState> actionIDToAnimMap=new HashMap();
    public Asteron(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(baseHP * Config.mobHPScale);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(baseDmg * Config.mobDmgScale);
        this.getAttribute(Attributes.ARMOR).setBaseValue(baseArmor * Config.mobArmorPtScale);
        this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(baseTough * Config.mobArmorToughnessScale);
        //this.setHealth(this.getMaxHealth());
        actionIDToAnimMap.put(0,idleAnimationState);
        actionIDToAnimMap.put(1,readyUpAnimationState);
        actionIDToAnimMap.put(2,walkAnimationState);
        actionIDToAnimMap.put(3,basicAttackAnimationState);
        actionIDToAnimMap.put(4,piercingRaysAnimationState);
        actionIDToAnimMap.put(5,piercingRaysRecoveryAnimationState);
        this.xpReward=12;
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

    public static AttributeSupplier.Builder createAttributes() {//TODO
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.3D)
                .add(Attributes.MAX_HEALTH, baseHP*Config.mobHPScale)
                .add(Attributes.ARMOR, baseArmor*armorPtMult)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough*armorToughMult)
                .add(Attributes.FOLLOW_RANGE,48D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg*dmgMult);
    }

    public static void updateAttributesFromConfig(){
        System.out.println("UPDATE " + Config.mobHPScale);
        maxHealthMult =Config.mobHPScale;
        dmgMult =  Config.mobDmgScale;
        armorPtMult = Config.armorPtScale;
        armorToughMult =Config.armorToughnessScale;
    }


    public static AttributeSupplier.Builder createScaledAttributes() {//TODO
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.3D)
                .add(Attributes.MAX_HEALTH, baseHP* Config.mobHPScale)
                .add(Attributes.ARMOR,baseArmor*Config.mobArmorPtScale)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough*Config.mobArmorToughnessScale)
                .add(Attributes.FOLLOW_RANGE,48D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg*Config.mobDmgScale);
    }
    public static void addScaledAttributes(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> entityType){
        addScaledAttributes(event, entityType,baseHP,baseArmor,baseTough,baseDmg);
    }


    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4,new AsteronCombatAIGoal(this));
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


}
