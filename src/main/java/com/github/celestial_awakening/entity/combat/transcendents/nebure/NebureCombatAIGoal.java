package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.LivingEntity;

public class NebureCombatAIGoal extends GenericCombatAIGoal {
    NebureBasicAttack basicAttack;
    NebureLightEntanglement lightEntanglement;
    NebureScorchingRays scorchingRays;
    NebureSolarExpansion solarExpansion;
    public NebureCombatAIGoal(AbstractCALivingEntity mob) {
        super(mob);
    }
    public void tick(){
        LivingEntity target=this.mob.getTarget();
/*
        basicAttack.decreaseCD(1);
        photonSurge.decreaseCD(1);
        if (this.mob.isActing){
            currentAbility.executeAbility(this.mob.getTarget());
        }
        else{
            currentAbility=basicAttack;
            if (photonSurge.getCurrentCD()==0){
                currentAbility= photonSurge;
            }
            double d0 = this.mob.distanceToSqr(target);
            currentAbility.startAbility(target,d0);
        }

 */
        movementController(target);
    }
}
