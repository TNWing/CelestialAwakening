package com.github.celestial_awakening.entity.combat.night_prowlers.hunter;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.combat.GenericCombatAIGoal;
import com.github.celestial_awakening.entity.combat.night_prowlers.NightProwlerBasicAttack;
import com.github.celestial_awakening.entity.combat.night_prowlers.NightProwlerShadowLeap;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.night_prowlers.ProwlerWhelp;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NP_HunterCombatAIGoal extends GenericCombatAIGoal {
    public NP_HunterCombatAIGoal(AbstractCAMonster mob) {
        super(mob);
    }
    NightProwlerBasicAttack basicAttack=new NightProwlerBasicAttack(this.mob,0,15,0,0);
    NightProwlerShadowLeap shadowLeap=new NightProwlerShadowLeap((ProwlerWhelp) this.mob,15,70,12,5,10);
    NP_HunterPhantomRush phantomRush=new NP_HunterPhantomRush(this.mob,10,90,15,10,7);
    NP_HunterUmbraWarp umbraWarp=new NP_HunterUmbraWarp(this.mob,20,100,15,25,5,30);
    List<GenericAbility> abilities=List.of(basicAttack,phantomRush,shadowLeap,umbraWarp);
    public void tick(){
        LivingEntity target=this.mob.getTarget();
        abilities.forEach(a-> a.decreaseCD(1));
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
