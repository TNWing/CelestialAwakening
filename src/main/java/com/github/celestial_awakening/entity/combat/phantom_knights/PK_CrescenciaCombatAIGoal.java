package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PK_CrescenciaCombatAIGoal extends GenericCombatAIGoal {
    PK_CrescenciaBasicAttack basicAttack=new PK_CrescenciaBasicAttack(this.mob,10,20,15,0,99);
    //PhantomKnightChargeAttack chargeAttack=new PhantomKnightChargeAttack(this.mob,50,200,50,30);
    //PK_CrescenciaDualCrescentSlash dualCrescentSlash=new PK_CrescenciaDualCrescentSlash(this.mob,20,160,14,30);
    PK_CrescenciaCrescentWhirlwind crescentWhirlwind=new PK_CrescenciaCrescentWhirlwind(this.mob,20,200,125,30,15);
    //PK_CrescenciaNightSlash nightSlash=new PK_CrescenciaNightSlash(this.mob,20,120,120,30);
    PK_CrescenciaStrikethrough strikethrough=new PK_CrescenciaStrikethrough(this.mob,12,150,40,36,9);
    PK_CrescenciaMoonCutter moonCutter=new PK_CrescenciaMoonCutter(this.mob,8,160,0,10,8);
    List<GenericAbility> abilities=List.of(basicAttack,crescentWhirlwind,moonCutter,strikethrough);
    public PK_CrescenciaCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
    public void tick(){//this tick occurs ever 2 standard ticks
        LivingEntity target=this.mob.getTarget();
        if (this.mob.getBossBarWindup()>=100){
            abilities.forEach(a->{a.decreaseCD(1);});
            if (this.mob.isActing){
                currentAbility.executeAbility(this.mob.getTarget());
            }
            else{
                currentAbility=null;
                AtomicInteger lowestP= new AtomicInteger(100);
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


}
