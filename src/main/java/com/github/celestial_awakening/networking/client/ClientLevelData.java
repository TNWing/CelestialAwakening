package com.github.celestial_awakening.networking.client;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class ClientLevelData {
    public static void setData(LevelCapability newData){
        @NotNull LazyOptional<LevelCapability> capOptional=Minecraft.getInstance().player.level().getCapability(LevelCapabilityProvider.LevelCap);
        capOptional.ifPresent(cap->{
            //new data not updated?
            System.out.println("NEW DATA " + newData.divinerEyeFromState + " TO " + newData.divinerEyeToState);
            cap.updateData(newData);
        });
    }
}
