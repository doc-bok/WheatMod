package com.bokmcdok.wheat.tag;

import net.minecraft.util.ResourceLocation;

public class ModTagRegistrar {
    private final ModTagDataManager mItemTagManager;
    private final ModTagDataManager mBlockTagManager;

    /**
     * Construction
     */
    public ModTagRegistrar() {
        mItemTagManager = new ModTagDataManager();
        mItemTagManager.loadDataEntries("tags/items");

        mBlockTagManager = new ModTagDataManager();
        mBlockTagManager.loadDataEntries("tags/blocks");
    }

    /**
     * Get an item tag.
     * @param namespace The namespace that the resource belongs to.
     * @param path The path to the resource location.
     * @return The item tag.
     */
    public ModTag getItemTag(String namespace, String path) {
        return mItemTagManager.getEntry(new ResourceLocation(namespace, path));
    }

    /**
     * Get an item tag.
     * @param resourceName The name of the resource.
     * @return The item tag.
     */
    public ModTag getItemTag(String resourceName) {
        return mItemTagManager.getEntry(new ResourceLocation(resourceName));
    }

    /**
     * Get an item tag.
     * @param resourceLocation The location of the resource.
     * @return The item tag.
     */
    public ModTag getItemTag(ResourceLocation resourceLocation) {
        return mItemTagManager.getEntry(resourceLocation);
    }

    /**
     * Get an block tag.
     * @param namespace The namespace that the resource belongs to.
     * @param path The path to the resource location.
     * @return The block tag.
     */
    public ModTag getBlockTag(String namespace, String path) {
        return mBlockTagManager.getEntry(new ResourceLocation(namespace, path));
    }

    /**
     * Get an block tag.
     * @param resourceName The name of the resource.
     * @return The block tag.
     */
    public ModTag getBlockTag(String resourceName) {
        return mBlockTagManager.getEntry(new ResourceLocation(resourceName));
    }

    /**
     * Get an block tag.
     * @param resourceLocation The location of the resource.
     * @return The block tag.
     */
    public ModTag getBlockTag(ResourceLocation resourceLocation) {
        return mBlockTagManager.getEntry(resourceLocation);
    }
}
