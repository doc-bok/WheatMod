package com.bokmcdok.wheat.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.FallbackResourceManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;

import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class ModResourceManager implements IResourceManager {
    private final Map<String, FallbackResourceManager> mNamespaceResourceManagers = Maps.newHashMap();
    private final Set<String> mResourceNamespaces = Sets.newLinkedHashSet();
    private final ResourcePackType mType;

    /**
     * Create a new ModResourceManager
     * @param side Either client-side or server-side.
     * @param modId The ID of the mod to relate this resource manager to.
     */
    public ModResourceManager(ResourcePackType side, String modId) {
        mType = side;

        ModFileInfo modFileInfo = ModList.get().getModFileById(modId);
        ModFileResourcePack resourcePack = new ModFileResourcePack(modFileInfo.getFile());
        addResourcePack(resourcePack);
    }

    /**
     * Get a list of resource namespaces.
     * @return A set of namespaces.
     */
    @Override
    public Set<String> getResourceNamespaces() {
        return mResourceNamespaces;
    }

    /**
     * Get a resource from a location.
     * @param resourceLocation The location of the resource.
     * @return The resource at the location.
     * @throws IOException Thrown if file not found.
     */
    @Override
    public IResource getResource(ResourceLocation resourceLocation) throws IOException {
        IResourceManager resourceManager = mNamespaceResourceManagers.get(resourceLocation.getNamespace());
        if (resourceManager != null) {
            return resourceManager.getResource(resourceLocation);
        } else {
            throw new FileNotFoundException((resourceLocation.toString()));
        }
    }

    /**
     * Check there is a resource at a location.
     * @param resourceLocation The location to check.
     * @return True if there is a resource at the specified location.
     */
    @Override
    public boolean hasResource(ResourceLocation resourceLocation) {
        IResourceManager resourceManager = mNamespaceResourceManagers.get(resourceLocation.getNamespace());
        return resourceManager != null && resourceManager.hasResource(resourceLocation);
    }

    /**
     * Get all the resources at the specified location.
     * @param resourceLocation The location to get the resources from.
     * @return A list of resources.
     * @throws IOException Thrown if location not found.
     */
    @Override
    public List<IResource> getAllResources(ResourceLocation resourceLocation) throws IOException {
        IResourceManager resourceManager = mNamespaceResourceManagers.get(resourceLocation.getNamespace());
        if (resourceManager != null) {
            return resourceManager.getAllResources(resourceLocation);
        } else {
            throw new FileNotFoundException((resourceLocation.toString()));
        }
    }

    /**
     * Get all the resource locations at a specific path.
     * @param path The path to get the locations from.
     * @param filter A filter to only obtain specific resources (e.g. JSON files).s
     * @return A list of resource locations at the path.
     */
    @Override
    public Collection<ResourceLocation> getAllResourceLocations(String path, Predicate<String> filter) {
        Set<ResourceLocation> set = Sets.newHashSet();

        for(FallbackResourceManager fallbackresourcemanager : mNamespaceResourceManagers.values()) {
            set.addAll(fallbackresourcemanager.getAllResourceLocations(path, filter));
        }

        List<ResourceLocation> list = Lists.newArrayList(set);
        Collections.sort(list);
        return list;
    }

    /**
     * Add a resource pack to the resource manager.
     * @param resourcePack The resource pack to add.
     */
    @Override
    public void addResourcePack(IResourcePack resourcePack) {
        for(String s : resourcePack.getResourceNamespaces(mType)) {
            mResourceNamespaces.add(s);
            FallbackResourceManager fallbackResourceManager = mNamespaceResourceManagers.get(s);
            if (fallbackResourceManager == null) {
                fallbackResourceManager = new FallbackResourceManager(mType);
                mNamespaceResourceManagers.put(s, fallbackResourceManager);
            }

            fallbackResourceManager.addResourcePack(resourcePack);
        }
    }
}
