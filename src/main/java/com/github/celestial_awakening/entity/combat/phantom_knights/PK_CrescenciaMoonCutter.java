package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.capabilities.MovementModifier;
import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

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
    float crescentDmgVals[]={4f,5.5f,7f};
    public PK_CrescenciaMoonCutter(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
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
        this.currentStateTimer--;
        if (currentStateTimer<=0) {
            switch (state) {
                case 0:{
                    changeToState1();

                    int diffMod=this.mob.level().getDifficulty().getId();
                    if (diffMod>0){
                        diffMod-=1;
                    }
                    Vec3 dir= MathFuncs.getDirVec(this.mob.position(),target.position());
                    float baseAng= MathFuncs.getAngFrom2DVec(dir);
                    ServerLevel serverLevel= (ServerLevel) this.mob.level();
                    float dmg=crescentDmgVals[diffMod];
                    Vec3 startPos=this.mob.position().add(dir.scale(0.2f)).add(new Vec3(0,1.25f,0));
                    float vAng=MathFuncs.getVertAngFromVec(dir);
                    for (int i=-2;i<=2;i++){
                        float ang=baseAng+i*12;

                        summonCrescent(serverLevel,ang,vAng,dmg,startPos);
                    }
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
        return 12;
    }

    void summonCrescent(ServerLevel lvl,float ang,float vAng,float dmg,Vec3 startPos){

        LunarCrescent crescent=new LunarCrescent(lvl,dmg,120,4.1f,ang,vAng,0,1f,0.25f,1f);
        ProjCapability cap=crescent.getCapability(ProjCapabilityProvider.ProjCap).orElse(null);
        if (cap!=null){
            MovementModifier modifier=new MovementModifier(
                    MovementModifier.modFunction.NUM, MovementModifier.modOperation.MULT,
                    MovementModifier.modFunction.NUM,MovementModifier.modOperation.SET,
                    MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,
                    1,
                    0,0,
                    100,
                    0,120);
            cap.putInBackOfList(modifier);
        }
        int id=crescent.getId();

        crescent.setPos(startPos);
        crescent.setOwner(this.mob);
        lvl.addFreshEntity(crescent);
        ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(id),lvl.dimension());
    }
}
