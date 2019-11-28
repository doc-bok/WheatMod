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

    public ModResourceManager(ResourcePackType side, String modId) {
        mType = side;

        ModFileInfo modFileInfo = ModList.get().getModFileById(modId);
        ModFileResourcePack resourcePack = new ModFileResourcePack(modFileInfo.getFile());
        addResourcePack(resourcePack);
    }

    @Override
    public Set<String> getResourceNamespaces() {
        return mResourceNamespaces;
    }

    @Override
    public IResource getResource(ResourceLocation resourceLocation) throws IOException {
        IResourceManager resourceManager = mNamespaceResourceManagers.get(resourceLocation.getNamespace());
        if (resourceManager != null) {
            return resourceManager.getResource(resourceLocation);
        } else {
            throw new FileNotFoundException((resourceLocation.toString()));
        }
    }

    @Override
    public boolean hasResource(ResourceLocation resourceLocation) {
        IResourceManager resourceManager = mNamespaceResourceManagers.get(resourceLocation.getNamespace());
        return resourceManager != null && resourceManager.hasResource(resourceLocation);
    }

    @Override
    public List<IResource> getAllResources(ResourceLocation resourceLocation) throws IOException {
        IResourceManager resourceManager = mNamespaceResourceManagers.get(resourceLocation.getNamespace());
        if (resourceManager != null) {
            return resourceManager.getAllResources(resourceLocation);
        } else {
            throw new FileNotFoundException((resourceLocation.toString()));
        }
    }

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
