package com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.planetary_guardians.CoreGuardian;
import net.minecraft.world.entity.LivingEntity;

public class CoreGuardianHarden extends GenericAbility {
    public CoreGuardianHarden(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime, int basePriority) {
        super(mob, castTime, CD, executeTime, recoveryTime, basePriority);
    }

    @Override
    public void executeAbility(LivingEntity target) {
        ((CoreGuardian)this.mob).setHardenStacks(5);
        this.currentStateTimer--;
        if (this.currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    break;
                }
                case 1:{
                    state++;
                    currentStateTimer=abilityRecoveryTime;

                    break;
                }
                case 2:{
                    state=0;
                    liftRestrictions();
                    break;
                }
            }
        }
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 0;
    }


    @Override
    public int calcPriority(){
        System.out.println("HARDEN PRIO");
        System.out.println(this.mob.getHealth()/this.mob.getMaxHealth());
        if (this.getCurrentCD()>0 || ((CoreGuardian) this.mob).getHardenStacks()>0 || this.mob.getHealth()/this.mob.getMaxHealth()>0.85f){
            return -1;
        }
        int p= this.getBasePriority();
        return p;
    }

    @Override
    public String getAbilityName(){
        return "Harden";
    }
}
