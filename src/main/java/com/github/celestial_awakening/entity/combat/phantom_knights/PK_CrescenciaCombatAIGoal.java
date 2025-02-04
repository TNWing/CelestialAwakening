package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.LivingEntity;

public class PK_CrescenciaCombatAIGoal extends GenericCombatAIGoal {
    PhantomKnightBasicAttack basicAttack=new PhantomKnightBasicAttack(this.mob,20,70,30,0);
    PhantomKnightChargeAttack chargeAttack=new PhantomKnightChargeAttack(this.mob,50,200,50,30);
    PK_CrescenciaDualCrescentSlash dualCrescentSlash=new PK_CrescenciaDualCrescentSlash(this.mob,20,160,14,30);
    PK_CrescenciaCrescentWhirlwind crescentWhirlwind=new PK_CrescenciaCrescentWhirlwind(this.mob,30,380,165,30);
    PK_CrescenciaNightSlash nightSlash=new PK_CrescenciaNightSlash(this.mob,20,200,120,30);
    PK_CrescenciaStrikethrough strikethrough=new PK_CrescenciaStrikethrough(this.mob,26,170,50,40);
    PK_CrescenciaMoonCutter moonCutter=new PK_CrescenciaMoonCutter(this.mob,30,200,0,20);
    public PK_CrescenciaCombatAIGoal(AbstractCALivingEntity mob) {
        super(mob);
    }
    public void tick(){
        LivingEntity target=this.mob.getTarget();
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
            if (crescentWhirlwind.getCurrentCD()==0){
                //currentAbility=crescentWhirlwind;
            }
            if (moonCutter.getCurrentCD()==0){
                currentAbility=moonCutter;
            }
            else if (strikethrough.getCurrentCD()==0){
               //currentAbility=strikethrough;
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
