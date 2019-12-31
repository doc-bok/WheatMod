package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.entity.tile.ModInventoryTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ModBlock extends Block implements IModBlock {
    private ModBlockImpl mImpl;

    /**
     * Construction
     * @param properties The properties for the block.
     */
    public ModBlock(ModBlockImpl.ModBlockProperties properties) {
        super(properties.asBlockProperties());
        mImpl = new ModBlockImpl(properties);
    }

    /**
     * Get the block color.
     * @return The color of the block.
     */
    @Override
    public IBlockColor getColor() {
        return mImpl.getColor();
    }

    /**
     * Get the flammability of the block.
     * @return How flammable the block is.
     */
    @Override
    public int getFlammability() {
        return mImpl.getFlammability();
    }

    /**
     * Get how much fire is encouraged by the block.
     * @return The fire encouragement.
     */
    @Override
    public int getFireEncouragement() {
        return mImpl.getFlammability();
    }

    /**
     * Get the shape for face-culling.
     * @param state State of the block
     * @param world The world the block is in
     * @param position The position of the block
     * @param selectionContext The context the block is being viewed in
     * @return The shape of the block
     */
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext selectionContext) {
        VoxelShape shape = mImpl.getShape();
        if (shape != null) {
            return shape;
        }

        return super.getShape(state, world, position, selectionContext);
    }

    /**
     * Get the shape for collision detection.
     * @param state State of the block
     * @param world The world the block is in
     * @param position The position of the block
     * @param selectionContext The context the block is being viewed in
     * @return The shape of the block
     */
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext selectionContext) {
        VoxelShape shape = mImpl.getCollisionShape();
        if (shape != null) {
            return shape;
        }

        return super.getCollisionShape(state, world, position, selectionContext);
    }

    /**
     * Drop the inventory
     * @param state The current block state.
     * @param world The current world.
     * @param position The position of the block.
     * @param newState The new state.
     * @param isMoving Whether or not the block is moving.
     */
    @Override
    public void onReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean isMoving) {
        if (state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = world.getTileEntity(position);
            if (tileEntity instanceof ModInventoryTileEntity) {
                ModInventoryTileEntity inventory = (ModInventoryTileEntity)tileEntity;
                for (int i = 0; i < inventory.getNumSlots(); i++) {
                    spawnAsEntity(world, position, inventory.getItemStack(i));
                }

            }

            world.removeTileEntity(position);
        }
    }

    /**
     * We have a tile entity for the mob drops.
     * @return Always TRUE.
     */
    @Override
    public boolean hasTileEntity() {
        return mImpl.hasTileEntity() || super.hasTileEntity();
    }

    /**
     * Create a new tile entity for a block.
     * @param world The current world.
     * @return The new tile entity.
     */
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        TileEntity entity = mImpl.createTileEntity(state, world);
        return entity == null ? super.createTileEntity(state, world) : entity;
    }
}
