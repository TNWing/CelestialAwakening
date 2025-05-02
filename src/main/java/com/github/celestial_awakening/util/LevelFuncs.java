package com.github.celestial_awakening.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.github.celestial_awakening.util.MathFuncs.angBtwnVec;

public class LevelFuncs {
    public static boolean detectIfLookingAtCelestialBody(Level level, Player player,int isSun){
        int time=(int)level.dayTime();//ranges from 0-24k
        float sunAngle = level.getSunAngle(time) + isSun * (float) Math.PI / 2; //-pi/2 for the moon, pi/2 for sun

        Vec3 sun = new Vec3(Math.cos(sunAngle), Math.sin(sunAngle), 0f);
        Vec3 view = player.getViewVector(1.0f);
        if (angBtwnVec(view,sun)<7D){
            //System.out.println("LOOKING AT CELESTIAL BODY  " + isSun);
            return true;
        }
        return false;
    }

    public static void clearViewOfCelestialBody(Level level, LivingEntity entity, int isSun){//use this later for diviner
        int time=(int)level.dayTime();//ranges from 0-24k
        float cAngle = level.getSunAngle(time) + isSun * (float) Math.PI / 2; //-pi/2 for the moon, pi/2 for sun
        Vec3 vec3 = new Vec3(Math.cos(cAngle), Math.sin(cAngle), 0f);
        if (true){
            //entity.addEffect()
        }
    }
}
