package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class NebureBasicAttack extends GenericAbility {
    ParticleOptions particleType = ParticleTypes.ELECTRIC_SPARK;
    public NebureBasicAttack(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.canMove=false;
            super.startAbility(target,dist);
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);

        }
    }
    @Override
    public void executeAbility(LivingEntity target) {
        if (state==0){
            int particleCnt=2*(int)(5-currentStateTimer/5);
            ServerLevel serverLevel= (ServerLevel) target.level();
            Random random=new Random();
            for (int i=0;i<particleCnt;i++){
                Vec3 particlePos=this.mob.position().add(this.mob.getLookAngle()).add(random.nextFloat(0.8f),1.25f+random.nextFloat(0.8f),random.nextFloat(0.8f));//will probs need to use yrot instead
                serverLevel.sendParticles(particleType, particlePos.x, particlePos.y, particlePos.z, 1, 0, 0, 0, 0);
            }

        }
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
        return 8;
    }
}
