package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

/*
Fires off 2 crescents, each aimed to the side of the target One crescent has a special glow
After a short delay, the following happens
The glowing crescent is destroyed and the knight tps to it, before immediately dashing towards the target
The other crescent redirects towards the target.
The knight will always reach the target first, and will attempt a single, powerful strike.

 */
public class PK_CrescenciaStrikethrough extends GenericAbility {
    public PK_CrescenciaStrikethrough(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        this.name="Strikethrough";
    }
    DamageSourceIgnoreIFrames dashSource=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),this.mob);
    LunarCrescent leftCrescent;
    LunarCrescent rightCrescent;
    boolean leftTP;
    float pkDir;
    float[] crescentDmgVals={3,4,5};
    float[] dashDmgVals={4,5,6};
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.canMove=false;
            super.startAbility(target,dist);
            this.mob.setActionId(4);
            //AttributeModifier attributeModifier=new AttributeModifier(UUID.fromString("KB_Res"),"CA_KB_Res",0.75f,AttributeModifier.Operation.MULTIPLY_BASE);
            //this.mob.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addTransientModifier();
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            if (this.currentStateTimer==40){
                int diffMod=this.mob.level().getDifficulty().getId();
                if (diffMod>0){
                    diffMod-=1;
                }
                ServerLevel serverLevel= (ServerLevel) this.mob.level();
                Random rand=new Random();
                int r=rand.nextInt(2);
                Vec3 vec=MathFuncs.getDirVec(this.mob.position(),target.position());
                float dir= MathFuncs.getAngFrom2DVec(vec);
                float vAng=MathFuncs.getVertAngFromVec(vec);
                float diffAng=50f;
                float leftDir=dir-diffAng;
                float rightDir=dir+diffAng;
                leftCrescent=new LunarCrescent(serverLevel,crescentDmgVals[diffMod],180,5.5f,leftDir,vAng,0,2f,0.5f,1f,2f);
                rightCrescent=new LunarCrescent(serverLevel,crescentDmgVals[diffMod],1880,5.5f,rightDir,vAng,0,2f,0.5f,1f,2f);
                leftCrescent.setPos(this.mob.position().add(MathFuncs.get2DVecFromAngle(leftDir).scale(0.2f)));
                leftCrescent.setOwner(this.mob);
                rightCrescent.setPos(this.mob.position().add(MathFuncs.get2DVecFromAngle(rightDir).scale(0.2f)));
                rightCrescent.setOwner(this.mob);
                if (r==0){
                    leftTP=true;
                }
                else{
                    leftTP=false;
                }
                serverLevel.addFreshEntity(leftCrescent);
                serverLevel.addFreshEntity(rightCrescent);
                ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(leftCrescent.getId()),serverLevel.dimension());
                ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(rightCrescent.getId()),serverLevel.dimension());
            }
            else if (this.currentStateTimer==20){
                if (leftTP){
                    Vec3 vec=MathFuncs.getDirVec(leftCrescent.position(),target.position());
                    pkDir=MathFuncs.getAngFrom2DVec(MathFuncs.getDirVec(leftCrescent.position(),target.position()));
                    float rightDir=MathFuncs.getAngFrom2DVec(MathFuncs.getDirVec(rightCrescent.position(),target.position()));
                    float vAng=MathFuncs.getVertAngFromVec(vec);
                    rightCrescent.setHAng(rightDir);
                    rightCrescent.setVAng(vAng);
                    rightCrescent.setSpd(8.5f);
                    this.mob.setPos(leftCrescent.position());
                    leftCrescent.discard();
                }
                else{
                    Vec3 vec=MathFuncs.getDirVec(leftCrescent.position(),target.position());
                    pkDir=MathFuncs.getAngFrom2DVec(MathFuncs.getDirVec(rightCrescent.position(),target.position()));
                    float leftDir=MathFuncs.getAngFrom2DVec(vec);
                    float vAng=MathFuncs.getVertAngFromVec(vec);
                    leftCrescent.setVAng(vAng);
                    leftCrescent.setHAng(leftDir);
                    leftCrescent.setSpd(9.5f);
                    this.mob.setPos(rightCrescent.position());
                    rightCrescent.discard();
                }
            }
            else if (this.currentStateTimer==15){
                Vec3 dir=MathFuncs.getDirVec(this.mob.position(),target.position());
                this.mob.setPos(target.position().subtract(dir.scale(0.4)));
                List<LivingEntity> entityList=this.mob.findCollidedEntities(CA_Predicates.opposingTeams_IgnoreSameClass_Predicate(this.mob),1.4f);
                if (!entityList.isEmpty()){
                    int diffMod=this.mob.level().getDifficulty().getId();
                    if (diffMod>0){
                        diffMod-=1;
                    }
                    for (LivingEntity entity:entityList) {
                        entity.hurt(dashSource,dashDmgVals[diffMod]);
                    }
                }

            }

        }
        this.currentStateTimer--;
        if (currentStateTimer<=0) {
            switch (state) {
                case 0:{
                    this.mob.setActionId(5);
                    this.mob.canMove=true;
                    changeToState1();
                    break;
                }
                case 1:{
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
        return 7;
    }
}
