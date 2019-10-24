package com.bokmcdok.wheat.Block;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class ModCampfireBlock extends CampfireBlock {
    public ModCampfireBlock(String registryName) {
        super(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD).lightValue(15).tickRandomly());
        setRegistryName(WheatMod.MOD_ID, registryName);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IWorld iworld = context.getWorld();
        BlockPos blockpos = context.getPos();
        boolean flag = iworld.getFluidState(blockpos).getFluid() == Fluids.WATER;
        return this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(flag)).with(SIGNAL_FIRE, Boolean.valueOf(this.isHayBlock(iworld.getBlockState(blockpos.down())))).with(LIT, Boolean.valueOf(!flag)).with(FACING, context.getPlacementHorizontalFacing());
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return facing == Direction.DOWN ? stateIn.with(SIGNAL_FIRE, Boolean.valueOf(this.isHayBlock(facingState))) : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    /**
     * Returns true if the block of the passed blockstate is a Hay block, otherwise false.
     */
    private boolean isHayBlock(BlockState stateIn) {
        return stateIn.getBlock() == Blocks.HAY_BLOCK ||
                stateIn.getBlock() == ModBlocks.wild_einkorn_bale ||
                stateIn.getBlock() == ModBlocks.common_straw_bale ||
                stateIn.getBlock() == ModBlocks.durum_straw_bale ||
                stateIn.getBlock() == ModBlocks.einkorn_straw_bale ||
                stateIn.getBlock() == ModBlocks.emmer_straw_bale||
                stateIn.getBlock() == ModBlocks.spelt_straw_bale;
    }
}
