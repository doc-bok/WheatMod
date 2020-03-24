package com.bokmcdok.wheat.supplier;

import com.bokmcdok.wheat.tag.ModTag;
import com.bokmcdok.wheat.tag.ModTagUtils;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class ModTagSupplier implements Supplier<ModTag> {
    private final ResourceLocation mResourceLocation;

    /**
     * Construction
     * @param namespace The namespace containing the resources.
     * @param path The path to the resource.
     */
    public ModTagSupplier(String namespace, String path) {
        this(new ResourceLocation(namespace, path));
    }

    /**
     * Construction
     * @param resourceName The name of the resource.
     */
    public ModTagSupplier(String resourceName) {
        this(new ResourceLocation(resourceName));
    }

    /**
     * Construction
     * @param resourceLocation The location of the resource.
     */
    public ModTagSupplier(ResourceLocation resourceLocation) {
        mResourceLocation = resourceLocation;
    }

    /**
     * Get the tag from the registry.
     * @return The tag.
     */
    @Override
    public ModTag get() {
        ModTag result = ModTagUtils.getBlockTag(mResourceLocation);
        return result;
    }
}
