package com.github.celestial_awakening.capabilities;

import com.github.celestial_awakening.events.GenericCommandPattern;

import java.util.UUID;

public class LivingEntityCapHelperFuncs {
    static GenericCommandPattern findPatternFromString(String s){
        switch (s){
            case "Fade":{
                return null;
            }
            default:{
                return null;
            }
        }
    }
    public static boolean onRemoveFromMap(String name, UUID id){
        switch (name){
            case "Knightmare_Honor_Duel":{
                return false;
            }
            default : {//no special effects, usually reserved for cooldown timers
                break;
            }
        }
        return true;
    }

}
