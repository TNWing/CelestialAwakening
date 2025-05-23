package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = CelestialAwakening.MODID, value = Dist.CLIENT)
public class ClientEventsManager {
    private static List<ResourceLocation> divinerEyeBase=new ArrayList<>();
    private String divienrEyeStr="textures/environment/diviner_eye";
    private static ResourceLocation transcendentAoDSprite=CelestialAwakening.createResourceLocation("textures/environment/transcendent_aod_overlay.png");

    public ClientEventsManager(){
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_closed.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_partial1.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_partial2.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_partial3.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_open0.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_open1.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_open2.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_open3.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_open4.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_open5.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_open6.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_open7.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_open8.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_intermediate1.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_intermediate2.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_intermediate3.png"));
        divinerEyeBase.add(CelestialAwakening.createResourceLocation(divienrEyeStr+"/diviner_eye_intermediate4.png"));
    }

    //TODO:
    //upon logging out and back in during a diviner event, the diviner is not rendered initially, unsure if it starts rendering only on update or just bc of delay
    @SubscribeEvent
    public void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage()== RenderLevelStageEvent.Stage.AFTER_SKY){
            Minecraft minecraft = Minecraft.getInstance();

                ClientLevel level=Minecraft.getInstance().level;
                if (level!=null && level.dimension()==Level.OVERWORLD){
                    LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
                    capOptional.ifPresent(cap->{

                        if (cap.divinerEyeToState>-2){
                            renderDivinerEye(event.getPoseStack(),level,cap,event);
                        }
                        else if (cap.divinerSunControlTimer>0){
                            renderTransAoDOverlay(event.getPoseStack(),level,cap,event);
                        }

                    });


                }
            }

    }
    public void renderTransAoDOverlay(PoseStack poseStack,ClientLevel level,LevelCapability cap,RenderLevelStageEvent event){
        float f12 = 15.0F;//f12=30 for a 32 by 32
        FogRenderer.levelFogColor();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        poseStack=poseStackModificationForSun(level,poseStack,event.getPartialTick());

        Matrix4f matrix4f1=poseStack.last().pose();//posestack
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, transcendentAoDSprite);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        float a=0.5f+0.5f*cap.divinerEyePower/100f;
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).color(1.0f,1.0f,1.0f,a).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).color(1.0f,1.0f,1.0f,a).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).color(1.0f,1.0f,1.0f,a).endVertex();
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).color(1.0f,1.0f,1.0f,a).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

//also, whenever a transition to state starts, theres a brief moment of glitched texture
    public void renderDivinerEye(PoseStack poseStack,ClientLevel level, LevelCapability cap,RenderLevelStageEvent event){
        //System.out.println("rendering on time " + level.getDayTime());
        Minecraft minecraft = Minecraft.getInstance();

        float f12 = 30.0F;
        FogRenderer.levelFogColor();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        poseStack=poseStackModificationForSun(level,poseStack,event.getPartialTick());

        Matrix4f matrix4f1=poseStack.last().pose();//posestack
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        int ind=0;
        /*

        Frames
        -1: closed
        0: open, iris in center
        1-8: iris at one of 8 edges of the eye
         */
        int fromState=cap.divinerEyeFromState;
        int toState=cap.divinerEyeToState;

        /*
        vertex defines a shape
        uv: maps 2d texture onto model
         */
        //for now, dont use intermediate images


        //System.out.println("TO STATE is " + toState);
        if (toState>0){
            ind=toState+5;
            ind=4;
            //eye is looking around
        }

        else if (toState==0){
            if (fromState==-1){
                ind=eyeLidRender(cap.divinerEyeFrameProgress,true);
            }
            else{
                //recentering
                ind=5;
                ind=4;
            }
        }
        else{
            if (fromState==-1){
                ind=0;
            }
            else{
                ind=eyeLidRender(cap.divinerEyeFrameProgress,false);
            }
        }
        /*
        if (!minecraft.isPaused() && level.getDayTime()%10==0) {
            System.out.println("rendering on time " + level.getDayTime());
            System.out.println("STATES ARE " + fromState + " TO " + toState);
            System.out.println("ind is " + ind);
        }

         */
        //System.out.println("IND IS " + ind);
        RenderSystem.setShaderTexture(0, divinerEyeBase.get(ind));
        //oob, hits 18 for some reason
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        poseStack.popPose();
    }

    public int eyeLidRender(float progress, boolean opening){
        int index=0;
        index= (int) (progress/25);
        if (!opening){
            index=4-index;//prev was 5-ind
        }
        //index goes very hiogh for some reason
        //System.out.println("IND IS " + index + " WITH OPENING BOOL IS " + opening);
        return index;
    }


    //performs the calculations on the posestack by copying vanilla code
    public PoseStack poseStackModificationForSun(ClientLevel level,PoseStack poseStack,float partialTicks){
        poseStack.pushPose();
        float f11 = 1.0F - level.getRainLevel(partialTicks);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTicks) * 360.0F));
        return poseStack;
    }
}
