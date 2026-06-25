package com.github.celestial_awakening.entity.combat.night_prowlers;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;

public class NightProwlerCubCombatAIGoal extends GenericCombatAIGoal {
    NightProwlerBasicAttack basicAttack=new NightProwlerBasicAttack(this.mob,0,1500,0,0);
    protected NightProwlerCubCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
}
