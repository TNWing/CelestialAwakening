package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.LivingEntity;

public class PhantomKnightCombatAIGoal extends GenericCombatAIGoal {
    PhantomKnightBasicAttack basicAttack=new PhantomKnightBasicAttack(this.mob,20,70,30,0);
    protected PhantomKnightCombatAIGoal(AbstractCALivingEntity mob) {
        super(mob);
    }
    public void tick(){
        LivingEntity target=this.mob.getTarget();
        movementController(target);
        basicAttack.decreaseCD(1);

        if (this.mob.isActing){
            currentAbility.executeAbility(this.mob.getTarget());
        }
        else{
            currentAbility=basicAttack;
            /*
            if (photonSurge.getCurrentCD()==0){
                currentAbility= photonSurge;
            }

             */
            double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(target);
            currentAbility.startAbility(target,d0);
        }
    }
}
