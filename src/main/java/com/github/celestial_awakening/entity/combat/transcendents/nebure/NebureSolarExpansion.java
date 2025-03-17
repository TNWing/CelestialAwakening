package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.init.MobEffectInit;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class NebureSolarExpansion extends GenericAbility {
    Predicate pred= CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob,List.of(AbstractTranscendent.class));
    DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),this.mob);
    ParticleOptions particleType = ParticleTypes.ASH;

    ParticleOptions particleTypeD = ParticleTypes.FLASH;

    float particleSpd=0;
    Vec3 centerPos;
    List<Vec3> particlePosWarning =new ArrayList<>();
    List<Vec3> particlePosDamage=new ArrayList<>();
    float donutAreaIncrement=1.5f;
    float innerRad;
    float outerRad;

    int donutAreaInterval=8;
    int donutIntervalDiv=4;

    public NebureSolarExpansion(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }

    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.canMove=false;
            innerRad=0.8f;
            outerRad=donutAreaIncrement+innerRad;
            centerPos=this.mob.position();
            super.startAbility(target,dist);
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);
            calculateWhereToDraw(outerRad,innerRad);

        }
    }
    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            if (this.currentStateTimer%donutAreaInterval==0){
                damageArea(outerRad,innerRad);
                drawDamage((ServerLevel) this.mob.level());
                innerRad+=donutAreaIncrement;
                outerRad+=donutAreaIncrement;
                calculateWhereToDraw(outerRad,innerRad);
            }
            else if (this.currentStateTimer%(donutAreaInterval/donutIntervalDiv)==0 && this.currentStateTimer>donutAreaInterval){
                drawWarning((ServerLevel) this.mob.level());
            }
        }
        else if (state==0 && this.currentStateTimer<=donutAreaInterval && this.currentStateTimer%(donutAreaInterval/donutIntervalDiv)==0){
            drawWarning((ServerLevel) this.mob.level());
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
        return 6;
    }

    void calculateWhereToDraw(float oRad,float iRad){
        particlePosWarning.clear();
        particlePosDamage.clear();
        float angInc=16-((oRad/donutAreaIncrement)-1)*2f;
        for (int ang=0;ang<360;ang+=angInc){
            Vec3 offset=MathFuncs.get2DVecFromAngle(ang);

            particlePosDamage.add(centerPos.add(offset.scale(oRad)));
            Vec3 pos1=centerPos.add(offset.scale(0.3+oRad-donutAreaIncrement));
            Vec3 pos2=centerPos.add(offset.scale(0.6+oRad-donutAreaIncrement));
            Vec3 pos3=centerPos.add(offset.scale(0.9+oRad-donutAreaIncrement));
            particlePosWarning.add(pos1);
            particlePosWarning.add(pos2);
            particlePosWarning.add(pos3);

        }
    }

    void drawWarning(ServerLevel lvl){
        for (Vec3 pos: particlePosWarning) {
            lvl.sendParticles(particleType,pos.x,pos.y+2,pos.z,5,0,0,0,particleSpd);
        }
    }
    void drawDamage(ServerLevel lvl){
        for (Vec3 pos: particlePosDamage) {
            lvl.sendParticles(particleTypeD,pos.x,pos.y+0.7f,pos.z,1,0,0,0,particleSpd);
        }
    }

    void damageArea(float oRad,float iRad){
        List<LivingEntity> entityList= MathFuncs.getEntitiesIn2DDonutArea(LivingEntity.class,centerPos,this.mob.level(),oRad,iRad,2,pred);
        for (LivingEntity entity :entityList) {
            entity.hurt(source,(float)this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 1.5f);
            MobEffectInstance purgingLight=new MobEffectInstance(MobEffectInit.PURGING_LIGHT.get(),140,1);
            entity.addEffect(purgingLight);
            if (entity instanceof Player){

            }
        }
    }
}
