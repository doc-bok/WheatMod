package com.bokmcdok.wheat.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;

public interface IModBlock {
    default Block asBlock() {
        return(Block)this;
    }

    Item asItem();

    IBlockColor getColor();

    String getRenderType();

    int getFlammability();
    int getFireEncouragement();

    ModBlockImpl getImpl();

    default ModCropProperties getCropProperties() {
        return null;
    }
}
