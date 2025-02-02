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
    public TranscendentAsteronRenderer(EntityRendererProvider.Context context) {
        super(context, new TranscendentAsteronModel<>(context.bakeLayer(ModelLayerInit.ASTERON_LAYER)),2);
    }

    @Override
    public ResourceLocation getTextureLocation(Asteron p_114482_) {
        return new ResourceLocation(CelestialAwakening.MODID,"textures/entity/transcendent_asteron.png");
    }

    @Override
    public void render(Asteron entity, float entityYaw, float particleTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, particleTicks,poseStack, bufferSource, packedLight);
    }
}
