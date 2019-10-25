package com.bokmcdok.wheat.Block;

import com.bokmcdok.wheat.Container.FlourMillContainer;
import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class FlourMillBlock extends Block {

    /**
     * Construction
     * @param registryName The name of this block in the registry.
     */
    public FlourMillBlock(String registryName) {
        super(Block.Properties.create(Material.ANVIL, MaterialColor.IRON)
                .hardnessAndResistance(2.0f, 6.0f)
                .sound(SoundType.STONE));
        setRegistryName(WheatMod.MOD_ID, registryName);
    }

    /**
     * Called when the player right-clicks on the block. Opens the Flour Mill inventory.
     * @param state State of the block
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param player The player entity
     * @param handIn The hand that is clicking
     * @param hit The result of the ray trace
     * @return TRUE, indicating the right-click action is consumed.
     */
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        player.openContainer(state.getContainer(worldIn, pos));
        //player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return true;
    }

    /**
     * Gets the flour mill container.
     * @param state State of the block
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @return A new instance of a flour mill's container.
     */
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return new SimpleNamedContainerProvider((windowId, playerInventory, playerEntity) -> {
            return new FlourMillContainer(windowId, playerInventory, IWorldPosCallable.of(worldIn, pos));
        }, TRANSLATION_TEXT);
    }

    /**
     * Get the shape for collision detection.
     * @param state State of the block
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param context The context the block is being viewed in
     * @return The shape of the block
     */
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    /**
     * Get the shape for face-culling.
     * @param state State of the block
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param context The context the block is being viewed in
     * @return The shape of the block
     */
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    private static final VoxelShape GRINDER_SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 9.0D, 14.0D);
    private static final VoxelShape STICK_SHAPE = Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape SHAPE = VoxelShapes.or(GRINDER_SHAPE, STICK_SHAPE);

    private static final ITextComponent TRANSLATION_TEXT = new TranslationTextComponent("container.flour_mill");
}
