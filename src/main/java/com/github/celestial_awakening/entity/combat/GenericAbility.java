package com.github.celestial_awakening.entity.combat;

import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.LivingEntity;

public abstract class GenericAbility {
    protected final int abilityCastTime;
    protected final int abilityExecuteTime;//time spent using an ability
    protected final int abilityRecoveryTime;//time to recover after using ability
    protected final int abilityCD;

    protected int currentCD;
    protected String name;
    protected final AbstractCALivingEntity mob;

    protected int state;//0 for casting, 1 for executing, 2 for recovery
    protected int currentStateTimer;//used for the 3 state timers


    //initiates ability
    public void startAbility(LivingEntity target,double dist){
        currentCD=abilityCD;
        currentStateTimer=abilityCastTime;
        this.mob.isActing=true;
        System.out.println("STARTING ABILITY " + this.getAbilityName());

    }
    public void setMoveVals(double min,double max,boolean keepDist){
        this.mob.minRange=min;
        this.mob.maxRange=max;
        this.mob.keepDist=keepDist;
    }

    public abstract void executeAbility(LivingEntity target);

    public GenericAbility(AbstractCALivingEntity mob, int castTime, int CD,int executeTime,int recoveryTime){
        this.mob=mob;
        this.abilityExecuteTime=executeTime;
        this.abilityCastTime=castTime;
        this.abilityCD=CD;
        this.currentCD=0;
        this.abilityRecoveryTime=recoveryTime;
    }

    public void decreaseCD(int i){
        currentCD=Math.max(currentCD-i,0);
    }

    public void decreaseStateTimer(int i){
        currentStateTimer=Math.max(currentStateTimer-i,0);
    }

    public void setCD(int i){
        currentCD=i;
    }
    public String getAbilityName(){
        return this.name;
    }
    protected void liftRestrictions(){
        mob.canMove=true;
        this.mob.fixedRot=false;
        this.mob.fixedHeadRot=false;
        mob.isActing=false;
        this.mob.setActionId(0);
        this.mob.spdMod=1f;
        System.out.println("RESETTING ACT ID");
    }

    protected void changeToState1(){
        state++;
        currentStateTimer=abilityExecuteTime;
    }

    protected void changeToState2(){
        state++;
        currentStateTimer=abilityRecoveryTime;
    }

    public int getCurrentCD(){
        return this.currentCD;
    }
    public int getCurrentStateTimer(){
        return this.currentStateTimer;
    }

    protected abstract double getAbilityRange(LivingEntity target);
}
