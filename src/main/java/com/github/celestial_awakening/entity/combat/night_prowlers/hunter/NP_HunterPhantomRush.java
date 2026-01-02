package com.github.celestial_awakening.entity.combat.night_prowlers.hunter;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class NP_HunterPhantomRush extends GenericAbility {

    DamageSourceIgnoreIFrames repSource=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),this.mob);

    Vec3 pathStart;
    Vec3 pathEnd;
    AABB pathBox;
    List<LivingEntity> entitiesHit;
    Predicate pred;
    Vec3 dir;
    public NP_HunterPhantomRush(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime,int p) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
        name="Phantom Rush";
    }

    @Override
    public void executeAbility(LivingEntity target) {
        switch (state){
            case 0:{
                break;
            }
            case 1:{
                //path isn't the best since it doesnt work for diagonal movement
                //Path path=this.mob.getNavigation().createPath(BlockPos.containing(pathEnd),0);
                //boolean b=this.mob.getNavigation().moveTo(path,this.mob.spdMod*4);
                this.mob.setDeltaMovement(dir.scale(0.5f));
                TargetingConditions conds=TargetingConditions.forCombat().range(1.5f);
                List<LivingEntity> livingEntityList=mob.level().getNearbyEntities(LivingEntity.class,conds,this.mob,mob.getBoundingBox().inflate(0.2f,0,0.2f));
                livingEntityList.forEach(livingEntity -> {
                    entitiesHit.add(livingEntity);
                    this.mob.doHurtTarget(livingEntity);
                });
                break;
            }
            case 2:{
                //pathStart
                pathBox=new AABB(pathStart,pathEnd);
                List<LivingEntity> livingEntityList=mob.level().getEntitiesOfClass(LivingEntity.class,pathBox,null);
                for(LivingEntity livingEntity:livingEntityList){

                    AABB entityBox = livingEntity.getBoundingBox();
                    entityBox.clip(pathStart,pathEnd).ifPresent(vec3 -> {
                        livingEntity.hurt(repSource,1f);
                    });
                }

                break;
            }
        }
        if (--this.currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    pathStart=this.mob.position();
                    dir= MathFuncs.getDirVec(pathStart,target.position());
                    //pathEnd=pathStart.add(new Vec3(dir.x,0,dir.z).scale(9));
                    /*

                    this.mob.setActionId(3);
                    double dist=target.position().distanceTo(this.mob.position());

                    Vec3 dm=new Vec3(dir.x,0,dir.z).scale(0.7*dist).add(0,0,0);
                    this.mob.setDeltaMovement(dm);

                     */
                    //this.mob.moveTo();
                    break;
                }
                case 1:{
                    state++;
                    currentStateTimer=abilityRecoveryTime;
                    pathEnd=this.mob.position();
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
        return 9;
    }
    /*
    basically, deal damage during execution, then another instance at the end of recovery time
     */
}
