package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class NebureSolarExpansion extends GenericAbility {
    Predicate pred= CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob,List.of(AbstractTranscendent.class));
    DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),this.mob);
    ParticleOptions particleType = ParticleTypes.LAVA;
    int particleCnt=4;
    float particleSpd=0;
    Vec3 centerPos;
    List<Vec3> particlePos;
    float donutAreaIncrement=1.5f;
    float innerRad=0;
    float outerRad=donutAreaIncrement;

    int donutAreaInterval=25;

    public NebureSolarExpansion(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }

    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.getDirection();
            this.mob.canMove=false;
            super.startAbility(target,dist);
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);
            calculateWhereToDraw(outerRad,innerRad);
        }

    }
    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            drawParticles((ServerLevel) this.mob.level());
            if (this.currentStateTimer%donutAreaInterval==0){
                damageArea(outerRad,innerRad);
                innerRad+=donutAreaIncrement;
                outerRad+=donutAreaIncrement;
                calculateWhereToDraw(outerRad,innerRad);
                drawParticles((ServerLevel) this.mob.level());
            }
            else if (this.currentStateTimer%(donutAreaInterval/5)==0){
                drawParticles((ServerLevel) this.mob.level());
            }
        }
        else if (state==0 && this.currentStateTimer<=donutAreaInterval && this.currentStateTimer%(donutAreaInterval/5)==0){
            drawParticles((ServerLevel) this.mob.level());
        }
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
        return 10;
    }

    void calculateWhereToDraw(float oRad,float iRad){
        particlePos.clear();
        for (int ang=0;ang<360;ang+=15){
            Vec3 offset=MathFuncs.get2DVecFromAngle(ang);
            Vec3 pos1=centerPos.add(offset.scale(0.3+oRad-donutAreaIncrement));
            Vec3 pos2=centerPos.add(offset.scale(0.6+oRad-donutAreaIncrement));
            Vec3 pos3=centerPos.add(offset.scale(0.9+oRad-donutAreaIncrement));
            particlePos.add(pos1);
            particlePos.add(pos2);
            particlePos.add(pos3);
        }
    }

    void drawParticles(ServerLevel lvl){
        for (Vec3 pos:particlePos) {
            lvl.sendParticles(particleType,pos.x,pos.y+1,pos.z,particleCnt,0,0,0,particleSpd);
        }
    }


    void damageArea(float oRad,float iRad){
        List<LivingEntity> entityList= MathFuncs.getEntitiesIn2DDonutArea(LivingEntity.class,centerPos,this.mob.level(),oRad,iRad,2,pred);
        for (LivingEntity entity :entityList) {
            entity.hurt(source,4f);
        }
    }
}
