package com.github.celestial_awakening.networking.client;

import com.github.celestial_awakening.capabilities.PlayerCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import net.minecraft.client.Minecraft;

public class ClientPlayerData {
    public static void setData(PlayerCapability newData){
        PlayerCapability cap= Minecraft.getInstance().player.getCapability(PlayerCapabilityProvider.playerCapability).orElse(null);
        if (cap!=null){
            cap.updateData(newData);
        }
    }
}
