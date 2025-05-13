package com.github.celestial_awakening.rendering.client.renderers;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.NightProwler;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.NightProwlerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

public class NightProwlerRenderer<T extends LivingEntity, M extends HierarchicalModel<T>> extends MobRenderer<NightProwler, NightProwlerModel<NightProwler>> {
    public NightProwlerRenderer(EntityRendererProvider.Context context) {
        super(context,new NightProwlerModel<>(context.bakeLayer(ModelLayerInit.NIGHT_PROWLER_LAYER)),2);
    }

    public void render(NightProwler entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<T, M>(entity, (LivingEntityRenderer)this, partialTicks, poseStack, bufferSource, packedLight))) return;
        poseStack.pushPose();
        this.model.attackTime = this.getAttackAnim(entity, partialTicks);

        boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = entity.isBaby();
        float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity.getVehicle();
            f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        if (isEntityUpsideDown(entity)) {
            f6 *= -1.0F;
            f2 *= -1.0F;
        }

        if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            if (direction != null) {
                float f4 = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate((float)(-direction.getStepX()) * f4, 0.0F, (float)(-direction.getStepZ()) * f4);
            }
        }

        float f7 = this.getBob(entity, partialTicks);
        this.setupRotations(entity, poseStack, f7, f, partialTicks);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entity, poseStack, partialTicks);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entity.isAlive()) {
            f8 = entity.walkAnimation.speed(partialTicks);
            f5 = entity.walkAnimation.position(partialTicks);
            if (entity.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.model.prepareMobModel(entity, f5, f8, partialTicks);
        this.model.setupAnim(entity, f5, f8, f7, f2, f6);
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = this.isBodyVisible(entity);
        boolean flag1 = !flag && !entity.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
        float alpha=entity.getOpacity();
        RenderType rendertype = this.getRenderType2(entity, flag, flag1, flag2);
        this.shadowStrength=1*alpha;
        if (rendertype != null) {
            VertexConsumer vertexconsumer =  bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
            int i = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : alpha);
        }

        if (!entity.isSpectator()) {
            for(RenderLayer<NightProwler, NightProwlerModel<NightProwler>> renderlayer : this.layers) {
                renderlayer.render(poseStack, bufferSource, packedLight, entity, f5, f8, partialTicks, f7, f2, f6);
            }
        }

        poseStack.popPose();
        var renderNameTagEvent = new net.minecraftforge.client.event.RenderNameTagEvent(entity, entity.getDisplayName(), this, poseStack, bufferSource, packedLight, partialTicks);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
        if (renderNameTagEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameTagEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(entity))) {
            this.renderNameTag(entity, renderNameTagEvent.getContent(), poseStack, bufferSource ,packedLight);
        }
        LivingEntityRenderer<T,M> rendererToPass= (LivingEntityRenderer<T, M>) this;
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<T, M>(entity,rendererToPass, partialTicks, poseStack, bufferSource, packedLight));
    }

    protected RenderType getRenderType2(NightProwler p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        ResourceLocation resourcelocation = this.getTextureLocation(p_115322_);
        if (p_115324_) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (p_115323_) {
            return this.model.renderType(resourcelocation);
        } else {
            return p_115325_ ? RenderType.outline(resourcelocation) : null;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(NightProwler p_114482_) {
        return CelestialAwakening.createResourceLocation("textures/entity/night_prowler.png");
    }
}
