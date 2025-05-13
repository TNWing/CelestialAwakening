package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.transcendents.Asteron;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.TranscendentAsteronModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TranscendentAsteronRenderer extends MobRenderer<Asteron, TranscendentAsteronModel<Asteron>> {
    private static final ResourceLocation ASTERON_BASE =CelestialAwakening.createResourceLocation("textures/entity/asteron/transcendent_asteron.png");
    private static final ResourceLocation ASTERON_PIERCING_1 = CelestialAwakening.createResourceLocation("textures/entity/asteron/transcendent_asteron_1.png");
    private static final ResourceLocation ASTERON_PIERCING_2 = CelestialAwakening.createResourceLocation("textures/entity/asteron/transcendent_asteron_2.png");
    private static final ResourceLocation ASTERON_PIERCING_3 = CelestialAwakening.createResourceLocation("textures/entity/asteron/transcendent_asteron_3.png");
    private static final ResourceLocation ASTERON_PIERCING_4 = CelestialAwakening.createResourceLocation("textures/entity/asteron/transcendent_asteron_4.png");
    private static final ResourceLocation ASTERON_PIERCING_5 = CelestialAwakening.createResourceLocation("textures/entity/asteron/transcendent_asteron_5.png");

    public TranscendentAsteronRenderer(EntityRendererProvider.Context context) {
        super(context, new TranscendentAsteronModel<>(context.bakeLayer(ModelLayerInit.ASTERON_LAYER)),2);
    }

    @Override
    public ResourceLocation getTextureLocation(Asteron mob) {
        if (mob.getActionId()==4){
            int actFrame=mob.getActionFrame();
            int quotient=actFrame/4;

            ResourceLocation valToReturn;
            switch(quotient){
                case 0:{
                    valToReturn= ASTERON_PIERCING_1;
                    break;
                }
                case 1:{
                    valToReturn= ASTERON_PIERCING_2;
                    break;
                }
                case 2:{
                    valToReturn= ASTERON_PIERCING_3;
                    break;
                }
                case 3:{
                    valToReturn=  ASTERON_PIERCING_4;
                    break;
                }
                default:{
                    valToReturn= ASTERON_PIERCING_5;
                    break;
                }
            }
            return valToReturn;

        }
        return ASTERON_BASE;
    }

    @Override
    public void render(Asteron entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(0.75f,0.75f,0.75f);

        super.render(entity, entityYaw, partialTicks,poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
