package com.github.celestial_awakening.mixins;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(LevelLightEngine.class)
public abstract class LevelLightEngineMixin {
    @Shadow
    private LevelHeightAccessor levelHeightAccessor;

    @Shadow @Final @Nullable private LightEngine<?, ?> skyEngine;

    @ModifyVariable(method="getRawBrightness", at=@At(value = "LOAD"),name = "i")
    private int testModVar(int i, BlockPos p_75832_, int p_75833_){
        final int[] returnVal={i};
        if (levelHeightAccessor instanceof Level level && Config.divinerAoDCosmetic){
            LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
            capOptional.ifPresent(cap->{
                int levelState = cap.divinerSunControlVal;
                if (levelState < 0) {
                    int maxLight = level.getMaxLightLevel();
                    int newLightLevel=Math.min(i, maxLight + levelState);
                    returnVal[0]=newLightLevel;
                }
            });
        }
        return returnVal[0];
    }

}
