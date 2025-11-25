package com.github.celestial_awakening.rendering.client.renderers.san_renderers;

import com.github.celestial_awakening.capabilities.PlayerCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.common.util.LazyOptional;

public class InsVillagerRenderer<T extends LivingEntity, M extends EntityModel<T>> extends VillagerRenderer {
    VillagerRenderer standardRenderer;
    public InsVillagerRenderer(EntityRendererProvider.Context p_174437_,VillagerRenderer og) {
        super(p_174437_);
        this.standardRenderer=og;
    }
    @Override
    public void render(Villager villager, float p_115309_, float p_115310_, PoseStack poseStack, MultiBufferSource buffer, int p_115313_) {
        LazyOptional<PlayerCapability> optional=Minecraft.getInstance().player.getCapability(PlayerCapabilityProvider.capability);
        if (optional.isPresent()){
            optional.ifPresent(cap->{
                short insPts=cap.getInsanityPts();
                if (insPts>12000){
                    super.render(villager, p_115309_,  p_115310_,  poseStack,  buffer, p_115313_);
                }
                else{

                    EntityModel newModel=null;
                    ResourceLocation newTexture=null;
                    InsManager.AppearanceData data= InsManager.INSTANCE.entityAppearanceMap.getOrDefault(villager,null);
                    if (data!=null){
                        String name=data.modelName;
                        data.delay=Math.max(0,data.delay-1);
                        if (data.delay>0){
                            if (!name.isBlank()){
                                InsManager.RendererHolder holder= InsManager.INSTANCE.rendererHolderHashMap.get(name);
                                newModel= holder.model;
                                newTexture=holder.texture;
                            }
                        }
                        else{
                            if (name.isBlank()){
                                //attempt to make a new texture change every 30 secs if it doesnt have a texture change
                                if (villager.tickCount% 600 ==0 &&  villager.getRandom().nextInt(100)<20){
                                    data.delay=villager.getRandom().nextInt(7)*120+600;
                                    data.modelName="pillager";
                                }
                            }
                            else{
                                data.delay=villager.getRandom().nextInt(5)*15+120;//no texture change for a while
                                data.modelName="";
                            }
                        }
                    }
                    else{
                        data=new InsManager.AppearanceData("",villager.getRandom().nextInt(6)*12 + 400);
                        InsManager.INSTANCE.insertIntoEntityMap(villager,data);
                    }
                    if (newModel!=null){
                        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<T, M>((LivingEntity) villager, (LivingEntityRenderer<T, M>) this, p_115310_, poseStack, buffer, p_115313_))) return;
                        poseStack.pushPose();
                        newModel.attackTime = this.getAttackAnim(villager, p_115310_);

                        boolean shouldSit = villager.isPassenger() && (villager.getVehicle() != null && villager.getVehicle().shouldRiderSit());
                        newModel.riding = shouldSit;
                        newModel.young = villager.isBaby();
                        float f = Mth.rotLerp(p_115310_, villager.yBodyRotO, villager.yBodyRot);
                        float f1 = Mth.rotLerp(p_115310_, villager.yHeadRotO, villager.yHeadRot);
                        float f2 = f1 - f;
                        if (shouldSit && villager.getVehicle() instanceof LivingEntity) {
                            LivingEntity livingentity = (LivingEntity)villager.getVehicle();
                            f = Mth.rotLerp(p_115310_, livingentity.yBodyRotO, livingentity.yBodyRot);
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

                        float f6 = Mth.lerp(p_115310_, villager.xRotO, villager.getXRot());
                        if (isEntityUpsideDown(villager)) {
                            f6 *= -1.0F;
                            f2 *= -1.0F;
                        }

                        if (villager.hasPose(Pose.SLEEPING)) {
                            Direction direction = villager.getBedOrientation();
                            if (direction != null) {
                                float f4 = villager.getEyeHeight(Pose.STANDING) - 0.1F;
                                poseStack.translate((float)(-direction.getStepX()) * f4, 0.0F, (float)(-direction.getStepZ()) * f4);
                            }
                        }

                        float f7 = this.getBob(villager, p_115310_);
                        this.setupRotations(villager, poseStack, f7, f, p_115310_);
                        poseStack.scale(-1.0F, -1.0F, 1.0F);
                        this.scale(villager, poseStack, p_115310_);
                        poseStack.translate(0.0F, -1.501F, 0.0F);
                        float f8 = 0.0F;
                        float f5 = 0.0F;
                        if (!shouldSit && villager.isAlive()) {
                            f8 = villager.walkAnimation.speed(p_115310_);
                            f5 = villager.walkAnimation.position(p_115310_);
                            if (villager.isBaby()) {
                                f5 *= 3.0F;
                            }

                            if (f8 > 1.0F) {
                                f8 = 1.0F;
                            }
                        }

                        newModel.prepareMobModel(villager, f5, f8, p_115310_);
                        newModel.setupAnim(villager, f5, f8, f7, f2, f6);
                        Minecraft minecraft = Minecraft.getInstance();
                        boolean flag = this.isBodyVisible(villager);
                        boolean flag1 = !flag && !villager.isInvisibleTo(minecraft.player);
                        boolean flag2 = minecraft.shouldEntityAppearGlowing(villager);
                        RenderType rendertype = this.getNewTexture(newTexture, villager, flag, flag1, flag2);
                        if (rendertype != null) {
                            VertexConsumer vertexconsumer = buffer.getBuffer(rendertype);
                            int i = getOverlayCoords(villager, this.getWhiteOverlayProgress(villager, p_115310_));
                            newModel.renderToBuffer(poseStack, vertexconsumer, p_115313_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
                        }

                        if (!villager.isSpectator()) {
                            for(RenderLayer<Villager, VillagerModel<Villager>> renderlayer : this.layers) {
                                renderlayer.render(poseStack, buffer, p_115313_, villager, f5, f8, p_115310_, f7, f2, f6);
                            }
                        }

                        poseStack.popPose();
                        //super.render(villager, p_115309_, p_115310_, poseStack, buffer, p_115313_);
                        var renderNameTagEvent = new net.minecraftforge.client.event.RenderNameTagEvent(villager, villager.getDisplayName(), this, poseStack, buffer, p_115313_, p_115310_);
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
                        if (renderNameTagEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameTagEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(villager))) {
                            this.renderNameTag(villager, renderNameTagEvent.getContent(), poseStack, buffer, p_115313_);
                        }
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<T, M>(villager, (LivingEntityRenderer<T, M>) this, p_115310_, poseStack, buffer, p_115313_));

                    }
                    else{
                        super.render(villager, p_115309_,  p_115310_,  poseStack,  buffer, p_115313_);
                    }
                }

            });

        }
        else{
            super.render(villager, p_115309_,  p_115310_,  poseStack,  buffer, p_115313_);
        }
    }

    protected RenderType getNewTexture(ResourceLocation resourcelocation, Villager p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        if (p_115324_) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (p_115323_) {
            return this.model.renderType(resourcelocation);
        } else {
            return p_115325_ ? RenderType.outline(resourcelocation) : null;
        }
    }
}
