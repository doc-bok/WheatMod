package com.bokmcdok.wheat.Block;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class FlourMillBlock extends Block {
    public static final VoxelShape GRINDER_SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 9.0D, 14.0D);
    public static final VoxelShape STICK_SHAPE = Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    public static final VoxelShape SHAPE = VoxelShapes.or(GRINDER_SHAPE, STICK_SHAPE);

    public FlourMillBlock(String registryName) {
        super(Block.Properties.create(Material.ANVIL, MaterialColor.IRON)
                .hardnessAndResistance(2.0f, 6.0f)
                .sound(SoundType.STONE));
        setRegistryName(WheatMod.MOD_ID, registryName);
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
