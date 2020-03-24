package com.bokmcdok.wheat.supplier;

import com.bokmcdok.wheat.tag.ModTag;
import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Set;
import java.util.function.Supplier;

public class ModRegistrySetSupplier<T extends IForgeRegistryEntry<T>> implements Supplier<Set<T>> {
    private final Set<ResourceLocation> mResourceLocations;
    private final Class<T> mClass;

    /**
     * Construction
     * @param cls An instance of the class that this supplier returns.
     * @param tag A tag containing the resource locations of all the entries.
     */
    public ModRegistrySetSupplier(Class<T> cls, ModTag tag) {
        this(cls, tag.getEntries());
    }

    /**
     * Construction
     * @param cls An instance of the class that this supplier returns.
     * @param resourceLocations A set of all the resource location.
     */
    public ModRegistrySetSupplier(Class<T> cls, Set<ResourceLocation> resourceLocations) {
        mClass = cls;
        mResourceLocations = resourceLocations;
    }

    /**
     * Get the values.
     * @return A set of objects pulled from the registry.
     */
    @Override
    public Set<T> get() {
        IForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(mClass);
        Set<T> result = Sets.newHashSet();
        for (ResourceLocation location : mResourceLocations) {
            T entry = registry.getValue(location);
            if (entry != null) {
                result.add(entry);
            }
        }

        return result;
    }
}
