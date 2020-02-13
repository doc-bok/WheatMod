package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.block.beehive.ModBeehiveBlockEventHandler;
import com.bokmcdok.wheat.tag.ModTagDataManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ModBlockEventHandler {
    private static final VoxelShape CAMPFIRE_COLLISION_BOX = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private final ModBeehiveBlockEventHandler mBeehiveBlockEventHandler;

    /**
     * Construction
     * @param blockTagDataManager The data manager for block tags.
     */
    public ModBlockEventHandler(ModTagDataManager blockTagDataManager) {
        mBeehiveBlockEventHandler = new ModBeehiveBlockEventHandler(blockTagDataManager);
    }

    /**
     * Forward the event to the correct block handler.
     * @param event The event data.
     */
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos blockPosition = event.getPos();
        BlockState state = world.getBlockState(blockPosition);
        Block block = state.getBlock();

        if (block == Blocks.field_226905_ma_ ||
            block == Blocks.field_226906_mb_) {
            mBeehiveBlockEventHandler.onRightClickBlock(event, world, blockPosition, state, block);
        }
    }
}
