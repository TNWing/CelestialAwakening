package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.phantom_knights.PhantomKnight_Crescencia;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.PKCrescenciaModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PKCrescenciaRenderer extends MobRenderer<PhantomKnight_Crescencia, PKCrescenciaModel<PhantomKnight_Crescencia>> {
    public PKCrescenciaRenderer(EntityRendererProvider.Context p_174304_, PKCrescenciaModel<PhantomKnight_Crescencia> p_174305_, float p_174306_) {
        super(p_174304_, p_174305_, p_174306_);
    }
    public PKCrescenciaRenderer(EntityRendererProvider.Context context) {
        super(context,new PKCrescenciaModel<>(context.bakeLayer(ModelLayerInit.PK_CRESCENCIA_LAYER)),2);
    }

    //@Override
    public void render(PhantomKnight_Crescencia entity, float entityYaw, float particleTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, particleTicks,poseStack, bufferSource, packedLight);
    }
/*
@Override
public void render(YourEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
    if (entity.isAnimationC()) {
        int frameX = entity.currentFrame * FRAME_WIDTH;
        float uMin = (float) frameX / (float) (FRAME_WIDTH * FRAME_COUNT);
        float uMax = (float) (frameX + FRAME_WIDTH) / (float) (FRAME_WIDTH * FRAME_COUNT);

        // Bind the sprite sheet texture
        Minecraft.getInstance().getTextureManager().bindTexture(ANIMATED_TEXTURE);

        // Render your model part with the updated UV coordinates
        // This example assumes a simple quad part
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getEntityCutout(ANIMATED_TEXTURE));
        matrixStack.push();

        // Replace this with your model part's vertices and apply the UV coordinates
        vertexBuilder.pos(matrixStack.getLast().getMatrix(), -0.5F, 0.0F, -0.5F).tex(uMin, 1.0F).endVertex();
        vertexBuilder.pos(matrixStack.getLast().getMatrix(), 0.5F, 0.0F, -0.5F).tex(uMax, 1.0F).endVertex();
        vertexBuilder.pos(matrixStack.getLast().getMatrix(), 0.5F, 1.0F, -0.5F).tex(uMax, 0.0F).endVertex();
        vertexBuilder.pos(matrixStack.getLast().getMatrix(), -0.5F, 1.0F, -0.5F).tex(uMin, 0.0F).endVertex();

        matrixStack.pop();
    } else {
        // Bind and render the static texture
        Minecraft.getInstance().getTextureManager().bindTexture(STATIC_TEXTURE);
        // Render your model part normally
    }

    // Render the entity
    super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
}

 */
    @Override
    public ResourceLocation getTextureLocation(PhantomKnight_Crescencia entity) {
        if (entity.getActionId()==0){
        }
        return CelestialAwakening.createResourceLocation("textures/entity/pk_cres.png");
    }
}


