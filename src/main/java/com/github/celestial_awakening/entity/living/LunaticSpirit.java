package com.github.celestial_awakening.entity.living;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class LunaticSpirit extends Monster {
    /**
    Spawns if a player stares at the moon for too long
     Afterwards, they will follow the player.
     Invisible but will periodically appear
     Drops cloths of lunacy
     */
    private LunaticSpiritTypes type;
    protected LunaticSpirit(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void setType(LunaticSpiritTypes type){
        this.type=type;
    }
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.5F)
                .add(Attributes.KNOCKBACK_RESISTANCE,1D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ARMOR, 3D)
                .add(Attributes.ARMOR_TOUGHNESS, 2D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D);
    }
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        //this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
        //this.goalSelector.addGoal(4, new MeleeAttackGoal(this,1D,true));
        //this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
    }

}
