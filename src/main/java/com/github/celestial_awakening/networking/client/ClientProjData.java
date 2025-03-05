package com.github.celestial_awakening.networking.client;

import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class ClientProjData {
    public static void setData(ProjCapability newData){
        @NotNull LazyOptional<ProjCapability> cap= Minecraft.getInstance().player.level().getCapability(ProjCapabilityProvider.ProjCap);

    }
}
