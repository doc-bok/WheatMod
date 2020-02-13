package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.block.beehive.ModBeehiveBlockEventHandler;
import com.bokmcdok.wheat.tag.ModTagDataManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ModBlockEventHandler {
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
