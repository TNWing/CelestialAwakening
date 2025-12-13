package com.github.celestial_awakening.entity.living.planetary_guardians;

import com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian.CoreGuardianCombatAIGoal;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class CoreGuardian extends AbstractGuardian{
    int hardenStacks=0;
    int hardenShieldBrokenCnt=0;

    static double baseHP=60.0D;
    static double baseDmg=4.0D;
    static double baseArmor=6D;
    static double baseTough=6.5D;

    public CoreGuardian(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
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
        this.hardenStacks--;
        if (hardenStacks==0){
            hardenShieldBrokenCnt++;
        }
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(5,new CoreGuardianCombatAIGoal(this));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D, 60));
    }

    @Override
    public boolean hurt(DamageSource source, float amt){
        amt=amt*hardenStacks/(hardenStacks+2) * (1+0.1f*hardenShieldBrokenCnt);
        return super.hurt(source,amt);
    }
}
