package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.projectile.CA_Projectile;
import com.github.celestial_awakening.entity.projectile.GenericShard;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
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

    public GenericShardRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(GenericShard p_114482_) {
        GenericShard.Type type=p_114482_.getShardType();
        System.out.println(type);
        if (type!=null){
            switch (p_114482_.getShardType()){
                case ICE :{
                    return ICE_TEXTURE;
                }
                case EARTH:{
                    return null;
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
    public void render(GenericShard entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        //VertexConsumer vertexconsumer =bufferSource.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        //poseStack.scale(6f*entity.getXRScale(),6f*entity.getYRScale(),6f*entity.getZRScale());
        //poseStack.translate(0,-1.35f,0);
        //need to translate
        //this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        float alpha=entity.getOpactiy();
        //System.out.println("OUR ALPHA IS " + alpha);
        //        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(END_FACE_TEXTURE));
        //beaconBeam(END_FACE_TEXTURE,false)

        VertexConsumer vertexconsumerEnd = bufferSource.getBuffer(CA_RenderTypes.translucentFullBright(getTextureLocation(entity)));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getHAng()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getZRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getVAng()));
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        float width=entity.getWidth()/2;
        float height=entity.getHeight();

        //drawFace(poseStack,matrix4f,matrix3f,vertexconsumerEnd,-width,-height/2,-width,alpha,1);
        //drawFace(poseStack,matrix4f,matrix3f,vertexconsumerEnd,width,height/2,width,alpha,1);

        //drawFace(poseStack,matrix4f,matrix3f,vertexconsumerEnd,-width,-height/2,-width,alpha,-1);
        //drawFace(poseStack,matrix4f,matrix3f,vertexconsumerEnd,width,height/2,width,alpha,-1);
        //MultiBufferSource.BufferSource buf= (MultiBufferSource.BufferSource) bufferSource;
        //buf.endBatch(CA_RenderTypes.translucentFullBright(getTextureLocation(entity)));
        poseStack.popPose();
    }
    public void drawFace(PoseStack poseStack, Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float xOffset, float yOffset, float zOffset, float alpha, float mult){
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,yOffset,zOffset,0,0, LightTexture.FULL_BRIGHT,alpha,mult,0,0);

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
