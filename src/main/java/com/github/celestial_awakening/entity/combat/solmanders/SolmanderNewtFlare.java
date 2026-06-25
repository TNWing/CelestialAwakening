package com.github.celestial_awakening.entity.combat.solmanders;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;

public class SolmanderNewtFlare extends GenericAbility {
    public SolmanderNewtFlare(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime, int p) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
    }

    @Override
    public void executeAbility(LivingEntity target) {
        this.currentStateTimer--;
        if (currentStateTimer<=0){
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
}
