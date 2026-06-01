package com.github.celestial_awakening.entity.combat.night_prowlers;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.night_prowlers.ProwlerWhelp;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class NightProwlerCombatAIGoal extends GenericCombatAIGoal {
    public NightProwlerCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
    NightProwlerBasicAttack basicAttack=new NightProwlerBasicAttack(this.mob,0,1500,0,0);
    NightProwlerShadowLeap shadowLeap=new NightProwlerShadowLeap((ProwlerWhelp) this.mob,15,7000,12,5);
    List<GenericAbility> abilities=List.of(basicAttack,shadowLeap);
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
            if (shadowLeap.getCurrentCD()==0){
                currentAbility=shadowLeap ;
            }
            double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(target);
            currentAbility.startAbility(target,d0);
        }
        movementController(target);
    }
}
