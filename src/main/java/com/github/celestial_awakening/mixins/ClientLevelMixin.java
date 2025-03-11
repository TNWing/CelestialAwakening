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
                if (b){
                    //System.out.println("MULT IS " + mult);
                    //System.out.println("RET VAL FOR SKY COLOR  was " + returnVal  + "  and now is  " + cir.getReturnValue());
                }
            }
            else if (b){
                //System.out.println("RET VAL FOR SKY COLOR  was " + returnVal);
            }
        });
    }
//affects sky color
    /*
    public Vec3 getSkyColor(Vec3 p_171661_, float p_171662_) {
        float f = this.getTimeOfDay(p_171662_);
        Vec3 vec3 = p_171661_.subtract(2.0D, 2.0D, 2.0D).scale(0.25D);
        BiomeManager biomemanager = this.getBiomeManager();
        Vec3 vec31 = CubicSampler.gaussianSampleVec3(vec3, (p_194161_, p_194162_, p_194163_) -> {
            return Vec3.fromRGB24(biomemanager.getNoiseBiomeAtQuart(p_194161_, p_194162_, p_194163_).value().getSkyColor());
        });
        float f1 = Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        float f2 = (float)vec31.x * f1;
        float f3 = (float)vec31.y * f1;
        float f4 = (float)vec31.z * f1;
        float f5 = this.getRainLevel(p_171662_);
        if (f5 > 0.0F) {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f7 = 1.0F - f5 * 0.75F;
            f2 = f2 * f7 + f6 * (1.0F - f7);
            f3 = f3 * f7 + f6 * (1.0F - f7);
            f4 = f4 * f7 + f6 * (1.0F - f7);
        }

        float f9 = this.getThunderLevel(p_171662_);
        if (f9 > 0.0F) {
            float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f8 = 1.0F - f9 * 0.75F;
            f2 = f2 * f8 + f10 * (1.0F - f8);
            f3 = f3 * f8 + f10 * (1.0F - f8);
            f4 = f4 * f8 + f10 * (1.0F - f8);
        }

        int i = this.getSkyFlashTime();
        if (i > 0) {
            float f11 = (float)i - p_171662_;
            if (f11 > 1.0F) {
                f11 = 1.0F;
            }

            f11 *= 0.45F;
            f2 = f2 * (1.0F - f11) + 0.8F * f11;
            f3 = f3 * (1.0F - f11) + 0.8F * f11;
            f4 = f4 * (1.0F - f11) + 1.0F * f11;
        }

        return new Vec3((double)f2, (double)f3, (double)f4);
    }

     */

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
                if (b){
                    //System.out.println("DARKEN OLD RETURN IS " + returnVal);
                }

                returnVal*=(11f+sunControl)/11f;
                if (b){
                    //System.out.println("DARKEN RETURN IS NOW " + returnVal);
                }

                cir.setReturnValue(returnVal);
            }
            else if (b){
                //System.out.println("DARKEN OLD RETURN IS " + returnVal);
            }
        });
    }
}
