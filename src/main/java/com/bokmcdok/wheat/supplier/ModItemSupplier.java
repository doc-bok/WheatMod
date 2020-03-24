package com.bokmcdok.wheat.supplier;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ModItemSupplier extends ModRegistrySupplier<Item> {

    /**
     * Construction
     * @param registryName The registry name of the block.
     */
    public ModItemSupplier(String registryName) {
        super(Item.class, registryName);
    }

    /**
     * Construction
     * @param registryName The registry name of the block.
     */
    public ModItemSupplier(ResourceLocation registryName) {
        super(Item.class, registryName);
    }
}