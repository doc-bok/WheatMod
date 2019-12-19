package com.bokmcdok.wheat.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraftforge.common.ToolType;

public class ModBlockImpl {
    private IBlockColor mColor;
    private int mFireEncouragement;
    private int mFireFlammability;
    private ModCropProperties mCropProperties;

    public ModBlockImpl(ModBlockProperties properties) {
        mColor = properties.mColor;
        mFireEncouragement = properties.mFireEncouragement;
        mFireFlammability = properties.mFireFlammability;
        mCropProperties = properties.mCropProperties;
    }

    /**
     * Get the item's color
     * @return The color of the item.
     */
    public IBlockColor getColor() { return  mColor; }

    public int getFlammability() {
        return mFireFlammability;
    }

    public int getFireEncouragement() {
        return mFireEncouragement;
    }

    public ModCropProperties getCropProperties() { return  mCropProperties; }

    public static class ModBlockProperties {
        private Block.Properties mBlockProperties;

        private IBlockColor mColor = null;
        private int mFireEncouragement = 0;
        private int mFireFlammability = 0;

        private ModCropProperties mCropProperties;

        public ModBlockProperties(Material material) {
            mBlockProperties = Block.Properties.create(material);
        }

        public ModBlockProperties(Material material, MaterialColor color) {
            mBlockProperties = Block.Properties.create(material, color);
        }

        public ModBlockProperties(Block block) {
            mBlockProperties = Block.Properties.from(block);
        }

        public void doesNotBlockMovement() {
            mBlockProperties.doesNotBlockMovement();
        }

        public void slipperiness(float value) {
            mBlockProperties.slipperiness(value);
        }

        public void sound(SoundType type) {
            mBlockProperties.sound(type);
        }

        public void lightValue(int value) {
            mBlockProperties.lightValue(value);
        }

        public void hardnessAndResistance(float hardness, float resistance) {
            mBlockProperties.hardnessAndResistance(hardness, resistance);
        }

        public void tickRandomly() {
            mBlockProperties.tickRandomly();
        }

        public void variableOpacity() {
            mBlockProperties.variableOpacity();
        }

        public void harvestLevel(int level) {
            mBlockProperties.harvestLevel(level);
        }

        public void harvestTool(ToolType type) {
            mBlockProperties.harvestTool(type);
        }

        public void noDrops() {
            mBlockProperties.noDrops();
        }

        public void lootFrom(Block block) {
            mBlockProperties.lootFrom(block);
        }

        public void color(IBlockColor color) {
            mColor = color;
        }

        public void flammable(int encouragement, int flammability) {
            mFireEncouragement = encouragement;
            mFireFlammability = flammability;
        }

        public void crop(ModCropProperties properties) {
            mCropProperties = properties;
        }

        public Block.Properties asBlockProperties() {
            return mBlockProperties;
        }
    }
}
