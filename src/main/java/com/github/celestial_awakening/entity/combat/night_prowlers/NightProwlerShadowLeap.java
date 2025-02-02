package com.github.celestial_awakening.entity.combat.night_prowlers;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.NightProwler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class NightProwlerShadowLeap extends GenericAbility {
    boolean canTeleport=true;
    boolean isTeleport=false;
    Vec3 teleportPos=Vec3.ZERO;
    public NightProwlerShadowLeap(NightProwler mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        if (this.getAbilityRange(target)>=dist){
            this.mob.getDirection();
            super.startAbility(target,dist);
            System.out.println("NP LEAP");
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (currentStateTimer>0){
            switch (state){
                case 0:{
                    if (currentStateTimer==30){
                        this.mob.setActionId(3);
                    }

                    break;
                }
                case 1:{//should take around 1.2 seconds total?, so 24 ticks
                /*
                    the first 0.25 seconds (5 ticks) are as normal
                    then, if the leap is a teleport, begin dissolve process for 0.75 seconds (lose collision and rapidly fade).
                    The last 0.2 seconds is reappearing from the ground behind, reenable collisionb and full opacity

                 */
                    if (currentStateTimer==24){
                        this.mob.setActionId(4);
                        Vec3 vec3 = this.mob.getDeltaMovement();
                        Vec3 vec31 = new Vec3(this.mob.getTarget().getX() - this.mob.getX(), 0.0D, this.mob.getTarget().getZ() - this.mob.getZ());
                        if (vec31.lengthSqr() > 1.0E-7D) {
                            vec31 = vec31.normalize().scale(0.4D).add(vec3.scale(0.2D));
                        }
                        this.mob.setDeltaMovement(vec31.x, 0.35D, vec31.z);
                    }
                    else if(isTeleport){

                        if(currentStateTimer<=19 && currentStateTimer>4){
                            if (currentStateTimer==19){
                                ((NightProwler)mob).hasCollision=false;
                                //disable collision
                                this.mob.setBoundingBox(new AABB(0,0,0,0,0,0));
                            }
                        }
                        else if (currentStateTimer==4){
                            System.out.println("TP POS IS " + teleportPos);
                            this.mob.setPos(teleportPos);
                            ((NightProwler)mob).hasCollision=true;
                            this.mob.setBoundingBox(((NightProwler) mob).standardAABB);
                        }
                    }
                    if (currentStateTimer==1){
                        this.mob.doHurtTarget(target);
                    }
                    break;
                }
                case 2:{
                    break;
                }
            }
            this.currentStateTimer--;
        }
        else{
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    Vec3 targetPos=target.position();
                    Vec3 targetDir=target.getLookAngle();
                    targetDir=new Vec3(targetDir.x,0,targetDir.y);

                    for (int i=-1;i>=-2;i--){
                        Vec3 tempDir=targetDir;
                        tempDir=tempDir.scale(i).normalize();//repeat this twice
                        Vec3 teleportPos=targetPos.add(tempDir);
                        BlockPos blockPos=new BlockPos((int)teleportPos.x,(int)teleportPos.y+ 1,(int)teleportPos.z);
                        BlockState blockState=target.level().getBlockState(blockPos);
                        if (!blockState.isAir()){
                            canTeleport=false;
                            break;
                        }
                    }
                    if (canTeleport){
                        Random random=new Random();
                        if (random.nextInt(3)==1){
                            isTeleport=true;
                            teleportPos=targetPos.add(targetDir.scale(-2).normalize());
                        }
                    }
                    break;
                }
                case 1:{
                    state++;
                    currentStateTimer=abilityRecoveryTime;
                    //TODO
                    /*
                    either set act ID to 0 OR make a new act/anim for leap recovery
                     */
                    this.mob.setActionId(0);
                    break;
                }
                case 2:{

                    state=0;
                    canTeleport=true;
                    isTeleport=false;
                    liftRestrictions();
                    break;
                }
            }
        }

    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 4;
    }
}
