package com.elduin.robo_buddy.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A little blue robot. It wanders around and looks at you. Hit it and it begs you not to delete it,
 * then goes flying 255 blocks away. It never gets hurt, because it is a friend.
 */
public class RoboBuddyEntity extends PathfinderMob {

	/** How far a hit sends it, in blocks. */
	public static final int KNOCKBACK_BLOCKS = 255;

	public static final String PLEA = "Don't delete me! I'm Elduin's friend!";

	public static final String GREETING = "Hmm... you might be Elduin.";

	private static final double HEARING_RANGE = 48.0;

	public RoboBuddyEntity(EntityType<? extends RoboBuddyEntity> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return PathfinderMob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.25);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		Entity attacker = source.getEntity();
		if (attacker == null) {
			// The void, /kill and so on still work. Only a hit from someone is a "delete".
			return super.hurtServer(level, source, amount);
		}
		this.beg(level);
		this.flyAwayFrom(level, attacker);
		return false;
	}

	/** Only runs the first time it appears, so it greets you when you spawn it, not every time the world loads. */
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
		SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, groupData);
		if (reason == EntitySpawnReason.SPAWN_ITEM_USE) {
			this.say(level.getLevel(), GREETING);
		}
		return result;
	}

	/** Says a line in chat to everyone close enough to hear it. */
	private void say(ServerLevel level, String text) {
		Component line = Component.literal("<Robo Buddy> ").withStyle(ChatFormatting.AQUA)
				.append(Component.literal(text).withStyle(ChatFormatting.WHITE));
		for (ServerPlayer player : level.players()) {
			if (player.distanceToSqr(this) <= HEARING_RANGE * HEARING_RANGE) {
				player.sendSystemMessage(line);
			}
		}
	}

	private void beg(ServerLevel level) {
		this.say(level, PLEA);
		level.playSound(null, this.blockPosition(), SoundEvents.IRON_GOLEM_HURT, SoundSource.NEUTRAL, 1.0F, 1.6F);
	}

	/** Sends the robot 255 blocks straight away from whoever hit it, and sets it down on the ground. */
	private void flyAwayFrom(ServerLevel level, Entity attacker) {
		Vec3 away = new Vec3(this.getX() - attacker.getX(), 0.0, this.getZ() - attacker.getZ());
		if (away.lengthSqr() < 1.0E-4) {
			double angle = this.random.nextDouble() * Math.PI * 2.0;
			away = new Vec3(Math.cos(angle), 0.0, Math.sin(angle));
		}
		away = away.normalize();

		this.puff(level);

		// Stay inside the world border. If 255 blocks is too far, go as far as is allowed.
		double x = this.getX();
		double z = this.getZ();
		for (int blocks = KNOCKBACK_BLOCKS; blocks > 0; blocks -= 15) {
			double tx = this.getX() + away.x * blocks;
			double tz = this.getZ() + away.z * blocks;
			if (level.getWorldBorder().isWithinBounds(tx, tz)) {
				x = tx;
				z = tz;
				break;
			}
		}

		// Asking for the height loads the chunk, so the robot never lands in empty space.
		int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) Math.floor(x), (int) Math.floor(z));
		this.getNavigation().stop();
		this.teleportTo(level, x, ground, z, Set.of(), this.getYRot(), this.getXRot(), true);
		this.fallDistance = 0.0;

		this.puff(level);
	}

	private void puff(ServerLevel level) {
		level.sendParticles(ParticleTypes.POOF, this.getX(), this.getY() + 0.6, this.getZ(), 20, 0.3, 0.5, 0.3, 0.05);
	}

	@Override
	public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.IRON_GOLEM_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.IRON_GOLEM_DEATH;
	}
}
