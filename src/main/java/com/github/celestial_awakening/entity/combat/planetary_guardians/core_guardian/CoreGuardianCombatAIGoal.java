package com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;

public class CoreGuardianCombatAIGoal extends GenericCombatAIGoal {
    CoreGuardianShockwave shockwave=new CoreGuardianShockwave(this.mob,30,120,45,10,20);
    public CoreGuardianCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
}
