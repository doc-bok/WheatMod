package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.entity.tile.ModTrapTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;

public class ModTrapBlock extends ModBlock {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    /**
     * Construction
     * @param properties The properties for the block.
     */
    public ModTrapBlock(ModBlockImpl.ModBlockProperties properties) {
        super(properties);
        setDefaultState(stateContainer.getBaseState().with(ACTIVATED, false));
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

    /**
     * Check if the trap is still armed.
     * @param world The current world.
     * @param position The position to check.
     * @return TRUE if there is an armed trap at this position.
     */
    public boolean getIsTrapArmed(IWorldReader world, BlockPos position) {
        TileEntity entity = world.getTileEntity(position);
        return entity instanceof ModTrapTileEntity && ((ModTrapTileEntity)entity).getIsInventoryEmpty();
    }

    /**
     * Get the implementation.
     * @return The implementation object.
     */
    @Override
    public ModBlockImpl getImpl() {
        return mImpl;
    }

    /**
     * Add the activated property to the block.
     * @param builder The state container builder.
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(ACTIVATED);
    }
}
