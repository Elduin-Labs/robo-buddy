package com.elduin.robo_buddy.client;

import com.elduin.robo_buddy.RoboBuddy;
import com.elduin.robo_buddy.entity.RoboBuddyEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class RoboBuddyRenderer extends MobRenderer<RoboBuddyEntity, RoboBuddyRenderState, RoboBuddyModel> {

	private static final Identifier TEXTURE = RoboBuddy.id("textures/entity/robo_buddy/robo_buddy.png");

	public RoboBuddyRenderer(EntityRendererProvider.Context context) {
		super(context, new RoboBuddyModel(context.bakeLayer(RoboBuddyClient.ROBO_BUDDY)), 0.4F);
	}

	@Override
	public Identifier getTextureLocation(RoboBuddyRenderState state) {
		return TEXTURE;
	}

	@Override
	public RoboBuddyRenderState createRenderState() {
		return new RoboBuddyRenderState();
	}
}
