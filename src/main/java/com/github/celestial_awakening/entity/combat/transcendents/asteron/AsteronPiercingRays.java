package com.github.celestial_awakening.entity.combat.transcendents.asteron;

import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.projectile.LightRay;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AsteronPiercingRays extends GenericAbility {
    public AsteronPiercingRays(AbstractCALivingEntity mob, int castTime, int CD, int rec, int et) {
        super(mob,castTime, CD,et,rec);
    }

    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.getDirection();
            this.mob.canMove=false;
            super.startAbility(target,dist);

            setMoveVals(0,this.getAbilityRange(target),false);
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
                    Vec3 ownerPos=this.mob.position();
                    Level level=this.mob.level();
                    Vec3 targetPos=target.position();
                    Vec3 dir=targetPos.subtract(ownerPos).normalize();
                    dir=new Vec3(dir.x,0,dir.z);
                    float yaw= (float) -(Math.toDegrees (Math.atan2(dir.z,dir.x)) +90f);

                    for (int i=1;i<4;i++){
                        Vec3 spt=new Vec3(ownerPos.x,ownerPos.y+2.5f,ownerPos.z);

                        spt=spt.add(dir.scale(i));
                        LightRay ray=LightRay.create(level,30,3.5f);
                        ray.setOwner(this.mob);
                        ray.initDims(0.2f,0,0.2f,0,0.2f,3.4f,0,1.8f);
                        ray.setHAng(yaw);
                        ray.setVAng(200);//test w/ 70, prod should be 200
                        ray.setPos(spt);
                        level.addFreshEntity(ray);
                        
                    }
                    break;
                }
                case 1:{
                    state++;
                    currentStateTimer=abilityRecoveryTime;
                    this.mob.setActionId(5);
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
        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth());
    }
}
