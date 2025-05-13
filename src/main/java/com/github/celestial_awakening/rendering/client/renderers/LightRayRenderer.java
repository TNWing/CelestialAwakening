package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.projectile.LightRay;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.LightRayModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class LightRayRenderer<T extends Entity> extends EntityRenderer<LightRay> {
    //BeaconRenderer
    private static final ResourceLocation END_FACE_TEXTURE = CelestialAwakening.createResourceLocation("textures/entity/lightray_face2.png");
    private static final ResourceLocation SIDE_FACE_TEXTURE = CelestialAwakening.createResourceLocation("textures/entity/lightray_side.png");

    private final LightRayModel model;
    public LightRayRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model=new LightRayModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayerInit.LIGHT_RAY_LAYER));
        this.shadowRadius=0f;
    }

    @Override
    public ResourceLocation getTextureLocation(LightRay p_114482_) {
        return null;
    }


    public void render(LightRay entity, float entityYaw, float partialTicks,PoseStack poseStack, MultiBufferSource bufferSource, int packedLight){
        poseStack.pushPose();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float alpha=entity.getOpactiy();
        //System.out.println("OUR ALPHA IS " + alpha);
        //        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(END_FACE_TEXTURE));
        //beaconBeam(END_FACE_TEXTURE,false)

        VertexConsumer vertexconsumerEnd = bufferSource.getBuffer(CA_RenderTypes.translucentFullBright(END_FACE_TEXTURE));
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        float width=entity.getWidth()/2;
        float height=entity.getHeight();
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getHAng()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getZRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getVAng()));//can probs replace with vang
        //TODO: modify collision detection
        //bottom face
        drawFace(poseStack,matrix4f,matrix3f,vertexconsumerEnd,width,0,width,alpha);
        //top face
        drawFace(poseStack,matrix4f,matrix3f,vertexconsumerEnd,width,height,width,alpha);
        VertexConsumer vertexconsumerSide = bufferSource.getBuffer(CA_RenderTypes.translucentFullBright(SIDE_FACE_TEXTURE));
        //front face
        drawFBFace(poseStack,matrix4f,matrix3f,vertexconsumerSide,packedLight,width,height,-width,alpha);
        //back face
        drawFBFace(poseStack,matrix4f,matrix3f,vertexconsumerSide,packedLight,-width,height,width,alpha);
        //left face
        drawLRFace(poseStack,matrix4f,matrix3f,vertexconsumerSide,packedLight,-width,height,width,alpha);
        //right face
        drawLRFace(poseStack,matrix4f,matrix3f,vertexconsumerSide,packedLight,width,height,-width,alpha);
        //RenderSystem.disableCull();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }
    public void drawFace(PoseStack poseStack,Matrix4f matrix4f,Matrix3f matrix3f,VertexConsumer vertexConsumer, float xOffset,float yOffset,float zOffset,float alpha){
        createVertex(vertexConsumer,matrix4f,matrix3f,-xOffset,yOffset,-zOffset,0,0, LightTexture.FULL_BRIGHT,alpha);
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,yOffset,-zOffset,1,0, LightTexture.FULL_BRIGHT,alpha);
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,yOffset,zOffset,1,1, LightTexture.FULL_BRIGHT,alpha);
        createVertex(vertexConsumer,matrix4f,matrix3f,-xOffset,yOffset,zOffset,0,1, LightTexture.FULL_BRIGHT,alpha);
    }

    //front back
    public void drawFBFace(PoseStack poseStack,Matrix4f matrix4f,Matrix3f matrix3f,VertexConsumer vertexConsumer, int packedLight,float xOffset,float yOffset,float zOffset,float alpha){
        createVertex(vertexConsumer,matrix4f,matrix3f,-xOffset,0,zOffset,0,0, LightTexture.FULL_BRIGHT,alpha);
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,0,zOffset,1,0, LightTexture.FULL_BRIGHT,alpha);
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,yOffset,zOffset,1,1, LightTexture.FULL_BRIGHT,alpha);
        createVertex(vertexConsumer,matrix4f,matrix3f,-xOffset,yOffset,zOffset,0,1, LightTexture.FULL_BRIGHT,alpha);
    }

    //left right
    public void drawLRFace(PoseStack poseStack,Matrix4f matrix4f,Matrix3f matrix3f,VertexConsumer vertexConsumer, int packedLight,float xOffset,float yOffset,float zOffset,float alpha){
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,0,-zOffset,0,0, LightTexture.FULL_BRIGHT,alpha);
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,0,zOffset,1,0, LightTexture.FULL_BRIGHT,alpha);
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,yOffset,zOffset,1,1, LightTexture.FULL_BRIGHT,alpha);
        createVertex(vertexConsumer,matrix4f,matrix3f,xOffset,yOffset,-zOffset,0,1, LightTexture.FULL_BRIGHT,alpha);
    }
    public void createVertex(VertexConsumer vertexConsumer,Matrix4f m4f, Matrix3f m3f,float x,float y,float z, float ux,float uy, int packedLight,float alpha){
        float ourAlpha=alpha;
        //System.out.println("Our alpoha " + ourAlpha);
        VertexConsumer v= vertexConsumer.vertex(m4f,x,y,z);
        v
                .color(1f,1f,1f,alpha)
                .uv(ux,uy)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2( LightTexture.FULL_BRIGHT)
                .normal(m3f,0,1,0)
                .endVertex();

    }


}
