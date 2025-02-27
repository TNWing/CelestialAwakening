package com.github.celestial_awakening.entity.living.phantom_knights;

import com.github.celestial_awakening.entity.combat.phantom_knights.PK_CrescenciaCombatAIGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

import java.util.HashMap;

public class PhantomKnight_Crescencia extends AbstractPhantomKnight{
    static double baseHP=225.0D;
    static double baseDmg=4.5D;
    static double baseArmor=9D;
    static double baseTough=6D;

    /*
    ACTION_IDs
    -1: dormant
    0: idle/walking
    1: idle to combat ready
    2: combat ready to idle
    3: basic 1
    4: basic 2
    5: basic 3
    6: whirlwind start
    7: whirlwind
    8: whirlwind to combat ready
    9: moon cutter start
    10: strikethrough start
    11: strikethrough strike
     */

    public final AnimationState idleAnimationState=new AnimationState();
    public final AnimationState wakeUpAnimationState=new AnimationState();
    public final AnimationState returnToIdleAnimationState=new AnimationState();
    public final AnimationState basicAttack1AnimationState=new AnimationState();
    public final AnimationState basicAttack2AnimationState=new AnimationState();
    public final AnimationState basicAttack3AnimationState=new AnimationState();
    public final AnimationState whirlwindStartAnimationState=new AnimationState();
    public final AnimationState whirlwindAnimationState=new AnimationState();
    public final AnimationState whirlwindEndAnimationState=new AnimationState();
    public final AnimationState moonCutterAnimationState=new AnimationState();
    public final AnimationState strikethroughStartAnimationState=new AnimationState();
    public final AnimationState strikethroughStrikeAnimationState=new AnimationState();

    HashMap<Integer,AnimationState> actionIDToAnimMap=new HashMap();




    public final AnimationState nightSlashStartAnimationState=new AnimationState();
    public final AnimationState nightSlashStrikeAnimationState=new AnimationState();
    public PhantomKnight_Crescencia(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.setActionId(-1);
        actionIDToAnimMap.put(0,idleAnimationState);
        actionIDToAnimMap.put(1,wakeUpAnimationState);
        actionIDToAnimMap.put(2,returnToIdleAnimationState);
        actionIDToAnimMap.put(3,basicAttack1AnimationState);
        actionIDToAnimMap.put(4,basicAttack2AnimationState);
        actionIDToAnimMap.put(5,basicAttack3AnimationState);
        actionIDToAnimMap.put(6,whirlwindStartAnimationState);
        actionIDToAnimMap.put(7,whirlwindAnimationState);
        actionIDToAnimMap.put(8,whirlwindEndAnimationState);
        actionIDToAnimMap.put(9,moonCutterAnimationState);
        actionIDToAnimMap.put(10,strikethroughStartAnimationState);
        actionIDToAnimMap.put(11,strikethroughStrikeAnimationState);
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
        super.registerGoals();
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
        int id=this.entityData.get(ACTION_ID);
        AnimationState currentState=actionIDToAnimMap.get(id);

        if (isSameAnim()){
            if (animTime>0){
                incrementActionFrame();
                if (id==1 && getActionFrame()==18 && this.getTarget()!=null){
                    this.lookControl.setLookAt(this.getTarget());
                }
                animTime--;
            }
            else{

            }
        }
        else if (id!=-1){
            actionIDToAnimMap.get(currentAction).stop();
            currentAction=id;
            switch(this.entityData.get(ACTION_ID)){
                case 1:{
                    animTime=96;
                }
                default:{
                    animTime=100;
                    break;
                }
            }

            currentState.start(this.tickCount);
        }
    }
}
