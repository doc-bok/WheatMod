package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.tag.ModTag;
import net.minecraft.block.Block;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.Set;

public class ModMoveToBlockGoal extends MoveToBlockGoal {
    private final Set<ResourceLocation> mBlocksToTarget;

    /**
     * Construction
     * @param owner The owning entity for this goal.
     * @param blocksToTarget A list of blocks to look for.
     * @param speed The speed the owning entity moves.
     * @param radius The radius to search along the x/z axes.
     * @param height The height to search.
     */
    public ModMoveToBlockGoal(CreatureEntity owner, ModTag blocksToTarget, double speed, int radius, int height) {
        super(owner, speed, radius, height);
        mBlocksToTarget = blocksToTarget.getEntries();
    }

    /**
     * Check if this is a block we should move to.
     * @param world The current world.
     * @param position The position to check.
     * @return TRUE if this is a target block.
     */
    @Override
    protected boolean shouldMoveTo(IWorldReader world, BlockPos position) {
        Block block = world.getBlockState(position).getBlock();
        return mBlocksToTarget.contains(block.getRegistryName());
    }
}
