package com.elduin.robo_buddy.platform.fabric;

//? fabric {

import com.elduin.robo_buddy.RoboBuddy;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		RoboBuddy.onInitializeClient();
	}

}
//?}
