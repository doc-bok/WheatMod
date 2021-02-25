package com.bokmcdok.wheat.entity.creature.animal.butterfly;

import com.bokmcdok.wheat.ai.goals.ModAttractToLightGoal;
import com.bokmcdok.wheat.ai.goals.ModPollinateGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class ModMothEntity extends ModButterflyEntity {

    /**
     * Create a new entity.
     * @param type The type of the entity.
     * @param world The current world.
     */
    public ModMothEntity(EntityType<? extends ModMothEntity> type, World world) {
        super(type, world);
    }

    /**
     * Check if the entity can spawn.
     * @param entity The entity to spawn.
     * @param world The current world.
     * @param reason The reason the entity is spawning.
     * @param position The position to spawn in.
     * @param random The random number generator to use.
     * @return TRUE if the entity can spawn.
     */
    public static boolean canMothSpawn(EntityType<ModMothEntity> entity, IWorld world, SpawnReason reason, BlockPos position, Random random) {
        int light = world.getLight(position);
        if (random.nextBoolean()) {
            return false;
        }

        return !world.getWorld().isDaytime() ||
                light < random.nextInt(8) && MobEntity.canSpawnOn(entity, world, reason, position, random);
    }

    /**
     * Get wether or not this entity is a butterfly.
     * @return TRUE if this is a butterfly, false if it is a moth.
     */
    public boolean getIsButterfly() {
        return false;
    }

    /**
     * Get the variety of butterfly.
     * @return The index of the texture to use for the butterfly.
     */
    public int getVariety() {
        return 0;
    }

    /**
     * On spawn check for day/night to determine if this is a moth or a butterfly.
     * @param world The current world.
     * @param difficulty The difficulty setting.
     * @param reason The reason for the spawn.
     * @param data The spawn data.
     * @param nbt NBT tags.
     * @return The updated spawn data.
     */
    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        return super.onInitialSpawn(world, difficulty, reason, data, nbt);
    }

    /**
     * Register the pollinate goal.
     */
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(4, new ModAttractToLightGoal(this, getFlyingSpeed(), 16, 8));
        goalSelector.addGoal(5, new ModPollinateGoal(this, getFlyingSpeed(), 16, 8));
    }
}
