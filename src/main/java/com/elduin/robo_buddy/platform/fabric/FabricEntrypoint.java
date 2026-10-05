package com.elduin.robo_buddy.platform.fabric;

//? fabric {

import com.elduin.robo_buddy.RoboBuddy;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		RoboBuddy.onInitialize();
		FabricEventSubscriber.registerEvents();
	}
}
//?}
