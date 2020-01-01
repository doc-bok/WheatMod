package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.entity.tile.ModTrapTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ModTrapBlock extends ModBlock {

    /**
     * Construction
     * @param properties The properties for the block.
     */
    public ModTrapBlock(ModBlockImpl.ModBlockProperties properties) {
        super(properties);
    }

    /**
     * Create a new tile entity for a block.
     * @param world The current world.
     * @return The new tile entity.
     */
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (mImpl.hasTileEntity()) {
            return  new ModTrapTileEntity(mImpl.getTargets(), mImpl.getInventorySize());
        }

        return super.createTileEntity(state, world);
    }
}
