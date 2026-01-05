package com.github.celestial_awakening.entity.combat.night_prowlers;

import com.github.celestial_awakening.capabilities.MovementModifier;
import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.night_prowlers.AbstractNightProwler;
import com.github.celestial_awakening.entity.projectile.IceShard;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.ProjCapS2CPacket;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class NightProwlerShadowLeap extends GenericAbility {
    boolean canTeleport=true;
    boolean isTeleport=false;
    Vec3 targetPos=Vec3.ZERO;
    Vec3 leapDir=Vec3.ZERO;
    Vec3 teleportPos=Vec3.ZERO;
    float prevOpacity=1;
    double prevDist;//used to check if leapt pass the mark
    float aabbInflate=1.25f;
    float dmg;
    double originalDist;
    ParticleOptions particleType = ParticleTypes.SMOKE;
    Predicate pred= CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob,List.of(AbstractNightProwler.class));
    TargetingConditions conds=TargetingConditions.forCombat().selector(pred).ignoreLineOfSight().ignoreInvisibilityTesting();
    DamageSourceIgnoreIFrames flameLeap=new DamageSourceIgnoreIFrames(mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.IN_FIRE),this.mob);
    public NightProwlerShadowLeap(AbstractNightProwler mob, int castTime, int CD, int executeTime, int recoveryTime) {
        this(mob, castTime, CD, executeTime, recoveryTime,1);
    }
    public NightProwlerShadowLeap(AbstractNightProwler mob, int castTime, int CD, int executeTime, int recoveryTime,int p) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
        name="Shadow Leap";
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange = Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.getDirection();
            super.startAbility(target,dist);
            this.mob.canMove=false;
            setMoveVals(0,this.getAbilityRange(target),false);
            this.mob.setActionId(2);
            dmg= (float) (this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE)*1.2f);
        }
    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (currentStateTimer>0){
            switch (state){
                case 1:{//should take around 1.2 seconds total?, so 24 ticks
                    if (currentStateTimer==12){
                        targetPos=target.position();

                        originalDist=prevDist=this.mob.position().distanceTo(target.position());
                        this.mob.setActionId(3);
                        double dist=target.position().distanceTo(this.mob.position());
                        Vec3 dir=MathFuncs.getDirVec(this.mob.position(),target.position());
                        float yM=0.44f;
                        if (dist>5){
                            yM=0.5f;
                        }
                        Vec3 dm=new Vec3(dir.x,0,dir.z).scale(0.4*dist).add(0,yM,0);
                        this.mob.setDeltaMovement(dm);
                        leapDir=this.mob.getDeltaMovement();
                        if (((AbstractNightProwler)this.mob).getInfuse()==-1){
                            ServerLevel serverLevel= (ServerLevel) this.mob.level();
                            /*
                            for (int i=-1;i<=1;i+=2){
                                IceShard iceShard=IceShard.create(this.mob.level(),200,5.4f,51*i+MathFuncs.getAngFrom2DVec(leapDir),42,1.5f,this.mob);
                                @NotNull LazyOptional<ProjCapability> capOptional=iceShard.getCapability(ProjCapabilityProvider.ProjCap);

                                iceShard.setPos(this.mob.position());
                                int finalI = i;
                                capOptional.ifPresent(cap->{
                                    MovementModifier reorient=new MovementModifier(MovementModifier.modFunction.NUM,MovementModifier.modOperation.MULT,
                                            MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,1,-17* finalI,-28,30,60);
                                    cap.putInBackOfList(reorient);
                                    ModNetwork.sendToClientsInDim(new ProjCapS2CPacket(iceShard.getId(), cap),serverLevel.dimension());
                                });
                                serverLevel.addFreshEntity(iceShard);
                                ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(iceShard.getId()),serverLevel.dimension());
                            }

                             */
                        }

                        AABB aabb=new AABB(this.mob.position(),this.mob.position());
                        aabb=aabb.inflate(aabbInflate);
                        LivingEntity entity=this.mob.level().getNearestEntity(LivingEntity.class,conds,this.mob,this.mob.position().x,this.mob.position().y,this.mob.position().z,aabb);
                        if (entity!=null){
                            boolean b=entity.hurt(this.mob.damageSources().mobAttack(this.mob), dmg);
                            currentStateTimer=1;
                            this.mob.setDeltaMovement(Vec3.ZERO);
                        }

                    }
                    else{
                        double currentDist=this.mob.position().distanceTo(target.position());
                        if (currentDist>prevDist+1f){//overshot, force tp if can
                            if (currentStateTimer>5){
                                if (isTeleport){
                                    currentStateTimer=5;
                                    this.mob.setOpacity(0f);
                                }
                                else{
                                    currentStateTimer=1;
                                }
                            }

                        }
                        else{
                            prevDist=currentDist;
                        }
                        if(isTeleport){
                            //TODO: make this cleaner at some point, this is a mess
                            if (this.mob.onGround() && this.currentStateTimer<7 && this.currentStateTimer>4){
                                this.currentStateTimer=4;
                            }
                            if(currentStateTimer>4){
                                if (currentStateTimer==11){
                                    mob.setCollision(false);
                                    this.mob.setBoundingBox(new AABB(0,0,0,0,0,0));
                                }
                                this.mob.setOpacity(Math.max(0,Math.min(prevOpacity,(float) (prevDist/originalDist))-0.08f));
                                prevOpacity=this.mob.getOpacity();
                            }
                            else if (currentStateTimer==4){
                                ServerLevel serverLevel= (ServerLevel) target.level();
                                teleportPos=targetPos=target.position();
                                for (int i=-1;i>=-2;i--){
                                    Vec3 tempDir=new Vec3(this.mob.getDeltaMovement().x,0,this.mob.getDeltaMovement().z).normalize();
                                    tempDir=tempDir.scale(i).normalize();
                                    System.out.println("OG TP POS IS " + teleportPos);
                                    Vec3 teleportPosCheck=teleportPos.add(tempDir);
                                    BlockPos blockPos= new BlockPos((int)teleportPosCheck.x,(int)teleportPosCheck.y+ 1,(int)teleportPosCheck.z);
                                    BlockState blockState=target.level().getBlockState(blockPos);
                                    if (!blockState.isAir()){
                                        break;
                                    }
                                    teleportPos=teleportPosCheck;
                                    System.out.println("NEW TP POS IS " + teleportPos);
                                }
                                Vec3 newDM=Vec3.ZERO;
                                Vec3 dir= MathFuncs.getDirVec(teleportPos,targetPos);
                                double dist=teleportPos.distanceTo(targetPos);
                                if (dist> 1.0E-5D){
                                    newDM=dir.scale(1.25f*dist);
                                }
                                newDM=newDM.add(0,0.4f,0);

                                this.mob.setDeltaMovement(newDM);
                                for (int i =0;i<1;i++){
                                    serverLevel.sendParticles(particleType, teleportPos.x,teleportPos.y+i*0.1f,teleportPos.z, 2, 0, 0, 0, 0);
                                }
                                System.out.println("PREV POS BEFORE TP IS " + this.mob.position());
                                this.mob.teleportTo(teleportPos.x,teleportPos.y,teleportPos.z);
                                System.out.println("POS after TP IS " + this.mob.position());

                                this.mob.setCollision(true);
                                this.mob.setBoundingBox(((AbstractNightProwler) mob).standardAABB);
                                AABB aabb=new AABB(this.mob.position(),this.mob.position());
                                aabb=aabb.inflate(1.4f);
                                this.mob.lookAt(target,30,30);
                                int infuse=((AbstractNightProwler)this.mob).getInfuse();
                                LivingEntity entity=this.mob.level().getNearestEntity(LivingEntity.class,conds,this.mob,this.mob.position().x,this.mob.position().y,this.mob.position().z,aabb);
                                if (entity!=null){
                                    entity.hurt(this.mob.damageSources().mobAttack(this.mob), dmg);
                                    if (infuse==-1){

                                    }
                                    else if (infuse==1){

                                    }
                                    currentStateTimer=1;
                                    this.mob.setDeltaMovement(Vec3.ZERO);
                                }
                                if (infuse==-1){

                                }
                                else if (infuse==1){
                                    aabb=new AABB(this.mob.position(),this.mob.position());
                                    aabb=aabb.inflate(3.5f);
                                    TargetingConditions conds2=TargetingConditions.forCombat().range(2.5f);
                                    //conds2.selector(CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob,List.of(AbstractNightProwler.class)));
                                    List<LivingEntity> entityList=this.mob.level().getNearbyEntities(LivingEntity.class,conds,this.mob,aabb);
                                    entityList.forEach(e->{

                                        if (e.hurt(flameLeap,2.5f)){
                                            e.setSecondsOnFire(4);
                                        }
                                    });
                                }
                                this.mob.setOpacity(1f);

                            }
                            else if (currentStateTimer<4){

                                this.mob.setBoundingBox(((AbstractNightProwler) mob).standardAABB);
                                AABB aabb=new AABB(this.mob.position(),this.mob.position());
                                aabb=aabb.inflate(aabbInflate);
                                int infuse=((AbstractNightProwler)this.mob).getInfuse();
                                LivingEntity entity=this.mob.level().getNearestEntity(LivingEntity.class,conds,this.mob,this.mob.position().x,this.mob.position().y,this.mob.position().z,aabb);
                                if (entity!=null){
                                    entity.hurt(this.mob.damageSources().mobAttack(this.mob), dmg);
                                    if (infuse==-1){

                                    }
                                    else if (infuse==1){

                                    }
                                    currentStateTimer=1;
                                    this.mob.setDeltaMovement(Vec3.ZERO);
                                }
                            }
                        }
                        else{
                            AABB aabb=new AABB(this.mob.position(),this.mob.position());
                            aabb=aabb.inflate(aabbInflate);
                            int infuse=((AbstractNightProwler)this.mob).getInfuse();
                            LivingEntity entity=this.mob.level().getNearestEntity(LivingEntity.class,conds,this.mob,this.mob.position().x,this.mob.position().y,this.mob.position().z,aabb);
                            if (entity!=null){
                                entity.hurt(this.mob.damageSources().mobAttack(this.mob), dmg);
                                if (infuse==-1){

                                }
                                else if (infuse==1){

                                }
                                this.mob.setDeltaMovement(Vec3.ZERO);
                                currentStateTimer=1;
                                this.mob.setOpacity(1f);
                            }

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
                    targetPos=target.position();
                    Vec3 targetDir=target.getLookAngle();
                    targetDir=new Vec3(targetDir.x,0,targetDir.y);
                    for (int i=-1;i>=-2;i--){
                        Vec3 tempDir=targetDir;
                        tempDir=tempDir.scale(i).normalize();
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
                            System.out.println("IS TP");
                        }
                    }
                    break;
                }
                case 1:{
                    state++;
                    currentStateTimer=abilityRecoveryTime;
                    this.mob.setActionId(4);
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
        return 4.2D;
    }

}
