package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import java.util.*;

public class PurgingLight extends MobEffect {
    /*
    Cleansing Flames
    When the timer runs out, remove a random buff from
    the target. Deal a burst of fire damage that bypasses
    I frames and does not trigger I frames, with the damage
    increasing for each debuff on the target (being on fire counts as a debuff).

     */
    public PurgingLight(MobEffectCategory category, int color)  {
        super(category,color);
    }

    Random rand = new Random();
    //the main thing is that there doesnt seem to be any other function that is triggered when the effect ends.
    //i could take a peek at other modded effects (like srp coth, headhunter's impending doom) to see if I missed some other trigger
    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(livingEntity,attributeMap,amplifier);
        Collection<MobEffectInstance> effects=livingEntity.getActiveEffects();
        ArrayList<MobEffectInstance> buffs=new ArrayList<>();
        int debuffCnt=1;
        for (MobEffectInstance effectInstance:effects) {
            MobEffectCategory category=effectInstance.getEffect().getCategory();
            if (category==MobEffectCategory.BENEFICIAL){
                buffs.add(effectInstance);
            }
            else if (category==MobEffectCategory.HARMFUL){
                debuffCnt++;
            }
        }
        if (livingEntity.isOnFire()){
            debuffCnt++;
        }
        if (debuffCnt>0){
            livingEntity.hurt(livingEntity.damageSources().magic(),debuffCnt*(1+amplifier/(amplifier+1)));
        }
        if (!buffs.isEmpty()){
            int index=rand.nextInt(buffs.size());
            livingEntity.removeEffect(buffs.get(index).getEffect());
        }
    }
}
