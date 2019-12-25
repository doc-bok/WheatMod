package com.bokmcdok.wheat.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

public class ModSpawnEggItem extends SpawnEggItem implements IModItem {
    private final ModItemImpl mImpl;
    private final ResourceLocation mEntityLocation;

    /**
     * Construction
     * @param entity The entity to spawn
     * @param primaryColor The primary color of the egg
     * @param secondaryColor The secondary color of the egg
     * @param properties The item's properties
     */
    public ModSpawnEggItem(ResourceLocation entity, int primaryColor, int secondaryColor, ModItemImpl.ModItemProperties properties) {
        super(null, primaryColor, secondaryColor, properties);
        mImpl = new ModItemImpl(properties);
        mEntityLocation = entity;
    }

    /**
     * Get the item's color
     * @return The color of the item.
     */
    public IItemColor getColor() { return  mImpl.getColor(); }

    /**
     * Get the chance an item will compost in the harvester.
     * @return A probability between 0 and 1
     */
    public float getCompostChance() { return mImpl.getCompostChance(); }

    public EntityType<?> getType(@Nullable CompoundNBT p_208076_1_) {
        return Registry.ENTITY_TYPE.getOrDefault(mEntityLocation);
    }
}
