package com.github.celestial_awakening.entity.living.transcendents;

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

public class Asteron extends AbstractTranscendent {
    //maybe two separate abstract goals
    /*
    one where it gets blocked and blocks other goals
    one that doesn't do that
     */

    /*
    model and stuff
    start of in a standard position (akin to player default pos)
    when switching to combat stance. torso rotates 90 degrees, leg rotates and moves
     */
///summon celestial_awakening:asteron ~3 ~ ~

    static double baseHP=20.0D;
    static double baseDmg=4.5D;
    static double baseArmor=6D;
    static double baseTough=1D;


    public Asteron(EntityType<? extends Monster> p_33002_, Level p_33003_) {
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

    public final AnimationState idleAnimationState=new AnimationState();
    public final AnimationState idleToAlertAnimationState=new AnimationState();
    public final AnimationState alertToIdleAnimationState=new AnimationState();
    public final AnimationState attackAnimationState=new AnimationState();
    public final AnimationState combatIdleState=new AnimationState();
    public static AttributeSupplier.Builder createAttributes() {//TODO
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.3D)
                .add(Attributes.MAX_HEALTH, baseHP)
                .add(Attributes.ARMOR, baseArmor)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough)
                .add(Attributes.FOLLOW_RANGE,48D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg);
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

    }

    //so yes, even vanilla mc would prevent movement if too close


}
