package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.projectile.ShiningOrb;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.ShiningOrbModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ShiningOrbRenderer <T extends Entity> extends EntityRenderer<ShiningOrb> {
    private static final ResourceLocation TEXTURE_LOCATION = CelestialAwakening.createResourceLocation("textures/entity/lightray_face2.png");
    private final ShiningOrbModel model;
    public ShiningOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model=new ShiningOrbModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayerInit.SHINING_ORB_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(ShiningOrb p_114482_) {
        return null;
    }

    public void render(ShiningOrb entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexconsumer =bufferSource.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        poseStack.scale(6f,6f,6f);
        poseStack.translate(0,-1.35f,0);
        //need to translate
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
