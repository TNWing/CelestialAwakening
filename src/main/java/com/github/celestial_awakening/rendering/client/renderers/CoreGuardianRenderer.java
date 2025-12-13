package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.phantom_knights.PhantomKnight_Crescencia;
import com.github.celestial_awakening.entity.living.planetary_guardians.CoreGuardian;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.PKCrescenciaModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CoreGuardianRenderer extends MobRenderer<CoreGuardian, PKCrescenciaModel<CoreGuardian>> {
    public CoreGuardianRenderer(EntityRendererProvider.Context context) {
        super(context,new PKCrescenciaModel<>(context.bakeLayer(ModelLayerInit.PK_CRESCENCIA_LAYER)),2);
    }


    //@Override
    public void render(CoreGuardian entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTicks,poseStack, bufferSource, packedLight);
        this.model.attackTime = this.getAttackAnim(entity, partialTicks);
    }
    @Override
    public ResourceLocation getTextureLocation(CoreGuardian entity) {
        if (entity.getActionId()==0){
        }
        return CelestialAwakening.createResourceLocation("textures/entity/pk_cres.png");
    }
}


