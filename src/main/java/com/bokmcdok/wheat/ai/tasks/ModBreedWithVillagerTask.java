package com.bokmcdok.wheat.ai.tasks;

import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFood;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

public class ModBreedWithVillagerTask extends Task<VillagerEntity> {
    private final ModVillagerFood mVillagerFood;
    private final int mTargetDistance;
    private final float mSpeed;
    private final int mMaxDistanceSquared;
    private final MemoryModuleType<VillagerEntity> mMemoryModuleType;

    /**
     * Construction
     * @param villagerFood Provides food functions for villagers that allow use of modded items.
     * @param maxDistance The maximum distance to find a mate.
     * @param memoryModuleType The memory module to get the breed target from.
     * @param speed The speed to move at.
     * @param targetDistance The target distance at which breeding can occur.
     */
    public ModBreedWithVillagerTask(ModVillagerFood villagerFood, int maxDistance, MemoryModuleType<VillagerEntity> memoryModuleType, float speed, int targetDistance) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT, memoryModuleType, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleStatus.VALUE_PRESENT));
        mVillagerFood = villagerFood;
        mSpeed = speed;
        mMaxDistanceSquared = maxDistance * maxDistance;
        mTargetDistance = targetDistance;
        mMemoryModuleType = memoryModuleType;
    }

    /**
     * Execute if any nearby entities are also villagers ready to breed.
     * @param world The current world.
     * @param villager The villager.
     * @return TRUE if the villager can breed with a nearby villager.
     */
    @Override
    protected boolean shouldExecute(ServerWorld world, VillagerEntity villager) {
        return mVillagerFood.canBreed(villager) &&
                villager.getBrain().getMemory(MemoryModuleType.VISIBLE_MOBS).get().stream()
                        .anyMatch((x) -> x instanceof VillagerEntity && mVillagerFood.canBreed((VillagerEntity) x));
    }

    /**
     * Find and set a breed target.
     * @param world The current world.
     * @param villager The villager.
     * @param gameTime The current game time.
     */
    @Override
    protected void startExecuting(ServerWorld world, VillagerEntity villager, long gameTime) {
        Brain<?> brain = villager.getBrain();
        brain.getMemory(MemoryModuleType.VISIBLE_MOBS).ifPresent((x) -> {
            x.stream().filter((y) -> EntityType.VILLAGER.equals(y.getType()))
                    .map((y) -> (VillagerEntity)y).filter((y) -> y.getDistanceSq(villager) <= (double) mMaxDistanceSquared)
                    .filter(((y) -> mVillagerFood.canBreed(y))).findFirst()
                    .ifPresent((y) -> {
                        brain.setMemory(mMemoryModuleType, y);
                        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(y));
                        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityPosWrapper(y), mSpeed, mTargetDistance));
                    });
        });
    }
}
