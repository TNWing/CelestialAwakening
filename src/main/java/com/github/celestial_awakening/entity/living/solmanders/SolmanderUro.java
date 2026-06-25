package com.github.celestial_awakening.entity.living.solmanders;

import com.github.celestial_awakening.entity.combat.SolmanderTargetGoal;
import com.github.celestial_awakening.entity.combat.solmanders.SolmanderNewtCombatAIGoal;
import com.github.celestial_awakening.entity.combat.solmanders.SolmanderUroCombatAIGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SolmanderUro extends AbstractSolmander{
    static double baseHP=32.0D;
    static double baseDmg=4.0D;
    static double baseArmor=3D;
    static double baseTough=2.5D;
    protected SolmanderUro(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.2F)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.15D)
                .add(Attributes.MAX_HEALTH, baseHP)
                .add(Attributes.ARMOR, baseArmor)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough)
                .add(Attributes.FOLLOW_RANGE,40D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg);

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
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new SolmanderTargetGoal(this, Player.class, true));
        this.goalSelector.addGoal(3, new SolmanderUroCombatAIGoal(this));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D, 60));
    }
}
