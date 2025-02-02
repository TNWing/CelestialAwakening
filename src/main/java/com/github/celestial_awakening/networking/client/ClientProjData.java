package com.github.celestial_awakening.networking.client;

import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import net.minecraft.client.Minecraft;

public class ClientProjData {
    public static void setData(ProjCapability newData){
        ProjCapability cap= Minecraft.getInstance().player.level().getCapability(ProjCapabilityProvider.ProjCap).orElse(null);

    }
}
