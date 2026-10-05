package com.elduin.robo_buddy.platform.fabric;

//? fabric {

import com.elduin.robo_buddy.entity.ModEntities;
import com.elduin.robo_buddy.entity.RoboBuddyEntity;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.world.item.CreativeModeTabs;
//? if >=26 {
/*import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
*///? } else {
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
//? }

public class FabricEventSubscriber {

	public static void registerEvents() {
		// A Robo Buddy that just asked "are you Elduin?" listens for the answer.
		ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
			String text = message.signedContent();
			double range = RoboBuddyEntity.chatRange();
			sender.level().getEntitiesOfClass(RoboBuddyEntity.class, sender.getBoundingBox().inflate(range))
					.forEach(robot -> robot.hearChat(sender, text));
		});

		// Fabric API renamed "item groups" to "creative mode tabs" in 26.
		//? if >=26 {
		/*CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS)
				.register(output -> output.accept(ModEntities.ROBO_BUDDY_SPAWN_EGG));
		*///? } else {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS)
				.register(entries -> entries.accept(ModEntities.ROBO_BUDDY_SPAWN_EGG));
		//? }
	}
}
//?}
