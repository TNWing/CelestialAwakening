package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.transcendents.Nebure;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.TranscendentNebureModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TranscendentNebureRenderer extends MobRenderer<Nebure, TranscendentNebureModel<Nebure>> {
    float scale=3.1f;
    public TranscendentNebureRenderer(EntityRendererProvider.Context context) {
        super(context, new TranscendentNebureModel<>(context.bakeLayer(ModelLayerInit.ASTRALITE_LAYER)),2);
    }

    @Override
    public ResourceLocation getTextureLocation(Nebure p_114482_) {
        return CelestialAwakening.createResourceLocation("textures/entity/astralite/astralite_base.png");
    }

    @Override
    public void render(Nebure entity, float entityYaw, float particleTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        //VertexConsumer vertexconsumer =bufferSource.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        poseStack.scale(scale,scale,scale);
        super.render(entity, entityYaw, particleTicks,poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
