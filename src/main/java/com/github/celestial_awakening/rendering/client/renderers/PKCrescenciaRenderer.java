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

    @Override
    public ResourceLocation getTextureLocation(PhantomKnight_Crescencia p_114482_) {
        return new ResourceLocation(CelestialAwakening.MODID,"textures/entity/pk_cres.png");
    }
}


