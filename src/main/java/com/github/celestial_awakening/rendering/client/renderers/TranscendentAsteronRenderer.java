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
    private static final ResourceLocation ASTERON_BASE = new ResourceLocation(CelestialAwakening.MODID,"textures/entity/asteron/transcendent_asteron.png");
    private static final ResourceLocation ASTERON_PIERCING_1 = new ResourceLocation(CelestialAwakening.MODID,"textures/entity/asteron/transcendent_asteron_1.png");
    private static final ResourceLocation ASTERON_PIERCING_2 = new ResourceLocation(CelestialAwakening.MODID,"textures/entity/asteron/transcendent_asteron_2.png");
    private static final ResourceLocation ASTERON_PIERCING_3 = new ResourceLocation(CelestialAwakening.MODID,"textures/entity/asteron/transcendent_asteron_3.png");
    private static final ResourceLocation ASTERON_PIERCING_4 = new ResourceLocation(CelestialAwakening.MODID,"textures/entity/asteron/transcendent_asteron_4.png");
    private static final ResourceLocation ASTERON_PIERCING_5 = new ResourceLocation(CelestialAwakening.MODID,"textures/entity/asteron/transcendent_asteron_5.png");

    public TranscendentAsteronRenderer(EntityRendererProvider.Context context) {
        super(context, new TranscendentAsteronModel<>(context.bakeLayer(ModelLayerInit.ASTERON_LAYER)),2);
    }

    @Override
    public ResourceLocation getTextureLocation(Asteron mob) {
        if (mob.getActionId()==2){
            return ASTERON_PIERCING_1;
        }
        return ASTERON_BASE;
    }

    @Override
    public void render(Asteron entity, float entityYaw, float particleTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, particleTicks,poseStack, bufferSource, packedLight);
    }
}
