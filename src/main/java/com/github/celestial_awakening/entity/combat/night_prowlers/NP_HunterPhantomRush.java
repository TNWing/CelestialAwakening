package com.github.celestial_awakening.entity.combat.night_prowlers;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class NP_HunterPhantomRush extends GenericAbility {

    Vec3 pathStart;
    Vec3 pathEnd;
    AABB pathBox;
    List<LivingEntity> entitiesHit;
    Predicate pred;

    public NP_HunterPhantomRush(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime,int p) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
    }

    @Override
    public void executeAbility(LivingEntity target) {
        switch (state){
            case 0:{
                break;
            }
            case 1:{
                List<LivingEntity> livingEntityList=mob.level().getNearbyEntities(LivingEntity.class,null,this.mob,mob.getBoundingBox().inflate(0.2f,0,0.2f));
                livingEntityList.forEach(livingEntity -> {
                    entitiesHit.add(livingEntity);
                    this.mob.doHurtTarget(livingEntity);
                });
                break;
            }
            case 2:{
                //pathStart
                List<LivingEntity> livingEntityList=mob.level().getEntitiesOfClass(LivingEntity.class,pathBox,null);
                for(LivingEntity livingEntity:livingEntityList){
                    AABB entityBox = livingEntity.getBoundingBox();
                    entityBox.clip(pathStart,pathEnd).ifPresent(vec3 -> {
                        livingEntity.hurt(null,1f);
                    });
                }

                break;
            }
        }
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 9;
    }
    /*
    basically, deal damage during execution, then another instance at the end of recovery time
     */
}
