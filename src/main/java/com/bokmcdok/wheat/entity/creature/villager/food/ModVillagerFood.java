package com.bokmcdok.wheat.entity.creature.villager.food;

import com.bokmcdok.wheat.data.ModIngredientSupplier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ModVillagerFood {
    private final Map<ResourceLocation, Integer> mFoodHungerValues;
    private final LazyValue<Ingredient> mFoodItems;

    /**
     * Construction
     * @param foodHungerValues The food hunger files loaded into the builder.
     */
    public ModVillagerFood(Map<ResourceLocation, Integer> foodHungerValues) {
        mFoodHungerValues = foodHungerValues;
        ModIngredientSupplier supplier = new ModIngredientSupplier(mFoodHungerValues.keySet().toArray(new ResourceLocation[0]));
        mFoodItems = new LazyValue<>(supplier);
    }

    /**
     * Check if the item stack contains food items.
     * @param stack The item stack.
     * @return TRUE if this is a food item.
     */
    public boolean isFoodItem(ItemStack stack) {
        return mFoodItems.getValue().test(stack);
    }

    /**
     * Get the hunger restored by a food item.
     * @param stack The item stack.
     * @return The hunger gained if the item is a food item.
     */
    public int getHunger(ItemStack stack) {
        if (isFoodItem(stack)) {
            return mFoodHungerValues.get(stack.getItem().getRegistryName());
        }

        return 0;
    }
}
