package com.bokmcdok.wheat.supplier;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.tag.ModTag;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

/**
 * Mod Tag Supplier
 *  To be used in LazyValues so values can be set on use.
 */
public class ModTagSupplier implements Supplier<ModTag> {
    public enum TagType {
        BLOCK,
        ITEM
    }

    private final TagType mType;
    private final ResourceLocation mRegistryName;

    /**
     * Construction
     * @param type The type of tag we want to use.
     * @param registryName The registry name of the tag.
     */
    public ModTagSupplier(TagType type, String registryName) {
        this(type, new ResourceLocation(registryName));
    }

    /**
     * Construction
     * @param type The type of tag we want to use.
     * @param registryName The registry name of the tag.
     */
    public ModTagSupplier(TagType type, ResourceLocation registryName) {
        mType = type;
        mRegistryName = registryName;
    }

    /**
     * Loads the tag from the tag manager.
     * @return The tag linked to the registry name.
     */
    @Override
    public ModTag get() {
        if (mType == TagType.BLOCK) {
            return WheatMod.TAG_REGISTRAR.getBlockTag(mRegistryName);
        }
        else
        {
            return WheatMod.TAG_REGISTRAR.getItemTag(mRegistryName);
        }
    }
}
