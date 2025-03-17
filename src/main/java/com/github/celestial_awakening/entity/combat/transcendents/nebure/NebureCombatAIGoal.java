package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.LivingEntity;

public class NebureCombatAIGoal extends GenericCombatAIGoal {//how is it moving
    NebureBasicAttack basicAttack=new NebureBasicAttack(this.mob,20,40,0,15);
    NebureLightEntanglement lightEntanglement=new NebureLightEntanglement(this.mob,25,250,80,0);
    NebureScorchingRays scorchingRays=new NebureScorchingRays(this.mob,30,200,15,0);
    NebureSolarExpansion solarExpansion=new NebureSolarExpansion(this.mob,20,100,48,20);
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
            else if (scorchingRays.getCurrentCD()==0){
                currentAbility= scorchingRays;
            }
            double d0 = this.mob.distanceToSqr(target);
            if (currentAbility!=null){
                currentAbility.startAbility(target,d0);
            }

        }
        movementController(target);
    }
}
