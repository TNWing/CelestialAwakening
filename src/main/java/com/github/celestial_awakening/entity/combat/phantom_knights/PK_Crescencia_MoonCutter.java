package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.LivingEntity;

public class PK_Crescencia_MoonCutter extends GenericAbility {
    /*
    Summons a row of rotating crescents.
     */
    public PK_Crescencia_MoonCutter(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }

    @Override
    public void executeAbility(LivingEntity target) {

    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 0;
    }

    void summonCrescent(int dir){

    }
}
