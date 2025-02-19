package com.github.celestial_awakening.entity.combat.transcendents.astralite;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.projectile.ShiningOrb;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AstraliteBasicAttack extends GenericAbility {
    public AstraliteBasicAttack(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);

    }

    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange = Math.pow(this.getAbilityRange(target),2);
        System.out.println("ATTEMPTING TO START BASIC ACO ATTACK");
        if (abilityRange>=dist && this.getCurrentCD()==0){
            System.out.println("BASIC ACO ATTAACK");
            mob.canMove=false;
            super.startAbility(target,dist);
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);
        }

    }


    @Override
    public void executeAbility(LivingEntity target) {
        this.currentStateTimer--;
        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    changeToState1();
                    Vec3 ownerPos=this.mob.position();
                    Level level=this.mob.level();

                    Vec3 targetPos=target.position();
                    Vec3 dir=targetPos.subtract(ownerPos).normalize();
                    float ang=MathFuncs.getAngFrom2DVec(dir);
                    Vec3 spt=new Vec3(ownerPos.x,ownerPos.y,ownerPos.z).add(dir.scale(1.1f));
                    ShiningOrb orb=ShiningOrb.create(level,100,5,ang,0,3);
                    orb.setPos(spt);
                    orb.setOwner(this.mob);
                    level.addFreshEntity(orb);
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
        return 8f;
    }
}
