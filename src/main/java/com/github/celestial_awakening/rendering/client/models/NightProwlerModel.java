package com.github.celestial_awakening.rendering.client.models;

import com.github.celestial_awakening.entity.living.NightProwler;
import com.github.celestial_awakening.rendering.client.animations.NightProwlerAnimations;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class NightProwlerModel<T extends Entity>  extends HierarchicalModel<T> {
	private final ModelPart model;
	private final ModelPart upperhalf;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart upperbody;
	private final ModelPart lowerbody;
	private final ModelPart tail;
	private final ModelPart limbs;
	private final ModelPart f_left_leg;
	private final ModelPart fl_upper;
	private final ModelPart fl_lower;
	private final ModelPart f_right_leg;
	private final ModelPart fr_upper;
	private final ModelPart fr_lower;
	private final ModelPart b_right_leg;
	private final ModelPart br_upper;
	private final ModelPart br_lower;
	private final ModelPart b_left_leg;
	private final ModelPart bl_upper;
	private final ModelPart bl_lower;

	public NightProwlerModel(@NotNull ModelPart root) {
		super(RenderType::entityTranslucent);
		this.model = root.getChild("root");
		this.upperhalf = this.model.getChild("upperhalf");
		this.head = this.upperhalf.getChild("head");
		this.body = this.upperhalf.getChild("body");
		this.upperbody = this.body.getChild("upperbody");
		this.lowerbody = this.body.getChild("lowerbody");
		this.tail = this.lowerbody.getChild("tail");
		this.limbs = this.model.getChild("limbs");
		this.f_left_leg = this.limbs.getChild("f_left_leg");
		this.fl_upper = this.f_left_leg.getChild("fl_upper");
		this.fl_lower = this.f_left_leg.getChild("fl_lower");
		this.f_right_leg = this.limbs.getChild("f_right_leg");
		this.fr_upper = this.f_right_leg.getChild("fr_upper");
		this.fr_lower = this.f_right_leg.getChild("fr_lower");
		this.b_right_leg = this.limbs.getChild("b_right_leg");
		this.br_upper = this.b_right_leg.getChild("br_upper");
		this.br_lower = this.b_right_leg.getChild("br_lower");
		this.b_left_leg = this.limbs.getChild("b_left_leg");
		this.bl_upper = this.b_left_leg.getChild("bl_upper");
		this.bl_lower = this.b_left_leg.getChild("bl_lower");

	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition model = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition upperhalf = model.addOrReplaceChild("upperhalf", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition head = upperhalf.addOrReplaceChild("head", CubeListBuilder.create().texOffs(41, 37).addBox(-2.0F, -10.0F, 5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 11).addBox(-6.0F, -10.0F, 5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 43).addBox(-6.0F, -8.0F, 1.0F, 6.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(56, 24).addBox(-5.0F, -6.0F, -2.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -4.0F, -12.0F));

		PartDefinition body = upperhalf.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upperbody = body.addOrReplaceChild("upperbody", CubeListBuilder.create().texOffs(28, 47).addBox(-7.5F, -9.5F, -1.0F, 9.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -4.0F, -5.0F));

		PartDefinition lowerbody = body.addOrReplaceChild("lowerbody", CubeListBuilder.create().texOffs(22, 7).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -4.0F, 0.0F));

		PartDefinition tail = lowerbody.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(-3.0F, -3.0F, 13.0F));

		PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(4, 2).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -2.0F, -0.6981F, 0.0F, 0.0F));

		PartDefinition limbs = model.addOrReplaceChild("limbs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition f_left_leg = limbs.addOrReplaceChild("f_left_leg", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, -3.5F));

		PartDefinition fl_upper = f_left_leg.addOrReplaceChild("fl_upper", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, -5.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 1.5F));

		PartDefinition fl_lower = f_left_leg.addOrReplaceChild("fl_lower", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition f_right_leg = limbs.addOrReplaceChild("f_right_leg", CubeListBuilder.create(), PartPose.offset(-4.0F, -8.0F, -3.5F));

		PartDefinition fr_upper = f_right_leg.addOrReplaceChild("fr_upper", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, -5.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 1.5F));

		PartDefinition fr_lower = f_right_leg.addOrReplaceChild("fr_lower", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 1.5F));

		PartDefinition b_right_leg = limbs.addOrReplaceChild("b_right_leg", CubeListBuilder.create(), PartPose.offset(-4.0F, -8.0F, 8.5F));

		PartDefinition br_upper = b_right_leg.addOrReplaceChild("br_upper", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -1.5F));

		PartDefinition br_lower = b_right_leg.addOrReplaceChild("br_lower", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -1.5F));

		PartDefinition b_left_leg = limbs.addOrReplaceChild("b_left_leg", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 8.5F));

		PartDefinition bl_upper = b_left_leg.addOrReplaceChild("bl_upper", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -1.5F));

		PartDefinition bl_lower = b_left_leg.addOrReplaceChild("bl_lower", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -1.5F));

		return LayerDefinition.create(meshdefinition, 64,64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.model.getAllParts().forEach(ModelPart::resetPose);
		this.animate(((NightProwler)entity).idleAnimationState, NightProwlerAnimations.idle,ageInTicks,1f);
		this.animateWalk(NightProwlerAnimations.walk,limbSwing,limbSwingAmount,1f,2f);
		this.animate(((NightProwler)entity).crouchAnimationState, NightProwlerAnimations.crouch,ageInTicks,1f);
		this.animate(((NightProwler)entity).leapAnimationState,NightProwlerAnimations.leap,ageInTicks,1f);
		this.animate(((NightProwler)entity).leapRecoveryAnimationState,NightProwlerAnimations.leapEnd,ageInTicks,1f);
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