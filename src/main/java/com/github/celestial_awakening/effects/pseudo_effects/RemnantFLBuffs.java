package com.github.celestial_awakening.effects.pseudo_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import java.util.ArrayList;
import java.util.Collection;

public class RemnantFLBuffs extends MobEffect {
    protected RemnantFLBuffs(MobEffectCategory category, int c) {
        super(category, c);
    }
    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(livingEntity,attributeMap,amplifier);
        Collection<MobEffectInstance> effects=livingEntity.getActiveEffects();
        ArrayList<MobEffectInstance> buffs=new ArrayList<>();
        int debuffCnt=0;
    }
    /*
    public static final MobEffect MOVEMENT_SPEED =
    register(1, "speed",
    (new MobEffect(MobEffectCategory.BENEFICIAL, color))
    .addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635",
    (double)0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
     */
}
