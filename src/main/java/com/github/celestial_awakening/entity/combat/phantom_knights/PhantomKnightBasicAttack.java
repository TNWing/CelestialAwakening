package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PhantomKnightBasicAttack extends GenericAbility {


    public PhantomKnightBasicAttack(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        this.name="PK Basic";
    }

    @Override
    public void startAbility(LivingEntity target, double dist) {
        double abilityRange = Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist && this.getCurrentCD()==0){
            mob.canMove=false;
            super.startAbility(target,dist);
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);
        }
        else{
            mob.canMove=false;
            super.startAbility(target,dist);
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);
            if (this.mob.getHealth()<this.mob.getMaxHealth()*0.5f){
                this.setCD(this.abilityCD/2);
            }
        }
    }

    @Override
    public void executeAbility(LivingEntity target) {
        this.currentStateTimer--;
        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    if (target.distanceTo(this.mob)<1.5f){
                        this.mob.doHurtTarget(target);
                    }

                    if (this.mob.getHealth()<this.mob.getMaxHealth()*0.5f){
                        Level lvl=this.mob.level();
                        Vec3 dir= MathFuncs.getDirVec(this.mob.position(),target.position());
                        float hAng= MathFuncs.getAngFrom2DVec(dir);
                        float vAng= MathFuncs.getVertAngFromVec(dir);
                        LunarCrescent crescent=LunarCrescent.create(lvl, (float) this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE),70,2.7f,hAng,vAng,0,1f,0.25f,1f);
                        int id=crescent.getId();
                        Vec3 startPos=this.mob.position().add(dir.scale(0.2f)).add(new Vec3(0,1.25f,0));
                        crescent.setPos(startPos);
                        crescent.setOwner(this.mob);
                        lvl.addFreshEntity(crescent);
                        ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(id),lvl.dimension());
                        this.setCD(this.abilityCD/2);
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

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 5f;
    }
}
