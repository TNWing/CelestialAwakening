package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.world.entity.LivingEntity;

public class PK_CrescenciaDualCrescentSlash extends GenericAbility {
    public PK_CrescenciaDualCrescentSlash(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        this.name="Dual Crescent Slash";
    }
    float cresSpd=11.5f;
    float[] crescentDmg={4.5f,6,8f};
    int diffMod;
    float a;
    @Override
    public void startAbility(LivingEntity target,double dist){
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            super.startAbility(target,dist);
            this.mob.canMove=false;
            setMoveVals(1f,this.getAbilityRange(target),false);
            diffMod=this.mob.level().getDifficulty().getId();
            if (diffMod>0){
                diffMod-=1;
            }
        }

    }
    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1 && currentStateTimer==7){

            float ang=a;
            LunarCrescent crescent=LunarCrescent.create(this.mob.level(),crescentDmg[diffMod],120,cresSpd,ang,0,90,4,1,4);
            crescent.setOwner(this.mob);
            crescent.setPos(this.mob.position().add(MathFuncs.get2DVecFromAngle(ang).scale(0.2f)));
            crescent.refreshDimensions();
            this.mob.level().addFreshEntity(crescent);
            ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(crescent.getId()),this.mob.level().dimension());
        }
        this.currentStateTimer--;
        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    changeToState1();
                    a=MathFuncs.getAngFrom2DVec(MathFuncs.getDirVec(this.mob.position(),target.position()));
                    this.mob.setYHeadRot(0);
                    this.mob.setYRot(a);
                    this.mob.setXRot(0);
                    this.mob.fixedRot=true;
                    this.mob.fixedHeadRot=true;
                    float ang=a+180;
                    LunarCrescent crescent=LunarCrescent.create(this.mob.level(),crescentDmg[diffMod],120,cresSpd,ang,0,90,4,1,4);
                    crescent.setOwner(this.mob);
                    crescent.setPos(this.mob.position().add(MathFuncs.get2DVecFromAngle(ang).scale(0.2f)));
                    crescent.refreshDimensions();

                    this.mob.level().addFreshEntity(crescent);
                    ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(crescent.getId()),this.mob.level().dimension());
                    break;
                }
                case 1:{
                    changeToState2();
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

