package com.github.celestial_awakening.entity.combat.night_prowlers.hunter;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.night_prowlers.AbstractNightProwler;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class NP_HunterUmbraWarp extends GenericAbility {
    List<LivingEntity> entitiesHitBySlam=new ArrayList<>();
    DamageSource slamSource=new DamageSource(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),this.mob);
    DamageSourceIgnoreIFrames slamWaveSource=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FLY_INTO_WALL),this.mob);

    DamageSourceIgnoreIFrames vortexSource=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),this.mob);
    boolean doVortex;
    int vortexTimer;
    float horiDiff=12f;
    float vertDiff=2.6f;
    float waveHoriDiff=6.5f;
    float waveVertDiff=0.4f;
    public NP_HunterUmbraWarp(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime, int p,int vt) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
        vortexTimer=vt;
        name="Umbra Warp";
    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            if (doVortex){
                Vec3 pos=this.mob.position();
                AABB aabb=new AABB(pos.x-horiDiff,1f + pos.y-vertDiff,pos.z-horiDiff,pos.x+horiDiff,1f + pos.y+vertDiff,pos.z+horiDiff);
                List<LivingEntity> entities= this.mob.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob,List.of(AbstractNightProwler.class)));
                for (LivingEntity entity:entities) {

                    Vec3 dir=MathFuncs.getDirVec(entity.position(),pos);
                    entity.push(dir.x*0.1f,dir.y*0.1f,dir.z*0.1f);
                    float amt=0.1f;
                    if (entity.distanceTo(this.mob)<4.5f){
                        amt=0.17f;
                    }
                    entity.hurt(vortexSource,amt);
                }
            }
            else if (currentStateTimer<abilityExecuteTime*2/3){
                this.mob.move(MoverType.SELF, this.mob.position().subtract(0,0.4f,0));
                List<LivingEntity> entities= this.mob.level().getEntitiesOfClass(LivingEntity.class,this.mob.getBoundingBox().inflate(0.6f,0.3f,0.6f), CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob,List.of(AbstractNightProwler.class)).and((e)->!entitiesHitBySlam.contains(e)));
                for (LivingEntity entity:entities) {
                    entitiesHitBySlam.add(entity);
                    entity.hurt(slamSource,4.5f);
                }
                if (this.mob.onGround()){
                    Vec3 pos=this.mob.position();
                    AABB aabb=new AABB(pos.x-waveHoriDiff,1f + pos.y-waveVertDiff,pos.z-waveHoriDiff,pos.x+waveHoriDiff,1f + pos.y+waveVertDiff,pos.z+waveHoriDiff);
                    entities= this.mob.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob,List.of(AbstractNightProwler.class)));
                    for (LivingEntity entity:entities) {
                        entity.hurt(slamWaveSource,2.5f);
                    }
                    this.currentStateTimer=0;
                }
            }
        }
        if (--this.currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=vortexTimer;
                    doVortex=this.mob.distanceToSqr(target)>30;
                    if (!doVortex){
                        currentStateTimer=abilityExecuteTime;
                        this.mob.setNoGravity(true);
                        //tp on top of target

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
                    entitiesHitBySlam.clear();
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
