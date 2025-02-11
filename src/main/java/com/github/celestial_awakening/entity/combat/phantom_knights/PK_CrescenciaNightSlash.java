package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class PK_CrescenciaNightSlash extends GenericAbility {
    float bigCrescentDmgVals[]={5f,5.8f,7f};
    float smallCrescentDmgVals[]={3,3.75f,4.5f};
    float ang=0;
    Vec3 dir=Vec3.ZERO;
    public PK_CrescenciaNightSlash(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.getDirection();
            this.mob.canMove=false;
            super.startAbility(target,dist);
            //this.mob.setActionId(4);
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){

            int diffMod=this.mob.level().getDifficulty().getId();
            if (diffMod>0){
                diffMod-=1;
            }
            ServerLevel serverLevel= (ServerLevel) this.mob.level();
            if (currentStateTimer==110){
                dir=MathFuncs.getDirVec(this.mob.position(),target.position());
                ang= MathFuncs.getAngFrom2DVec(dir);
                LunarCrescent crescent=new LunarCrescent(serverLevel,bigCrescentDmgVals[diffMod],270,1f,ang,0,0,8f,2f,8f);
                int id=crescent.getId();

                crescent.setPos(this.mob.position().add(dir.scale(0.2f)));
                crescent.setOwner(this.mob);
                serverLevel.addFreshEntity(crescent);
                ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(id),serverLevel.dimension());
            }
            else if (currentStateTimer%20==10){
                LunarCrescent crescent=new LunarCrescent(serverLevel,smallCrescentDmgVals[diffMod],120,6f,ang,0,0,1f,0.25f,1f);
                int id=crescent.getId();
                crescent.setPos(this.mob.position().add(dir.scale(0.2f)));
                crescent.setOwner(this.mob);
                serverLevel.addFreshEntity(crescent);
                ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(id),serverLevel.dimension());
            }
        }
        this.currentStateTimer--;
        if (currentStateTimer<=0) {
            switch (state) {
                case 0:{
                    changeToState1();

                    this.mob.canMove=true;
                    break;
                }
                case 1:{
                    this.mob.canMove=true;
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
        return 12;
    }
}
