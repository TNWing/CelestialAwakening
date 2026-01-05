package com.github.celestial_awakening.entity.combat.night_prowlers.mauler;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

public class NP_MaulerBite extends GenericAbility {
    public NP_MaulerBite(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime, int basePriority) {
        super(mob, castTime, CD, executeTime, recoveryTime, basePriority);
    }

    @Override
    public void executeAbility(LivingEntity target) {

    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 0;
    }
}
