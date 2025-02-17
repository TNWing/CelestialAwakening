package com.github.celestial_awakening.capabilities;

import com.github.celestial_awakening.events.GenericCommandPattern;

import java.util.UUID;

public class PlayerCapHelperFuncs {
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
    public static void onRemoveFromMap(String name, UUID id){
        switch (name){
            default -> {//no special effects, usually reserved for cooldown timers
                break;
            }
        }
    }

}
