package com.github.celestial_awakening.mixins;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Inject(method="getSkyColor" , at=@At("TAIL"),cancellable = true)
    public void onGetSkyColor(Vec3 p_171661_, float p_171662_, CallbackInfoReturnable<Vec3> cir){
        ClientLevel level=(ClientLevel)  (Object) this;
        LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
        capOptional.ifPresent(cap->{
            boolean b=level.getDayTime()%100==0 && !Minecraft.getInstance().isPaused();
            int sunControl=cap.divinerSunControlVal;
            Vec3 returnVal=cir.getReturnValue();
            if (sunControl<0){
                float mult=(11f+sunControl)/11f;

                cir.setReturnValue(returnVal.multiply(mult,mult,mult));
            }
        });
    }
    //(0.47058823704719543, 0.6549019813537598, 1.0) to (0.1516672521829605, 0.211070254445076, 0.32229289412498474), day to night (aka daytime to sunset)
    //affects ground darkness level
    @Inject(method="getSkyDarken" , at=@At("TAIL"),cancellable = true)
    public void getSkyDarken(float p_104806_, CallbackInfoReturnable<Float> cir) {
        ClientLevel level=(ClientLevel)  (Object) this;
        LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
        capOptional.ifPresent(cap->{
            int sunControl=cap.divinerSunControlVal;
            float returnVal=cir.getReturnValue();
            boolean b=level.getDayTime()%100==0 && !Minecraft.getInstance().isPaused();
            if (sunControl<0){
                returnVal*=(11f+sunControl)/11f;
                cir.setReturnValue(returnVal);
            }
        });
    }
}
