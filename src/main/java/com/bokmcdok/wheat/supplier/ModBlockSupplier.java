package com.bokmcdok.wheat.supplier;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class ModBlockSupplier extends ModRegistrySupplier<Block> {

    /**
     * Construction
     * @param registryName The registry name of the block.
     */
    public ModBlockSupplier(String registryName) {
        super(Block.class, registryName);
    }

    /**
     * Construction
     * @param registryName The registry name of the block.
     */
    public ModBlockSupplier(ResourceLocation registryName) {
        super(Block.class, registryName);
    }
}