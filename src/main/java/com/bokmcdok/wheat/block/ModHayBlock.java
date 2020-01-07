package com.bokmcdok.wheat.block;

import net.minecraft.block.HayBlock;
import net.minecraft.client.renderer.color.IBlockColor;

public class ModHayBlock extends HayBlock implements IModBlock {
    private ModBlockImpl mImpl;

    public ModHayBlock(ModBlockImpl.ModBlockProperties properties) {
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
     * Get the implementation.
     * @return The implementation object.
     */
    @Override
    public ModBlockImpl getImpl() {
        return mImpl;
    }
}
