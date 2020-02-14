package com.bokmcdok.wheat.ai.tasks;

import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFood;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class ModCreateBabyVillagerTask extends Task<VillagerEntity> {
    private final ModVillagerFood mVillagerFood;
    private final MemoryModuleType<VillagerEntity> mMemoryModuleType;
    private long mGameTimeEnd;

    /**
     * Construction
     * @param villagerFood The villager food handler.
     */
    public ModCreateBabyVillagerTask(ModVillagerFood villagerFood, MemoryModuleType<VillagerEntity> memoryModuleType) {
        super(ImmutableMap.of(memoryModuleType, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleStatus.VALUE_PRESENT), 350, 350);
        mVillagerFood = villagerFood;
        mMemoryModuleType = memoryModuleType;
    }

    /**
     * Execute if there is a breed target nearby.
     * @param world The current world.
     * @param villager The villager to attempt execution.
     * @return TRUE if the task should execute.
     */
    @Override
    protected boolean shouldExecute(ServerWorld world, VillagerEntity villager) {
        return hasBreedTarget(villager);
    }

    /**
     * Stop executing after enough time has elapsed.
     * @param world The current world.
     * @param villager The villager.
     * @param gameTime The current game time.
     * @return TRUE if the task should continue executing.
     */
    @Override
    protected boolean shouldContinueExecuting(ServerWorld world, VillagerEntity villager, long gameTime) {
        return gameTime <= mGameTimeEnd && hasBreedTarget(villager);
    }

    /**
     * Start executing the task - villagers will move towards each other.
     * @param world The current world.
     * @param villager The villager executing the task.
     * @param gameTime The current game time.
     */
    @Override
    protected void startExecuting(ServerWorld world, VillagerEntity villager, long gameTime) {
        VillagerEntity breedTarget = getBreedTarget(villager);
        BrainUtil.lookApproachEachOther(villager, breedTarget);
        world.setEntityState(breedTarget, (byte)18);
        world.setEntityState(villager, (byte)18);
        int duration = 275 + villager.getRNG().nextInt(50);
        mGameTimeEnd = gameTime + duration;
    }

    /**
     * Breed after a certain time if the villagers are close enough.
     * @param world The current world.
     * @param villager The villager executing the task.
     * @param gameTime The current game time.
     */
    @Override
    protected void updateTask(ServerWorld world, VillagerEntity villager, long gameTime) {
        VillagerEntity breedTarget = this.getBreedTarget(villager);
        if (!(villager.getDistanceSq(breedTarget) > 5.0D)) {
            BrainUtil.lookApproachEachOther(villager, breedTarget);
            if (gameTime >= mGameTimeEnd) {
                mVillagerFood.consumeFood(villager);
                mVillagerFood.consumeFood(breedTarget);
                breed(world, villager, breedTarget);
            } else if (villager.getRNG().nextInt(35) == 0) {
                world.setEntityState(breedTarget, (byte)12);
                world.setEntityState(villager, (byte)12);
            }
        }
    }

    /**
     * Remove the breed target.
     * @param world The current world.
     * @param villager The villager executing the task.
     * @param gameTime The current game time.
     */
    @Override
    protected void resetTask(ServerWorld world, VillagerEntity villager, long gameTime) {
        villager.getBrain().removeMemory(mMemoryModuleType);
    }

    /**
     * Check if the villager has a breed target.
     * @param villager The villager.
     * @return TRUE if the villager has a breed target.
     */
    private boolean hasBreedTarget(VillagerEntity villager) {
        Brain<VillagerEntity> brain = villager.getBrain();
        if (!brain.getMemory(mMemoryModuleType).isPresent()) {
            return false;
        } else {
            VillagerEntity breedTarget = getBreedTarget(villager);
            return BrainUtil.isCorrectVisibleType(brain, mMemoryModuleType, EntityType.VILLAGER) &&
                    mVillagerFood.canBreed(villager) && mVillagerFood.canBreed(breedTarget);
        }
    }

    /**
     * Get the villager's breed target.
     * @param villager The villager.
     * @return The other villager to breed with.
     */
    private VillagerEntity getBreedTarget(VillagerEntity villager) {
        return villager.getBrain().getMemory(mMemoryModuleType).get();
    }

    /**
     * Do the hanky panky.
     * @param world The current world.
     * @param villager The villager.
     * @param breedTarget The breed target.
     */
    private void breed(ServerWorld world, VillagerEntity villager, VillagerEntity breedTarget) {
        Optional<BlockPos> homePosition = getHomePosition(world, villager);
        if (!homePosition.isPresent()) {
            world.setEntityState(breedTarget, (byte)13);
            world.setEntityState(villager, (byte)13);
        } else {
            Optional<VillagerEntity> child = createChild(villager, breedTarget);
            if (child.isPresent()) {
                setChildHome(world, child.get(), homePosition.get());
            } else {
                world.getPointOfInterestManager().func_219142_b(homePosition.get());
                DebugPacketSender.func_218801_c(world, homePosition.get());
            }
        }
    }

    /**
     * Get the home position, if any.
     * @param world The current world.
     * @param villager The villager.
     * @return The villager's home position.
     */
    private Optional<BlockPos> getHomePosition(ServerWorld world, VillagerEntity villager) {
        return world.getPointOfInterestManager().func_219157_a(PointOfInterestType.HOME.func_221045_c(),
                (x) -> canFindHome(villager, x),
                new BlockPos(villager), 48);
    }

    /**
     * Check if the villager is close enough to home.
     * @param villager The villager.
     * @param position The villager's position.
     * @return TRUE if the villager can find its home.
     */
    private boolean canFindHome(VillagerEntity villager, BlockPos position) {
        Path path = villager.getNavigator().getPathToPos(position, PointOfInterestType.HOME.func_225478_d());
        return path != null && path.func_224771_h();
    }

    /**
     * Create a child villager.
     * @param villager The villager.
     * @param breedTarget The breed target.
     * @return The new child villager.
     */
    private Optional<VillagerEntity> createChild(VillagerEntity villager, VillagerEntity breedTarget) {
        VillagerEntity child = villager.createChild(breedTarget);
        if (child == null) {
            return Optional.empty();
        } else {
            villager.setGrowingAge(6000);
            breedTarget.setGrowingAge(6000);
            child.setGrowingAge(-24000);
            child.setLocationAndAngles(villager.func_226277_ct_(), villager.func_226278_cu_(), villager.func_226281_cx_(), 0.0F, 0.0F);
            villager.world.addEntity(child);
            villager.world.setEntityState(child, (byte)12);
            return Optional.of(child);
        }
    }

    /**
     * Set the child's home.
     * @param world The current world.
     * @param child The child villager.
     * @param position The child's home position.
     */
    private void setChildHome(ServerWorld world, VillagerEntity child, BlockPos position) {
        GlobalPos globalpos = GlobalPos.of(world.getDimension().getType(), position);
        child.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }
}
