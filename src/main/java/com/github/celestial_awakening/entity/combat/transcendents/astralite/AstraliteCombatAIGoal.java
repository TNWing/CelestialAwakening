package com.github.celestial_awakening.entity.combat.transcendents.astralite;

import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.LivingEntity;

public class AstraliteCombatAIGoal extends GenericCombatAIGoal {

    AstralitePhotonSurge photonSurge =new AstralitePhotonSurge(this.mob,20,160,120,30);
    AstraliteBasicAttack basicAttack=new AstraliteBasicAttack(this.mob,20,70,30,0);

    public AstraliteCombatAIGoal(AbstractCALivingEntity mob) {
        super(mob);
    }
    public void tick(){
        LivingEntity target=this.mob.getTarget();

        basicAttack.decreaseCD(1);
        photonSurge.decreaseCD(1);
        if (this.mob.isActing){
            currentAbility.executeAbility(this.mob.getTarget());
        }
        else{
            currentAbility=basicAttack;
            if (photonSurge.getCurrentCD()==0){
                currentAbility= photonSurge;
            }
            double d0 = this.mob.distanceToSqr(target);
            currentAbility.startAbility(target,d0);
        }
        movementController(target);
    }
}
