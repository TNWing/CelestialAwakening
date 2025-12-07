package com.github.celestial_awakening.capabilities;

import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;

public class CapabilityFromString {
    public static HashMap<String, Capability> map=new HashMap<>();
    void initMap(){
        map.put("SearedStone",SearedStoneToolCapabilityProvider.capability);
    }

    public static Capability getFromString(String s){
        return map.getOrDefault(s,null);
    }
}
