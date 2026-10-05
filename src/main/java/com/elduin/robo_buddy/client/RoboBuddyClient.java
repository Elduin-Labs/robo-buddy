package com.elduin.robo_buddy.client;

import com.elduin.robo_buddy.RoboBuddy;
import com.elduin.robo_buddy.entity.ModEntities;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
//? if >=26 {
/*import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
*///? } else {
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
//? }
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class RoboBuddyClient {

	public static final ModelLayerLocation ROBO_BUDDY = new ModelLayerLocation(RoboBuddy.id("robo_buddy"), "main");

	private RoboBuddyClient() {
	}

	public static void register() {
		// Fabric renamed its model layer registry in 26.
		//? if >=26 {
		/*ModelLayerRegistry.registerModelLayer(ROBO_BUDDY, RoboBuddyModel::createBodyLayer);
		*///? } else {
		EntityModelLayerRegistry.registerModelLayer(ROBO_BUDDY, RoboBuddyModel::createBodyLayer);
		//? }
		EntityRendererRegistry.register(ModEntities.ROBO_BUDDY, RoboBuddyRenderer::new);
	}
}
