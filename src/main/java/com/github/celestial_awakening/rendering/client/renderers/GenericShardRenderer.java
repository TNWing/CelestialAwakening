package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.projectile.CA_Projectile;
import com.github.celestial_awakening.entity.projectile.GenericShard;
import com.github.celestial_awakening.util.MathFuncs;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class GenericShardRenderer  <T extends Entity> extends EntityRenderer<GenericShard> {
    private static final ResourceLocation ICE_TEXTURE = CelestialAwakening.createResourceLocation("textures/entity/ice_shard.png");
    private static final ResourceLocation EARTH_TEXTURE = CelestialAwakening.createResourceLocation("textures/entity/earth_shard.png");
    public GenericShardRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(GenericShard p_114482_) {
        GenericShard.Type type=p_114482_.getShardType();
        if (type!=null){
            switch (p_114482_.getShardType()){
                case ICE :{
                    return ICE_TEXTURE;
                }
                case EARTH:{
                    return EARTH_TEXTURE;
                }
                case MOON:{
                    return null;
                }
                default:{
                    return ICE_TEXTURE;
                }
            }
        }
        return ICE_TEXTURE;

    }

    public void r(GenericShard entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight){

        poseStack.pushPose();
        float alpha=entity.getOpactiy();
        VertexConsumer vc = bufferSource.getBuffer(
                RenderType.entityCutoutNoCull(getTextureLocation(entity))
        );
        float vA= MathFuncs.getVertAngFromVec(entity.getDeltaMovement());
        float hA=MathFuncs.getAngFrom2DVec(entity.getDeltaMovement());
        poseStack.mulPose(Axis.YP.rotationDegrees(hA));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getZRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(-vA));
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        float width=entity.getWidth()/2;
        float height=entity.getHeight();
        drawFace(poseStack,matrix4f,matrix3f,vc,width,height/2,width,alpha,1);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90));
        poseStack.translate(width,-height/2,0);
        drawFace(poseStack,matrix4f,matrix3f,vc,width,height/2,width,alpha,1);
        poseStack.popPose();


    }
    public void render(GenericShard entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        r(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);

    }
    public void drawFace(PoseStack poseStack, Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float xOffset, float yOffset, float zOffset, float alpha, float mult){
        createVertex(vertexConsumer,matrix4f,matrix3f,-xOffset,yOffset,-zOffset,0,0, LightTexture.FULL_BRIGHT,alpha,0,mult,0);
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,yOffset,-zOffset,1,0, LightTexture.FULL_BRIGHT,alpha,0,mult,0);
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,yOffset,zOffset,1,1, LightTexture.FULL_BRIGHT,alpha,0,mult,0);
        createVertex(vertexConsumer,matrix4f,matrix3f,-xOffset,yOffset,zOffset,0,1, LightTexture.FULL_BRIGHT,alpha,0,mult,0);
    }
    public void createVertex(VertexConsumer vertexConsumer,Matrix4f m4f, Matrix3f m3f,float x,float y,float z, float ux,float uy, int packedLight,float alpha,float n1,float n2,float n3){
        float ourAlpha=alpha;
        VertexConsumer v= vertexConsumer.vertex(m4f,x,y,z);
        v
                .color(1f,1f,1f,alpha)
                .uv(ux,uy)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2( LightTexture.FULL_BRIGHT)
                .normal(m3f,n1,n2,n3)
                .endVertex();

    }
}
