package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.projectile.LightRay;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class NebureScorchingRays extends GenericAbility {
    double[] angles;
    float aChange;
    float angStartOffset;
    float angDiff;
    int dir;
    ParticleOptions particleType = ParticleTypes.ELECTRIC_SPARK;
    public NebureScorchingRays(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        if (mob.level().getRandom().nextInt(2)==0){
            dir=-1;
        }
        else{
            dir=1;
        }
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.canMove=false;
            super.startAbility(target,dist);
            int diff=target.level().getDifficulty().getId();
            dir=-dir;
            int v=(diff+1)*4;
            angles=new double[v];
            angDiff=360f/(v);
            /*
            E:8
            M:12
            H:16
             */
            aChange=0;
            angStartOffset=target.level().random.nextInt(360);
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);
        }
    }
    @Override
    public void executeAbility(LivingEntity target) {
        if (state==0){
            if (this.currentStateTimer%1==0){
                for (int i=0;i< angles.length;i++){

                    float ang= i*angDiff+dir*angStartOffset;
                    angles[i]=Math.toRadians(ang);
                }
                drawLine((ServerLevel) target.level(),this.mob.position());
                aChange+=3;
                angStartOffset+=Math.cos(Math.toRadians(aChange))*14;
            }

        }
        else if (state==1){

        }
        currentStateTimer--;

        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    //create light rays
                    for (int i=0;i<angles.length;i++){
                        double ang=angles[i];
                        LightRay ray=LightRay.create(this.mob.level(),30, (float) (this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue()*1.75f));
                        ray.setOwner(this.mob);
                        ray.initDims(0.25f,0,0.25f,0,0.4f,8f,0,2f);
                        ray.setHAng(-(float) Math.toDegrees(ang)+90);
                        ray.setXPR(90);
                        ray.setPos(new Vec3(Math.cos(ang),0,Math.sin(ang)).add(this.mob.position()).add(0,1.25f,0));
                        this.mob.level().addFreshEntity(ray);
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

    public void drawLine(ServerLevel serverLevel,Vec3 centerPt){
        for (int i=0;i< angles.length;i++){
            double ang= angles[i];
            Vec3 dirVec=new Vec3(Math.cos(ang),0,Math.sin(ang));
            for (float d=1.5f;d<8.5f;d+=0.4f){
                Vec3 drawPt=dirVec.scale(d).add(0,1.25f,0).add(centerPt);
                serverLevel.sendParticles(particleType, drawPt.x,drawPt.y,drawPt.z, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 7;
    }
}
