package com.github.celestial_awakening.entity.combat.transcendents.astralite;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AstraliteCombatAIGoal extends GenericCombatAIGoal {

    AstralitePhotonSurge photonSurge =new AstralitePhotonSurge(this.mob,10,11000,80,30);
    AstraliteBasicAttack basicAttack=new AstraliteBasicAttack(this.mob,18,3000,0,0);
    List<GenericAbility> abilities=List.of(basicAttack,photonSurge);
    public AstraliteCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
    public void tick(){
        LivingEntity target=this.mob.getTarget();
        double cdMult=getCDDecMult();
        double cdDec=cdMult*100;
        abilities.forEach(ability->{
            ability.decreaseCD((int) (100*cdMult));
        });
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
