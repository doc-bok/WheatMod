package com.bokmcdok.wheat.tag;

import com.bokmcdok.wheat.supplier.ModRegistrySetSupplier;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class ModTag {
    private final Set<ResourceLocation> mEntries;
    private final LazyValue<Set<Block>> mBlocks;
    private final LazyValue<Set<Item>> mItems;

    /**
     * Construction
     * @param entries The list of resource locations related to this tag.
     */
    public ModTag(Set<ResourceLocation> entries) {
        mEntries = ImmutableSet.copyOf(entries);
        mBlocks = new LazyValue<>(new ModRegistrySetSupplier<>(Block.class, this));
        mItems = new LazyValue<>(new ModRegistrySetSupplier<>(Item.class, this));
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

    /**
     * Get the set of blocks that match the tag entries.
     * @return The set of blocks.
     */
    public Set<Block> getBlocks() {
        return mBlocks.getValue();
    }

    /**
     * Get the set of items that match the tag entries.
     * @return The set of items.
     */
    public Set<Item> getItems() {
        return mItems.getValue();
    }
}
