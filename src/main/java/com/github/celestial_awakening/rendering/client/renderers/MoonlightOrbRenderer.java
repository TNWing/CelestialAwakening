package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.projectile.MoonlightOrb;
import com.github.celestial_awakening.entity.projectile.OrbiterProjectile;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.OrbiterModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class MoonlightOrbRenderer<T extends Entity> extends EntityRenderer<MoonlightOrb> {
    private static final ResourceLocation TEXTURE_LOCATION = CelestialAwakening.createResourceLocation("textures/entity/orbiter.png");
    private final OrbiterModel model;
    public MoonlightOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model=new OrbiterModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayerInit.ORBITER_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(MoonlightOrb p_114482_) {
        return null;
    }

    public void render(MoonlightOrb entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexconsumer =bufferSource.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        poseStack.scale(1.3f,1.3f,1.3f);
        poseStack.translate(0f,-1.3f,0);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
