package com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Predicate;

public class CoreGuardianShockwave extends GenericAbility {
    /*
    how this should work
    sends a shockwave that activates 3 times
    first 2 just slow
    third instance slows and damages
     */
    Vec3 startPos;
    Vec3 endPos;
    Predicate pred= o -> true;
    int triggers;
    int triggerDmgCnt=2;
    AABB rayBox;
    DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FLY_INTO_WALL),this.mob);
    double angle;
    public CoreGuardianShockwave(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime,int p) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            if (this.currentStateTimer%15==1){
                triggers++;

                List<LivingEntity> livingEntityList=this.mob.level().getEntitiesOfClass(LivingEntity.class,rayBox,pred);

                //not sure if these vals even work?
                /*
                need to use the angle of the ray to calculate this
                 */
                float xOffset= (float) Math.sin(Math.toRadians(angle));
                float zOffset=(float) Math.cos(Math.toRadians(angle));
                System.out.println("X OFF " + xOffset + "   zoff "  + zOffset);
                float yOffset=0.5f;
                Vec3[] rayOffsets = new Vec3[] {
                        new Vec3(-xOffset, -yOffset, 0),
                        new Vec3(-xOffset, yOffset, 0),
                        new Vec3(xOffset, -yOffset, 0),
                        new Vec3(xOffset, yOffset, 0),
                        new Vec3(0, -yOffset, -zOffset),
                        new Vec3(0, -yOffset, zOffset),
                        new Vec3(0, yOffset, -zOffset),
                        new Vec3(0, yOffset, zOffset)
                };
                Set<LivingEntity> entitiesToHit=new HashSet<>();
                for (LivingEntity entity: livingEntityList) {
                    if (entitiesToHit.contains(entity)){
                        continue;
                    }
                    AABB aabb=entity.getBoundingBox();
                    for (int i=0;i< rayOffsets.length;i++){
                        Vec3 rayOffset=rayOffsets[i];
                        Optional<Vec3> edgeRay= aabb.clip(startPos.add(rayOffset), endPos.add(rayOffset));//does the entity intersect with the ray;
                        if (edgeRay.isPresent()){
                            entitiesToHit.add(entity);
                        }
                    }
                }
                for (LivingEntity entity:entitiesToHit) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,70,1));
                }
                if (triggers>triggerDmgCnt){
                    //deal damage
                    for (LivingEntity entity:entitiesToHit) {
                        entity.hurt(source,3.5f);
                    }
                }


            }
        }
        this.currentStateTimer--;
        if (this.currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    this.mob.canMove=false;
                    currentStateTimer=abilityExecuteTime;
                    triggers=0;
                    Vec3 dirVec= MathFuncs.getDirVec(this.mob.position(),target.position());
                    startPos=this.mob.position();
                    endPos=dirVec.normalize().scale(7);
                    angle=MathFuncs.getAngFrom2DVec(MathFuncs.getDirVec(startPos,endPos));
                    rayBox=new AABB(startPos,endPos);
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
        return 7;
    }
}
