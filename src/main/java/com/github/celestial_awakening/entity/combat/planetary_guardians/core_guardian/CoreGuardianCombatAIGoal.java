package com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CoreGuardianCombatAIGoal extends GenericCombatAIGoal {
    CoreGuardianShockwave shockwave=new CoreGuardianShockwave(this.mob,30,5000,30,10,20);
    CoreGuardianRockSmash rockSmash=new CoreGuardianRockSmash(this.mob,20,3000,10,15);
    CoreGuardianShiftingPlates shiftingPlates=new CoreGuardianShiftingPlates(this.mob,0,14000,0,0,20);
    CoreGuardianHarden harden=new CoreGuardianHarden(this.mob,0,20000,0,0,10);
    List<GenericAbility> abilities=List.of(shockwave,shiftingPlates, harden);
    public CoreGuardianCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
    public void tick(){
        LivingEntity target=this.mob.getTarget();
        double cdMult=getCDDecMult();
        double cdDec=cdMult*100;
        abilities.forEach(ability->{
            ability.decreaseCD((int) (cdDec));
        });
        shiftingPlates.updatePlates();
        if (this.mob.isActing){
            currentAbility.executeAbility(this.mob.getTarget());
        }
        else{
            AtomicInteger lowestP= new AtomicInteger(Integer.MAX_VALUE);
            if (this.mob.tickCount%20<=1){
                System.out.println("selecting ability ");
                abilities.forEach(ability->{
                    ability.calcPriority();
                });
            }
            abilities.forEach(ability->{
                int p=ability.getPriority();
                if (p>0){
                    System.out.println(ability.getAbilityName() + "   ability priority is  " +  p);
                }

                if (p>0 && p< lowestP.get()){
                    currentAbility=ability;
                    lowestP.set(p);
                }
            });
            if (currentAbility!=null){
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(target);
                System.out.println("STARITING ABILITY " + currentAbility.getAbilityName());
                currentAbility.startAbility(target,d0);
            }

        }
        movementController(target);
    }
}
