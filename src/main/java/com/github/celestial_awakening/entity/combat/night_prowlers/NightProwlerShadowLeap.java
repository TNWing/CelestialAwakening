package com.github.celestial_awakening.entity.combat.night_prowlers;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.NightProwler;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Random;
import java.util.function.Predicate;

public class NightProwlerShadowLeap extends GenericAbility {
    boolean canTeleport=true;
    boolean isTeleport=false;
    Vec3 teleportPos=Vec3.ZERO;
    Predicate pred= CA_Predicates.opposingTeams_IgnoreSameClass_Predicate((NightProwler)this.mob);
    TargetingConditions conds=TargetingConditions.forCombat().selector(pred).ignoreLineOfSight().ignoreInvisibilityTesting();

    public NightProwlerShadowLeap(NightProwler mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange = Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.getDirection();
            super.startAbility(target,dist);
            this.mob.canMove=false;
            setMoveVals(0,this.getAbilityRange(target),false);
            this.mob.setActionId(3);
        }
        else{
            System.out.println("FAILED TO START ,  DIST FOR LEAP IS " + dist + "  AND WE GOT RANGE " + abilityRange);
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (currentStateTimer>0){
            switch (state){
                case 1:{//should take around 1.2 seconds total?, so 24 ticks
                /*
                    the first 0.25 seconds (5 ticks) are as normal
                    then, if the leap is a teleport, begin dissolve process for 0.75 seconds (lose collision and rapidly fade).
                    The last 0.2 seconds is reappearing from the ground behind, reenable collisionb and full opacity

                 */
                    if (currentStateTimer==24){
                        System.out.println("STARTING AT " + this.mob.position());
                        this.mob.setActionId(4);
                        Vec3 vec3 = this.mob.getDeltaMovement();
                        Vec3 vec31 = new Vec3(this.mob.getTarget().getX() - this.mob.getX(), 0.0D, this.mob.getTarget().getZ() - this.mob.getZ());
                        if (vec31.lengthSqr() > 1.0E-7D) {
                            vec31 = vec31.normalize().scale(2.1D).add(vec3.scale(0.2D));
                        }
                        this.mob.setDeltaMovement(vec31.x, 0.35D, vec31.z);
                    }
                    else if(isTeleport){
                        //TODO: make this cleaner at some point
                        if(currentStateTimer<=19 && currentStateTimer>4){
                            if (currentStateTimer==19){
                                ((NightProwler)mob).hasCollision=false;
                                //disable collision
                                this.mob.setBoundingBox(new AABB(0,0,0,0,0,0));
                            }
                        }
                        else if (currentStateTimer==4){
                            System.out.println("TP POS IS " + teleportPos);
                            System.out.println("CURRENT POs " + this.mob.position());
                            this.mob.setPos(teleportPos);
                            System.out.println("new POs " + this.mob.position());
                            ((NightProwler)mob).hasCollision=true;
                            this.mob.setBoundingBox(((NightProwler) mob).standardAABB);
                            AABB aabb=new AABB(this.mob.position(),this.mob.position());
                            aabb=aabb.inflate(1.4f);
                            System.out.println(aabb);

                            LivingEntity entity=this.mob.level().getNearestEntity(LivingEntity.class,conds,this.mob,this.mob.position().x,this.mob.position().y,this.mob.position().z,aabb);
                            if (entity!=null){
                                float f = (float)this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
                                System.out.println("TP found");
                                boolean b=entity.hurt(this.mob.damageSources().mobAttack(this.mob), f*1.2f);
                                System.out.println("DID DMG " + b);
                                currentStateTimer=1;
                                this.mob.setDeltaMovement(Vec3.ZERO);
                            }
                        }
                        else if (currentStateTimer<4){

                        }
                    }
                    else{
                        System.out.println("isTeleport "+isTeleport);
                        AABB aabb=new AABB(this.mob.position(),this.mob.position());
                        aabb=aabb.inflate(1.4f);
                        System.out.println("mob POs " + this.mob.position());
                        System.out.println(aabb);
                        LivingEntity entity=this.mob.level().getNearestEntity(LivingEntity.class,conds,this.mob,this.mob.position().x,this.mob.position().y,this.mob.position().z,aabb);
                        System.out.println("CHECKING FOR ENT");
                        if (entity!=null){
                            float f = (float)this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
                            System.out.println("ound");
                            boolean b=entity.hurt(this.mob.damageSources().mobAttack(this.mob), f*1.2f);
                            this.mob.setDeltaMovement(Vec3.ZERO);
                            System.out.println("DID DMG " + b);
                            currentStateTimer=1;
                        }

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
        return 2.2D;
    }
}
