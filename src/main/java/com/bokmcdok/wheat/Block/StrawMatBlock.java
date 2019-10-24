package com.bokmcdok.wheat.Block;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class StrawMatBlock extends HorizontalBlock {
    private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    StrawMatBlock(String registryName) {
        super(Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.5F).sound(SoundType.CROP));
        setDefaultState(stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH));
        setRegistryName(WheatMod.MOD_ID, registryName);
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return !worldIn.isAirBlock(pos.down());
    }

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

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }
}
