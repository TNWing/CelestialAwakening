package com.github.celestial_awakening.rendering.client.models;// Made with Blockbench 4.12.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.github.celestial_awakening.entity.animations.PK_CrescenciaAnimations;
import com.github.celestial_awakening.entity.living.phantom_knights.PhantomKnight_Crescencia;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PKCrescenciaModel<T extends Entity>extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "temppk"), "main");
	private final ModelPart model;
	private final ModelPart legRPivot;
	private final ModelPart thighR;
	private final ModelPart lowerlegR;
	private final ModelPart shinR;
	private final ModelPart footR;
	private final ModelPart legLPivot;
	private final ModelPart lowerlegL;
	private final ModelPart footL;
	private final ModelPart shinL;
	private final ModelPart thighL;
	private final ModelPart upperbody;
	private final ModelPart elbowL;
	private final ModelPart armLPivot;
	private final ModelPart upperarmL;
	private final ModelPart lowerarmL;
	private final ModelPart armRPivot;
	private final ModelPart lowerarmR;
	private final ModelPart sword;
	private final ModelPart upperarmR;
	private final ModelPart torsoPivot;
	private final ModelPart headPivot;

	public PKCrescenciaModel(ModelPart root) {
		this.model = root.getChild("model");
		this.legRPivot = this.model.getChild("legRPivot");
		this.thighR = this.legRPivot.getChild("thighR");
		this.lowerlegR = this.legRPivot.getChild("lowerlegR");
		this.shinR = this.lowerlegR.getChild("shinR");
		this.footR = this.lowerlegR.getChild("footR");
		this.legLPivot = this.model.getChild("legLPivot");
		this.lowerlegL = this.legLPivot.getChild("lowerlegL");
		this.footL = this.lowerlegL.getChild("footL");
		this.shinL = this.lowerlegL.getChild("shinL");
		this.thighL = this.legLPivot.getChild("thighL");
		this.upperbody = this.model.getChild("upperbody");
		this.elbowL = this.upperbody.getChild("elbowL");
		this.armLPivot = this.upperbody.getChild("armLPivot");
		this.upperarmL = this.armLPivot.getChild("upperarmL");
		this.lowerarmL = this.armLPivot.getChild("lowerarmL");
		this.armRPivot = this.upperbody.getChild("armRPivot");
		this.lowerarmR = this.armRPivot.getChild("lowerarmR");
		this.sword = this.lowerarmR.getChild("sword");
		this.upperarmR = this.armRPivot.getChild("upperarmR");
		this.torsoPivot = this.upperbody.getChild("torsoPivot");
		this.headPivot = this.model.getChild("headPivot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition model = partdefinition.addOrReplaceChild("model", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition legRPivot = model.addOrReplaceChild("legRPivot", CubeListBuilder.create(), PartPose.offset(-2.0F, -12.0F, 0.0F));

		PartDefinition thighR = legRPivot.addOrReplaceChild("thighR", CubeListBuilder.create().texOffs(1, 23).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lowerlegR = legRPivot.addOrReplaceChild("lowerlegR", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, -2.0F));

		PartDefinition shinR = lowerlegR.addOrReplaceChild("shinR", CubeListBuilder.create().texOffs(14, 7).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition footR = lowerlegR.addOrReplaceChild("footR", CubeListBuilder.create().texOffs(9, 23).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 6.0F, 1.0F));

		PartDefinition legLPivot = model.addOrReplaceChild("legLPivot", CubeListBuilder.create(), PartPose.offset(2.0F, -12.0F, 0.0F));

		PartDefinition lowerlegL = legLPivot.addOrReplaceChild("lowerlegL", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, -2.0F));

		PartDefinition footL = lowerlegL.addOrReplaceChild("footL", CubeListBuilder.create().texOffs(9, 23).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 3.0F));

		PartDefinition shinL = lowerlegL.addOrReplaceChild("shinL", CubeListBuilder.create().texOffs(14, 7).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition thighL = legLPivot.addOrReplaceChild("thighL", CubeListBuilder.create().texOffs(1, 23).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -1.0F));

		PartDefinition upperbody = model.addOrReplaceChild("upperbody", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition elbowL = upperbody.addOrReplaceChild("elbowL", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -16.0F, -1.0F));

		PartDefinition armLPivot = upperbody.addOrReplaceChild("armLPivot", CubeListBuilder.create(), PartPose.offset(6.0F, -22.0F, 0.0F));

		PartDefinition upperarmL = armLPivot.addOrReplaceChild("upperarmL", CubeListBuilder.create().texOffs(0, 40).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

		PartDefinition lowerarmL = armLPivot.addOrReplaceChild("lowerarmL", CubeListBuilder.create().texOffs(0, 40).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, -2.0F));

		PartDefinition armRPivot = upperbody.addOrReplaceChild("armRPivot", CubeListBuilder.create(), PartPose.offset(-6.0F, -22.0F, 0.0F));

		PartDefinition lowerarmR = armRPivot.addOrReplaceChild("lowerarmR", CubeListBuilder.create().texOffs(0, 40).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, -2.0F));

		PartDefinition sword = lowerarmR.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(29, 25).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 25).addBox(-2.5F, -4.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 25).addBox(-3.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 25).addBox(2.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 25).addBox(1.5F, -4.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 25).addBox(-1.5F, -3.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 26).addBox(-1.0F, -3.0F, 0.0F, 2.0F, -9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.35F, 1.5708F, 0.2182F, -1.5708F));

		PartDefinition upperarmR = armRPivot.addOrReplaceChild("upperarmR", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

		PartDefinition torsoPivot = upperbody.addOrReplaceChild("torsoPivot", CubeListBuilder.create().texOffs(16, 15).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(15, 15).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition headPivot = model.addOrReplaceChild("headPivot", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -32.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 66, 67);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.model.getAllParts().forEach(ModelPart::resetPose);
		this.animate(((PhantomKnight_Crescencia)entity).idleAnimationState,PK_CrescenciaAnimations.idle,ageInTicks,1f);
		this.animateWalk(PK_CrescenciaAnimations.walk,limbSwing,limbSwingAmount,1f,2f);
		this.animate(((PhantomKnight_Crescencia)entity).nightSlashStartAnimationState, PK_CrescenciaAnimations.nightSlashStart,ageInTicks,1f);
		this.animate(((PhantomKnight_Crescencia)entity).nightSlashStrikeAnimationState, PK_CrescenciaAnimations.nightSlashStrike,ageInTicks,1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		model.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.model;
	}
}