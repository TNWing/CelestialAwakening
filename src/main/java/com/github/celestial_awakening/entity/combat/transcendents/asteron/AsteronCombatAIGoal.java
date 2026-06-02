package com.github.celestial_awakening.entity.combat.transcendents.asteron;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.living.transcendents.Asteron;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AsteronCombatAIGoal extends GenericCombatAIGoal {
    public AsteronCombatAIGoal(Asteron mob) {
        super(mob);
    }
    AsteronPiercingRays piercingRays=new AsteronPiercingRays(this.mob,2000,40,15,30);
    AsteronBasicAttack basicAttack=new AsteronBasicAttack(this.mob,0,1500,0,0);//(this.mob,10,30,1,10,15);
    List<GenericAbility> abilities=List.of(basicAttack,piercingRays);
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
            currentAbility=basicAttack;
            if (piercingRays.getCurrentCD()==0){
                currentAbility=piercingRays;
            }
            double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(target);
            currentAbility.startAbility(target,d0);
        }
        movementController(target);
    }
}
