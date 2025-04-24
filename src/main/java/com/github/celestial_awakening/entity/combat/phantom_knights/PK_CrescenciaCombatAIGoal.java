package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

public class PK_CrescenciaCombatAIGoal extends GenericCombatAIGoal {
    PK_CrescenciaBasicAttack basicAttack=new PK_CrescenciaBasicAttack(this.mob,10,20,15,0);
    PhantomKnightChargeAttack chargeAttack=new PhantomKnightChargeAttack(this.mob,50,200,50,30);
    PK_CrescenciaDualCrescentSlash dualCrescentSlash=new PK_CrescenciaDualCrescentSlash(this.mob,20,160,14,30);
    PK_CrescenciaCrescentWhirlwind crescentWhirlwind=new PK_CrescenciaCrescentWhirlwind(this.mob,20,200,125,30);
    PK_CrescenciaNightSlash nightSlash=new PK_CrescenciaNightSlash(this.mob,20,120,120,30);
    PK_CrescenciaStrikethrough strikethrough=new PK_CrescenciaStrikethrough(this.mob,12,150,40,36);
    PK_CrescenciaMoonCutter moonCutter=new PK_CrescenciaMoonCutter(this.mob,8,160,0,10);

    public PK_CrescenciaCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
    public void tick(){//this tick occurs ever 2 standard ticks
        LivingEntity target=this.mob.getTarget();
        if (this.mob.getBossBarWindup()>=100){
            basicAttack.decreaseCD(1);
            chargeAttack.decreaseCD(1);
            dualCrescentSlash.decreaseCD(1);
            crescentWhirlwind.decreaseCD(1);
            strikethrough.decreaseCD(1);
            nightSlash.decreaseCD(1);
            moonCutter.decreaseCD(1);
            if (this.mob.isActing){
                currentAbility.executeAbility(this.mob.getTarget());
            }
            else{
                currentAbility=basicAttack;
                if (crescentWhirlwind.getCurrentCD()==0 && crescentWhirlwind.otherConds(target)){
                    currentAbility=crescentWhirlwind;
                }
                else if (moonCutter.getCurrentCD()==0){
                    currentAbility=moonCutter;
                }
                else if (strikethrough.getCurrentCD()==0){
                    currentAbility=strikethrough;
                }
                else if (dualCrescentSlash.getCurrentCD()==0){
                    //currentAbility=dualCrescentSlash;
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(target);
                currentAbility.startAbility(target,d0);
            }
            movementController(target);
        }

    }
}
