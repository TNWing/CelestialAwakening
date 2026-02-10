package com.github.celestial_awakening.entity.living.planetary_guardians;

import com.github.celestial_awakening.entity.combat.CoreGuardianDeepYTargetGoal;
import com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian.CoreGuardianCombatAIGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CoreGuardian extends AbstractGuardian{
    int hardenStacks=0;
    int hardenShieldBrokenCnt=0;

    static double baseHP=110.0D;
    static double baseDmg=5.0D;
    static double baseArmor=8D;
    static double baseTough=6.5D;

    public CoreGuardian(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward=80;
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.15f)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.35D)
                .add(Attributes.MAX_HEALTH, baseHP)
                .add(Attributes.ARMOR, baseArmor)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough)
                .add(Attributes.FOLLOW_RANGE,40D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg);

    }
    public int getHardenStacks() {
        return hardenStacks;
    }

    public void setHardenStacks(int hardenStacks) {
        this.hardenStacks = hardenStacks;
    }
    public void decrementHardenStacks() {
        if (hardenStacks<=0){
            return;
        }
        System.out.println("DEC stacks from " + this.hardenStacks);
        this.hardenStacks--;
        System.out.println("HARD STACKS " + this.hardenStacks);
        if (hardenStacks==0){
            this.playSound(SoundEvents.ITEM_BREAK,1,1f);
            hardenShieldBrokenCnt++;
            System.out.println("HARD SHIELD BROKE AT " + hardenShieldBrokenCnt);
        }
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new CoreGuardianDeepYTargetGoal(this, Player.class, true));
        this.goalSelector.addGoal(5,new CoreGuardianCombatAIGoal(this));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D, 60));
    }

    @Override
    public boolean hurt(DamageSource source, float amt){
        if (!(this.level().isClientSide)) {
            float mult=(1f-hardenStacks/(hardenStacks+2f) + 0.1f*hardenShieldBrokenCnt);
            amt=amt*mult;
        }

        return super.hurt(source,amt);
    }
    protected SoundEvent getHurtSound(DamageSource p_30424_) {
        return SoundEvents.BLAZE_HURT;
    }

    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.4F;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }
}
