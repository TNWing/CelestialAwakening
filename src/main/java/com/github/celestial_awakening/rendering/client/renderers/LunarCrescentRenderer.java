package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.LunarCrescentModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class LunarCrescentRenderer<T extends Entity> extends EntityRenderer<LunarCrescent> {
    private static final ResourceLocation TEXTURE_LOCATION = new  ResourceLocation(CelestialAwakening.MODID,"textures/entity/lunar_crescent.png");
    private final LunarCrescentModel model;
    public LunarCrescentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model=new LunarCrescentModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayerInit.LUNAR_CRESCENT_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(LunarCrescent p_114482_) {
        return null;
    }

    public void render(LunarCrescent entity, float entityYaw, float particleTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexconsumer =bufferSource.getBuffer(this.model.renderType(TEXTURE_LOCATION));

        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.getVAng()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getZRot()));
        poseStack.scale(entity.getXRScale(),entity.getYRScale(),entity.getZRScale());
        poseStack.translate(0f,-1.5f,0);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
