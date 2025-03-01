package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.NightProwler;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.NightProwlerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NightProwlerRenderer extends MobRenderer<NightProwler, NightProwlerModel<NightProwler>> {
    public NightProwlerRenderer(EntityRendererProvider.Context context) {
        super(context,new NightProwlerModel<>(context.bakeLayer(ModelLayerInit.NIGHT_PROWLER_LAYER)),2);
    }

    //@Override
    public void render(NightProwler entity, float entityYaw, float particleTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, particleTicks,poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(NightProwler p_114482_) {
        return CelestialAwakening.createResourceLocation("textures/entity/night_prowler.png");
    }
}
