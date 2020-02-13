package com.bokmcdok.wheat.entity.creature.villager.food;

import com.bokmcdok.wheat.data.ModDataManager;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ModVillagerFoodDataManager extends ModDataManager<ModVillagerFood> {

    /**
     * Deserialize the food
     * @param location The location of the resource.
     * @param json The JSON data to parse.
     * @return A new villager food instance.
     */
    @Override
    protected ModVillagerFood deserialize(ResourceLocation location, JsonObject json) {
        ModVillagerFoodBuilder builder = new ModVillagerFoodBuilder();
        setObjectArray(builder, json, "food", this::deserializeFoodItem);
        return builder.build();
    }

    /**
     * Deserialize an individual food item.
     * @param builder The builder used to create the food.
     * @param json The JSON object.
     */
    private void deserializeFoodItem(ModVillagerFoodBuilder builder, JsonObject json) {
        String registryName = JSONUtils.getString(json, "item");
        int hunger = JSONUtils.getInt(json, "hunger");
        builder.addFoodItem(new ResourceLocation(registryName), hunger);
    }
}
