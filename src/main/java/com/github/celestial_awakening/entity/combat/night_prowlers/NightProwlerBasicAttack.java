package com.github.celestial_awakening.entity.combat.night_prowlers;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

public class NightProwlerBasicAttack extends GenericAbility {
    public NightProwlerBasicAttack(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        name="NP Basic";
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        if (this.getAbilityRange(target)>=dist){
            this.mob.getDirection();
            super.startAbility(target,dist);
            setMoveVals(0,this.getAbilityRange(target),false);
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {
        this.currentStateTimer--;
        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    this.mob.doHurtTarget(target);
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
        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth());
    }
}
