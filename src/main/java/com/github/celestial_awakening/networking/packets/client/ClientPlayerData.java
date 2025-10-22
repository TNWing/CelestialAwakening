package com.github.celestial_awakening.networking.packets.client;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class ClientPlayerData {
    public static void setData(LivingEntityCapability newData){
        @NotNull LazyOptional<LivingEntityCapability> capOptional= Minecraft.getInstance().player.getCapability(LivingEntityCapabilityProvider.capability);
        capOptional.ifPresent(cap->{
            cap.updateData(newData);
        });
    }
}
