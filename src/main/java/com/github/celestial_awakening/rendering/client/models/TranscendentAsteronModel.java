package com.github.celestial_awakening.rendering.client.models;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.github.celestial_awakening.rendering.client.animations.TranscendentAsteronAnimations;
import com.github.celestial_awakening.entity.living.transcendents.Asteron;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class TranscendentAsteronModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart model;
	private final ModelPart mainbody;
	private final ModelPart leg2_y;
	private final ModelPart leg2_z;
	private final ModelPart seg2mid;
	private final ModelPart seg2top;
	private final ModelPart seg2bot;
	private final ModelPart leg4_y;
	private final ModelPart leg4_z;
	private final ModelPart seg4mid;
	private final ModelPart seg4top;
	private final ModelPart seg4bot;
	private final ModelPart leg1_y;
	private final ModelPart leg1_z;
	private final ModelPart seg1mid;
	private final ModelPart seg1top;
	private final ModelPart seg1bot;
	private final ModelPart leg3_y;
	private final ModelPart leg3_z;
	private final ModelPart seg3mid;
	private final ModelPart seg3top;
	private final ModelPart seg3bot;


	public TranscendentAsteronModel(ModelPart mp_root) {
		this.model = mp_root.getChild("root");
		this.mainbody = this.model.getChild("mainbody");
		this.leg2_y = this.model.getChild("leg2_y");
		this.leg2_z = this.leg2_y.getChild("leg2_z");
		this.seg2mid = this.leg2_z.getChild("seg2mid");
		this.seg2top = this.leg2_z.getChild("seg2top");
		this.seg2bot = this.leg2_z.getChild("seg2bot");
		this.leg4_y = this.model.getChild("leg4_y");
		this.leg4_z = this.leg4_y.getChild("leg4_z");
		this.seg4mid = this.leg4_z.getChild("seg4mid");
		this.seg4top = this.leg4_z.getChild("seg4top");
		this.seg4bot = this.leg4_z.getChild("seg4bot");
		this.leg1_y = this.model.getChild("leg1_y");
		this.leg1_z = this.leg1_y.getChild("leg1_z");
		this.seg1mid = this.leg1_z.getChild("seg1mid");
		this.seg1top = this.leg1_z.getChild("seg1top");
		this.seg1bot = this.leg1_z.getChild("seg1bot");
		this.leg3_y = this.model.getChild("leg3_y");
		this.leg3_z = this.leg3_y.getChild("leg3_z");
		this.seg3mid = this.leg3_z.getChild("seg3mid");
		this.seg3top = this.leg3_z.getChild("seg3top");
		this.seg3bot = this.leg3_z.getChild("seg3bot");


	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition mainbody = root.addOrReplaceChild("mainbody", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -16.0F, -2.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -6.8F, -6.0F));

		PartDefinition leg2_y = root.addOrReplaceChild("leg2_y", CubeListBuilder.create(), PartPose.offsetAndRotation(6.7F, -11.4F, -6.8F, 0.0F, 0.7854F, 0.0F));

		PartDefinition leg2_z = leg2_y.addOrReplaceChild("leg2_z", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.3F, 0.6F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition seg2mid = leg2_z.addOrReplaceChild("seg2mid", CubeListBuilder.create().texOffs(10, 20).addBox(-2.0F, -7.2F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.1197F, 9.0F, -0.0263F));

		PartDefinition seg2top = leg2_z.addOrReplaceChild("seg2top", CubeListBuilder.create(), PartPose.offset(7.1197F, 2.0F, -0.0263F));

		PartDefinition seg2top_r1 = seg2top.addOrReplaceChild("seg2top_r1", CubeListBuilder.create().texOffs(10, 20).addBox(-1.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.0F, 0.05F, 0.0F, 0.0F, -0.829F));

		PartDefinition seg2bot = leg2_z.addOrReplaceChild("seg2bot", CubeListBuilder.create(), PartPose.offsetAndRotation(7.1197F, 10.0F, -0.0263F, 0.0F, 0.0F, -0.2618F));

		PartDefinition seg2bot_r1 = seg2bot.addOrReplaceChild("seg2bot_r1", CubeListBuilder.create().texOffs(9, 20).addBox(-1.6F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 2.4F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg4_y = root.addOrReplaceChild("leg4_y", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.8F, -11.4F, -6.8F, 0.0F, 2.3562F, 0.0F));

		PartDefinition leg4_z = leg4_y.addOrReplaceChild("leg4_z", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.3F, 0.6F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition seg4mid = leg4_z.addOrReplaceChild("seg4mid", CubeListBuilder.create().texOffs(10, 20).addBox(-2.0F, -7.2F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.1197F, 9.0F, -0.0263F));

		PartDefinition seg4top = leg4_z.addOrReplaceChild("seg4top", CubeListBuilder.create(), PartPose.offset(7.1197F, 2.0F, -0.0263F));

		PartDefinition seg4top_r1 = seg4top.addOrReplaceChild("seg4top_r1", CubeListBuilder.create().texOffs(10, 20).addBox(-1.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.0F, 0.05F, 0.0F, 0.0F, -0.829F));

		PartDefinition seg4bot = leg4_z.addOrReplaceChild("seg4bot", CubeListBuilder.create(), PartPose.offsetAndRotation(7.1197F, 10.0F, -0.0263F, 0.0F, 0.0F, -0.2618F));

		PartDefinition seg4bot_r1 = seg4bot.addOrReplaceChild("seg4bot_r1", CubeListBuilder.create().texOffs(9, 20).addBox(-1.6F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 2.4F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg1_y = root.addOrReplaceChild("leg1_y", CubeListBuilder.create(), PartPose.offsetAndRotation(6.7F, -11.4F, 6.7F, 0.0F, -0.7854F, 0.0F));

		PartDefinition leg1_z = leg1_y.addOrReplaceChild("leg1_z", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.3F, 0.6F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition seg1mid = leg1_z.addOrReplaceChild("seg1mid", CubeListBuilder.create().texOffs(10, 20).addBox(-2.0F, -7.2F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.1197F, 9.0F, -0.0263F));

		PartDefinition seg1top = leg1_z.addOrReplaceChild("seg1top", CubeListBuilder.create(), PartPose.offset(7.1197F, 2.0F, -0.0263F));

		PartDefinition seg1top_r1 = seg1top.addOrReplaceChild("seg1top_r1", CubeListBuilder.create().texOffs(10, 20).addBox(-1.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.0F, 0.05F, 0.0F, 0.0F, -0.829F));

		PartDefinition seg1bot = leg1_z.addOrReplaceChild("seg1bot", CubeListBuilder.create(), PartPose.offsetAndRotation(7.1197F, 10.0F, -0.0263F, 0.0F, 0.0F, -0.2618F));

		PartDefinition seg1bot_r1 = seg1bot.addOrReplaceChild("seg1bot_r1", CubeListBuilder.create().texOffs(9, 20).addBox(-1.6F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 2.4F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg3_y = root.addOrReplaceChild("leg3_y", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.8F, -11.4F, 6.7F, 0.0F, -2.3562F, 0.0F));

		PartDefinition leg3_z = leg3_y.addOrReplaceChild("leg3_z", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.3F, 0.6F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition seg3mid = leg3_z.addOrReplaceChild("seg3mid", CubeListBuilder.create().texOffs(10, 20).addBox(-2.0F, -7.2F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.1197F, 9.0F, -0.0263F));

		PartDefinition seg3top = leg3_z.addOrReplaceChild("seg3top", CubeListBuilder.create(), PartPose.offset(7.1197F, 2.0F, -0.0263F));

		PartDefinition seg1top_r2 = seg3top.addOrReplaceChild("seg1top_r2", CubeListBuilder.create().texOffs(10, 20).addBox(-1.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.0F, 0.05F, 0.0F, 0.0F, -0.829F));

		PartDefinition seg3bot = leg3_z.addOrReplaceChild("seg3bot", CubeListBuilder.create(), PartPose.offsetAndRotation(7.1197F, 10.0F, -0.0263F, 0.0F, 0.0F, -0.2618F));

		PartDefinition seg1bot_r2 = seg3bot.addOrReplaceChild("seg1bot_r2", CubeListBuilder.create().texOffs(9, 20).addBox(-1.6F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 2.4F, 0.0F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.model.getAllParts().forEach(ModelPart::resetPose);
		this.animate(((Asteron)entity).idleAnimationState, TranscendentAsteronAnimations.idle,ageInTicks,1f);
		this.animateWalk(TranscendentAsteronAnimations.idle,limbSwing,limbSwingAmount,1f,2f);
		this.animate(((Asteron)entity).piercingRaysAnimationState, TranscendentAsteronAnimations.piercing_rays,ageInTicks,1f);
		this.animate(((Asteron)entity).piercingRaysRecoveryAnimationState, TranscendentAsteronAnimations.piercing_rays_recover,ageInTicks,1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		model.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public ModelPart root(){
		return model;
	}

}