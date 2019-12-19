package com.bokmcdok.wheat.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.BlockNamedItem;

public class ModBlockNamedItem extends BlockNamedItem implements IModItem {
    private final ModItemImpl mImpl;

    /**
     * Construction
     * @param blockIn The block for the item
     * @param properties The properties of the item
     */
    public ModBlockNamedItem(Block blockIn, ModItemImpl.ModItemProperties properties) {
        super(blockIn, properties);
        mImpl = new ModItemImpl(properties);
    }

    /**
     * Get the item's color
     * @return The color of the item.
     */
    public IItemColor getColor() { return  mImpl.getColor(); }

    /**
     * Get the chance an item will compost in the harvester.
     * @return A probability between 0 and 1
     */
    public float getCompostChance() { return mImpl.getCompostChance(); }
}
