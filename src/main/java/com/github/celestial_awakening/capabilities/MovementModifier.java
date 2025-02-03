package com.github.celestial_awakening.capabilities;

import net.minecraft.client.Minecraft;

public class MovementModifier {
/*
if i use the 2 angle+spd float in ca_proj, then mm will need to be changed as follows
for crescentwhirlwind, acc/deacc is just modifying spd var
the flip is adjusting the verticalvar
 */

    public MovementModifier(modFunction spdFunc, modOperation spdOp, modFunction angFunc, modOperation angOp, float spd, float hA, float vA, int delay, int duration){
        this.spdFunction=spdFunc;
        this.spdOperation=spdOp;
        this.spdMod=spd;
        this.angFunction=angFunc;
        this.angOperation=angOp;
        this.horiAngMod=hA;
        this.vertAngMod=vA;
        this.delayTicks=delay;
        this.remainingTicks=duration;
        this.initialTicks=duration;
        this.timeOfCreation= Minecraft.getInstance().level.getGameTime();
    }

    public MovementModifier(modFunction spdFunc, modOperation spdOp, modFunction angFunc,
                            modOperation angOp, modFunction rotFunc, modOperation rotOp,float spd, float hA, float vA, float hRot, float vRot,int delay, int duration){
        this.spdFunction=spdFunc;
        this.spdOperation=spdOp;
        this.spdMod=spd;
        this.angFunction=angFunc;
        this.angOperation=angOp;
        this.horiAngMod=hA;
        this.vertAngMod=vA;
        this.delayTicks=delay;
        this.remainingTicks=duration;
        this.initialTicks=duration;
        this.timeOfCreation= Minecraft.getInstance().level.getGameTime();
        this.horiRotMod=hRot;
        this.vertRotMod=vRot;
        this.rotFunction=rotFunc;
        this.rotOperation=rotOp;
    }

    public enum modOperation {
        ADD,
        SET,
        MULT,
    };
    public enum modFunction {
        NUM,
        TRIG,
    }


    private modFunction spdFunction;
    private modOperation spdOperation;
    private float spdMod;

    private float horiAngMod;
    private float vertAngMod;
    private modFunction angFunction;
    private modOperation angOperation;

    private float horiRotMod;
    private float vertRotMod;
    private modFunction rotFunction;
    private modOperation rotOperation;

    public modFunction getSpdFunction(){
        return this.spdFunction;
    }

    public modOperation getSpdOperation(){
        return this.spdOperation;
    }

    public float getSpd(){
        return this.spdMod;
    }

    public modFunction getAngFunction(){
        return this.angFunction;
    }

    public modOperation getAngOperation(){
        return this.angOperation;
    }

    public float getHAng(){
        return this.horiAngMod;
    }
    public float getVAng(){
        return this.vertAngMod;
    }

    public modFunction getRotFunction(){
        return this.rotFunction;
    }

    public modOperation getRotOperation(){
        return this.rotOperation;
    }

    public float getHRot(){
        return this.horiRotMod;
    }


    public float getVRot(){
        return this.vertRotMod;
    }
    
    private int delayTicks;
    private int remainingTicks;
    private int initialTicks;
    private long timeOfCreation;//used to ensure the client executes this at the same time as the server
    public long getServerTime(){
        return this.timeOfCreation;
    }
    public int getDelay(){
        return this.delayTicks;
    }
    public int getRemainingTicks(){
        return this.remainingTicks;
    }
    public int getStartingTicks(){
        return this.initialTicks;
    }
    public int getElapsedTicks(){
        return this.getStartingTicks()-this.getRemainingTicks();
    }
    public boolean decrementDelay(){//returns true if no need for delay
        if (delayTicks<=0){
            return true;
        }
        this.delayTicks--;
        return false;
    }
    public boolean decrementTicks(){//returns true if done
        this.remainingTicks--;
        if (remainingTicks<=0){
            return true;
        }
        return false;
    }
}
