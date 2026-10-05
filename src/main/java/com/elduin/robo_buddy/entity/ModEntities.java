package com.elduin.robo_buddy.entity;

import com.elduin.robo_buddy.RoboBuddy;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class ModEntities {

	public static final ResourceKey<EntityType<?>> ROBO_BUDDY_KEY =
			ResourceKey.create(Registries.ENTITY_TYPE, RoboBuddy.id("robo_buddy"));

	// A bit shorter than a player. It only comes from the spawn egg, so there is no spawn rule.
	public static final EntityType<RoboBuddyEntity> ROBO_BUDDY = Registry.register(BuiltInRegistries.ENTITY_TYPE, ROBO_BUDDY_KEY,
			FabricEntityType.Builder.createMob(RoboBuddyEntity::new, MobCategory.CREATURE, mob -> mob
							.defaultAttributes(RoboBuddyEntity::createAttributes))
					.sized(0.6F, 1.4F)
					.clientTrackingRange(10)
					.build(ROBO_BUDDY_KEY));

	public static final ResourceKey<Item> SPAWN_EGG_KEY =
			ResourceKey.create(Registries.ITEM, RoboBuddy.id("robo_buddy_spawn_egg"));

	public static final Item ROBO_BUDDY_SPAWN_EGG = Registry.register(BuiltInRegistries.ITEM, SPAWN_EGG_KEY,
			new SpawnEggItem(new Item.Properties().spawnEgg(ROBO_BUDDY).setId(SPAWN_EGG_KEY)));

	private ModEntities() {
	}

	public static void register() {
		// Touching this class registers everything above.
	}
}
