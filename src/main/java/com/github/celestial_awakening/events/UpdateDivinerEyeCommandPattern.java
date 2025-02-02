package com.github.celestial_awakening.events;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.LevelCapS2CPacket;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Random;

//i could possible attempt to store commands as a cap, but more of a last resort
public class UpdateDivinerEyeCommandPattern extends GenericCommandPattern {
    Random random=new Random();
    //lazy implementation
    public UpdateDivinerEyeCommandPattern(Object[] params, int delay) {
        super(params,delay);
    }

    //need to use delay and divinetimer
    /*
    Level has this
    Codec<ResourceKey<Level>> RESOURCE_KEY_CODEC = ResourceKey.codec(Registries.DIMENSION);
    so i think i can use this for the dimID param


     */
    @Override
    protected boolean execute() {;
        LevelCapability cap= (LevelCapability) params[0];
        ResourceKey<Level > dimID= (ResourceKey<Level>) params[1];
        cap.divinerEyeTimer-=cap.divinerEyeCurrentChangeDelay;//change delay is how much time has passed since the last change
        cap.divinerEyeFromState=cap.divinerEyeToState;
            //eye will start to open
        if (cap.divinerEyeFromState==-1 && cap.divinerEyeTimer!=0){
            System.out.println("OPENING EYE");
            cap.divinerEyeCurrentChangeDelay =90;//4.5 sec until next change
            cap.divinerEyeToState=0;
        }
        else if (cap.divinerEyeTimer==0){//never reaches
            System.out.println("CLOSED EYE");
            cap.divinerEyeToState=-2;
            cap.divinerEyeFromState=-2;
            ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),dimID);
            return true;//stop recursion
        }
        else if (cap.divinerEyeTimer<=60){//time to close
            System.out.println("STARTING TO CLOSE");
            //System.out.println("TIMER IS " +cap.divinerEyeTimer);
            cap.divinerEyeCurrentChangeDelay =cap.divinerEyeTimer;
            cap.divinerEyeToState=-1;
        }
        else if (cap.divinerEyeTimer<=100){//recenter
            System.out.println("RECENTERING");
            cap.divinerEyeCurrentChangeDelay =cap.divinerEyeTimer-60;
            cap.divinerEyeToState=0;
        }
        else{//pick diff spot
            int time=random.nextInt(100,160);
            cap.divinerEyeToState= MathFuncs.getRandomWithExclusion(random,1,8,cap.divinerEyeFromState);
            if (cap.divinerEyeTimer-time<=100){
                System.out.println(cap.divinerEyeTimer);
                System.out.println(time);
                time=(cap.divinerEyeTimer-100);
            }
            cap.divinerEyeCurrentChangeDelay =time;
        }

        //time value is always 0
        this.setDelay(cap.divinerEyeCurrentChangeDelay);
        //it changes to 0 there for some reason
        ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),dimID);
        System.out.println("DIVINER EYE IS MOVING FROM STATE " + cap.divinerEyeFromState + " TO " + cap.divinerEyeToState + " WITH a time value of " + cap.divinerEyeCurrentChangeDelay);

        if (cap.divinerEyeTimer>0){

            return false;
        }
        cap.divinerEyeToState=-2;
        cap.divinerEyeFromState=-2;
        return true;//stop recursion
    }
}
