package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.entity.tile.ModInventoryTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import java.util.Set;

public class ModBlockImpl {
    private final Set<ResourceLocation> mTargets;
    private final ModCropProperties mCropProperties;
    private final ModNestProperties mNestProperties;
    private final VoxelShape mShape;
    private final VoxelShape mCollisionShape;
    private final IBlockColor mColor;
    private final String mRenderType;
    private final int mInventorySize;
    private final int mFireEncouragement;
    private final int mFireFlammability;

    /**
     * Construction
     * @param properties The block properties.
     */
    public ModBlockImpl(ModBlockProperties properties) {
        mColor = properties.mColor;
        mFireEncouragement = properties.mFireEncouragement;
        mFireFlammability = properties.mFireFlammability;
        mCropProperties = properties.mCropProperties;
        mNestProperties = properties.mNestProperties;
        mShape = properties.mShape;
        mCollisionShape = properties.mCollisionShape;
        mInventorySize = properties.mInventorySize;
        mTargets = properties.mTargets;
        mRenderType = properties.mRenderType;
    }

    /**
     * Get the block's color
     * @return The color of the block.
     */
    public IBlockColor getColor() { return  mColor; }

    /**
     * Get the flammability of the block.
     * @return How flammable the block is.
     */
    public int getFlammability() {
        return mFireFlammability;
    }

    /**
     * Get how much the block encourages fire.
     * @return The fire encouragement.
     */
    public int getFireEncouragement() {
        return mFireEncouragement;
    }

    /**
     * Get the crop properties of the block.
     * @return The crop properties instance, if any.
     */
    public ModCropProperties getCropProperties() {
        return  mCropProperties;
    }

    /**
     * Get the shape of the block for rendering.
     * @return The block's shape.
     */
    public VoxelShape getShape() {
        return  mShape;
    }

    /**
     * Get the shape of the block for collisions.
     * @return The collision shape.
     */
    public VoxelShape getCollisionShape() {
        return  mCollisionShape == null ? mShape : mCollisionShape;
    }

    /**
     * Check if the block has a tile entity.
     * @return TRUE if the block has a tile entity.
     */
    public boolean hasTileEntity() {
        return mInventorySize > 0;
    }

    /**
     * Create the block's tile entity.
     * @param state The current block state.
     * @param world The current world.
     * @return A new tile entity.
     */
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (hasTileEntity()) {
            return new ModInventoryTileEntity(mInventorySize);
        }

        return null;
    }

    /**
     * Get the targets for a trap.
     * @return The list of trap targets.
     */
    public final Set<ResourceLocation> getTargets() {
        return mTargets;
    }

    /**
     * Get the inventory size.
     * @return The number of slots in the block's inventory.
     */
    public int getInventorySize() {
        return mInventorySize;
    }

    /**
     * Get the render type of the block.
     * @return
     */
    public String getRenderType() {
        return mRenderType;
    }

    /**
     * Get the nest properties of the block.
     * @return The nest properties.
     */
    public ModNestProperties getNestProperties() {
        return mNestProperties;
    }

    /**
     * The properties of a modded block.
     */
    public static class ModBlockProperties {
        private Set<ResourceLocation> mTargets;
        private Block.Properties mBlockProperties;
        private ModCropProperties mCropProperties;
        private ModNestProperties mNestProperties;
        private VoxelShape mShape;
        private VoxelShape mCollisionShape;
        private IBlockColor mColor = null;
        private String mRenderType = "solid";
        private int mInventorySize = 0;
        private int mFireEncouragement = 0;
        private int mFireFlammability = 0;

        /**
         * Construction
         * @param material The material for the block.
         */
        public ModBlockProperties(Material material) {
            mBlockProperties = Block.Properties.create(material);
        }

        /**
         * Construction
         * @param material The material for the block.
         * @param mapColor The color the block appears on the map.
         */
        public ModBlockProperties(Material material, MaterialColor mapColor) {
            mBlockProperties = Block.Properties.create(material, mapColor);
        }

        /**
         * Construction
         * @param block The block to copy properties from.
         */
        public ModBlockProperties(Block block) {
            mBlockProperties = Block.Properties.from(block);

            if (block instanceof IModBlock) {
                ModBlockImpl impl = ((IModBlock)block).getImpl();
                mTargets = impl.mTargets;
                mCropProperties = impl.mCropProperties;
                mNestProperties = impl.mNestProperties;
                mShape = impl.mShape;
                mCollisionShape = impl.mCollisionShape;
                mColor = impl.mColor;
                mInventorySize = impl.mInventorySize;
                mFireEncouragement = impl.mFireEncouragement;
                mFireFlammability = impl.mFireEncouragement;
            }
        }

        /**
         * This block has no collision.
         */
        public void doesNotBlockMovement() {
            mBlockProperties.doesNotBlockMovement();
        }

        /**
         * This block is slippery, like ice.
         * @param value The amount of slipperiness.
         */
        public void slipperiness(float value) {
            mBlockProperties.slipperiness(value);
        }

        /**
         * The sound type of this block.
         * @param type The sound type to use.
         */
        public void sound(SoundType type) {
            mBlockProperties.sound(type);
        }

        /**
         * This block emits light.
         * @param value The light level emitted.
         */
        public void lightValue(int value) {
            mBlockProperties.lightValue(value);
        }

        /**
         * This is a tough block.
         * @param hardness How hard the block is.
         * @param resistance How resistant to damage the block is.
         */
        public void hardnessAndResistance(float hardness, float resistance) {
            mBlockProperties.hardnessAndResistance(hardness, resistance);
        }

        /**
         * This block ticks randomly.
         */
        public void tickRandomly() {
            mBlockProperties.tickRandomly();
        }

        /**
         * This block has a variable opacity.
         */
        public void variableOpacity() {
            mBlockProperties.variableOpacity();
        }

        /**
         * This block requires a higher level tool to harvest.
         * @param level The level required to harvest.
         */
        public void harvestLevel(int level) {
            mBlockProperties.harvestLevel(level);
        }

        /**
         * This block requires a specific tool to harvest.
         * @param type The type of tool required.
         */
        public void harvestTool(ToolType type) {
            mBlockProperties.harvestTool(type);
        }

        /**
         * This block has no drops.
         */
        public void noDrops() {
            mBlockProperties.noDrops();
        }

        /**
         * This block has the same loot as another block.
         * @param block The block to take the loot table from.
         */
        public void lootFrom(Block block) {
            mBlockProperties.lootFrom(block);
        }

        /**
         * This block has a specific color.
         * @param color The color tint of the block.
         */
        public void color(IBlockColor color) {
            mColor = color;
        }

        /**
         * This block is inflammable.
         * @param encouragement How much the block encourages fire.
         * @param flammability How easy the block burns.
         */
        public void flammable(int encouragement, int flammability) {
            mFireEncouragement = encouragement;
            mFireFlammability = flammability;
        }

        /**
         * This is a crop block.
         * @param properties The crop-specific properties.
         */
        public void crop(ModCropProperties properties) {
            mCropProperties = properties;
        }

        /**
         * This block has a non-cube shape.
         * @param shape The shape of the block.
         */
        public void setShape(VoxelShape shape) {
            mShape = shape;
        }

        /**
         * This block has a non-cube collision shape.
         * @param shape The collision shape of the block.
         */
        public void setCollisionShape(VoxelShape shape) {
            mCollisionShape = shape;
        }

        /**
         * This block has an inventory.
         * @param numSlots The number of slots in the inventory.
         */
        public void setInventory(int numSlots) {
            mInventorySize = numSlots;
        }

        /**
         * This block targets these specific entities.
         * @param targets The entities to target.
         */
        public void setTargets(Set<ResourceLocation> targets) {
            mTargets = targets;
        }

        /**
         * Convert the properties to vanilla block properties.
         * @return The aggregated vanilla block properties.
         */
        public Block.Properties asBlockProperties() {
            return mBlockProperties;
        }

        public void setRenderType(String renderType) {
            mRenderType = renderType;
        }

        /**
         * Set the nest properties
         * @param properties The properties of the nest.
         */
        public void setNestProperties(ModNestProperties properties) {
            mNestProperties = properties;
        }
    }
}
