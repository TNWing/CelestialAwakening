package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

public class NebureCombatAIGoal extends GenericCombatAIGoal {
    NebureBasicAttack basicAttack=new NebureBasicAttack(this.mob,20,20,0,8);
    NebureLightEntanglement lightEntanglement=new NebureLightEntanglement(this.mob,25,250,80,0);
    NebureScorchingRays scorchingRays=new NebureScorchingRays(this.mob,30,160,15,0);
    NebureSolarExpansion solarExpansion=new NebureSolarExpansion(this.mob,20,100,32,20);
    public NebureCombatAIGoal(AbstractCAMonster mob) {
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
