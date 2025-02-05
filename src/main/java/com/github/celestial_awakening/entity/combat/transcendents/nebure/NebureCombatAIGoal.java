package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.LivingEntity;

public class NebureCombatAIGoal extends GenericCombatAIGoal {
    NebureBasicAttack basicAttack;
    NebureLightEntanglement lightEntanglement;
    NebureScorchingRays scorchingRays;
    NebureSolarExpansion solarExpansion=new NebureSolarExpansion(this.mob,70,300,100,50);
    public NebureCombatAIGoal(AbstractCALivingEntity mob) {
        super(mob);
    }
    public void tick(){
        LivingEntity target=this.mob.getTarget();
        basicAttack.decreaseCD(1);
        solarExpansion.decreaseCD(1);
        lightEntanglement.decreaseCD(1);
        scorchingRays.decreaseCD(1);

        if (this.mob.isActing){
            currentAbility.executeAbility(this.mob.getTarget());
        }
        else{
            currentAbility=basicAttack;
            if (solarExpansion.getCurrentCD()==0){
                currentAbility= solarExpansion;
            }
            double d0 = this.mob.distanceToSqr(target);
            currentAbility.startAbility(target,d0);
        }
        movementController(target);
    }
}
