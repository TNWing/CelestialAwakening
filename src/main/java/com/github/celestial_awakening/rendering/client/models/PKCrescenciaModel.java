package com.github.celestial_awakening.rendering.client.models;// Made with Blockbench 4.12.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.phantom_knights.PhantomKnight_Crescencia;
import com.github.celestial_awakening.rendering.client.animations.PK_CrescenciaAnimations;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class PKCrescenciaModel<T extends Entity>extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CelestialAwakening.createResourceLocation("temppk"), "main");
	private final ModelPart model;
	private final ModelPart headPivot;
	private final ModelPart bodyPivot;
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
	private final ModelPart armLPivot;
	private final ModelPart upperarmL;
	private final ModelPart lowerarmL;
	private final ModelPart armRPivot;
	private final ModelPart lowerarmR;
	private final ModelPart sword;
	private final ModelPart handle;
	private final ModelPart upperarmR;
	private final ModelPart torsoPivot;

	public PKCrescenciaModel(ModelPart root) {
		this.model = root.getChild("root");
		this.headPivot = this.model.getChild("headPivot");
		this.bodyPivot = this.model.getChild("bodyPivot");
		this.legRPivot = this.bodyPivot.getChild("legRPivot");
		this.thighR = this.legRPivot.getChild("thighR");
		this.lowerlegR = this.legRPivot.getChild("lowerlegR");
		this.shinR = this.lowerlegR.getChild("shinR");
		this.footR = this.lowerlegR.getChild("footR");
		this.legLPivot = this.bodyPivot.getChild("legLPivot");
		this.lowerlegL = this.legLPivot.getChild("lowerlegL");
		this.footL = this.lowerlegL.getChild("footL");
		this.shinL = this.lowerlegL.getChild("shinL");
		this.thighL = this.legLPivot.getChild("thighL");
		this.upperbody = this.bodyPivot.getChild("upperbody");
		this.armLPivot = this.upperbody.getChild("armLPivot");
		this.upperarmL = this.armLPivot.getChild("upperarmL");
		this.lowerarmL = this.armLPivot.getChild("lowerarmL");
		this.armRPivot = this.upperbody.getChild("armRPivot");
		this.lowerarmR = this.armRPivot.getChild("lowerarmR");
		this.sword = this.lowerarmR.getChild("sword");
		this.handle = this.sword.getChild("handle");
		this.upperarmR = this.armRPivot.getChild("upperarmR");
		this.torsoPivot = this.upperbody.getChild("torsoPivot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition headPivot = root.addOrReplaceChild("headPivot", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -32.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyPivot = root.addOrReplaceChild("bodyPivot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition legRPivot = bodyPivot.addOrReplaceChild("legRPivot", CubeListBuilder.create(), PartPose.offset(-2.0F, -12.0F, 0.0F));

		PartDefinition thighR = legRPivot.addOrReplaceChild("thighR", CubeListBuilder.create().texOffs(0, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lowerlegR = legRPivot.addOrReplaceChild("lowerlegR", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, -2.0F));

		PartDefinition shinR = lowerlegR.addOrReplaceChild("shinR", CubeListBuilder.create().texOffs(0, 41).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition footR = lowerlegR.addOrReplaceChild("footR", CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 6.0F, 1.0F));

		PartDefinition legLPivot = bodyPivot.addOrReplaceChild("legLPivot", CubeListBuilder.create(), PartPose.offset(2.0F, -12.0F, 0.0F));

		PartDefinition lowerlegL = legLPivot.addOrReplaceChild("lowerlegL", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, -2.0F));

		PartDefinition footL = lowerlegL.addOrReplaceChild("footL", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 3.0F));

		PartDefinition shinL = lowerlegL.addOrReplaceChild("shinL", CubeListBuilder.create().texOffs(0, 41).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition thighL = legLPivot.addOrReplaceChild("thighL", CubeListBuilder.create().texOffs(0, 39).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -1.0F));

		PartDefinition upperbody = bodyPivot.addOrReplaceChild("upperbody", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armLPivot = upperbody.addOrReplaceChild("armLPivot", CubeListBuilder.create(), PartPose.offset(4.0F, -22.0F, 0.0F));

		PartDefinition upperarmL = armLPivot.addOrReplaceChild("upperarmL", CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, -3.5F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.5F, 0.0F));

		PartDefinition lowerarmL = armLPivot.addOrReplaceChild("lowerarmL", CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 6.0F, 2.0F));

		PartDefinition armRPivot = upperbody.addOrReplaceChild("armRPivot", CubeListBuilder.create(), PartPose.offset(-4.0F, -22.0F, 0.0F));

		PartDefinition lowerarmR = armRPivot.addOrReplaceChild("lowerarmR", CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, -0.0969F, -4.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 6.0F, 2.0F));

		PartDefinition sword = lowerarmR.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(53, 53).addBox(-1.5063F, -2.0394F, -0.4976F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 53).addBox(-2.5063F, 0.9606F, -0.4976F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 35).addBox(-1.0063F, 2.9606F, -0.4976F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0531F, 5.8732F, -2.8524F, -1.5708F, 0.5236F, 1.5708F));

		PartDefinition handle = sword.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(29, 23).addBox(-2.4969F, 0.8768F, -1.4476F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(29, 23).addBox(0.5031F, 5.8768F, -1.4476F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(29, 23).addBox(-5.4969F, 5.8768F, -1.4476F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(27, 23).addBox(-3.4969F, 3.8768F, -1.4476F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(29, 23).addBox(-0.4969F, 4.8768F, -1.4476F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(29, 23).addBox(-4.4969F, 4.8768F, -1.4476F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9907F, -0.9162F, 0.95F));

		PartDefinition upperarmR = armRPivot.addOrReplaceChild("upperarmR", CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, -3.5969F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.5F, 0.0F));

		PartDefinition torsoPivot = upperbody.addOrReplaceChild("torsoPivot", CubeListBuilder.create().texOffs(16, 15).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(15, 15).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.model.getAllParts().forEach(ModelPart::resetPose);
		if (((AbstractCAMonster)entity).getActionId()==0){
			this.animateWalk(PK_CrescenciaAnimations.walk,limbSwing,limbSwingAmount,1f,2f);
		}
		this.animate(((PhantomKnight_Crescencia)entity).asleepAnimationState,PK_CrescenciaAnimations.idle,ageInTicks,1f);
		this.animate(((PhantomKnight_Crescencia)entity).idleAnimationState,PK_CrescenciaAnimations.idle,ageInTicks,1f);
		this.animate(((PhantomKnight_Crescencia)entity).wakeUpAnimationState,PK_CrescenciaAnimations.wakeUp,ageInTicks,1f);
		this.animate(((PhantomKnight_Crescencia)entity).strikethroughStartAnimationState, PK_CrescenciaAnimations.strikeThroughStart,ageInTicks,1f);
		this.animate(((PhantomKnight_Crescencia)entity).strikethroughStrikeAnimationState, PK_CrescenciaAnimations.strikeThroughStrike,ageInTicks,1f);
		this.animate(((PhantomKnight_Crescencia)entity).moonCutterAnimationState, PK_CrescenciaAnimations.mooncutter,ageInTicks,1f);
		setupAttackAnimation((T) entity,limbSwing);
	}
	protected void setupAttackAnimation(T p_102858_, float p_102859_) {
		if (!(this.attackTime <= 0.0F)) {
			ModelPart modelpart = this.armRPivot;
			float f = this.attackTime;
			this.upperbody.yRot = Mth.sin(Mth.sqrt(f) * ((float)Math.PI * 2F)) * 0.2F;
			this.armRPivot.z = Mth.sin(this.upperbody.yRot) * 5.0F;
			this.armRPivot.x = -Mth.cos(this.upperbody.yRot) * 5.0F;
			this.armLPivot.z = -Mth.sin(this.upperbody.yRot) * 5.0F;
			this.armLPivot.x = Mth.cos(this.upperbody.yRot) * 5.0F;
			this.armRPivot.yRot += this.upperbody.yRot;
			this.armLPivot.yRot += this.upperbody.yRot;
			this.armLPivot.xRot += this.upperbody.yRot;
			f = 1.0F - this.attackTime;
			f *= f;
			f *= f;
			f = 1.0F - f;
			float f1 = Mth.sin(f * (float)Math.PI);
			float f2 = Mth.sin(this.attackTime * (float)Math.PI) * -(this.headPivot.xRot - 0.7F) * 0.75F;
			modelpart.xRot -= f1 * 1.2F + f2;
			modelpart.yRot += this.upperbody.yRot * 2.0F;
			modelpart.zRot += Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
		}
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