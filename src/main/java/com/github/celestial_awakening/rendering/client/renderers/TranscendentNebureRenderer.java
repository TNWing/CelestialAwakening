package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.entity.living.transcendents.Nebure;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.TranscendentAstraliteModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TranscendentNebureRenderer extends MobRenderer<Nebure, TranscendentAstraliteModel<Nebure>> {
    public TranscendentNebureRenderer(EntityRendererProvider.Context context) {
        super(context, new TranscendentAstraliteModel<>(context.bakeLayer(ModelLayerInit.ASTRALITE_LAYER)),2);
    }

    @Override
    public ResourceLocation getTextureLocation(Nebure p_114482_) {
        return null;
    }

    @Override
    public void render(Nebure entity, float entityYaw, float particleTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, particleTicks,poseStack, bufferSource, packedLight);
    }
}
