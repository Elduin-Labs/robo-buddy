package com.elduin.robo_buddy.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * A little round-headed robot: a big square head with a screen for a face, an antenna with a ball
 * on top, a small body with a chest screen, short arms and short legs. 21 pixels tall.
 */
public class RoboBuddyModel extends EntityModel<RoboBuddyRenderState> {

	private final ModelPart head;
	private final ModelPart antenna;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public RoboBuddyModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.antenna = this.head.getChild("antenna");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition head = root.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(0.0F, 11.0F, 0.0F));
		head.addOrReplaceChild("antenna",
				CubeListBuilder.create()
						.texOffs(32, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F)
						.texOffs(40, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offset(0.0F, -8.0F, 0.0F));

		root.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F),
				PartPose.offset(0.0F, 11.0F, 0.0F));

		root.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(24, 16).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F),
				PartPose.offset(-5.5F, 12.0F, 0.0F));
		root.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(36, 16).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F),
				PartPose.offset(5.5F, 12.0F, 0.0F));

		root.addOrReplaceChild("right_leg",
				CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
				PartPose.offset(-2.0F, 19.0F, 0.0F));
		root.addOrReplaceChild("left_leg",
				CubeListBuilder.create().texOffs(12, 32).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
				PartPose.offset(2.0F, 19.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(RoboBuddyRenderState state) {
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;

		// The antenna wobbles a little all the time.
		this.antenna.zRot = Mth.sin(state.ageInTicks * 0.12F) * 0.12F;
		this.antenna.xRot = Mth.cos(state.ageInTicks * 0.09F) * 0.08F;

		// Walking: arms and legs swing against each other.
		float swing = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.2F * state.walkAnimationSpeed;
		this.rightLeg.xRot = swing;
		this.leftLeg.xRot = -swing;
		this.rightArm.xRot = -swing;
		this.leftArm.xRot = swing;
		// Arms hang out a little to the side, and bob gently when standing still.
		float bob = Mth.sin(state.ageInTicks * 0.1F) * 0.05F;
		this.rightArm.zRot = 0.1F + bob;
		this.leftArm.zRot = -0.1F - bob;
	}
}
