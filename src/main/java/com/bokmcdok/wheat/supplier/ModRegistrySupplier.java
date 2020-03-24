package com.bokmcdok.wheat.supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

import java.util.function.Supplier;

public class ModRegistrySupplier<T extends IForgeRegistryEntry<T>> implements Supplier<T> {
    private final ResourceLocation mResourceLocation;
    private final Class<T> mClass;

    /**
     * Construction
     * @param cls An instance of the class that this supplier returns.
     * @param namespace The namespace that the resorce belongs to.
     * @param path The path to the resource location.
     */
    public ModRegistrySupplier(Class<T> cls, String namespace, String path) {
        this(cls, new ResourceLocation(namespace, path));
    }

    /**
     * Construction
     * @param cls An instance of the class that this supplier returns.
     * @param resourceName The name of the resource.
     */
    public ModRegistrySupplier(Class<T> cls, String resourceName) {
        this(cls, new ResourceLocation(resourceName));
    }

    /**
     * Construction
     * @param cls An instance of the class that this supplier returns.
     * @param resourceLocation The location of the resource.
     */
    public ModRegistrySupplier(Class<T> cls, ResourceLocation resourceLocation) {
        mResourceLocation = resourceLocation;
        mClass = cls;
    }

    /**
     * Get the value.
     * @return The object stored in the registry at the specified location.
     */
    @Override
    public T get() {
        IForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(mClass);
        return registry.getValue(mResourceLocation);
    }
}
