package com.github.celestial_awakening.entity.living.sub_stars;

import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class ProtoSubStar extends AbstractSubStar{
    static double baseHP=35.0D;
    static double baseDmg=6.5D;
    static double baseArmor=0D;
    static double baseTough=0D;
    protected ProtoSubStar(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public void updateAnim() {

    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.KNOCKBACK_RESISTANCE,1.0D)
                .add(Attributes.MAX_HEALTH, baseHP)
                .add(Attributes.ARMOR, baseArmor)
                .add(Attributes.ARMOR_TOUGHNESS, baseTough)
                .add(Attributes.FOLLOW_RANGE,24D)
                .add(Attributes.ATTACK_DAMAGE, baseDmg);

    }
}
