package com.github.celestial_awakening.entity.combat.solmanders;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SolmanderUroCombatAIGoal extends GenericCombatAIGoal {
    SolmanderNewtBasicAttack basicAttack=new SolmanderNewtBasicAttack(this.mob,5,1500,5,10,99);
    SolmanderConflagration conflagration =new SolmanderConflagration(this.mob,25,8000,1,0,20);
    SolmanderSolarBeam solarBeam = new SolmanderSolarBeam(this.mob,15,10000,40,10,10);
    List<GenericAbility> abilities=List.of(basicAttack,solarBeam, conflagration);
    public SolmanderUroCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
    public void tick(){
        LivingEntity target=this.mob.getTarget();
        double cdMult=getCDDecMult();
        double cdDec=cdMult*100;
        abilities.forEach(ability->{
            ability.decreaseCD((int) (cdDec));
        });
        if (this.mob.isActing){
            currentAbility.executeAbility(this.mob.getTarget());
        }
        else{
            AtomicInteger lowestP= new AtomicInteger(Integer.MAX_VALUE);
            if (this.mob.tickCount%20<=1){
                abilities.forEach(ability->{
                    ability.calcPriority();
                });
            }
            abilities.forEach(ability->{
                int p=ability.getPriority();
                if (p>0 && p< lowestP.get()){
                    currentAbility=ability;
                    lowestP.set(p);
                }
            });
            if (currentAbility!=null){
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(target);
                currentAbility.startAbility(target,d0);
            }

        }
        movementController(target);
    }
}
