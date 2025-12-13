package com.github.celestial_awakening.rendering.client.renderers;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CA_RenderTypes {
    public static RenderType translucentFullBright(ResourceLocation texture) {
        return RenderType.create(
                "translucent_full_bright_" + texture.toString(), // Unique name per texture
                DefaultVertexFormat.POSITION_COLOR_TEX, // Correct vertex format
                VertexFormat.Mode.QUADS, // Render in quads
                256, // Buffer size
                false, // No culling
                true, // Affects transparency sorting
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader)) // Uses position-color-texture shader
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)) // Binds correct texture
                        .setTransparencyState( new RenderStateShard.TransparencyStateShard("translucent_transparency",
                                () -> { // Enable blend for transparency
                                    RenderSystem.enableBlend();
                                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                                },
                                () -> { // Disable blend after rendering
                                    RenderSystem.disableBlend();
                                }))
                        .setCullState(new RenderStateShard.CullStateShard(false)) // Disable back-face culling
                        .setLightmapState(new RenderStateShard.LightmapStateShard(false)) // Disable lightmap for full brightness
                        .createCompositeState(true) // Finalize RenderType
        );
    }
/*
old
    public static RenderType translucentFullBright(ResourceLocation texture) {
        return RenderType.create(
                "translucent_full_bright_" + texture.toString(), // Unique name per texture
                DefaultVertexFormat.POSITION_COLOR_TEX, // Correct vertex format
                VertexFormat.Mode.QUADS, // Render in quads
                256, // Buffer size
                false, // No culling
                true, // Affects transparency sorting
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader)) // Uses position-color-texture shader
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)) // Binds correct texture
                        .setTransparencyState(new RenderStateShard.TransparencyStateShard("translucent_transparency",
                                () -> { // Enable blend for transparency
                                    RenderSystem.enableBlend();
                                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                                },
                                () -> { // Disable blend after rendering
                                    RenderSystem.disableBlend();
                                }))
                        .setCullState(new RenderStateShard.CullStateShard(false)) // Disable back-face culling
                        .setLightmapState(new RenderStateShard.LightmapStateShard(false)) // Disable lightmap for full brightness
                        .createCompositeState(true) // Finalize RenderType
        );
    }

 */
    //Stuff stolen from protected/private render variables
protected static final RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
}, () -> {
    RenderSystem.disableBlend();
    RenderSystem.defaultBlendFunc();
});
}
