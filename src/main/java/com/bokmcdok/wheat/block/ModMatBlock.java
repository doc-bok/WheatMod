package com.bokmcdok.wheat.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class ModMatBlock extends HorizontalBlock implements IModBlock {

    private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private ModBlockImpl mImpl;

    public ModMatBlock(ModBlockImpl.ModBlockProperties properties) {
        super(properties.asBlockProperties());
        mImpl = new ModBlockImpl(properties);
    }

    public IBlockColor getColor() {
        return mImpl.getColor();
    }

    /**
     * Get the render type's name.
     * @return The name of the render type.
     */
    @Override
    public String getRenderType() {
        return mImpl.getRenderType();
    }

    public int getFlammability() {
        return mImpl.getFlammability();
    }

    public int getFireEncouragement() {
        return mImpl.getFireEncouragement();
    }

    /**
     * Get the shape of the block.
     * @param state The current block state
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param context The selection context
     * @return The shape of the mat (similar to carpet).
     */
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     * @param stateIn The current block state
     * @param facing The direction the block is facing
     * @param facingState The state of the block that is being faced
     * @param worldIn The world the block is in
     * @param currentPos The position of the block
     * @param facingPos The position of the block being faced
     * @return The new block state based on the placement.
     */
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() :
                super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    /**
     * Check if this is a valid position for the block (must be above a solid block)
     * @param state The current block state
     * @param worldIn The world
     * @param pos The position to check
     * @return True if the block can be placed
     */
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return !worldIn.isAirBlock(pos.down());
    }

    /**
     * Get the block state based on the direction the block is facing.
     * @param context The context of the user placement action
     * @return A new block state with the right direction
     */
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        float yaw = context.getPlayer().rotationYaw;
        while (yaw < 0) { yaw += 360; }
        while (yaw > 360) { yaw -= 360; }

        BlockState blockstate = getDefaultState();
        if (yaw >= 315 || yaw < 45) {
            return blockstate.with(HORIZONTAL_FACING, Direction.SOUTH);
        } else if (yaw < 135) {
            return blockstate.with(HORIZONTAL_FACING, Direction.WEST);
        } else if (yaw < 225) {
            return blockstate.with(HORIZONTAL_FACING, Direction.NORTH);
        } else {
            return blockstate.with(HORIZONTAL_FACING, Direction.EAST);
        }
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
     * Add the HORIZONTAL_FACING state to the block.
     * @param builder The state builder for the block.
     */
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }
}
