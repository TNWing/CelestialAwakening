package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.transcendents.Astralite;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.TranscendentAstraliteModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TranscendentAstraliteRenderer extends MobRenderer<Astralite, TranscendentAstraliteModel<Astralite>> {
    float scale=1.4f;
    public TranscendentAstraliteRenderer(EntityRendererProvider.Context context) {
        super(context, new TranscendentAstraliteModel<>(context.bakeLayer(ModelLayerInit.ASTRALITE_LAYER)),2);
    }

    @Override
    public ResourceLocation getTextureLocation(Astralite entity) {
        if (entity.getActionId()==3){
            int index=entity.getActionFrame()/2;

            if (index>0){
                if (index>16){
                    index=16;
                }
                String rl=String.format("textures/entity/astralite/astralite_basic%d.png",index);
                return CelestialAwakening.createResourceLocation(rl);
            }

        }
        return CelestialAwakening.createResourceLocation("textures/entity/astralite/astralite_base.png");
    }

    @Override
    public void render(Astralite entity, float entityYaw, float particleTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        //VertexConsumer vertexconsumer =bufferSource.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        poseStack.scale(scale,scale,scale);
        super.render(entity, entityYaw, particleTicks,poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }


}
