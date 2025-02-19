package com.github.celestial_awakening.events;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParticleManager {
    public static ParticleManager particleManager=createParticleManager();
    class ParticleData{
        double x;
        double y;
        double z;
        ServerLevel serverLevel;
        ParticleOptions particle;
        int amt;
        float spd;
        int reps;
        int tickDelay;
        ParticleData(double x,double y,double z,ServerLevel serverLevel,ParticleOptions options, int amt, float spd, int reps, int tickDelay){
            this.x=x;
            this.y=y;
            this.z=z;
            this.serverLevel=serverLevel;
            this.particle=options;
            this.amt=amt;
            this.spd=spd;
            this.reps=reps;
            this.tickDelay=tickDelay;
        };
    }
    
    protected void insertParticleData(ParticleData obj,int startTicks){
        particleDataMap.put(obj,startTicks);
    }

    protected ParticleData createParticleDataect(double x,double y,double z,ServerLevel serverLevel,ParticleOptions options, int amt, float spd, int reps, int tickDelay){
        return new ParticleData(x, y, z, serverLevel, options, amt, spd, reps, tickDelay);
    }
    private HashMap<ParticleData,Integer> particleDataMap=new HashMap<>();


    public static ParticleManager createParticleManager(){
        if (particleManager==null){
            ParticleManager newManager=new ParticleManager();
            ParticleManager.particleManager=newManager;

        }
        return particleManager;
    }

    private ParticleManager(){

    }
    protected void generateParticles(ServerLevel serverLevel){
        moonstoneParticles(serverLevel);
    }

    protected void moonstoneParticles(ServerLevel serverLevel){
        ParticleOptions particleType = ParticleTypes.ENCHANT; // Change this to the particle type you want
        LevelCapability cap=serverLevel.getCapability(LevelCapabilityProvider.LevelCap).orElse(null);

        if (cap!=null){
            Iterator<Map.Entry<BlockPos,Integer>> iterator = cap.currentMoonstonePos.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<BlockPos, Integer> entry = iterator.next();
                BlockPos blockPos = entry.getKey();
                int timeLeft =entry.getValue();
                System.out.println("MOONSTONE IS AT " + blockPos + " with time left " + cap.currentMoonstonePos.get(blockPos) );
                if (timeLeft%10==0){
                    Vec3 pos=blockPos.getCenter();
                    double x = pos.x;
                    double y = pos.y + 0.75D;
                    double z = pos.z;
                    int count = 15; // Number of particles
                    double speed = 0.12; // Speed of particles
                    System.out.println("RENDERING MOOSTONE AT POS " + x + " " + y + " " + z);
                    System.out.println("CENTER POS IS "  + blockPos.getCenter());
                    serverLevel.sendParticles(particleType, x, y, z, count, 0, 0, 0, speed);
                }
                cap.currentMoonstonePos.put(blockPos,cap.currentMoonstonePos.get(blockPos)-1);
                if (timeLeft<=0){
                    System.out.println("MOONSTONE being removed AT " + blockPos );
                    iterator.remove();
                }
            }

        }

    }

    protected void purgingLightParticles(ServerLevel serverLevel){
        ParticleOptions particleType = ParticleTypes.ELECTRIC_SPARK; // Change this to the particle type you want
        LevelCapability cap=serverLevel.getCapability(LevelCapabilityProvider.LevelCap).orElse(null);

        if (cap!=null){
            for (BlockPos blockPos:cap.currentMoonstonePos.keySet()) {
                if (cap.currentMoonstonePos.get(blockPos)%10==0){
                    double x = blockPos.getX();
                    double y = blockPos.getY() + 0.5D;
                    double z = blockPos.getZ();
                    int count = 15; // Number of particles
                    double speed = 0.12; // Speed of particles
                    //System.out.println("RENDERING MOOSTONE AT POS " + x + " " + y + " " + z);
                    serverLevel.sendParticles(particleType, x, y, z, count, 0, 0, 0, speed);
                }
                cap.currentMoonstonePos.put(blockPos,cap.currentMoonstonePos.get(blockPos)-1);
                if (cap.currentMoonstonePos.get(blockPos)<=0){
                    cap.currentMoonstonePos.remove(blockPos);
                }
            }

        }

    }

    //rework this
    protected void tick(){
        for (Map.Entry<ParticleData,Integer> entry:particleDataMap.entrySet()) {
               
        }
    }
}
