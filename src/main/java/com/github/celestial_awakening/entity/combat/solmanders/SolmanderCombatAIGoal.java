package com.github.celestial_awakening.entity.combat.solmanders;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SolmanderCombatAIGoal extends GenericCombatAIGoal {
    public SolmanderCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
    SolmanderNewtBasicAttack basicAttack=new SolmanderNewtBasicAttack(this.mob,5,15,5,10,99);
    SolmanderConflagarate conflagarate=new SolmanderConflagarate(this.mob,25,80,1,0,20);
    SolmanderSolarBeam solarBeam = new SolmanderSolarBeam(this.mob,15,100,40,10,10);
    List<GenericAbility> abilities=List.of(basicAttack,solarBeam,conflagarate);
    public void tick(){
        LivingEntity target=this.mob.getTarget();
        abilities.forEach(ability->{
            ability.decreaseCD(1);
        });
        if (this.mob.isActing){
            currentAbility.executeAbility(this.mob.getTarget());
        }
        else{
            AtomicInteger lowestP= new AtomicInteger(Integer.MAX_VALUE);
            abilities.forEach(ability->{
                int p=ability.calcPriority();
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
