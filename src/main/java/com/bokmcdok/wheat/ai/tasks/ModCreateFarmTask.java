package com.bokmcdok.wheat.ai.tasks;

import com.bokmcdok.wheat.block.ModBlock;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;

public class ModCreateFarmTask extends Task<VillagerEntity> {
    private final List<BlockPos> mFarmableBlocks = Lists.newArrayList();
    @Nullable
    private BlockPos mPosition;
    private long mCooldown;
    private int mDuration;

    /**
     * Construction
     */
    public ModCreateFarmTask() {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
    }

    /**
     * Determine wether or not this task should execute.
     * @param world The current world.
     * @param owner The owner of the task.
     * @return True if the goal should execute.
     */
    @Override
    public boolean shouldExecute(ServerWorld world, VillagerEntity owner) {
        if (!ForgeEventFactory.getMobGriefingEvent(world, owner)) {
                return false;
        } else if (owner.getVillagerData().getProfession() != VillagerProfession.FARMER) {
            return false;
        } else {



            BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable(owner);
            mFarmableBlocks.clear();

            for (int i1 = -1; i1 <= 1; ++i1) {
                for (int k = -1; k <= 1; ++k) {
                    for (int l = -1; l <= 1; ++l) {
                        blockpos$mutableblockpos.setPos(owner.func_226277_ct_() + (double)i1, owner.func_226278_cu_() + (double)k, owner.func_226281_cx_() + (double)l);
                        if (isFarmableBlock(blockpos$mutableblockpos, world)) {
                            mFarmableBlocks.add(new BlockPos(blockpos$mutableblockpos));
                        }
                    }
                }
            }

            mPosition = getRandomFarmableBlock(world);
            return mPosition != null;
        }
    }

    /**
     * Start walking towards the target block.
     * @param world The current world.
     * @param owner The owner of the task.
     * @param gameTime The current game time.
     */
    @Override
    protected void startExecuting(ServerWorld world, VillagerEntity owner, long gameTime) {
        if (gameTime > mCooldown && mPosition != null) {
            owner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosWrapper(mPosition));
            owner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosWrapper(mPosition), 0.5f, 1));
        }
    }

    /**
     * Stop executing the task.
     * @param world The current world.
     * @param owner The owner of the task;
     * @param gameTime The current game time.
     */
    @Override
    protected void resetTask(ServerWorld world, VillagerEntity owner, long gameTime) {
        owner.getBrain().removeMemory(MemoryModuleType.LOOK_TARGET);
        owner.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
        mDuration = 0;
        mCooldown = gameTime + 40l;
    }

    /**
     * Convert a grass block to farmland.
     * @param world The current world.
     * @param owner The owner of the task.
     * @param gameTime The current game time.
     */
    @Override
    protected void updateTask(ServerWorld world, VillagerEntity owner, long gameTime) {
        if (mPosition != null && gameTime > mCooldown) {
            BlockState state = world.getBlockState(mPosition);
            Block block = state.getBlock();
            if (block == Blocks.GRASS_BLOCK) {
                world.setBlockState(mPosition, Blocks.FARMLAND.getDefaultState());
                world.playSound(null, mPosition, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                mFarmableBlocks.remove(mPosition);
                mPosition = getRandomFarmableBlock(world);
                if (mPosition != null) {
                    mCooldown = gameTime + 20l;
                    owner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosWrapper(mPosition), 0.5F, 1));
                    owner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosWrapper(mPosition));
                }
            }
        }

        ++mDuration;
    }


    /**
     * Check if we should continue this task.
     * @param world The current world.
     * @param owner The owner of the task.
     * @param gameTime The current game time.
     * @return TRUE if we should continue executing.
     */
    @Override
    protected boolean shouldContinueExecuting(ServerWorld world, VillagerEntity owner, long gameTime) {
        return mDuration < 200;
    }

    /**
     * Returns a random block that can be farmed, if present.
     * @param world The current world.
     * @return A random block to farm if present.
     */
    @Nullable
    private BlockPos getRandomFarmableBlock(ServerWorld world) {
        return mFarmableBlocks.isEmpty() ? null : mFarmableBlocks.get(world.getRandom().nextInt(mFarmableBlocks.size()));
    }

    /**
     * Checks whether we can create a farm on this block.
     * @param position The position to check.
     * @param world The current world.
     * @return
     */
    private boolean isFarmableBlock(BlockPos position, ServerWorld world) {
        BlockState blockstate = world.getBlockState(position);
        Block block = blockstate.getBlock();
        BlockState up = world.getBlockState(position.up());
        AxisAlignedBB bounds = new AxisAlignedBB(position).grow(4, 1, 4);

        return (block == Blocks.GRASS_BLOCK || block == Blocks.DIRT) && up.isAir(world, position) &&
                ModBlock.isBlockPresent(world, Blocks.WATER, bounds);
    }
}
