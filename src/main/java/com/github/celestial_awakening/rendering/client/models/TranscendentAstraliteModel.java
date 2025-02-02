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

public class TranscendentAstraliteModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart center;

	public TranscendentAstraliteModel(ModelPart root) {
		System.out.println(root);
		this.center = root.getChild("center");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition center = partdefinition.addOrReplaceChild("center", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 0.0F));
		System.out.println(center);
		PartDefinition center_r1 = center.addOrReplaceChild("center_r1", CubeListBuilder.create().texOffs(20, 13).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition bot2_r1 = center.addOrReplaceChild("bot2_r1", CubeListBuilder.create().texOffs(15, 6).addBox(0.0F, 0.0F, -1.0F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(15, 7).addBox(0.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 6).addBox(0.0F, 0.5F, -1.0F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition center_r2 = center.addOrReplaceChild("center_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.8018F, -0.7119F, 0.5299F));
		System.out.println(meshdefinition.getRoot().getChild("center"));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		center.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}