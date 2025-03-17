package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.entity.projectile.CA_ArrowProjectile;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class CArrowRenderer  <T extends Entity> extends ArrowRenderer<CA_ArrowProjectile> {
    public CArrowRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    public static final ResourceLocation CA_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/spectral_arrow.png");
    @Override
    public ResourceLocation getTextureLocation(CA_ArrowProjectile p_114482_) {
        return CA_ARROW_LOCATION;
    }


}
