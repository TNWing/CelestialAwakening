package com.github.celestial_awakening.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class MathFuncs {

    public static boolean isInRange(int i, int midVal, int offset){
        return i>=midVal-offset && i<=midVal+offset;
    }

    public static double angBtwnVec(Vec3 v1, Vec3 v2){
        double angle=v1.dot(v2);
        angle/=v1.length();
        angle/=v2.length();
        angle=Math.toDegrees(Math.acos(angle));
        return angle;
    }
    public static double clampAngle(double deg){
        double angle=(deg % 360 + 360) % 360;
        return angle;
    }
    public static float getAngFrom2DVec(Vec3 dir){
        double ang=Math.atan2(dir.x,dir.z);

        return (float) Math.toDegrees(ang);
    }
    public static float getVertAngFromVec(Vec3 dir) {
        // Calculate the horizontal distance in the xz-plane
        double horizontalDist = Math.sqrt(dir.x * dir.x + dir.z * dir.z);

        // Calculate the vertical angle using arctangent
        double ang = Math.atan2(dir.y, horizontalDist);

        return (float) Math.toDegrees(ang);
    }
    //Used in CA_Proj to manage MovementMods that multiply the value. X>0,y>=0
    public static double findBaseFromExponentAndResult(double x,double y) {
        double b = Math.exp(Math.log(y) / x);
        return b;
    }
    public static Vec3 getDirVec(Vec3 from,Vec3 to){
        Vec3 dir=to.subtract(from).normalize();
        return dir;
    }
    public static Vec3 getDirVecNoNormalize(Vec3 from,Vec3 to){
        Vec3 dir=to.subtract(from);
        return dir;
    }
    public static Vec3 get2DVecFromAngle(double angle){
        double rads=Math.toRadians(angle);
        Vec3 dir=new Vec3(Math.sin(rads),0,Math.cos(rads));
        return dir;
    }
    public static float clamp(float val, float min, float max){
        if (val>max){
            val=max;
        }
        else if (val<min){
            val=min;
        }
        return val;
    }

    public static Vec3 fromVector3f(Vector3f vec){
        return new Vec3(vec.x(), vec.y(), vec.z());
    }

    public static Vec3 expVec3(Vec3 vec,float e){
        return new Vec3(Math.pow(vec.x(),e),Math.pow(vec.y(),e), Math.pow(vec.z(),e));
    }


    public static int getRandomWithExclusion(Random rnd, int start, int end, int... exclude) {
        int random = start + rnd.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }
    public static <T extends Entity> List<T> getEntitiesIn2DDonutArea(Class<T> eClass,Vec3 center,Level level, float outerRad, float innerRad, float height,Predicate pred){
        AABB aabb=new AABB(center.x - outerRad,
                center.y-height,
                center.z-outerRad,
                center.x+outerRad,
                center.y+height,
                center.z+outerRad);
        List<T> entitiyList=level.getEntitiesOfClass(eClass,aabb,pred);
        for (Entity entity:entitiyList) {
            if (Math.sqrt(entity.distanceToSqr(center))<=innerRad){
                entitiyList.remove(entity);
            }
        }
        return entitiyList;
    }

}
