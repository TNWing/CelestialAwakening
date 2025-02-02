package com.github.celestial_awakening.networking.client;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import net.minecraft.client.Minecraft;

public class ClientLevelData {
    public static void setData(LevelCapability newData){
        LevelCapability cap=Minecraft.getInstance().player.level().getCapability(LevelCapabilityProvider.LevelCap).orElse(null);
        if (cap!=null){
            //new data not updated?
            System.out.println("NEW DATA " + newData.divinerEyeFromState + " TO " + newData.divinerEyeToState);
            cap.updateData(newData);
        }
    }
}
