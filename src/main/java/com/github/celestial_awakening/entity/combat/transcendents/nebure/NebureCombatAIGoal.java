package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class NebureCombatAIGoal extends GenericCombatAIGoal {
    NebureBasicAttack basicAttack=new NebureBasicAttack(this.mob,20,2000,0,8);
    NebureLightEntanglement lightEntanglement=new NebureLightEntanglement(this.mob,25,25000,80,0);
    NebureScorchingRays scorchingRays=new NebureScorchingRays(this.mob,30,16000,15,0);
    NebureSolarExpansion solarExpansion=new NebureSolarExpansion(this.mob,20,10000,32,20);
    List<GenericAbility> abilities=List.of(basicAttack,scorchingRays,solarExpansion,lightEntanglement);
    public NebureCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
    public void tick(){
        LivingEntity target=this.mob.getTarget();
        double cdMult=getCDDecMult();
        abilities.forEach(ability->{
            ability.decreaseCD((int) (100*cdMult));
        });
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
