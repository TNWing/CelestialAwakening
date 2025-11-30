package com.github.celestial_awakening.rendering.client.renderers.san_renderers;

import com.github.celestial_awakening.capabilities.PlayerCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.common.util.LazyOptional;

public class InsPigRenderer<T extends LivingEntity, M extends EntityModel<T>> extends PigRenderer {
    PigRenderer standardRenderer;
    public InsPigRenderer(EntityRendererProvider.Context p_174437_, PigRenderer og) {
        super(p_174437_);
        this.standardRenderer=og;
    }
    @Override
    public void render(Pig pig, float p_115309_, float p_115310_, PoseStack poseStack, MultiBufferSource buffer, int p_115313_) {
        LazyOptional<PlayerCapability> optional=Minecraft.getInstance().player.getCapability(PlayerCapabilityProvider.capability);
        if (optional.isPresent()){
            optional.ifPresent(cap->{
                short insPts=cap.getInsanityPts();
                if (insPts>12000){
                    super.render(pig, p_115309_,  p_115310_,  poseStack,  buffer, p_115313_);
                }
                else{

                    EntityModel newModel=null;
                    ResourceLocation newTexture=null;
                    InsManager.AppearanceData data= InsManager.INSTANCE.entityAppearanceMap.getOrDefault(pig,null);
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
                                if (pig.tickCount% 600 ==0 &&  pig.getRandom().nextInt(100)<100){
                                    data.delay=pig.getRandom().nextInt(7)*120+1200;
                                    data.modelName="creeper";
                                }
                            }
                            else{
                                data.delay=pig.getRandom().nextInt(5)*15+120;//no texture change for a while
                                data.modelName="";
                            }
                        }
                    }
                    else{
                        data=new InsManager.AppearanceData("",pig.getRandom().nextInt(6)*12 + 100);
                        InsManager.INSTANCE.insertIntoEntityMap(pig,data);
                    }
                    if (newModel!=null){
                        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<T, M>((LivingEntity) pig, (LivingEntityRenderer<T, M>) this, p_115310_, poseStack, buffer, p_115313_))) return;
                        poseStack.pushPose();
                        newModel.attackTime = this.getAttackAnim(pig, p_115310_);

                        boolean shouldSit = pig.isPassenger() && (pig.getVehicle() != null && pig.getVehicle().shouldRiderSit());
                        newModel.riding = shouldSit;
                        newModel.young = pig.isBaby();
                        float f = Mth.rotLerp(p_115310_, pig.yBodyRotO, pig.yBodyRot);
                        float f1 = Mth.rotLerp(p_115310_, pig.yHeadRotO, pig.yHeadRot);
                        float f2 = f1 - f;
                        if (shouldSit && pig.getVehicle() instanceof LivingEntity) {
                            LivingEntity livingentity = (LivingEntity)pig.getVehicle();
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

                        float f6 = Mth.lerp(p_115310_, pig.xRotO, pig.getXRot());
                        if (isEntityUpsideDown(pig)) {
                            f6 *= -1.0F;
                            f2 *= -1.0F;
                        }

                        if (pig.hasPose(Pose.SLEEPING)) {
                            Direction direction = pig.getBedOrientation();
                            if (direction != null) {
                                float f4 = pig.getEyeHeight(Pose.STANDING) - 0.1F;
                                poseStack.translate((float)(-direction.getStepX()) * f4, 0.0F, (float)(-direction.getStepZ()) * f4);
                            }
                        }

                        float f7 = this.getBob(pig, p_115310_);
                        this.setupRotations(pig, poseStack, f7, f, p_115310_);
                        poseStack.scale(-1.0F, -1.0F, 1.0F);
                        this.scale(pig, poseStack, p_115310_);
                        poseStack.translate(0.0F, -1.501F, 0.0F);
                        float f8 = 0.0F;
                        float f5 = 0.0F;
                        if (!shouldSit && pig.isAlive()) {
                            f8 = pig.walkAnimation.speed(p_115310_);
                            f5 = pig.walkAnimation.position(p_115310_);
                            if (pig.isBaby()) {
                                f5 *= 3.0F;
                            }

                            if (f8 > 1.0F) {
                                f8 = 1.0F;
                            }
                        }

                        newModel.prepareMobModel(pig, f5, f8, p_115310_);
                        //newModel.setupAnim(pig, f5, f8, f7, f2, f6);
                        Minecraft minecraft = Minecraft.getInstance();
                        boolean flag = this.isBodyVisible(pig);
                        boolean flag1 = !flag && !pig.isInvisibleTo(minecraft.player);
                        boolean flag2 = minecraft.shouldEntityAppearGlowing(pig);
                        RenderType rendertype = this.getNewTexture(newTexture, pig, flag, flag1, flag2);
                        if (rendertype != null) {
                            VertexConsumer vertexconsumer = buffer.getBuffer(rendertype);
                            int i = getOverlayCoords(pig, this.getWhiteOverlayProgress(pig, p_115310_));
                            newModel.renderToBuffer(poseStack, vertexconsumer, p_115313_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
                        }

                        if (!pig.isSpectator()) {
                            /*
                            for(RenderLayer<Villager, VillagerModel<Villager>> renderlayer : this.layers) {
                                renderlayer.render(poseStack, buffer, p_115313_, pig, f5, f8, p_115310_, f7, f2, f6);
                            }

                             */
                        }

                        poseStack.popPose();
                        //super.render(pig, p_115309_, p_115310_, poseStack, buffer, p_115313_);
                        var renderNameTagEvent = new net.minecraftforge.client.event.RenderNameTagEvent(pig, pig.getDisplayName(), this, poseStack, buffer, p_115313_, p_115310_);
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
                        if (renderNameTagEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameTagEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(pig))) {
                            this.renderNameTag(pig, renderNameTagEvent.getContent(), poseStack, buffer, p_115313_);
                        }
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<T, M>(pig, (LivingEntityRenderer<T, M>) this, p_115310_, poseStack, buffer, p_115313_));

                    }
                    else{
                        super.render(pig, p_115309_,  p_115310_,  poseStack,  buffer, p_115313_);
                    }
                }

            });

        }
        else{
            super.render(pig, p_115309_,  p_115310_,  poseStack,  buffer, p_115313_);
        }
    }

    protected RenderType getNewTexture(ResourceLocation resourcelocation, Pig p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        if (p_115324_) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (p_115323_) {
            return this.model.renderType(resourcelocation);
        } else {
            return p_115325_ ? RenderType.outline(resourcelocation) : null;
        }
    }
    /*
    class net.minecraft.world.entity.npc.Villager cannot be cast to class net.minecraft.world.entity.monster.AbstractIllager (net.minecraft.world.entity.npc.Villager and net.minecraft.world.entity.monster.AbstractIllager are in module minecraft@1.20.1 of loader 'TRANSFORMER' @6bcc3f27)
	at net.minecraft.client.model.IllagerModel.setupAnim(IllagerModel.java:17) ~[forge-1.20.1-47.3.33_mapped_official_1.20.1-recomp.jar:?] {re:classloading,pl:runtimedistcleaner:A}
	at com.github.celestial_awakening.rendering.client.renderers.san_renderers.InsVillagerRenderer.lambda$render$0(InsVillagerRenderer.java:138) ~[main/:?] {re:classloading}
	at net.minecraftforge.common.util.LazyOptional.ifPresent(LazyOptional.java:137) ~[forge-1.20.1-47.3.33_mapped_official_1.20.1-recomp.jar:?] {re:mixin,re:computing_frames,re:classloading}
	at com.github.celestial_awakening.rendering.client.renderers.san_renderers.InsVillagerRenderer.render(InsVillagerRenderer.java:34) ~[main/:?] {re:classloading}
	at com.github.celestial_awakening.rendering.client.renderers.san_renderers.InsVillagerRenderer.render(InsVillagerRenderer.java:24) ~[main/:?] {re:classloading}
     */
}
