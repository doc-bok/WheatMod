package com.bokmcdok.wheat.entity.creature.villager.food;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ModVillagerFoodBuilder {
    public final Map<ResourceLocation, Integer> mFoodHungerValues;

    /**
     * Construction.
     */
    public ModVillagerFoodBuilder() {
        mFoodHungerValues = Maps.newHashMap();
    }

    /**
     * Add a new food item.
     * @param registryKey The registry key of the item.
     * @param hunger The hunger restored by the item.
     */
    public void addFoodItem(ResourceLocation registryKey, int hunger) {
        mFoodHungerValues.put(registryKey, hunger);
    }

    /**
     * Build the villager food class.
     * @return A new villager food instance.
     */
    public ModVillagerFood build() {
        return new ModVillagerFood(mFoodHungerValues);
    }
}
