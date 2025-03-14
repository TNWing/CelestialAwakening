package com.github.celestial_awakening.mixins;


import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelMixin {
    int counter=0;
    @Shadow
    private int skyDarken;
    @Inject(method="updateSkyBrightness" , at=@At("TAIL"))
    public void updateSkyBrightness(CallbackInfo ci) {//only called once on client level
        Level level=(Level) (Object) this;
        LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
        boolean b=level.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD;
        capOptional.ifPresent(cap->{
            skyDarken=Math.max(skyDarken,-cap.divinerSunControlVal);
            //ModNetwork.sendToClientsInDim(new LevelSkyDarknessS2CPacket(skyDarken),level.dimension());//may need to readd

        });

    }

}
