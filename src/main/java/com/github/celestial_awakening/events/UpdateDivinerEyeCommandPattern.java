package com.github.celestial_awakening.events;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.LevelCapS2CPacket;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Random;

public class UpdateDivinerEyeCommandPattern extends GenericCommandPattern {

    Random random=new Random();
    public UpdateDivinerEyeCommandPattern(Object[] params, int delay) {
        super(params,delay);
    }

    //need to use delay and divinetimer
    /*
    Level has this
    Codec<ResourceKey<Level>> RESOURCE_KEY_CODEC = ResourceKey.codec(Registries.DIMENSION);
    so i think i can use this for the dimID param


     */

    //As there is no pause for this, moving from/to opening/closing is instant
    @Override
    protected boolean execute() {;
        LevelCapability cap= (LevelCapability) params[0];
        ResourceKey<Level > dimID= (ResourceKey<Level>) params[1];
        cap.divinerEyeTimer-=cap.divinerEyeCurrentChangeDelay;//change delay is how much time has passed since the last change
        boolean isPause=false;

        //System.out.println("TO STATE WAS "+ cap.divinerEyeToState);
        if (cap.divinerEyeToState==0 && cap.divinerEyeFromState==-1 && cap.divinerEyeCurrentChangeDelay==90){
            //scuffed but does work.
            //System.out.println("PAUSE");
            cap.divinerEyeCurrentChangeDelay =60;
            isPause=true;
        }
        else{
            //System.out.println("NOT PAUSE");
        }
        cap.divinerEyeFromState=cap.divinerEyeToState;
            //eye will start to open
        if(!isPause){
            if (cap.divinerEyeFromState==-1 && cap.divinerEyeTimer!=0){
                //System.out.println("OPENING EYE");
                cap.divinerEyeCurrentChangeDelay =90;//4.5 sec until next change
                cap.divinerEyeToState=0;
            }
            else if (cap.divinerEyeTimer==0){
                //System.out.println("CLOSED EYE");
                cap.divinerEyeToState=-2;
                cap.divinerEyeFromState=-2;
                ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),dimID);
                return true;//stop recursion
            }
            else if (cap.divinerEyeTimer<=60){//time to clo e
                //System.out.println("STARTING TO CLOSE");
                //System.out.println("TIMER IS " +cap.divinerEyeTimer);
                cap.divinerEyeCurrentChangeDelay =cap.divinerEyeTimer;
                cap.divinerEyeToState=-1;
            }
            else if (cap.divinerEyeTimer<=100){//recenter
                //System.out.println("RECENTERING");
                cap.divinerEyeCurrentChangeDelay =cap.divinerEyeTimer-60;
                cap.divinerEyeToState=0;
            }
            else{//pick diff spot
                //System.out.println("MOVING");
                int time=random.nextInt(100,160);
                cap.divinerEyeToState= (byte) MathFuncs.getRandomWithExclusion(random,1,8,cap.divinerEyeFromState);
                if (cap.divinerEyeTimer-time<=100){
                    /*
                    System.out.println("ADJUSTING TIME FOR END PHASE");
                    System.out.println(cap.divinerEyeTimer);
                    System.out.println(time);

                     */
                    time=(cap.divinerEyeTimer-100);
                }
                cap.divinerEyeCurrentChangeDelay =time;
            }
        }


        //time value is always 0
        this.setDelay(cap.divinerEyeCurrentChangeDelay);
        //System.out.println("OUR remaining time is IS " + cap.divinerEyeTimer);
        //it changes to 0 there for some reason
        cap.divinerEyeFrameProgress=0;
        //System.out.println("DIVINER EYE IS MOVING FROM STATE " + cap.divinerEyeFromState + " TO " + cap.divinerEyeToState + " WITH a change delay of " + cap.divinerEyeCurrentChangeDelay);

        if (cap.divinerEyeTimer>0){
            ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),dimID);
            return false;
        }
        cap.divinerEyeToState=-2;
        cap.divinerEyeFromState=-2;
        ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),dimID);

        return true;//stop recursion
    }
}
