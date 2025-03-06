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
	//public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "transcendent_astralite"), "main");
	private final ModelPart root;
	private final ModelPart botRightLimb;
	private final ModelPart botLeftLimb;
	private final ModelPart upperLeftLimb;
	private final ModelPart upperRightLimb;
	private final ModelPart body;
	private final ModelPart tophalf2;
	private final ModelPart bone6;
	private final ModelPart bone7;
	private final ModelPart bottomhalf2;
	private final ModelPart bone8;
	private final ModelPart bone9;

	public TranscendentAstraliteModel(ModelPart root) {
		this.root = root.getChild("root");
		this.botRightLimb = this.root.getChild("botRightLimb");
		this.botLeftLimb = this.root.getChild("botLeftLimb");
		this.upperLeftLimb = this.root.getChild("upperLeftLimb");
		this.upperRightLimb = this.root.getChild("upperRightLimb");
		this.body = this.root.getChild("body");
		this.tophalf2 = this.body.getChild("tophalf2");
		this.bone6 = this.tophalf2.getChild("bone6");
		this.bone7 = this.tophalf2.getChild("bone7");
		this.bottomhalf2 = this.body.getChild("bottomhalf2");
		this.bone8 = this.bottomhalf2.getChild("bone8");
		this.bone9 = this.bottomhalf2.getChild("bone9");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition botRightLimb = root.addOrReplaceChild("botRightLimb", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition brSeg4_r1 = botRightLimb.addOrReplaceChild("brSeg4_r1", CubeListBuilder.create().texOffs(6, 7).mirror().addBox(0.0F, -0.0147F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.6403F, 4.879F, -6.0556F, 1.1049F, -0.3947F, -2.9506F));

		PartDefinition brSeg3_r1 = botRightLimb.addOrReplaceChild("brSeg3_r1", CubeListBuilder.create().texOffs(6, 7).mirror().addBox(0.0F, -0.0147F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.7766F, 5.3083F, -6.9977F, 0.5165F, -1.0737F, -2.1445F));

		PartDefinition brSeg2_r1 = botRightLimb.addOrReplaceChild("brSeg2_r1", CubeListBuilder.create().texOffs(6, 7).mirror().addBox(0.0F, -0.0147F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 4.3569F, -4.91F, -0.8994F, -0.8411F, -0.5347F));

		PartDefinition brSeg1_r1 = botRightLimb.addOrReplaceChild("brSeg1_r1", CubeListBuilder.create().texOffs(6, 7).mirror().addBox(-1.0F, -0.4147F, -0.09F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 2.5F, -1.8F, -1.1432F, 0.0F, 0.0F));

		PartDefinition botLeftLimb = root.addOrReplaceChild("botLeftLimb", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition blSeg4_r1 = botLeftLimb.addOrReplaceChild("blSeg4_r1", CubeListBuilder.create().texOffs(6, 7).addBox(-1.0F, -0.0147F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.6403F, 4.879F, -6.0556F, 1.1049F, 0.3947F, 2.9506F));

		PartDefinition blSeg3_r1 = botLeftLimb.addOrReplaceChild("blSeg3_r1", CubeListBuilder.create().texOffs(6, 7).addBox(-1.0F, -0.0147F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.7766F, 5.3083F, -6.9977F, 0.5165F, 1.0737F, 2.1445F));

		PartDefinition blSeg2_r1 = botLeftLimb.addOrReplaceChild("blSeg2_r1", CubeListBuilder.create().texOffs(6, 7).addBox(-1.0F, -0.0147F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 4.3569F, -4.91F, -0.8994F, 0.8411F, 0.5347F));

		PartDefinition blSeg1_r1 = botLeftLimb.addOrReplaceChild("blSeg1_r1", CubeListBuilder.create().texOffs(6, 7).addBox(0.0F, -0.4147F, -0.09F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 2.5F, -1.8F, -1.1432F, 0.0F, 0.0F));

		PartDefinition upperLeftLimb = root.addOrReplaceChild("upperLeftLimb", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition ulSeg4_r1 = upperLeftLimb.addOrReplaceChild("ulSeg4_r1", CubeListBuilder.create().texOffs(6, 7).addBox(-1.0F, -3.9853F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.6403F, -2.379F, -6.0556F, -1.1049F, 0.3947F, -2.9506F));

		PartDefinition ulSeg3_r1 = upperLeftLimb.addOrReplaceChild("ulSeg3_r1", CubeListBuilder.create().texOffs(6, 7).addBox(-1.0F, -3.9853F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.7766F, -2.8083F, -6.9977F, -0.5165F, 1.0737F, -2.1445F));

		PartDefinition ulSeg2_r1 = upperLeftLimb.addOrReplaceChild("ulSeg2_r1", CubeListBuilder.create().texOffs(6, 7).addBox(-1.0F, -3.9853F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -1.8569F, -4.91F, 0.8994F, 0.8411F, -0.5347F));

		PartDefinition ulSeg1_r1 = upperLeftLimb.addOrReplaceChild("ulSeg1_r1", CubeListBuilder.create().texOffs(6, 7).addBox(0.0F, -3.5853F, -0.09F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -1.8F, 1.1432F, 0.0F, 0.0F));

		PartDefinition upperRightLimb = root.addOrReplaceChild("upperRightLimb", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition urSeg4_r1 = upperRightLimb.addOrReplaceChild("urSeg4_r1", CubeListBuilder.create().texOffs(6, 7).mirror().addBox(0.0F, -3.9853F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.6403F, -2.379F, -6.0556F, -1.1049F, -0.3947F, 2.9506F));

		PartDefinition urSeg3_r1 = upperRightLimb.addOrReplaceChild("urSeg3_r1", CubeListBuilder.create().texOffs(6, 7).mirror().addBox(0.0F, -3.9853F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.7766F, -2.8083F, -6.9977F, -0.5165F, -1.0737F, 2.1445F));

		PartDefinition urSeg2_r1 = upperRightLimb.addOrReplaceChild("urSeg2_r1", CubeListBuilder.create().texOffs(6, 7).mirror().addBox(0.0F, -3.9853F, -0.49F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -1.8569F, -4.91F, 0.8994F, -0.8411F, 0.5347F));

		PartDefinition urSeg1_r1 = upperRightLimb.addOrReplaceChild("urSeg1_r1", CubeListBuilder.create().texOffs(6, 7).mirror().addBox(-1.0F, -3.5853F, -0.09F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 0.0F, -1.8F, 1.1432F, 0.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition tophalf2 = body.addOrReplaceChild("tophalf2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.9F, 0.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition bone6 = tophalf2.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r1 = bone6.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(5, 9).addBox(-2.5F, -6.0F, 0.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4276F, 0.0F, 0.0F));

		PartDefinition cube_r2 = bone6.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(5, 9).addBox(-2.5F, -6.0F, 0.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4276F, 0.0F, 0.0F));

		PartDefinition bone7 = tophalf2.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = bone7.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(5, 9).addBox(-2.5F, -6.0F, 0.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4276F, 0.0F, 0.0F));

		PartDefinition cube_r4 = bone7.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(5, 9).addBox(-2.5F, -6.0F, 0.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4276F, 0.0F, 0.0F));

		PartDefinition bottomhalf2 = body.addOrReplaceChild("bottomhalf2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone8 = bottomhalf2.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r5 = bone8.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(5, 9).addBox(-2.5F, -6.0F, 0.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4276F, 0.0F, 0.0F));

		PartDefinition cube_r6 = bone8.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(5, 9).addBox(-2.5F, -6.0F, 0.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4276F, 0.0F, 0.0F));

		PartDefinition bone9 = bottomhalf2.addOrReplaceChild("bone9", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r7 = bone9.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(5, 9).addBox(-2.5F, -6.0F, 0.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4276F, 0.0F, 0.0F));

		PartDefinition cube_r8 = bone9.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(5, 9).addBox(-2.5F, -6.0F, 0.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4276F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}