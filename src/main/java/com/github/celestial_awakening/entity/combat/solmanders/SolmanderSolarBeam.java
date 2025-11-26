package com.github.celestial_awakening.entity.combat.solmanders;

import com.github.celestial_awakening.entity.AlertInterface;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.entity.projectile.LightRay;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import org.apache.commons.numbers.quaternion.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
public class SolmanderSolarBeam extends GenericAbility {
    Vec3 targetPos=Vec3.ZERO;
    LightRay ray;
    public SolmanderSolarBeam(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime,int p) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.getDirection();
            this.mob.canMove=false;
            super.startAbility(target,dist);
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==0 && currentStateTimer==abilityCastTime/4){
            targetPos=target.getEyePosition();
        }
        else if (state==1){
            //have ray slowly move
            if (this.mob.distanceToSqr(target.getEyePosition())>Math.pow(30,2)){

                System.out.println("DISCARD RAY");
                ray.discard();
                currentStateTimer=1;
            }
            else{
                /*
                Vec3 currentNorm=ray.calculateMoveVec().normalize();
                Vec3 desiredVec=target.position().subtract(this.mob.position()).normalize();
                double ang=MathFuncs.radBtwnVec(desiredVec,currentNorm);
                double angCap=Math.toRadians(1.5f);
                float newYaw=MathFuncs.getAngFrom2DVec(desiredVec);
                float newV=MathFuncs.getVertAngFromVec(desiredVec);
                if (ang>angCap){
                    double step=(angCap/ang);
                    Vec3 slerpVec =currentNorm.scale(Math.sin((1-step)*ang)/Math.sin(ang)).add(desiredVec.scale(Math.sin(step*ang)/Math.sin(ang))).normalize();
                    newYaw=MathFuncs.getAngFrom2DVec(slerpVec);
                    newV=MathFuncs.getVertAngFromVec(slerpVec);
                }
                System.out.printf("old angs %s %s",ray.getHAng(),ray.getVAng());
                System.out.printf("New angs %s %s",newYaw,newV);
                ray.setHAng(newYaw);
                ray.setVAng(newV);

                 */
            }

        }
        this.currentStateTimer--;
        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    Vec3 dir=targetPos.subtract(this.mob.getEyePosition()).normalize();
                    ServerLevel serverLevel= (ServerLevel) this.mob.level();
                    ray=LightRay.create(serverLevel,abilityExecuteTime*2,1.5f,true,false);
                    float yaw= MathFuncs.getAngFrom2DVec(dir)+180;
                    float v=MathFuncs.getVertAngFromVec(dir);
                    System.out.println("  va  " + v);
                    ray.setPred(o -> !(o instanceof AbstractTranscendent));
                    ray.setOwner(this.mob);
                    ray.setPos(this.mob.getEyePosition());
                    ray.initDims(0.25f,0,0.25f,0,0.4f,30f,0,1f);
                    ray.setHAng(yaw);
                    ray.setPred(CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob, List.of(AbstractTranscendent.class)));
                    ray.setVAng(-90);
                    ray.setStopOnContact(true);
                    ray.setAlertInterface(new AlertInterface() {
                        @Override
                        public void onAlert() {
                            Vec3 pos=ray.getEndPt();
                            for (int x=-1;x<=1;x++){
                                for (int z=-1;z<=1;z++){
                                    BlockPos offsetPos=BlockPos.containing(pos.add(x,0,z));
                                    if (BaseFireBlock.canBePlacedAt(serverLevel, offsetPos, Direction.UP)){
                                        //serverLevel.setBlockAndUpdate(offsetPos, BaseFireBlock.getState(serverLevel,offsetPos));
                                    }
                                }
                            }
                        }

                        @Override
                        public void alertOthers() {

                        }
                    });
                    serverLevel.addFreshEntity(ray);
                    break;
                }
                case 1:{
                    state++;
                    currentStateTimer=abilityRecoveryTime;
                    if (!ray.isRemoved()){
                        ray.discard();
                    }
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
        return 29;
    }
}
