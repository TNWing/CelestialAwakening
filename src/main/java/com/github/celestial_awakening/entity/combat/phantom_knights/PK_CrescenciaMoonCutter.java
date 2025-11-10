package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.capabilities.MovementModifier;
import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class PK_CrescenciaMoonCutter extends GenericAbility {
    /*
    Summons a row of rotating crescents.
     */
    /*
    /teleport Dev -239.4 90 -232.4
    /teleport Dev -239.4 90 -233.0

    /teleport Dev -237.9 90 -232.4
    /teleport Dev -237.9 90 -233.0

        /teleport Dev -242.0 90 -232.4
    /teleport Dev -242.0 90 -233.0

            /teleport Dev -245.0 90 -232.4
    /teleport Dev -245.0 90 -233.0
     */

    float crescentDmgVals[]={7f,9.5f,12f};

    public PK_CrescenciaMoonCutter(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime,int p) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
    }
    Vec3 stepDir;
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.getDirection();
            this.mob.canMove=false;
            super.startAbility(target,dist);
            this.mob.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addTransientModifier(this.mob.kbImmune);
            if (dist<9){
                stepDir=MathFuncs.getDirVec(target.position(),this.mob.position());

            }
            else{
                stepDir=Vec3.ZERO;
            }
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==0){
            if (this.currentStateTimer==8){
                this.mob.setActionId(9);
            }
            Vec3 newPos=this.mob.position().add(stepDir.scale(0.3f));
            //BlockPos blockPos=new BlockPos(new Vec3i(newPos.x,newPos.y,newPos.z));
            if (false){

            }
        }
        this.currentStateTimer--;
        if (currentStateTimer<=0) {
            switch (state) {
                case 0:{
                    this.mob.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(this.mob.kbImmune);
                    changeToState1();

                    int diffMod=this.mob.level().getDifficulty().getId();
                    if (diffMod>0){
                        diffMod-=1;
                    }
                    //maybe perform a dash backwards?
                    Vec3 dir= MathFuncs.getDirVec(this.mob.position(),target.position());
                    float baseAng= MathFuncs.getAngFrom2DVec(dir);
                    ServerLevel serverLevel= (ServerLevel) this.mob.level();
                    float dmg=crescentDmgVals[diffMod];
                    Vec3 startPos=this.mob.position().add(dir.scale(0.2f)).add(new Vec3(0,1.25f,0));
                    float vAng=MathFuncs.getVertAngFromVec(dir);
                    vAng=22.5f * (int)(vAng/22.5f);
                    if (this.mob.distanceToSqr(target)<1.1f){
                        vAng=0;
                    }
                    int diff=target.level().getDifficulty().getId();
                    int bound=3;
                    if (diff>=2){
                        bound=5;
                    }
                    for (int i=-bound;i<=bound;i++){
                        float ang=baseAng+i*(24f-1.5f*i);
                        summonCrescent(serverLevel,ang,vAng,dmg,startPos);
                    }
                    break;
                }
                case 1:{
                    this.mob.setActionId(0);
                    changeToState2();
                    break;
                }
                case 2:{
                    liftRestrictions();
                    state=0;
                    break;
                }
            }
        }
    }
    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 8;
    }

    void summonCrescent(ServerLevel lvl,float ang,float vAng,float dmg,Vec3 startPos){

        LunarCrescent crescent=LunarCrescent.create(lvl,dmg,120,1f,ang,vAng,0,1f,0.25f,1f);
        @NotNull LazyOptional<ProjCapability> capOptional=crescent.getCapability(ProjCapabilityProvider.ProjCap);
        capOptional.ifPresent(cap->{
            MovementModifier modifier=new MovementModifier(
                    MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,
                    MovementModifier.modFunction.NUM,MovementModifier.modOperation.SET,
                    MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,
                    0f,
                    0,0,
                    100,
                    0,4);
            MovementModifier modifier2=new MovementModifier(
                    MovementModifier.modFunction.NUM, MovementModifier.modOperation.MULT,
                    MovementModifier.modFunction.NUM,MovementModifier.modOperation.SET,
                    MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,
                    4.8f,
                    0,0,
                    100,
                    0,116);
            cap.putInBackOfList(modifier);
            cap.putInBackOfList(modifier2);
        });
        int id=crescent.getId();
        crescent.setPos(startPos);
        crescent.setOwner(this.mob);
        crescent.setDisableShields(true);
        crescent.setDisableTicks(60);
        lvl.addFreshEntity(crescent);
        ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(id),lvl.dimension());
    }

    @Override
    public int calcPriority(){
        if (this.getCurrentCD()>0){
            return -1;
        }
        int p=this.getBasePriority();
        if (mob.getPerceivedTargetDistanceSquareForMeleeAttack(mob.getTarget())>36){
            System.out.println("Player is far, mooncutter is less likely");
            p+=10;
        }
        return p;
    }
}
