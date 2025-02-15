package com.github.celestial_awakening.rendering.client.models;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class TranscendentAsteronModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart root;
	private final ModelPart face;
	private final ModelPart leg1;
	private final ModelPart seg1mid;
	private final ModelPart seg1top;
	private final ModelPart seg1bot;
	private final ModelPart leg3;
	private final ModelPart seg3mid;
	private final ModelPart seg3top;
	private final ModelPart seg3bot;
	private final ModelPart leg4;
	private final ModelPart seg4mid;
	private final ModelPart seg4top;
	private final ModelPart seg4bot;
	private final ModelPart leg2;
	private final ModelPart seg2mid;
	private final ModelPart seg2top;
	private final ModelPart seg2bot;



	public TranscendentAsteronModel(ModelPart mp_root) {
		this.root = mp_root.getChild("root");
		this.face = this.root.getChild("face");
		this.leg1 = this.root.getChild("leg1");
		this.seg1mid = this.leg1.getChild("seg1mid");
		this.seg1top = this.leg1.getChild("seg1top");
		this.seg1bot = this.leg1.getChild("seg1bot");
		this.leg3 = this.root.getChild("leg3");
		this.seg3mid = this.leg3.getChild("seg3mid");
		this.seg3top = this.leg3.getChild("seg3top");
		this.seg3bot = this.leg3.getChild("seg3bot");
		this.leg4 = this.root.getChild("leg4");
		this.seg4mid = this.leg4.getChild("seg4mid");
		this.seg4top = this.leg4.getChild("seg4top");
		this.seg4bot = this.leg4.getChild("seg4bot");
		this.leg2 = this.root.getChild("leg2");
		this.seg2mid = this.leg2.getChild("seg2mid");
		this.seg2top = this.leg2.getChild("seg2top");
		this.seg2bot = this.leg2.getChild("seg2bot");


	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -24.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition face = root.addOrReplaceChild("face", CubeListBuilder.create().texOffs(16, 16).addBox(-8.0F, -8.0F, 1.99F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, -10.0F));

		PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.15F, 0.0F, -4.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition seg1mid = leg1.addOrReplaceChild("seg1mid", CubeListBuilder.create().texOffs(10, 20).addBox(-2.0F, -7.2F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(17.0F, -5.0F, 2.0F));

		PartDefinition seg1top = leg1.addOrReplaceChild("seg1top", CubeListBuilder.create(), PartPose.offset(17.0F, -9.0F, 2.0F));

		PartDefinition seg1top_r1 = seg1top.addOrReplaceChild("seg1top_r1", CubeListBuilder.create().texOffs(10, 20).addBox(-1.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, 0.0F, 0.0F, 0.0F, -0.829F));

		PartDefinition seg1bot = leg1.addOrReplaceChild("seg1bot", CubeListBuilder.create(), PartPose.offset(13.0F, -1.0F, 2.0F));

		PartDefinition seg1bot_r1 = seg1bot.addOrReplaceChild("seg1bot_r1", CubeListBuilder.create().texOffs(9, 20).addBox(-1.6F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(3.9F, 0.0F, -1.1F, 0.0F, -2.3562F, 0.0F));

		PartDefinition seg3mid = leg3.addOrReplaceChild("seg3mid", CubeListBuilder.create().texOffs(10, 20).addBox(-2.0F, -7.2F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(17.0F, -5.0F, 2.0F));

		PartDefinition seg3top = leg3.addOrReplaceChild("seg3top", CubeListBuilder.create(), PartPose.offset(17.0F, -9.0F, 2.0F));

		PartDefinition seg3top_r1 = seg3top.addOrReplaceChild("seg3top_r1", CubeListBuilder.create().texOffs(10, 20).addBox(-1.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, 0.0F, 0.0F, 0.0F, -0.829F));

		PartDefinition seg3bot = leg3.addOrReplaceChild("seg3bot", CubeListBuilder.create(), PartPose.offset(13.0F, -1.0F, 2.0F));

		PartDefinition seg3bot_r1 = seg3bot.addOrReplaceChild("seg3bot_r1", CubeListBuilder.create().texOffs(10, 20).addBox(-1.6F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(1.15F, 0.0F, 4.05F, 0.0F, 2.3562F, 0.0F));

		PartDefinition seg4mid = leg4.addOrReplaceChild("seg4mid", CubeListBuilder.create().texOffs(10, 20).addBox(-2.0F, -7.2F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(17.0F, -5.0F, 2.0F));

		PartDefinition seg4top = leg4.addOrReplaceChild("seg4top", CubeListBuilder.create(), PartPose.offset(17.0F, -9.0F, 2.0F));

		PartDefinition seg4top_r1 = seg4top.addOrReplaceChild("seg4top_r1", CubeListBuilder.create().texOffs(10, 20).addBox(-1.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, 0.0F, 0.0F, 0.0F, -0.829F));

		PartDefinition seg4bot = leg4.addOrReplaceChild("seg4bot", CubeListBuilder.create(), PartPose.offset(13.0F, -1.0F, 2.0F));

		PartDefinition seg4bot_r1 = seg4bot.addOrReplaceChild("seg4bot_r1", CubeListBuilder.create().texOffs(10, 20).addBox(-1.6F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.8F, 0.0F, -1.6F, 0.0F, 0.7854F, 0.0F));

		PartDefinition seg2mid = leg2.addOrReplaceChild("seg2mid", CubeListBuilder.create().texOffs(10, 20).addBox(-2.0F, -7.2F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(12.8F, -5.0F, 1.6F));

		PartDefinition seg2top = leg2.addOrReplaceChild("seg2top", CubeListBuilder.create(), PartPose.offset(12.8F, -9.0F, 1.6F));

		PartDefinition seg2top_r1 = seg2top.addOrReplaceChild("seg2top_r1", CubeListBuilder.create().texOffs(10, 20).addBox(-1.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, 0.05F, 0.0F, 0.0F, -0.829F));

		PartDefinition seg2bot = leg2.addOrReplaceChild("seg2bot", CubeListBuilder.create(), PartPose.offset(8.8F, -1.0F, 1.6F));

		PartDefinition seg2bot_r1 = seg2bot.addOrReplaceChild("seg2bot_r1", CubeListBuilder.create().texOffs(9, 20).addBox(-1.6F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, 0.0F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public ModelPart root(){
		return root;
	}

}