package com.bokmcdok.wheat.tag;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class ModTag {
    private Set<ResourceLocation> mEntries;

    /**
     * Construction
     * @param entries The list of resource locations related to this tag.
     */
    public ModTag(Set<ResourceLocation> entries) {
        mEntries = ImmutableSet.copyOf(entries);
    }

    /**
     * Get the list of entries.
     * @return The list of entries.
     */
    public Set<ResourceLocation> getEntries() {
        return mEntries;
    }

    /**
     * Test if the tag contains the specified entry.
     * @param location The entry to search for.
     * @return TRUE if this tag contains the entry.
     */
    public boolean contains(ResourceLocation location) {
        return mEntries.contains(location);
    }
}
