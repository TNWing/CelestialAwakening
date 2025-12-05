package com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

public class CoreGuardianShockwave extends GenericAbility {
    /*
    how this should work
    sends a shockwave that activates 3 times
    first 2 just slow
    third instance slows and damages
     */
    public CoreGuardianShockwave(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            if (this.currentStateTimer%10==0){
                
            }
        }
        this.currentStateTimer--;
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 0;
    }
}
