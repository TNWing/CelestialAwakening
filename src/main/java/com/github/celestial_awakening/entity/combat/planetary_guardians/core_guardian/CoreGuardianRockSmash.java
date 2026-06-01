package com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CoreGuardianRockSmash extends GenericAbility {
    public CoreGuardianRockSmash(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }

    @Override
    public void executeAbility(LivingEntity target) {
        this.currentStateTimer--;
        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    //maybe do raycast instead\
                    Vec3 dir=mob.getLookAngle();
                    float lookAng=MathFuncs.getAngFrom2DVec(dir);
                    AABB box=new AABB(mob.position(),mob.position().add(dir.scale(2)));
                    System.out.println("AABB RANGE " + box);
                    List<LivingEntity> entities=mob.level().getEntitiesOfClass(LivingEntity.class,box,null);
                    for (LivingEntity entity:entities) {
                        double ang= MathFuncs.angBtwnDirVec(this.mob.position(),entity.position());
                        if (Math.abs(ang-lookAng)<=20){
                            this.mob.doHurtTarget(entity);
                        }
                    }
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
        return 2.1d;
    }
}
