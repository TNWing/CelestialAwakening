package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.entity.projectile.LightRay;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NebureScorchingRays extends GenericAbility {
    float aChange;
    float angStartOffset;
    float angDiff;
    int dir;
    LightRay[] rays;
    public NebureScorchingRays(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        if (mob.level().getRandom().nextInt(2)==0){
            dir=-1;
        }
        else{
            dir=1;
        }
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.canMove=false;
            super.startAbility(target,dist);
            int diff=target.level().getDifficulty().getId();
            dir=-dir;
            int v=(diff+1)*4;
            rays=new LightRay[v];
            angDiff=360f/(v);
            /*
            E:8
            M:12
            H:16
             */
            aChange=0;
            angStartOffset=target.level().random.nextInt(360);
            for (int i=0;i<v;i++){
                double ang=Math.toRadians(angStartOffset+angDiff*i);
                rays[i]=LightRay.create(this.mob.level(),90, (float) (this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue()*1.75f));
                rays[i].setPred(o -> !(o instanceof AbstractTranscendent));
                rays[i].setOwner(this.mob);
                rays[i].setCollision(false);
                rays[i].setOpacity(0.25f);
                rays[i].initDims(0.25f,0,0.25f,0,0.4f,8f,0,2f);
                rays[i].setHAng(-(float) Math.toDegrees(ang)+90);
                rays[i].setPred(CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob, List.of(AbstractTranscendent.class)));
                rays[i].setVAng(90);
                rays[i].setPos(new Vec3(Math.cos(ang),0,Math.sin(ang)).add(this.mob.position()).add(0,1.85f,0));
                this.mob.level().addFreshEntity(rays[i]);
            }
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);
        }
    }
    @Override
    public void executeAbility(LivingEntity target) {
        if (state==0){
            if (this.currentStateTimer%1==0){
                float yOffset=currentStateTimer*0.02f;
                for (int i=0;i<rays.length;i++){
                    double ang= Math.toRadians(i*angDiff+dir*angStartOffset);
                    rays[i].setHAng(-(float) Math.toDegrees(ang)+90);
                    rays[i].setPos(new Vec3(Math.cos(ang),0,Math.sin(ang)).add(this.mob.position()).add(0,1.25f+yOffset,0));
                }
                aChange+=3;
                angStartOffset+=Math.cos(Math.toRadians(aChange))*15;
            }
        }
        else if (state==1){

        }
        currentStateTimer--;

        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    for (int i=0;i<rays.length;i++){
                        double ang= Math.toRadians(i*angDiff+dir*angStartOffset);
                        rays[i].setHAng(-(float) Math.toDegrees(ang)+90);
                        rays[i].setPos(new Vec3(Math.cos(ang),0,Math.sin(ang)).add(this.mob.position()).add(0,1.25f,0));
                        rays[i].setCollision(true);
                        rays[i].setOpacity(1f);
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
        return 7;
    }
}
